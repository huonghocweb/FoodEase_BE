package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.TableServicesMapper;
import poly.foodease.Model.Entity.TableServices;
import poly.foodease.Model.Request.TableServicesRequest;
import poly.foodease.Model.Response.TableServicesResponse;
import poly.foodease.Repository.TableServiceRepo;
import poly.foodease.Service.TableServicesService;

@Service
public class TableServicesServiceImpl implements TableServicesService {

    @Autowired
    private TableServiceRepo tableServiceRepo;
    @Autowired
    private TableServicesMapper tableServicesMapper;

    @Override
    public List<TableServicesResponse> getAllTableServicesChanh() {
        return tableServiceRepo.findAll().stream()
                .map(tableServicesMapper::convertEnToRes)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TableServicesResponse> getAllTableServices(Integer pageCurrent, Integer pageSize, String sortOrder,
            String sortBy) {
        Sort sort = Sort.by(
                new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable = PageRequest.of(pageCurrent, pageSize, sort);
        Page<TableServices> tableServicesPage = tableServiceRepo.findAll(pageable);
        List<TableServicesResponse> tableServicesResponses = tableServicesPage.getContent().stream()
                .map(tableServicesMapper::convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(tableServicesResponses, pageable, tableServicesPage.getTotalElements());
    }

    @Override
    public Optional<TableServicesResponse> getTableServicesById(Integer tableServicesId) {
        TableServices tableServices = tableServiceRepo.findById(tableServicesId)
                .orElseThrow(() -> new EntityNotFoundException("Not found TableServices By Id"));
        return Optional.of(tableServicesMapper.convertEnToRes(tableServices));
    }

    @Override
    public TableServicesResponse createTableServices(TableServicesRequest tableServicesRequest) {
        TableServices newTableServices = tableServicesMapper.convertReqToEn(tableServicesRequest);
        tableServiceRepo.save(newTableServices);
        return tableServicesMapper.convertEnToRes(newTableServices);
    }

    @Override
    public Optional<TableServicesResponse> updateTableServices(Integer tableServicesId,
            TableServicesRequest tableServicesRequest) {
        return Optional.of(tableServiceRepo.findById(tableServicesId)
                .map(existingTableServices -> {
                    TableServices tableServices = tableServicesMapper.convertReqToEn(tableServicesRequest);
                    tableServices.setServiceId(existingTableServices.getServiceId());
                    TableServices updatedTableServices = tableServiceRepo.save(tableServices);
                    return tableServicesMapper.convertEnToRes(updatedTableServices);
                })
                .orElseThrow(() -> new EntityNotFoundException("not found Coupon")));
    }

    @Override
    public void deleteTableServices(Integer tableServicesId) {
        TableServices tableServices = tableServiceRepo.findById(tableServicesId)
                .orElseThrow(() -> new EntityNotFoundException("TableServices not found"));
        tableServiceRepo.delete(tableServices);
    }
}

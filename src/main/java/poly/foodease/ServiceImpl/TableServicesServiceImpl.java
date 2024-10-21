package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.TableServicesMapper;
import poly.foodease.Model.Entity.TableServices;
import poly.foodease.Model.Response.TableServicesResponse;
import poly.foodease.Repository.TableServiceRepo;
import poly.foodease.Service.TableServicesService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TableServicesServiceImpl implements TableServicesService {

    @Autowired
    private TableServiceRepo tableServiceRepo;
    @Autowired
    private TableServicesMapper tableServicesMapper;
    @Override
    public Page<TableServicesResponse> getAllTableServices(Integer pageCurrent, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort = Sort.by(new Sort.Order(Objects.equals(sortOrder, "asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        Pageable pageable = PageRequest.of(pageCurrent, pageSize, sort);
        Page<TableServices> tableServicesPage =tableServiceRepo.findAll(pageable);
        List<TableServicesResponse> tableServicesResponses = tableServicesPage.getContent().stream()
                .map(tableServicesMapper :: convertEnToRes)
                .collect(Collectors.toList());
        return new PageImpl<>(tableServicesResponses,pageable,tableServicesPage.getTotalElements());
    }

    @Override
    public Optional<TableServicesResponse> getTableServicesById(Integer tableServicesId) {
        TableServices tableServices = tableServiceRepo.findById(tableServicesId)
                .orElseThrow(() -> new EntityNotFoundException("Not found TableServices By Id"));
        return Optional.of(tableServicesMapper.convertEnToRes(tableServices));
    }
}

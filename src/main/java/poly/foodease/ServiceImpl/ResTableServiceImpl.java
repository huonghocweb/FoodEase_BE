package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.ResTableMapper;
import poly.foodease.Model.Entity.ResTable;
import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Response.ResTableResponse;
import poly.foodease.Repository.ResTableRepo;
import poly.foodease.Service.ResTableService;

@Service
public class ResTableServiceImpl implements ResTableService {

    @Autowired
    private ResTableRepo resTableRepo;

    @Autowired
    private ResTableMapper resTableMapper;

    @Override
    public ResTableResponse createResTable(ResTableRequest resTableRequest) {
        ResTable newTable = resTableMapper.convertReqToEn(resTableRequest);
        resTableRepo.save(newTable);
        return resTableMapper.convertEnToRes(newTable);
    }

    @Override
    public ResTableResponse updateResTable(Integer tableId, ResTableRequest resTableRequest) {
        ResTable existingTable = resTableRepo.findById(tableId)
                .orElseThrow(() -> new EntityNotFoundException("Table not found"));
        ResTable updatedTable = resTableMapper.convertReqToEn(resTableRequest);
        updatedTable.setTableId(existingTable.getTableId());
        resTableRepo.save(updatedTable);
        return resTableMapper.convertEnToRes(updatedTable);
    }

    @Override
    public ResTableResponse getResTableById(Integer tableId) {
        ResTable resTable = resTableRepo.findById(tableId)
                .orElseThrow(() -> new EntityNotFoundException("Table not found"));
        return resTableMapper.convertEnToRes(resTable);
    }

    @Override
    public void deleteResTable(Integer tableId) {
        ResTable resTable = resTableRepo.findById(tableId)
                .orElseThrow(() -> new EntityNotFoundException("Table not found"));
        resTableRepo.delete(resTable);
    }

    @Override
    public List<ResTableResponse> getAllResTables() {
        return resTableRepo.findAll().stream()
                .map(resTableMapper::convertEnToRes)
                .collect(Collectors.toList());
    }
}

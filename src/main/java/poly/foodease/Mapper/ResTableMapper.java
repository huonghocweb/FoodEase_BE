package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Entity.ResTable;
import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Response.ResTableResponse;
import poly.foodease.Repository.TableCategoryRepo;

@Mapper(componentModel = "spring")
public abstract class ResTableMapper {

    @Autowired
    private TableCategoryMapper tableCategoryMapper;
    @Autowired
    private TableCategoryRepo tableCategoryRepo;

    public ResTableResponse convertEnToRes(ResTable resTable){
        return ResTableResponse.builder()
                .tableId(resTable.getTableId())
                .tableName(resTable.getTableName())
                .price(resTable.getPrice())
                .deposit(resTable.getDeposit())
                .isAvailable(resTable.getIsAvailable())
                .imageUrl(resTable.getImageUrl())
                .capacity(resTable.getCapacity())
                .tableCategory(tableCategoryMapper.convertEnToRes(resTable.getTableCategory()))
                .build();
    }

    public ResTable convertReqToEn(ResTableRequest resTableRequest){
        return ResTable.builder()
                .tableName(resTableRequest.getTableName())
                .isAvailable(resTableRequest.getIsAvailable())
                .deposit(resTableRequest.getDeposit())
                .price(resTableRequest.getPrice())
                .capacity(resTableRequest.getCapacity())
                .imageUrl(resTableRequest.getImageUrl())
                .tableCategory(tableCategoryRepo.findById(resTableRequest.getTableCategoryId())
                        .orElseThrow(() -> new EntityNotFoundException("not found Table Category")))
                .build();
    }
}

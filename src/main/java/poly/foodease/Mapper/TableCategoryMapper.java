package poly.foodease.Mapper;

import org.mapstruct.Mapper;

import poly.foodease.Model.Entity.TableCategory;
import poly.foodease.Model.Response.TableCategoryResponse;

@Mapper(componentModel = "spring")
public abstract class TableCategoryMapper {
    public TableCategoryResponse convertEnToRes(TableCategory tableCategory) {
        return TableCategoryResponse.builder()
                .tableCategoryId(tableCategory.getTableCategoryId())
                .tableCategoryName(tableCategory.getTableCategoryName())
                .price(tableCategory.getPrice())
                .build();
    }

}

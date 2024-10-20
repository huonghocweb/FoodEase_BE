package poly.foodease.Service;

import java.util.List;

import poly.foodease.Model.Response.TableCategoryResponse;

public interface TableCategoryService {
    TableCategoryResponse getTableCategoryById(Integer tableCategoryId);

    List<TableCategoryResponse> getAllTableCategory();
}

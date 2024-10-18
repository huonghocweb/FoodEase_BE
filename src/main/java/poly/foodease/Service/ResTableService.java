package poly.foodease.Service;

import java.util.List;

import poly.foodease.Model.Entity.ResTable;
import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Response.ResTableResponse;

public interface ResTableService {
    List<ResTable> findAll();
    List<ResTable> getAvailableTables();

    ResTable findTableById(Integer tableId);

    ResTableResponse createTable(ResTableRequest table);
    ResTableResponse updateTable(int tableId, ResTableRequest resTableRequest);
    List<ResTable> findAvailableTablesByCapacity(int guests);

}

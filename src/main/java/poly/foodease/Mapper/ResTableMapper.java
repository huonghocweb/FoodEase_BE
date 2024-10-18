package poly.foodease.Mapper;

import org.springframework.stereotype.Component;

import poly.foodease.Model.Entity.ResTable;
import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Response.ResTableResponse;

@Component
public class ResTableMapper {

    public ResTable toEntity(ResTableRequest request) {
        ResTable table = new ResTable();
        table.setTableName(request.getTableName());
        table.setCapacity(request.getCapacity());
        table.setIsAvailable(request.getIsAvailable());
        return table;
    }

    public ResTableResponse toResponse(ResTable table) {
        ResTableResponse response = new ResTableResponse();
        response.setTableId(table.getTableId());
        response.setTableName(table.getTableName());
        response.setCapacity(table.getCapacity());
        response.setIsAvailable(table.getIsAvailable());
        return response;
    }
}

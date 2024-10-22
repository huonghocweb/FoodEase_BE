package poly.foodease.Service;

import java.util.List;
import java.util.Optional;

import poly.foodease.Model.Request.ResTableRequest;
import poly.foodease.Model.Response.ResTableResponse;

public interface ResTableService {
    ResTableResponse createResTable(ResTableRequest resTableRequest);

    // ResTableResponse updateResTable(Integer tableId, ResTableRequest
    // resTableRequest);

    Optional<ResTableResponse> updateResTableNew(Integer tableId, ResTableRequest resTableRequest);

    // ResTableResponse getResTableById(Integer tableId);

    Optional<ResTableResponse> getResTableByIdNew(Integer tableId);

    void deleteResTable(Integer tableId);

    List<ResTableResponse> getAllResTables();
}

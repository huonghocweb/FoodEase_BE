package poly.foodease.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import poly.foodease.Model.Request.TableServicesRequest;
import poly.foodease.Model.Response.TableServicesResponse;

@Service
public interface TableServicesService {
    Page<TableServicesResponse> getAllTableServices(Integer pageCurrent, Integer pageSize, String sortOrder,
            String sortBy);

    Optional<TableServicesResponse> getTableServicesById(Integer tableServicesId);

    TableServicesResponse createTableServices(TableServicesRequest tableServicesRequest);

    Optional<TableServicesResponse> updateTableServices(Integer tableServiceId, TableServicesRequest tableServicesRequest);

    void deleteTableServices(Integer tableServiceId);

    List<TableServicesResponse> getAllTableServicesChanh();
}

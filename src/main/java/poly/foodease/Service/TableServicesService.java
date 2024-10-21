package poly.foodease.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Response.TableServicesResponse;

import java.util.Optional;

@Service
public interface TableServicesService {
    Page<TableServicesResponse> getAllTableServices(Integer pageCurrent, Integer pageSize, String sortOrder, String sortBy);
    Optional<TableServicesResponse> getTableServicesById(Integer tableServicesId);
}

package poly.foodease.Mapper;

import org.mapstruct.Mapper;

import poly.foodease.Model.Entity.TableServices;
import poly.foodease.Model.Request.TableServicesRequest;
import poly.foodease.Model.Response.TableServicesResponse;

@Mapper(componentModel = "spring")
public abstract class TableServicesMapper {

    public TableServicesResponse convertEnToRes(TableServices tableServices) {
        return TableServicesResponse.builder()
                .serviceId(tableServices.getServiceId())
                .serviceName(tableServices.getServiceName())
                .servicePrice(tableServices.getServicePrice())
                .description(tableServices.getDescription())
                .imageUrl(tableServices.getImageUrl())
                .build();
    }

    public TableServices convertReqToEn(TableServicesRequest tableServicesRequest) {
        return TableServices.builder()
                .serviceName(tableServicesRequest.getServiceName())
                .servicePrice(tableServicesRequest.getServicePrice())
                .description(tableServicesRequest.getDescription())
                .imageUrl(tableServicesRequest.getImageUrl() != null ? tableServicesRequest.getImageUrl() : "")
                .build();
    }
}

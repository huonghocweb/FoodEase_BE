package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.TableServices;
import poly.foodease.Model.Response.TableServicesResponse;

@Mapper(componentModel = "spring")
public abstract class TableServicesMapper {

    public TableServicesResponse convertEnToRes(TableServices tableServices){
        return TableServicesResponse.builder()
                .serviceId(tableServices.getServiceId())
                .serviceName(tableServices.getServiceName())
                .servicePrice(tableServices.getServicePrice())
                .description(tableServices.getDescription())
                .imageUrl(tableServices.getImageUrl())
                .build();
    }
}

package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import poly.foodease.Model.Entity.ShipMethod;
import poly.foodease.Model.Response.ShipMethodResponse;

@Mapper(componentModel = "spring")
public abstract class ShipMethodMapper {

    public ShipMethodResponse convertEnToRes(ShipMethod shipMethod){
        return ShipMethodResponse.builder()
                .shipId(shipMethod.getShipId())
                .shipDuration(shipMethod.getShipDuration())
                .shipFee(shipMethod.getShipFee())
                .shipDescription(shipMethod.getShipDescription())
                .shipName(shipMethod.getShipName())
                .shipStatus(shipMethod.getShipStatus())
                .build();
    }
}

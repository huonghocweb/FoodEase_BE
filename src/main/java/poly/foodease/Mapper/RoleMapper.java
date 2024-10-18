package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import poly.foodease.Model.Entity.Role;
import poly.foodease.Model.Response.RoleResponse;

@Mapper(componentModel = "spring")
public interface RoleMapper {

     default RoleResponse convertToRoleResponse(Role role){
          return  RoleResponse.builder()
                    .id(role.getRoleId())
                    .roleName(role.getRoleName())
                    .build();
    }
}
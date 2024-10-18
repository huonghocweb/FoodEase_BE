package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Request.UserRequest;
import poly.foodease.Model.Response.UserResponse;
import poly.foodease.Repository.RoleRepo;
import poly.foodease.Repository.UserRepo;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    private UserRepo userRepo;

    public UserResponse convertEnToRes(User user){
        return UserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .status(user.getStatus())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .password(user.getPassword())
                .roles(user.getRoles() != null ? user.getRoles().stream()
                        .map(role -> roleMapper.convertToRoleResponse(role))
                        .toList() : null)
                .build();
    }

    public User convertReqToEn(UserRequest userRequest){
        return User.builder()
                .userName(userRequest.getUsername())
                .fullName(userRequest.getFullname())
                .email(userRequest.getEmail())
                .birthday(userRequest.getBirthday())
                .address(userRequest.getAddress())
                .password(userRequest.getPassword())
                .imageUrl(userRequest.getImageUrl())
                .phoneNumber(userRequest.getPhonenumber())
                .status(true)
                .roles(userRequest.getRoleIds() != null ? userRequest.getRoleIds().stream()
                        .map(roleId -> roleRepo.findById(roleId)
                                .orElseThrow(() -> new EntityNotFoundException("not found Entity")))
                        .toList() : null)
                .build();
    }
}

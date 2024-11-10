package poly.foodease.Mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import poly.foodease.Model.Entity.UserPoint;
import poly.foodease.Model.Request.UserPointRequest;
import poly.foodease.Model.Response.UserPointResponse;
import poly.foodease.Repository.UserRepo;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public abstract class UserPointMapper {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRepo  userRepo;

    public UserPointResponse convertEnToRes(UserPoint userPoint){
        return UserPointResponse.builder()
                .totalPoint(userPoint.getTotalPoint())
                .availablePoint(userPoint.getAvailablePoint())
                .usedPoint(userPoint.getUsedPoint())
                .user(userMapper.convertEnToRes(userPoint.getUser()))
                .build();
    }
    public UserPoint convertReqToEn(UserPointRequest userPointRequest){
        return UserPoint.builder()
                .totalPoint(userPointRequest.getTotalPoint())
                .availablePoint(userPointRequest.getAvailablePoint())
                .usedPoint(userPointRequest.getUsedPoint())
                .user(userRepo.findById(userPointRequest.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("Not found User")))
                .createAt(userPointRequest.getCreateAt())
                .updateAt(LocalDateTime.now())
                .build();
    }

}

package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.foodease.Mapper.UserPointMapper;
import poly.foodease.Model.Entity.UserPoint;
import poly.foodease.Model.Request.UserPointRequest;
import poly.foodease.Model.Response.UserPointResponse;
import poly.foodease.Repository.UserPointRepo;
import poly.foodease.Repository.UserRepo;
import poly.foodease.Service.UserPointService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserPointServiceImpl implements UserPointService {

    @Autowired
    UserPointRepo userPointRepo;
    @Autowired
    UserPointMapper userPointMapper;
    @Autowired
    UserRepo  userRepo;

    @Override
    public List<UserPointResponse> getAllUserPoint() {
        return List.of();
    }

    @Override
    public Optional<UserPointResponse> getUserPointByUserName(String userName) {
        UserPoint userPoint = userPointRepo.getUserPointByUserId(userName)
                .orElseGet(() -> {
                    UserPoint newUserPoint = new UserPoint();
                    newUserPoint.setUser(userRepo.findUserByUserName(userName)
                            .orElseThrow(() -> new EntityNotFoundException("Not found User")));
                    newUserPoint.setAvailablePoint(0.0);
                    newUserPoint.setUsedPoint(0.0);
                    newUserPoint.setTotalPoint(0.0);
                    newUserPoint.setCreateAt(LocalDateTime.now());
                    newUserPoint.setUpdateAt(LocalDateTime.now());
                    return userPointRepo.save(newUserPoint);
                });
        return Optional.of(userPointMapper.convertEnToRes(userPoint));
    }

    @Override
    public UserPointResponse createUserPoint(UserPointRequest userPointRequest) {
        UserPoint userPoint = userPointMapper.convertReqToEn(userPointRequest);
        UserPoint userPointCreated = userPointRepo.save(userPoint);
        return userPointMapper.convertEnToRes(userPointCreated);
    }

    @Override
    public Optional<UserPointResponse> updateUserPoint(Integer userPointId, UserPointRequest userPointRequest) {
        return Optional.of(userPointRepo.findById(userPointId).map(userPointExists -> {
            UserPoint userPoint = userPointMapper.convertReqToEn(userPointRequest);
            userPoint.setUserPointId(userPointExists.getUserPointId());
            UserPoint userPointUpdated = userPointRepo.save(userPoint);
            return userPointMapper.convertEnToRes(userPointUpdated);
        })
                .orElseThrow(() -> new EntityNotFoundException("not found UserPoint")));
    }

    @Override
    public UserPointResponse updatePointByUserName(String userName, Double point ,Boolean isUsedPoint) {
        System.out.println(point);
        System.out.println(isUsedPoint);
        UserPoint userPoint = userPointRepo.getUserPointByUserId(userName)
                .orElseThrow(() -> new EntityNotFoundException("not found UserPoint"));
        if(isUsedPoint){
            if(point > userPoint.getAvailablePoint()){
                point = userPoint.getAvailablePoint();
            }
            userPoint.setUsedPoint(userPoint.getUsedPoint() + point);
        }else{
            userPoint.setTotalPoint(userPoint.getTotalPoint() + point);
            userPoint.setAvailablePoint(userPoint.getAvailablePoint() + point);
        }
        userPoint.setAvailablePoint(userPoint.getTotalPoint() - userPoint.getUsedPoint());
        userPoint.setUpdateAt(LocalDateTime.now());
        UserPoint userPointUpdated =userPointRepo.save(userPoint);
        System.out.println(userPointMapper.convertEnToRes(userPointUpdated));
        return userPointMapper.convertEnToRes(userPointUpdated);
    }

    @Override
    public Void deleteUserPoint(Integer userId) {
        return null;
    }
}

package poly.foodease.Service;

import org.springframework.stereotype.Service;
import poly.foodease.Model.Request.UserPointRequest;
import poly.foodease.Model.Response.UserPointResponse;

import java.util.List;
import java.util.Optional;

@Service
public interface UserPointService {
    List<UserPointResponse> getAllUserPoint();
    Optional<UserPointResponse> getUserPointByUserName(String userName);
    UserPointResponse createUserPoint(UserPointRequest userPointRequest);
    Optional<UserPointResponse> updateUserPoint(Integer userId,UserPointRequest userPointRequest);
    UserPointResponse updatePointByUserName(String userName, Double point ,Boolean isUsedPoint);
    Void deleteUserPoint(Integer userId);

}

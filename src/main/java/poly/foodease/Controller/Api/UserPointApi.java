package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Service.UserPointService;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/userPoint")
public class UserPointApi {
    @Autowired
    UserPointService userPointService;

    @GetMapping("/{userName}")
    public ResponseEntity<Object> getUserPointByUserId(@PathVariable("userName") String userName){
        Map<String,Object> result = new HashMap<>();
        System.out.println("getUserPointByUserId");
        try {
            result.put("success", true);
            result.put("message","get UserPoint By UserId");
            result.put("data",userPointService.getUserPointByUserName(userName));
        }catch (Exception e){
            result.put("success", false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }
}

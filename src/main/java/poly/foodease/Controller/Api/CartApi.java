package poly.foodease.Controller.Api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Service.CartService;
import poly.foodease.ServiceImpl.ShipServiceImpl;

import java.util.HashMap;
import java.util.Map;
@CrossOrigin("*")
@RestController
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequestMapping("/api/cart")
public class CartApi {

    @Autowired
    CartService cartService;
    @Autowired
    ShipServiceImpl ghnService;
// ghi log ứng dụng

    @GetMapping("/{cartId}")
    public ResponseEntity<Object> getCartByCartId(@PathVariable("cartId") Integer cartId) throws JsonProcessingException {
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message", "Get Cart By CartId");
            result.put("data",cartService.getCart(cartId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping("/addCartItem/{cartId}/{foodVaId}/{quantity}")
    public ResponseEntity<Object> addItem(@PathVariable("cartId") Integer cartId,
                                          @PathVariable("quantity") Integer quantity,
                                          @PathVariable("foodVaId") Integer foodVaId){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message", "Add Cart Item in Cart");
            result.put("data",cartService.addCart(cartId, foodVaId, quantity));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{cartId}/totalQuantity")
    public ResponseEntity<Object> getToTalQuantityByCartId(@PathVariable("cartId") Integer cartId){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message", "Get Total Quantity By CartId");
            result.put("data",cartService.getTotalQuantity( cartId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{cartId}/totalPrice")
    public ResponseEntity<Object> getToTalPriceByCartId(@PathVariable("cartId") Integer cartId){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message", "Get TotalPrice By CartId ");
            result.put("data",cartService.getTotalPrice(cartId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{cartId}/{foodVaId}")
    public ResponseEntity<Object> getToTalPriceByCartId(
            @PathVariable("cartId") Integer cartId,
            @PathVariable("foodVaId") Integer foodVaId){
        Map<String,Object> result = new HashMap<>();
        try {
            result.put("success",true);
            result.put("message", "Remove CartItem By FoodVaId");
            result.put("data",cartService.removeCartItem(cartId, foodVaId));
        }catch (Exception e){
            result.put("success",false);
            result.put("message",e.getMessage());
            result.put("data",null);
        }
        return ResponseEntity.ok(result);
    }
}

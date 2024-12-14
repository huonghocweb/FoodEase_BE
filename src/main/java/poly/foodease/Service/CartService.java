package poly.foodease.Service;

import org.springframework.stereotype.Service;
import poly.foodease.Model.Response.Cart;

@Service
public interface CartService {
    Cart getCart(Integer cartId);
    Cart addCart(Integer cartId, Integer foodId, Integer quantity);
    Double getTotalPrice(Integer cartId);
    Integer getTotalQuantity (Integer cartId);
    Void removeCart(Integer cartId);
    Cart removeCartItem(Integer cartId, Integer foodVaId);

}

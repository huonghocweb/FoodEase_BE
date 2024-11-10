package poly.foodease.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Response.Cart;
import poly.foodease.Model.Response.CartItem;
import poly.foodease.Service.CartService;
import poly.foodease.Service.FoodVariationsService;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    public static final Map<Integer, Cart> cartStore = new HashMap<>();

    @Autowired
    FoodVariationsService foodVariationsService;

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    public Cart getCart(Integer cartId){
        Cart cart = cartStore.getOrDefault(cartId, new Cart());
        return cart;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    public Cart addCart(Integer cartId, Integer foodVaId, Integer quantity){
        Cart cart = cartStore.getOrDefault(cartId, new Cart());
        CartItem cartItem = cart.getItems().getOrDefault(foodVaId, new CartItem());
       // System.out.println("Cart Item : " + cartItem.getFoodVariation().getFoodVariationId());
        FoodVariations foodVariation = foodVariationsService.findById(foodVaId)
                .orElseThrow(() -> new EntityNotFoundException("not found FoodVariation"));
        System.out.println("Food VariationId  :  " + foodVariation.getFoodVariationId());
        Double priceVa = (foodVariation.getFood().getBasePrice()
                - (double) foodVariation.getFood().getDiscount() / 100 *foodVariation.getFood().getBasePrice())
                + foodVariation.getFoodSize().getPrice();
//                + foodVariation.getFoodVariationToppings().stream()
//                .mapToDouble(topping -> topping.getToppings().getPrice())
//                .sum();
        if(cart.getItems().containsKey(foodVaId)){
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }else{
            cartItem.setQuantity(quantity);
        }
        if (cartItem.getQuantity() == 0){
            this.removeCartItem(cartId, foodVaId);
            return cart;
        }
        cartItem.setFoodVariation(foodVariation);
        cartItem.setPrice(cartItem.getQuantity() * priceVa);
        cart.getItems().put(foodVaId, cartItem);
        cart.setCartId(cartId);
        cartStore.put(cartId, cart);
        return cart;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    @Override
    public Double getTotalPrice(Integer cartId) {
        Double totalPrice = 0.0;
        Cart cart = cartStore.getOrDefault(cartId, new Cart());
        for(Map.Entry<Integer,CartItem> cartItem : cart.getItems().entrySet()){
            totalPrice += cartItem.getValue().getPrice();
        }
        return totalPrice;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    @Override
    public Integer getTotalQuantity(Integer cartId) {
        Cart cart = cartStore.getOrDefault(cartId, new Cart());
        Integer totalQuantity = 0;
        for(Map.Entry<Integer,CartItem> cartItem : cart.getItems().entrySet()){
            totalQuantity += cartItem.getValue().getQuantity();
        }
        return totalQuantity;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    @Override
    public Void removeCart(Integer cartId) {
        cartStore.remove(cartId);
        return null;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'USER')")
    @Override
    public Void removeCartItem(Integer cartId, Integer foodVaId) {
        Cart cart = cartStore.get(cartId);
        CartItem cartItem = cart.getItems().remove(foodVaId);
        return null;
    }
}

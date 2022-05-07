package karbanovich.fit.bstu.foodie.adapters.cart;
import karbanovich.fit.bstu.foodie.models.CartItem;

public interface CartListener {
    void removeClicked(CartItem cartItem, int itemPosition);
}

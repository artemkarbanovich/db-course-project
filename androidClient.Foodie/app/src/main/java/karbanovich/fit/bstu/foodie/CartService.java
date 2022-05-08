package karbanovich.fit.bstu.foodie;
import java.util.ArrayList;
import karbanovich.fit.bstu.foodie.models.CartItem;

public class CartService {
    private static final ArrayList<CartItem> cartItems = new ArrayList<>();

    private CartService() { }

    public static ArrayList<CartItem> getCartItems() { return cartItems; }

    public static void addCartItem(CartItem item) {
        if(cartItems.size() == 0) {
            cartItems.add(item);
            return;
        }

        for(CartItem i : cartItems) {
            if(i.dishId == item.dishId) {
                i.count++;
                return;
            }
        }

        cartItems.add(item);
    }

    public static void removeCartItem(int dishId) {
        if(cartItems.size() == 0)
            return;

        for (CartItem i : cartItems) {
            if(i.dishId == dishId && i.count > 1) {
                i.count--;
                return;
            }
            else if(i.dishId == dishId && i.count == 1) {
                cartItems.remove(i);
                return;
            }
        }
    }

    public static double getTotalPrice() {
        if(cartItems.size() == 0)
            return 0.0;

        double price = 0;

        for(CartItem i : cartItems) {
            price += i.getCount() * i.getPrice();
        }

        return price;
    }

    public static double getTotalPrice(int dishId) {
        if(cartItems.size() == 0)
            return 0.0;

        for (CartItem i : cartItems) {
            if(i.dishId == dishId) {
                return i.getCount() * i.getPrice();
            }
        }

        return 0.0;
    }

    public static void removeAllItems() {
        cartItems.clear();
    }
}

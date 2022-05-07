package karbanovich.fit.bstu.foodie.views;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import karbanovich.fit.bstu.foodie.CartService;
import karbanovich.fit.bstu.foodie.R;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import karbanovich.fit.bstu.foodie.adapters.cart.CartAdapter;
import karbanovich.fit.bstu.foodie.adapters.cart.CartListener;
import karbanovich.fit.bstu.foodie.databinding.ActivityCartBinding;
import karbanovich.fit.bstu.foodie.models.CartItem;

public class CartActivity extends DrawerBaseActivity implements CartListener {

    private TextView noData;
    private RecyclerView cartRecycler;
    private CartAdapter cartAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCartBinding activityCartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(activityCartBinding.getRoot());
        allocateActivityTitle("Cart");

        bindingView();
        setData();
    }

    private void bindingView() {
        noData = findViewById(R.id.cart__no_data);
        cartRecycler = findViewById(R.id.cart__items_list_recycler);
        cartAdapter = new CartAdapter(this, CartService.getCartItems(), this);
    }

    private void setData() {
        if (CartService.getCartItems().size() == 0) {
            cartRecycler.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        } else {
            cartRecycler.setHasFixedSize(true);
            cartRecycler.setLayoutManager(new GridLayoutManager(this, 1));
            cartRecycler.setAdapter(cartAdapter);
        }
    }

    @Override public void removeClicked(CartItem cartItem, int itemPosition) {
        if(cartItem.getCount() > 1) {
            CartService.removeCartItem(cartItem.getDishId());
            cartAdapter.notifyItemChanged(itemPosition);
        }
        else if(cartItem.getCount() == 1) {
            CartService.removeCartItem(cartItem.getDishId());
            cartAdapter.notifyItemRemoved(itemPosition);
            cartAdapter.notifyDataSetChanged();
        }
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}

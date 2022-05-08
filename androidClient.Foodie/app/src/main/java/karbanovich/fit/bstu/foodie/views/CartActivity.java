package karbanovich.fit.bstu.foodie.views;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
    private LinearLayout makeOrderContainer;
    private TextView totalPrice;
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
        makeOrderContainer = findViewById(R.id.cart__make_order_container);
        cartRecycler = findViewById(R.id.cart__items_list_recycler);
        totalPrice = findViewById(R.id.cart__total_price);
        findViewById(R.id.cart__make_order).setOnClickListener(view -> {
            if(CartService.getCartItems().size() != 0)
                startActivity(new Intent(this, MakeOrderActivity.class));
        });

        cartAdapter = new CartAdapter(this, CartService.getCartItems(), this);
    }

    private void setData() {
        if (CartService.getCartItems().size() == 0) {
            noItemsBehavior();
        } else {
            cartRecycler.setHasFixedSize(true);
            cartRecycler.setLayoutManager(new GridLayoutManager(this, 1));
            cartRecycler.setAdapter(cartAdapter);
            totalPrice.setText(String.format("%.2f", CartService.getTotalPrice()) + " BYN");
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

        totalPrice.setText(String.format("%.2f", CartService.getTotalPrice()) + " BYN");

        if(CartService.getCartItems().size() == 0)
            noItemsBehavior();
    }

    private void noItemsBehavior() {
        makeOrderContainer.setVisibility(View.GONE);
        cartRecycler.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}

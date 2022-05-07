package karbanovich.fit.bstu.foodie.adapters.cart;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import karbanovich.fit.bstu.foodie.CartService;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.helpers.SystemHelper;
import karbanovich.fit.bstu.foodie.models.CartItem;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private Context context;
    private ArrayList<CartItem> cartItems;
    private CartListener listener;

    public CartAdapter(Context context, ArrayList<CartItem> cartItems, CartListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @Override public int getItemCount() { return cartItems.size(); }

    @NonNull @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        if(SystemHelper.isNetworkAvailable(context))
            Picasso.get().load(cartItems.get(position).getImageUrl()).into(holder.photo);
        holder.name.setText(cartItems.get(position).getName());
        holder.price.setText(String.format("%.2f", CartService.getTotalPrice(cartItems.get(position).getDishId())) + " BYN");
        holder.count.setText("Count: " + cartItems.get(position).getCount());
        holder.remove.setOnClickListener(view -> {
            listener.removeClicked(cartItems.get(position), position);
        });
    }

}

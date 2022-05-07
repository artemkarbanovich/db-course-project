package karbanovich.fit.bstu.foodie.adapters.cart;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import karbanovich.fit.bstu.foodie.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder {

    public ImageView photo;
    public TextView name;
    public TextView price;
    public TextView count;
    public ImageButton remove;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        photo = itemView.findViewById(R.id.cart_item__photo);
        name = itemView.findViewById(R.id.cart_item__dish_name);
        price = itemView.findViewById(R.id.cart_item__dishes_price);
        count = itemView.findViewById(R.id.cart_item__dishes_count);
        remove = itemView.findViewById(R.id.cart_item__remove_btn);
    }
}

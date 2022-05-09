package karbanovich.fit.bstu.foodie.adapters.orders;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import karbanovich.fit.bstu.foodie.R;

public class OrdersViewHolder extends RecyclerView.ViewHolder {

    public TextView orderDate;
    public TextView deliveryDate;
    public TextView address;
    public TextView totalPrice;
    public TextView status;

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        orderDate = itemView.findViewById(R.id.order_item__order_date);
        deliveryDate = itemView.findViewById(R.id.order_item__delivery_date);
        address = itemView.findViewById(R.id.order_item__address);
        totalPrice = itemView.findViewById(R.id.order_item__total_price);
        status = itemView.findViewById(R.id.order_item__status);
    }
}

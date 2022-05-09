package karbanovich.fit.bstu.foodie.adapters.orders;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.models.Order;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersViewHolder> {

    private Context context;
    private ArrayList<Order> orders;

    public OrdersAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override public int getItemCount() { return orders.size(); }


    @NonNull @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersViewHolder(LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        holder.orderDate.setText("Order date: " + orders.get(position).getOrderDate()
                .replace("T", "  ")
                .substring(0, orders.get(position).getOrderDate().length() - 2));
        holder.deliveryDate.setText("Delivery date: " + orders.get(position).getDeliveryDate()
                .replace("T", "  ")
                .substring(0, orders.get(position).getDeliveryDate().length() - 2));
        holder.address.setText(orders.get(position).getAddress());
        holder.totalPrice.setText(String.format("%.2f", orders.get(position).getTotalPrice()) + " BYN");
        holder.status.setText(orders.get(position).getStatus().replace("_", " "));
    }
}

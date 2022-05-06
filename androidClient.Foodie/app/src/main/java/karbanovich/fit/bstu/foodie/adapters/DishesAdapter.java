package karbanovich.fit.bstu.foodie.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.models.Dish;

public class DishesAdapter extends RecyclerView.Adapter<DishesViewHolder> {

    private Context context;
    private ArrayList<Dish> dishes;

    public DishesAdapter(Context context, ArrayList<Dish> dishes) {
        this.context = context;
        this.dishes = dishes;
    }

    @Override public int getItemCount() { return dishes.size(); }

    @NonNull @Override
    public DishesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DishesViewHolder(LayoutInflater.from(context).inflate(R.layout.dish_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DishesViewHolder holder, int position) {
        holder.name.setText(dishes.get(position).getName());
        holder.cookingTime.setText("Cooking time: " + dishes.get(position).getCookingTime().substring(0, dishes.get(position).getCookingTime().length() - 3));
        holder.weight.setText("Dish weight: " + dishes.get(position).getDishWeight());
        holder.ingredients.setText(dishes.get(position).getIngredients());
        holder.youWillNeed.setText(dishes.get(position).getYouWillNeed());
        holder.price.setText(dishes.get(position).getPrice() + " BYN");

        holder.toCart.setOnClickListener(view -> {
            // TODO: handle TO CART

            Toast.makeText(context, "HELLO, I'm " + dishes.get(position).getName(), Toast.LENGTH_LONG).show();
        });
    }
}

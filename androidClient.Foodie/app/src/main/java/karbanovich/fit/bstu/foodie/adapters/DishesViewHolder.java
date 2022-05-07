package karbanovich.fit.bstu.foodie.adapters;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;

import karbanovich.fit.bstu.foodie.R;

public class DishesViewHolder extends RecyclerView.ViewHolder {

    public ImageSlider photos;
    public TextView name;
    public TextView cookingTime;
    public TextView weight;
    public TextView ingredients;
    public TextView youWillNeed;
    public TextView price;
    public Button toCart;

    public DishesViewHolder(@NonNull View itemView) {
        super(itemView);

        photos = itemView.findViewById(R.id.dish_item__image_slider);
        name = itemView.findViewById(R.id.dish_item__name);
        cookingTime = itemView.findViewById(R.id.dish_item__cooking_time);
        weight = itemView.findViewById(R.id.dish_item__weight);
        ingredients = itemView.findViewById(R.id.dish_item__ingredients);
        youWillNeed = itemView.findViewById(R.id.dish_item__you_will_need);
        price = itemView.findViewById(R.id.dish_item__price);
        toCart = itemView.findViewById(R.id.dish_item__to_cart_btn);
    }
}

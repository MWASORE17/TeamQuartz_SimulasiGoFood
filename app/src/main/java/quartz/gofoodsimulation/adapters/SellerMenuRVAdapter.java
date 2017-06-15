package quartz.gofoodsimulation.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.SellerActivity;
import quartz.gofoodsimulation.models.CartModel;
import quartz.gofoodsimulation.models.FoodModel;
import quartz.gofoodsimulation.models.GroceryModel;
import quartz.gofoodsimulation.models.SellerModel;

/**
 * Created by kened on 5/10/2017.
 * Modified by:
 * sxio - 23-05-2017: terapkan sistem cart
 */

public class SellerMenuRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<FoodModel> foodModels;
    private Context context;
    private SellerModel seller;

    public SellerMenuRVAdapter(Context context) {
        this.context = context;
        seller = (SellerModel) ((SellerActivity) context).getIntent().getExtras().get("SELLER");
        foodModels = new ArrayList<>();
    }

    public void setFood(ArrayList<FoodModel> foodModels) {
        this.foodModels = foodModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder _holder = (ViewHolder) holder;
        final FoodModel food = this.foodModels.get(position);
        _holder.menuImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ico_gofood));
        _holder.menuName.setText(food.getNameFood());
        _holder.menuPrice.setText(String.format("Rp %,d", food.getPriceFood()));
        _holder.quantity.setText("0");

        try {
            InputStream ims = context.getAssets().open("foodMenuPicture/" + food.getPhotoFood());
            Drawable d = Drawable.createFromStream(ims, null);
            _holder.menuImage.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < CartModel.cart.getGroceries().size(); i++) {
            if (CartModel.cart.getGroceries().get(i).getFood().getIdFood().equals(food.getIdFood())) {
                _holder.quantity.setText(String.valueOf(CartModel.cart.getGroceries().get(i).getQuantity()));
            }
        }

        _holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(_holder.quantity.getText().toString()) + 1;

                if (CartModel.cart.getTotalQuantity() == 0) {
                    SellerActivity.quickReview.show();
                    ((SellerActivity) context).addSubtractViewPagerMargin(true);
                }

                GroceryModel g = new GroceryModel(food, q);
                CartModel.cart.addGrocery(g);
                SellerActivity.quickReview.setPrice(CartModel.cart.getTotalPrice());
                _holder.quantity.setText(String.valueOf(q));
            }
        });

        _holder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(_holder.quantity.getText().toString()) - 1;
                if (q < 0) q = 0;
                else {
                    GroceryModel g = new GroceryModel(food, q);
                    CartModel.cart.subtractGrocery(g);

                    if (CartModel.cart.getTotalQuantity() <= 0) {
                        SellerActivity.quickReview.hide();
                        ((SellerActivity) context).addSubtractViewPagerMargin(false);
                    }
                    SellerActivity.quickReview.setPrice(CartModel.cart.getTotalPrice());
                }
                _holder.quantity.setText(String.valueOf(q));
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView menuImage;
        private TextView menuName, menuPrice, quantity;
        private Button subtract, add;

        public ViewHolder(View ItemView) {
            super(ItemView);
            menuImage = (ImageView) ItemView.findViewById(R.id.sellermenu_iv_image);
            menuName = (TextView) ItemView.findViewById(R.id.sellermenu_tv_name);
            menuPrice = (TextView) ItemView.findViewById(R.id.sellermenu_tv_price);
            subtract = (Button) ItemView.findViewById(R.id.sellermenu_btn_subtract);
            add = (Button) ItemView.findViewById(R.id.sellermenu_btn_add);
            quantity = (TextView) ItemView.findViewById(R.id.sellermenu_tv_quantity);

            if (seller.getStatus() == 0) {
                add.setEnabled(false);
                subtract.setEnabled(false);
                add.setBackgroundColor(context.getResources().getColor(R.color.gray));
                subtract.setBackgroundColor(context.getResources().getColor(R.color.gray));
            }
        }
    }
}
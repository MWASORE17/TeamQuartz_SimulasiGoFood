package quartz.gofoodsimulation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.models.GroceryModel;

/**
 * Created by sxio on 31-May-17.
 */

public class HistoryDetailRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<GroceryModel> groceries;

    public HistoryDetailRVAdapter() {
    }

    public void setGroceries(ArrayList<GroceryModel> groceries) {
        this.groceries = groceries;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_order_detail_item, parent, false);
        return new HistoryDetailRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder _holder = (ViewHolder) holder;
        final GroceryModel grocery = groceries.get(position);
        _holder.tvFoodName.setText(grocery.getFood().getNameFood());
        _holder.tvFoodQuantity.setText(String.valueOf(grocery.getQuantity()));
        _holder.tvFoodPrice.setText(String.format("Rp %,d", grocery.getFood().getPriceFood()));
    }

    @Override
    public int getItemCount() {
        return groceries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName, tvFoodQuantity, tvFoodPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFoodName = (TextView) itemView.findViewById(R.id.historydetailitem_tv_foodname);
            tvFoodQuantity = (TextView) itemView.findViewById(R.id.historydetailitem_tv_foodquantity);
            tvFoodPrice = (TextView) itemView.findViewById(R.id.historydetailitem_tv_foodprice);
        }
    }
}

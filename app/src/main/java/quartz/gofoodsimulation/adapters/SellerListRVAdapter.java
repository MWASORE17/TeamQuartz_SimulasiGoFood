package quartz.gofoodsimulation.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.SellerActivity;
import quartz.gofoodsimulation.models.CartModel;
import quartz.gofoodsimulation.models.SellerModel;

/**
 * Created by kened on 4/13/2017.
 */

public class SellerListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SellerModel> sellerModelList;
    private Context context;

    public SellerListRVAdapter() {
        this.sellerModelList = new ArrayList<>();
    }

    public void setSeller(ArrayList<SellerModel> _sellerModelList) {
        this.sellerModelList = _sellerModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.seller_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder _holder = (ViewHolder) holder;
        final SellerModel seller = this.sellerModelList.get(position);
        _holder.ivCloseForeground.setBackgroundResource(0);
        _holder.tvSellerName.setText(seller.getName());
        _holder.tvStatus.setText(seller.getStatus() == 1 ? "OPEN" : "CLOSED");
        _holder.tvOperationTime.setText(seller.getTime());

        if (seller.getStatus() == 0) {
            _holder.ivCloseForeground.setBackgroundResource(R.drawable.darker_foreground);
        }

        _holder.tvAddress.setText(seller.getAddress());
        _holder.tvCategory.setText(seller.getCategory());
        _holder.tvRange.setText(String.format("%.2f km", seller.getRange()));
        try {
            InputStream ims = context.getAssets().open("sellerCoverPhoto/" + seller.getCoverPhoto());
            Drawable d = Drawable.createFromStream(ims, null);
            _holder.ivSellerPhoto.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        _holder.flSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (CartModel.cart != null) {
                    if (CartModel.cart.getTotalQuantity() != 0 && CartModel.cart.getIdSeller() != seller.getId()) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Change Resto?")
                                .setMessage("Changing resto will cancel your existing order. Are you sure you want to change resto?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        CartModel.cart.clearCart();
                                        CartModel.cart.setIdSeller(seller.getId());
                                        changeActivity(v, seller);
                                    }
                                })
                                .setNegativeButton("NO", null)
                                .show();
                    } else {
                        changeActivity(v, seller);
                    }
                } else {
                    changeActivity(v, seller);
                }
            }
        });
    }

    private void changeActivity(View v, SellerModel seller) {
        Intent intent = new Intent(v.getContext(), SellerActivity.class);
        intent.putExtra("SELLER", seller);
        v.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return sellerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout flSeller;
        private ImageView ivSellerPhoto, ivCloseForeground;
        private TextView tvSellerName, tvRange, tvCategory, tvAddress, tvStatus, tvOperationTime;

        public ViewHolder(View ItemView) {
            super(ItemView);
            flSeller = (FrameLayout) ItemView.findViewById(R.id.sellerlist_fl);
            ivSellerPhoto = (ImageView) ItemView.findViewById(R.id.sellerlist_iv_gofoodicon);
            ivCloseForeground = (ImageView) ItemView.findViewById(R.id.sellerlist_iv_closeForeground);
            tvSellerName = (TextView) ItemView.findViewById(R.id.sellerlist_tv_name);
            tvStatus = (TextView) ItemView.findViewById(R.id.sellerlist_tv_status);
            tvAddress = (TextView) ItemView.findViewById(R.id.sellerlist_tv_addr);
            tvCategory = (TextView) ItemView.findViewById(R.id.sellerlist_tv_category);
            tvRange = (TextView) ItemView.findViewById(R.id.sellerlist_tv_range);
            tvOperationTime = (TextView) ItemView.findViewById(R.id.sellerlist_tv_operationTime);
        }
    }
}

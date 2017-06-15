package quartz.gofoodsimulation.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.HistoryActivity;
import quartz.gofoodsimulation.activity.SellerActivity;
import quartz.gofoodsimulation.data.SellerData;
import quartz.gofoodsimulation.data.TransactionDetailData;
import quartz.gofoodsimulation.fragments.HistoryDetailFragment;
import quartz.gofoodsimulation.models.CartModel;
import quartz.gofoodsimulation.models.HistoryHeaderModel;
import quartz.gofoodsimulation.models.SellerModel;
import quartz.gofoodsimulation.models.TransactionHeaderModel;
import quartz.gofoodsimulation.utility.DateHelper;

/**
 * Created by sxio on 30-May-17.
 */

public class HistoryRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    SellerData sellerData;
    ArrayList<HistoryHeaderModel> historyHeaderModel;

    public HistoryRVAdapter() {
    }

    public void setHistoryHeaderModel(ArrayList<HistoryHeaderModel> historyHeaderModel) {
        this.historyHeaderModel = historyHeaderModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_order_item, parent, false);
        return new HistoryRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder _holder = (ViewHolder) holder;
        final HistoryHeaderModel hhm = this.historyHeaderModel.get(position);
        sellerData = new SellerData(context);
        final SellerModel seller = sellerData.getMatchSellers(hhm.getSellerName(), 5, "").get(0);

        try {
            InputStream ims = context.getAssets().open("sellerCoverPhoto/" + seller.getCoverPhoto());
            Drawable d = Drawable.createFromStream(ims, null);
            _holder.ivPhoto.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        _holder.tvOrderDate.setText(DateHelper.format(hhm.getTglOrder()));
        _holder.tvSellerName.setText(hhm.getSellerName());
        _holder.tvSellerAddress.setText(hhm.getSellerAddress());
        _holder.tvCost.setText(String.format("Rp %,d", hhm.getGrandTotal()));
        _holder.tvStatus.setText(hhm.getStatusBayar());

        //Untuk mengimplementasikan style status
        switch (hhm.getStatusBayar()) {
            case TransactionHeaderModel.PAID:
                _holder.tvStatus.setTextColor(Color.parseColor("#11d843"));
                _holder.tvStatus.setBackgroundResource(R.drawable.seller_status_open_style);
                break;
            case TransactionHeaderModel.UNPAID:
                _holder.tvStatus.setTextColor(Color.parseColor("#ff6100"));
                _holder.tvStatus.setBackgroundResource(R.drawable.status_bayar_unpaid_style);
                break;
            case TransactionHeaderModel.CANCEL:
                _holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                _holder.tvStatus.setBackgroundResource(R.drawable.seller_status_closed_style);
                break;
        }

        _holder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HistoryActivity) v.getContext()).changeFragment(new HistoryDetailFragment(hhm.getIdTransaksi()));
            }
        });

        Date now = new Date();
        Date tglOrder = DateHelper.convertToDate(hhm.getTglOrder());

        // 259200000 = 3 days
        if (now.getTime() - tglOrder.getTime() > 259200000) {
            _holder.tvReOrder.setEnabled(false);
            _holder.tvReOrder.setTextColor(Color.BLACK);
            _holder.tvReOrder.setBackgroundColor(context.getResources().getColor(R.color.gray));
        }

        _holder.tvReOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SellerData sellerData = new SellerData(v.getContext());
                TransactionDetailData transactionDetailData = new TransactionDetailData(v.getContext());
//                CartModel.cart.setIdSeller(sellerData.getMatchSellers(hhm.getSellerName(), 5, "").get(0).getId());
                CartModel.cart.setGroceries(transactionDetailData.getTransactionDetailById(hhm.getIdTransaksi()));

                HistoryActivity.isReOrder = true;

                Intent intent = new Intent(v.getContext(), SellerActivity.class);
                intent.putExtra("SELLER", seller);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyHeaderModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private TextView tvOrderDate,
                tvSellerName,
                tvSellerAddress,
                tvCost,
                tvDetail,
                tvReOrder,
                tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderDate = (TextView) itemView.findViewById(R.id.history_tv_date);
            tvSellerName = (TextView) itemView.findViewById(R.id.history_tv_sellername);
            tvSellerAddress = (TextView) itemView.findViewById(R.id.history_tv_selleraddr);
            tvCost = (TextView) itemView.findViewById(R.id.history_tv_cost);
            tvDetail = (TextView) itemView.findViewById(R.id.history_tv_detail);
            tvReOrder = (TextView) itemView.findViewById(R.id.history_tv_reorder);
            tvStatus = (TextView) itemView.findViewById(R.id.history_tv_status);
            ivPhoto = (ImageView) itemView.findViewById(R.id.history_iv_sellerpic);
        }
    }
}

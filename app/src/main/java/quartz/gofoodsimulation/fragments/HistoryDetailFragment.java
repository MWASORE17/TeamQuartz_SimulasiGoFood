package quartz.gofoodsimulation.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import quartz.gofoodsimulation.adapters.HistoryDetailRVAdapter;
import quartz.gofoodsimulation.data.SellerData;
import quartz.gofoodsimulation.data.TransactionDetailData;
import quartz.gofoodsimulation.data.TransactionHeaderData;
import quartz.gofoodsimulation.models.CartModel;
import quartz.gofoodsimulation.models.GroceryModel;
import quartz.gofoodsimulation.models.HistoryHeaderModel;
import quartz.gofoodsimulation.models.SellerModel;
import quartz.gofoodsimulation.models.TransactionHeaderModel;
import quartz.gofoodsimulation.utility.DateHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryDetailFragment extends Fragment {
    /* data */
    SellerData sellerData;
    SellerModel seller;
    private TransactionHeaderData transactionHeaderData;
    private TransactionDetailData transactionDetailData;
    private HistoryHeaderModel historyHeaderModel;
    private ArrayList<GroceryModel> groceries;
    private HistoryDetailRVAdapter adapter;
    private RecyclerView rv;
    private String idTransaksi;
    /* view component */
    private TextView tvHistoryDetailDate;
    private TextView tvHistoryDetailSellerName;
    private TextView tvHistoryDetailSellerAddress;
    private TextView tvHistoryDetailCost;
    private TextView tvHistoryDetailReOrder;
    private TextView tvHistoryDetailStatus;
    private ImageView ivHistoryDetailPhoto;

    public HistoryDetailFragment(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_order_detail_layout, container, false);

        /* Collecting data from database */
        sellerData = new SellerData(getContext());
        transactionHeaderData = new TransactionHeaderData(getContext());
        transactionDetailData = new TransactionDetailData(getContext());
        historyHeaderModel = transactionHeaderData.getTransactionById(idTransaksi);
        groceries = transactionDetailData.getTransactionDetailById(idTransaksi);
        seller = sellerData.getMatchSellers(historyHeaderModel.getSellerName(), -1, "").get(0);

        /* instantiate view component */
        tvHistoryDetailDate = (TextView) view.findViewById(R.id.historydetail_tv_date);
        tvHistoryDetailSellerName = (TextView) view.findViewById(R.id.historydetail_tv_sellername);
        tvHistoryDetailSellerAddress = (TextView) view.findViewById(R.id.historydetail_tv_selleraddr);
        tvHistoryDetailCost = (TextView) view.findViewById(R.id.historydetail_tv_cost);
        tvHistoryDetailReOrder = (TextView) view.findViewById(R.id.historydetail_tv_reorder);
        tvHistoryDetailStatus = (TextView) view.findViewById(R.id.historydetail_tv_status);
        ivHistoryDetailPhoto = (ImageView) view.findViewById(R.id.historydetail_iv_sellerpic);

        try {
            InputStream ims = getContext().getAssets().open("sellerCoverPhoto/" + seller.getCoverPhoto());
            Drawable d = Drawable.createFromStream(ims, null);
            ivHistoryDetailPhoto.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        tvHistoryDetailDate.setText(DateHelper.format(historyHeaderModel.getTglOrder()));
        tvHistoryDetailSellerName.setText(historyHeaderModel.getSellerName());
        tvHistoryDetailSellerAddress.setText(historyHeaderModel.getSellerAddress());
        tvHistoryDetailCost.setText(String.format("Rp %,d", historyHeaderModel.getGrandTotal()));
        tvHistoryDetailStatus.setText(historyHeaderModel.getStatusBayar());

        //Untuk mengimplementasikan style status
        switch (historyHeaderModel.getStatusBayar()) {
            case TransactionHeaderModel.PAID:
                tvHistoryDetailStatus.setTextColor(Color.parseColor("#11d843"));
                tvHistoryDetailStatus.setBackgroundResource(R.drawable.seller_status_open_style);
                break;
            case TransactionHeaderModel.UNPAID:
                tvHistoryDetailStatus.setTextColor(Color.parseColor("#ff6100"));
                tvHistoryDetailStatus.setBackgroundResource(R.drawable.status_bayar_unpaid_style);
                break;
            case TransactionHeaderModel.CANCEL:
                tvHistoryDetailStatus.setTextColor(getResources().getColor(R.color.red));
                tvHistoryDetailStatus.setBackgroundResource(R.drawable.seller_status_closed_style);
                break;
        }

        Date now = new Date();
        Date tglOrder = DateHelper.convertToDate(historyHeaderModel.getTglOrder());

        // 259200000 = 3 days
        if (now.getTime() - tglOrder.getTime() > 259200000) {
            tvHistoryDetailReOrder.setEnabled(false);
            tvHistoryDetailReOrder.setTextColor(Color.BLACK);
            tvHistoryDetailReOrder.setBackgroundColor(getResources().getColor(R.color.gray));
        }

        tvHistoryDetailReOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionDetailData = new TransactionDetailData(v.getContext());
                CartModel.cart.setGroceries(transactionDetailData.getTransactionDetailById(historyHeaderModel.getIdTransaksi()));

                HistoryActivity.isReOrder = true;

                Intent intent = new Intent(v.getContext(), SellerActivity.class);
                intent.putExtra("SELLER", seller);
                v.getContext().startActivity(intent);
            }
        });

        /* Setting up RecyclerView and its Adapter */
        adapter = new HistoryDetailRVAdapter();
        adapter.setGroceries(groceries);
        rv = (RecyclerView) view.findViewById(R.id.historydetail_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);


        /* Toolbar */
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.historydetail_toolbar, new ToolBarFragment(1, "DETAILS"))
                .commit();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        historyHeaderModel = transactionHeaderData.getTransactionById(idTransaksi);
        groceries = transactionDetailData.getTransactionDetailById(idTransaksi);
        adapter.setGroceries(groceries);
        rv.setAdapter(adapter);
        tvHistoryDetailCost.setText(String.format("Rp %,d", historyHeaderModel.getGrandTotal()));
    }
}
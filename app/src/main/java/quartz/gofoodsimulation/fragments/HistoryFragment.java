package quartz.gofoodsimulation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.adapters.HistoryRVAdapter;
import quartz.gofoodsimulation.data.TransactionHeaderData;
import quartz.gofoodsimulation.models.CartModel;
import quartz.gofoodsimulation.models.HistoryHeaderModel;
import quartz.gofoodsimulation.models.UserModel;
import quartz.gofoodsimulation.utility.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Created by sxio on 30-May-17.
 */
public class HistoryFragment extends Fragment {
    /* view component */
    private View emptyHistoryView;
    private TextView tvEmptyHistory;
    private RecyclerView rv;

    /* data */
    private UserModel user;
    private HistoryRVAdapter adapter;
    private TransactionHeaderData trhData;
    private ArrayList<HistoryHeaderModel> historyHeaderModels;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_order_layout, container, false);
        user = SessionManager.with(getContext()).getUserLoggedIn();

        trhData = new TransactionHeaderData(getContext());
        historyHeaderModels = trhData.getTransactionByUserId(user.getId());

        adapter = new HistoryRVAdapter();
        adapter.setHistoryHeaderModel(historyHeaderModels);
        rv = (RecyclerView) view.findViewById(R.id.history_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.history_toolbar, new ToolBarFragment(1, "ORDER HISTORY"))
                .commit();

        emptyHistoryView = (View) view.findViewById(R.id.history_empty_history);
        if (historyHeaderModels.isEmpty()) {
            tvEmptyHistory = (TextView) view.findViewById(R.id.nohistory_tv_redGOFOOD);
            tvEmptyHistory.setText(Html.fromHtml(getString(R.string.startUsingGOFOOD)));
            emptyHistoryView.setVisibility(View.VISIBLE);
        } else {
            emptyHistoryView.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CartModel.cart != null) {
            CartModel.cart.clearCart();
        }
        historyHeaderModels = trhData.getTransactionByUserId(user.getId());
        adapter.setHistoryHeaderModel(historyHeaderModels);
        rv.setAdapter(adapter);
    }
}

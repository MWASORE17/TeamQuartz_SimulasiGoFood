package quartz.gofoodsimulation.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.OrderActivity;
import quartz.gofoodsimulation.data.TransactionHeaderData;
import quartz.gofoodsimulation.models.TransactionHeaderModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchingDriverFragment extends Fragment {
    Context context;
    TransactionHeaderData trhData;
    TransactionHeaderModel trhModel;

    Handler handler;
    Runnable runnable;

    ImageView ivCancel;

    public SearchingDriverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.searching_driver_layout, container, false);
        ivCancel = (ImageView) view.findViewById(R.id.searchingdriver_iv_cancel);

        trhModel = (TransactionHeaderModel) getArguments().getSerializable("TRANSACTION_HEADER");

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                OrderTrackingFragment otf = new OrderTrackingFragment();
                Bundle b = new Bundle();
                b.putSerializable("TRANSACTION_HEADER", trhModel);
                otf.setArguments(b);
                ((OrderActivity) context).changeFragment(otf);
            }
        };
        Random rand = new Random();
        handler.postDelayed(runnable, rand.nextInt(8000) + 1000);

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderConfirmFragment.backable = true;
                handler.removeCallbacks(runnable);
                trhData = new TransactionHeaderData(context);
                trhModel.setStatusBayar(TransactionHeaderModel.CANCEL);
                trhData.updateTransaction(trhModel);

                Toast.makeText(context, "Your order has been canceled!", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
        return view;
    }
}

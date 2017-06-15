package quartz.gofoodsimulation.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.OrderActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThankYouFragment extends Fragment {

    TextView tvArrived,
            tvThankYou;

    public ThankYouFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thanks_banner_layout, container, false);
        tvArrived = (TextView) view.findViewById(R.id.thanksbanner_tv_arrived);
        tvThankYou = (TextView) view.findViewById(R.id.thanksbanner_tv_thankyou);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/contb.ttf");
        tvArrived.setTypeface(typeface);
        tvThankYou.setTypeface(typeface);

        tvThankYou.setText(Html.fromHtml(getString(R.string.thankYouGOFOOD)));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((OrderActivity) getContext()).finish();
            }
        }, 2500);
        return view;
    }

}

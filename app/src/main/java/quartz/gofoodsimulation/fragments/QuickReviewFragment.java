package quartz.gofoodsimulation.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.OrderActivity;

/**
 * A simple {@link Fragment} subclass.
 * Created by sxio on 24-May-17.
 */
public class QuickReviewFragment extends Fragment {
    private LinearLayout llQuickReview;
    private TextView tvEstimatedPrice;

    public QuickReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_quick_review, container, false);
        init(view);
        return view;
    }

    private void init(View v) {
        llQuickReview = (LinearLayout) v.findViewById(R.id.quickreview_bs);
        tvEstimatedPrice = (TextView) v.findViewById(R.id.quickreview_tv_estimatedprice);

        llQuickReview.setClickable(true);
        llQuickReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OrderActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void setPrice(long price) {
        tvEstimatedPrice.setText(String.format("Rp %,d", price));
    }

    /**
     * To show QuickReviewFragment
     */
    public void show() {
        final FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        if (this.isHidden()) {
            ft.show(this);
            ft.commit();
        }
    }

    /**
     * To hide QuickReviewFragment
     */
    public void hide() {
        final FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        if (!this.isHidden()) {
            ft.hide(this);
            ft.commit();
        }
    }
}

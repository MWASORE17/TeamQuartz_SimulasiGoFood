package quartz.gofoodsimulation.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.AccountActivity;
import quartz.gofoodsimulation.activity.HistoryActivity;
import quartz.gofoodsimulation.activity.ParentActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToolBarFragment extends Fragment {
    TextView title;
    Toolbar toolbar;
    ImageView accountIcon, historyIcon;
    int flag = 0;
    String titles;

    public ToolBarFragment() {
        // Required empty public constructor
    }

    public ToolBarFragment(int flag, String titles) {
        this.flag = flag;
        this.titles = titles;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tool_bar, container, false);
        init(view);
        if (flag == 0) {
            mainTitle();
        } else {
            changeTitle(titles);
        }
        return view;
    }

    private void init(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        accountIcon = (ImageView) view.findViewById(R.id.toolbar_iv_accounticon);
        title = (TextView) view.findViewById(R.id.toolbar_title);
        historyIcon = (ImageView) view.findViewById(R.id.toolbar_iv_historyorder);
    }

    public void changeTitle(String titles) {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/contb.ttf");
        toolbar.setLogo(null);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        title.setText(titles);
        title.setTextColor(getResources().getColor(R.color.white));
        title.setTextSize(20);
        title.setTypeface(typeface);
        title.setGravity(Gravity.LEFT);
        accountIcon.setVisibility(View.GONE);
        historyIcon.setVisibility(View.GONE);
    }

    public void mainTitle() {
        ((ParentActivity) getActivity()).setSupportActionBar(toolbar);
        ((ParentActivity) getActivity()).getSupportActionBar().setTitle("");
        toolbar.setLogo(R.drawable.logo_gfs_small);

        accountIcon.setClickable(true);
        accountIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _intent = new Intent(getContext(), AccountActivity.class);
                _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(_intent);
            }
        });
        historyIcon.setClickable(true);
        historyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _intent = new Intent(getContext(), HistoryActivity.class);
                _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(_intent);
            }
        });
    }
}

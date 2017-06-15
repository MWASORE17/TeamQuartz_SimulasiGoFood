package quartz.gofoodsimulation.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.AccountActivity;
import quartz.gofoodsimulation.activity.AuthenticationActivity;
import quartz.gofoodsimulation.activity.ParentActivity;
import quartz.gofoodsimulation.models.UserModel;
import quartz.gofoodsimulation.utility.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Created by sxio on 04-Jun-17.
 */
public class ProfileFragment extends Fragment {
    Context context;
    TextView tvName, tvEmail, tvPhone, tvDeposit, tvPoint, tvLogout;
    Button btnTopUp;
    UserModel userLoggedIn;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);
        context = getContext();
        init(view);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.profile_toolbar, new ToolBarFragment(1, "Account")).commit();

        loadingFragment(this);

        return view;
    }

    private void init(View view) {
        tvName = (TextView) view.findViewById(R.id.profile_tv_username);
        tvEmail = (TextView) view.findViewById(R.id.profile_tv_useremail);
        tvPhone = (TextView) view.findViewById(R.id.profile_tv_userphone);
        tvDeposit = (TextView) view.findViewById(R.id.profile_tv_gopaydeposit);
        tvPoint = (TextView) view.findViewById(R.id.profile_tv_points);
        tvLogout = (TextView) view.findViewById(R.id.logout);
        btnTopUp = (Button) view.findViewById(R.id.profile_btn_topup);

        userLoggedIn = SessionManager.with(context).getUserLoggedIn();
        tvName.setText(userLoggedIn.getNama());
        tvEmail.setText(userLoggedIn.getEmail());
        tvPhone.setText(userLoggedIn.getNoTelp());
        tvDeposit.setText(String.format("Rp %,d", userLoggedIn.getGoPay()));
        tvPoint.setText(String.format("%,d pt", userLoggedIn.getPoint()));

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.with(context).clearSession();
                ((ParentActivity) context).doChangeActivity(context, AuthenticationActivity.class);
                getActivity().finish();
            }
        });

        btnTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AccountActivity) context).changeFragment(new TopUpFragment());
            }
        });
    }

    private void loadingFragment(Fragment fragment) {
        final Fragment thisFragment = fragment;
        final ProgressDialog progressDialog = new ProgressDialog(fragment.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        progressDialog.show();
        getFragmentManager().beginTransaction().hide(thisFragment).commit();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                getFragmentManager().beginTransaction().show(thisFragment).commit();
            }
        }, 2500);
    }
}

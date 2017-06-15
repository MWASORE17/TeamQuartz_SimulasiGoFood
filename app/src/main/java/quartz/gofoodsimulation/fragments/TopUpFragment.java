package quartz.gofoodsimulation.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.data.UserData;
import quartz.gofoodsimulation.models.UserModel;
import quartz.gofoodsimulation.utility.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopUpFragment extends Fragment {
    Context context;
    TextView tvTopUpValue,
            tvTopUp50000,
            tvTopUp100000,
            tvTopUp150000,
            tvTopUp200000,
            tvTopUpAccountNumber;
    EditText etTopUpInput;
    ImageView ivBCA, ivPermata, ivMandiri, ivMaybank;
    Button btnTopUp;
    long topUpValue;
    boolean bankChosen = false,
            nominalChosen = false;
    UserModel user;

    public TopUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.topup_gopay_layout, container, false);
        context = getContext();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.topup_toolbar, new ToolBarFragment(1, "Top Up")).commit();
        init(view);
        return view;
    }

    private void init(View view) {
        user = SessionManager.with(context).getUserLoggedIn();
        tvTopUpValue = (TextView) view.findViewById(R.id.topup_tv_value);
        tvTopUp50000 = (TextView) view.findViewById(R.id.topup_tv_50000);
        tvTopUp100000 = (TextView) view.findViewById(R.id.topup_tv_100000);
        tvTopUp150000 = (TextView) view.findViewById(R.id.topup_tv_150000);
        tvTopUp200000 = (TextView) view.findViewById(R.id.topup_tv_200000);
        tvTopUpAccountNumber = (TextView) view.findViewById(R.id.topup_tv_accountnumber);
        etTopUpInput = (EditText) view.findViewById(R.id.topup_et_input);
        ivBCA = (ImageView) view.findViewById(R.id.topup_iv_bca);
        ivPermata = (ImageView) view.findViewById(R.id.topup_iv_permata);
        ivMandiri = (ImageView) view.findViewById(R.id.topup_iv_mandiri);
        ivMaybank = (ImageView) view.findViewById(R.id.topup_iv_maybank);
        btnTopUp = (Button) view.findViewById(R.id.topup_btn_topup);
        btnTopUp.setEnabled(false);
        btnTopUp.setBackgroundColor(getResources().getColor(R.color.gray));
        tvTopUpValue.setText(String.format("Rp %,d", user.getGoPay()));

        tvTopUp50000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominalChosen = true;
                etTopUpInput.setText(String.valueOf(50000));
                tvTopUp50000.setTextColor(Color.WHITE);
                tvTopUp50000.setBackgroundColor(getResources().getColor(R.color.darkred));
                tvTopUp100000.setTextColor(Color.BLACK);
                tvTopUp100000.setBackgroundResource(R.drawable.add_menu_border_set);
                tvTopUp150000.setTextColor(Color.BLACK);
                tvTopUp150000.setBackgroundResource(R.drawable.add_menu_border_set);
                tvTopUp200000.setTextColor(Color.BLACK);
                tvTopUp200000.setBackgroundResource(R.drawable.add_menu_border_set);
            }
        });
        tvTopUp100000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominalChosen = true;
                etTopUpInput.setText(String.valueOf(100000));
                tvTopUp50000.setTextColor(Color.BLACK);
                tvTopUp50000.setBackgroundResource(R.drawable.add_menu_border_set);
                tvTopUp100000.setTextColor(Color.WHITE);
                tvTopUp100000.setBackgroundColor(getResources().getColor(R.color.darkred));
                tvTopUp150000.setTextColor(Color.BLACK);
                tvTopUp150000.setBackgroundResource(R.drawable.add_menu_border_set);
                tvTopUp200000.setTextColor(Color.BLACK);
                tvTopUp200000.setBackgroundResource(R.drawable.add_menu_border_set);
            }
        });
        tvTopUp150000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominalChosen = true;
                etTopUpInput.setText(String.valueOf(150000));
                tvTopUp50000.setTextColor(Color.BLACK);
                tvTopUp50000.setBackgroundResource(R.drawable.add_menu_border_set);
                tvTopUp100000.setTextColor(Color.BLACK);
                tvTopUp100000.setBackgroundResource(R.drawable.add_menu_border_set);
                tvTopUp150000.setTextColor(Color.WHITE);
                tvTopUp150000.setBackgroundColor(getResources().getColor(R.color.darkred));
                tvTopUp200000.setTextColor(Color.BLACK);
                tvTopUp200000.setBackgroundResource(R.drawable.add_menu_border_set);
            }
        });
        tvTopUp200000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominalChosen = true;
                etTopUpInput.setText(String.valueOf(200000));
                tvTopUp50000.setTextColor(Color.BLACK);
                tvTopUp50000.setBackgroundResource(R.drawable.add_menu_border_set);
                tvTopUp100000.setTextColor(Color.BLACK);
                tvTopUp100000.setBackgroundResource(R.drawable.add_menu_border_set);
                tvTopUp150000.setTextColor(Color.BLACK);
                tvTopUp150000.setBackgroundResource(R.drawable.add_menu_border_set);
                tvTopUp200000.setTextColor(Color.WHITE);
                tvTopUp200000.setBackgroundColor(getResources().getColor(R.color.darkred));
            }
        });

        ivBCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankChosen = true;
                ivBCA.setBackgroundResource(R.drawable.selected_box_border_set);
                ivPermata.setBackground(null);
                ivMandiri.setBackground(null);
                ivMaybank.setBackground(null);
                topUpChecker(tvTopUpAccountNumber, nominalChosen, bankChosen);
            }
        });
        ivPermata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankChosen = true;
                ivPermata.setBackgroundResource(R.drawable.selected_box_border_set);
                ivBCA.setBackground(null);
                ivMandiri.setBackground(null);
                ivMaybank.setBackground(null);
                topUpChecker(tvTopUpAccountNumber, nominalChosen, bankChosen);
            }
        });
        ivMandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankChosen = true;
                ivMandiri.setBackgroundResource(R.drawable.selected_box_border_set);
                ivPermata.setBackground(null);
                ivBCA.setBackground(null);
                ivMaybank.setBackground(null);
                topUpChecker(tvTopUpAccountNumber, nominalChosen, bankChosen);
            }
        });
        ivMaybank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankChosen = true;
                ivMaybank.setBackgroundResource(R.drawable.selected_box_border_set);
                ivPermata.setBackground(null);
                ivMandiri.setBackground(null);
                ivBCA.setBackground(null);
                topUpChecker(tvTopUpAccountNumber, nominalChosen, bankChosen);
            }
        });

        tvTopUpAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                topUpChecker(tvTopUpAccountNumber, nominalChosen, bankChosen);
                if (s.length() == 10) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etTopUpInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!etTopUpInput.getText().toString().isEmpty()) {
                    topUpValue = Long.parseLong(etTopUpInput.getText().toString());
                } else {
                    topUpValue = 0;
                }

                if (topUpValue > 10000000) {
                    topUpValue = 10000000;
                    etTopUpInput.setText(String.valueOf(10000000));
                    etTopUpInput.setSelection(etTopUpInput.getText().length());
                }

                if (topUpValue % 5000 != 0 || topUpValue < 10000) {
                    nominalChosen = false;
                } else {
                    nominalChosen = true;
                }

                topUpChecker(tvTopUpAccountNumber, nominalChosen, bankChosen);
                tvTopUpValue.setText(String.format("Rp %,d", topUpValue + user.getGoPay()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = SessionManager.with(context).getUserLoggedIn();
                UserData userData = new UserData(context);
                userModel.setGoPay(topUpValue);
                userModel = userData.updateGoPay(userModel);
                SessionManager.with(context).updateSession(userModel);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void topUpChecker(TextView tvTopUpAccountNumber, boolean nominalChosen, boolean bankChosen) {
        if (tvTopUpAccountNumber.getText().length() == 10 && nominalChosen == true && bankChosen == true) {
            btnTopUp.setEnabled(true);
            btnTopUp.setBackgroundColor(getResources().getColor(R.color.darkred));
        } else {
            btnTopUp.setEnabled(false);
            btnTopUp.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }
}

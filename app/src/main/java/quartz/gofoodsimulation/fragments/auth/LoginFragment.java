package quartz.gofoodsimulation.fragments.auth;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.AuthenticationActivity;
import quartz.gofoodsimulation.activity.MainActivity;
import quartz.gofoodsimulation.data.UserData;
import quartz.gofoodsimulation.models.UserModel;
import quartz.gofoodsimulation.utility.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    /*data component*/
    UserModel user;
    UserData userData;

    /*view component*/
    private TextInputLayout containerEmail;
    private TextInputLayout containerPassword;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignUp;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_layout, container, false);
        init(view);
        event();
        return view;
    }

    private void init(View view) {
        containerEmail = (TextInputLayout) view.findViewById(R.id.login_email_container);
        containerPassword = (TextInputLayout) view.findViewById(R.id.login_password_container);
        etEmail = (EditText) view.findViewById(R.id.login_et_email);
        etPassword = (EditText) view.findViewById(R.id.login_et_password);
        btnLogin = (Button) view.findViewById(R.id.login_btn_login);
        tvSignUp = (TextView) view.findViewById(R.id.login_tv_link_signup);
    }

    private void event() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                containerEmail.setErrorEnabled(false);
                containerPassword.setErrorEnabled(false);
                String email = etEmail.getText().toString().toLowerCase();
                String password = etPassword.getText().toString();

                /*
                * checking data validation
                * front-end
                */
                if (TextUtils.isEmpty(email)) {
                    isValid = false;
                    containerEmail.setErrorEnabled(true);
                    containerEmail.setError("Email is required");
                } else if (!AuthenticationActivity.isValidEmail(email)) {
                    isValid = false;
                    containerEmail.setErrorEnabled(true);
                    containerEmail.setError("Email is not valid");
                } else if (TextUtils.isEmpty(password)) {
                    isValid = false;
                    containerPassword.setErrorEnabled(true);
                    containerPassword.setError("Password is required");
                }

                /*
                * check the account
                * back-end
                */
                if (isValid) {
                    boolean isRegis = false;
                    boolean isMatch = false;
                    userData = new UserData(getContext());
                    user = userData.getSingleEntry(email);

                    if (user.getEmail().equals(email)) {
                        isRegis = true;
                        if (user.getPass().equals(password)) {
                            isMatch = true;
                        }
                    }


                    if (!isRegis) {
                        containerEmail.setErrorEnabled(true);
                        containerEmail.setError("Email is not registered as a user.");
                    } else if (!isMatch) {
                        containerPassword.setErrorEnabled(true);
                        containerPassword.setError("Password is incorrect.");
                    }

                    if (isRegis && isMatch) {
                        SessionManager.with(getContext()).createSession(user);
                        ((AuthenticationActivity) getActivity()).doChangeActivity(getContext(), MainActivity.class);
                    }

                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthenticationActivity) getActivity()).changeFragment(new RegisterFragment());
            }
        });
    }
}

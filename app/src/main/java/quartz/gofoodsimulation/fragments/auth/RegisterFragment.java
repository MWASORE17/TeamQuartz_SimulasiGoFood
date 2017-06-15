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
import android.widget.Toast;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.AuthenticationActivity;
import quartz.gofoodsimulation.data.UserData;
import quartz.gofoodsimulation.models.UserModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    /*data component*/
    UserModel newUser;
    UserData userData;

    /*view component*/
    private TextInputLayout containerEmail;
    private TextInputLayout containerName;
    private TextInputLayout containerPhone;
    private TextInputLayout containerPass;
    private TextInputLayout containerRepass;
    private EditText etEmail;
    private EditText etName;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etRepass;
    private Button btnRegister;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_layout, container, false);
        init(view);
        event();
        return view;
    }

    private void init(View view) {
        containerEmail = (TextInputLayout) view.findViewById(R.id.register_email_container);
        containerName = (TextInputLayout) view.findViewById(R.id.register_name_container);
        containerPhone = (TextInputLayout) view.findViewById(R.id.register_phone_container);
        containerPass = (TextInputLayout) view.findViewById(R.id.register_password_container);
        containerRepass = (TextInputLayout) view.findViewById(R.id.register_repassword_container);
        etEmail = (EditText) view.findViewById(R.id.register_et_email);
        etName = (EditText) view.findViewById(R.id.register_et_name);
        etPhone = (EditText) view.findViewById(R.id.register_et_phone);
        etPassword = (EditText) view.findViewById(R.id.register_et_password);
        etRepass = (EditText) view.findViewById(R.id.register_et_repassword);
        btnRegister = (Button) view.findViewById(R.id.register_btn_register);
    }

    private void event() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                containerEmail.setErrorEnabled(false);
                containerName.setErrorEnabled(false);
                containerPhone.setErrorEnabled(false);
                containerPass.setErrorEnabled(false);
                containerRepass.setErrorEnabled(false);

                String email = etEmail.getText().toString().toLowerCase();
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                String repassword = etRepass.getText().toString();

                /* front-end validation */
                if (TextUtils.isEmpty(email)) {
                    isValid = false;
                    containerEmail.setErrorEnabled(true);
                    containerEmail.setError("Email is required");
                } else if (!AuthenticationActivity.isValidEmail(email)) {
                    isValid = false;
                    containerEmail.setErrorEnabled(true);
                    containerEmail.setError("Email is not valid");
                } else if ((new UserData(getContext()).getSingleEntry(email)).getEmail().equals(email)) {
                    isValid = false;
                    containerEmail.setErrorEnabled(true);
                    containerEmail.setError("Email already exist");
                } else if (TextUtils.isEmpty(name)) {
                    isValid = false;
                    containerName.setErrorEnabled(true);
                    containerName.setError("Name is required");
                } else if (TextUtils.isEmpty(phone)) {
                    isValid = false;
                    containerPhone.setErrorEnabled(true);
                    containerPhone.setError("Phone is required");
                } else if (!AuthenticationActivity.isPhoneValid(phone)) {
                    isValid = false;
                    containerPhone.setErrorEnabled(true);
                    containerPhone.setError("Phone is not valid");
                } else if (TextUtils.isEmpty(password)) {
                    isValid = false;
                    containerPass.setErrorEnabled(true);
                    containerPass.setError("Password is required");
                } else if (!AuthenticationActivity.isPasswordValid(password)) {
                    isValid = false;
                    containerPass.setErrorEnabled(true);
                    containerPass.setError("Password must contains at least 1 number and miimum 8 characters");
                } else if (TextUtils.isEmpty(repassword)) {
                    isValid = false;
                    containerRepass.setErrorEnabled(true);
                    containerRepass.setError("Retype Password is required");
                } else if (!password.equals(repassword)) {
                    isValid = false;
                    containerRepass.setErrorEnabled(true);
                    containerRepass.setError("Password not match");
                }

                if (isValid) {
                    userData = new UserData(getContext());
                    newUser = new UserModel(email, name, phone, password);
                    userData.createUser(newUser);
                    Toast.makeText(getContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                    ((AuthenticationActivity) getActivity()).changeFragment(new LoginFragment());
                }
            }
        });
    }
}

package quartz.gofoodsimulation.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.fragments.auth.LoginFragment;

public class AuthenticationActivity extends ParentActivity {

    /**
     * TODO: validate email
     *
     * @param email email to validate
     * @return true or false
     */
    public static boolean isValidEmail(String email) {
        String expression = "^[a-z]([a-z0-9-\\.]+)?+@[a-z]+\\.[a-z]{2,4}+(\\.[a-z]{2,4})?$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    /**
     * TODO: validate password
     *
     * @param password password to validate (password must contain at least
     *                 1 number at least 8 character long
     * @return true or false
     */
    public static boolean isPasswordValid(String password) {
        String expression = "^(?=.*[\\d])[\\w]{8,}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    /**
     * TODO: validate phone
     *
     * @param phone
     * @return true or false
     */
    public static boolean isPhoneValid(String phone) {
        String expression = "^(^\\+62\\s?|^0)(\\d{3,4}-?){2}\\d{3,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.auth_activity, new LoginFragment()).commit();
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.auth_activity, fragment).addToBackStack(null).commit();
    }
}

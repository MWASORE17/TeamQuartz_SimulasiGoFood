package quartz.gofoodsimulation.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.fragments.ProfileFragment;

/**
 * Created by sxio on 14-May-17.
 */

public class AccountActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.account_activity, new ProfileFragment()).commit();
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.account_activity, fragment).addToBackStack(null).commit();
    }
}

package quartz.gofoodsimulation.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.fragments.HistoryFragment;

public class HistoryActivity extends ParentActivity {

    public static boolean isReOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.history_main, new HistoryFragment()).commit();
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.history_main, fragment).addToBackStack(null).commit();
    }
}

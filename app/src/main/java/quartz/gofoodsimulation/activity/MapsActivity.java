package quartz.gofoodsimulation.activity;

import android.os.Bundle;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.fragments.OrderTrackingFragment;

/**
 * Created by meikelwis on 25-May-17.
 */

public class MapsActivity extends ParentActivity {

    public MapsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_tracking_layout);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.order_tracking_activity, new OrderTrackingFragment())
                .commit();

    }


}

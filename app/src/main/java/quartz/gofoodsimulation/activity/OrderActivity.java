package quartz.gofoodsimulation.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.fragments.OrderConfirmFragment;
import quartz.gofoodsimulation.utility.GPSTracker;

/**
 * Created by sxio on 24-May-17.
 */

public class OrderActivity extends ParentActivity {
    public LatLng deliveryLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_layout);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.order_activity, new OrderConfirmFragment())
                .commit();
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.order_activity, fragment).commit();
    }

    /**
     * TODO: retrieve delivery location (Getter)
     *
     * @return LatLng of delivery location
     */
    public LatLng getDeliveryLocation() {
        if (this.deliveryLocation == null) {
            GPSTracker gps = new GPSTracker(this);
            this.deliveryLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
            gps.stopUsingGPS();
        }
        return deliveryLocation;
    }

    /**
     * TODO: set delivery location (Setter)
     *
     * @param deliveryLocation LatLng to set
     */
    public void setDeliveryLocation(LatLng deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    /**
     * TODO: Find delivery address base on LatLng property
     *
     * @return Delivery Address
     */
    public String getDeliveryAddress() {
        GPSTracker gps = new GPSTracker(this);
        String address = gps.getAddress(this.deliveryLocation.latitude, this.deliveryLocation.longitude);
        gps.stopUsingGPS();
        return address;
    }

    @Override
    public void onBackPressed() {
        if (!OrderConfirmFragment.backable) {
            // do nothing
        } else {
            super.onBackPressed();
        }
    }
}
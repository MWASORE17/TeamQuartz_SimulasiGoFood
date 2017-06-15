package quartz.gofoodsimulation.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.data.UserData;
import quartz.gofoodsimulation.fragments.HomeFragment;
import quartz.gofoodsimulation.fragments.SearchBarFragment;
import quartz.gofoodsimulation.fragments.ToolBarFragment;
import quartz.gofoodsimulation.models.UserModel;
import quartz.gofoodsimulation.utility.GPSTracker;
import quartz.gofoodsimulation.utility.SessionManager;

public class MainActivity extends ParentActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    SearchBarFragment searchBarFragment = new SearchBarFragment();
    ToolBarFragment toolBarFragment = new ToolBarFragment();
    HomeFragment homeFragment = new HomeFragment();

    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*check session*/
        if (!SessionManager.with(getApplicationContext()).isLoggedIn()) {
            this.doChangeActivity(MainActivity.this, AuthenticationActivity.class);
        } else {
            // Update Session
            UserData userData = new UserData(this);
            UserModel userModel = SessionManager.with(this).getUserLoggedIn();
            SessionManager.with(this).updateSession(userData.getSingleEntry(userModel.getEmail()));

            /*FRAGMENT*/
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.replace(R.id.home, homeFragment, "homeFragment");
            ft.replace(R.id.tool_bar, toolBarFragment);
            ft.replace(R.id.search_bar, searchBarFragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeFragment.LISTTYPE = 5;
        searchBarFragment.etSearch.clearFocus();
        searchBarFragment.etSearch.setText("");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsEnabled && !networkEnabled) {
            GPSTracker.showSettingsAlert(this);
        }

        // Permission Location for android >= M
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                try {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                } catch (Exception e) {

                }
            }
        }
    }
}

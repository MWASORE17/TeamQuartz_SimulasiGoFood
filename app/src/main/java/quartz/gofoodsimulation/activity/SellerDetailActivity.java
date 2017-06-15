package quartz.gofoodsimulation.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.fragments.ToolBarFragment;
import quartz.gofoodsimulation.models.SellerModel;

public class SellerDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    TextView sellerName, sellerPhone, sellerAddr;
    Button callButton;
    private GoogleMap mMap;
    private int MY_PERMISSIONS_REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_detail_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SupportMapFragment map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.orderconfirm_map));
        init();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.sellerdetail_toolbar, new ToolBarFragment(1, "SELLER INFORMATION"))
                .commit();
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(SellerModel.seller.getLatitude(), SellerModel.seller.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(SellerModel.seller.getName()))
                .setIcon(BitmapDescriptorFactory.fromBitmap(markerResize()));
        mMap.setMinZoomPreference(10.0f);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        permission(mMap);
    }

    // custom marker
    private Bitmap markerResize() {
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.resto_icon);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }

    // View instantiate
    public void init() {
        sellerName = (TextView) findViewById(R.id.sellerdetail_tv_sellername);
        sellerPhone = (TextView) findViewById(R.id.sellerdetail_tv_sellerphone);
        sellerAddr = (TextView) findViewById(R.id.sellerdetail_tv_selleraddress);
        callButton = (Button) findViewById(R.id.sellerdetail_button_call);

        sellerName.setText(SellerModel.seller.getName());
        sellerPhone.setText(SellerModel.seller.getPhone());
        sellerAddr.setText(SellerModel.seller.getAddress());

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:" + SellerModel.seller.getPhone()));
                startActivity(phoneIntent);
            }
        });
    }

    /**
     * TODO: Request permission
     *
     * @param mMap map
     */
    private void permission(GoogleMap mMap) {
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
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SellerActivity.abl.setVisibility(View.VISIBLE);
        SellerActivity.nsv.setVisibility(View.VISIBLE);
    }
}
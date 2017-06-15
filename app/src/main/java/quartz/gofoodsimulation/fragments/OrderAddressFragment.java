package quartz.gofoodsimulation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.OrderActivity;
import quartz.gofoodsimulation.utility.GPSTracker;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderAddressFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {
    GoogleMap mMap;
    MapView mapView;
    Marker marker;
    TextView tvAddress;
    GPSTracker gps;
    LatLng latLng;

    String address;

    public OrderAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.order_address_layout, container, false);
        tvAddress = (TextView) view.findViewById(R.id.orderaddress_tv_address);
        tvAddress.setText("");

        MapsInitializer.initialize(getActivity().getApplicationContext());
        mapView = (MapView) view.findViewById(R.id.orderaddress_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.orderaddress_toolbar, new ToolBarFragment(1, "Change Address"))
                .commit();
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gps = new GPSTracker(getContext());
        latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
        gps.stopUsingGPS();

        mMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        marker = mMap.addMarker(new MarkerOptions()
                .position(mMap.getCameraPosition().target)
                .title("SET DESTINATION"));
        marker.showInfoWindow();

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                latLng = mMap.getCameraPosition().target;
                marker.setPosition(latLng);
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                address = gps.getAddress(latLng.latitude, latLng.longitude);
                tvAddress.setText(address);
            }
        });
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        this.onInfoWindowClick(marker);
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        ((OrderActivity) getContext()).setDeliveryLocation(mMap.getCameraPosition().target);
        getActivity().getSupportFragmentManager().popBackStack();
    }
}

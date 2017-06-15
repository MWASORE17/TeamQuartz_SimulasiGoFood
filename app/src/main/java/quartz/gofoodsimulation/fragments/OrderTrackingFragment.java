package quartz.gofoodsimulation.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;
import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.OrderActivity;
import quartz.gofoodsimulation.data.DriverData;
import quartz.gofoodsimulation.data.SellerData;
import quartz.gofoodsimulation.data.TransactionHeaderData;
import quartz.gofoodsimulation.data.UserData;
import quartz.gofoodsimulation.models.DriverModel;
import quartz.gofoodsimulation.models.SellerModel;
import quartz.gofoodsimulation.models.TransactionHeaderModel;
import quartz.gofoodsimulation.models.UserModel;
import quartz.gofoodsimulation.utility.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Created by meikelwis on 28-May-17.
 */
public class OrderTrackingFragment extends Fragment implements OnMapReadyCallback {
    private final String apiKey = "AIzaSyDMQheI4mY1weGFeRA5U55_gL5gu_-faCc";
    // view component
    Context context;
    MapView mapView;
    GoogleMap mMap;
    Marker driverMarker;
    Marker sellerMarker;
    Marker userMarker;
    TextView tvUserName,
            tvUserAddr,
            tvSellerName,
            tvSellerAddr,
            tvDriverName,
            tvOrderId;
    ImageView ivCallDriver,
            ivCancelOrder;
    CircleImageView ivDriverPhoto;
    AlertDialog userCancelDialog;
    // data component
    int indexMarker;
    UserData userData;
    UserModel user;
    SellerData sellerData;
    DriverData driverData;
    DriverModel driverModel;
    TransactionHeaderData trhData;
    TransactionHeaderModel trhModel;
    LatLng origin, dest, driverLoc;
    ArrayList<LatLng> originDestPositionList;
    ArrayList<LatLng> driverOriginPosisionList;
    Handler driverOriginHandler = new Handler();
    Runnable driverRunnable;

    public OrderTrackingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_tracking_layout, container, false);
        // instantiate view
        tvUserName = (TextView) view.findViewById(R.id.ordertracking_tv_username);
        tvUserAddr = (TextView) view.findViewById(R.id.ordertracking_tv_useraddr);
        tvSellerName = (TextView) view.findViewById(R.id.ordertracking_tv_sellername);
        tvSellerAddr = (TextView) view.findViewById(R.id.ordertracking_tv_selleraddr);
        tvDriverName = (TextView) view.findViewById(R.id.driverdetail_tv_drivername);
        tvOrderId = (TextView) view.findViewById(R.id.driverdetail_tv_orderID);
        ivCallDriver = (ImageView) view.findViewById(R.id.driverdetail_iv_call);
        ivCancelOrder = (ImageView) view.findViewById(R.id.driverdetail_iv_cancel);
        ivDriverPhoto = (CircleImageView) view.findViewById(R.id.driverdetail_civ_driverpic);

        // collecting data
        context = getContext();
        user = SessionManager.with(context).getUserLoggedIn();
        userData = new UserData(context);
        sellerData = new SellerData(context);
        driverData = new DriverData(context);
        trhData = new TransactionHeaderData(context);

        driverModel = getClosestDriverToSeller(driverData.getAllDriver());
        trhModel = (TransactionHeaderModel) getArguments().getSerializable("TRANSACTION_HEADER");
        trhModel.setIdDriver(driverModel.getId());

        origin = new LatLng(SellerModel.seller.getLatitude(), SellerModel.seller.getLongitude()); //seller location
        dest = ((OrderActivity) getContext()).getDeliveryLocation();
        driverLoc = new LatLng(driverModel.getLatitude(), driverModel.getLongitude());

        String address = ((OrderActivity) context).getDeliveryAddress();
        tvUserName.setText(user.getNama());
        tvUserAddr.setText(address);
        tvSellerName.setText(SellerModel.seller.getName());
        tvSellerAddr.setText(SellerModel.seller.getAddress());
        tvDriverName.setText(driverModel.getName());
        tvOrderId.setText(trhModel.getIdTransaksi());

        try {
            InputStream ims = context.getAssets().open("gojek_driver.png");
            Drawable d = Drawable.createFromStream(ims, null);
            ivDriverPhoto.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MapsInitializer.initialize(this.getActivity());
        mapView = (MapView) view.findViewById(R.id.orderconfirm_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        //call driver
        ivCallDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:" + driverModel.getPhone()));
                startActivity(phoneIntent);
            }
        });

        ivCancelOrder.setClickable(true);
        ivCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCancelDialog();
            }
        });
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        sellerMarker = mMap.addMarker(new MarkerOptions()
                .position(origin)
                .title(SellerModel.seller.getName())
                .icon(BitmapDescriptorFactory.fromBitmap(markerResize(R.drawable.resto_icon, 0))));

        driverMarker = mMap.addMarker(new MarkerOptions()
                .position(driverLoc)
                .title(driverModel.getName())
                .icon(BitmapDescriptorFactory.fromBitmap(markerResize(R.drawable.driver_icon, 0))));

        userMarker = mMap.addMarker(new MarkerOptions()
                .position(dest)
                .title(user.getNama())
                .icon(BitmapDescriptorFactory.fromBitmap(markerResize(R.drawable.ic_location, 1))));

        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));

        // get driver track from driver loc to seller loc
        GoogleDirection.withServerKey(apiKey)
                .from(driverLoc)
                .to(origin)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            driverOriginPosisionList = leg.getDirectionPoint();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.e("Google Direction", "Driver track failed");
                    }
                });
        // show polylines and move driver marker from origin to dest coordinate
        GoogleDirection.withServerKey(apiKey)
                .from(origin)
                .to(dest)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(final Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            originDestPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(
                                    getContext(), originDestPositionList, 5, Color.RED);
                            mMap.addPolyline(polylineOptions);

                            // moving driver marker animation from origin to dest
                            indexMarker = 0;
                            driverRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    driverMarker.setPosition(driverOriginPosisionList.get(indexMarker));
                                    if (DriverModel.cancelId % 2 == 1 && indexMarker == driverOriginPosisionList.size() / 4) {
                                        driverOriginHandler.removeCallbacks(this);
                                        driverMarker.remove();
                                        driverCancelDialog();
                                    } else if (indexMarker == driverOriginPosisionList.size() - 1) {
                                        driverOriginHandler.removeCallbacks(this);
                                        animateMarkerOriginToDest();
                                    } else {
                                        indexMarker += 1;
                                        driverOriginHandler.postDelayed(this, 1000);
                                    }
                                }
                            };
                            driverOriginHandler.postDelayed(driverRunnable, 500);
                        } else {
                            Toast.makeText(getContext(), "Direction failed to load", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(getContext(), "Direction failed to load", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // custom marker
    private Bitmap markerResize(int icon, int iconType) {
        int height = 100;
        int width = 100;
        Bitmap b, smallMarker;
        BitmapDrawable bitmapdraw;
        switch (iconType) {
            case 0:
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(icon);
                b = bitmapdraw.getBitmap();
                smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                break;
            case 1:
                Bitmap bitmapvec = getBitmapFromVectorDrawable(context, icon);
                smallMarker = Bitmap.createScaledBitmap(bitmapvec, 48, 48, false);
                break;

            default:
                bitmapdraw = (BitmapDrawable) getResources().getDrawable(icon);
                b = bitmapdraw.getBitmap();
                smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                break;
        }
        return smallMarker;
    }

    /**
     * TODO: retrieve closest driver location to seller
     *
     * @param driverModels all registered driver in database
     * @return closest Driver
     */
    private DriverModel getClosestDriverToSeller(ArrayList<DriverModel> driverModels) {
        Location sellerLocation = new Location("");
        sellerLocation.setLatitude(SellerModel.seller.getLatitude());
        sellerLocation.setLongitude(SellerModel.seller.getLongitude());

        for (DriverModel d : driverModels) {
            d.setRange(sellerLocation);
        }

        Collections.sort(driverModels, new Comparator<DriverModel>() {
            @Override
            public int compare(DriverModel o1, DriverModel o2) {
                return Double.compare(o1.getRange(), o2.getRange());
            }
        });
        DriverModel.cancelId++;
        return driverModels.get(0);
    }

    /**
     * TODO: Update database Transaction table
     */
    private void doPayment() {
        long grandTotal = trhModel.getGrandTotal() + trhModel.getDeliveryFee();
        if (trhModel.getPaymentMethod() == TransactionHeaderModel.GOPAY) {
            user.setGoPay(grandTotal * -1);
            user = userData.updateGoPay(user);
        } else if (trhModel.getPaymentMethod() == TransactionHeaderModel.GOPOINT) {
            user.setPoint(grandTotal * -1);
            user = userData.updatePoint(user);
        }
        trhModel.setStatusBayar(TransactionHeaderModel.PAID);
        trhModel = trhData.updateTransaction(trhModel);

        /**
         * TODO: Increase jumlah_order in table MSTSELLER
         */
        sellerData.updateTotalOrder(SellerModel.seller.getId());

        /**
         * TODO: Top up GoPoints if total cost (exclude delivery fee) is greater than 50000
         * get 5000 points for each 50000
         */
        int points = (int) (trhModel.getGrandTotal() / 50000) * 5000;
        user.setPoint(points);
        user = userData.updatePoint(user);
        SessionManager.with(context).updateSession(user);
    }

    // marker animation method
    private void animateMarkerOriginToDest() {
        indexMarker = 0;
        if (userCancelDialog != null) {
            userCancelDialog.dismiss();
        }
        ivCancelOrder.setClickable(false);
        ivCancelOrder.setVisibility(View.GONE);
        final Handler originDestHandler = new Handler();
        originDestHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                driverMarker.setPosition(originDestPositionList.get(indexMarker));
                if (indexMarker == originDestPositionList.size() - 1) {
                    driverArrivedDialog();
                } else {
                    indexMarker += 1;
                    originDestHandler.postDelayed(this, 1000);
                }
            }
        }, 5000);
    }

    // driver arrive dialog
    private void driverArrivedDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Arrived")
                .setMessage("Driver has arrived!")
                .setCancelable(false)
                .setNegativeButton("Oh Yeah!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doPayment();
                        ((OrderActivity) context).changeFragment(new ThankYouFragment());
                    }
                })
                .show();
    }

    // driver cancel dialog
    private void driverCancelDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Cancel")
                .setMessage("Driver has canceled your order!")
                .setCancelable(false)
                .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trhModel.setStatusBayar(TransactionHeaderModel.CANCEL);
                        trhData.updateTransaction(trhModel);
                        ((OrderActivity) context).finish();
                    }
                })
                .show();
    }

    // user cancel dialog
    private void userCancelDialog() {
        userCancelDialog = new AlertDialog.Builder(context)
                .setTitle("Canceled ?")
                .setMessage("Are you sure you want to cancel your order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trhModel.setStatusBayar(TransactionHeaderModel.CANCEL);
                        trhData.updateTransaction(trhModel);
                        ((OrderActivity) context).finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (driverRunnable != null) {
            driverOriginHandler.removeCallbacks(driverRunnable);
        }
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}

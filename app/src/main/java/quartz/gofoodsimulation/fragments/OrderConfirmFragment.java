package quartz.gofoodsimulation.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.OrderActivity;
import quartz.gofoodsimulation.data.SellerData;
import quartz.gofoodsimulation.data.TransactionDetailData;
import quartz.gofoodsimulation.data.TransactionHeaderData;
import quartz.gofoodsimulation.data.UserData;
import quartz.gofoodsimulation.models.CartModel;
import quartz.gofoodsimulation.models.GroceryModel;
import quartz.gofoodsimulation.models.SellerModel;
import quartz.gofoodsimulation.models.TransactionDetailModel;
import quartz.gofoodsimulation.models.TransactionHeaderModel;
import quartz.gofoodsimulation.models.UserModel;
import quartz.gofoodsimulation.utility.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Created by sxio on 24-May-17.
 */
public class OrderConfirmFragment extends Fragment {
    public static boolean searchingDriver_IsVisible = true;
    public static boolean backable = true;
    
    Context context;
    UserData userData;
    SellerData sellerData;
    TransactionHeaderData trhData;
    TransactionDetailData trdData;
    TransactionHeaderModel trhModel;
    ArrayList<TransactionDetailModel> trdModel;
    /* View Component */
    LinearLayout llOrderConfirm;
    Button btnOrdermore, btnOrder;
    TextView tvOrderitemName,
            tvOrderitemSubtotalPrice,
            tvOrderitemItemPrice,
            tvOrderTotal,
            tvOrderDeliveryFee,
            tvOrderGrandTotal,
            tvOrderUserAddr,
            tvOrderChangeUserAddr,
            tvOrderGoPay,
            tvOrderGoPoint;
    RadioButton rbGoPay,
            rbPoin,
            rbCash;
    long deliveryCost;
    long grandTotal;
    UserModel user;

    public OrderConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_confirm_layout, container, false);
        context = getContext();
        init(view);
        backable = true;
        rbCash.setChecked(true);

        user = SessionManager.with(context).getUserLoggedIn();

        // add toolbar to view
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.orderconfirm_toolbar, new ToolBarFragment(1, "CONFIRMATION"))
                .commit();

        loadingView();

        /**
         * TODO: add order item to view via looping
         */
        for (GroceryModel grocery : CartModel.cart.getGroceries()) {
            View order_item = inflater.inflate(R.layout.order_item, container, false);
            tvOrderitemName = (TextView) order_item.findViewById(R.id.orderitem_name);
            tvOrderitemItemPrice = (TextView) order_item.findViewById(R.id.orderitem_price);
            tvOrderitemSubtotalPrice = (TextView) order_item.findViewById(R.id.orderitem_subtotalprice);

            String item_name = grocery.getFood().getNameFood() + " (x" + grocery.getQuantity() + ")";
            tvOrderitemName.setText(item_name);
            tvOrderitemItemPrice.setText(String.format("Rp %,d", grocery.getFood().getPriceFood()));
            tvOrderitemSubtotalPrice.setText(String.format("Rp %,d", grocery.getPrice()));
            llOrderConfirm.addView(order_item);
        }
        btnOrdermore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        tvOrderChangeUserAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OrderActivity) context).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.order_activity, new OrderAddressFragment())
                        .addToBackStack(null).commit();
            }
        });

        LatLng deliveryLatLng = ((OrderActivity) context).getDeliveryLocation();

        // Set delivery address base on latitude and longitude
        String address = ((OrderActivity) context).getDeliveryAddress();
        tvOrderUserAddr.setText(address);

        tvOrderTotal.setText(String.format("Rp %,d", CartModel.cart.getTotalPrice()));

        // Calculate the delivery cost
        float deliveryRange = 0;
        DecimalFormat decFormat = new DecimalFormat("#.##");

        // delivery location
        Location deliveryLocation = new Location("");
        deliveryLocation.setLatitude(deliveryLatLng.latitude);
        deliveryLocation.setLongitude(deliveryLatLng.longitude);

        // seller location
        Location sellerLocation = new Location("");
        sellerLocation.setLatitude(SellerModel.seller.getLatitude());
        sellerLocation.setLongitude(SellerModel.seller.getLongitude());

        // calculate delivery cost
        deliveryRange = Float.valueOf(decFormat.format(sellerLocation.distanceTo(deliveryLocation) / 1000));
        deliveryCost = Math.round(deliveryRange) * 2000;
        tvOrderDeliveryFee.setText(String.format("Rp %,d", deliveryCost));

        tvOrderGoPay.setText(String.format("Rp %,d", user.getGoPay()));
        tvOrderGoPoint.setText(String.format("%,d pt", user.getPoint()));

        // Calculate grand total cost
        grandTotal = CartModel.cart.getTotalPrice() + deliveryCost;
        tvOrderGrandTotal.setText(String.format("Rp %,d", grandTotal));

        if (grandTotal > user.getGoPay()) {
            rbGoPay.setEnabled(false);
        }
        if (grandTotal > user.getPoint() || user.getPoint() < 10000) {
            rbPoin.setEnabled(false);
        }

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userData = new UserData(context);
                sellerData = new SellerData(context);
                trhData = new TransactionHeaderData(context);
                trdData = new TransactionDetailData(context);
                trhModel = new TransactionHeaderModel(user.getId(), SellerModel.seller.getId(),
                        CartModel.cart.getTotalPrice(), TransactionHeaderModel.UNPAID, 0, deliveryCost);
                /**
                 * TODO: insert order to database's TRANSACTION table
                 */
                trhModel = trhData.insertTransaction(trhModel);
                trdModel = new ArrayList<>();
                for (GroceryModel grocery : CartModel.cart.getGroceries()) {
                    TransactionDetailModel trd = new TransactionDetailModel(
                            trhModel.getIdTransaksi(),
                            grocery.getFood().getIdFood(),
                            grocery.getQuantity(),
                            grocery.getFood().getPriceFood()
                    );
                    trdModel.add(trd);
                }
                trdData.insertTransaction(trdModel);

                if (rbGoPay.isChecked()) {
                    trhModel.setPaymentMethod(TransactionHeaderModel.GOPAY);
                } else if (rbPoin.isChecked()) {
                    trhModel.setPaymentMethod(TransactionHeaderModel.GOPOINT);
                } else {
                    trhModel.setPaymentMethod(TransactionHeaderModel.CASH);
                }

                CartModel.cart.clearCart();
                SearchingDriverFragment sdf = new SearchingDriverFragment();
                Bundle b = new Bundle();
                b.putSerializable("TRANSACTION_HEADER", trhModel);
                backable = false;
                sdf.setArguments(b);
                ((OrderActivity) context).changeFragment(sdf);
            }
        });
        return view;
    }

    private void init(View view) {
        llOrderConfirm = (LinearLayout) view.findViewById(R.id.orderconfirm_ll_orders);
        tvOrderUserAddr = (TextView) view.findViewById(R.id.orderconfirm_tv_useraddr);
        tvOrderChangeUserAddr = (TextView) view.findViewById(R.id.orderconfirm_tv_changeuseraddr);
        btnOrdermore = (Button) view.findViewById(R.id.orderconfirm_btn_more);
        tvOrderTotal = (TextView) view.findViewById(R.id.orderconfirm_tv_total);
        tvOrderDeliveryFee = (TextView) view.findViewById(R.id.orderconfirm_tv_deliveryfee);
        tvOrderGrandTotal = (TextView) view.findViewById(R.id.orderconfirm_tv_grandtotal);
        tvOrderGoPay = (TextView) view.findViewById(R.id.orderconfirm_tv_gopay);
        tvOrderGoPoint = (TextView) view.findViewById(R.id.orderconfirm_tv_gopoints);
        btnOrder = (Button) view.findViewById(R.id.orderconfirm_btn_order);
        rbGoPay = (RadioButton) view.findViewById(R.id.orderconfirm_rb_goPay);
        rbPoin = (RadioButton) view.findViewById(R.id.orderconfirm_rb_gopoints);
        rbCash = (RadioButton) view.findViewById(R.id.orderconfirm_rb_cash);
    }

    private void loadingView(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        final Fragment thisFragment = this;
        getFragmentManager().beginTransaction().hide(thisFragment).commit();
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                getFragmentManager().beginTransaction().show(thisFragment).commit();
            }
        }, 2500);
    }

}

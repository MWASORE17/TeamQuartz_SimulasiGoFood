package quartz.gofoodsimulation.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.adapters.SellerMenuVPAdapter;
import quartz.gofoodsimulation.data.FoodCategoryData;
import quartz.gofoodsimulation.fragments.QuickReviewFragment;
import quartz.gofoodsimulation.models.CartModel;
import quartz.gofoodsimulation.models.SellerModel;

public class SellerActivity extends ParentActivity {
    public static QuickReviewFragment quickReview;
    public static AppBarLayout abl;
    public static NestedScrollView nsv;
    /*view component*/
    TextView tvSellerAddr,
            tvSellerStatus,
            tvSellerTime,
            tvSellerDetail;
    ImageView ivCoverPicture;
    ViewPager vp;
    Toolbar toolbar;
    TabLayout tabLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_page_layout);
        toolbar = (Toolbar) findViewById(R.id.sellerpage_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setContentInsetsAbsolute(0, 150);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        init();

        loadingView();
    }

    /**
     * View component instantiate
     */
    private void init() {
        SellerModel.seller = (SellerModel) getIntent().getExtras().get("SELLER");
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/contb.ttf");

        /* used in Recycle View & Order*/
        if (CartModel.cart == null) {
            CartModel.cart = new CartModel();
        }
        CartModel.cart.setIdSeller(SellerModel.seller.getId());

        tvSellerAddr = (TextView) findViewById(R.id.sellerpage_tv_restoaddr);
        tvSellerStatus = (TextView) findViewById(R.id.sellerpage_tv_status);
        tvSellerTime = (TextView) findViewById(R.id.sellerpage_tv_time);
        tvSellerDetail = (TextView) findViewById(R.id.sellerpage_tv_detail);
        ivCoverPicture = (ImageView) findViewById(R.id.sellerpage_iv_coverpicture);
        abl = (AppBarLayout) findViewById(R.id.sellerpage_appBarLayout);
        nsv = (NestedScrollView) findViewById(R.id.sellerpage_nsv);
        // load image
        try {
            InputStream ims = getAssets().open("sellerCoverPhoto/" + SellerModel.seller.getCoverPhoto());
            Drawable d = Drawable.createFromStream(ims, null);
            ivCoverPicture.setImageDrawable(d);
        } catch (IOException ex) {
            return;
        }
        tvSellerAddr.setText(SellerModel.seller.getAddress());
        tvSellerDetail.setClickable(true);

        tvSellerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abl.setVisibility(View.INVISIBLE);
                nsv.setVisibility(View.INVISIBLE);
                final ProgressDialog progressDialog = new ProgressDialog(SellerActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                            progressDialog.dismiss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                Intent _intent = new Intent(SellerActivity.this, SellerDetailActivity.class);
                _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(_intent);
            }
        });

        if (SellerModel.seller.getStatus() == 1) {
            tvSellerStatus.setText("OPEN");
            tvSellerStatus.setTextColor(getResources().getColor(R.color.green));
            tvSellerStatus.setBackground(getResources().getDrawable(R.drawable.seller_status_open_style));
        } else {
            tvSellerStatus.setText("CLOSED");
            tvSellerStatus.setTextColor(getResources().getColor(R.color.red));
            tvSellerStatus.setBackground(getResources().getDrawable(R.drawable.seller_status_closed_style));
        }
        tvSellerTime.setText(SellerModel.seller.getTime());

        if (vp == null) {
            SellerMenuVPAdapter sellerMenuVPAdapter = new SellerMenuVPAdapter(getSupportFragmentManager(), this);
            vp = (ViewPager) findViewById(R.id.sellerpage_vp_menu);
            vp.setOffscreenPageLimit((new FoodCategoryData(this)).getAllFoodCategory().size());
            vp.setAdapter(sellerMenuVPAdapter);
        }

        tabLayout = (TabLayout) findViewById(R.id.sellerpage_tl_category);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vp);
        tabLayout.setSelectedTabIndicatorHeight(10);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.white));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.red));

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.sellerpage_collapsingtoolbarlayout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.sellerpage_appBarLayout);
        appBarLayout.setExpanded(true, true);
        collapsingToolbarLayout.setTitle(SellerModel.seller.getName());
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedTheme);
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTheme);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
    }

    /**
     * TODO: adjust viewpager height when Quick Review Fragment show / hide
     *
     * @param isAdd identifier to add or to subtract
     */
    public void addSubtractViewPagerMargin(boolean isAdd) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) vp.getLayoutParams();

        /* convert dp unit to px*/
        Resources r = vp.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 79, r.getDisplayMetrics());

        if (isAdd) {
            layoutParams.bottomMargin += px;
        } else {
            layoutParams.bottomMargin -= px;
        }
        vp.setLayoutParams(layoutParams);
    }

    @Override
    protected void onStart() {
        super.onStart();
        quickReview = (QuickReviewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.sellerpage_bs_quickreview);
        if (CartModel.cart.getTotalQuantity() == 0) {
            quickReview.hide();
        } else {
            addSubtractViewPagerMargin(true);
        }
        quickReview.setPrice(CartModel.cart.getTotalPrice());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SellerMenuVPAdapter sellerMenuVPAdapter = new SellerMenuVPAdapter(getSupportFragmentManager(), this);
        vp = (ViewPager) findViewById(R.id.sellerpage_vp_menu);
        vp.setOffscreenPageLimit((new FoodCategoryData(this)).getAllFoodCategory().size());
        vp.setAdapter(sellerMenuVPAdapter);
        addSubtractViewPagerMargin(false);
    }

    /**
     * TODO: loading view
     */
    private void loadingView() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        quickReview = (QuickReviewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.sellerpage_bs_quickreview);

        collapsingToolbarLayout.setTitle("");
        tvSellerAddr.setVisibility(View.INVISIBLE);
        tvSellerDetail.setVisibility(View.INVISIBLE);
        tvSellerStatus.setVisibility(View.INVISIBLE);
        tvSellerTime.setVisibility(View.INVISIBLE);
        ivCoverPicture.setVisibility(View.INVISIBLE);
        vp.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        if (HistoryActivity.isReOrder == true || CartModel.cart.getTotalQuantity() > 0) {
            getSupportFragmentManager().beginTransaction().hide(quickReview).commit();
        }
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                tvSellerAddr.setVisibility(View.VISIBLE);
                tvSellerDetail.setVisibility(View.VISIBLE);
                tvSellerStatus.setVisibility(View.VISIBLE);
                tvSellerTime.setVisibility(View.VISIBLE);
                ivCoverPicture.setVisibility(View.VISIBLE);
                vp.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                if (HistoryActivity.isReOrder == true || CartModel.cart.getTotalQuantity() > 0) {
                    getSupportFragmentManager().beginTransaction().show(quickReview).commit();
                    HistoryActivity.isReOrder = false;
                }
                collapsingToolbarLayout.setTitle(SellerModel.seller.getName());

            }
        }, 2500);
    }
}

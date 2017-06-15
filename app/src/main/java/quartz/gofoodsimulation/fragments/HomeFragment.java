package quartz.gofoodsimulation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.activity.ParentActivity;
import quartz.gofoodsimulation.adapters.MainAdsVPAdapter;
import quartz.gofoodsimulation.data.FoodGroupData;
import quartz.gofoodsimulation.models.FoodGroupModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    public static final String CATEGORY = "category";
    public static ArrayList<String> images = new ArrayList<>();
    /**
     * LISTTYPE untuk membedakan tombol-tombol
     * 0: Near Me
     * 1: 24 Hours
     * 2: Best Seller
     * 3: New Seller
     * 4: Typing search
     */
    public static int LISTTYPE = 5;
    ViewPager viewPager;
    MainAdsVPAdapter adapter;
    LinearLayout nearMe, hours24, bestSellers, newSeller;
    FrameLayout healthy, nasi, ayamBebek, snack, minuman, fastfood, japanese, korean, roti, seafood, chinese, martabak;

    FoodGroupData foodGroupData;
    ArrayList<FrameLayout> categoryView;
    ArrayList<FoodGroupModel> foodGroupModels;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_layout, container, false);
        init(view);

        images.clear();
        images.add("http://3.bp.blogspot.com/-_BiTH_oLsBw/VjsT1ptG1lI/AAAAAAAABMM/u8T67yWuXL4/s1600/go-food-daftar.jpg");
        images.add("https://www.maxmanroe.com/wp-content/uploads/2015/04/Go-Foodd.jpg");
        images.add("http://www.jawapos.com/uploads/imgs/2016/04/23655_41731_Kompetisi%20Video%20Gojek.jpg");
        images.add("http://www.jawapos.com/uploads/imgs/2016/05/30124_48385_TOKOPEDIA%20GO-KILAT.jpg");

        event();

        adapter = new MainAdsVPAdapter(this.getContext());
        adapter.setImage(images);
        viewPager.setAdapter(adapter);
        return view;
    }

    private void init(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.home_viewpager);
        nearMe = (LinearLayout) view.findViewById(R.id.home_nearme);
        hours24 = (LinearLayout) view.findViewById(R.id.home_24hours);
        bestSellers = (LinearLayout) view.findViewById(R.id.home_bestseller);
        newSeller = (LinearLayout) view.findViewById(R.id.home_new);

        healthy = (FrameLayout) view.findViewById(R.id.home_healthy_category);
        nasi = (FrameLayout) view.findViewById(R.id.home_anekaNasi_category);
        ayamBebek = (FrameLayout) view.findViewById(R.id.home_ayamBebek_category);
        snack = (FrameLayout) view.findViewById(R.id.home_snackJajanan_category);
        minuman = (FrameLayout) view.findViewById(R.id.home_minuman_category);
        fastfood = (FrameLayout) view.findViewById(R.id.home_fastfood_category);
        japanese = (FrameLayout) view.findViewById(R.id.home_japanese_category);
        korean = (FrameLayout) view.findViewById(R.id.home_korean_category);
        roti = (FrameLayout) view.findViewById(R.id.home_roti_category);
        seafood = (FrameLayout) view.findViewById(R.id.home_seafood_category);
        chinese = (FrameLayout) view.findViewById(R.id.home_chinese_category);
        martabak = (FrameLayout) view.findViewById(R.id.home_martabak_category);

        nearMe.setClickable(true);
        hours24.setClickable(true);
        bestSellers.setClickable(true);
        newSeller.setClickable(true);

        categoryView = new ArrayList<>();
        categoryView.add(healthy);
        categoryView.add(nasi);
        categoryView.add(ayamBebek);
        categoryView.add(snack);
        categoryView.add(minuman);
        categoryView.add(fastfood);
        categoryView.add(japanese);
        categoryView.add(korean);
        categoryView.add(roti);
        categoryView.add(seafood);
        categoryView.add(chinese);
        categoryView.add(martabak);

        foodGroupData = new FoodGroupData(getContext());
        foodGroupModels = foodGroupData.getAllFoodGroup();
    }

    private void event() {
        nearMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LISTTYPE = 0;
                sendToSellerListFragment();
            }
        });

        hours24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LISTTYPE = 1;
                sendToSellerListFragment();
            }
        });

        bestSellers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LISTTYPE = 2;
                sendToSellerListFragment();
            }
        });

        newSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LISTTYPE = 3;
                sendToSellerListFragment();
            }
        });

        for (int cv = 0; cv < categoryView.size(); cv++) {
            final String category = foodGroupModels.get(cv).getCodeGroup();
            categoryView.get(cv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LISTTYPE = 4;
                    SellerListFragment sellerListFragment = new SellerListFragment();
                    Bundle b = new Bundle();
                    b.putString(SearchBarFragment.SEARCH_KEY, "");
                    b.putString(CATEGORY, category);
                    SearchBarFragment.searchCategory = category;
                    sellerListFragment.setArguments(b);
                    ((ParentActivity) getActivity()).changeFragment(R.id.home, sellerListFragment);
                }
            });
        }
    }

    private void sendToSellerListFragment() {
        SellerListFragment sellerListFragment = new SellerListFragment();
        Bundle b = new Bundle();
        b.putString(SearchBarFragment.SEARCH_KEY, "");
        sellerListFragment.setArguments(b);
        ((ParentActivity) getActivity()).changeFragment(R.id.home, sellerListFragment);
    }
}

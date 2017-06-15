package quartz.gofoodsimulation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.adapters.SellerMenuRVAdapter;
import quartz.gofoodsimulation.data.FoodData;
import quartz.gofoodsimulation.models.FoodModel;
import quartz.gofoodsimulation.models.SellerModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerMenuFragment extends Fragment {
    private RecyclerView rv;
    private SellerMenuRVAdapter adapter;
    private FoodData fd;
    private ArrayList<FoodModel> food;
    private String foodCategoryCode;

    public SellerMenuFragment(String foodCategoryCode) {
        this.foodCategoryCode = foodCategoryCode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_menu_layout, container, false);

        adapter = new SellerMenuRVAdapter(container.getContext());
        rv = (RecyclerView) view.findViewById(R.id.sellermenu_rv);

        /* setting */
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        String[] arg = {Long.toString(SellerModel.seller.getId()), foodCategoryCode};

        fd = new FoodData(getContext());
        food = fd.getFoodByIdSeller(arg);
        adapter.setFood(food);
        rv.setAdapter(adapter);
        return view;
    }
}

package quartz.gofoodsimulation.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Calendar;

import quartz.gofoodsimulation.R;
import quartz.gofoodsimulation.adapters.SellerListRVAdapter;
import quartz.gofoodsimulation.data.SellerData;
import quartz.gofoodsimulation.models.SellerModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerListFragment extends Fragment {

    /* data */
    ArrayList<SellerModel> sellerModel;
    SellerData sellerData;
    String searchValue, categoryValue;
    /* view component */
    public RecyclerView rv;
    private SellerListRVAdapter adapter;
    public View bannerWK;

    ProgressBar pb;

    public SellerListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_list_layout, container, false);

        init(view);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        pb.setVisibility(View.INVISIBLE);
        bannerWK.setVisibility(View.GONE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(View.VISIBLE);
            }
        }, 1000);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(View.INVISIBLE);
                switch (HomeFragment.LISTTYPE) {
                    // Near Me
                    case 0:
                        if (searchValue.matches("")) {
                            sellerModel = sellerData.getAllSellers(0, "");
                            adapter.setSeller(sellerModel);
                        } else {
                            sellerModel = (sellerData.getMatchSellers(searchValue, 0, ""));
                        }
                        break;

                    // 24 Hours
                    case 1:
                        ArrayList<SellerModel> hours24 = new ArrayList<>();
                        hours24.clear();
                        if (searchValue.matches("")) {
                            sellerModel = sellerData.getAllSellers(1, "");
                            for(SellerModel sm : sellerModel){
                                if(sm.getTime().matches("00.00 - 23.59")){
                                    hours24.add(sm);
                                }
                            }
                            sellerModel = hours24;
                            adapter.setSeller(sellerModel);
                        } else {
                            sellerModel = (sellerData.getMatchSellers(searchValue, 1, ""));
                            for(SellerModel sm : sellerModel){
                                if(sm.getTime().matches("00.00 - 23.59")){
                                    hours24.add(sm);
                                }
                            }
                            sellerModel = hours24;
                        }
                        break;

                    // Best Seller
                    case 2:
                        if (searchValue.matches("")) {
                            sellerModel = sellerData.getAllSellers(2, "");
                            adapter.setSeller(sellerModel);
                        } else {
                            sellerModel = (sellerData.getMatchSellers(searchValue, 2, ""));
                        }
                        break;

                    //New
                    case 3:
                        ArrayList<SellerModel> newSeller = new ArrayList<>();
                        newSeller.clear();
                        Calendar calendar = Calendar.getInstance();
                        if (searchValue.matches("")) {
                            sellerModel = sellerData.getAllSellers(3, "");
                            for(SellerModel sm : sellerModel){
                                if (Math.abs(calendar.getTime().getMonth() - sm.getJoinDate().getMonth()) <= 2 && calendar.getTime().getYear() == sm.getJoinDate().getYear()) {
                                    newSeller.add(sm);
                                }
                            }
                            sellerModel = newSeller;
                            adapter.setSeller(sellerModel);
                        } else {
                            sellerModel = (sellerData.getMatchSellers(searchValue, 3, ""));
                            for(SellerModel sm : sellerModel){
                                if (Math.abs(calendar.getTime().getMonth() - sm.getJoinDate().getMonth()) <= 2 && calendar.getTime().getYear() == sm.getJoinDate().getYear()) {
                                    newSeller.add(sm);
                                }
                            }
                            sellerModel = newSeller;
                        }
                        break;

                    //Category
                    case 4:
                        if (searchValue.matches("")) {
                            sellerModel = sellerData.getAllSellers(4, categoryValue);
                            adapter.setSeller(sellerModel);
                        } else {
                            sellerModel = (sellerData.getMatchSellers(searchValue, 4, categoryValue));
                        }
                        break;

                    // Normal Search
                    default:
                        sellerModel = (sellerData.getMatchSellers(searchValue, 5, ""));
                }
                searchList(sellerModel);
                rv.setAdapter(adapter);
            }
        }, 2000);

        return view;
    }

    private void init(View view) {
        adapter = new SellerListRVAdapter();
        searchValue = getArguments().getString(SearchBarFragment.SEARCH_KEY);
        categoryValue = getArguments().getString(SearchBarFragment.SEARCH_CATEGORY);
        rv = (RecyclerView) view.findViewById(R.id.sellerlist_rv);
        bannerWK = (View) view.findViewById(R.id.sellerlist_banner_wrongkeyword);
        sellerData = new SellerData(getContext());
        pb = (ProgressBar) view.findViewById(R.id.sellerlist_pb);
    }

    private void searchList(ArrayList<SellerModel> sellerModel) {
        if (sellerModel.isEmpty()) {
            bannerWK.setVisibility(View.VISIBLE);
        } else if (searchValue.matches("")) {
            bannerWK.setVisibility(View.GONE);
        } else {
            bannerWK.setVisibility(View.GONE);
            adapter.setSeller(sellerModel);
        }
    }
}

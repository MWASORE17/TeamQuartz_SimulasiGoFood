package quartz.gofoodsimulation.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import quartz.gofoodsimulation.data.FoodCategoryData;
import quartz.gofoodsimulation.fragments.SellerMenuFragment;
import quartz.gofoodsimulation.models.FoodCategoryModel;
import quartz.gofoodsimulation.models.SellerModel;

/**
 * Created by kened on 5/9/2017.
 * Modified by:
 * sxio - 16-May-17: tambah algoritma memanfaatkan 1 fragment untuk smua viewpager
 */

public class SellerMenuVPAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    public SellerMenuVPAdapter(FragmentManager fm, Context context) {
        super(fm);
        FoodCategoryData fcd = new FoodCategoryData(context);
        ArrayList<FoodCategoryModel> fc = fcd.getSellerFoodCategory(Long.toString(SellerModel.seller.getId()));
        for (FoodCategoryModel cat : fc) {
            this.addFragment(new SellerMenuFragment(cat.getCodeCategory()), cat.getNameCategory());
        }
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

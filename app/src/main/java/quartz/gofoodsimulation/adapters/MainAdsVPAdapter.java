package quartz.gofoodsimulation.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import quartz.gofoodsimulation.R;

/**
 * Created by kened on 3/23/2017.
 */

public class MainAdsVPAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> images;
    ImageView IvAds;

    public MainAdsVPAdapter(Context context) {
        this.context = context;
        this.images = new ArrayList<>();
    }

    public void setImage(ArrayList<String> images) {
        this.images = images;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewpager_item, container, false);
        IvAds = (ImageView) view.findViewById(R.id.vp_imageview);
        Picasso.with(context)
                .load(images.get(position))
                .into(IvAds);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public float getPageWidth(int position) {
        return (3f/4f);
    }
}

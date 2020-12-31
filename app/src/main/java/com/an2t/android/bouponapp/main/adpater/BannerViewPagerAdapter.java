package com.an2t.android.bouponapp.main.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.model.GetSpotlightImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.an2t.android.bouponapp.services.RetrofitSingleton.BASE_IMAGE_URL;


/**
 * Created by anantawasthy on 7/3/17.
 */

public class BannerViewPagerAdapter extends PagerAdapter {
    private ArrayList<GetSpotlightImages.Item> bannerList;
    private Context mContext;
    private LayoutInflater inflater;
    private OnSpotLightItemClick onSpotLightClickedListener;

    public BannerViewPagerAdapter(ArrayList<GetSpotlightImages.Item> bannerList, Context mContext,OnSpotLightItemClick onSpotLightClickedListener) {
        this.bannerList = bannerList;
        this.mContext = mContext;
        this.onSpotLightClickedListener = onSpotLightClickedListener;
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final GetSpotlightImages.Item imageBannerData = bannerList.get(position);

        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_banner_list,null);

        ImageView img_main_rest_slider = (ImageView)view.findViewById(R.id.img_main_rest_slider);
        FrameLayout frame_main= (FrameLayout) view.findViewById(R.id.frame_main);

        imageBannerData.getMainImage();

        Picasso.with(mContext)
                .load(imageBannerData.getMainImage() + (position) + imageBannerData.getKey())
//                .resize(520,470)
                .placeholder(R.drawable.view_pager_banner_back)
                .into(img_main_rest_slider);

        onItemClicked(frame_main,imageBannerData);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view,0);
        return view;
    }

    private void onItemClicked(FrameLayout frame_main, final GetSpotlightImages.Item imageBannerData) {
        frame_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSpotLightClickedListener.onSpotLightItemClicked(imageBannerData);
            }
        });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
    public interface OnSpotLightItemClick{
        void onSpotLightItemClicked(GetSpotlightImages.Item imageBannerData);
    }

}

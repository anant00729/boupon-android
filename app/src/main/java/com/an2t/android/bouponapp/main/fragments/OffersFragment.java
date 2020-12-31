package com.an2t.android.bouponapp.main.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.adpater.VPOfferChildAdapter;
import com.an2t.android.bouponapp.recharge.FreeDealsRechargeActivity;
import com.an2t.android.bouponapp.recharge.fragment_free_deals.FreeDealsRechargeFragment;
import com.an2t.android.bouponapp.recharge.model.FreeDealsRes;
import com.an2t.android.bouponapp.services.RetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.an2t.android.bouponapp.services.TrackGPS;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static com.an2t.android.bouponapp.recharge.model.FreeDealsRes.Item.distanceComparator;

public class OffersFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    private static final String TAG = "OffersFragment";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private TabLayout tab_items;
    private ViewPager vp_items;
    private VPOfferChildAdapter adapter;

    private FreeDealsRechargeActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    private ArrayList<FreeDealsRes.Item> mAll;
    private String authToken;
    private OnItemSelectedOffer mOnItemSelectedOffer;
    private ProgressBar progress_bar;
    private StringBuilder sb_token;
    private TrackGPS mTrackGPS;



    public void onAttachToParentFragment(Activity activity)
    {
        try
        {   if(mOnItemSelectedOffer instanceof FreeDealsRechargeActivity){
            mOnItemSelectedOffer = (OnItemSelectedOffer)activity;
            }

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    activity.toString() + " must implement OnItemSelecetedFreeDealsActvity");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getSharedPrefData();

        View v =inflater.inflate(R.layout.fragment_offers, container, false);

        onAttachToParentFragment(getActivity());

        mAll = new ArrayList<>();
        mSectionsPagerAdapter = new FreeDealsRechargeActivity.SectionsPagerAdapter(getActivity().getSupportFragmentManager(),mAll);
        // init Component
        tab_items = (TabLayout)v.findViewById(R.id.tab_items);
        progress_bar= (ProgressBar) v.findViewById(R.id.progress_bar);
        vp_items= (ViewPager) v.findViewById(R.id.vp_items);


        loadData();
        return v;
    }

    private void getSharedPrefData() {
        SharedPreferences sp = getContext().getSharedPreferences(getString(R.string.spFileUser), getContext().MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken),"");
        sb_token = new StringBuilder("Bearer ")
                .append(authToken);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListeners();

    }

    private void setListeners() {
        adapter = new VPOfferChildAdapter(getChildFragmentManager());
        vp_items.setAdapter(adapter);

        vp_items.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Fragment frag = mSectionsPagerAdapter.fragments[position];
                if (frag != null && frag instanceof FreeDealsRechargeFragment) {
                    ((FreeDealsRechargeFragment)frag).getData(mAll,position);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_items.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_items));
        tab_items.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp_items));
    }

    private void loadData() {

        Log.e(TAG, "loadData: Offer Fragment");

        showProgressDialog();

            Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();
            ServicesAPI service = mRetrofit.create(ServicesAPI.class);

            Call<FreeDealsRes> call = service.getAllFreeDealsForRecharge(sb_token.toString());

            call.enqueue(new Callback<FreeDealsRes>() {
                @Override
                public void onResponse(Call<FreeDealsRes> call, Response<FreeDealsRes> response) {

                    FreeDealsRes res = response.body();
                    if (res != null) {
                        processData(res);
                    }
                }

                @Override
                public void onFailure(Call<FreeDealsRes> call, Throwable t) {
                    hideProgreeDialog();

                }
            });
        }
    private void processData(FreeDealsRes res) {
        if (res.getItems() != null) {
            mAll.addAll(res.getItems());
            for (FreeDealsRes.Item item : mAll) {
                if (item.getCategory().equals("foodsandcafe")) {
                    item.setPosition("1");
                } else if (item.getCategory().equals("lifestyle")) {
                    item.setPosition("2");
                } else if (item.getCategory().equals("beautyandsaloon")) {
                    item.setPosition("3");
                } else if (item.getCategory().equals("electronics")) {
                    item.setPosition("4");
                } else if (item.getCategory().equals("outdoorandsports")) {
                    item.setPosition("5");
                }
            }
            calculateDistance(mAll);
        }
    }
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        String tabTitles[] = new String[] { "ALL", "FOODS & CAFE", "LIFE STYLE", "BEAUTY & SALOON", "ELECTRONICS", "OUTDOORS AND SPORTS"};
        private static final String TAG = "SectionsPagerAdapter";
        ArrayList<FreeDealsRes.Item> mAll;
        public Fragment[] fragments = new Fragment[tabTitles.length];

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<FreeDealsRes.Item> mAll) {
            super(fm);
            this.mAll = mAll;
        }

        @Override
        public Fragment getItem(int position) {
            return FreeDealsRechargeFragment.newInstance(/*mAll*/);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FreeDealsRechargeFragment createdFragment = (FreeDealsRechargeFragment) super.instantiateItem(container, position);
            fragments[position]  = createdFragment;
            return createdFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }



    public interface OnItemSelectedOffer{
        void onItemSelectedOffer(FreeDealsRes.Item item, boolean selected);

    }

    private void showProgressDialog() {
        progress_bar.setVisibility(View.VISIBLE);
        vp_items.setVisibility(View.GONE);
        tab_items.setVisibility(View.GONE);

    }

    private void hideProgreeDialog() {
        progress_bar.setVisibility(View.GONE);
        vp_items.setVisibility(View.VISIBLE);
        tab_items.setVisibility(View.VISIBLE);

    }

    private void calculateDistance(ArrayList<FreeDealsRes.Item> mAll) {



        SharedPreferences sp = getContext().getSharedPreferences(getString(R.string.sp_location_file), MODE_PRIVATE);

        mTrackGPS = new TrackGPS(getActivity());

        SharedPreferences.Editor editor = sp.edit();


        Log.e(TAG, "saveUserLocationInSharedPref: " + mTrackGPS.getLatitude());
        Log.e(TAG, "saveUserLocationInSharedPref: " + mTrackGPS.getLongitude());

        editor.putString(getString(R.string.lat), String.valueOf(mTrackGPS.getLatitude()));
        editor.putString(getString(R.string.lng), String.valueOf(mTrackGPS.getLongitude()));

        editor.apply();



        String userLatStr = sp.getString(getString(R.string.lat), "");
        String userLngStr = sp.getString(getString(R.string.lng), "");


        if(!userLatStr.isEmpty() && !userLngStr.isEmpty() && !userLatStr.equals("0.0") && !userLngStr.equals("0.0") ) {
            Log.e(TAG, "calculateDistance: " + userLatStr + "\t " + userLngStr);

            Double userLat = Double.parseDouble(userLatStr);
            Double userLng = Double.parseDouble(userLngStr);


            for (FreeDealsRes.Item item : mAll) {

                double dist = distance(userLng, userLat, item.getLocation().get(0), item.getLocation().get(1), 'K');
                NumberFormat formatter = new DecimalFormat("##.000");
                String dis = formatter.format(dist);
                item.setDistance(dis);

            }

            Collections.sort(mAll, distanceComparator);
        }
            this.mAll = mAll;
            vp_items.setAdapter(mSectionsPagerAdapter);
            tab_items.setupWithViewPager(vp_items);
            vp_items.setOffscreenPageLimit(vp_items.getAdapter().getCount());
            hideProgreeDialog();

    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/*::  This function converts decimal degrees to radians             :*/
/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/*::  This function converts radians to decimal degrees             :*/
/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void seeFieldArea(String name){
        name = name.toLowerCase();

        Log.e(TAG, "seeFieldArea: " + (mAll == null));

        ArrayList<FreeDealsRes.Item> mAllNew = new ArrayList<>();

        if (!name.isEmpty()){
            mAllNew.clear();
            for (FreeDealsRes.Item item : mAll) {

                if(item.getAreaName().toLowerCase().equals(name) || item.getCity().toLowerCase().equals(name)){
                    Log.e(TAG, "seeFieldArea: " + item.getTitle() + "\t" + item.getAreaName());
                    Log.e(TAG, "seeCity: " + item.getCity() + "\t" + item.getCity());
                    mAllNew.add(item);
                }
            }
        }else {
            mAllNew.clear();
            mAllNew.addAll(mAll);
        }

        vp_items.setCurrentItem(0);

        Fragment frag = mSectionsPagerAdapter.fragments[0];
        if (frag != null && frag instanceof FreeDealsRechargeFragment) {
            ((FreeDealsRechargeFragment)frag).getData(mAllNew,0);
        }
        vp_items.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Fragment frag = mSectionsPagerAdapter.fragments[position];
                if (frag != null && frag instanceof FreeDealsRechargeFragment) {
                    ((FreeDealsRechargeFragment)frag).getData(mAllNew,position);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAll != null){
            mAll.clear();
            mAll = null;
        }
    }
}

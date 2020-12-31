package com.an2t.android.bouponapp.recharge.fragment_free_deals;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.recharge.adapter.FreeDealsAdapter;
import com.an2t.android.bouponapp.recharge.adapter.VegaLayoutManager;
import com.an2t.android.bouponapp.recharge.model.FreeDealsRes;
import com.an2t.android.bouponapp.services.RetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;

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

/**
 * Created by anantawasthy on 9/28/17.
 */

public class FreeDealsRechargeFragment extends Fragment implements FreeDealsAdapter.OnSelectItemClickFreeDealsRecharge {


    private static final String TAG = "FreeDealsRechargeFragme";
    private ArrayList<FreeDealsRes.Item> mSelectedList = new ArrayList<>();
    private FreeDealsAdapter adapter;
    public OnItemSelecetedFreeDealsActvity OnItemSelecetedFreeDealsActvity;
    private String authToken;
    private ProgressBar progress_bar;
    private StringBuilder sb_token;
    private boolean isFirstTimeLoaded;
    private SharedPreferences sp;
    private static int count = 1;
    private RelativeLayout no_items;
    private SharedPreferences splocation;
    private String userLatStr,userLngStr;


    public void onAttachToParentFragment(Activity activity) {
        try {
            OnItemSelecetedFreeDealsActvity = (OnItemSelecetedFreeDealsActvity) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnItemSelecetedFreeDealsActvity");
        }
    }

    public FreeDealsRechargeFragment() {
    }

    public static FreeDealsRechargeFragment newInstance() {
        FreeDealsRechargeFragment fragment = new FreeDealsRechargeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    private RecyclerView rv_deals_free;
    private ArrayList<FreeDealsRes.Item> mAll;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_deals_recharge, container, false);

        onAttachToParentFragment(getActivity());


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
            return;

        getSharedPrefData();

        rv_deals_free = (RecyclerView) getView().findViewById(R.id.rv_deals_free);
        progress_bar = (ProgressBar) getView().findViewById(R.id.progress_bar);
        no_items = (RelativeLayout) getView().findViewById(R.id.no_items);
        rv_deals_free.setHasFixedSize(true);
        VegaLayoutManager lm = new VegaLayoutManager();
        rv_deals_free.setLayoutManager(lm);

        /*mAll = getArguments().getParcelableArrayList(PARCELABELE_ALL);*/

        Log.e(TAG, "onActivityCreated: " );
        Log.e(TAG, "count : " +  count);
        if(count == 1){
            loadData();

        }else {
            hideProgreeDialog();
        }
        ++count;
    }

    public void getData(ArrayList<FreeDealsRes.Item> mAll, int position) {



        mSelectedList.clear();
        for (FreeDealsRes.Item item : mAll) {
            if (item.getPosition().equals(String.valueOf(position))) {
                mSelectedList.add(item);
            }

        }

        if (position == 0) {
            mSelectedList.addAll(mAll);
        }

        Log.e(TAG, "getData: " + mSelectedList.size() );

        if(mSelectedList.size() == 0){
            no_items.setVisibility(View.VISIBLE);
            rv_deals_free.setVisibility(View.GONE);
            Log.e(TAG, "if " + mSelectedList.size() );
            return;
        }else {
            no_items.setVisibility(View.GONE);
            rv_deals_free.setVisibility(View.VISIBLE);
            Log.e(TAG, "else " + mSelectedList.size() );
        }

        adapter = new FreeDealsAdapter(getContext(), mSelectedList, this);
        rv_deals_free.setAdapter(adapter);

    }

    @Override
    public void onSelectItemClickedFreeDealsRecharge(boolean selected, FreeDealsRes.Item item) {

        /*if(OnItemSelecetedFreeDealsActvity instanceof FreeDealsRechargeActivity){
            Log.e(TAG, "FreeDealsRechargeActivity #$%#%$#$%#$%^$&*()" );*/
        OnItemSelecetedFreeDealsActvity.onItemSelectedFreeDealsClickedActvity(item, selected);
        /*}else {
            Log.e(TAG, "OffersFragment #$%#%$#$%#$%^$&*()" );
            OnItemSelecetedFreeDealsActvity.onItemSelectedFreeDealsClickedActvity(item,selected);
        }*/
    }

    public interface OnItemSelecetedFreeDealsActvity {
        void onItemSelectedFreeDealsClickedActvity(FreeDealsRes.Item item, boolean selected);
    }

    private void loadData() {

        Log.e(TAG, "loadData: FreeDealsRechargeFragment");

        showProgressDialog();

        if (!authToken.isEmpty()) {

            Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();
            ServicesAPI service = mRetrofit.create(ServicesAPI.class);

            Call<FreeDealsRes> call = service.getAllFreeDealsForRecharge(sb_token.toString());

            call.enqueue(new Callback<FreeDealsRes>() {
                @Override
                public void onResponse(Call<FreeDealsRes> call, Response<FreeDealsRes> response) {
                    hideProgreeDialog();
                    response.code();
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
    }

    private void getSharedPrefData() {

        sp = getContext().getSharedPreferences(getString(R.string.spFileUser), getContext().MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken), "");
        sb_token = new StringBuilder("Bearer ")
                .append(authToken);

        splocation = getContext().getSharedPreferences(getString(R.string.sp_location_file), MODE_PRIVATE);

        userLatStr = splocation.getString(getString(R.string.lat), "");
        userLngStr = splocation.getString(getString(R.string.lng), "");
    }

    private void processData(FreeDealsRes res) {
        mAll = new ArrayList<>();
        mAll.clear();
        if (res.getItems() != null) {
            mAll.addAll(res.getItems());
        }


        checkNumberOfDeals(mAll);
        calculateDistance(mAll);


        adapter = new FreeDealsAdapter(getContext(), mAll, this);
        rv_deals_free.setAdapter(adapter);
    }


    private void showProgressDialog() {
        progress_bar.setVisibility(View.VISIBLE);
        rv_deals_free.setVisibility(View.GONE);
    }

    private void hideProgreeDialog() {
        progress_bar.setVisibility(View.GONE);
        rv_deals_free.setVisibility(View.VISIBLE);
    }

    private void calculateDistance(ArrayList<FreeDealsRes.Item> mAll) {



        Log.e(TAG, "calculateDistance: " + userLatStr + "\t " + userLngStr);


        if(!userLatStr.isEmpty() && !userLngStr.isEmpty() && !userLatStr.equals("0.0") && !userLngStr.equals("0.0")){
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

    @Override
    public void onPause() {
        super.onPause();
        count = 1;
        Log.e(TAG, "onDetach: " + count);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void checkNumberOfDeals(ArrayList<FreeDealsRes.Item> mAll){
        Log.e(TAG, "getData: " + mAll.size());

    }


}

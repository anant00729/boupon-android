package com.an2t.android.bouponapp.main.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.adpater.BannerViewPagerAdapter;
import com.an2t.android.bouponapp.main.adpater.HomeFragmentGridMenuAdpater;
import com.an2t.android.bouponapp.main.adpater.HomeSpotLightAdapter;
import com.an2t.android.bouponapp.main.adpater.PurchaseHisAdapter;
import com.an2t.android.bouponapp.main.model.GetSpotlightImages;
import com.an2t.android.bouponapp.main.model.HomeGridMenu;
import com.an2t.android.bouponapp.main.model.Offer;
import com.an2t.android.bouponapp.main.model.RechargeHistory;
import com.an2t.android.bouponapp.my_coupon.CoponsActivity;
import com.an2t.android.bouponapp.recharge.MobileRechargeActivity;
import com.an2t.android.bouponapp.services.RetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.an2t.android.bouponapp.transaction.TransactionMainActivity;
import com.auth0.android.jwt.JWT;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.an2t.android.bouponapp.helpers.Constants.HOME_MENU_LIST_ITEMS;

public class HomeFragment extends Fragment implements HomeFragmentGridMenuAdpater.HomeGirdMenuCallbacks, BannerViewPagerAdapter.OnSpotLightItemClick {


    public HomeFragment() {
        // Required empty public constructor
    }


    private static final String TAG = HomeFragment.class.getSimpleName();



    private ArrayList<HomeGridMenu> mHomeGridItemsList = HOME_MENU_LIST_ITEMS;
    private RecyclerView rv_purchase_history, rv_main_page_menu, rv_deals_spotlight;

    private ArrayList<Offer.Data> mOfferResList;
    private String category;
    private HomeSpotLightAdapter spotLightAdapter;
    private LinearLayout ll_profile;
    private ProfileClickImage mProfileListner;
    private String authToken;
    private ArrayList<GetSpotlightImages.Item> mImages;
    private ViewPager vp_img_restaturant_img;
    private String userId,email;
    private ArrayList<RechargeHistory.RechageHistory> historyList;
    private PurchaseHisAdapter adapter;
    private ProgressDialog mProgressDialog;
    private LinearLayout ll_trans;
    private ImageView img_trans;
    private ImageView img_coupon_click;
    private LinearLayout ll_coupon;
    private RelativeLayout rl_no_recharge;
    private ProgressBar progress_bar;
    private RelativeLayout main_home_container;
    private OnSpotLightClickedFragment onSpotLightClickedFragment;
    private StringBuilder sb_token;
    private OnRetry onRetryShowListener;
    private InvalidToken invalidToken;
    private SwitchBetweenMainAcFragments mSwitchBetweenMainAcFragments;

    public void onAttachToParentFragment(Activity activity) {
        try {
            mProfileListner = (ProfileClickImage) activity;
            onSpotLightClickedFragment= (OnSpotLightClickedFragment) activity;
            onRetryShowListener= (OnRetry) activity;
            invalidToken= (InvalidToken) activity;
            mSwitchBetweenMainAcFragments= (SwitchBetweenMainAcFragments) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnItemSelecetedFreeDealsActvity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        historyList = new ArrayList<>();
        onAttachToParentFragment(getActivity());
        getSharedPrefData();
        getInfo();
        //TODO: init Component
        initComponents(view);



        //TODO: set listeners
        setListeners();

        return view;
    }

    private void setListeners() {
        adapter = new PurchaseHisAdapter(getContext(), historyList);
        rv_purchase_history.setAdapter(adapter);
        rv_main_page_menu.setAdapter(new HomeFragmentGridMenuAdpater(getContext(), mHomeGridItemsList, this));

        callApiAndGettingDeals();
        loadRechargeHistroty();

        ll_profile.setOnClickListener(view -> mProfileListner.profileClickImage(2));



        onTransClick();
        onCoponClick();

    }

    private void onCoponClick() {
        ll_coupon.setOnClickListener(view -> sendToCouponPage());

        img_coupon_click.setOnClickListener(view -> sendToCouponPage());
    }

    private void sendToCouponPage() {
        Intent intent = new Intent(getContext(), CoponsActivity.class);
        startActivity(intent);
    }


    private void onTransClick() {
        img_trans.setOnClickListener(view -> sendToTransPage());

        ll_trans.setOnClickListener(view -> sendToTransPage());
    }

    private void sendToTransPage() {
        Intent transIntent = new Intent(getContext(), TransactionMainActivity.class);
        getContext().startActivity(transIntent);
    }


    private void initComponents(View view) {
        mImages = new ArrayList<>();
        mProgressDialog = new ProgressDialog(getContext());
        rv_purchase_history =  view.findViewById(R.id.rv_purchase_history);
        rv_main_page_menu =  view.findViewById(R.id.rv_main_page_menu);
        rv_deals_spotlight =  view.findViewById(R.id.rv_deals_spotlight);
        ll_profile =  view.findViewById(R.id.ll_profile);
        vp_img_restaturant_img = view.findViewById(R.id.vp_img_restaturant_img);
        ll_trans=  view.findViewById(R.id.ll_trans);
        img_trans= view.findViewById(R.id.img_trans);
        img_coupon_click= view.findViewById(R.id.img_coupon_click);
        ll_coupon= view.findViewById(R.id.ll_coupon);
        rl_no_recharge= view.findViewById(R.id.rl_no_recharge);
        progress_bar= view.findViewById(R.id.progress_bar);
        main_home_container= view.findViewById(R.id.main_home_container);



        rv_purchase_history.setHasFixedSize(true);
        rv_main_page_menu.setHasFixedSize(true);
        rv_deals_spotlight.setHasFixedSize(true);


        rv_purchase_history.setLayoutManager(new LinearLayoutManager(getContext()));

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rv_main_page_menu.setItemAnimator(new DefaultItemAnimator());


        rv_deals_spotlight.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        rv_main_page_menu.setLayoutManager(manager);

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(rv_deals_spotlight);


        rv_purchase_history.setNestedScrollingEnabled(false);
        rv_main_page_menu.setNestedScrollingEnabled(false);
        rv_deals_spotlight.setNestedScrollingEnabled(true);




    }

    private void getSharedPrefData() {
        SharedPreferences sp = getContext().getSharedPreferences(getString(R.string.spFileUser), getContext().MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken), "");
    }


    private void callApiAndGettingDeals() {

        showProgressDialog();
        Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();

        ServicesAPI service = mRetrofit.create(ServicesAPI.class);

        Call<GetSpotlightImages> call = service.getAllImages(sb_token.toString());

        call.enqueue(new Callback<GetSpotlightImages>() {
            @Override
            public void onResponse(Call<GetSpotlightImages> call, Response<GetSpotlightImages> response) {
                mProgressDialog.dismiss();
                GetSpotlightImages getData = response.body();
                if (getData != null) {
                    processRes(getData);
                }
            }

            @Override
            public void onFailure(Call<GetSpotlightImages> call, Throwable t) {
                mProgressDialog.dismiss();
                onRetryShowListener.onRetryFragmentShow();

            }
        });

    }

    private void processRes(GetSpotlightImages getData) {

        if(getData.getMessage() != null){
            invalidToken.invalidToken(getData.getMessage());
        }


        mImages.clear();
        if(mImages != null && getData.getItems()!= null){
            mImages.addAll(getData.getItems());
            Log.e(TAG, "processRes: " + mImages.size() );
            spotLightAdapter = new HomeSpotLightAdapter(getContext(), mImages);
            rv_deals_spotlight.setAdapter(spotLightAdapter);

            BannerViewPagerAdapter adapter = new BannerViewPagerAdapter(mImages, getContext(), this);
            vp_img_restaturant_img.setAdapter(adapter);
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new BannerTimerTask(), 5000, 5000);
        }

    }

    @Override
    public void onItemClick(int position) {
        position = position % mHomeGridItemsList.size();
        if (position == 0) {
            startActivity(new Intent(getContext(), MobileRechargeActivity.class));
        }if(position == 1){
            mSwitchBetweenMainAcFragments.onSwitchBetweenMainAcFragments();
        }

    }

    public interface ProfileClickImage {
        void profileClickImage(int i);
    }

    private int mCurrentBanner = 0;

    public class BannerTimerTask extends TimerTask {
        @Override
        public void run() {

            if (getActivity() == null)
                return;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (mCurrentBanner == (mImages.size())) {
                        mCurrentBanner = 0;
                    }
                    vp_img_restaturant_img.setCurrentItem(mCurrentBanner++, true);
                }
            });
        }

    }

    private void loadRechargeHistroty() {
        showProgressDialog();
        RechargeHistory.RechargeHistorySend sendData = new RechargeHistory.RechargeHistorySend(userId,email);
        Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();

        ServicesAPI service = mRetrofit.create(ServicesAPI.class);

        Call<RechargeHistory> call = service.getHistory(sb_token.toString(),sendData);

        call.enqueue(new Callback<RechargeHistory>() {
            @Override
            public void onResponse(Call<RechargeHistory> call, Response<RechargeHistory> response) {
                hideProgreeDialog();
                RechargeHistory res = response.body();
                if (res != null) {
                    processResHistory(res);
                }
            }

            @Override
            public void onFailure(Call<RechargeHistory> call, Throwable t) {
                hideProgreeDialog();
            }
        });

    }

    private void processResHistory(RechargeHistory res) {
        historyList.clear();
        if (res.getRechageHistory() != null && historyList!= null) {
            historyList.addAll(res.getRechageHistory());
        }
        if(historyList.size() > 0){
            adapter.notifyDataSetChanged();
        }else{
            rl_no_recharge.setVisibility(View.VISIBLE);
            rv_purchase_history.setVisibility(View.GONE);
        }
    }

    private void getInfo() {
//        JWT jwt = new JWT(authToken);

//        sb_token = new StringBuilder("Bearer ")
//                .append(authToken);
//        userId = jwt.getClaim("id").asString();
//        email = jwt.getClaim("email").asString();

        sb_token = new StringBuilder("Bearer ")
                .append(authToken);
        userId = "some_user_id";
        email = "aa@g.com";
    }



    private void showProgressDialog() {
        progress_bar.setVisibility(View.VISIBLE);
        main_home_container.setVisibility(View.GONE);
    }

    private void hideProgreeDialog() {
        progress_bar.setVisibility(View.GONE);
        main_home_container.setVisibility(View.VISIBLE);


    }

    @Override
    public void onSpotLightItemClicked(GetSpotlightImages.Item imageBannerData) {
        onSpotLightClickedFragment.onSpotLightClickedFragment(imageBannerData);
    }

    public interface OnSpotLightClickedFragment{
        void onSpotLightClickedFragment(GetSpotlightImages.Item imageBannerData);
    }

    public interface OnRetry {
        void onRetryFragmentShow();
    }

    public interface InvalidToken{
        void invalidToken(String message);
    }

    public interface SwitchBetweenMainAcFragments{
        void onSwitchBetweenMainAcFragments();
    }

}




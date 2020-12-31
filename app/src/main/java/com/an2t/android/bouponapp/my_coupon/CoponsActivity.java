package com.an2t.android.bouponapp.my_coupon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.my_coupon.adpater.CouponMainAdapter;
import com.an2t.android.bouponapp.my_coupon.fragment.DialogCouponFragment;
import com.an2t.android.bouponapp.my_coupon.fragment.DialogGiftTransferOptions;
import com.an2t.android.bouponapp.my_coupon.fragment.DialogPhone;
import com.an2t.android.bouponapp.my_coupon.model.CouponCall;
import com.an2t.android.bouponapp.my_coupon.model.RedeemCoupon;
import com.an2t.android.bouponapp.my_coupon.model.SendGift;
import com.an2t.android.bouponapp.my_coupon.model.ShortenUrl;
import com.an2t.android.bouponapp.services.RetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.auth0.android.jwt.JWT;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CoponsActivity extends AppCompatActivity implements CouponMainAdapter.OnRedeemClick,
        DialogCouponFragment.OnRedeemClickDialog,
        DialogPhone.OnGiftClick {

    public static final String BRANCH_LIVE_KEY = "key_live_obrk6I96JXyTkXLVv4B5phldEFc7uify";


    private RecyclerView rv_coupon_list;
    private String authToken;
    private StringBuilder sb_token;
    private String userId;
    private CouponMainAdapter mAdapter;
    ArrayList<CouponCall.CouponRes.Coupon> mCouponList;
    private ProgressDialog mProgressDialog;
    private ImageView img_back;
    private TextView tv_toolbar_name;
    private ImageView img_no_coupon;
    private TextView tv_no_coupon;
    private CoordinatorLayout main_container;
    private CouponMainAdapter.CouponMainViewHolder holder;
    private CouponCall.CouponRes.Coupon itemForRemvalOnGiftSent;
    private String host_url;
    private SwipeRefreshLayout swipe_to_reload;
    private StringBuilder sb_url_for_gift;
    private String shortUrlFinal;

    private static final String TAG = CoponsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copons);
        getSharedPrefData();
        getInfo();
        initComponenet();
        loadData();
        setListerner();
    }

    private void setListerner() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        swipe_to_reload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                swipe_to_reload.setRefreshing(false);
            }
        });
    }


    private void initComponenet() {
        mProgressDialog = new ProgressDialog(this);
        mCouponList = new ArrayList<>();
        rv_coupon_list = (RecyclerView)findViewById(R.id.rv_coupon_list);
        tv_toolbar_name= (TextView) findViewById(R.id.tv_toolbar_name);
        tv_no_coupon= (TextView) findViewById(R.id.tv_no_coupon);
        img_no_coupon= (ImageView) findViewById(R.id.img_no_coupon);
        img_back= (ImageView)findViewById(R.id.img_back);
        main_container= (CoordinatorLayout) findViewById(R.id.main_container);
        swipe_to_reload=  findViewById(R.id.swipe_to_reload);
        rv_coupon_list.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setReverseLayout(false);
        rv_coupon_list.setLayoutManager(lm);
        mAdapter = new CouponMainAdapter(this,mCouponList,this);
        rv_coupon_list.setAdapter(mAdapter);
        tv_toolbar_name.setText(getString(R.string.redeem_coupon));
    }

    private void loadData() {

        showProgressDialog();

        CouponCall mCouponCall = new CouponCall(userId);
        Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();
        ServicesAPI service = mRetrofit.create(ServicesAPI.class);

        Call<CouponCall.CouponRes> call = service.getAllCoupons(sb_token.toString(),mCouponCall);

        call.enqueue(new Callback<CouponCall.CouponRes>() {
            @Override
            public void onResponse(Call<CouponCall.CouponRes> call, Response<CouponCall.CouponRes> response) {
                mProgressDialog.dismiss();
                CouponCall.CouponRes res = response.body();
                if (res != null) {
                    processData(res);
                }
            }

            @Override
            public void onFailure(Call<CouponCall.CouponRes> call, Throwable t) {
                mProgressDialog.dismiss();
                showNoCouponFound();
                showSnackbar(getString(R.string.oops));
            }
        });
    }

    private void processData(CouponCall.CouponRes res) {
        if (res.getHost() != null) {
            host_url = res.getHost();
        }
        // TODO: Display Expiry Date in Current format -> (D)
        mCouponList.clear();
        mCouponList.addAll(res.getCoupons());
        if(mCouponList.size() == 0){
            showNoCouponFound();
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }



    private void getInfo() {
        JWT jwt = new JWT(authToken);
        userId = jwt.getClaim("id").asString();
    }

    private void showProgressDialog() {
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onRedeemClicked(CouponMainAdapter.CouponMainViewHolder holder, CouponCall.CouponRes.Coupon mCoupon) {
        if(!mCoupon.getShareToken().isEmpty() && mCoupon.getShareToken() != null ){
            sb_url_for_gift = new StringBuilder(host_url)
                    .append("/giftmycouponbranchio/?token=")
                    .append(String.valueOf(mCoupon.getShareToken()));
            loadDataForShortUrl(mCoupon,holder);
        }

    }

    @Override
    public void onRedeemClickedDialog(CouponCall.CouponRes.Coupon item) {

        showProgressDialog();
        RedeemCoupon mRedeem= new RedeemCoupon(item.getCode());
        Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();

        ServicesAPI service = mRetrofit.create(ServicesAPI.class);

        Call<RedeemCoupon.RedeemCouponRes> call = service.setRedeemsion(sb_token.toString(),mRedeem);

        call.enqueue(new Callback<RedeemCoupon.RedeemCouponRes>() {
            @Override
            public void onResponse(Call<RedeemCoupon.RedeemCouponRes> call, Response<RedeemCoupon.RedeemCouponRes> response) {
                mProgressDialog.dismiss();
                RedeemCoupon.RedeemCouponRes res =response.body();
                if (res != null) {
                    processRedeem(res,item);
                }
            }
            @Override
            public void onFailure(Call<RedeemCoupon.RedeemCouponRes> call, Throwable t) {
                mProgressDialog.dismiss();
                showSnackbar(getString(R.string.oops));
            }
        });
    }

    private void processRedeem(RedeemCoupon.RedeemCouponRes res, CouponCall.CouponRes.Coupon item) {
        if(res.getIsExpired()){
            showSnackbar(getString(R.string.redeemed_message));

        }else{
            mAdapter.updateRedeemStatus(holder,item);
        }
    }

    private void getSharedPrefData() {
        SharedPreferences sp = getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken), "");
        sb_token = new StringBuilder("Bearer ")
                .append(authToken);
    }

    public interface RedeemCouponResAdpaterNotify{
        void redeemCouponResAdpaterNotify();
    }

    private void showNoCouponFound(){
        img_no_coupon.setVisibility(View.VISIBLE);
        tv_no_coupon.setVisibility(View.VISIBLE);
        rv_coupon_list.setVisibility(View.GONE);
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(main_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onGiftClick(String shortUrlFinal, CouponCall.CouponRes.Coupon item) {

        itemForRemvalOnGiftSent = item;
        String code = item.getCode();
        String couponId = item.get_id();


        if (shortUrlFinal != null && !shortUrlFinal.isEmpty()) {
            DialogGiftTransferOptions options = DialogGiftTransferOptions.newInstance(shortUrlFinal,code,couponId);
            options.show(getSupportFragmentManager(), "dialog_show_gift");
        }
    }

    @Override
    public void onGiftClickFromOptsContacts(String gift_url, String code, String couponId) {
        DialogPhone options = DialogPhone.newInstance(gift_url,code ,couponId);
        options.show(getSupportFragmentManager(), "dialog_show_gift_contacts");
    }

    @Override
    public void onGiftClickFromOptsApps(String shortUrlFinal) {
        Intent i=new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT,"Boupon : Coupon Gifted");
        i.putExtra(Intent.EXTRA_TEXT, "please click here to get your gifteed coupon : \n" + shortUrlFinal);
        startActivity(Intent.createChooser(i,"Share via"));

    }

    @Override
    public void onGiftClicked(String phone_number, String gift_url, String code, String couponId) {
        hideSoftKeyboard();
        loadSendGift(phone_number,gift_url,code,couponId);

    }

    private void loadSendGift(String phone_number, String gift_url, String code, String couponId) {

        showProgressDialog();

        SendGift sendGift = new SendGift(phone_number,code,couponId,gift_url);
        Retrofit retrofit = RetrofitSingleton.getRetrofitInstance();
        ServicesAPI service = retrofit.create(ServicesAPI.class);

        Call<SendGift.SendGiftRes> call = service.getShortUrlForServer(sb_token.toString(),sendGift);
        call.enqueue(new Callback<SendGift.SendGiftRes>() {
            @Override
            public void onResponse(Call<SendGift.SendGiftRes> call, Response<SendGift.SendGiftRes> response) {

                SendGift.SendGiftRes res = response.body();
                if (res != null) {
                    processGiftRes(res);
                }
            }

            @Override
            public void onFailure(Call<SendGift.SendGiftRes> call, Throwable t) {
                mProgressDialog.dismiss();
                showSnackbar(getString(R.string.err_connect));
            }
        });
    }

    private void processGiftRes(SendGift.SendGiftRes res) {
        if(res.isStatus()){
            showSnackbar(res.getMessage());
            mCouponList.remove(itemForRemvalOnGiftSent);
            mAdapter.notifyDataSetChanged();
            hideSoftKeyboard();
            mProgressDialog.dismiss();
        }else{
            showSnackbar(res.getMessage());
            hideSoftKeyboard();
            mProgressDialog.dismiss();
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void loadDataForShortUrl(CouponCall.CouponRes.Coupon mCoupon, CouponMainAdapter.CouponMainViewHolder holder) {

        showProgressDialog();

        String urlForGift = sb_url_for_gift.toString();
        Log.e(TAG, urlForGift);

        ShortenUrl url = new ShortenUrl(urlForGift);

        Log.e(TAG, "loadDataForShortUrl: " + url.getShortUrl());

        Retrofit retrofit = RetrofitSingleton.getRetrofitInstance();


        ServicesAPI mServicesAPI = retrofit.create(ServicesAPI.class);

        Log.e(TAG, "auth_token : " +  sb_token.toString() + " url : " + url.getShortUrl());

        Call<ShortenUrl.ShortenUrlRes> call = mServicesAPI.getShortUrlForServer(sb_token.toString(),url);

        call.enqueue(new Callback<ShortenUrl.ShortenUrlRes>() {
            @Override
            public void onResponse(Call<ShortenUrl.ShortenUrlRes> call, Response<ShortenUrl.ShortenUrlRes> response) {
                mProgressDialog.dismiss();
                ShortenUrl.ShortenUrlRes res  = response.body();
                if ( res != null&& res.getStatus() != null && res.getStatus()) {
                    processRes(res,mCoupon,holder);
                }
            }

            @Override
            public void onFailure(Call<ShortenUrl.ShortenUrlRes> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });
    }

    private void processRes(ShortenUrl.ShortenUrlRes shortenUrl, CouponCall.CouponRes.Coupon mCoupon, CouponMainAdapter.CouponMainViewHolder holder) {
        if(!shortenUrl.getShortUrl().isEmpty() && shortenUrl.getShortUrl() != null){
            shortUrlFinal = shortenUrl.getShortUrl();
            if(!shortUrlFinal.isEmpty() && shortUrlFinal != null){
                DialogCouponFragment mRedirect = DialogCouponFragment.newInstance(shortUrlFinal,mCoupon );
                mRedirect.show(getSupportFragmentManager(),"dailog_show_coupon_main");
                this.holder = holder;
            }
        }
    }
}

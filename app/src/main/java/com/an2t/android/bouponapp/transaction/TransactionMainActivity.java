package com.an2t.android.bouponapp.transaction;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.model.RechargeHistory;
import com.an2t.android.bouponapp.services.RetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.an2t.android.bouponapp.transaction.adpater.MainTransAdpater;
import com.auth0.android.jwt.JWT;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransactionMainActivity extends AppCompatActivity {

    private RecyclerView rv_get_all_trans;
    private String authToken,userId,email;
    private StringBuilder sb_token;
    private ProgressDialog mProgressDialog;
    private MainTransAdpater mAdapter;
    private ArrayList<RechargeHistory.RechageHistory> transList;

    private static final String TAG = "TransactionMainActivity";
    private ImageView img_back;
    private ImageView img_no_coupon;
    private TextView tv_toolbar_name;
    private TextView tv_no_coupon;
    private CoordinatorLayout main_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSharedPrefData();
        getInfo();
        setContentView(R.layout.activity_transaction_main);

        //initComponent
        initComponent();

        //loadData
        loadData();
    }

    private void loadData() {

        showProgressDialog();



        RechargeHistory.RechargeHistorySend sendData = new RechargeHistory.RechargeHistorySend(userId,email);
        Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();

        ServicesAPI service = mRetrofit.create(ServicesAPI.class);
        Call<RechargeHistory> call = service.getHistory(sb_token.toString(),sendData);

        call.enqueue(new Callback<RechargeHistory>() {
            @Override
            public void onResponse(Call<RechargeHistory> call, Response<RechargeHistory> response) {
                mProgressDialog.dismiss();
                RechargeHistory res = response.body();
                if (res != null) {
                    processResHistory(res);
                }
            }
            @Override
            public void onFailure(Call<RechargeHistory> call, Throwable t) {
                mProgressDialog.dismiss();
                showSnackbar(getString(R.string.oops));
            }
        });
    }


    private void processResHistory(RechargeHistory res) {

        transList.clear();
        for (RechargeHistory.RechageHistory rechageHistory : res.getRechageHistory()) {
            if (rechageHistory.getTransactionID() != null) {
                    transList.add(rechageHistory);
            }
        }

        if(transList.size() > 0){
            mAdapter.notifyDataSetChanged();
        }else{
            tv_no_coupon.setVisibility(View.VISIBLE);
            img_no_coupon.setVisibility(View.VISIBLE);
            rv_get_all_trans.setVisibility(View.GONE);
        }

    }

    private void initComponent() {
        mProgressDialog = new ProgressDialog(this);
        transList = new ArrayList<>();
        rv_get_all_trans = (RecyclerView)findViewById(R.id.rv_get_all_trans);
        img_no_coupon= (ImageView) findViewById(R.id.img_no_coupon);
        tv_no_coupon= (TextView) findViewById(R.id.tv_no_coupon);
        tv_toolbar_name= (TextView) findViewById(R.id.tv_toolbar_name);
        img_back= (ImageView) findViewById(R.id.img_back);
        main_container= (CoordinatorLayout) findViewById(R.id.main_container);
        rv_get_all_trans.setHasFixedSize(true);
        rv_get_all_trans.setLayoutManager(new LinearLayoutManager(this));

        tv_toolbar_name.setText(getString(R.string.my_transactions));

        mAdapter = new MainTransAdpater(TransactionMainActivity.this,transList);
        rv_get_all_trans.setAdapter(mAdapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getInfo() {
        JWT jwt = new JWT(authToken);
        userId = jwt.getClaim("id").asString();
        email = jwt.getClaim("email").asString();
    }

    private void getSharedPrefData() {
        SharedPreferences sp = getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken), "");
        sb_token = new StringBuilder("Bearer ")
                .append(authToken);
    }

    private void showProgressDialog() {
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(main_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}

package com.an2t.android.bouponapp.field_area;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.field_area.adapter.FieldNamesAdapter;
import com.an2t.android.bouponapp.field_area.model.MainAreaName;
import com.an2t.android.bouponapp.services.RetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FieldAreaActivity extends AppCompatActivity implements FieldNamesAdapter.SendFieldArea {


    private RecyclerView rl_locality_names;
    private List<String> mNames;
    private FieldNamesAdapter mAdpater, mCityNamesAdapter;
    private String authToken;
    private CoordinatorLayout main_container;
    private RelativeLayout rl_main_container;
    private StringBuilder sb_token;
    private ImageView img_back;
    private String name;
    private TextView tv_toolbar_name;
    private RecyclerView rl_locality_city;
    private List<String> cityNamesList;

    private static final String TAG = "FieldAreaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_area);

        getSharedPrefData();
        // init component
        initComponet();

        //set listener
        setListener();
    }

    private void initComponet() {
        name = "";
        mNames = new ArrayList<>();
        cityNamesList = new ArrayList<>();
        rl_main_container = (RelativeLayout) findViewById(R.id.rl_main_container);

        main_container = (CoordinatorLayout) findViewById(R.id.main_container);
        rl_locality_names = (RecyclerView) findViewById(R.id.rl_locality_names);
        rl_locality_city = (RecyclerView) findViewById(R.id.rl_locality_city);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_toolbar_name = (TextView) findViewById(R.id.tv_toolbar_name);
        rl_locality_names.setHasFixedSize(true);
        rl_locality_city.setHasFixedSize(true);

        rl_locality_names.setNestedScrollingEnabled(false);
        rl_locality_city.setNestedScrollingEnabled(false);

        rl_locality_names.setLayoutManager(new GridLayoutManager(this, 2));
        rl_locality_city.setLayoutManager(new GridLayoutManager(this, 2));

        tv_toolbar_name.setText(getString(R.string.location_picker));

        mAdpater = new FieldNamesAdapter(this, mNames, this, false);
        mCityNamesAdapter = new FieldNamesAdapter(this, cityNamesList, this, true);
        rl_locality_names.setAdapter(mAdpater);
        rl_locality_city.setAdapter(mCityNamesAdapter);

    }

    private void setListener() {
        rl_main_container.setOnClickListener(view -> calculateDistance());

        img_back.setOnClickListener(view -> onBackPressed());
        loadData();
    }

    private void loadData() {
        Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();
        ServicesAPI service = mRetrofit.create(ServicesAPI.class);


        Call<MainAreaName> call = service.getMainAreaNames(sb_token.toString());

        call.enqueue(new Callback<MainAreaName>() {
            @Override
            public void onResponse(Call<MainAreaName> call, Response<MainAreaName> response) {
                MainAreaName res = response.body();
                if (res != null) {
                    processRes(res);
                }
            }

            @Override
            public void onFailure(Call<MainAreaName> call, Throwable t) {
                showSnackbar(getString(R.string.err_connect));
            }
        });


    }


    private void getSharedPrefData() {
        SharedPreferences sp = getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken), "");
        sb_token = new StringBuilder("Bearer ")
                .append(authToken);
        Log.e(TAG, "getSharedPrefData: " + authToken);
    }


    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(main_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void processRes(MainAreaName res) {
        mNames.clear();
        cityNamesList.clear();
        cityNamesList.addAll(res.getAreaCity());
        mNames.addAll(res.getAreaNames());
        mAdpater.notifyDataSetChanged();
        mCityNamesAdapter.notifyDataSetChanged();
    }

    private void calculateDistance() {
        Intent intent = new Intent();
        intent.putExtra("areaName", name);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void sendFieldArea(String name) {
        this.name = name;
        Intent intent = new Intent();
        intent.putExtra("areaName", name);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra("areaName", name);
        setResult(RESULT_OK, intent);
        finish();
    }
}

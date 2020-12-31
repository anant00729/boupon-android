package com.an2t.android.bouponapp.recharge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.helpers.Constants;
import com.an2t.android.bouponapp.main.adpater.PurchaseHisAdapter;
import com.an2t.android.bouponapp.main.model.RechargeHistory;
import com.an2t.android.bouponapp.recharge.fragment_free_deals.AddNumber;
import com.an2t.android.bouponapp.recharge.fragment_free_deals.ConfirmNumber;
import com.an2t.android.bouponapp.services.RetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.auth0.android.jwt.JWT;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.an2t.android.bouponapp.recharge.fragment_free_deals.AddNumber.PERMISSION_REQUEST_CODE;

public class MobileRechargeActivity extends AppCompatActivity implements AddNumber.OnNextClick,ConfirmNumber.OnChangeClick,ConfirmNumber.ShowProvideValidFields {

    private ImageView img_btn_contact_list,img_btn_close ;
    private List<String> mOperatorList = Constants.OPERATOR_ITEMS;
    private Spinner spin_operator;
    private EditText in_phone_number;
    private Button btn_next;
    private Toolbar toolbar;
    private FrameLayout frame_add_number;
    private RecyclerView rv_recharge_records;
    private String phoneNumber;
    private String amount;
    private boolean isFrom;
    private String authToken,userId,email;
    private PurchaseHisAdapter adapter;
    private ArrayList<RechargeHistory.RechageHistory> mHistoryList;
    private RelativeLayout rl_no_recharge;
    private CardView card_recharge_record;
    private String rechargeType,provider;
    private StringBuilder sb_token;
    private ProgressBar progress_bar;
    private CoordinatorLayout main_container;

    private static final String TAG = "MobileRechargeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*mSendToContactPageListener = (SendToContactPage) this;*/
        setContentView(R.layout.activity_mobile_recharge);
        getSharedPrefData();
        getInfo();
        getIntentDataForSpeedRecharge();
        // init Component
        initComponent();
        // set Listener
        setListener();
        loadRechargeHistroty();
    }

    private void getIntentDataForSpeedRecharge() {
        Intent intent = getIntent();
        if (intent != null) {

            phoneNumber = intent.getStringExtra("phoneNumber");
            amount =intent.getStringExtra("amount");
            isFrom = intent.getBooleanExtra("isFromSpeed",false);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setListener() {

        Log.e(TAG, "setListener: " + String.valueOf(amount) );

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                onBackPressed();
            }
        });
        if(isFrom){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_add_number, ConfirmNumber.newInstance(phoneNumber,String.valueOf(amount),rechargeType,provider))
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_add_number, new AddNumber())
                    .commit();
        }

        adapter = new PurchaseHisAdapter(MobileRechargeActivity.this,mHistoryList);
        rv_recharge_records.setAdapter(adapter);
        rv_recharge_records.setFocusable(false);
    }

    private void initComponent() {

        mHistoryList = new ArrayList<>();
        toolbar = (Toolbar)findViewById(R.id.common_toolbar);
        rv_recharge_records= (RecyclerView) findViewById(R.id.rv_recharge_records);
        btn_next = (Button) findViewById(R.id.btn_next);
        spin_operator = (Spinner) findViewById(R.id.spin_operator);
        img_btn_contact_list = (ImageView) findViewById(R.id.img_btn_contact_list);
        in_phone_number= (EditText) findViewById(R.id.in_phone_number);
        img_btn_close = (ImageView) findViewById(R.id.img_btn_close);
        frame_add_number= (FrameLayout) findViewById(R.id.frame_add_number);
        rl_no_recharge= (RelativeLayout) findViewById(R.id.rl_no_recharge);
        card_recharge_record= (CardView) findViewById(R.id.card_recharge_record);
        progress_bar= (ProgressBar) findViewById(R.id.progress_bar);
        main_container= (CoordinatorLayout) findViewById(R.id.main_container);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mobile Recharge");
        rv_recharge_records.setHasFixedSize(true);
        rv_recharge_records.setLayoutManager(new LinearLayoutManager(this));


    }

    private void showProgress() {
        card_recharge_record.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        card_recharge_record.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void onNextClick(String phone_number) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_add_number,ConfirmNumber.newInstance(phone_number))
                .addToBackStack(null)
                .commit();
    }


    private void loadRechargeHistroty() {

        showProgress();

        RechargeHistory.RechargeHistorySend sendData = new RechargeHistory.RechargeHistorySend(userId,email);
        Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();

        ServicesAPI service = mRetrofit.create(ServicesAPI.class);
        Call<RechargeHistory> call = service.getHistory(sb_token.toString(),sendData);

        call.enqueue(new Callback<RechargeHistory>() {
            @Override
            public void onResponse(Call<RechargeHistory> call, Response<RechargeHistory> response) {
                hideProgress();
                RechargeHistory res = response.body();
                if (res != null) {
                    processResHistory(res);
                }
            }
            @Override
            public void onFailure(Call<RechargeHistory> call, Throwable t) {
                hideProgress();
                showMessageWithNoContacts();
            }
        });
    }

    private void processResHistory(RechargeHistory res) {
        mHistoryList.clear();
        if (res.getRechageHistory() != null) {
            mHistoryList.addAll(res.getRechageHistory());
        }
        if(mHistoryList.size() > 0){
            adapter.notifyDataSetChanged();
        }else{
            showMessageWithNoContacts();
        }

    }

    private void showMessageWithNoContacts() {
        card_recharge_record.setPadding(16,16,16,16);
        rl_no_recharge.setVisibility(View.VISIBLE);
        rv_recharge_records.setVisibility(View.GONE);
    }

    private void getSharedPrefData() {
        SharedPreferences sp = getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken),"");
        sb_token = new StringBuilder("Bearer ")
                .append(authToken);
    }

    private void getInfo() {
        JWT jwt = new JWT(authToken);
        userId = jwt.getClaim("id").asString();
        email = jwt.getClaim("email").asString();
    }

    @Override
    public void onChangeClick() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.e(TAG, "onRequestPermissionsResult: ");

        if (requestCode == PERMISSION_REQUEST_CODE) {

            Log.e(TAG, "onRequestPermissionsResult: " + String.valueOf(grantResults));
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AddNumber fragment = (AddNumber) getSupportFragmentManager().findFragmentById(R.id.frame_add_number);
                fragment.goToContactsPage();
            } else {
                showSnackbar("Oops you need press 'Allow' to get the contact number from the contact list");
            }
        }

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void showProvideValidFields(String phoneNumber) {
        if(!phoneNumber.isEmpty()){
            String phoneKey = phoneNumber.substring(0,3);
            if(phoneKey.equals("015")){
                showSnackbar("Teletalk will be added soon, please recharge with other operators");
            }else {
                showSnackbar("Please provide valid fields to proceed !!");
            }
        }
    }

    private void showSnackbar(String message){
        Snackbar snackbar = Snackbar
                .make(main_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void showAmountCantBeProceed(String s) {
        showSnackbar(s);
    }
}

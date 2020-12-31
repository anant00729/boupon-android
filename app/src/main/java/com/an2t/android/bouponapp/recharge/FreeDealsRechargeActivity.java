package com.an2t.android.bouponapp.recharge;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.field_area.FieldAreaActivity;
import com.an2t.android.bouponapp.recharge.fragment_free_deals.DialogFreeDealsFragment;
import com.an2t.android.bouponapp.recharge.fragment_free_deals.DialogFreeDealsProceed;
import com.an2t.android.bouponapp.recharge.fragment_free_deals.FreeDealsRechargeFragment;
import com.an2t.android.bouponapp.recharge.model.FreeDealsRes;
import com.an2t.android.bouponapp.services.RetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.an2t.android.bouponapp.services.TrackGPS;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.an2t.android.bouponapp.main.MainActivity.REQUEST_CODE_FOR_LOCATION_BASED_SEARCH;
import static com.an2t.android.bouponapp.payment.PayActivity.PAYMENT_AMOUNT;
import static com.an2t.android.bouponapp.payment.PayActivity.PAYMENT_MOBILE_NUMBER;
import static com.an2t.android.bouponapp.payment.PayActivity.PAYMENT_OPERATOR;
import static com.an2t.android.bouponapp.payment.PayActivity.PAYMENT_TYPE;
import static com.an2t.android.bouponapp.recharge.model.FreeDealsRes.Item.distanceComparator;

public class FreeDealsRechargeActivity extends AppCompatActivity implements FreeDealsRechargeFragment.OnItemSelecetedFreeDealsActvity,DialogFreeDealsFragment.DialogItemAdd,DialogFreeDealsProceed.ShowSackBar,DialogFreeDealsProceed.NotifyActivityValue {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ArrayList<FreeDealsRes.Item> mSelectedList;


    private ViewPager mViewPager;

    private static final String TAG = "FreeDealsRechargeActivi";

    private ArrayList<FreeDealsRes.Item> mAll;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private Button btn_recharge;
    private TextView tv_total_amount,tv_value;
    private int value,temp_val;
    private String payment_url,payment_amt,payment_number,payment_operator,payment_type;
    private String authToken;
    private Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    private TextView tv_toolbar_name;
    private ImageView img_back;
    private boolean isFirstEqual;
    private StringBuilder sb_token;
    private CoordinatorLayout main_container;
    private String cityName;
    private MenuItem itemLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFirstEqual= true;
        getSharedPrefData();
        getIntentData();
        mAll = new ArrayList<>();
        mSelectedList = new ArrayList<>();
        mProgressDialog = new ProgressDialog(this);
        loadData();

        setContentView(R.layout.activity_free_deals_recharge);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        showLocation();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),mAll);
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);
        main_container= (CoordinatorLayout) findViewById(R.id.main_container);
        tv_total_amount= (TextView) findViewById(R.id.tv_total_amount);
        tv_value= (TextView) findViewById(R.id.tv_value);
        tv_toolbar_name= (TextView) findViewById(R.id.tv_toolbar_name);
        img_back= (ImageView) findViewById(R.id.img_back);

        toolbar= findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        setButtonDrawable();


        tv_toolbar_name.setText("Free Deals");

        onBackPres();

        tv_total_amount.setText("৳ " + payment_amt);
        tv_value.setText(String.valueOf(Integer.parseInt(payment_amt) - value));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: " + mAll.size());
                Fragment frag = mSectionsPagerAdapter.fragments[position];
                if (frag != null && frag instanceof FreeDealsRechargeFragment) {
                    ((FreeDealsRechargeFragment)frag).getData(mAll,position);
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectedList.size() > 0){
                    showProceedPage();
                }
                else{
                    showAlertDialogForNotDealSelection();
                }
            }
        });
    }

    private void setButtonDrawable() {
        final int sdk = Build.VERSION.SDK_INT;
        if(sdk > Build.VERSION_CODES.KITKAT) {
            btn_recharge.setBackgroundDrawable( getResources().getDrawable(R.drawable.btn_selector_next));
            btn_recharge.setPadding(32,0,0,0);
        } else {
            btn_recharge.setBackgroundColor(getResources().getColor(R.color.black_trans_grad));
        }
    }

    private void showProceedPage() {
        DialogFreeDealsProceed dialogProceed = DialogFreeDealsProceed.newInstance(mSelectedList,payment_amt,value,payment_type,payment_operator,payment_url,payment_number);
        dialogProceed.show(getSupportFragmentManager(), "Show_proceed_page");
    }

    private void showAlertDialogForNotDealSelection() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(FreeDealsRechargeActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("No Deals Selected");
        dialog.setMessage("Are you sure you want to continue ?" );
        dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                showProceedPage();
            }
        }).setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }
    private void onBackPres() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getSharedPrefData() {
        SharedPreferences sp = getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken),"");
        sb_token = new StringBuilder("Bearer ")
                .append(authToken);
    }

    private void getIntentData() {
        Intent intent =getIntent();
        if (intent != null) {
            payment_amt = intent.getStringExtra(PAYMENT_AMOUNT);
            payment_operator = intent.getStringExtra(PAYMENT_OPERATOR);
            payment_type = intent.getStringExtra(PAYMENT_TYPE);
            payment_number = intent.getStringExtra(PAYMENT_MOBILE_NUMBER);
        }
    }



    private void showSanckbarMessageToAddDeals() {
        value = 0;
        tv_value.setText(String.valueOf(Integer.parseInt(payment_amt) - value));
        Snackbar.make(main_container, "Please select some free deals", Snackbar.LENGTH_SHORT)
                .show();
    }


    private void loadData() {

        showProgressDialog();

        if(!authToken.isEmpty()){

            Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();
            ServicesAPI service = mRetrofit.create(ServicesAPI.class);

            Call<FreeDealsRes> call = service.getAllFreeDealsForRecharge(sb_token.toString());

            call.enqueue(new Callback<FreeDealsRes>() {
                @Override
                public void onResponse(Call<FreeDealsRes> call, Response<FreeDealsRes> response) {
                    response.code();
                    FreeDealsRes res = response.body();
                    if (res != null) {
                        processData(res);
                    }
                }
                @Override
                public void onFailure(Call<FreeDealsRes> call, Throwable t) {
                    mProgressDialog.dismiss();
                    showSnackbar(getString(R.string.oops));
                }
            });
        }
    }

    private void showProgressDialog() {
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }


    public static class SectionsPagerAdapter extends FragmentStatePagerAdapter {

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

    private void processData(FreeDealsRes res) {
        if (res.getItems() != null) {
            mAll.addAll(res.getItems());
        }
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

    @Override
    public void onItemSelectedFreeDealsClickedActvity(FreeDealsRes.Item item, boolean selected) {

        DialogFreeDealsFragment dialog = DialogFreeDealsFragment.newInstance(item,payment_amt,String.valueOf(value));
        dialog.show(getSupportFragmentManager(), "Show_free_deals_dialog");
    }

    @Override
    public void onDialogItemAddClicked(FreeDealsRes.Item item) {

        if(Integer.parseInt(payment_amt) >= value) {

            temp_val = value;
            temp_val += Integer.parseInt(item.getValue());

            if(Integer.parseInt(payment_amt) >= temp_val){
                value = temp_val;
            }else if(temp_val > value){
                showSnackbar(getString(R.string.points_insufficient));
                return;
            }
            if(Integer.parseInt(payment_amt) >= value ){
                if (Integer.parseInt(payment_amt) == value && isFirstEqual){
                    if(isFirstEqual){
                        tv_value.setText(String.valueOf(Integer.parseInt(payment_amt) - value));
                        mSelectedList.add(item);
                        isFirstEqual = false;
                        showSnackbar(getString(R.string.points_equal_to_amount));
                    }else {
                        return;
                    }
                }else{
                    mSelectedList.add(item);
                    tv_value.setText(String.valueOf(Integer.parseInt(payment_amt) - value));
                }
            }
        }
    }

    @Override
    public void showSnackBarToAddDeals() {
        showSanckbarMessageToAddDeals();
    }

    @Override
    public void onNotifyActivityValue(String value_dec) {
        value -= Integer.parseInt(value_dec);
        tv_value.setText(String.valueOf(Integer.parseInt(payment_amt) - value));
    }

    private void calculateDistance(ArrayList<FreeDealsRes.Item> mAll) {
        SharedPreferences sp = getSharedPreferences(getString(R.string.sp_location_file), MODE_PRIVATE);


        String userLatStr = sp.getString(getString(R.string.lat), "");
        String userLngStr = sp.getString(getString(R.string.lng), "");

        if(!userLatStr.isEmpty() && !userLngStr.isEmpty() && !userLatStr.equals("0.0") && !userLngStr.equals("0.0")) {
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
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount());
        mProgressDialog.dismiss();


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

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(main_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showLocation() {
        TrackGPS mTracker = new TrackGPS(this);

        if(mTracker.canGetLocation()){


            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(mTracker.getLatitude(), mTracker.getLongitude(), 1);
                if(addresses.size() != 0){
                    cityName = addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.main_activity_menu);

        itemLocation = menu.findItem(R.id.menu_city_name);
        if (cityName != null) {
            itemLocation.setTitle(cityName);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_city_name:
                intent = new Intent(FreeDealsRechargeActivity.this, FieldAreaActivity.class);
                startActivityForResult(intent,REQUEST_CODE_FOR_LOCATION_BASED_SEARCH);
                break;
            case R.id.menu_map_pointer:
                intent = new Intent(FreeDealsRechargeActivity.this, FieldAreaActivity.class);
                startActivityForResult(intent,REQUEST_CODE_FOR_LOCATION_BASED_SEARCH);
                break;
        }
        return true;
    }

    // This method is called when the second activity finishes

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == REQUEST_CODE_FOR_LOCATION_BASED_SEARCH) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK


                String returnString = data.getStringExtra("areaName");


                if (returnString != null && !returnString.isEmpty()) {
                    showSnackbar("showing deals for " + returnString + " locality");
                    itemLocation.setTitle(returnString);
                } else {
                    showSnackbar(getString(R.string.showing_nearest));
                    showLocation();
                    if (cityName != null) {
                        itemLocation.setTitle(cityName);
                    }
                }
                seeFieldArea(returnString);
            }
        }
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

        mViewPager.setCurrentItem(0);

        Fragment frag = mSectionsPagerAdapter.fragments[0];
        if (frag != null && frag instanceof FreeDealsRechargeFragment) {
            ((FreeDealsRechargeFragment)frag).getData(mAllNew,0);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

}

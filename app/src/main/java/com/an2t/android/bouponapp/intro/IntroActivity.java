package com.an2t.android.bouponapp.intro;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.login.LoginActivity;
import com.an2t.android.bouponapp.main.MainActivity;
import com.an2t.android.bouponapp.my_coupon.model.ReadBranchGiftData;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.an2t.android.bouponapp.services.TrackGPS;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.an2t.android.bouponapp.my_coupon.CoponsActivity.BRANCH_LIVE_KEY;

public class IntroActivity extends Activity {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private TrackGPS mTrackGPS;
    private static final String TAG = "IntroActivity";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private ProgressDialog mProgressDialog;

    // for location
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        mProgressDialog = new ProgressDialog(this);
        showProgressDialog();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        sp = getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
        editor = sp.edit();

        layouts = new int[]{
                R.layout.slide1,
                R.layout.slide2,
                R.layout.slide3};



        // adding bottom dots
        addBottomDots(0);

        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        getGPSLocation();

    }

    private void getGPSLocation() {
        initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission
    }

    public  void btnSkipClick(View v)
    {
        launchHomeScreen();
    }

    public  void btnNextClick(View v)
    {
        // checking for last page
        // if last page home screen will be launched
        int current = getItem(1);
        if (current < layouts.length) {
            // move to next screen
            viewPager.setCurrentItem(current);
        } else {
            launchHomeScreen();
        }
    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dot_inactive));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.dot_active));
    }


    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        SharedPreferences sp = getSharedPreferences(getString(R.string.spFileUser),MODE_PRIVATE);
        String authToken = sp.getString(getString(R.string.userLoginToken),"");

        Log.e(TAG, "launchHomeScreen: " + authToken);

        if(authToken.isEmpty()){
            Intent intent = new Intent(IntroActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(IntroActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;


        public ViewPagerAdapter() {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
    private void saveUserLocationInSharedPref() {

        mTrackGPS = new TrackGPS(this);

        SharedPreferences sp = getSharedPreferences(getString(R.string.sp_location_file), MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();


        Log.e(TAG, "saveUserLocationInSharedPref: " + mTrackGPS.getLatitude());
        Log.e(TAG, "saveUserLocationInSharedPref: " + mTrackGPS.getLongitude());

        editor.putString(getString(R.string.lat), String.valueOf(mTrackGPS.getLatitude()));
        editor.putString(getString(R.string.lng), String.valueOf(mTrackGPS.getLongitude()));

        editor.apply();

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setCancelable(false);
        alertDialog.setTitle("Location not enabled");
        alertDialog.setMessage("Oops you need press 'Allow' to get the nearest deals around you ..!!");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getGPSLocation();
            }
        });
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);
    }

    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            showSettingDialog();
        }
    };

    /* Broadcast receiver to check status of GPS */
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device");

                    saveUserLocationInSharedPref();
                } else {
                    //If GPS turned OFF show Location Dialog
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    // showSettingDialog();

                    Log.e("About GPS", "GPS is Disabled in your device");
                }

            }
        }
    };




    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //If permission granted show location dialog if APIClient is not null
                    if (mGoogleApiClient == null) {
                        initGoogleAPIClient();
                        showSettingDialog();
                    } else
                        showSettingDialog();
                } else {
                    showAlertDialog();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(IntroActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(IntroActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialog();
        } else{
            if (ContextCompat.checkSelfPermission(IntroActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialog();
        }
    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(IntroActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(IntroActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(IntroActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    /* Show Location Access Dialog */
    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        saveUserLocationInSharedPref();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(IntroActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");

                        initGoogleAPIClient();
                        showSettingDialog();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                saveUserLocationInSharedPref();
                            }
                        }, 3000);
                        break;
                    case RESULT_CANCELED:
                        getGPSLocation();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

        branch.initSession(new Branch.BranchReferralInitListener(){
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {

                    JSONObject firstParams = Branch.getInstance().getFirstReferringParams();
                    JSONObject lastParams = Branch.getInstance().getLatestReferringParams();

                    Log.e(TAG, "firstParams :  " +  firstParams.toString() );
                    Log.e(TAG, "lastParams :  " +  lastParams.toString() );

                    String urlToDecode = referringParams.optString("+non_branch_link", "");
                    Log.e(TAG, "onInitFinished: " + referringParams.toString() );
                    if(!urlToDecode.isEmpty()){
                        Log.e(TAG, "!urlToDecode.isEmpty() " );
                        sendGiftRequest(urlToDecode);
                    }else {
                        Log.e(TAG, " urlToDecode.isEmpty() " );
                        mProgressDialog.dismiss();
                    }

                } else {
                    mProgressDialog.dismiss();
                    Log.e("MyApp", error.getMessage());
                }


            }


        }, this.getIntent().getData(), this);


    }

    private void sendGiftRequest(String urlToDecode) {



        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(50000, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.branch.io/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServicesAPI service  = retrofit.create(ServicesAPI.class);

        Call<ReadBranchGiftData> call = service.getBranchGiftData(urlToDecode, BRANCH_LIVE_KEY);

        call.enqueue(new Callback<ReadBranchGiftData>() {
            @Override
            public void onResponse(Call<ReadBranchGiftData> call, Response<ReadBranchGiftData> response) {
                ReadBranchGiftData res = response.body();
                if (res != null && res.getData().getToken() != null) {
                    String[] urls = res.getData().getToken().split("[=]");
                    editor.putString(getString(R.string.giftCouponToken), urls[1]);
                    editor.apply();
                    mProgressDialog.dismiss();
                }else {
                    mProgressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ReadBranchGiftData> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });
    }
    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    private void showProgressDialog() {
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }


}

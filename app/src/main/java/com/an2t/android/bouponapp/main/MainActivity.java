package com.an2t.android.bouponapp.main;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.an2t.android.bouponapp.BuildConfig;
import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.fcm_service.Config;
import com.an2t.android.bouponapp.fcm_service.NotificationUtils;
import com.an2t.android.bouponapp.field_area.FieldAreaActivity;
import com.an2t.android.bouponapp.login.LoginActivity;
import com.an2t.android.bouponapp.main.adpater.MainViewPagerAdapter;
import com.an2t.android.bouponapp.main.fragments.HomeFragment;
import com.an2t.android.bouponapp.main.fragments.OffersFragment;
import com.an2t.android.bouponapp.main.fragments.ProfileFragment;
import com.an2t.android.bouponapp.main.fragments.SettingsFragment;
import com.an2t.android.bouponapp.main.fragments.frament_offer.DialogDealRedirect;
import com.an2t.android.bouponapp.main.model.GetSpotlightImages;
import com.an2t.android.bouponapp.main.model.VersionCodeRes;
import com.an2t.android.bouponapp.my_coupon.CoponsActivity;
import com.an2t.android.bouponapp.my_coupon.fragment.DialogDeepLinkSuccees;
import com.an2t.android.bouponapp.my_coupon.model.TransferCouponRes;
import com.an2t.android.bouponapp.recharge.MobileRechargeActivity;
import com.an2t.android.bouponapp.recharge.fragment_free_deals.FreeDealsRechargeFragment;
import com.an2t.android.bouponapp.recharge.model.FreeDealsRes;
import com.an2t.android.bouponapp.services.LoginRetrofitSingleton;
import com.an2t.android.bouponapp.services.RetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.an2t.android.bouponapp.services.TrackGPS;
import com.auth0.android.jwt.JWT;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class
MainActivity extends AppCompatActivity implements FreeDealsRechargeFragment.OnItemSelecetedFreeDealsActvity,
        HomeFragment.ProfileClickImage, DialogDealRedirect.SetNavigatioToHomeFragmentChild,
        HomeFragment.OnSpotLightClickedFragment, HomeFragment.OnRetry, HomeFragment.InvalidToken,
        HomeFragment.SwitchBetweenMainAcFragments, DialogDeepLinkSuccees.OnDeeplinkUIButtonClick {

    private BottomNavigationViewEx bottomNavigationView;
    /*private TrackGPS mTrackGPS;*/

    private static final String TAG = MainActivity.class.getSimpleName();

    private String authToken;
    private String giftToken;
    private Toolbar toolbar;
    private CoordinatorLayout main_container;
    String firstName;
    private TextView tv_toolbar_name;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ShimmerFrameLayout shimmer_view_container;
    private ViewPager viewPager;
    private MenuItem prevMenuItem;
    private RelativeLayout rl_no_connection;
    private Button btn_retry;

    public static int REQUEST_CODE_FOR_LOCATION_BASED_SEARCH = 506;
    private OffersFragment offerObject;
    private String cityName;
    private MenuItem itemLocation;
    private StringBuilder sb_token;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        Log.e(TAG, "onCreate: " + pref.getString("regId", ""));


        getSharedPrefData();
        getInfo();

        // TODO: init Component (D)
        initComponent();

        // TODO: set Listeners  (D)
        checkVersionCode();
        setListeners();
        implementBroadcastReciever();
        showLocation();
        checkAndLoadGiftCoupon();


    }


    private void checkVersionCode() {
        int versionCode = BuildConfig.VERSION_CODE;
        if (versionCode < 12) {

        }
    }

    private void showLocation() {
        TrackGPS mTracker = new TrackGPS(this);
        if (mTracker.canGetLocation()) {
            SharedPreferences sp = getSharedPreferences(getString(R.string.sp_location_file), MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();


            Log.e(TAG, "saveUserLocationInSharedPref: " + mTracker.getLatitude());
            Log.e(TAG, "saveUserLocationInSharedPref: " + mTracker.getLongitude());

            editor.putString(getString(R.string.lat), String.valueOf(mTracker.getLatitude()));
            editor.putString(getString(R.string.lng), String.valueOf(mTracker.getLongitude()));

            editor.apply();

            double userLat = Double.parseDouble(sp.getString(getString(R.string.lat), ""));
            double userLng = Double.parseDouble(sp.getString(getString(R.string.lng), ""));


            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(userLat, userLng, 1);
                if (addresses.size() != 0) {
                    cityName = addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void initComponent() {


        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);
        rl_no_connection = findViewById(R.id.rl_no_connection);
        main_container = findViewById(R.id.main_container);
        btn_retry = findViewById(R.id.btn_retry);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        shimmer_view_container.setBaseAlpha(0.7f);
        shimmer_view_container.setRepeatMode(ObjectAnimator.REVERSE);
        shimmer_view_container.setDropoff(0.7f);
//        shimmer_view_container.setTilt(0);
        shimmer_view_container.setIntensity(0.35f);
        shimmer_view_container.startShimmerAnimation();


        tv_toolbar_name = findViewById(R.id.tv_toolbar_name);
        tv_toolbar_name.setText(StringUtils.capitalize(firstName));
    }


    private void setListeners() {
        // TODO: setting the Tabs in the Bottom Navigation View. -> (D)
        bottomNavigationView.enableItemShiftingMode(false);

        bottomNavigationView.enableShiftingMode(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.menu_offers:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.menu_profile:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.menu_settings:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();

            }
        });

        loadViewPager();

        loadAppVersionCodeRes();


    }

    private void loadAppVersionCodeRes() {

        Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();

        ServicesAPI service = mRetrofit.create(ServicesAPI.class);

        Call<VersionCodeRes> call = service.getVersionCode(sb_token.toString());

        call.enqueue(new Callback<VersionCodeRes>() {
            @Override
            public void onResponse(Call<VersionCodeRes> call, Response<VersionCodeRes> response) {
                VersionCodeRes res = response.body();
                if (res != null) {
                    processVersionCodeRes(res);
                }
            }

            @Override
            public void onFailure(Call<VersionCodeRes> call, Throwable t) {

            }
        });
    }

    private void processVersionCodeRes(VersionCodeRes res) {
        int currentVersionCode = BuildConfig.VERSION_CODE;

        if (res.getVersionCode() != null && !res.getVersionCode().isEmpty()) {
            int resVersionCode = Integer.parseInt(res.getVersionCode());
            if (currentVersionCode < resVersionCode) {
                showUpdateDialog();
            }
        }

    }

    private void showUpdateDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("App not updated");
        alertBuilder.setMessage("Added new features in Application. Please update.");


        alertBuilder.setPositiveButton("Update now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String urlString = "https://play.google.com/store/apps/details?id=com.an2t.android.bouponapp";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialogInterface.dismiss();
            }
        });

        alertBuilder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertBuilder.create().show();
    }

    private void loadData() {
        viewPager.setVisibility(View.VISIBLE);
        rl_no_connection.setVisibility(View.GONE);
        loadViewPager();
    }

    private void loadViewPager() {
        setupViewPager(viewPager);
//        viewPager.setOffscreenPageLimit(3);
    }

    private void setupViewPager(ViewPager viewPager) {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());

        offerObject = new OffersFragment();
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(offerObject);
        adapter.addFragment(new ProfileFragment());
        adapter.addFragment(new SettingsFragment());
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onItemSelectedFreeDealsClickedActvity(FreeDealsRes.Item item, boolean selected) {

        DialogDealRedirect mRedirect = DialogDealRedirect.newInstance(item);
        mRedirect.show(getSupportFragmentManager(), "dailog_show_redirect");
    }


    @Override
    public void profileClickImage(int i) {
        bottomNavigationView.setCurrentItem(i);
    }

    private void getSharedPrefData() {

        sp = getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken), "");
        giftToken = sp.getString(getString(R.string.giftCouponToken), "");



        sb_token = new StringBuilder("Bearer ")
                .append(authToken);
        Log.e(TAG, "authToken: " + authToken  );
        Log.e(TAG, " giftToken " + giftToken);
    }

    private void getInfo() {

//        JWT jwt = new JWT(authToken);
//      firstName = jwt.getClaim("firstName").asString();
        firstName = "Anant";

    }

    private void implementBroadcastReciever() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                final String message = intent.getStringExtra("message");
                final String imageUrl = intent.getStringExtra("image");


                final NotificationUtils notificationUtils = new NotificationUtils(MainActivity.this);

                if (TextUtils.isEmpty(imageUrl)) {
                    notificationUtils.showNotificationMessage("Boupon", message, "2017-07-05 13:05:17", intent);
                } else {

                    final Thread t = new Thread() {
                        @Override
                        public void run() {
                            try {
                                notificationUtils.showNotificationMessage("Boupon", message, "2017-07-05 13:05:17", intent, imageUrl);
                            } finally {

                            }
                        }
                    };
                    t.start();

                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());


    }

    @Override
    public void onClickFrag() {
        startActivity(new Intent(MainActivity.this, MobileRechargeActivity.class));
    }

    @Override
    public void onSpotLightClickedFragment(GetSpotlightImages.Item imageBannerData) {
        DialogDealRedirect mRedirect = DialogDealRedirect.newInstance(imageBannerData);
        mRedirect.show(getSupportFragmentManager(), "dailog_show_redirect");
    }

    @Override
    public void onRetryFragmentShow() {
        viewPager.setVisibility(View.GONE);
        rl_no_connection.setVisibility(View.VISIBLE);
    }


    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(main_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setCancelable(false);
        alertDialog.setTitle("Location not enabled");
        alertDialog.setMessage("Oops you need press 'Allow' to get the nearest deals around you ..!!");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
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
        switch (item.getItemId()) {
            case R.id.menu_city_name:
                intent = new Intent(MainActivity.this, FieldAreaActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FOR_LOCATION_BASED_SEARCH);
                break;
            case R.id.menu_map_pointer:
                intent = new Intent(MainActivity.this, FieldAreaActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FOR_LOCATION_BASED_SEARCH);
                break;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_FOR_LOCATION_BASED_SEARCH) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                viewPager.setCurrentItem(1);


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
                offerObject.seeFieldArea(returnString);
            }
        }
    }

    @Override
    public void invalidToken(String message) {

        SharedPreferences sp = getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.clear();
        if (editor.commit()) {
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("invalid_token", message);
//            startActivity(intent);
//            finish();
        }
    }

    @Override
    public void onSwitchBetweenMainAcFragments() {
        viewPager.setCurrentItem(1);
    }

    private void checkAndLoadGiftCoupon() {

        if (!giftToken.isEmpty()) {
            Retrofit mRetrofit = LoginRetrofitSingleton.getRetrofitInstance();
            ServicesAPI servicesAPI = mRetrofit.create(ServicesAPI.class);

            Call<TransferCouponRes> call = servicesAPI.getTransferRes(sb_token.toString(), giftToken);

            call.enqueue(new Callback<TransferCouponRes>() {
                @Override
                public void onResponse(Call<TransferCouponRes> call, Response<TransferCouponRes> response) {
                    TransferCouponRes res = response.body();
                    if (res != null) {
                        processResForTransfer(res);
                    }
                }

                @Override
                public void onFailure(Call<TransferCouponRes> call, Throwable t) {

                }
            });
        }
    }


    private void processResForTransfer(TransferCouponRes res) {
        if (res.getMessage() != null) {
            Log.e(TAG, "processResForTransfer : giftToken  " + giftToken );
            if (res.getStatus()) {
                edit = sp.edit();
                edit.putString(getString(R.string.giftCouponToken),"");
                edit.apply();

                Log.e(TAG, "processResForTransfer : giftToken  " + giftToken );

                DialogDeepLinkSuccees mRedirect = DialogDeepLinkSuccees.newInstance();
                mRedirect.show(getSupportFragmentManager(), "dailog_show_redirect_deeplink");
            }
        }
    }

    @Override
    public void onDeeplinkUIButtonClick() {
        Intent intent = new Intent(MainActivity.this, CoponsActivity.class);
        startActivity(intent);
    }
}





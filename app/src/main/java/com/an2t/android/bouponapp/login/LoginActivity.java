package com.an2t.android.bouponapp.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.forgot_password.ForgotPasswordActivityOne;
import com.an2t.android.bouponapp.login.model.Login;
import com.an2t.android.bouponapp.main.MainActivity;
import com.an2t.android.bouponapp.register.RegisterActivity;
import com.an2t.android.bouponapp.services.LoginRetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginActivity extends AppCompatActivity {

    private TextView btn_forgot_password;
    private Button btn_register, btn_sign_in;
    private EditText in_password, in_phone_number;
    private ProgressDialog mProgressDialog;
    private RelativeLayout rl_main_container;
    private ShimmerFrameLayout shimmer_view_container;
    private CoordinatorLayout main_container;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sp = getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
        editor = sp.edit();
        getInvalidTokenData();
        setContentView(R.layout.activity_login);

        // TODO: intit Component (D)
        initComponent();


        // TODO: set Listeners (D)
        setListeners();
    }

    private void getInvalidTokenData() {
        Intent intent = getIntent();
        if (intent != null) {
            String invalidTokenMessage = intent.getStringExtra("invalid_token");
            if (invalidTokenMessage != null) {
                Log.e(TAG, "getInvalidTokenData: " + invalidTokenMessage );
            }
        }
    }

    private void initComponent() {
        mProgressDialog = new ProgressDialog(this);
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_register = (Button) findViewById(R.id.btn_register);
        rl_main_container= (RelativeLayout) findViewById(R.id.rl_main_container);

        btn_forgot_password = (TextView) findViewById(R.id.btn_forgot_password);

        in_phone_number = (EditText) findViewById(R.id.in_phone_number);
        in_password = (EditText) findViewById(R.id.in_password);
        main_container= (CoordinatorLayout) findViewById(R.id.main_container);

        shimmer_view_container= (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        shimmer_view_container.setDuration(1000);
        shimmer_view_container.setBaseAlpha(0.6f);
        shimmer_view_container.setIntensity(0.1f);
        shimmer_view_container.startShimmerAnimation();

        setTheFeaturesforTheEditText();
    }

    private void setTheFeaturesforTheEditText() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            in_phone_number.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_mobile), null, null, null);
            in_phone_number.setCompoundDrawablePadding(16);
            in_password.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_lock), null, null, null);
            in_password.setCompoundDrawablePadding(16);
        } else{
            // do something for phones running an SDK before KITKAT
        }

    }

    private void setListeners() {

        // TODO: on Sign In Click (D)
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseMessaging.getInstance().subscribeToTopic("userBoupon");
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                String phone = String.valueOf(in_phone_number.getText());
                String password = String.valueOf(in_password.getText());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
//                if (isValid(phone, password)) {
//                    initaiteLoginProcess(phone, password);
//                }
            }
        });

        // TODO: on Forgot password Click (D)
        btn_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivityOne.class));
            }
        });
        // TODO: on Register Password Click (D)
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        rl_main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
            }
        });
    }

    private void initaiteLoginProcess(String phone, String password) {

        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);


        Retrofit retrofit = LoginRetrofitSingleton.getRetrofitInstance();

        Login login = new Login(phone, password);
        ServicesAPI mServicesAPI = retrofit.create(ServicesAPI.class);

        Call<Login.LoginRes> call = mServicesAPI.sendLoginCredentials(login);

        call.enqueue(new Callback<Login.LoginRes>() {
            @Override
            public void onResponse(Call<Login.LoginRes> call, Response<Login.LoginRes> response) {
                mProgressDialog.dismiss();
                Login.LoginRes res = response.body();
                if (res != null) {
                    processLoginData(res);
                } else {
                    showSnackbar("Sorry, Wrong phone number or password");
                }
            }

            @Override
            public void onFailure(Call<Login.LoginRes> call, Throwable t) {
                mProgressDialog.dismiss();
                showSnackbar(getString(R.string.net_err));
            }
        });
    }


    private boolean isValid(String mobile_no, String password) {
        if (mobile_no.isEmpty() && mobile_no.length() < 10) {
            in_phone_number.setError("Please provide your mobile number correctly");
            return false;
        } else if (password.isEmpty()) {
            in_password.setError("Please provide your password");
            return false;
        }
        return true;

    }


    private void processLoginData(Login.LoginRes res) {
        Login.LoginRes.Message message = res.getMessage();
        Log.e(TAG, "processLoginData: " + message.getToken());

        editor.putString(getString(R.string.userLoginToken), message.getToken());

        if (editor.commit()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void showSnackbar(String message){
        Snackbar snackbar = Snackbar
                .make(main_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}

package com.an2t.android.bouponapp.forgot_password;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.forgot_password.model.OTPModel;
import com.an2t.android.bouponapp.main.MainActivity;
import com.an2t.android.bouponapp.services.LoginRetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.auth0.android.jwt.JWT;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OTPActivity extends AppCompatActivity {

    private Button btn_register,btn_sign_in;
    private EditText in_otp;
    private ProgressDialog mProgressDialog ;
    private CoordinatorLayout main_container;
    public static final String USER_ID= "userId";
    public static final String IS_FROM_REG=  "is_from_reg";
    private boolean isFromReg;
    private String token;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserId();
        getInfo();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp);

        //TODO: init Components -> (D)
        initComponet();

        //TODO: set Listeners -> (D)
        setListners();
    }

    private void getUserId() {
        Intent intent = getIntent();
        if (intent != null) {
            token =  intent.getStringExtra(USER_ID);
            isFromReg =  intent.getBooleanExtra(IS_FROM_REG,false);
        }

    }

    private void setTheFeaturesforTheEditText() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            in_otp.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_mobile), null, null, null);
            in_otp.setCompoundDrawablePadding(16);

        } else{
            // do something for phones running an SDK before KITKAT
        }

    }

    private void setListners() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = in_otp.getText().toString();
                if(!otp.isEmpty()){
                    sentToRegisterActivity(otp);
                }
            }
        });
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initComponet() {
        mProgressDialog = new ProgressDialog(this);
        btn_register = (Button)findViewById(R.id.btn_register_otp);
        btn_sign_in = (Button)findViewById(R.id.btn_sign_in_otp);
        main_container= (CoordinatorLayout) findViewById(R.id.main_container);
        in_otp= (EditText) findViewById(R.id.in_otp);

        setTheFeaturesforTheEditText();
    }



    private void sentToRegisterActivity(String otp) {

        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);

        OTPModel model = new OTPModel(id,otp);

        Retrofit retrofit = LoginRetrofitSingleton.getRetrofitInstance();
        ServicesAPI service  =retrofit.create(ServicesAPI.class);


        Call<OTPModel.OTPModelRes> call = service.getOTPRes(model);

        call.enqueue(new Callback<OTPModel.OTPModelRes>() {
            @Override
            public void onResponse(Call<OTPModel.OTPModelRes> call, Response<OTPModel.OTPModelRes> response) {
                mProgressDialog.dismiss();
                OTPModel.OTPModelRes res =response.body();
                if (res != null) {
                    processResponse(res);
                }
            }
            @Override
            public void onFailure(Call<OTPModel.OTPModelRes> call, Throwable t) {
                mProgressDialog.dismiss();
                showSnackbar(getString(R.string.oops));
            }
        });

    }



    private void processResponse(OTPModel.OTPModelRes res) {
        if(res.getMessage().equals("Success")){
            showAlertDailog();
        }
    }


    private void showAlertDailog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(OTPActivity.this);

        alert.setTitle("Registration Successful ");
        alert.setPositiveButton("proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                saveTokenToSharedPref();

            }
        });
        alert.show();
    }

    private void saveTokenToSharedPref() {
        SharedPreferences sp = getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.userLoginToken),token);
        if(editor.commit()){
            Intent intent = new Intent(OTPActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void getInfo() {
        if(isFromReg){
        JWT jwt = new JWT(token);
            String firstName;
            firstName = jwt.getClaim("firstName").asString();
            id = jwt.getClaim("id").asString();
            String userType = jwt.getClaim("userType").asString();
            String email = jwt.getClaim("email").asString();
            String phoneNumber = jwt.getClaim("phoneNumber").asString();
            String otpVerificationStatus = jwt.getClaim("otpVerificationStatus").asString();
        }
    }

    private void showSnackbar(String message){
        Snackbar snackbar = Snackbar
                .make(main_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}

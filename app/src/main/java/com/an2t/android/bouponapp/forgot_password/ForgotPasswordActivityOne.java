package com.an2t.android.bouponapp.forgot_password;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.forgot_password.model.ForgetPassword;
import com.an2t.android.bouponapp.services.LoginRetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForgotPasswordActivityOne extends AppCompatActivity {

    private Button btn_otp;
    private EditText  in_phone_number;
    private ProgressDialog mProgressDialog;
    private CoordinatorLayout main_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password_one);

        // init component
        initComponent();

        // set Listner
        setListener();
    }



    private void initComponent() {
        mProgressDialog = new ProgressDialog(this);
        btn_otp = (Button)findViewById(R.id.btn_otp);
        in_phone_number= (EditText) findViewById(R.id.in_phone_number);
        main_container= (CoordinatorLayout) findViewById(R.id.main_container);

        setTheFeaturesforTheEditText();
    }

    private void setListener() {
        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumer = in_phone_number.getText().toString();
                if(!TextUtils.isEmpty(phoneNumer)){
                    initateForgotPasswordProcess(phoneNumer);
                }else{
                    in_phone_number.setError("Please provide your phone number");
                }
            }
        });
    }

    private void initateForgotPasswordProcess(String phoneNumer) {

        showProgressDialog();

        ForgetPassword forgetPassword = new ForgetPassword(phoneNumer);

            Retrofit mRetrofit = LoginRetrofitSingleton.getRetrofitInstance();

            ServicesAPI service = mRetrofit.create(ServicesAPI.class);

            Call<ForgetPassword.ForgetPasswrdRes> call = service.forgetPasswordPageOne(forgetPassword);

            call.enqueue(new Callback<ForgetPassword.ForgetPasswrdRes>() {
                @Override
                public void onResponse(Call<ForgetPassword.ForgetPasswrdRes> call, Response<ForgetPassword.ForgetPasswrdRes> response) {
                    mProgressDialog.dismiss();
                    ForgetPassword.ForgetPasswrdRes res = response.body();
                    if (res != null) {
                        processRes(res);
                    }
                }

                @Override
                public void onFailure(Call<ForgetPassword.ForgetPasswrdRes> call, Throwable t) {
                    mProgressDialog.dismiss();
                    showSnackbar(getString(R.string.net_err));
                }
            });


    }

    private void processRes(ForgetPassword.ForgetPasswrdRes res) {
        if(res.getMessage().contains("Success")){
            Intent intent = new Intent(ForgotPasswordActivityOne.this,ChangePasswordPageTwo.class);
            startActivity(intent);
        }
    }

    private void setTheFeaturesforTheEditText() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            in_phone_number.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_mobile), null, null, null);
            in_phone_number.setCompoundDrawablePadding(16);

        } else{
            // do something for phones running an SDK before KITKAT
        }

    }

    private void showProgressDialog(){
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);

    }

    private void showSnackbar(String message){
        Snackbar snackbar = Snackbar
                .make(main_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}

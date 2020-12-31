package com.an2t.android.bouponapp.forgot_password;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.forgot_password.model.ChangePassword;
import com.an2t.android.bouponapp.main.MainActivity;
import com.an2t.android.bouponapp.services.LoginRetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordPageTwo extends AppCompatActivity {


    private EditText in_token,in_password,in_password_confirm;
    private Button btn_proceed;
    private ProgressDialog mProgressDialog;
    private RelativeLayout rl_frame;
    private CoordinatorLayout main_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_password_page_two);

        // init Component
        initComponent();

        // set Listener
        setListerner();
    }



    private void initComponent() {
        mProgressDialog = new ProgressDialog(this);
        in_token = (EditText)findViewById(R.id.in_token);
        in_password= (EditText)findViewById(R.id.in_password);
        in_password_confirm= (EditText)findViewById(R.id.in_password_confirm);
        btn_proceed= (Button) findViewById(R.id.btn_proceed);
        rl_frame= (RelativeLayout) findViewById(R.id.rl_frame);
        main_container= (CoordinatorLayout) findViewById(R.id.main_container);

        setTheFeaturesforTheEditText();
    }

    private void setListerner() {
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = in_token.getText().toString();
                String pass = in_password.getText().toString();
                String con_pass = in_password_confirm.getText().toString();

                if(isValid(token,pass,con_pass)){
                    initaChangePassword(token,pass,con_pass);
                }
            }
        });

        rl_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
            }
        });

    }




    private boolean isValid(String token, String password,String con_pass) {
        if (token.isEmpty()) {
            in_token.setError("Please provide your mobile number correctly");
            return false;
        } else if (password.isEmpty() || password.length() < 6) {
            in_password.setError("Please provide your password, the password must contain at least 6 characters");
            return false;
        }
        else if (con_pass.isEmpty()) {
            in_password.setError("Please provide your confirm password");
            return false;
        }else if (!password.equals(con_pass)) {
            showSnackbar("password and confirm password is not matching");
            return false;
        }
        return true;
    }

    private void initaChangePassword(String token, String pass, String con_pass) {

        showProgressDialog();
        ChangePassword changePassword = new ChangePassword(token,pass,con_pass);
        Retrofit mRetrofit = LoginRetrofitSingleton.getRetrofitInstance();

        ServicesAPI service = mRetrofit.create(ServicesAPI.class);

        Call<ChangePassword.ChangePasswordRes> call = service.forgetPasswordPageTwo(changePassword);

        call.enqueue(new Callback<ChangePassword.ChangePasswordRes>() {
            @Override
            public void onResponse(Call<ChangePassword.ChangePasswordRes> call, Response<ChangePassword.ChangePasswordRes> response) {

                ChangePassword.ChangePasswordRes res = response.body();
                if (call != null) {
                    processChangePasswordRes(res);
                }
            }

            @Override
            public void onFailure(Call<ChangePassword.ChangePasswordRes> call, Throwable t) {
                mProgressDialog.dismiss();
                showSnackbar(getString(R.string.net_err));

            }
        });

    }

    private void processChangePasswordRes(ChangePassword.ChangePasswordRes res) {


        if(res.getMessage().getMessage() !=null &&res.getMessage().getMessage().toLowerCase().equals("success")){
            ChangePassword.ChangePasswordRes.Message message = res.getMessage();
            SharedPreferences sp = getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString(getString(R.string.userLoginToken), message.getToken());

            if (editor.commit()) {
                mProgressDialog.dismiss();
                Intent intent = new Intent(ChangePasswordPageTwo.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }


    private void setTheFeaturesforTheEditText() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            in_token.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_user_his), null, null, null);
            in_token.setCompoundDrawablePadding(16);
            in_password.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_lock), null, null, null);
            in_password.setCompoundDrawablePadding(16);
            in_password_confirm.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_lock), null, null, null);
            in_password_confirm.setCompoundDrawablePadding(16);

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

package com.an2t.android.bouponapp.register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.forgot_password.OTPActivity;
import com.an2t.android.bouponapp.register.model.Register;
import com.an2t.android.bouponapp.services.LoginRetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.an2t.android.bouponapp.R.id.in_phone_number;
import static com.an2t.android.bouponapp.forgot_password.OTPActivity.IS_FROM_REG;
import static com.an2t.android.bouponapp.forgot_password.OTPActivity.USER_ID;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_get_otp, btn_sign_in;
    private EditText in_email, in_phone, in_password,in_name,in_referral_code;
    private RelativeLayout rl_frame;
    private CoordinatorLayout main_container;
    private ProgressDialog mProgressDialog;

    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        // TODO: init Component -> (D)
        initComponent();

        // TODO: set Listeners -> (D)
        setListeners();
    }

    private void initComponent() {
        // TODO: init editText -> (D)
        mProgressDialog = new ProgressDialog(this);
        in_name= (EditText) findViewById(R.id.in_name);
        in_email = (EditText) findViewById(R.id.in_email);
        in_phone = (EditText) findViewById(in_phone_number);
        in_password = (EditText) findViewById(R.id.in_password);
        in_referral_code= (EditText) findViewById(R.id.in_referral_code);
        rl_frame= (RelativeLayout) findViewById(R.id.rl_frame);
        // TODO: init buttons -> (D)
        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_get_otp = (Button) findViewById(R.id.btn_get_otp);
        main_container = (CoordinatorLayout) findViewById(R.id.main_container);

        setTheFeaturesforTheEditText();
    }

    private void setTheFeaturesforTheEditText() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            in_name.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_user_his), null, null, null);
            in_name.setCompoundDrawablePadding(16);
            in_email.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_mail), null, null, null);
            in_email.setCompoundDrawablePadding(16);
            in_phone.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_mobile), null, null, null);
            in_phone.setCompoundDrawablePadding(16);
            in_password.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_lock), null, null, null);
            in_password.setCompoundDrawablePadding(16);
            in_referral_code.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.ic_referral_code), null, null, null);
            in_referral_code.setCompoundDrawablePadding(16);
        } else{
            // do something for phones running an SDK before KITKAT
        }

    }

    private void setListeners() {

        // TODO: on Sign In Click. -> (D)
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // TODO: on get OTP Click. -> (D)
        btn_get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(in_name.getText());
                String email = String.valueOf(in_email.getText());
                String phone = String.valueOf(in_phone.getText());
                String password = String.valueOf(in_password.getText());
                String refferal_code = String.valueOf(in_referral_code.getText());

                if (isValid(name,email, phone, password) && isContainingNumber(phone)) {

                    initialteRegisterProcess(name,email, phone, password,refferal_code);
                }else {
                    showSnackbar(getString(R.string.err_phone));
                }
                // TODO: send the user to the next page
//                startActivity(new Intent(RegisterActivity.this,OTPActivity.class));
//                finish();
            }
        });

        rl_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
            }
        });
    }


    private void initialteRegisterProcess(String name, String email, String phone, String password, String refferal_code) {



        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);

        Register register = getRegisterData(name,email, phone, password,refferal_code);

        Retrofit retrofit = LoginRetrofitSingleton.getRetrofitInstance();
        ServicesAPI service = retrofit.create(ServicesAPI.class);

        Call<Register.RegisterRes> call = service.sendRegisterData(register);

        call.enqueue(new Callback<Register.RegisterRes>() {
            @Override
            public void onResponse(Call<Register.RegisterRes> call, Response<Register.RegisterRes> response) {

                mProgressDialog.dismiss();
                Register.RegisterRes res = response.body();
                if (res != null) {
                    processRegisterData(res);
                }else{
                    showSnackbar(getString(R.string.oops));
                }
            }
            @Override
            public void onFailure(Call<Register.RegisterRes> call, Throwable t) {

                mProgressDialog.dismiss();
                showSnackbar(getString(R.string.oops));
            }
        });
    }

    private void showSnackbar(String message){
        Snackbar snackbar = Snackbar
                .make(main_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }



    private void processRegisterData(Register.RegisterRes res) {

        if (res.getMessage() != null) {
            showSnackbar(res.getMessage());
        }


        if (res.getMessage().equals("Success")) {
            sentToOTPPage(res);
        } else {
            if(res.getMessage() != null){
                showSnackbar(String.valueOf(res.getMessage()));
            }
        }
    }

    private void sentToOTPPage(Register.RegisterRes res) {

        Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
        intent.putExtra(USER_ID, res.getToken());
        intent.putExtra(IS_FROM_REG, true);
        startActivity(intent);
    }

    private Register getRegisterData(String name, String email, String phoneNumber, String password, String refferal_code) {
        //TODO:  name is not added here. because it is not present as the key in the Register API. -> (Not D)
        //TODO: consfirm Passowrd editText is not provided in the UI. -> (Not D)

        if(refferal_code.isEmpty()){
            return new Register(name,email, phoneNumber, password, password);
        }
        else {
            return new Register(name,email, phoneNumber, password, password,refferal_code);
        }
    }

    private boolean isValid(String name, String email, String phone, String password) {

        if(name.isEmpty()){
            in_name.setError(getString(R.string.err_name));
            return false;
        }
        else if (email.isEmpty() || !email.contains("@")) {
            in_email.setError(getString(R.string.err_email));
            return false;
        } else if (phone.isEmpty() || phone.charAt(0) != '0' && phone.length() != 11) {
            in_phone.setError(getString(R.string.err_phone));
            return false;
        } else if (password.isEmpty() || password.length() < 6) {
            in_password.setError(getString(R.string.err_password));
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void showAlertDailog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);

        alert.setTitle("Invalid Phone Number");
        alert.setMessage("Please enter a valid phone number");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    private boolean isContainingNumber(String phone) {

        Log.e(TAG, "isContainingNumber: " + phone.length());
        if(phone.length() == 11){
            if (phone.contains("017") || phone.contains("016") || phone.contains("018") || phone.contains("019")) {
                Log.e(TAG, "if : " + phone );
                return true;
            } else {
                Log.e(TAG, "else : " + phone );
                showAlertDailog();
                return false;
            }
        }else {
            return false;
        }

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


}

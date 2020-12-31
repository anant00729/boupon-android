package com.an2t.android.bouponapp.payment;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.MainActivity;

public class RechargeSuccessActivity extends AppCompatActivity {


    private TextView tv_amount,tv_trans_id,tv_trans_message;
    private String amount,transaction_id,message;
    private ImageView img_back;
    private TextView tv_toolbar_name,tv_status;
    private ImageView img_status;

    public static final String RECHARGE_AMOUNT = "amount";
    public static final String RECHARGE_TRANCSACTION_ID = "transaction_id";
    public static final String RECHARGE_MESSAGE = "message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(R.layout.activity_recharge_success);

        initComponent();

        setListerner();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            amount = intent.getStringExtra(RECHARGE_AMOUNT);
            transaction_id = intent.getStringExtra(RECHARGE_TRANCSACTION_ID);
            message= intent.getStringExtra(RECHARGE_MESSAGE);
        }
    }


    private void initComponent() {

        tv_trans_id = (TextView)findViewById(R.id.tv_trans_id);
        tv_amount = (TextView)findViewById(R.id.tv_amount);
        tv_trans_message= (TextView)findViewById(R.id.tv_trans_message);
        tv_toolbar_name= (TextView)findViewById(R.id.tv_toolbar_name);
        img_back= (ImageView)findViewById(R.id.img_back);
        img_status= (ImageView)findViewById(R.id.img_success);
        tv_status= (TextView) findViewById(R.id.tv_status);

        tv_toolbar_name.setText(getString(R.string.payment_status));

    }

    private void setListerner() {

        tv_trans_id.setText("Transaction Id: " + transaction_id);
        tv_amount.setText("৳ " + amount);
        tv_trans_message.setText(message);

        if(message.contains("unsuccess")){
            img_status.setImageResource(R.drawable.ic_cancel);
            tv_status.setText(getString(R.string.paid_unsccess));
        }else{
            img_status.setImageResource(R.drawable.ic_success_logo);
            tv_status.setText(getString(R.string.paid_successfully));
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendToHomePage();
    }

    private void sendToHomePage() {
        Intent intent = new Intent (RechargeSuccessActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

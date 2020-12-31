package com.an2t.android.bouponapp.payment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.payment.model.RechargeAmountMobile;
import com.google.gson.Gson;

import java.util.HashMap;

import static com.an2t.android.bouponapp.payment.RechargeSuccessActivity.RECHARGE_AMOUNT;
import static com.an2t.android.bouponapp.payment.RechargeSuccessActivity.RECHARGE_MESSAGE;
import static com.an2t.android.bouponapp.payment.RechargeSuccessActivity.RECHARGE_TRANCSACTION_ID;

public class PayActivity extends AppCompatActivity implements WebClient.ProgressListener{

    private WebView web_pay;

    public static final String PAYMENT_URL = "payment_url";
    public static final String PAYMENT_AMOUNT = "payment_amount";
    public static final String PAYMENT_MOBILE_NUMBER = "payment_number";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String PAYMENT_OPERATOR = "payment_operator";
    private static int count = 1;

    private static final String TAG = "PayActivity";
    private String payment_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getPaymentData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        web_pay = (WebView)findViewById(R.id.web_pay);

        final WebSettings webSettings = web_pay.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web_pay.clearHistory();

        web_pay.setWebViewClient(new WebViewClient());
        web_pay.setWebChromeClient(new WebChromeClient());
        //web_pay.setWebChromeClient(new WebClient(this));

        web_pay.getSettings();


        if (payment_url == null) {
            finish();
        }

        web_pay.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                if(payment_url.equals("https://payment.portwallet.com/payment/?invoice=") && count == 1){
                    view.loadUrl(payment_url);
                }

                ++count;

                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                super.onPageFinished(view, url);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    web_pay.evaluateJavascript(
                            "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(final String html) {

                                    Log.e(TAG, "onReceiveValue: URL: " + url);
                                    Log.e(TAG, "onReceiveValue: html: " + html );


                                    if(url.toLowerCase().contains("verify")){
                                        String[] parts = html.split("\\{|\\}");
                                        if(parts.length >1 ){
                                            String[] newParts = parts[1].replaceAll("\\\\","").replaceAll("\"", "").split(",");

                                            for (int i=0;i<newParts.length;i++) {
                                                Log.e(TAG, "onReceiveValue: " +  newParts[i]);
                                            }


                                            HashMap<String,String> map = new HashMap<String, String>(newParts.length*2);

                                            for (String newPart : newParts) {
                                                String[] tempPart = newPart.split(":");
                                                map.put(tempPart[0],tempPart[1]);
                                            }
                                            Gson gson = new Gson();
                                            Log.i(TAG, "SomeText: " +gson.toJson(map));
                                            RechargeAmountMobile mobileconfirmationData = gson.fromJson(gson.toJson(map), RechargeAmountMobile.class);

                                            Log.e(TAG, "onReceiveValue: " + mobileconfirmationData.getMessage());

                                            web_pay.setVisibility(View.GONE);
                                            sendToSuccesspage(mobileconfirmationData);
                                        }

                                    }
                                    // code here
                                }
                            });
                }
            }
        });


        web_pay.loadUrl(payment_url);
    }

    private void sendToSuccesspage(RechargeAmountMobile mobileconfirmationData) {
        Intent intent = new Intent (PayActivity.this,RechargeSuccessActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(RECHARGE_AMOUNT,mobileconfirmationData.getAmount());
        intent.putExtra(RECHARGE_TRANCSACTION_ID,mobileconfirmationData.getTransactionID());
        intent.putExtra(RECHARGE_MESSAGE,mobileconfirmationData.getMessage());
        startActivity(intent);
        finish();
    }

    private void getPaymentData() {
        Intent intent = getIntent();
        if (intent != null) {
         payment_url = intent.getStringExtra(PAYMENT_URL);
        }
    }

    @Override
    public void onUpdateProgress(int progressValue) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(PayActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
    }
}

package com.an2t.android.bouponapp.main.setting_web_view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.adpater.InfoKeyValueAdpater;
import com.an2t.android.bouponapp.main.model.InfoRestList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ContactUsActivity extends AppCompatActivity implements InfoKeyValueAdpater.OnPhoneCall{


    private RecyclerView rv_list_key_value;
    private InfoKeyValueAdpater mAdapter;
    private ArrayList<InfoRestList> infoResList;
    private boolean isContact;
    private ImageView img_term;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private static int PERMISSION_REQUEST_CODE_CALL = 234;
    private CoordinatorLayout main_container;

    private static final String TAG = "ContactUsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getIntentData();
        // initComponent
        initComponent();

        // setListener
        setListener();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            isContact = intent.getBooleanExtra("contact",false);
        }
    }

    private void initComponent() {
        infoResList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");



        rv_list_key_value = (RecyclerView)findViewById(R.id.rv_list_key_value);
        main_container= (CoordinatorLayout) findViewById(R.id.main_container);
        img_term= (ImageView) findViewById(R.id.img_term);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        rv_list_key_value.setHasFixedSize(true);
        rv_list_key_value.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new InfoKeyValueAdpater(this,infoResList,isContact,this);
        rv_list_key_value.setAdapter(mAdapter);
    }

    private void setListener() {



        if(isContact){
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setBackgroundResource(android.R.color.black);
            infoResList.clear();
            infoResList.add(new InfoRestList("Name","Mamun Billah"));
            infoResList.add(new InfoRestList("Email","mb@jatra.co"));
            infoResList.add(new InfoRestList("Phone","+8801713176429"));
        }else{
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
            rv_list_key_value.setPadding(0,0,0,0);
            fab.setVisibility(View.GONE);
            img_term.setVisibility(View.VISIBLE);
            infoResList.clear();
            infoResList.add(new InfoRestList("All airtime sales are final and non-refundable.",""));
            infoResList.add(new InfoRestList("No additional fee or charges are added by PortWallet for airtime recharge service.",""));
            infoResList.add(new InfoRestList("If you are the requester, please wait up to 30 minutes to receive a SMS and email confirmation for the number you have specified.",""));
            infoResList.add(new InfoRestList("PortWallet will assume no liability if the recipients mobile number is entered incorrectly and no refunds will be issued.",""));
            infoResList.add(new InfoRestList("If you have accidentally chosen the wrong connection type, open a support ticket immediately by contacting support.portwallet.com or email support@portwallet.com.",""));
            infoResList.add(new InfoRestList("PortWallet will not reconcile any complaints made after 5 days of the actual transaction. Failure to do so within the maximum stipulated time will be considered successful.",""));

        }
        mAdapter.notifyDataSetChanged();

        setToolbarListner();
    }

    private void setToolbarListner() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onPhoneCallClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkRuntimePermissions();
        } else{
            startPhoneCall();
        }
    }

    @SuppressLint("MissingPermission")
    private void startPhoneCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "+8801713176429"));
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        startActivity(callIntent);
    }

    private void checkRuntimePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                startPhoneCall();
                Log.e(TAG, "checkPermission() T" );
            } else {
                Log.e(TAG, "checkPermission() T" );
                requestPermission();
            }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE_CALL);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_CALL) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startPhoneCall();
            } else {
                showSnackbar("Oops you need press 'Allow' to make call to our support staff member");
            }
        }
    }

    private void showSnackbar(String message){
        Snackbar snackbar = Snackbar
                .make(main_container, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}

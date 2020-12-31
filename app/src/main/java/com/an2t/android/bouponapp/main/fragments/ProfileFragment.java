package com.an2t.android.bouponapp.main.fragments;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.my_coupon.CoponsActivity;
import com.an2t.android.bouponapp.transaction.TransactionMainActivity;
import com.auth0.android.jwt.JWT;
import com.facebook.shimmer.ShimmerFrameLayout;

public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    private String authToken;
    private StringBuilder sb_token;
    private static final String TAG = "ProfileFragment";

    private TextView in_username,in_email;
    private TextView tv_mobile_content;
    private String phoneNumber,email,firstName;
    private LinearLayout ll_trans;
    private ImageView img_trans;
    private ImageView img_coupon_click;
    private LinearLayout ll_coupon;

    private ShimmerFrameLayout shimmer_view_container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        onAttachToParentFragment(getActivity());
        getSharedPrefData();
        getInfo();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initComponent(view);

        loadData();

        return view;
    }

    public void onAttachToParentFragment(Activity activity) {
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnItemSelecetedFreeDealsActvity");
        }
    }

    private void getSharedPrefData() {
        SharedPreferences sp = getContext().getSharedPreferences(getString(R.string.spFileUser), getContext().MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken),"");
        sb_token = new StringBuilder("Bearer ")
                .append(authToken);
    }

    private void initComponent(View view) {
        in_email = (TextView)view.findViewById(R.id.in_email);
        in_username = (TextView)view.findViewById(R.id.in_username);
        tv_mobile_content = (TextView)view.findViewById(R.id.tv_mobile_content);

        ll_trans= (LinearLayout) view.findViewById(R.id.ll_trans);
        img_trans= (ImageView) view.findViewById(R.id.img_trans);
        img_coupon_click= (ImageView) view.findViewById(R.id.img_coupon_click);
        ll_coupon= (LinearLayout) view.findViewById(R.id.ll_coupon);


        shimmer_view_container= (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);

        shimmer_view_container.setBaseAlpha(.9f);
        shimmer_view_container.setRepeatMode(ObjectAnimator.REVERSE);
        shimmer_view_container.setDropoff(0.7f);
        shimmer_view_container.setIntensity(0.35f);
        shimmer_view_container.startShimmerAnimation();

        showOrGideLogo();
        onCoponClick();
        onTransClick();
    }

    private void showOrGideLogo() {
        final int sdk = Build.VERSION.SDK_INT;
        if(sdk < Build.VERSION_CODES.LOLLIPOP) {
            shimmer_view_container.setVisibility(View.GONE);
        }
    }

    private void onCoponClick() {
        ll_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToCouponPage();
            }
        });

        img_coupon_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToCouponPage();
            }
        });
    }

    private void sendToCouponPage() {
        Intent intent = new Intent(getContext(), CoponsActivity.class);
        startActivity(intent);
    }


    private void onTransClick() {
        img_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToTransPage();
            }
        });

        ll_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToTransPage();
            }
        });
    }

    private void sendToTransPage() {
        Intent transIntent = new Intent(getContext(), TransactionMainActivity.class);
        getContext().startActivity(transIntent);
    }


    private void loadData() {
        in_email.setText(email);
        in_username.setText(firstName);
        tv_mobile_content.setText(phoneNumber);
    }


    private void getInfo() {

//        JWT jwt = new JWT(authToken);
//        firstName = jwt.getClaim("firstName").asString();
//        email = jwt.getClaim("email").asString();
//        phoneNumber = jwt.getClaim("phoneNumber").asString();

        firstName = "";
        email = "";
        phoneNumber = "";

    }
}

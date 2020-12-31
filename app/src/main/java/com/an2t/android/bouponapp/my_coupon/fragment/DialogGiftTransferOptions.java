package com.an2t.android.bouponapp.my_coupon.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.fragment.app.DialogFragment;

import com.an2t.android.bouponapp.R;

public class DialogGiftTransferOptions extends DialogFragment {

    public static final String GIFT_URL = "gift_url";
    public static final String ITEM_COUPON_ID = "item_coupon_id";
    public static final String ITEM_COUPON_CODE = "item_coupon_code";
    private DialogCouponFragment.OnRedeemClickDialog mListner;
    public DialogGiftTransferOptions() {
        // Required empty public constructor
    }


    public static DialogGiftTransferOptions newInstance(String shortUrlFinal, String code, String couponId) {
        DialogGiftTransferOptions f = new DialogGiftTransferOptions();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(GIFT_URL, shortUrlFinal);
        args.putString(ITEM_COUPON_CODE, code);
        args.putString(ITEM_COUPON_ID, couponId);
        f.setArguments(args);

        return f;
    }

    public void onAttachToParentFragment(Activity activity) {
        try {
            mListner = (DialogCouponFragment.OnRedeemClickDialog) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnRedeemClickDialog");
        }
    }
    String gift_url,code, couponId;
    private FrameLayout img_contacts,img_apps;
    private ImageButton btn_close;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_dialog_gift_transfer_options, container, false);

        onAttachToParentFragment(getActivity());
        setCancelable(false);

        img_contacts = view.findViewById(R.id.img_contacts);
        img_apps= view.findViewById(R.id.img_apps);
        btn_close= view.findViewById(R.id.btn_close);

        if (getArguments().getString(GIFT_URL) != null) {
            gift_url= getArguments().getString(GIFT_URL);
            code= getArguments().getString(ITEM_COUPON_CODE);
            couponId= getArguments().getString(ITEM_COUPON_ID);
        }
        setListeners();
        return view;
    }

    private void setListeners() {
        img_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.onGiftClickFromOptsContacts(gift_url,code,couponId);
                dismiss();
            }
        });

        img_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.onGiftClickFromOptsApps(gift_url);
                dismiss();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


}

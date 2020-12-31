package com.an2t.android.bouponapp.my_coupon.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.my_coupon.model.CouponCall;
import com.squareup.picasso.Picasso;

import static com.an2t.android.bouponapp.services.RetrofitSingleton.BASE_IMAGE_URL;

public class DialogCouponFragment extends DialogFragment {


    private static final String TAG = DialogCouponFragment.class.getSimpleName();

    public static final String ITEM_COUPON = "item_coupon";
    public static final String HOST_URL = "host_url";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String SHORT_URL = "short_url";




    public DialogCouponFragment() {
        // Required empty public constructor
    }

    public static DialogCouponFragment newInstance(String shortUrlFinal, CouponCall.CouponRes.Coupon mCoupon) {
        DialogCouponFragment f = new DialogCouponFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();

        args.putParcelable(ITEM_COUPON, mCoupon);
        args.putString(SHORT_URL, shortUrlFinal);
        f.setArguments(args);
        return f;
    }

    public void onAttachToParentFragment(Activity activity) {
        try {
            mListner = (OnRedeemClickDialog) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnRedeemClickDialog");
        }
    }

    private CouponCall.CouponRes.Coupon item;

    private ImageView img_for_single_deal,img_back;
    private TextView tv_single_deal_name,tv_single_deal_desc,tv_value;
    private Button btn_redeem,btn_gift,btn_later;
    private OnRedeemClickDialog mListner;
    private StringBuilder sb_url_for_gift;
    private String shortUrlFinal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view =inflater.inflate(R.layout.fragment_dialog, container, false);


        onAttachToParentFragment(getActivity());
        //initComponent
        initComponent(view);
        setListner();
        return view;
    }

    private void setListner() {

        tv_single_deal_name.setText(item.getName());
        tv_single_deal_desc.setText("Coupon Code : " +item.getCode());
        tv_value.setText(String.valueOf(item.getAmount()));

        displayImages();

        btn_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.onRedeemClickedDialog(item);
                dismiss();
            }
        });
        btn_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shortUrlFinal!= null) {
                    mListner.onGiftClick(shortUrlFinal,item);
                    dismiss();
                }
            }
        });
        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    private void displayImages() {
        if(item.getImage().contains("http")){
            Picasso.with(getContext())
                    .load(item.getImage())
                    .into(img_for_single_deal);

            Picasso.with(getContext())
                    .load(item.getImage())
                    .into(img_back);
        }else {
            Picasso.with(getContext())
                    .load(BASE_IMAGE_URL+item.getImage())
                    .into(img_for_single_deal);

            Picasso.with(getContext())
                    .load(BASE_IMAGE_URL+ item.getImage())
                    .into(img_back);
        }
    }

    private void initComponent(View view) {
        img_for_single_deal = (ImageView)view.findViewById(R.id.img_for_single_deal);
        tv_single_deal_name= (TextView) view.findViewById(R.id.tv_single_deal_name);
        tv_single_deal_desc= (TextView) view.findViewById(R.id.tv_single_deal_desc);
        tv_value= (TextView) view.findViewById(R.id.tv_value);
        btn_redeem= (Button) view.findViewById(R.id.btn_redeem);
        img_back= (ImageView) view.findViewById(R.id.img_back);
        btn_gift= (Button) view.findViewById(R.id.btn_gift);
        btn_later= (Button) view.findViewById(R.id.btn_later);

        if (getArguments() != null) {
            item = getArguments().getParcelable(ITEM_COUPON);
            shortUrlFinal = getArguments().getString(SHORT_URL);
            Log.e(TAG, "item: " + item.getShareToken());
        }
    }

    public interface OnRedeemClickDialog{
        void onRedeemClickedDialog(CouponCall.CouponRes.Coupon item);
        void onGiftClick(String shortUrlFinal, CouponCall.CouponRes.Coupon item);
        void onGiftClickFromOptsContacts(String gift_url, String code, String shortUrlFinal);
        void onGiftClickFromOptsApps(String shortUrlFinal);
    }


}

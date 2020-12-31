package com.an2t.android.bouponapp.main.fragments.frament_offer;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.model.GetSpotlightImages;
import com.an2t.android.bouponapp.recharge.model.FreeDealsRes;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.an2t.android.bouponapp.recharge.fragment_free_deals.DialogFreeDealsFragment.ITEM_FREE_DEAL;
import static com.an2t.android.bouponapp.recharge.fragment_free_deals.DialogFreeDealsFragment.ITEM_SPOTLIGHT;
import static com.an2t.android.bouponapp.services.RetrofitSingleton.BASE_IMAGE_URL;

public class DialogDealRedirect extends DialogFragment {


    public DialogDealRedirect() {
        // Required empty public constructor
    }

    private CircleImageView img_for_single_deal;
    private TextView tv_single_deal_name, tv_single_deal_desc;
    private ImageView img_back;
    private TextView tv_single_deal_value;
    private Button btn_continue, btn_recharge;
    private SetNavigatioToHomeFragmentChild mListner;
    private TextView tv_expires_data, tv_tac, tv_store, tv_store_content, tv_category_content;


    public static DialogDealRedirect newInstance(FreeDealsRes.Item item) {
        DialogDealRedirect f = new DialogDealRedirect();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable(ITEM_FREE_DEAL, item);


        f.setArguments(args);

        return f;
    }

    public static DialogDealRedirect newInstance(GetSpotlightImages.Item item) {
        DialogDealRedirect f = new DialogDealRedirect();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable(ITEM_SPOTLIGHT, item);


        f.setArguments(args);

        return f;
    }

    public void onAttachToParentFragment(Activity activity) {
        try {
            mListner = (SetNavigatioToHomeFragmentChild) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement SetNavigatioToHomeFragmentChild");
        }
    }

    private FreeDealsRes.Item item;
    private GetSpotlightImages.Item itemSpotLight;

    private static final String TAG = "DialogDealRedirect";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getArguments().getParcelable(ITEM_FREE_DEAL);
        itemSpotLight = getArguments().getParcelable(ITEM_SPOTLIGHT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_dialog_deal_redirect, container, false);
        img_for_single_deal = (CircleImageView) view.findViewById(R.id.img_for_single_deal);
        tv_single_deal_name = (TextView) view.findViewById(R.id.tv_single_deal_name);
        tv_single_deal_desc = (TextView) view.findViewById(R.id.tv_single_deal_desc);
        tv_single_deal_value = (TextView) view.findViewById(R.id.tv_single_deal_value);
        tv_expires_data = (TextView) view.findViewById(R.id.tv_expires_data);
        tv_tac = (TextView) view.findViewById(R.id.tv_tac);
        btn_recharge = (Button) view.findViewById(R.id.btn_recharge);
        btn_continue = (Button) view.findViewById(R.id.btn_continue);
        img_back = (ImageView) view.findViewById(R.id.img_back);
        tv_store = (TextView) view.findViewById(R.id.tv_store);
        tv_store_content = (TextView) view.findViewById(R.id.tv_store_content);
        tv_category_content = (TextView) view.findViewById(R.id.tv_category_content);

        onAttachToParentFragment(getActivity());


        if (item != null) {

            tv_single_deal_name.setText(item.getTitle());
            tv_single_deal_desc.setText(Html.fromHtml(item.getLongDescription()).toString());
            setCategoryData(String.valueOf(item.getCategory()));


            tv_single_deal_value.setText("Points : " + item.getValue());

            SimpleDateFormat current_format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat changed_format = new SimpleDateFormat("dd-MM-yyyy");
            // TODO: Display Expiry Date in Current format -> (D)
            try {
                String date = changed_format.format(current_format.parse(item.getExpiry()));
                if (date != null) {
                    tv_expires_data.setText(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Picasso.with(getContext())
                    .load(BASE_IMAGE_URL + item.getMainImage())
                    .resize(250, 180)
                    .placeholder(R.drawable.view_pager_banner_back)
                    .into(img_for_single_deal);

            Picasso.with(getContext())
                    .load(BASE_IMAGE_URL + item.getMainImage())
                    .resize(420, 390)
                    .placeholder(R.drawable.view_pager_banner_back)
                    .into(img_back);

            tv_tac.setText(Html.fromHtml(item.getTnc()).toString());



            if (item != null && item.getParentMerchantId() != null) {
                if (item.getParentMerchantId().getStoreName() != null && item.getParentMerchantId().getStoreAddress() != null && item.getParentMerchantId().getStorePhoneNumber() != null) {
                    StringBuilder sb_store_content = new StringBuilder("Store Name : ")
                            .append(String.valueOf(item.getParentMerchantId().getStoreName()))
                            .append("\n\nStore Address : ")
                            .append(String.valueOf(item.getParentMerchantId().getStoreAddress()))
                            .append("\n\nStore Phone : ")
                            .append(String.valueOf(item.getParentMerchantId().getStorePhoneNumber()));
                    tv_store_content.setText(sb_store_content.toString());
                }
            }

        } else if (itemSpotLight != null) {

            tv_store_content.setVisibility(View.GONE);
            tv_store.setVisibility(View.GONE);
            tv_single_deal_name.setText(itemSpotLight.getTitle());
            tv_single_deal_desc.setText(Html.fromHtml(itemSpotLight.getLongDescription()).toString());
            tv_single_deal_value.setText("Points : " + itemSpotLight.getValue());


            setCategoryData(String.valueOf(itemSpotLight.getCategory()));
            SimpleDateFormat current_format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat changed_format = new SimpleDateFormat("dd-MM-yyyy");
            // TODO: Display Expiry Date in Current format -> (D)
            try {
                String date = changed_format.format(current_format.parse(itemSpotLight.getExpiry()));
                if (date != null) {
                    tv_expires_data.setText(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Glide.with(getContext())
                    .load(BASE_IMAGE_URL + itemSpotLight.getMainImage())
                    .into(img_for_single_deal);

            Glide.with(getContext())
                    .load(BASE_IMAGE_URL + itemSpotLight.getMainImage())
                    .into(img_back);

            tv_tac.setText(Html.fromHtml(itemSpotLight.getTnc()).toString());

        }

        onRechargeClick();
        onContinueClick();


        return view;
    }

    private void setCategoryData(String s) {

        if (s.equals("foodsandcafe")) {
            tv_category_content.setText("Foods And Cafe");
        } else if (s.equals("lifestyle")) {
            tv_category_content.setText("Lifestyle");
        } else if (s.equals("beautyandsaloon")) {
            tv_category_content.setText("Beauty And Saloon");
        } else if (s.equals("electronics")) {
            tv_category_content.setText("Electronics");
        } else if (s.equals("outdoorandsports")) {
            tv_category_content.setText("Outdoor And Sports");
        } else if (s.equals("Hotels")) {
            tv_category_content.setText("Hotel");
        } else {
            tv_category_content.setText("All");
        }

    }


    private void onRechargeClick() {
        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mListner.onClickFrag();
            }
        });
    }

    private void onContinueClick() {
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface SetNavigatioToHomeFragmentChild {
        void onClickFrag();
    }
}

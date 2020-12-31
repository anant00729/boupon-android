package com.an2t.android.bouponapp.recharge.fragment_free_deals;


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
import com.an2t.android.bouponapp.recharge.model.FreeDealsRes;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.an2t.android.bouponapp.services.RetrofitSingleton.BASE_IMAGE_URL;

public class DialogFreeDealsFragment extends DialogFragment {

    public static final String ITEM_FREE_DEAL = "item_free_deal";
    public static final String ITEM_SPOTLIGHT= "item_spotlight";
    public static final String ITEM_PAYMENT_AMOUNT = "payment_amt";
    public static final String ITEM_TOTAL_VALUE = "payment_value";

    private DialogItemAdd mDialogItemAddedListener;

    public void onAttachToParentFragment(Activity activity) {
        try {
            mDialogItemAddedListener = (DialogItemAdd) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement AddItemsToTheOfferClass");
        }
    }

    public static DialogFreeDealsFragment newInstance(FreeDealsRes.Item item, String payment_amt, String value) {
        DialogFreeDealsFragment f = new DialogFreeDealsFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable(ITEM_FREE_DEAL, item);
        args.putString(ITEM_PAYMENT_AMOUNT, payment_amt);
        args.putString(ITEM_TOTAL_VALUE, value);
        f.setArguments(args);

        return f;
    }

    private FreeDealsRes.Item item;
    private String payment_amt_str;
    private String payment_value;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getArguments().getParcelable(ITEM_FREE_DEAL);
        payment_amt_str = getArguments().getString(ITEM_PAYMENT_AMOUNT);
        payment_value = getArguments().getString(ITEM_TOTAL_VALUE);
    }

    private ImageView img_for_single_deal,img_back;
    private TextView tv_single_deal_name, tv_single_deal_desc, tv_value, tv_amount,tv_single_deal_value,tv_expires_data,tv_tac,tv_store_content;
    private Button btn_add_item;
    int left;
    boolean isFirstTime;

    private static final String TAG = "DialogFreeDealsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_dialog_free_deals, container, false);

        onAttachToParentFragment(getActivity());
        img_for_single_deal = (ImageView) view.findViewById(R.id.img_for_single_deal);
        tv_single_deal_desc = (TextView) view.findViewById(R.id.tv_single_deal_desc);
        tv_single_deal_name = (TextView) view.findViewById(R.id.tv_single_deal_name);
        tv_amount = (TextView) view.findViewById(R.id.tv_amount);
        tv_value = (TextView) view.findViewById(R.id.tv_value);
        tv_single_deal_value= (TextView)view.findViewById(R.id.tv_single_deal_value);
        tv_expires_data= (TextView)view.findViewById(R.id.tv_expires_data);
        tv_tac= (TextView)view.findViewById(R.id.tv_tac);
        tv_store_content= (TextView)view.findViewById(R.id.tv_store_content);

        btn_add_item = (Button) view.findViewById(R.id.btn_add_item);
        img_back= (ImageView) view.findViewById(R.id.img_back);

        tv_single_deal_name.setText(item.getTitle());
        tv_single_deal_desc.setText(Html.fromHtml(item.getLongDescription()).toString());

        tv_single_deal_value.setText("Value : " + item.getValue());

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


        if(Integer.parseInt(payment_amt_str)-Integer.parseInt(payment_value)>=0){

            if(Integer.parseInt(payment_amt_str)-Integer.parseInt(payment_value)>=0){
                tv_amount.setText(String.valueOf(Integer.parseInt(payment_amt_str)-Integer.parseInt(payment_value)));
            }
        }else{
            left = Integer.parseInt(payment_amt_str)-Integer.parseInt(payment_value);
            tv_amount.setText(getString(R.string.inssuficient_amount));
            tv_amount.setTextSize(14);
        }

        tv_value.setText(item.getValue());
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

        if (item.getParentMerchantId().getStoreName() != null && item.getParentMerchantId().getStoreAddress() != null && item.getParentMerchantId().getStorePhoneNumber() != null) {
            StringBuilder sb_store_content = new StringBuilder("Store Name : ")
                    .append(item.getParentMerchantId().getStoreName())
                    .append("\n\nStore Address : ")
                    .append(item.getParentMerchantId().getStoreAddress())
                    .append("\n\nStore Phone : ")
                    .append(item.getParentMerchantId().getStorePhoneNumber());
            tv_store_content.setText(sb_store_content.toString());
        }
        onAddClick();

        return view;
    }

    private void onAddClick() {
        btn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogItemAddedListener.onDialogItemAddClicked(item);
                dismiss();
            }
        });
    }

    public interface DialogItemAdd {
        void onDialogItemAddClicked(FreeDealsRes.Item item);
    }
}

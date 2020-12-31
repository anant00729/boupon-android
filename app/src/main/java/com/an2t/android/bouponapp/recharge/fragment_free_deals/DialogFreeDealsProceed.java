package com.an2t.android.bouponapp.recharge.fragment_free_deals;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.payment.PayActivity;
import com.an2t.android.bouponapp.recharge.adapter.ProceedItemsAdpater;
import com.an2t.android.bouponapp.recharge.model.FreeDealsRes;
import com.an2t.android.bouponapp.recharge.model.InitiateRecharge;
import com.an2t.android.bouponapp.services.RetrofitSingleton;
import com.an2t.android.bouponapp.services.ServicesAPI;
import com.auth0.android.jwt.JWT;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.an2t.android.bouponapp.payment.PayActivity.PAYMENT_URL;

public class DialogFreeDealsProceed extends DialogFragment implements ProceedItemsAdpater.OnItemRemoved {

    public DialogFreeDealsProceed() {
        // Required empty public constructor
    }

    public static final String PROCEED_DEALS_LIST = "proceed_item_list";
    public static final String PROCEED_AMOUNT = "proceed_item_amt";
    public static final String PROCEED_VALUE = "proceed_item_value";
    public static final String PROCEED_PAYMENT_TYPE = "proceed_payment_type";
    public static final String PROCEED_PAYMENT_OPERATOR = "proceed_payment_operator";
    public static final String PROCEED_PAYMENT_URL = "proceed_payment_url";
    public static final String PROCEED_PAYMENT_PHONE = "proceed_payment_phone";
    private ArrayList<FreeDealsRes.Item> items;
    private static final String TAG = "DialogFreeDealsProceed";
    private RecyclerView rv_item_selected;
    private ProceedItemsAdpater mAdapter;
    private ShowSackBar mShowSnackListner;
    private ProgressBar progress_bar;
    private String authToken;
    private int amount, value;
    private String firstName, email, userId;
    private String payment_type, payment_operator, payment_url, payment_number;
    int total_value;
    private NotifyActivityValue notifyActivityValue;
    private StringBuilder sb_token;


    public void onAttachToParentFragment(Activity activity) {
        try {
            mShowSnackListner = (ShowSackBar) activity;
            notifyActivityValue= (NotifyActivityValue) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement ShowSackBar");
        }
    }


    public static DialogFreeDealsProceed newInstance(ArrayList<FreeDealsRes.Item> items, String payment_amt, int value, String payment_type, String payment_operator, String payment_url, String payment_number) {
        DialogFreeDealsProceed f = new DialogFreeDealsProceed();

        Bundle args = new Bundle();
        args.putParcelableArrayList(PROCEED_DEALS_LIST, items);
        args.putInt(PROCEED_AMOUNT, Integer.parseInt(payment_amt));
        args.putInt(PROCEED_VALUE, value);
        args.putString(PROCEED_PAYMENT_TYPE, payment_type);
        args.putString(PROCEED_PAYMENT_OPERATOR, payment_operator);
        args.putString(PROCEED_PAYMENT_URL, payment_url);
        args.putString(PROCEED_PAYMENT_PHONE, payment_number);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onAttachToParentFragment(getActivity());
        items = new ArrayList<>();
        if (getArguments() != null) {
            items = getArguments().getParcelableArrayList(PROCEED_DEALS_LIST);
            amount = getArguments().getInt(PROCEED_AMOUNT);
            value = getArguments().getInt(PROCEED_VALUE);
            payment_type = getArguments().getString(PROCEED_PAYMENT_TYPE);
            payment_operator = getArguments().getString(PROCEED_PAYMENT_OPERATOR);
            payment_url = getArguments().getString(PROCEED_PAYMENT_URL);
            payment_number = getArguments().getString(PROCEED_PAYMENT_PHONE);

            Log.e(TAG, "onCreate: " +  payment_number );
        }
    }

    private void getUserToken() {
        SharedPreferences sp = getContext().getSharedPreferences(getString(R.string.spFileUser), getContext().MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken), "");
        sb_token = new StringBuilder("Bearer ")
                .append(authToken);
    }

    private TextView tv_total_amount, tv_value;
    private Button btn_recharge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getUserToken();
        getInfo();

        View view = inflater.inflate(R.layout.fragment_dialog_free_deals_proceed, container, false);
        rv_item_selected = (RecyclerView) view.findViewById(R.id.rv_item_selected);
        tv_total_amount = (TextView) view.findViewById(R.id.tv_total_amount);
        tv_value = (TextView) view.findViewById(R.id.tv_value);
        btn_recharge = (Button) view.findViewById(R.id.btn_recharge);
        progress_bar= (ProgressBar) view.findViewById(R.id.progress_bar);

        tv_total_amount.setText(String.valueOf(amount));

        showTotalValue();

        rv_item_selected.setHasFixedSize(true);
        rv_item_selected.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new ProceedItemsAdpater(getContext(), items, this);

        if (items.size() > 5) {
            ViewGroup.LayoutParams params = rv_item_selected.getLayoutParams();
            params.height = 600;
            rv_item_selected.setLayoutParams(params);
        }

        onRechargeClick();

        rv_item_selected.setAdapter(mAdapter);
        return view;
    }

    private void showProgress() {
        progress_bar.setVisibility(View.VISIBLE);
        btn_recharge.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progress_bar.setVisibility(View.GONE);
        btn_recharge.setVisibility(View.VISIBLE);
    }

    private void showTotalValue() {
        for (FreeDealsRes.Item item : items) {
            total_value += Integer.parseInt(item.getValue());
        }
        tv_value.setText(String.valueOf(amount - total_value));

    }

    private void onRechargeClick() {
        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initateRechargeProcess();
            }
        });
    }

    @Override
    public void onItemRemoved(FreeDealsRes.Item item) {

        total_value = total_value - Integer.parseInt(item.getValue());
        tv_value.setText(String.valueOf(amount - total_value));
        items.remove(item);
        mAdapter.notifyDataSetChanged();

        notifyActivityValue.onNotifyActivityValue(item.getValue());

        if (items.size() == 0) {
            dismiss();
            mShowSnackListner.showSnackBarToAddDeals();
        }
    }

    public interface ShowSackBar {
        void showSnackBarToAddDeals();
    }

    private void initateRechargeProcess() {

        showProgress();

        List<InitiateRecharge.Deals> mDealList = new ArrayList<>();
        for (FreeDealsRes.Item item : items) {
            InitiateRecharge.Deals deals = new InitiateRecharge.Deals(item.getId(),item.getSku() , item.getTitle(), item.getValue(), item.getMainImage(), item.getExpiry());
            mDealList.add(deals);
        }


        // TODO : Ask to enter which person's Details needs to be send when recharge is going to happen
        InitiateRecharge mRecharge = new InitiateRecharge(payment_operator, firstName, String.valueOf(amount), email, payment_number, payment_type, userId, mDealList);
        Retrofit mRetrofit = RetrofitSingleton.getRetrofitInstance();

        ServicesAPI service = mRetrofit.create(ServicesAPI.class);

        Call<InitiateRecharge.InitiateRechargeRes> call = service.getRechargeRes(mRecharge, sb_token.toString());

        call.enqueue(new Callback<InitiateRecharge.InitiateRechargeRes>() {
            @Override
            public void onResponse(Call<InitiateRecharge.InitiateRechargeRes> call, Response<InitiateRecharge.InitiateRechargeRes> response) {
                InitiateRecharge.InitiateRechargeRes res = response.body();
                if (res != null) {
                    proceesRes(res);
                }
            }

            @Override
            public void onFailure(Call<InitiateRecharge.InitiateRechargeRes> call, Throwable t) {
                hideProgress();

            }
        });
    }

    private void proceesRes(InitiateRecharge.InitiateRechargeRes res) {
        if (res.getPaymentUrl() != null && !res.getPaymentUrl().isEmpty()) {
            Intent payIntent = new Intent(getContext(), PayActivity.class);
            payIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            payIntent.putExtra(PAYMENT_URL, res.getPaymentUrl());
            getActivity().startActivity(payIntent);
            getActivity().finish();
        }
    }


    private void getInfo() {
        JWT jwt = new JWT(authToken);
        firstName = jwt.getClaim("firstName").asString();
        userId = jwt.getClaim("id").asString();
        email = jwt.getClaim("email").asString();
    }

    public interface NotifyActivityValue{
        void onNotifyActivityValue(String value);
    }


}

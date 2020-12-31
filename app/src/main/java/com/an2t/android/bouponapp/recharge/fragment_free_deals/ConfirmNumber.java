package com.an2t.android.bouponapp.recharge.fragment_free_deals;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.recharge.FreeDealsRechargeActivity;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.an2t.android.bouponapp.helpers.Constants.OPERATOR_ITEMS;
import static com.an2t.android.bouponapp.payment.PayActivity.PAYMENT_AMOUNT;
import static com.an2t.android.bouponapp.payment.PayActivity.PAYMENT_MOBILE_NUMBER;
import static com.an2t.android.bouponapp.payment.PayActivity.PAYMENT_OPERATOR;
import static com.an2t.android.bouponapp.payment.PayActivity.PAYMENT_TYPE;

public class ConfirmNumber extends Fragment {


    public static final String PHONE_NUMBER = "phone_number";
    public static final String AMOUNT = "amount";
    public static final String RECHARGE_TYPE  = "recharge_type";
    public static final String PROVIDER  = "provider";
    private String phoneNumber;

    private TextView tv_confirm_number;
    private TextView tv_change;
    private Spinner spin_operator;
    private Button btn_recharge;
    private RadioGroup radio_group_for_payment_mode;
    private List<String> mListOfOperators= OPERATOR_ITEMS;
    private EditText in_phone_number;
    private RadioButton btn_radio_mode_payment;
    private String mode_of_payment, operator, amount;
    private String authToken,userId,userEmail,rechargeType,provider;
    private OnChangeClick mChangeListner;
    private ShowProvideValidFields showProvideValidFields;
    private CardView card_recharge;

    private static final String TAG = "ConfirmNumber";

    public ConfirmNumber() {
        // Required empty public constructor
    }

    public static ConfirmNumber newInstance(String phone_number,String amount,String rechargeType,String provider){
        ConfirmNumber mConfirmNumber = new ConfirmNumber();
        Bundle bundle = new Bundle();
        bundle.putString(PHONE_NUMBER, phone_number);
        bundle.putString(AMOUNT, amount);
        bundle.putString(RECHARGE_TYPE, rechargeType);
        bundle.putString(PROVIDER, provider);
        mConfirmNumber.setArguments(bundle);
        return mConfirmNumber;
    }

    public static ConfirmNumber newInstance(String phone_number){
        ConfirmNumber mConfirmNumber = new ConfirmNumber();
        Bundle bundle = new Bundle();
        bundle.putString(PHONE_NUMBER, phone_number);
        mConfirmNumber.setArguments(bundle);
        return mConfirmNumber;
    }

    public void onAttachToParentFragment(Activity activity) {
        try {
            mChangeListner = (OnChangeClick) activity;
            showProvideValidFields= (ShowProvideValidFields) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement AddItemsToTheOfferClass");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phoneNumber = getArguments().getString(PHONE_NUMBER);
            amount = getArguments().getString(AMOUNT);
            rechargeType = getArguments().getString(RECHARGE_TYPE);
            provider = getArguments().getString(PROVIDER);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getSharedPrefData();
        onAttachToParentFragment(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_number, container, false);
        tv_confirm_number = (TextView)view.findViewById(R.id.tv_confirm_number);
        tv_change= (TextView)view.findViewById(R.id.tv_change);
        spin_operator = (Spinner)view.findViewById(R.id.spin_operator);
        btn_recharge = (Button) view.findViewById(R.id.btn_recharge);
        radio_group_for_payment_mode= (RadioGroup) view.findViewById(R.id.radio_group_for_payment_mode);
        in_phone_number= (EditText) view.findViewById(R.id.in_phone_number);
        card_recharge= (CardView) view.findViewById(R.id.card_recharge);
        tv_confirm_number.setText(phoneNumber);


        card_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeys();
            }
        });

        radio_group_for_payment_mode.check(R.id.btn_prepaid);
        hideSoftKeys();

        return view;
    }

    private void hideSoftKeys() {
            if (getActivity().getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
    }

    private void getSharedPrefData() {
        SharedPreferences sp = getContext().getSharedPreferences(getString(R.string.spFileUser), MODE_PRIVATE);
        authToken = sp.getString(getString(R.string.userLoginToken),"");
        userId = sp.getString(getString(R.string.userLoginID),"");
        userEmail = sp.getString(getString(R.string.userLoginEmail),"");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListners();

    }

    private boolean checkPhoneAndSetOperator() {

        String phoneKey = phoneNumber.substring(0,3);

        Log.e(TAG, "checkPhoneAndSetOperator: " + " phone number " + phoneNumber + " phone key " + phoneKey );

        if(phoneKey.equals("017")){
            spin_operator.setSelection(OPERATOR_ITEMS.indexOf("Grameen Phone"));
            return true;
        }else if (phoneKey.equals("018")){
            spin_operator.setSelection(OPERATOR_ITEMS.indexOf("Robi"));
            return true;
        }else if (phoneKey.equals("019")){
            spin_operator.setSelection(OPERATOR_ITEMS.indexOf("Banglalink"));
            return true;
        }else if (phoneKey.equals("016")) {
            spin_operator.setSelection(OPERATOR_ITEMS.indexOf("Airtel"));
            return true;
        }
//        }else if (phoneKey.equals("015")){
//            spin_operator.setSelection(OPERATOR_ITEMS.indexOf("Teletalk"));
//            return true;
//        }
        return false;
    }

    private void setListners() {
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChangeListner.onChangeClick();
            }
        });
        ArrayAdapter<String> adpater = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,mListOfOperators);
        spin_operator.setAdapter(adpater);

        checkPhoneAndSetOperator();

        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeys();
                int selectedId = radio_group_for_payment_mode.getCheckedRadioButtonId();
                btn_radio_mode_payment = (RadioButton)getView().findViewById(selectedId);
                mode_of_payment = btn_radio_mode_payment.getText().toString();
                operator = spin_operator.getSelectedItem().toString();
                amount = in_phone_number.getText().toString();

                if(!amount.isEmpty() && Integer.parseInt(amount) > 1000){
                    in_phone_number.setText("");
                    showProvideValidFields.showAmountCantBeProceed("You cannot recharge more than 1000 taka. Please enter amount again !!");
                    return;
                }
                if (btn_radio_mode_payment.getText().toString().equals("postpaid") && Integer.parseInt(amount) < 50 ){
                    Log.e(TAG, "onClick: "  + btn_radio_mode_payment.getText().toString() +"\t "  +amount);
                    in_phone_number.setText("");
                    showProvideValidFields.showAmountCantBeProceed("The minimum amount is 50 taka for postpaid. please recharge with minimum 50 taka.");
                    return;
                }

                if(!mode_of_payment.isEmpty() && !operator.isEmpty() && !amount.isEmpty() && !operator.equals("Select Operator") ){
                    if(checkPhoneAndSetOperator()){
                        proceesRes();
                    }
                }else{
                    showProvideValidFields.showProvideValidFields(phoneNumber);

                }
            }
        });
        if (phoneNumber != null && amount != null) {
            in_phone_number.setText(amount);
            in_phone_number.setSelection(in_phone_number.getText().toString().length());
            tv_confirm_number.setText(phoneNumber);
        }
    }
    private void proceesRes() {
            Intent payIntent = new Intent(getContext(), FreeDealsRechargeActivity.class);
            payIntent.putExtra(PAYMENT_AMOUNT,amount);
            payIntent.putExtra(PAYMENT_MOBILE_NUMBER,phoneNumber);
            payIntent.putExtra(PAYMENT_OPERATOR,operator);
            payIntent.putExtra(PAYMENT_TYPE,mode_of_payment);
            getContext().startActivity(payIntent);
        }
    public interface OnChangeClick{
        public void onChangeClick();
    }

    public interface ShowProvideValidFields{
        void showProvideValidFields(String phoneNumber);
        void showAmountCantBeProceed(String s);
    }
}





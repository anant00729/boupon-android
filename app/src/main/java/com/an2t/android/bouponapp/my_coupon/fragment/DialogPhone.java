package com.an2t.android.bouponapp.my_coupon.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.helpers.Constants;

import static com.an2t.android.bouponapp.my_coupon.fragment.DialogGiftTransferOptions.GIFT_URL;
import static com.an2t.android.bouponapp.my_coupon.fragment.DialogGiftTransferOptions.ITEM_COUPON_CODE;
import static com.an2t.android.bouponapp.my_coupon.fragment.DialogGiftTransferOptions.ITEM_COUPON_ID;
import static com.an2t.android.bouponapp.recharge.fragment_free_deals.AddNumber.PERMISSION_REQUEST_CODE;

public class DialogPhone extends DialogFragment {


    public DialogPhone() {
        // Required empty public constructor
    }

    public static DialogPhone newInstance(String giftUrl, String code, String couponId) {
        DialogPhone f = new DialogPhone();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(GIFT_URL, giftUrl);
        args.putString(ITEM_COUPON_CODE, code);
        args.putString(ITEM_COUPON_ID, couponId);
        f.setArguments(args);

        return f;
    }
    private OnGiftClick onGiftClick;
    private Button btn_next;
    private ImageView img_btn_contact_list,img_btn_close;
    private EditText in_phone_number;
    private ImageButton btn_close;

    public void onAttachToParentFragment(Activity activity) {
        try {
            onGiftClick = (OnGiftClick) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnRedeemClickDialog");
        }
    }
    private String gift_url,code, couponId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view =inflater.inflate(R.layout.fragment_dialog_phone, container, false);
        onAttachToParentFragment(getActivity());
        setCancelable(false);

        btn_next = view.findViewById(R.id.btn_next);
        img_btn_contact_list = view.findViewById(R.id.img_btn_contact_list);
        in_phone_number=  view.findViewById(R.id.in_phone_number);
        img_btn_close = view.findViewById(R.id.img_btn_close);
        btn_close= view.findViewById(R.id.btn_close);

        btn_next.setText(getString(R.string.gift));

        if (getArguments() != null) {
            gift_url = getArguments().getString(GIFT_URL);
            code= getArguments().getString(ITEM_COUPON_CODE);
            couponId= getArguments().getString(ITEM_COUPON_ID);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListeners();
        checkEditTextContent();
    }

    private void setListeners() {

        in_phone_number.setSelection(in_phone_number.getText().toString().length());

        img_btn_contact_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    checkRuntimePermissions();
                } else{
                    goToContactsPage();
                }
            }
        });
        in_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                img_btn_close.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    img_btn_close.setVisibility(View.GONE);
                }
                else{
                    img_btn_close.setVisibility(View.VISIBLE);
                    img_btn_contact_list.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number = in_phone_number.getText().toString();

                if (checkPhoneAndSetOperator(phone_number)){
                    onGiftClick.onGiftClicked(phone_number,gift_url,code,couponId);
                    img_btn_close.setVisibility(View.VISIBLE);
                    img_btn_contact_list.setVisibility(View.VISIBLE);
                    dismiss();
                }
                else{
                    in_phone_number.setError("please provide valid phone number");
                    img_btn_close.setVisibility(View.INVISIBLE);
                    img_btn_contact_list.setVisibility(View.INVISIBLE);

                }
            }
        });


        in_phone_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {

                    return true;
                }
                return false;
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void goToContactsPage() {
        Intent contactNumberPickIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        contactNumberPickIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(contactNumberPickIntent, Constants.CONTACTS_REQUEST_CODE);
    }

    private void checkRuntimePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                goToContactsPage();

            } else {
                requestPermission();
            }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS
                /*Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE*/}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }

    }

    private void checkEditTextContent() {

        img_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_btn_close.setVisibility(View.INVISIBLE);
                in_phone_number.setText("");
                in_phone_number.setSelection(in_phone_number.getText().toString().length());
            }
        });
    }



    public interface OnGiftClick{
        void onGiftClicked(String phone_number, String gift_url, String code, String couponId);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.CONTACTS_REQUEST_CODE && data != null) {
            Uri contactData = data.getData();
            Cursor c = getContext().getContentResolver().query(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                String resultantContactNumber = "";
                // checks the contact number presence
                if (Integer.parseInt(hasNumber) == 1) {
                    Cursor cursorForPhoneNO = getContext().getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.Contacts._ID + " =?",
                            new String[]{contactId},
                            null);
                    while (cursorForPhoneNO.moveToNext()) {
                        resultantContactNumber = cursorForPhoneNO.getString(cursorForPhoneNO.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                        ));
                    }
                    cursorForPhoneNO.close();
                    String final_number = resultantContactNumber.replaceAll("\\D+","");

                    if (final_number.length() == 11) {
                        if(checkPhoneAndSetOperator(final_number)){
                            in_phone_number.setText(final_number);
                            in_phone_number.setSelection(in_phone_number.getText().toString().length());
                        }else{
                            resetInputPhone();
                        }

                        //mOnNextListner.onNextClick(final_number);
                    } else if (final_number.length() > 11) {
                        if(checkPhoneAndSetOperator(final_number.substring(final_number.length() - 11))) {
                            in_phone_number.setText(final_number.substring(final_number.length() - 11));
                        }
                        //mOnNextListner.onNextClick(final_number.substring(final_number.length() - 11));
                    } else {
                        resetInputPhone();
                    }
                }
            }
            c.close();
        }
    }

    private void resetInputPhone() {
        img_btn_contact_list.setVisibility(View.GONE);
        in_phone_number.setText("");
        in_phone_number.setError(getString(R.string.err_phone));
    }

    private boolean checkPhoneAndSetOperator(String final_number) {

        if(final_number.length() >= 11 && !final_number.isEmpty()){
            String phoneKey = final_number.substring(0,3);
            if(phoneKey.equals("017")){

                return true;
            }else if (phoneKey.equals("018")){

                return true;
            }else if (phoneKey.equals("019")){

                return true;
            }else if (phoneKey.equals("016")){

                return true;
            }else if (phoneKey.equals("015")){

                return true;
            }
            return false;
        }

        return false;

    }

}

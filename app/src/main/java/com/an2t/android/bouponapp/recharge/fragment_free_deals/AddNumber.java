package com.an2t.android.bouponapp.recharge.fragment_free_deals;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.helpers.Constants;

import java.util.List;

public class AddNumber extends Fragment {


    public static final int PERMISSION_REQUEST_CODE = 1;
    private ImageView img_btn_contact_list,img_btn_close ;
    private List<String> mOperatorList = Constants.OPERATOR_ITEMS;

    private EditText in_phone_number;
    private Button btn_next;
    private OnNextClick mOnNextListner;
    private String phone_number;

    private static final String TAG = AddNumber.class.getSimpleName();
    public AddNumber() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNextClick) {
            mOnNextListner = (OnNextClick) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement mOnNextListner");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_number, container, false);
        btn_next = (Button) view.findViewById(R.id.btn_next);
        img_btn_contact_list = (ImageView) view.findViewById(R.id.img_btn_contact_list);
        in_phone_number= (EditText) view.findViewById(R.id.in_phone_number);
        img_btn_close = (ImageView) view.findViewById(R.id.img_btn_close);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkEditTextContent();
        Log.d(TAG, "onClick: " + in_phone_number.getText().toString());

        setListeners();
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

                phone_number = in_phone_number.getText().toString();

                if (!phone_number.isEmpty() && (phone_number.length() >= 11) && phone_number.charAt(0) == '0' && checkPhoneAndSetOperator()){
                    mOnNextListner.onNextClick(phone_number);
                    img_btn_close.setVisibility(View.VISIBLE);
                    img_btn_contact_list.setVisibility(View.VISIBLE);
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
                Log.e(TAG, "Permission granted");
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




    public interface OnNextClick{
        void onNextClick(String phone_number);
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
                        in_phone_number.setText(final_number);
                        mOnNextListner.onNextClick(final_number);
                    } else if (final_number.length() > 11) {
                        in_phone_number.setText(final_number.substring(final_number.length() - 11));
                        mOnNextListner.onNextClick(final_number.substring(final_number.length() - 11));
                    } else {

                    }
                }
            }
            c.close();
        }
    }

    private boolean checkPhoneAndSetOperator() {

        String phoneKey = phone_number.substring(0,3);

        Log.e(TAG, "checkPhoneAndSetOperator: " + " phone number " + phone_number + " phone key " + phoneKey );

        if(phoneKey.equals("017")){

            return true;
        }else if (phoneKey.equals("018")){

            return true;
        }else if (phoneKey.equals("019")){

            return true;
        }else if (phoneKey.equals("016")) {

            return true;
        }
//        }else if (phoneKey.equals("015")){
//            spin_operator.setSelection(OPERATOR_ITEMS.indexOf("Teletalk"));
//            return true;
//        }
        return false;
    }


}

package com.an2t.android.bouponapp.my_coupon.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.an2t.android.bouponapp.R;

public class DialogDeepLinkSuccees extends DialogFragment {

    public static DialogDeepLinkSuccees newInstance() {
        DialogDeepLinkSuccees f = new DialogDeepLinkSuccees();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        f.setArguments(args);

        return f;
    }

    private OnDeeplinkUIButtonClick onDeeplinkUIButtonClick;

    public void onAttachToParentFragment(Activity activity) {
        try {
            onDeeplinkUIButtonClick = (OnDeeplinkUIButtonClick) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnRedeemClickDialog");
        }
    }

    private Button btn_later,btn_view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_dialog_deep_link_succees, container, false);

        setCancelable(false);

        onAttachToParentFragment(getActivity());
        btn_view = view.findViewById(R.id.btn_view);
        btn_later = view.findViewById(R.id.btn_later);

        setListners();

        return view;
    }

    private void setListners() {
        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onDeeplinkUIButtonClick.onDeeplinkUIButtonClick();
            }
        });
    }

    public interface OnDeeplinkUIButtonClick{
        void onDeeplinkUIButtonClick();
    }

}

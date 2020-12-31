package com.an2t.android.bouponapp.main.adpater;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.model.InfoRestList;

import java.util.ArrayList;

/**
 * Created by anantawasthy on 7/3/17.
 */

public class InfoKeyValueAdpater extends RecyclerView.Adapter<InfoKeyValueAdpater.InfoKeyValueViewHolder> {
    private Context mContext;
    private ArrayList<InfoRestList> list;
    private boolean isContact;
    private OnPhoneCall onPhoneCallListner;

    private static final String TAG = "InfoKeyValueAdpater";

    public InfoKeyValueAdpater(Context mContext, ArrayList<InfoRestList> list, boolean isContact, OnPhoneCall onPhoneCallListner) {
        this.mContext = mContext;
        this.list = list;
        this.isContact = isContact;
        this.onPhoneCallListner = onPhoneCallListner;
    }

    @Override
    public InfoKeyValueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.info_rest_list, parent, false);
        return new InfoKeyValueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoKeyValueViewHolder holder, int position) {
        final InfoRestList data = list.get(position);

        if (!isContact) {
            holder.tv_value.setVisibility(View.GONE);
        }

        if (data.getKey().isEmpty()) {
            data.setKey("NA");
        }

        if (data.getValue().isEmpty()) {
            data.setValue("NA");
        }

        holder.tv_key.setText(data.getKey());
        holder.tv_value.setText(data.getValue());
        holder.rl_main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getKey().equals("Phone")) {
                    Log.e(TAG, "onPhoneCallListner.onPhoneCallClick();");
                    onPhoneCallListner.onPhoneCallClick();

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InfoKeyValueViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_key, tv_value;
        private RelativeLayout rl_main_container;

        public InfoKeyValueViewHolder(View itemView) {
            super(itemView);
            tv_value = (TextView) itemView.findViewById(R.id.tv_value);
            tv_key = (TextView) itemView.findViewById(R.id.tv_key);
            rl_main_container = (RelativeLayout) itemView.findViewById(R.id.rl_main_container);
        }
    }

    public interface OnPhoneCall{
        void onPhoneCallClick();
    }
}

package com.an2t.android.bouponapp.field_area.adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;

import java.util.List;

/**
 * Created by anantawasthy on 11/14/17.
 */

public class FieldNamesAdapter extends RecyclerView.Adapter<FieldNamesAdapter.FieldNamesViewHolder> {
    private Context mContext;
    private List<String> mNames;
    private SendFieldArea mSendFieldNames;
    private boolean isCityName = true;

    private static final String TAG = "FieldNamesAdapter";

    public FieldNamesAdapter(Context mContext, List<String> mNames, SendFieldArea mSendFieldNames, Boolean isCityName) {
        this.mContext = mContext;
        this.mNames = mNames;
        this.mSendFieldNames = mSendFieldNames;
        this.isCityName = isCityName;

    }

    @Override
    public FieldNamesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.area_names_list, parent, false);
        return new FieldNamesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FieldNamesViewHolder holder, int position) {
        String name = mNames.get(position);

        Log.e(TAG, "onBindViewHolder: " + name);

        if (name != null) {
            holder.btn_area_names.setText(name);
        }
        holder.btn_area_names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSendFieldNames.sendFieldArea(name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class FieldNamesViewHolder extends RecyclerView.ViewHolder {
        public Button btn_area_names;

        public FieldNamesViewHolder(View itemView) {
            super(itemView);
            btn_area_names = (Button) itemView.findViewById(R.id.btn_area_names);
        }
    }

    public interface SendFieldArea {
        void sendFieldArea(String name);
    }
}

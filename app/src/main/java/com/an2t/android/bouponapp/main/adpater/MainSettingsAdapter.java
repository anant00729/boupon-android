package com.an2t.android.bouponapp.main.adpater;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.setting_web_view.ContactUsActivity;
import com.an2t.android.bouponapp.main.setting_web_view.WebViewSettingActivity;

import java.util.List;

/**
 * Created by anantawasthy on 8/23/17.
 */

public class MainSettingsAdapter extends RecyclerView.Adapter<MainSettingsAdapter.MainSettingsViewHolder>{
    private List<String> mSettingList;
    private Context mContext;


    public MainSettingsAdapter(List<String> mSettingList, Context mContext) {
        this.mSettingList = mSettingList;
        this.mContext = mContext;
    }

    @Override
    public MainSettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_list,parent,false);
        return new MainSettingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainSettingsViewHolder holder, int position) {
        String settingElement = mSettingList.get(position);
        holder.tv_setting_title.setText(settingElement);



        onMainContainerClick(holder,settingElement);
    }

    private void onMainContainerClick(MainSettingsViewHolder holder,final String settingElement) {
        holder.rl_main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(settingElement.contains("Contact Support")){
                    Intent intent = new Intent(mContext, ContactUsActivity.class);
                    intent.putExtra("contact",true);
                    mContext.startActivity(intent);
                }else if(settingElement.contains("Terms of Service")){
                    Intent intent = new Intent(mContext, ContactUsActivity.class);
                    intent.putExtra("contact",false);
                    mContext.startActivity(intent);
                }else if(settingElement.contains("FAQ")){
                    Intent intent = new Intent(mContext, WebViewSettingActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSettingList.size();
    }


    public class MainSettingsViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_setting_title;
        private RelativeLayout rl_main_container;
        public MainSettingsViewHolder(View itemView) {
            super(itemView);
            tv_setting_title = (TextView)itemView.findViewById(R.id.tv_setting_title);
            rl_main_container= (RelativeLayout) itemView.findViewById(R.id.rl_main_container);
        }
    }
}

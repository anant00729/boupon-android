package com.an2t.android.bouponapp.main.adpater;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.model.RechargeHistory;
import com.an2t.android.bouponapp.recharge.MobileRechargeActivity;

import java.util.ArrayList;

/**
 * Created by anantawasthy on 8/24/17.
 */

public class PurchaseHisAdapter extends RecyclerView.Adapter<PurchaseHisAdapter.PurchaseHisViewHolder> {

    private Context mContext;
    private ArrayList<RechargeHistory.RechageHistory> mPurchaseHistroyList;

    public PurchaseHisAdapter(Context mContext, ArrayList<RechargeHistory.RechageHistory> mPurchaseHistroyList) {
        this.mContext = mContext;
        this.mPurchaseHistroyList = mPurchaseHistroyList;
    }

    @Override
    public PurchaseHisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchase_history_list,parent,false);
        return new PurchaseHisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PurchaseHisViewHolder holder, int position) {
        RechargeHistory.RechageHistory history = mPurchaseHistroyList.get(position);

        holder.tv_name.setText(history.getMobileNumber());
        holder.tv_price.setText("৳"+history.getAmount());

        onSpeedMobileClick(holder,history);
    }

    private void onSpeedMobileClick(PurchaseHisViewHolder holder, final RechargeHistory.RechageHistory history) {
        holder.rl_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MobileRechargeActivity.class);
                intent.putExtra("isFromSpeed",true);
                intent.putExtra("phoneNumber",history.getMobileNumber());
                intent.putExtra("amount",String.valueOf(history.getAmount()));
                intent.putExtra("rechargeType",String.valueOf(history.getRechargeType()));
                intent.putExtra("provider",String.valueOf(history.getProvider()));
                if(mContext instanceof MobileRechargeActivity){
                    mContext.startActivity(intent);
                    ((MobileRechargeActivity) mContext).finish();
                }else{
                    mContext.startActivity(intent);
                }



            }
        });
    }

    @Override
    public int getItemCount() {
        return mPurchaseHistroyList.size();
    }

    public class PurchaseHisViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_profile_his;
        private TextView tv_name,tv_price,tv_id;
        private RelativeLayout rl_frame;
        public PurchaseHisViewHolder(View itemView) {
            super(itemView);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_name= (TextView)itemView.findViewById(R.id.tv_name);
            rl_frame= (RelativeLayout) itemView.findViewById(R.id.rl_frame);

            img_profile_his = (ImageView) itemView.findViewById(R.id.img_profile_his);

        }
    }
}

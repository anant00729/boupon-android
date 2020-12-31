package com.an2t.android.bouponapp.transaction.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.model.RechargeHistory;

import java.util.ArrayList;

/**
 * Created by anantawasthy on 10/11/17.
 */

public class MainTransAdpater extends RecyclerView.Adapter<MainTransAdpater.MainTransViewHolder> {
    private Context mContext;
    private ArrayList<RechargeHistory.RechageHistory> mHistoryList;
    private TransAdpater mTransAdpater;

    private static final String TAG = "MainTransAdpater";

    public MainTransAdpater(Context mContext, ArrayList<RechargeHistory.RechageHistory> mHistoryList) {
        this.mContext = mContext;
        this.mHistoryList = mHistoryList;
    }

    @Override
    public MainTransViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transcation_list,parent,false);
        return new MainTransViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainTransViewHolder holder, int position) {
        RechargeHistory.RechageHistory mHistroy = mHistoryList.get(position);



        holder.tv_trans_id.setText("Trasaction ID : "  + mHistroy.getTransactionID());
        holder.tv_name.setText("Customer Name : "  +mHistroy.getCustomerName());
        holder.tv_mobile.setText("Mobile : "  +mHistroy.getMobileNumber());
        holder.tv_amt.setText("৳ "+String.valueOf(mHistroy.getAmount()));

        String[] stringPart = mHistroy.getTimeAdded().split("T");

        holder.tv_date.setText(stringPart[0]);

        holder.rv_deals.setHasFixedSize(true);
        holder.rv_deals.setLayoutManager(new LinearLayoutManager(mContext));

        mTransAdpater = new TransAdpater(mContext,mHistroy.getDeals());
        holder.rv_deals.setAdapter(mTransAdpater);
        holder.tv_status.setText(mHistroy.getPaymentStatus());

        if(mHistroy.getPaymentStatus().equals("success")){
            holder.rv_deals.setVisibility(View.VISIBLE);
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.light_green));

        }else {
            holder.rv_deals.setVisibility(View.GONE);
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.indian_red));
        }
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }

    public class  MainTransViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_trans_id,tv_name,tv_mobile,tv_amt,tv_date;
        private RecyclerView rv_deals;
        private TextView tv_status;
        private CardView card;
        public MainTransViewHolder(View itemView) {
            super(itemView);

            tv_trans_id = (TextView)itemView.findViewById(R.id.tv_trans_id);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_mobile = (TextView)itemView.findViewById(R.id.tv_mobile);
            tv_amt = (TextView)itemView.findViewById(R.id.tv_amt);
            rv_deals= (RecyclerView) itemView.findViewById(R.id.rv_deals);
            tv_date= (TextView) itemView.findViewById(R.id.tv_date);
            tv_status= (TextView) itemView.findViewById(R.id.tv_status);
            card= (CardView) itemView.findViewById(R.id.card);
        }
    }

}

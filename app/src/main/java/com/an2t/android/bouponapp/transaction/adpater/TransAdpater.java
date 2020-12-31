package com.an2t.android.bouponapp.transaction.adpater;

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
import com.an2t.android.bouponapp.my_coupon.CoponsActivity;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.an2t.android.bouponapp.services.RetrofitSingleton.BASE_IMAGE_URL;

/**
 * Created by anantawasthy on 10/11/17.
 */

public class TransAdpater extends RecyclerView.Adapter<TransAdpater.TransViewHolder>{
    private Context mContext;
    private ArrayList<RechargeHistory.RechageHistory.Deal> mRechargeHistory;

    private static final String TAG = "TransAdpater";

    public TransAdpater(Context mContext, ArrayList<RechargeHistory.RechageHistory.Deal> mRechargeHistory) {
        this.mContext = mContext;
        this.mRechargeHistory = mRechargeHistory;
    }

    @Override
    public TransViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trans_deals_list,parent,false);

        return new TransViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransViewHolder holder, int position) {
        RechargeHistory.RechageHistory.Deal history = mRechargeHistory.get(position);

        holder.tv_desc_offer_item.setVisibility(View.GONE);
        holder.tv_title_offer_item.setText(history.getDealTitle());
        holder.tv_desc_offer_item.setText(history.getDealSku());
        holder.tv_price_offer_item.setText("Value : " + history.getDealValue());

        SimpleDateFormat current_format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat changed_format = new SimpleDateFormat("dd-MM-yyyy");
        // TODO: Display Expiry Date in Current format -> (D)
        try {
            String date = changed_format.format(current_format.parse(history.getDealExpiry()));
            if (date != null) {
                holder.tv_date_expiry.setText(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(history.getDealImage().contains("http")){
            Picasso.with(mContext)
                    .load(history.getDealImage())
                    .into(holder.img_offer_item);
        }else{
            Picasso.with(mContext)
                    .load(BASE_IMAGE_URL+history.getDealImage())
                    .into(holder.img_offer_item);
        }

        onItemClick(holder);

        if(++position == mRechargeHistory.size()){
            holder.line2.setVisibility(View.GONE);
        }
    }

    private void onItemClick(final TransViewHolder holder) {
        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CoponsActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRechargeHistory.size();
    }
    public class TransViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_offer_item;
        private TextView tv_title_offer_item, tv_desc_offer_item, tv_price_offer_item, tv_date_expiry;
        private TextView line2;
        private RelativeLayout main_container;
        public TransViewHolder(View itemView) {
            super(itemView);

            img_offer_item = (ImageView) itemView.findViewById(R.id.img_offer_item);
            tv_title_offer_item = (TextView) itemView.findViewById(R.id.tv_title_offer_item);
            tv_desc_offer_item = (TextView) itemView.findViewById(R.id.tv_desc_offer_item);
            tv_price_offer_item = (TextView) itemView.findViewById(R.id.tv_price_offer_item);
            tv_date_expiry = (TextView) itemView.findViewById(R.id.tv_date_expiry);
            line2 = (TextView) itemView.findViewById(R.id.line2);
            main_container= (RelativeLayout) itemView.findViewById(R.id.main_container);

        }
    }
}

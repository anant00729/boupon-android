package com.an2t.android.bouponapp.my_coupon.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.my_coupon.model.CouponCall;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.an2t.android.bouponapp.services.RetrofitSingleton.BASE_IMAGE_URL;

/**
 * Created by anantawasthy on 10/11/17.
 */

public class CouponMainAdapter extends RecyclerView.Adapter<CouponMainAdapter.CouponMainViewHolder> {

    private Context mContext;
    private ArrayList<CouponCall.CouponRes.Coupon> mCouponList;
    private OnRedeemClick onRedeemClickListner;


    private static final String TAG = "CouponMainAdapter";

    public CouponMainAdapter(Context mContext, ArrayList<CouponCall.CouponRes.Coupon> mCouponList, OnRedeemClick onRedeemClickListner) {
        this.mContext = mContext;
        this.mCouponList = mCouponList;
        this.onRedeemClickListner= onRedeemClickListner;

    }

    @Override
    public CouponMainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trans_deals_list,parent,false);

        return new CouponMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CouponMainViewHolder holder, int position) {
        CouponCall.CouponRes.Coupon mCoupon = mCouponList.get(position);



        initComponent(holder);

        mCoupon.setName(mCoupon.getName().replaceAll("\\n+\\t+", ""));



        holder.main_container.setPadding(16,16,0,16);

        setRedeemStatus(holder, mCoupon);


        holder.tv_title_offer_item.setText(mCoupon.getName());
        holder.tv_desc_offer_item.setText(mCoupon.getCode());
        holder.tv_price_offer_item.setText("Value : " + String.valueOf(mCoupon.getAmount()));



        SimpleDateFormat current_format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat changed_format = new SimpleDateFormat("dd-MM-yyyy");
        String date = null;
        // TODO: Display Expiry Date in Current format -> (D)
        try {
            date = changed_format.format(current_format.parse(mCoupon.getExpiry()));
            if (date != null) {

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(mCoupon.getImage().contains("http")){
            Picasso.with(mContext)
                    .load(mCoupon.getImage())
                    .into(holder.img_offer_item);
        }else{
            Picasso.with(mContext)
                    .load(BASE_IMAGE_URL+mCoupon.getImage())
                    .into(holder.img_offer_item);
        }

        onRedeemClick(holder,mCoupon);

    }

    private void setRedeemStatus(CouponMainViewHolder holder, CouponCall.CouponRes.Coupon mCoupon) {
        if(!mCoupon.getRedeemed()){
            holder.btn_redeem.setText(String.valueOf("Redeem"));
        }else{
            holder.btn_redeem.setVisibility(View.GONE);
            holder.tv_redeemed.setVisibility(View.VISIBLE);
        }
    }

    private void onRedeemClick(CouponMainViewHolder holder, final CouponCall.CouponRes.Coupon mCoupon) {
        holder.btn_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onRedeemClickListner.onRedeemClicked(holder,mCoupon);
            }
        });


    }

    private void initComponent(CouponMainViewHolder holder) {
        holder.btn_redeem.setVisibility(View.VISIBLE);
        holder.line1.setVisibility(View.GONE);
        holder.line2.setVisibility(View.GONE);
        holder.line3.setVisibility(View.GONE);
        holder.dot.setVisibility(View.GONE);
        holder.card.setUseCompatPadding(true);
    }

    @Override
    public int getItemCount() {
        return mCouponList.size();
    }

    public class CouponMainViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_offer_item;
        private TextView tv_title_offer_item, tv_desc_offer_item, tv_price_offer_item, tv_date_expiry,tv_redeemed;
        private TextView line2,line1,line3;
        private ImageView dot;
        private CardView card;
        private RelativeLayout main_container;
        private Button btn_redeem;
        public CouponMainViewHolder(View itemView) {
            super(itemView);
            img_offer_item = (ImageView) itemView.findViewById(R.id.img_offer_item);
            tv_title_offer_item = (TextView) itemView.findViewById(R.id.tv_title_offer_item);
            tv_desc_offer_item = (TextView) itemView.findViewById(R.id.tv_desc_offer_item);
            tv_price_offer_item = (TextView) itemView.findViewById(R.id.tv_price_offer_item);
            tv_date_expiry = (TextView) itemView.findViewById(R.id.tv_date_expiry);
            line1= (TextView) itemView.findViewById(R.id.line1);
            line2 = (TextView) itemView.findViewById(R.id.line2);
            line3= (TextView) itemView.findViewById(R.id.line3);
            dot= (ImageView) itemView.findViewById(R.id.dot);
            card= (CardView) itemView.findViewById(R.id.card);
            main_container = (RelativeLayout)itemView.findViewById(R.id.main_container);
            btn_redeem = (Button)itemView.findViewById(R.id.btn_redeem);
            tv_redeemed= itemView.findViewById(R.id.tv_redeemed);
        }
    }

    public interface OnRedeemClick{
        void onRedeemClicked(CouponMainViewHolder holder, CouponCall.CouponRes.Coupon mCoupon);

    }

    public void updateRedeemStatus(CouponMainViewHolder holder, CouponCall.CouponRes.Coupon item){

        item.setRedeemed(true);
        notifyDataSetChanged();
    }
}

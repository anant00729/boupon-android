package com.an2t.android.bouponapp.recharge.adapter;

import android.content.Context;
import android.os.Build;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.recharge.model.FreeDealsRes;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.an2t.android.bouponapp.services.RetrofitSingleton.BASE_IMAGE_URL;

/**
 * Created by anantawasthy on 9/30/17.
 */

public class FreeDealsAdapter extends RecyclerView.Adapter<FreeDealsAdapter.FreeDealsViewHolder> {
    private Context mContext;
    private ArrayList<FreeDealsRes.Item> mSelectedList;
    private OnSelectItemClickFreeDealsRecharge mListner;

    public FreeDealsAdapter(Context mContext, ArrayList<FreeDealsRes.Item> mSelectedList, OnSelectItemClickFreeDealsRecharge mListner) {
        this.mContext = mContext;
        this.mSelectedList = mSelectedList;
        this.mListner = mListner;
    }

    private static final String TAG = "FreeDealsAdapter";
    @Override
    public FreeDealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_item_list, parent, false);
        return new FreeDealsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FreeDealsViewHolder holder, int position) {
        final FreeDealsRes.Item item = mSelectedList.get(position);

        item.setSelected(false);

        String areaName = StringUtils.capitalize(item.getAreaName());
        String areaCity = StringUtils.capitalize(item.getCity());

        StringBuilder sb_address = null;
        if (areaName != null && areaCity != null) {
            sb_address = new StringBuilder(areaName)
                    .append(", ")
                    .append(areaCity);
        }



        if (sb_address != null) {
            holder.tv_area.setText(sb_address);
        }

        if(item.getTitle() != null && item.getShortDescription() != null && item.getValue() != null){
            holder.tv_title_offer_item.setText(item.getTitle());
            holder.tv_desc_offer_item.setText(item.getShortDescription());

            holder.tv_price_offer_item.setText(item.getValue() + " Points");
        }

        displayDistance(holder, item);


        SimpleDateFormat current_format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat changed_format = new SimpleDateFormat("dd-MM-yyyy");
        // TODO: Display Expiry Date in Current format -> (D)
        try {
            String date = changed_format.format(current_format.parse(item.getExpiry()));
            if (date != null) {
                holder.tv_date_expiry.setText(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(item.getMainImage() != null && !item.getMainImage().isEmpty()){
            Picasso.with(mContext)
                    .load(BASE_IMAGE_URL+item.getMainImage())
                    .resize(320,270)
                    .placeholder(R.drawable.boupon_logo)
                    .into(holder.img_offer_item);
        }
        setButtonDrawable(holder.btn_see_more);
        onSeeMoreClicked(holder, item);

    }

    private void onSeeMoreClicked(FreeDealsViewHolder holder, FreeDealsRes.Item item) {

        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.onSelectItemClickedFreeDealsRecharge(item.isSelected(),item);
            }
        });
        holder.btn_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.onSelectItemClickedFreeDealsRecharge(item.isSelected(),item);
            }
        });
    }

    private void displayDistance(FreeDealsViewHolder holder, FreeDealsRes.Item item) {
        StringBuilder sb;
        double dist = 0;
        if (item.getDistance() != null ) {
            dist = Double.parseDouble(item.getDistance());
        }
        if(dist < 1.0D){
            dist = dist * 1000;
            sb = new StringBuilder(String.valueOf(dist))
                    .append(" m away");
        }else {
            int distInt = (int) dist;
            sb = new StringBuilder(String.valueOf(distInt))
                    .append(" Km away");
        }



        if (item.getDistance() != null) {
            if (dist !=0.0 && !item.getDistance().isEmpty()) {
                holder.tv_distance.setText(sb.toString());
            }else if(dist == 0.0 && item.getDistance().isEmpty()){
                holder.tv_distance.setVisibility(View.GONE);
                holder.img_home.setVisibility(View.GONE);
            }
        }else {
            holder.tv_distance.setVisibility(View.GONE);
            holder.img_home.setVisibility(View.GONE);
        }
    }

    private void setButtonDrawable(Button btn_see_more) {
        final int sdk = Build.VERSION.SDK_INT;
        if(sdk > Build.VERSION_CODES.KITKAT) {
            btn_see_more.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.btn_selector_send));
            btn_see_more.setPadding(24,0,0,0);
        } else {
            btn_see_more.setBackgroundColor(mContext.getResources().getColor(R.color.light_green));
            btn_see_more.setPadding(12,0,0,0);
            btn_see_more.setTextSize(12);
            btn_see_more.setText("More");
        }
    }

    @Override
    public int getItemCount() {
        return mSelectedList.size();
    }

    public class FreeDealsViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_offer_item,img_home;
        private TextView tv_title_offer_item, tv_desc_offer_item, tv_price_offer_item, tv_date_expiry,tv_distance,tv_area;
        private Button btn_see_more;



        private LinearLayout main_container;
        public FreeDealsViewHolder(View itemView) {
            super(itemView);

            img_offer_item = (ImageView) itemView.findViewById(R.id.img_offer_item);

            tv_title_offer_item = (TextView) itemView.findViewById(R.id.tv_title_offer_item);
            tv_desc_offer_item = (TextView) itemView.findViewById(R.id.tv_desc_offer_item);
            tv_price_offer_item = (TextView) itemView.findViewById(R.id.tv_price_offer_item);
            tv_date_expiry = (TextView) itemView.findViewById(R.id.tv_date_expiry);
            tv_distance= (TextView) itemView.findViewById(R.id.tv_distance);
            tv_area= (TextView) itemView.findViewById(R.id.tv_area);
            main_container = (LinearLayout)itemView.findViewById(R.id.main_container);
            btn_see_more= (Button) itemView.findViewById(R.id.btn_see_more);
            img_home= (ImageView) itemView.findViewById(R.id.img_home);

        }
    }

    public interface OnSelectItemClickFreeDealsRecharge{
        void onSelectItemClickedFreeDealsRecharge(boolean selected, FreeDealsRes.Item item);
    }
}

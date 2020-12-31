package com.an2t.android.bouponapp.main.adpater;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.model.Offer;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.an2t.android.bouponapp.services.RetrofitSingleton.BASE_IMAGE_URL;

/**
 * Created by anantawasthy on 8/23/17.
 */

public class OfferTypeAdapter extends RecyclerView.Adapter<OfferTypeAdapter.OfferTypeViewHolder> {

    private Context mContext;
    private List<Offer.Data> mOfferList;



    private static final String TAG = OfferTypeAdapter.class.getSimpleName();

    public OfferTypeAdapter(Context mContext, List<Offer.Data> mOfferList) {
        this.mContext = mContext;
        this.mOfferList = mOfferList;

    }



    @Override
    public OfferTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_item_list, parent, false);
        return new OfferTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferTypeViewHolder holder, int position) {
        final Offer.Data offer = mOfferList.get(position);

        holder.tv_title_offer_item.setText(offer.getTitle());
        holder.tv_desc_offer_item.setText(offer.getCategory());
        holder.tv_price_offer_item.setText("৳" /*offer.()*/);

        SimpleDateFormat current_format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat changed_format = new SimpleDateFormat("dd-MM-yyyy");
        // TODO: Display Expiry Date in Current format -> (D)
        try {
            String date = changed_format.format(current_format.parse(offer.getExpiry()));
            if (date != null) {
                holder.tv_date_expiry.setText(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Picasso.with(mContext)
                .load(offer.getMainImage())
                .into(holder.img_offer_item);
    }







    @Override
    public void onBindViewHolder(OfferTypeViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);


    }

    @Override
    public int getItemCount() {
        return mOfferList.size();
    }

    public class OfferTypeViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_offer_item;
        private TextView tv_title_offer_item, tv_desc_offer_item, tv_price_offer_item, tv_date_expiry;
        private Button btn_select;

        public OfferTypeViewHolder(View itemView) {
            super(itemView);
            img_offer_item = (ImageView) itemView.findViewById(R.id.img_offer_item);
            tv_title_offer_item = (TextView) itemView.findViewById(R.id.tv_title_offer_item);
            tv_desc_offer_item = (TextView) itemView.findViewById(R.id.tv_desc_offer_item);
            tv_price_offer_item = (TextView) itemView.findViewById(R.id.tv_price_offer_item);
            tv_date_expiry = (TextView) itemView.findViewById(R.id.tv_date_expiry);

        }


    }




}

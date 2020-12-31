package com.an2t.android.bouponapp.main.adpater;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.model.GetSpotlightImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.an2t.android.bouponapp.services.RetrofitSingleton.BASE_IMAGE_URL;

/**
 * Created by anantawasthy on 9/6/17.
 */

public class HomeSpotLightAdapter extends RecyclerView.Adapter<HomeSpotLightAdapter.HomeSpotLightHolder> {

    private Context mContext;
    private ArrayList<GetSpotlightImages.Item> mImages;


    
    public HomeSpotLightAdapter(Context mContext, ArrayList<GetSpotlightImages.Item> mImages) {
        this.mContext = mContext;
        this.mImages = mImages;
    }



    @Override
    public HomeSpotLightHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spotling_deals_list,parent,false);
        return new HomeSpotLightHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeSpotLightHolder holder, int position) {
        GetSpotlightImages.Item image = mImages.get(position);



        Picasso.with(mContext)
                .load(BASE_IMAGE_URL+image.getMainImage())
                .resize(300,230)
                .placeholder(R.drawable.boupon_logo)
                .into(holder.img_spotlight);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class HomeSpotLightHolder extends RecyclerView.ViewHolder{
        private ImageView img_spotlight;
        public HomeSpotLightHolder(View itemView) {
            super(itemView);
            img_spotlight = (ImageView)itemView.findViewById(R.id.img_spotlight);
        }
    }
}

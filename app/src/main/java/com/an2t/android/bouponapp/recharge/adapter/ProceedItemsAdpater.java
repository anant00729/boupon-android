package com.an2t.android.bouponapp.recharge.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.recharge.model.FreeDealsRes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.an2t.android.bouponapp.services.RetrofitSingleton.BASE_IMAGE_URL;

/**
 * Created by anantawasthy on 10/3/17.
 */

public class ProceedItemsAdpater extends RecyclerView.Adapter<ProceedItemsAdpater.ProceedItemsViewHolder> {

    private Context mContext;
    private ArrayList<FreeDealsRes.Item> mItemList;
    private OnItemRemoved mListner;

    public ProceedItemsAdpater(Context mContext, ArrayList<FreeDealsRes.Item> mItemList,OnItemRemoved mListner) {
        this.mContext = mContext;
        this.mItemList = mItemList;
        this.mListner = mListner;
    }

    @Override
    public ProceedItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.proceed_item_list,parent,false);

        return new ProceedItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProceedItemsViewHolder holder, int position) {
        FreeDealsRes.Item item = mItemList.get(position);

        if(item.getValue() != null && item.getTitle() != null )

        holder.tv_item_value.setText(item.getValue());
        holder.tv_item_title.setText(item.getTitle());
        Picasso.with(mContext)
                .load(BASE_IMAGE_URL+item.getMainImage())
                .resize(80,55)
                .into(holder.img_item);

        onDeleteClicked(holder,item);
    }

    private void onDeleteClicked(ProceedItemsViewHolder holder, final FreeDealsRes.Item item) {
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mListner.onItemRemoved(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class ProceedItemsViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_item_title,tv_item_value;
        private CircleImageView img_item;
        private ImageView btn_delete;
        public ProceedItemsViewHolder(View itemView) {
            super(itemView);

            img_item = (CircleImageView)itemView.findViewById(R.id.img_item);
            tv_item_title = (TextView)itemView.findViewById(R.id.tv_item_title);
            tv_item_value = (TextView)itemView.findViewById(R.id.tv_item_value);
            btn_delete = (ImageView)itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnItemRemoved{
        void onItemRemoved(FreeDealsRes.Item item);
    }
}

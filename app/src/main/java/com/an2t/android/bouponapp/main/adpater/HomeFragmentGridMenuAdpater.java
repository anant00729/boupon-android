package com.an2t.android.bouponapp.main.adpater;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.model.HomeGridMenu;

import java.util.List;

/**
 * Created by anantawasthy on 9/4/17.
 */

public class HomeFragmentGridMenuAdpater extends RecyclerView.Adapter<HomeFragmentGridMenuAdpater.HomeFragmentGridMenuViewHolder> {
    private List<HomeGridMenu> mHomeGridMenusList;
    private Context mContext;
    private HomeGirdMenuCallbacks menuCallback;

    public HomeFragmentGridMenuAdpater(Context mContext, List<HomeGridMenu> mHomeGridMenusList,HomeGirdMenuCallbacks mCallback) {
        this.mHomeGridMenusList = mHomeGridMenusList;
        this.mContext = mContext;
        this.menuCallback = mCallback;
    }

    @Override
    public HomeFragmentGridMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_grid_menu_items,parent,false);

        return new HomeFragmentGridMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeFragmentGridMenuViewHolder holder, int position) {
        HomeGridMenu mHomeGridMenu = mHomeGridMenusList.get(position);

        int imageResource = mContext.getResources().getIdentifier(mHomeGridMenu.getImage(), "drawable", mContext.getPackageName());

        holder.tv_menu.setText(mHomeGridMenu.getName());
        holder.img_menu.setImageResource(imageResource);

        onItemClick(holder,position);
    }

    private void onItemClick(HomeFragmentGridMenuViewHolder holder, final int position) {
        holder.rl_grid_menu_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuCallback.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHomeGridMenusList.size();
    }

    public class HomeFragmentGridMenuViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_menu;
        private ImageView img_menu;
        private RelativeLayout rl_grid_menu_main;
        public HomeFragmentGridMenuViewHolder(View itemView) {
            super(itemView);
            tv_menu = (TextView)itemView.findViewById(R.id.tv_menu);
            img_menu = (ImageView)itemView.findViewById(R.id.img_menu);
            rl_grid_menu_main= (RelativeLayout) itemView.findViewById(R.id.rl_grid_menu_main);
        }

    }

    public interface HomeGirdMenuCallbacks {
        void onItemClick(int position);
    }
}

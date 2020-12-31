package com.an2t.android.bouponapp.main.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.an2t.android.bouponapp.R;
import com.an2t.android.bouponapp.main.adpater.MainSettingsAdapter;
import com.an2t.android.bouponapp.login.LoginActivity;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    private RecyclerView rv_show_settings_list;
    private Button btn_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // init Component
        initComponent(view);

        // set Listeners
        setListeners(view);
        return view;
    }

    private void initComponent(View view) {
        rv_show_settings_list = (RecyclerView)view.findViewById(R.id.rv_show_settings_list);
        btn_logout= (Button) view.findViewById(R.id.btn_logout);

        rv_show_settings_list.setHasFixedSize(true);
        rv_show_settings_list.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<String> settingList = new ArrayList<>();
        settingList.add("Contact Support");
        settingList.add("Terms of Service");
        settingList.add("FAQ");

        MainSettingsAdapter adapter = new MainSettingsAdapter(settingList,getContext());

        rv_show_settings_list.setAdapter(adapter);

    }

    private void setListeners(View view){

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getContext().getSharedPreferences(getString(R.string.spFileUser), getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(getString(R.string.userLoginToken),"");
                if(editor.commit()){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

    }

}

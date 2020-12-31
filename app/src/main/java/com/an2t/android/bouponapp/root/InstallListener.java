package com.an2t.android.bouponapp.root;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by anantawasthy on 11/29/17.
 */

public class InstallListener extends BroadcastReceiver {

    private static final String TAG = "InstallListener";

    @Override
    public void onReceive(Context context, Intent intent) {

        String rawReferrerString = intent.getStringExtra("referrer");
        if(rawReferrerString != null) {
            Log.e("MyApp", "Received the following intent " + rawReferrerString);
        }
    }
}

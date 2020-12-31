package com.an2t.android.bouponapp.root;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.flurry.android.FlurryAgent;

import io.branch.referral.Branch;

/**
 * Created by anantawasthy on 8/31/17.
 */

// 6FKK5BMBJ5PCB6TPN5T8

public class BouponApp extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Branch.enableLogging();
        // Initialize the Branch object
        Branch.getAutoInstance(this);

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "6FKK5BMBJ5PCB6TPN5T8");
    }
}

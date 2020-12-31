package com.an2t.android.bouponapp.helpers;

import com.an2t.android.bouponapp.main.model.HomeGridMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anantawasthy on 8/23/17.
 */

public class Constants {
    public static final int CONTACTS_REQUEST_CODE = 100;

    public static final List<String> OPERATOR_ITEMS;

    public static final ArrayList<HomeGridMenu> HOME_MENU_LIST_ITEMS;

    static {
        OPERATOR_ITEMS = new ArrayList<>();

        HOME_MENU_LIST_ITEMS = new ArrayList<>();

        //
        HOME_MENU_LIST_ITEMS.add(new HomeGridMenu("Mobile Recharge", "ic_mobile"));
        HOME_MENU_LIST_ITEMS.add(new HomeGridMenu("Get free coupons on successful bill recharge with us.", "ic_coupon"));
        /*HOME_MENU_LIST_ITEMS.add(new HomeGridMenu("Mall", "ic_mall"));
        HOME_MENU_LIST_ITEMS.add(new HomeGridMenu("Deals", "ic_deals"));*/

        OPERATOR_ITEMS.add("Select Operator");
        OPERATOR_ITEMS.add("Grameen Phone");
        OPERATOR_ITEMS.add("Robi");
        OPERATOR_ITEMS.add("Banglalink");
        OPERATOR_ITEMS.add("Airtel");
//        OPERATOR_ITEMS.add("Teletalk");
    }
}

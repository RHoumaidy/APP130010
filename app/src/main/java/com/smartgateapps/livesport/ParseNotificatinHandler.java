package com.smartgateapps.livesport;

import android.content.Context;
import android.content.Intent;

import com.parse.ParseBroadcastReceiver;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Raafat on 24/01/2016.
 */
public class ParseNotificatinHandler extends ParsePushBroadcastReceiver {


    @Override
    protected void onPushReceive(Context context, Intent intent) {
        boolean b = MyApplication.pref.getBoolean(MyApplication.APP_CTX.getString(R.string.parse_notification_enabled), true);
        if (b)
            super.onPushReceive(context, intent);
    }



}

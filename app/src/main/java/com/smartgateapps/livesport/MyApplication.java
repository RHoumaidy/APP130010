package com.smartgateapps.livesport;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by Raafat on 24/01/2016.
 */
public class MyApplication extends Application {

    public static SharedPreferences pref;
    public static InterstitialAd mInterstitialAd;
    public static Context APP_CTX;

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        APP_CTX = getApplicationContext();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdOpened() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();


        pref = PreferenceManager.getDefaultSharedPreferences(MyApplication.APP_CTX);
        boolean b = pref.getBoolean(getString(R.string.parse_notification_enabled),true);
        pref.edit().putBoolean(getString(R.string.parse_notification_enabled),b).apply();

        Parse.initialize(this, "YGP040MQAiCIKE3HCSZOdcpuTmZ8jOJ6JTLSjloL", "Vw23aL9DZIixjByjfTuqcUmFvlFywzNTO2Hl7OMb");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}

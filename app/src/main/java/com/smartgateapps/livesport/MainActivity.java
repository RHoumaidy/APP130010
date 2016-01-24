package com.smartgateapps.livesport;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.ParseAnalytics;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private Fragment webViewFragment;
    private Fragment aboutFragment;
    private Fragment sendEmailFragment;
    private FragmentManager fragmentManager;
    private boolean backPressed = false;
    private int prevSelectedId = 0;
    private AdView mAdView;

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        mAdView = (AdView) findViewById(R.id.adView);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                requestNewInterstitial();
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        webViewFragment = new WebViewFragment();
        aboutFragment = new AboutFragment();
        sendEmailFragment = new SendEmailFragment();
        fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, webViewFragment)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .commit();

    }

    @Override
    public void onBackPressed() {

        if (fragmentManager.getBackStackEntryCount() == 0) {

            if (!backPressed) {
                backPressed = true;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        backPressed = false;
                    }
                }, 3500);
                Toast.makeText(this, ("اضغط مرة اخرى للاغلاق"), Toast.LENGTH_LONG).show();
            } else {
                if (MyApplication.mInterstitialAd.isLoaded())
                    MyApplication.mInterstitialAd.show();
                super.onBackPressed();
            }
        } else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem parseNotification = menu.getItem(3);
        parseNotification.setChecked(
                MyApplication.pref.getBoolean(getString(R.string.parse_notification_enabled),false));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == prevSelectedId)
            return false;
        switch (id) {
            case android.R.id.home:
                if (WebViewFragment.goBack())
                    return true;
                else {
                    onBackPressed();
                    return true;
                }
            case R.id.notifMenuItem:
                MyApplication.pref
                        .edit()
                        .putBoolean(getString(R.string.parse_notification_enabled),!item.isChecked())
                        .apply();
                item.setChecked(!item.isChecked());
                return true;

            case R.id.refreshMenuItem:
                WebViewFragment.refreash();
                return true;
            case R.id.aboutUsMenuItem:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, aboutFragment)
                        .addToBackStack("aboutUs")
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .commit();
                return true;
            case R.id.contactUsMenuItem:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, sendEmailFragment)
                        .addToBackStack("contactUs")
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .commit();
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}

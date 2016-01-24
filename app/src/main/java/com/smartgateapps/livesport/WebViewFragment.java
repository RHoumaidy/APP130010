package com.smartgateapps.livesport;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by Raafat on 24/01/2016.
 */
public class WebViewFragment extends Fragment {
    private static WebView webView1;
    private static final String BASE_URL = "http://www.yalla-shoot.com/m2/";


    public static boolean goBack(){
        if(webView1 == null)
            return false;
        if(!webView1.getUrl().equalsIgnoreCase(BASE_URL)) {
            webView1.stopLoading();
            webView1.loadUrl(BASE_URL);
            return true;
        }else
            return false;
    }

    public static void refreash(){
        if(webView1 == null)
            return;
        webView1.loadUrl(webView1.getUrl());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview_laytout,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView1 = (WebView) view.findViewById(R.id.webView);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setAppCacheEnabled(true);
        webView1.getSettings().setLoadsImagesAutomatically(true);
        webView1.stopLoading();
        webView1.loadUrl(BASE_URL);

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(R.string.progress_bar_title);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgress(0);
        dialog.setMax(100);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();


        webView1.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.hide();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog.show();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Toast.makeText(getActivity(), "httpError", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();
            }

        });
        webView1.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress >= 100){
                    dialog.hide();
                }else {
                    dialog.setProgress(newProgress);

                }
            }


        });
    }
}

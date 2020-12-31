package com.an2t.android.bouponapp.payment;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by anantawasthy on 9/25/17.
 */


public class WebClient extends WebChromeClient {
    private ProgressListener mListener;
    public WebClient(ProgressListener listener) {
        mListener = listener;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mListener.onUpdateProgress(newProgress);
        super.onProgressChanged(view, newProgress);
    }

    public interface ProgressListener {
        void onUpdateProgress(int progressValue);
    }
}

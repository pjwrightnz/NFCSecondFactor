package com.example.paul.nfcsecondfactor0.nfcSecondFactorClient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.example.paul.nfcsecondfactor0.R;
/**
 *  BTWebSiteActivity is the activity shown when a user successfully logs in.
 *  onCreate() sets up the Activity.
 *
 * @author Paul Wright
 * @version 1.0 Prototype 1. Activities, intents and servers.
 * @version 1.1 Prototype 2. Add NFC Functionality.
 * @see #onCreate(Bundle savedInstanceState)
 *
 */

public class BTWebSiteActivity extends AppCompatActivity {

     /**
     * The onCreate method calls up a webview to BT's home page.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btweb_site);

        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webView.loadUrl("http://home.bt.com/");
        webSettings.setJavaScriptEnabled(true);
        webView.measure(100, 100);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
    }
}

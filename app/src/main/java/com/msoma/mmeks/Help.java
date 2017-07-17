package com.msoma.mmeks;

/**
 * Created by gikundi on 3/6/15.
 */
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Build;



public class Help extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        WebView wv = (WebView) findViewById(R.id.web_company_profile);
        wv.loadUrl("file:///android_asset/Help.html");
    }



    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return(false);
        }
    }

    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }



}
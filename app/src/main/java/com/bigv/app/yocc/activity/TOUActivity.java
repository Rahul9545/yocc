package com.bigv.app.yocc.activity;

import android.view.View;
import android.webkit.WebView;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.utils.MyBaseActivity;

/**
 * Created by MiTHUN on 12/9/17.
 */

public class TOUActivity extends MyBaseActivity {

    private WebView webView;

    @Override
    protected void genrateId() {

        setContentView(R.layout.activity_tou);
        webView = findViewById(R.id.webView);
        //setToolbarTitle("Terms of Use");
        setToolbarTitle(getResources().getString(R.string.str_title_terms_use));
    }

    @Override
    protected void registerEvents() {

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);
        webView.setHapticFeedbackEnabled(false);
    }

    @Override
    public void initData() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/tou.html");
    }

}

package com.testing.simplesp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.testing.simplesp.R;
import com.testing.simplesp.adapter.DocumentAdapter;
import com.testing.simplesp.domain.DocumentItem.Data;

public class DetailDocumentActivity extends AppCompatActivity {


    private WebView wv_doc;
    private boolean isWebView = false;
    private TextView tv_title;
    private TextView tv_unit;
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();

    }

    private void initData() {
        if (isWebView) {
            //展示原网页
            String url = getIntent().getStringExtra("currentId");
            WebSettings settings = wv_doc.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setBuiltInZoomControls(true);
            settings.setUseWideViewPort(true);

            wv_doc.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            wv_doc.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                }

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                }
            });
            wv_doc.loadUrl(url);
        } else {
            //不展示原网页
            int position = getIntent().getIntExtra("currentPosition", 0);
            Data data = DocumentAdapter.mValues.get(position);
            tv_title.setText(data.getTitle());
            tv_unit.setText(data.getUnit());
            tv_content.setText(data.getContent());
        }
    }

    private void initView() {
        setContentView(R.layout.activity_detail_document);
        wv_doc = (WebView) findViewById(R.id.wv_doc);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_unit = (TextView) findViewById(R.id.tv_unit);
        tv_content = (TextView) findViewById(R.id.tv_content);
    }

}

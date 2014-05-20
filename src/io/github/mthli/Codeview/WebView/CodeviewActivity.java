package io.github.mthli.Codeview.WebView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import io.github.mthli.Codeview.Other.AboutActivity;
import io.github.mthli.Codeview.R;

public class CodeviewActivity extends Activity {
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codeview);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(getIntent().getStringExtra("title"));
        actionBar.setSubtitle(getIntent().getStringExtra("sub_title"));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.codeview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.setVisibility(View.VISIBLE);

        /* Maybe use thread would better */
        String content = SyntaxSetting.setCodeAsHtml(getIntent().getStringExtra("path"));
        webView.loadDataWithBaseURL(
                SyntaxSetting.baseUrl,
                content,
                null,
                null,
                null
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.webview_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.webview_menu_refresh:
                /* Do something */
                break;
            case R.id.webview_menu_setting:
                /* Do something */
                break;
            case R.id.webview_menu_help:
                /* Do something */
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}

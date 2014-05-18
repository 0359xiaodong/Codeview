package io.github.mthli.Codeview.WebView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import io.github.mthli.Codeview.Other.AboutActivity;
import io.github.mthli.Codeview.R;

import java.io.File;

public class CodeviewActivity extends Activity {
    private WebView web_view;

    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.code_view);

        ActionBar action_bar = getActionBar();
        action_bar.setTitle(getIntent().getStringExtra("title"));
        action_bar.setSubtitle(getIntent().getStringExtra("sub_title"));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        web_view = (WebView) findViewById(R.id.code_view);
        WebSettings web_settings = web_view.getSettings();
        web_settings.setJavaScriptEnabled(true);
        web_settings.setLoadWithOverviewMode(true);
        web_view.setVisibility(View.VISIBLE);

        String content = SyntaxSetting.setCodeAsHtml(getIntent().getStringExtra("path"));
        /* Maybe we should use new thread to loal this url */
        web_view.loadDataWithBaseURL(
                SyntaxSetting.base_url,
                content,
                null,
                null,
                null
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

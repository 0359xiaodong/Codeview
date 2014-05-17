package io.github.mthli.Codeview.WebView;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import io.github.mthli.Codeview.R;

import java.io.File;

public class CodeviewActivity extends Activity {
    private WebView web_view;

    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.code_view);

        ActionBar action_bar = getActionBar();
        action_bar.setTitle(getIntent().getStringExtra("title"));
        String str[] = getIntent().getStringExtra("sub_title").split("/");
        String sub_title = str[0];
        for (int i = 1; i < str.length - 1; i++) {
            sub_title = sub_title + File.separator + str[i];
        }
        action_bar.setSubtitle(sub_title + File.separator);
        getActionBar().setDisplayShowHomeEnabled(true);

        web_view = (WebView) findViewById(R.id.code_view);
        WebSettings web_settings = web_view.getSettings();
        web_settings.setJavaScriptEnabled(true);
        // web_settings.setUseWideViewPort(true);
        // web_settings.setLoadWithOverviewMode(true);
        web_view.setVisibility(View.VISIBLE);

        String content = SyntaxSetting.setCodeAsHtml(getIntent().getStringExtra("path"));
        web_view.loadDataWithBaseURL(
                SyntaxSetting.base_url,
                content,
                "text/html",
                null,
                null
        );
    }
}

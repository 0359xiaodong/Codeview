package io.github.mthli.Codeview.WebView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import io.github.mthli.Codeview.R;
import io.github.mthli.Codeview.Setting.SettingActivity;

public class CodeviewActivity extends Activity {
    private WebView webView;
    private SharedPreferences sharedPreferences;
    private int fontsize;
    private String highlight;

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

        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        fontsize = sharedPreferences.getInt("fontsize", 12);
        highlight = sharedPreferences.getString("highlight", "default.css");
        /* Maybe use thread would better */
        String content = SyntaxSetting.setCodeAsHtml(fontsize, highlight, getIntent().getStringExtra("path"));
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
                sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
                fontsize = sharedPreferences.getInt("fontsize", 12);
                highlight = sharedPreferences.getString("highlight", "default.css");
                String content = SyntaxSetting.setCodeAsHtml(fontsize, highlight, getIntent().getStringExtra("path"));
                webView.loadDataWithBaseURL(
                        SyntaxSetting.baseUrl,
                        content,
                        null,
                        null,
                        null
                );
                break;
            case R.id.webview_menu_setting:
                Intent intent_setting = new Intent(this, SettingActivity.class);
                startActivity(intent_setting);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}

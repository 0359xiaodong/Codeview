package io.github.mthli.Codeview;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    private SharedPreferences shared_preferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.login);

        shared_preferences = getSharedPreferences("login_info", MODE_PRIVATE);
        editor = shared_preferences.edit();
        String username = shared_preferences.getString("username", null);
        String password = shared_preferences.getString("password", null);
        if (username == null || password == null) {
            final EditText username_edit_text = (EditText)findViewById(R.id.password_edittext);
            final EditText password_edit_text = (EditText)findViewById(R.id.password_edittext);
            Button login_button = (Button)findViewById(R.id.login_button);
            setContentView(R.layout.login);

            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String get_username = username_edit_text.getText().toString();
                    String get_password = password_edit_text.getText().toString();
                    if (get_username == null && get_password == null) {
                        Toast.makeText(LoginActivity.this, "Please enter your username and password.", 3000).show();
                    } else if (get_username != null && get_password == null) {
                        Toast.makeText(LoginActivity.this, "Please enter your username and password.", 3000).show();
                    } else if (get_username == null && get_password != null) {
                        Toast.makeText(LoginActivity.this, "Please enter your username.", 3000).show();
                    } else {
                        /* Login action */
                    }
                }
            });
        } else {
            setContentView(R.layout.login);
            /* Next interface */
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                Intent intent_about = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
                startActivity(intent_about);
                return true;
            case R.id.menu_help:
                Intent intent_help = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
                startActivity(intent_help);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
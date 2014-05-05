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

        String sp_username = shared_preferences.getString("username", null);
        String sp_password = shared_preferences.getString("password", null);
        if (sp_username == null || sp_password == null) {
            final EditText et_username = (EditText)findViewById(R.id.edit_text_username);
            final EditText et_password = (EditText)findViewById(R.id.edit_text_password);
            Button button_login = (Button)findViewById(R.id.button_login);

            button_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String get_username = et_username.getText().toString();
                    String get_password = et_password.getText().toString();

                    if (get_username.length() == 0 && get_password.length() == 0) {
                        Toast.makeText(LoginActivity.this, "Please input your username and password.", Toast.LENGTH_SHORT).show();
                    } else if (get_username.length() != 0 && get_password.length() == 0) {
                        Toast.makeText(LoginActivity.this, "Please input your password.", Toast.LENGTH_SHORT).show();
                    } else if (get_username.length() == 0 && get_password.length() != 0) {
                        Toast.makeText(LoginActivity.this, "Please input your username.", Toast.LENGTH_SHORT).show();
                    } else {
                        /* Login action */
                    }
                }
            });
        } else {
            /* Next interface */
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
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
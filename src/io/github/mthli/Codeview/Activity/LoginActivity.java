package io.github.mthli.Codeview.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import io.github.mthli.Codeview.R;

public class LoginActivity extends Activity {
    private SharedPreferences shared_preferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.login);

        shared_preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        editor = shared_preferences.edit();

        final EditText et_username = (EditText) findViewById(R.id.login_username);
        final EditText et_password = (EditText) findViewById(R.id.login_password);
        Button button = (Button) findViewById(R.id.login_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String get_username = et_username.getText().toString();
                final String get_password = et_password.getText().toString();

                if (get_username.length() == 0 && get_password.length() == 0) {
                    Toast.makeText(
                            LoginActivity.this,
                            getString(R.string.login_error_miss_username_and_password),
                            Toast.LENGTH_SHORT
                    ).show();
                } else if (get_username.length() == 0 && get_password.length() != 0) {
                    Toast.makeText(
                            LoginActivity.this,
                            getString(R.string.login_error_miss_username),
                            Toast.LENGTH_SHORT
                    ).show();
                } else if (get_username.length() != 0 && get_password.length() == 0) {
                    Toast.makeText(
                            LoginActivity.this,
                            getString(R.string.login_error_miss_password),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {

                }
            }
        });
    }
}

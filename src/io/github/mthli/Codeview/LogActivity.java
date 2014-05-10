package io.github.mthli.Codeview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LogActivity extends Activity {
    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.list_view_log);
        getActionBar().setDisplayShowHomeEnabled(true);

        /*
         * Also we need to implement something here,
         * such as setTitle() and so on.
         */
        Intent intent_log = getIntent();
        getActionBar().setTitle(intent_log.getStringExtra("item_title"));
    }
}

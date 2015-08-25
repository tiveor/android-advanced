package com.possiblelabs.parsepushtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by possiblelabs on 8/14/15.
 */
public class OtherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.txt)).setText("OTHER ACTIVITY");
    }
}

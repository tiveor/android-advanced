package com.possiblelabs.parseanalyticstest;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseAnalytics;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseTrackeableActivty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_trackeable)
    public void track() {
        ParseAnalytics.trackEventInBackground("CLICK_BUTTON_TRACK");
        Toast.makeText(this, "TRACKING", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.btn_trackeable_2)
    public void track2() {
        Map<String, String> map = new HashMap<>();
        map.put("who", "xxxxx");
        ParseAnalytics.trackEventInBackground("CLICK_BUTTON_TRACK_2", map);
        Toast.makeText(this, "TRACKING 2", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, OtherActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

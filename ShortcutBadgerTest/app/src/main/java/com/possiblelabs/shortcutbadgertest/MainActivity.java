package com.possiblelabs.shortcutbadgertest;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SHORTCUT_BADGER";
    private static final String BADGE_COUNT = "badge_count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ShortcutBadger.with(getApplicationContext()).remove();
    }

    private void updateCount(int count){
        ShortcutBadger.with(getApplicationContext()).count(count);

        SharedPreferences.Editor editor = getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        editor.putInt(BADGE_COUNT, count);
        editor.commit();
    }


    public void doAdd(View view) {
        int count = getSharedPreferences(TAG, Context.MODE_PRIVATE).getInt(BADGE_COUNT, 0);
        count++;

        updateCount(count);
    }

    public void doRemove(View view) {
        int count = getSharedPreferences(TAG, Context.MODE_PRIVATE).getInt(BADGE_COUNT, 1);
        count--;

        if (count == 0) {
            return;
        }

        updateCount(count);
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

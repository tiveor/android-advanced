package com.possiblelabs.parseanalyticstest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseAnalytics;

/**
 * Created by possiblelabs on 8/25/15.
 */
public abstract class BaseTrackeableActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}

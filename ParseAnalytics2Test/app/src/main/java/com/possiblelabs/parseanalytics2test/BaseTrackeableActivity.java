package com.possiblelabs.parseanalytics2test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseAnalytics;

/**
 * Created by possiblelabs on 1/11/16.
 */
public class BaseTrackeableActivity extends AppCompatActivity {

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}

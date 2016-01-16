package com.possiblelabs.ganalyticstest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.Tracker;

/**
 * Created by possiblelabs on 8/25/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Tracker mTracker;
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        GAApplication application = (GAApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }
}

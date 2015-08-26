package com.possiblelabs.parseanalyticstest;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * Created by possiblelabs on 8/25/15.
 */
public class ParseApplication extends Application {

    public void onCreate() {
        super.onCreate();
        ParseCrashReporting.enable(this);
        Parse.initialize(this, "YOUR_APP_ID", "YOUR_CLIENT_KEY");
    }
}

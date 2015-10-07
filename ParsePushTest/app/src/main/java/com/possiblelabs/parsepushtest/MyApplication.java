package com.possiblelabs.parsepushtest;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by possiblelabs on 8/14/15.
 */
public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "YOUR Application ID", "YOUR Client Key");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}

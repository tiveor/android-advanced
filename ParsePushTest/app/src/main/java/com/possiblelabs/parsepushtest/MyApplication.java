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
        Parse.initialize(this, "CREDENTIAL_HERE", "CREDENTIAL_HERE");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}

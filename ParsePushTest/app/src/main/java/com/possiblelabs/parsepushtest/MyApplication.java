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
        Parse.initialize(this, "3fDfKhMMvUQoN7Bjvnmfx2Tbtt5VvlqVugYtCS9c", "1UFtiRlaFD0bd13d23BbOIhU2N4TGY7ItxmbOZZo");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}

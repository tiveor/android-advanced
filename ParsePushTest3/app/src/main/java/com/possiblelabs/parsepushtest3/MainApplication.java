package com.possiblelabs.parsepushtest3;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

/**
 * Created by possiblelabs on 1/8/16.
 */
public class MainApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "3fDfKhMMvUQoN7Bjvnmfx2Tbtt5VvlqVugYtCS9c", "1UFtiRlaFD0bd13d23BbOIhU2N4TGY7ItxmbOZZo");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("ALL_PARSE");
    }
}

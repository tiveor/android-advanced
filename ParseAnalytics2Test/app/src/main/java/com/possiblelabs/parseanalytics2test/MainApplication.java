package com.possiblelabs.parseanalytics2test;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by possiblelabs on 1/11/16.
 */
public class MainApplication extends Application {

    public void onCreate(){
        super.onCreate();
        Parse.initialize(this, "APP_ID", "APP_CLIENT");
    }
}

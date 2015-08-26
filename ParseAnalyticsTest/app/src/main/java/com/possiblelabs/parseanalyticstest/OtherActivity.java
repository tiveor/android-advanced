package com.possiblelabs.parseanalyticstest;

import android.os.Bundle;

/**
 * Created by possiblelabs on 8/25/15.
 */
public class OtherActivity extends BaseTrackeableActivty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        throw new RuntimeException("Test Exception!");
    }
}

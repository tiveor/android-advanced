package com.possiblelabs.gametest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.possiblelabs.gametest.pong.PongPanel;

/**
 * Created by possiblelabs on 9/8/15.
 */
public class PongActivity extends Activity {

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new PongPanel(this));

    }
}

package com.possiblelabs.gametest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.possiblelabs.gametest.tetris.TetrisPanel;

/**
 * Created by possiblelabs on 9/8/15.
 */
public class TetrisActivity extends Activity {
    private TetrisPanel tetrisPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("autorotate", true))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        tetrisPanel = new TetrisPanel(this, null);

        setContentView(tetrisPanel);
    }

    public void onDestroy() {
        tetrisPanel.gameOver();
        tetrisPanel.destroy();
        super.onDestroy();
    }


}

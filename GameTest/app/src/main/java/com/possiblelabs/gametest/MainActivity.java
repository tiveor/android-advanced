package com.possiblelabs.gametest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by possiblelabs on 9/8/15.
 */
public class MainActivity extends AppCompatActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
    }

    public void goPong(View view) {
        Intent intent = new Intent(this, PongActivity.class);
        startActivity(intent);
    }

    public void goTetris(View view){
        Intent intent = new Intent(this, TetrisActivity.class);
        startActivity(intent);
    }
}

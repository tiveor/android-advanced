package com.possiblelabs.eventbustest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    private Button btnHi;
    private EditText txtMessage;
    private TextView txtLarge;
    private TextView txtNormal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnHi = (Button) findViewById(R.id.btnHi);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        txtLarge = (TextView) findViewById(R.id.txtLarge);
        txtNormal = (TextView) findViewById(R.id.txtNormal);


        btnHi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = txtMessage.getText().toString();
                if (text == null || text.isEmpty())
                    text = "Hail Idra";

                EventBus.getDefault().post(new HiEvent(text));
                txtMessage.setText("");
            }
        });

    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe
    public void onHiEventLarge(HiEvent event) {
        txtLarge.setText(event.getMessage());
    }

    @Subscribe
    public void onHiEventNormal(HiEvent event) {
        txtNormal.setText(event.getMessage());
    }


}

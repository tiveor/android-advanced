package com.possiblelabs.watsontest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.question_and_answer.v1.QuestionAndAnswer;
import com.ibm.watson.developer_cloud.question_and_answer.v1.model.Answer;
import com.ibm.watson.developer_cloud.question_and_answer.v1.model.QuestionAndAnswerDataset;
import com.ibm.watson.developer_cloud.question_and_answer.v1.model.WatsonAnswer;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText ediQ;
    private TextView txtA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ediQ = (EditText) findViewById(R.id.ediQ);
        txtA = (TextView) findViewById(R.id.txtA);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MainTask().execute(ediQ.getText().toString());
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    public class MainTask extends AsyncTask<String, Void, WatsonAnswer> {

        @Override
        protected WatsonAnswer doInBackground(String... params) {
            QuestionAndAnswer service = new QuestionAndAnswer();
            service.setUsernameAndPassword("user", "pass");
            service.setDataset(QuestionAndAnswerDataset.HEALTHCARE);
            return service.ask(params[0]);
        }

        public void onPostExecute(WatsonAnswer wa) {
            List<Answer> answerList = wa.getAnswers();
            if (answerList.size() > 0) {
                txtA.setText(wa.getAnswers().get(0).getText());
            } else {
                txtA.setText("I don't know");
            }
            Log.d("WATSON:", wa.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

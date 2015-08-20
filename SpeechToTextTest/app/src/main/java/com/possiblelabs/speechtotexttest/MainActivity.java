package com.possiblelabs.speechtotexttest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    private ListView lvLanguages;
    private ListView lvMatches;
    private Spinner spinMatches;
    private Button btnSpeak;
    private String currentLanguage = "es";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvMatches = (ListView) findViewById(R.id.lv_matches);
        lvLanguages = (ListView) findViewById(R.id.lv_languages);
        spinMatches = (Spinner) findViewById(R.id.s_matches);
        btnSpeak = (Button) findViewById(R.id.btn_speak);

        // CHECK ALL LANGUAGES AVAILABLE
        Intent detailsIntent = new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
        sendOrderedBroadcast(detailsIntent, null, new LanguageDetailsChecker(), null, Activity.RESULT_OK, null, null);
    }

    public boolean checkVoiceRecognition() {
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            btnSpeak.setEnabled(false);
            showToastMessage("Voice recognizer not present");
            return false;
        }
        return true;
    }

    public void speak(View view) {

        if (!checkVoiceRecognition())
            return;

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        //CHANGE TO ENGLISH
        // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, currentLanguage);

        if (spinMatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            showToastMessage("Please select No. of Matches from spinner");
            return;
        }

        int noOfMatches = Integer.parseInt(spinMatches.getSelectedItem().toString());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

            if (resultCode == RESULT_OK) {

                ArrayList<String> textMatchList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (!textMatchList.isEmpty()) {
                    if (textMatchList.get(0).contains("search")) {
                        String searchQuery = textMatchList.get(0).replace("search", " ");
                        Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                        search.putExtra(SearchManager.QUERY, searchQuery);
                        startActivity(search);
                    } else {
                        lvMatches.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, textMatchList));
                    }

                }
            } else if (resultCode == RecognizerIntent.RESULT_AUDIO_ERROR) {
                showToastMessage("Audio Error");
            } else if (resultCode == RecognizerIntent.RESULT_CLIENT_ERROR) {
                showToastMessage("Client Error");
            } else if (resultCode == RecognizerIntent.RESULT_NETWORK_ERROR) {
                showToastMessage("Network Error");
            } else if (resultCode == RecognizerIntent.RESULT_NO_MATCH) {
                showToastMessage("No Match");
            } else if (resultCode == RecognizerIntent.RESULT_SERVER_ERROR) {
                showToastMessage("Server Error");
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public class LanguageDetailsChecker extends BroadcastReceiver {
        private ArrayList<String> supportedLanguages;

        private String languagePreference;

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle results = getResultExtras(true);
            if (results.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)) {
                languagePreference = results.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE);
            }
            if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
                supportedLanguages = results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES);
                lvLanguages.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, supportedLanguages));
                lvLanguages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        currentLanguage = supportedLanguages.get(i);
                        showToastMessage("Language Selected:" + currentLanguage);
                    }
                });
            }
        }
    }

}

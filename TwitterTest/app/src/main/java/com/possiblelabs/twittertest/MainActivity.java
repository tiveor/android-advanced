package com.possiblelabs.twittertest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUi;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "YVsZtBSFP8qTZ9i4Oc7tCG4xn";
    private static final String TWITTER_SECRET = "aYbDxLA0b0kaAndAXbIZ97tZOw8VP7MANIwrCyxytVyL5QWzLO";

    private LinearLayout layout;
    private TwitterLoginButton lg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics(), new TweetUi(), new Twitter(authConfig));

        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.layout);
        lg = (TwitterLoginButton) findViewById(R.id.login_button);
        lg.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                loadTweets();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(MainActivity.this, "Something weird", Toast.LENGTH_SHORT).show();
            }
        });

        loadTweets();
    }

    private void loadTweets() {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        //https://dev.twitter.com/rest/reference/get/search/tweets
        twitterApiClient.getSearchService().tweets("Bolivia", null, "es", "es", "recent", 20, null, null, null, null, new Callback<Search>() {
            @Override
            public void success(Result<Search> result) {
                for (Tweet tweet : result.data.tweets) {
                    layout.addView(new TweetView(MainActivity.this, tweet));
                }
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(MainActivity.this, "Something weird", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        lg.onActivityResult(requestCode, resultCode, data);
    }
}

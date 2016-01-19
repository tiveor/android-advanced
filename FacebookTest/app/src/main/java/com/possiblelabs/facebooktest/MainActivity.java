package com.possiblelabs.facebooktest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private TextView userNameView;
    private ProfilePictureView profilePictureView;
    private Profile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        userNameView = (TextView) findViewById(R.id.user_name);
        profilePictureView = (ProfilePictureView) findViewById(R.id.user_pic);

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends"));
//        loginButton.setPublishPermissions(Arrays.asList("publish_actions"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("FB", loginResult.getAccessToken().getToken());
                currentProfile = Profile.getCurrentProfile();

                if (currentProfile != null && currentProfile.getName() != null) {
                    Log.d("FB", currentProfile.getName());
                } else {
                    Log.d("FB", "null profile");
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                MainActivity.this.currentProfile = currentProfile;
                setProfile(currentProfile);
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Profile.fetchProfileForCurrentAccessToken();
            }
        };

        Profile.fetchProfileForCurrentAccessToken();
        setProfile(Profile.getCurrentProfile());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setProfile(Profile profile) {
        if (userNameView == null || profilePictureView == null) {
            return;
        }

        if (profile == null) {
            profilePictureView.setProfileId(null);
            userNameView.setText("NO USER");
        } else {
            profilePictureView.setProfileId(profile.getId());
            userNameView.setText(profile.getName());
        }


    }

    public void getFriends(View view) {

        Bundle parameters = new Bundle();
        parameters.putInt("limit", 10);


        AccessToken access_token = AccessToken.getCurrentAccessToken();

        GraphRequestBatch batch = new GraphRequestBatch(
                GraphRequest.newMeRequest(
                        access_token,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject jsonObject,
                                    GraphResponse response) {
                                // Application code for user
                            }
                        }),
                GraphRequest.newMyFriendsRequest(
                        access_token,
                        new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(
                                    JSONArray jsonArray,
                                    GraphResponse response) {
                                Log.d("FB", "onCompleted");
                        JSONObject baseJson = response.getJSONObject();

                        try {
                            Log.d("FB", baseJson.toString());
                            JSONArray arrayJson = baseJson.getJSONArray("data");
                            Log.d("FB", arrayJson.length() + "<<");
                            for (int i = 0; i < arrayJson.length(); i++) {
                                JSONObject friend = arrayJson.getJSONObject(i);
                                Log.d("FB", friend.getString("name").toString());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                            }
                        })
        );
        batch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch graphRequests) {
                // Application code for when the batch finishes
            }
        });
        batch.executeAsync();

//
//        GraphRequest.newMyFriendsRequest(
//                AccessToken.getCurrentAccessToken(),
//                "v2.2/me/friends",
//                parameters,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//                        Log.d("FB", "onCompleted");
//                        JSONObject baseJson = response.getJSONObject();
//
//                        try {
//                            Log.d("FB", baseJson.toString());
//                            JSONArray arrayJson = baseJson.getJSONArray("data");
//                            Log.d("FB", arrayJson.length() + "<<");
//                            for (int i = 0; i < arrayJson.length(); i++) {
//                                JSONObject friend = arrayJson.getJSONObject(i);
//                                Log.d("FB", friend.getString("name").toString());
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }
//        ).executeAsync();
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
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
        accessTokenTracker.stopTracking();
    }
}

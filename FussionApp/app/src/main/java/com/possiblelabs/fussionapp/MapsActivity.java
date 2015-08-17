package com.possiblelabs.fussionapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker currentMarker;
    private LatLng currentPosition;

    private TextView txtLat;
    private TextView txtLng;
    private EditText ediName;
    private Button btnSave;
    private FPlaceDAO dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        dao = new FPlaceDAO();

        loadReferences();
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doSave();
                clearMap();
                loadAllPlaces();
            }
        });

        setUpMapIfNeeded();
    }

    private void loadReferences() {
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLng = (TextView) findViewById(R.id.txtLng);
        ediName = (EditText) findViewById(R.id.ediName);
        btnSave = (Button) findViewById(R.id.btnSave);
    }

    private void doSave() {
        String name = ediName.getText().toString();

        if (name.isEmpty()) {
            return;
        }
        MyApplication.speakOut(name);

        FPlace place = new FPlace();
        place.name = name;
        place.lat = currentPosition.latitude;
        place.lng = currentPosition.longitude;
        dao.addPlace(place);
        ediName.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        loadAllPlaces();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                doMapClick(latLng);
            }
        });
    }

    private void loadAllPlaces() {
        dao.getPlaces(new FPlaceCallback() {
            @Override
            public void getPlaces(List<FPlace> places) {
                for (FPlace place : places) {
                    mMap.addMarker(place.toMarker());
                }
            }
        });
    }

    private void updateLatLng(LatLng latLng) {
        txtLat.setText("LAT:" + latLng.latitude);
        txtLng.setText("LNG:" + latLng.longitude);
        currentPosition = latLng;
    }

    private void clearMap() {
        mMap.clear();
        currentMarker = null;
    }

    private void doMapClick(LatLng latLng) {
        if (currentMarker == null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Posición Actual"));
        } else {
            currentMarker.setPosition(latLng);
        }
        updateLatLng(latLng);
    }


    public void openYoutubeVideo(View view) {
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, getResources().getString(R.string.google_maps_key), "BZP1rYjoBgI");
        if (intent != null) {
            startActivity(intent);
        }
    }
}

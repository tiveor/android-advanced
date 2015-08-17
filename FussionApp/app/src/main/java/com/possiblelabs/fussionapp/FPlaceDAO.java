package com.possiblelabs.fussionapp;


import com.activeandroid.query.Select;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by possiblelabs on 8/15/15.
 */
public class FPlaceDAO {


    private boolean WORKING_LOCAL = false;

    public void addPlace(FPlace place) {
        if (WORKING_LOCAL)
            place.saveLocally();
        else
            place.saveInParse();
    }

    private void getPlacesLocally(FPlaceCallback callback) {
        List<FPlace> places = new Select()
                .from(FPlace.class)
                .execute();
        callback.getPlaces(places);
    }

    public void getPlacesRemotely(final FPlaceCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FPlace");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> placeList, ParseException e) {

                List<FPlace> places = new ArrayList<FPlace>();

                for (ParseObject p : placeList)
                    places.add(FPlace.fromParseObject(p));


                callback.getPlaces(places);
            }

        });
    }

    public void getPlaces(FPlaceCallback callback) {
        if (WORKING_LOCAL) {
            getPlacesLocally(callback);
        } else {
            getPlacesRemotely(callback);
        }
    }

    public void syncPlace() {

    }


}

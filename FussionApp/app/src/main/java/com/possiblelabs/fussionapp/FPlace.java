package com.possiblelabs.fussionapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by possiblelabs on 8/15/15.
 */
@Table(name = "places")
public class FPlace extends Model {
    @Column(name = "nombre", index = true)
    public String name;
    @Column(name = "lat")
    public double lat;
    @Column(name = "lng")
    public double lng;
    @Column(name = "image")
    public String image = "";

    public FPlace() {
        super();
    }

    public void saveLocally() {
        save();
    }

    public void saveInParse() {
        ParseObject testObject = new ParseObject("FPlace");
        testObject.put("name", name);

        ParseGeoPoint pg = new ParseGeoPoint();
        pg.setLatitude(lat);
        pg.setLongitude(lng);
        testObject.put("position", pg);
        if (image != null)
            testObject.put("image", image);
        testObject.saveInBackground();
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    public static FPlace fromParseObject(ParseObject p) {
        FPlace place = new FPlace();
        place.name = p.getString("name");
        ParseGeoPoint geo = p.getParseGeoPoint("position");
        place.lat = geo.getLatitude();
        place.lng = geo.getLongitude();
        place.image = p.getString("image");
        return place;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FPlace{");
        sb.append("name='").append(name).append('\'');
        sb.append(", lat=").append(lat);
        sb.append(", lng=").append(lng);
        sb.append(", imageName='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }


    public MarkerOptions toMarker() {
        MarkerOptions mo = new MarkerOptions();
        mo.position(getLatLng());
        mo.title(name);
        BitmapDescriptor b = BitmapDescriptorFactory.fromResource(R.drawable.place);
        mo.icon(b);
        return mo;
    }
}
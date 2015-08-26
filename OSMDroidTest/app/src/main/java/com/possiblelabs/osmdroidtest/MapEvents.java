package com.possiblelabs.osmdroidtest;

import org.osmdroid.util.GeoPoint;

/**
 * Created by possiblelabs on 8/26/15.
 */
public interface MapEvents {

    boolean singleTapConfirmedHelper(GeoPoint geoPoint);

    boolean longPressHelper(GeoPoint geoPoint);

}

package com.possiblelabs.osmdroidtest;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

/**
 * Created by possiblelabs on 8/26/15.
 */
public class MyOverlay extends Overlay {

    private MapEvents mReceiver;

    public MyOverlay(Context ctx, MapEvents receiver) {
        super(ctx);
        mReceiver = receiver;
    }

    @Override protected void draw(Canvas c, MapView osmv, boolean shadow) {
        //Nothing to draw
    }

    @Override public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView){
        Projection proj = mapView.getProjection();
        GeoPoint p = (GeoPoint)proj.fromPixels((int)e.getX(), (int)e.getY());
        return mReceiver.singleTapConfirmedHelper(p);
    }

    @Override public boolean onLongPress(MotionEvent e, MapView mapView) {
        Projection proj = mapView.getProjection();
        GeoPoint p = (GeoPoint)proj.fromPixels((int)e.getX(), (int)e.getY());
        return mReceiver.longPressHelper(p);
    }

}
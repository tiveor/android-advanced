package com.possiblelabs.osmdroidtest;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView;
    private MapController mMapController;
    private OverlayItem currentMarker;

    private DefaultResourceProxyImpl mResourceProxy;
    private ItemizedOverlay<OverlayItem> positionOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.mapview);
//        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
//        mMapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);

        mMapView.setBuiltInZoomControls(true);
        mMapView.setClickable(true);

        mResourceProxy = new DefaultResourceProxyImpl(getApplicationContext());


        mMapController = (MapController) mMapView.getController();
        mMapController.setZoom(10);
        GeoPoint gPt = new GeoPoint(-17392556, -66158836);
        mMapController.setCenter(gPt);

        addMarker(gPt);
        addOverlay();

    }

    private void addOverlay() {

        Overlay newOverlay = new MyOverlay(this, new MapEvents() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                mMapView.getOverlays().remove(positionOverlay);
                addMarker(geoPoint);
                mMapView.invalidate();
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint geoPoint) {
                mMapView.getOverlays().remove(positionOverlay);
                addMarker(geoPoint);
                mMapView.invalidate();
                return true;
            }
        });

        mMapView.getOverlays().add(newOverlay);
    }

    private void addMarker(GeoPoint gPt) {

        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        currentMarker = new OverlayItem("Here", "SampleDescription", gPt);
        items.add(currentMarker);

        positionOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(MainActivity.this, "Tap Item: " + item.getTitle(), Toast.LENGTH_LONG).show();
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        Toast.makeText(MainActivity.this, "Long Item: " + item.getTitle(), Toast.LENGTH_LONG).show();
                        return false;
                    }
                }, mResourceProxy);
        mMapView.getOverlays().add(positionOverlay);
        mMapView.invalidate();
    }

    private GeoPoint geoPointFromScreenCoords(int x, int y, MapView vw) {
        if (x < 0 || y < 0 || x > vw.getWidth() || y > vw.getHeight()) {
            return null;
        }
        Projection projection = vw.getProjection();
        GeoPoint geoPointTopLeft = (GeoPoint) projection.fromPixels(0, 0);
        Point topLeftPoint = new Point();
        projection.toPixels(geoPointTopLeft, topLeftPoint);
        GeoPoint rtnGeoPoint = (GeoPoint) projection.fromPixels(x, y);
        return rtnGeoPoint;
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

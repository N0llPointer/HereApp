package com.nollpointer.hereapp;

import android.graphics.PointF;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapScreenMarker;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "HereApp";

    public boolean isOnScreenMarkerShown = false;

    private Map map = null;

    // map fragment embedded in this activity
    private MapFragment mapFragment = null;

    private Toolbar toolbar;
    private MapScreenMarker screenMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void showOnScreenMarker(){
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.getMenu().findItem(R.id.check).setVisible(true);
        screenMarker = new MapScreenMarker();
        Image image = new Image();
        try {
            image.setImageResource(R.drawable.ic_marker);
        } catch (IOException e) {
            e.printStackTrace();
        }
        screenMarker.setIcon(image);
        View view = findViewById(R.id.container);
        screenMarker.setScreenCoordinate(new PointF(view.getWidth()/2,view.getHeight()/2));
        map.addMapObject(screenMarker);
    }

    private void hideOnScreenMarker(){
        toolbar.setNavigationIcon(R.drawable.ic_add_marker);
        toolbar.getMenu().findItem(R.id.check).setVisible(false);
        map.removeMapObject(screenMarker);
    }

    private void initialize() {
        setContentView(R.layout.activity_main);

        // Search for the map fragment to finish setup by calling init().
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_add_marker);

        toolbar.inflateMenu(R.menu.toolbar_menu);

        toolbar.getMenu().findItem(R.id.check).setVisible(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnScreenMarkerShown) {
                    hideOnScreenMarker();
                } else {
                    showOnScreenMarker();
                }
                isOnScreenMarkerShown = !isOnScreenMarkerShown;
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.check){
                    Image image = new Image();
                    try {
                        image.setImageResource(R.drawable.ic_marker);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    map.addMapObject(new MapMarker(map.getCenter(),image));
                }
                return true;
            }
        });

        // Set up disk cache path for the map service for this application
        // It is recommended to use a path under your application folder for storing the disk cache
        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                getApplicationContext().getExternalFilesDir(null) + File.separator + ".here-maps",
                "HereApp"); /* ATTENTION! Do not forget to update {YOUR_INTENT_NAME} */

        if (!success) {
            Toast.makeText(getApplicationContext(), "Unable to set isolated disk cache path.", Toast.LENGTH_LONG).show();
        } else {
            mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == OnEngineInitListener.Error.NONE) {
                        // retrieve a reference of the map from the map fragment
                        map = mapFragment.getMap();
                        // Set the map center to the Vancouver region (no animation)
                        map.setCenter(new GeoCoordinate(45.039496, 41.958023, 0.0),
                                Map.Animation.BOW);
                        // Set the zoom level to the average between min and max
                        map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);

                        try {
                            Image image = new Image();
                            image.setImageResource(R.drawable.ic_marker);
                            map.addMapObject(new MapMarker(new GeoCoordinate(45.039496, 41.958023),image));
                        } catch (IOException e) {
                            Log.wtf(TAG,e);
                        }

                        mapFragment.getMapGesture().addOnGestureListener(new MapGesture.OnGestureListener.OnGestureListenerAdapter() {

                            @Override
                            public boolean onMapObjectsSelected(List<ViewObject> list) {
                                for(ViewObject v: list){
                                    MapObject m = (MapObject) v;
                                    if(m instanceof MapMarker){
                                        MapMarker marker = (MapMarker) v;
                                        //GeoCoordinate coords = marker.getCoordinate();
                                        Snackbar.make(findViewById(R.id.container),"Marker is clicked",Snackbar.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                                return true;
                            }
                        },0,false);
                    } else {
                        System.out.println("ERROR: Cannot initialize Map Fragment");
                    }
                }
            });
        }
    }
}

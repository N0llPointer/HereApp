package com.nollpointer.hereapp;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private RecyclerView resultsRecycler;
    private DrawerLayout drawerLayout;
    private EditText searchEditText;
    private LinearLayout searchContainer;

    private DrawerArrowDrawable arrow;

    private MapScreenMarker screenMarker;
    //private View splash_screen_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initializeViews();

        initialize();
    }

    private void showOnScreenMarker(){
        //toolbar.setNavigationIcon(R.drawable.ic_close);
        //toolbar.getMenu().findItem(R.id.check).setVisible(true);
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

    private void initializeViews(){
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);

        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        resultsRecycler = findViewById(R.id.results_recycler);
        resultsRecycler.setLayoutManager(new LinearLayoutManager(this));

        searchEditText = findViewById(R.id.search_edit_text);

        searchContainer = findViewById(R.id.search_results_container);

        arrow = new DrawerArrowDrawable(this);
        arrow.setProgress(0f);

        toolbar.setNavigationIcon(arrow);

        toolbar.inflateMenu(R.menu.toolbar_menu);

        toolbar.getMenu().findItem(R.id.erase_text).setVisible(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    showSearchUi();
                }else{
                    closeSearchUi();
                }
            }
        });
    }

    //private void

    private void showSearchUi(){
        searchContainer.setVisibility(View.VISIBLE);
        SearchResultCardsAdapter adapter = new SearchResultCardsAdapter();
        resultsRecycler.setAdapter(adapter);

    }

    private void closeSearchUi(){
        searchContainer.setVisibility(View.GONE);
    }

    private void hideOnScreenMarker(){
        //toolbar.setNavigationIcon(R.drawable.ic_add_marker);
        //toolbar.getMenu().findItem(R.id.check).setVisible(false);
        map.removeMapObject(screenMarker);
    }

    private void loseFocus(){

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawer(Gravity.START);
        else if(searchEditText.isFocused())
            loseFocus();
        else
            super.onBackPressed();
    }

    private void initialize() {

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
                        //splash_screen_view.setVisibility(View.GONE);

                        // retrieve a reference of the map from the map fragment
                        map = mapFragment.getMap();
                        // Set the map center to the Vancouver region (no animation)
                        map.setCenter(new GeoCoordinate(45.039496, 41.958023, 0.0),
                                Map.Animation.LINEAR);
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
                        Log.wtf(TAG,"ERROR: Cannot initialize Map Fragment");
                    }
                }
            });
        }
    }
}

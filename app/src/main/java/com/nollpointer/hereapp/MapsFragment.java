package com.nollpointer.hereapp;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.LocationDataSource;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapScreenMarker;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class MapsFragment extends Fragment {

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
    private AHBottomNavigation bottomNavigation;
    private FloatingActionButton fab;

    private View mainView;

    private DrawerArrowDrawable arrow;

    private MapScreenMarker screenMarker;
    //private View splash_screen_view;

    private boolean isEditTextHasFocus = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_maps, container, false);


        initializeViews();

        initialize();

        return mainView;
    }

    private void initializeViews(){
        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapfragment);

        toolbar = mainView.findViewById(R.id.toolbar);

        drawerLayout = mainView.findViewById(R.id.drawer_layout);

        bottomNavigation = mainView.findViewById(R.id.bottom_navigation);

        fab = mainView.findViewById(R.id.floatingActionButton);

        resultsRecycler = mainView.findViewById(R.id.results_recycler);
        resultsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        searchEditText = mainView.findViewById(R.id.search_edit_text);

        searchContainer = mainView.findViewById(R.id.search_results_container);

        arrow = new DrawerArrowDrawable(getContext());
        arrow.setProgress(0f);
        arrow.setColor(Color.BLACK);

        toolbar.setNavigationIcon(arrow);

        toolbar.inflateMenu(R.menu.toolbar_menu);

        toolbar.getMenu().findItem(R.id.erase_text).setVisible(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEditTextHasFocus)
                    loseFocus();
                else
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

                isEditTextHasFocus = hasFocus;
            }
        });
        //TODO Разобраться с этой шляпой. Как меня она уже достала
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PositioningManager manager = PositioningManager.getInstance();
                manager.start(PositioningManager.LocationMethod.GPS_NETWORK);
                GeoPosition geo = manager.getPosition();
                map.setCenter(geo.getCoordinate(),
                        Map.Animation.LINEAR);

                PositioningManager.OnPositionChangedListener positionListener = new
                        PositioningManager.OnPositionChangedListener() {

                            public void onPositionUpdated(PositioningManager.LocationMethod method,
                                                          GeoPosition position, boolean isMapMatched) {
                                    map.setCenter(position.getCoordinate(),
                                            Map.Animation.NONE);
                            }

                            public void onPositionFixChanged(PositioningManager.LocationMethod method,
                                                             PositioningManager.LocationStatus status) {

                            }
                        };

                manager.addListener(new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));
            }
        });

        AHBottomNavigationItem item1 =
                new AHBottomNavigationItem("Что рядом?",
                        R.drawable.ic_nearby);

        AHBottomNavigationItem item2 =
                new AHBottomNavigationItem("Акции",
                        R.drawable.ic_sale);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setDefaultBackgroundColor(Color.WHITE);
        bottomNavigation.setAccentColor(getResources().getColor(R.color.colorAccent));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                AHNotification notification = new AHNotification.Builder()
                        .setText(Integer.toString(5))
                        .setBackgroundColor(Color.YELLOW)
                        .setTextColor(Color.BLACK)
                        .build();
                // Adding notification to last item.
                bottomNavigation.setNotification(notification, position);
                //notificationVisible = true;

                return true;
            }
        });
    }


    private void showSearchUi(){
        arrow.setProgress(1f);
        searchContainer.setVisibility(View.VISIBLE);
        SearchResultCardsAdapter adapter = new SearchResultCardsAdapter();
        resultsRecycler.setAdapter(adapter);

    }

    private void closeSearchUi(){
        searchContainer.setVisibility(View.GONE);
    }

    private void loseFocus(){
        arrow.setProgress(0f);
        searchEditText.clearFocus();
        mainView.findViewById(R.id.voice_input).requestFocus();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Вызывается, когда пользователь нажал назад. Возвращает true, если фрагмент обработал нажатие
    public boolean onBackPressed(){
        if(drawerLayout.isDrawerOpen(Gravity.START)){
            drawerLayout.closeDrawer(Gravity.START);
            return true;
        }else if(searchEditText.hasFocus()){
            loseFocus();
            return true;
        }else
            return false;
    }

//    @Override
//    public void onBackPressed() {
//        if(drawerLayout.isDrawerOpen(Gravity.START))
//            drawerLayout.closeDrawer(Gravity.START);
//        else if(searchEditText.hasFocus())
//            loseFocus();
//        else
//            super.onBackPressed();
//    }



    private void initialize() {

        // Set up disk cache path for the map service for this application
        // It is recommended to use a path under your application folder for storing the disk cache
        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                getContext().getExternalFilesDir(null) + File.separator + ".here-maps",
                "HereApp"); /* ATTENTION! Do not forget to update {YOUR_INTENT_NAME} */

        if (!success) {
            Toast.makeText(getContext(), "Unable to set isolated disk cache path.", Toast.LENGTH_LONG).show();
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
                                        Snackbar.make(mainView.findViewById(R.id.container),"Marker is clicked",Snackbar.LENGTH_SHORT).show();
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

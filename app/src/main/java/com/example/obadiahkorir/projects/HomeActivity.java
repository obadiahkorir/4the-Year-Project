package com.example.obadiahkorir.projects;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>,View.OnClickListener {

    LinearLayout nextofkin,firelayout,police_layout,theft_layout,corruption_layout,locationstatisticslinear,phone_layout,attack_layout,accident_layout,gallery_layout,childabuse,layout_premium,ambulancelayout;

    private SessionHandler session;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;

    private static final String TAG = HomeActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        txtProfileName.setText("Welcome "+user.getFullName()+", your session will expire on "+user.getSessionExpiryDate());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);


        nextofkin = (LinearLayout) findViewById(R.id.nextofkin);
        nextofkin.setOnClickListener(this);

        locationstatisticslinear= (LinearLayout) findViewById(R.id. locationstatisticslinear);
        locationstatisticslinear.setOnClickListener(this);

        firelayout= (LinearLayout) findViewById(R.id.firelayout);
        firelayout.setOnClickListener(this);


        ambulancelayout= (LinearLayout) findViewById(R.id.ambulancelayout);
        ambulancelayout.setOnClickListener(this);

        theft_layout= (LinearLayout) findViewById(R.id.theft_layout);
        theft_layout.setOnClickListener(this);

        corruption_layout= (LinearLayout) findViewById(R.id.corruption_layout);
        corruption_layout.setOnClickListener(this);

        accident_layout= (LinearLayout) findViewById(R.id.accident_layout);
        accident_layout.setOnClickListener(this);

        phone_layout= (LinearLayout) findViewById(R.id.phone_layout);
        phone_layout.setOnClickListener(this);


        layout_premium= (LinearLayout) findViewById(R.id.layout_premium);
        layout_premium.setOnClickListener(this);

        attack_layout= (LinearLayout) findViewById(R.id.attack_layout);
        attack_layout.setOnClickListener(this);

        locationstatisticslinear= (LinearLayout) findViewById(R.id.locationstatisticslinear);
        locationstatisticslinear.setOnClickListener(this);

        gallery_layout= (LinearLayout) findViewById(R.id.gallery_layout);
        gallery_layout.setOnClickListener(this);

        police_layout= (LinearLayout) findViewById(R.id.police_layout);
        police_layout.setOnClickListener(this);

        childabuse= (LinearLayout) findViewById(R.id.childabuse);
        childabuse.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onClick(View v) {
        if (v == nextofkin) {
            NextOfKinFunction();
        }
        if (v == locationstatisticslinear) {
            PickPlace();
        }
        if (v == firelayout) {
            FireFunction();

        }
        if (v == gallery_layout) {
            GalleryFunction();

        }
        if (v == childabuse) {
            ChildFunction();

        }
        if (v == corruption_layout) {
            CorruptionFunction();

        }
        if (v == theft_layout) {
            TheftFunction();

        }
        if (v == locationstatisticslinear) {
            PickPlace();

        }
        if (v == ambulancelayout) {
            AmbulanceFunction();

        }
        if (v == phone_layout) {
            ContactsFunction();

        }
        if (v == layout_premium) {
            PremiumFunction();

        }
        if (v == phone_layout) {
            ContactsFunction();
        }
        if (v == accident_layout) {
            AccidentFunction();
        }
        if (v == attack_layout) {
            AttackFunction();
        }
        if (v == police_layout) {
            PoliceFunction();
        }

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);

    } else if (id == R.id.nav_profile) {
            Intent i = new Intent(getApplicationContext(),MyLocationUsingLocationAPI.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);

        }
        else if (id == R.id.nav_search) {
            Intent i = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);

        }else if (id == R.id.nav_location) {
            Intent i = new Intent(getApplicationContext(),PlacePickerActivity.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);

        } else if (id == R.id.nav_chat) {
            Intent i = new Intent(getApplicationContext(),ChatActivity.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.zoom_in,
                    R.anim.zoom_out);
        } else if (id == R.id.nav_oba) {
            Intent i = new Intent(getApplicationContext(),ObaActivity.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.zoom_in,
                    R.anim.zoom_out);

        } else if (id == R.id.nav_about) {
            Intent i = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.zoom_in,
                    R.anim.zoom_out);

        }  else if (id == R.id.nav_logout) {
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.zoom_in,
                    R.anim.zoom_out);

        }
        else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "Check it out. Your message goes here";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            return true;

        }
        else if (id == R.id.nav_send) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "Check it out. Your message goes here";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            return true;

        }
        else if (id == R.id.nav_rate) {
            Intent i = new Intent(getApplicationContext(),RateUsActivity.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.zoom_in,
                    R.anim.zoom_out);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Sets up the options menu.
     *
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     *
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        if (item.getItemId() == R.id.action_image) {
            Intent i = new Intent(getApplicationContext(),UploadImageActivity.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
        }
        if (item.getItemId() == R.id.action_location) {
            Intent i = new Intent(getApplicationContext(),SearchActivity.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
        }
        if (item.getItemId() == R.id.view_Place) {
            Intent i = new Intent(getApplicationContext(),CurrentLocationNearByPlacesActivity.class);
            startActivity(i);
            HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
        }
        return true;
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                mLikelyPlaceNames = new String[count];
                                mLikelyPlaceAddresses = new String[count];
                                mLikelyPlaceAttributions = new String[count];
                                mLikelyPlaceLatLngs = new LatLng[count];

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                            .getAddress();
                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                            .getAttributions();
                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }

                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));
                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }
    }

    @SuppressLint("NewApi")
    public void NextOfKinFunction() {
        Intent i = new Intent(getApplicationContext(),NextofKinActivity.class);
        startActivity(i);
        HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @SuppressLint("NewApi")
    public void PickPlace() {
        Intent i = new Intent(getApplicationContext(),MyLocationUsingLocationAPI.class);
        startActivity(i);
        HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @SuppressLint("NewApi")
    public void FireFunction() {
        Intent i = new Intent(getApplicationContext(),HomeFireActivity.class);
        startActivity(i);
        HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @SuppressLint("NewApi")
    public void AmbulanceFunction() {
        Intent i = new Intent(getApplicationContext(),AmbulanceActivity.class);
        startActivity(i);
      HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @SuppressLint("NewApi")
    public void ChildFunction() {
        Intent i = new Intent(getApplicationContext(),ChildAbuseActivity.class);
        startActivity(i);
       HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @SuppressLint("NewApi")
    public void ContactsFunction() {
        Intent i = new Intent(getApplicationContext(),HotlineActivity.class);
        startActivity(i);
       HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @SuppressLint("NewApi")
    public void AttackFunction() {
        Intent i = new Intent(getApplicationContext(),AttackActivity.class);
        startActivity(i);
        HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }


    @SuppressLint("NewApi")
    public void  PremiumFunction() {
        Intent i = new Intent(getApplicationContext(),Premium.class);
        startActivity(i);
        HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @SuppressLint("NewApi")
    public void  AccidentFunction() {
        Intent i = new Intent(getApplicationContext(),AccidentActivity.class);
        startActivity(i);
       HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @SuppressLint("NewApi")
    public void  PoliceFunction() {
        Intent i = new Intent(getApplicationContext(),PoliceActivity.class);
        startActivity(i);
       HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @SuppressLint("NewApi")
    public void  TheftFunction() {
        Intent i = new Intent(getApplicationContext(),TheftActivity.class);
        startActivity(i);
        HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @SuppressLint("NewApi")
    public void  CorruptionFunction() {
        Intent i = new Intent(getApplicationContext(),CorruptionActivity.class);
        startActivity(i);
        HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
    @SuppressLint("NewApi")
    public void  GalleryFunction() {
        Intent i = new Intent(getApplicationContext(),GalleryActivity.class);
        startActivity(i);
        HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }


}
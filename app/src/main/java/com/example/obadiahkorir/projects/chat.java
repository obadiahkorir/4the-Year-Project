package com.example.obadiahkorir.projects;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
//import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class chat extends AppCompatActivity {
    String email;
    String twitter;



    String value;
    TextView txtv1;
    TextView txtv2;
    TextView txtv3;
    TextView txtv4;
    TextView txtv5;
    TextView txtv6;

    TextView txtv15;
    TextView txtv16;
    TextView txtv25;
    TextView txtv26;

    String [] words;

    LocationManager locationManager ;
    LocationListener locationListener;

    private static final String TAG = LearningActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;


    /////// Camera parts
    private static final int ACTION_TAKE_PHOTO_B = 1;
    //private static final int ACTION_TAKE_PHOTO_S = 2;


    private ImageView mImageView;
    //private Bitmap mImageBitmap;

    private String mCurrentPhotoPath;
    /////////////////////////my file////////////////
    private File myFile;


    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;



    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    private static final String LOCATION_ADDRESS_KEY = "location-address";
    private boolean mAddressRequested;
    private String mAddressOutput;
    private AddressResultReceiver mResultReceiver;
    private TextView mLocationAddressTextView;
    private ProgressBar mProgressBar;
    private Button mFetchAddressButton;


    LinearLayout ll1;
    LinearLayout ll2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polic);

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);

        txtv1 = (TextView) findViewById(R.id.textView1);
        txtv2 = (TextView) findViewById(R.id.textView2);
        txtv3 = (TextView) findViewById(R.id.textView3);
        txtv4 = (TextView) findViewById(R.id.textView4);
        txtv5 = (TextView) findViewById(R.id.textView5);
        txtv6 = (TextView) findViewById(R.id.textView6);

        txtv15 = (TextView) findViewById(R.id.textView15);
        txtv16 = (TextView) findViewById(R.id.textView16);
        txtv25 = (TextView) findViewById(R.id.textView25);
        txtv26 = (TextView) findViewById(R.id.textView26);




        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mAlbumStorageDirFactory = new BaseAlbumDirFactory();

        mResultReceiver = new AddressResultReceiver(new Handler());

        mFetchAddressButton = (Button) findViewById(R.id.fetch_address_button);


        mAddressRequested = false;
        mAddressOutput = "";


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }


    public void makeUseOfNewLocation(Location l)
    {
        mLastLocation = l;
        Log.w(TAG, "SET !!: "+l.toString(),new Exception() );
        locationManager.removeUpdates(locationListener);
    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            if(isNetworkAvailable()==false)
            {
                Toast.makeText(chat.this, "Not connected to Internet", Toast.LENGTH_SHORT).show();
                return;
            }
            getLastLocation();
            getAddress();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public void fetchAddressButtonHandler(View view) {
        if (mLastLocation != null) {
            startIntentService();


            new myAsyncTask().execute();
            mLastLocation.getLongitude();

           // updateUIWidgets();
            return;
        }

        else
        {
            mAddressRequested = true;
            //getLastLocation();
            Toast.makeText(chat.this, "Cannot access location..", Toast.LENGTH_SHORT).show();
        }

    }


    private void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }


    private void getAddress() {
        int permissionCheck = ContextCompat.checkSelfPermission(chat.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.w(TAG, "onSuccess:null");
                            return;
                        }

                        mLastLocation = location;

                        // Determine whether a Geocoder is available.
                        if (!Geocoder.isPresent()) {
                            showSnackbar(getString(R.string.no_geocoder_available));
                            return;
                        }

                        if (mAddressRequested) {
                            startIntentService();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(chat.this, "Cannot access address.", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "getLastLocation:onFailure", e);
                    }
                });
    }


    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            //displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
            //updateUIWidgets();
        }
    }


    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());

                        }
                    }
                });
    }

    private void showSnackbar(final String text) {
        View container = findViewById(R.id.main_activity_container);
        if (container != null) {
            Toast.makeText(getApplicationContext(), "Location not detected.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {

        Toast.makeText(getApplicationContext(), "Grant permission to access location.",
                Toast.LENGTH_SHORT).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(chat.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            startLocationPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Toast.makeText(chat.this, "Permission not granted.", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLastLocation();
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    class myAsyncTask extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                while(mLastLocation == null);
                //URL url = new URL("http://52.168.24.171:5000/?lat="+mLastLocation.getLatitude()+"&lon="+mLastLocation.getLongitude());

                URL url = new URL("https://chemisoftsolutions.000webhostapp.com/?lat="+mLastLocation.getLatitude()+"&lon="+mLastLocation.getLongitude());

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                value = br.readLine();
                words = value.split(";");
                email = words[6];
                twitter = words[7];
                //System.out.print(value);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String t = "<b>" + "Hospital:"+ "</b> " + words[0];
                        txtv1.setText(Html.fromHtml(t));
                        t = "<b>" + "Address:"+ "</b> " + words[1];
                        txtv2.setText(Html.fromHtml(t));
                        t = "<b>" + "Phone:"+ "</b> " + words[2];
                        txtv3.setText(Html.fromHtml(t));
                        t = "<b>" + "Police Station:"+ "</b> " + words[3];
                        txtv4.setText(Html.fromHtml(t));
                        t = "<b>" + "Address:"+ "</b> " + words[4];
                        txtv5.setText(Html.fromHtml(t));
                        t = "<b>" + "Phone:"+ "</b> " + words[5];
                        txtv6.setText(Html.fromHtml(t));

                        t = "<b>" + "Distance:"+ "</b> " + words[8];
                        txtv15.setText(Html.fromHtml(t));
                        t = "<b>" + "Duration:"+ "</b> " + words[9];
                        txtv16.setText(Html.fromHtml(t));
                        t = "<b>" + "Distance:"+ "</b> " + words[10];
                        txtv25.setText(Html.fromHtml(t));
                        t = "<b>" + "Duration:"+ "</b> " + words[11];
                        txtv26.setText(Html.fromHtml(t));

                        ll1.setVisibility(View.VISIBLE);
                        ll2.setVisibility(View.VISIBLE);


                    }
                });

            }
            catch (Exception e)
            {
                Log.e("----", "doInBackground: "+e.toString(),e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //spinner.setVisibility(View.GONE);

        }
    }

    public void send(View view) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, email);
        i.putExtra(Intent.EXTRA_SUBJECT, "Emergency !");

        if(mAddressOutput!=null && mLastLocation!=null) {
            i.putExtra(Intent.EXTRA_TEXT, "Address:" + mAddressOutput + "\nLatitude:" + mLastLocation.getLatitude() + "\nLongitude:" + mLastLocation.getLongitude() + "\nAccuracy:" + mLastLocation.getAccuracy());
        }

        if(myFile!=null) {
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myFile));
        }

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(chat.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }


    public void dailerp(View view) {

        if(!words[5].equals("Not Available")) {
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);

            phoneIntent.setData(Uri.parse("tel:" + words[5]));
            startActivity(phoneIntent);
        }

    }

    public void dailerh(View view) {
        if(!words[2].equals("Not Available")) {
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:" + words[2]));
            startActivity(phoneIntent);
        }
    }
}

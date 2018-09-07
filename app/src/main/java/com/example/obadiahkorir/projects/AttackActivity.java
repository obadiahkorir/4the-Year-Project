package com.example.obadiahkorir.projects;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AttackActivity extends AppCompatActivity implements
        View.OnClickListener, LocationListener, AdapterView.OnItemSelectedListener {
    public Spinner spinner;
    private SessionHandler session;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String lat;
    String provider;
    protected String latitude, longitude;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Toolbar toolbar;
    // Creating EditText.
    EditText FirstName, LastName, Email, County, Attacktype, Contact, Date;
    // Creating button;
    Button InsertButton;
    // Creating Volley RequestQueue.
    RequestQueue requestQueue;
    // Create string variable to hold the EditText Value.
    String FirstNameHolder, LastNameHolder, EmailHolder, SpinnerHolder, CountyHolder, AttackTypeHolder, TimeHolder, ContactHolder;
    // Creating Progress dialog.
    ProgressDialog progressDialog;
    // Storing server url into String variable.
    String HttpUrl = "https://chemisoftsolutions.000webhostapp.com/android/attack_insert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack);
        toolbar = (Toolbar) findViewById(R.id.back);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setTitle("Report Attack Incident.");


        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        // Assigning ID's to EditText.
        FirstName = (EditText) findViewById(R.id.editTextFirstName);
        LastName = (EditText) findViewById(R.id.editTextLastName);
        Email = (EditText) findViewById(R.id.editTextEmail);
        Email.setText(user.getFullName());
        Contact = (EditText) findViewById(R.id.contact);
        Attacktype = (EditText) findViewById(R.id.attacktype);
        Date = (EditText) findViewById(R.id.in_date);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        EditText autoD8 = (EditText)findViewById(R.id.in_date);
        EditText autoTime = (EditText)findViewById(R.id.in_time);

        SimpleDateFormat dateF = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String date = dateF.format(Calendar.getInstance().getTime());
        String time = timeF.format(Calendar.getInstance().getTime());
        autoD8.setText(date);
        autoTime.setText(time);
        spinner = (Spinner) findViewById(R.id.spinner);

        List<String> list = new ArrayList<String>();
        list.add("Please Select the County");
        list.add("Kilifi County");
        list.add("Nairobi County");
        list.add("Mombasa County");
        list.add("Kiambu County");
        list.add("Baringo County");
        list.add("Bomet County");
        list.add("Busia County");
        list.add("Elgeyo Marakwet County");
        list.add("Embu County");
        list.add("Garissa County");
        list.add("Homa Bay County");
        list.add("Isiolo County");
        list.add("Kajiado County");
        list.add("Kakamega County");
        list.add("Kericho County");
        list.add("Kirinyaga County");
        list.add("Kisii County");
        list.add("Kisumu County");
        list.add("Kitui County");
        list.add("Kwale County");
        list.add("Laikipia County");
        list.add("Lamu County");
        list.add("Machakos County");
        list.add("Makueni County");
        list.add("Mandera County");
        list.add("Meru County");
        list.add("Migori County");
        list.add("Marsabit County");
        list.add("Muranga County");
        list.add("Nakuru County");
        list.add("Nandi County");
        list.add("Narok County");
        list.add("Nyamira County");
        list.add("Nyandarua County");
        list.add("Nyeri County");
        list.add("Samburu County");
        list.add("Siaya County");
        list.add("Taita Taveta County");
        list.add("Tana River County");
        list.add("Tharaka Nithi County");
        list.add("Trans Nzoia County");
        list.add("Uasin Gishu County");
        list.add("Vihiga County");
        list.add("Wajir County");
        list.add("West Pokot County");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
        // Assigning ID's to Button.
        InsertButton = (Button) findViewById(R.id.ButtonInsert);

        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(AttackActivity.this);

        progressDialog = new ProgressDialog(AttackActivity.this);

        // Adding click listener to button.
        InsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Showing progress dialog at user registration time.
                progressDialog.setMessage("Please Wait, Data Being Reported to the Relevant Authority");
                progressDialog.show();

                // Calling method to get value from EditText.
                GetValueFromEditText();

                // Creating string request with post method.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String ServerResponse) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                // Showing response message coming from server.
                                Toast.makeText(AttackActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), AttackImageActivity.class);
                                startActivity(i);
                                AttackActivity.this.overridePendingTransition(R.anim.push_left_in,
                                        R.anim.push_left_out);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                // Showing error message if something goes wrong.
                                Toast.makeText(AttackActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {

                        // Creating Map String Params.
                        Map<String, String> params = new HashMap<String, String>();

                        // Adding All values to Params.
                        params.put("first_name", FirstNameHolder);
                        params.put("last_name", LastNameHolder);
                        params.put("email", EmailHolder);
                        params.put("county", SpinnerHolder);
                        params.put("attacktype", AttackTypeHolder);
                        params.put("attacktime", TimeHolder);
                        params.put("contact", ContactHolder);


                        return params;
                    }

                };

                // Creating RequestQueue.
                RequestQueue requestQueue = Volley.newRequestQueue(AttackActivity.this);

                // Adding the StringRequest object into requestQueue.
                requestQueue.add(stringRequest);

            }
        });

    }

    // Creating method to get value from EditText.
    public void GetValueFromEditText(){
        FirstNameHolder = FirstName.getText().toString().trim();
        if(TextUtils.isEmpty(FirstNameHolder)) {
            FirstName.setError("Please Fill in the Accident Description Field.");
            return;
        }
        LastNameHolder = LastName.getText().toString().trim();
        if(TextUtils.isEmpty(LastNameHolder)) {
            LastName.setError("Please Select Location.");
            return;
        }
        EmailHolder = Email.getText().toString().trim();
        if(TextUtils.isEmpty( EmailHolder)) {
            Email.setError("Please Enter Email Address.");
            return;
        }
        AttackTypeHolder= Attacktype.getText().toString().trim();
        if(TextUtils.isEmpty( AttackTypeHolder)) {
            Attacktype.setError("Please Enter Accident Type.");
            return;
        }
        TimeHolder= Date.getText().toString().trim();
        if(TextUtils.isEmpty( TimeHolder)) {
            Date.setError("Please Select Date.");
            return;
        }
        ContactHolder= Contact.getText().toString().trim();
        if(TextUtils.isEmpty( ContactHolder)) {
            Contact.setError("Please Enter Contact.");
            return;
        }
        SpinnerHolder =spinner.getSelectedItem().toString().trim();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.attack, menu);
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
            Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(i);
            AttackActivity.this.overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
        }
        if (id == R.id.action_chat) {

        }
        if (id == R.id.action_image) {
            Intent i = new Intent(getApplicationContext(),UploadImageActivity.class);
            startActivity(i);
            AttackActivity.this.overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);

        }
        if (id == R.id.action_location) {

        }
        if (id == R.id.action_Contacts) {

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        if (v == txtDate) {

// Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == txtTime) {

// Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AttackActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onLocationChanged(Location location) {
        LastName = (EditText) findViewById(R.id.editTextLastName);
        LastName.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub
        String selected = parent.getItemAtPosition(position).toString();
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        ((TextView) parent.getChildAt(0)).setTextSize(20);

        if (selected.equals("Please Select Your County")) {

            Toast.makeText(parent.getContext(), "Please Select Your County", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

}
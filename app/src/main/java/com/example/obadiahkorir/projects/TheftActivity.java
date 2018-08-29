package com.example.obadiahkorir.projects;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public class TheftActivity extends AppCompatActivity  implements
        View.OnClickListener  {
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Toolbar toolbar;
    // Creating EditText.
    EditText FirstName, LastName, Email,County,Thefttype,Contact,Date ;
    // Creating button;
    Button InsertButton;
    // Creating Volley RequestQueue.
    RequestQueue requestQueue;
    // Create string variable to hold the EditText Value.
    String FirstNameHolder, LastNameHolder, EmailHolder,CountyHolder,TheftTypeHolder,TimeHolder,ContactHolder ;
    // Creating Progress dialog.
    ProgressDialog progressDialog;
    // Storing server url into String variable.
    String HttpUrl = "https://chemisoftsolutions.000webhostapp.com/android/theft_insert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_theft);
        toolbar = (Toolbar) findViewById(R.id.back);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setTitle("Report Theft Incident.");
        // Assigning ID's to EditText.
        FirstName = (EditText) findViewById(R.id.editTextFirstName);
        LastName = (EditText) findViewById(R.id.editTextLastName);
        Email = (EditText) findViewById(R.id.editTextEmail);
        County=(EditText) findViewById(R.id.county);
        Contact=(EditText) findViewById(R.id.contact);
        Thefttype =(EditText) findViewById(R.id.thefttype);
        Date=(EditText)findViewById(R.id.in_date);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        EditText autoD8 = (EditText)findViewById(R.id.in_date);
        EditText autoTime = (EditText)findViewById(R.id.in_time);

        SimpleDateFormat dateF = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String date = dateF.format(Calendar.getInstance().getTime());
        String time = timeF.format(Calendar.getInstance().getTime());
        autoD8.setText(date);
        autoTime.setText(time);

        // Assigning ID's to Button.
        InsertButton = (Button) findViewById(R.id.ButtonInsert);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
                TheftActivity.this.overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });
        // Creating Volley newRequestQueue .
        requestQueue = Volley.newRequestQueue(TheftActivity.this);

        progressDialog = new ProgressDialog(TheftActivity.this);

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
                                Toast.makeText(TheftActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                // Showing error message if something goes wrong.
                                Toast.makeText(TheftActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
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
                        params.put("county", CountyHolder);
                        params.put("thefttype", TheftTypeHolder);
                        params.put("thefttime", TimeHolder);
                        params.put("contact", ContactHolder);


                        return params;
                    }

                };

                // Creating RequestQueue.
                RequestQueue requestQueue = Volley.newRequestQueue(TheftActivity.this);

                // Adding the StringRequest object into requestQueue.
                requestQueue.add(stringRequest);

            }
        });

    }

    // Creating method to get value from EditText.
    public void GetValueFromEditText(){
        FirstNameHolder = FirstName.getText().toString().trim();
        if(TextUtils.isEmpty(FirstNameHolder)) {
            FirstName.setError("Please Fill in the Theft Description.");
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
        CountyHolder = County.getText().toString().trim();
        if(TextUtils.isEmpty(CountyHolder)) {
            County.setError("Please Enter County.");
            return;
        }
        TheftTypeHolder =  Thefttype.getText().toString().trim();
        if(TextUtils.isEmpty( TheftTypeHolder)) {
            Thefttype.setError("Please Enter Theft Type.");
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

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            TheftActivity.this.overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
        }
        if (id == R.id.action_chat) {

        }
        if (id == R.id.action_image) {

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
                        TheftActivity.this.finish();
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
}
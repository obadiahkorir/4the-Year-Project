package com.example.obadiahkorir.projects;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ActivityDecide extends AppCompatActivity {
    private ImageButton yes,no;
    private LocationManager locationMangaer=null;
    private LocationListener locationListener=null;

    private Button btnGetLocation = null;
    private EditText editLocation = null;

    //private ProgressBar pb =null;

    private static final String TAG = "Debug";
    private Boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide);
        yes = (ImageButton) findViewById(R.id.btn_yes);
        no = (ImageButton) findViewById(R.id.btn_no);
  /*      no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                ActivityDecide.this.overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });
  */
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActivitySpecial.class);
                startActivity(i);
                ActivityDecide.this.overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });

        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        no.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                flag = displayGpsStatus();
                if (flag) {

                    Log.v(TAG, "onClick");



                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    ActivityDecide.this.overridePendingTransition(R.anim.push_left_in,
                            R.anim.push_left_out);

                } else {
                    alertbox("Gps Status!!", "Your GPS is: OFF");
                }

            }
        });
    }

    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_SECURITY_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }



    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }


}

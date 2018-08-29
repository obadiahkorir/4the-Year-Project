package com.example.obadiahkorir.projects;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PremiumContent extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listViewprem;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_content);


        listViewprem = (ListView) findViewById(R.id.premium);
        listViewprem.setOnItemClickListener(this);




    }

    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                Toast.LENGTH_SHORT).show();

        switch (position) {
            case 0:

                Toast.makeText(getApplicationContext(), "welcome......",
                        Toast.LENGTH_SHORT).show();
                break;

            case 1:

                Toast.makeText(getApplicationContext(), "welcome......",
                        Toast.LENGTH_SHORT).show();
                break;
            case 2:

                Toast.makeText(getApplicationContext(), "welcome......",
                        Toast.LENGTH_SHORT).show();
                break;
            case 3:


                break;
            case 4:
                Toast.makeText(getApplicationContext(), "welcome......",
                        Toast.LENGTH_SHORT).show();

                break;
            case 5:
                Toast.makeText(getApplicationContext(), "welcome......",
                        Toast.LENGTH_SHORT).show();

                break;
            case 6:
                Toast.makeText(getApplicationContext(), "welcome......",
                        Toast.LENGTH_SHORT).show();

                break;

        }


    }






}

package com.example.obadiahkorir.projects;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class NextofKinActivity extends AppCompatActivity {
    String FileName ="myFile";
    Button BtnSave, BtnRead,BtnCall;
    EditText editName;
    TextView readname;
    String name;

    public EditText phone1,phone2;
    public Button btn_login;
    private FloatingActionMenu fam;
    private FloatingActionButton fabEdit, fabDelete, fabAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nextofkin);
        //Getting the edittext and button instance

        BtnSave = (Button) findViewById(R.id.save);
        BtnRead = (Button) findViewById(R.id.call);
        BtnCall = (Button) findViewById(R.id.calls);
        phone1 = (EditText) findViewById(R.id.numb1);
        phone2 = (EditText) findViewById(R.id.numb2);
        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile();
            }

        });
        BtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFile();
            }

        });
        editName =(EditText)findViewById(R.id.numb1);
        readname =(TextView) findViewById(R.id.readno);
        //Performing action on button click
        BtnCall.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                String number=readname.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+number));
                startActivity(callIntent);
            }

        });
        fabAdd = (FloatingActionButton) findViewById(R.id.fab2);
        fabDelete = (FloatingActionButton) findViewById(R.id.fab3);
        fabEdit = (FloatingActionButton) findViewById(R.id.fab1);
        fam = (FloatingActionMenu) findViewById(R.id.fab_menu);

        //handling menu status (open or close)
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    showToast("Menu is opened");
                } else {
                    showToast("Menu is closed");
                }
            }
        });

        //handling each floating action button clicked
        fabDelete.setOnClickListener(onButtonClick());
        fabEdit.setOnClickListener(onButtonClick());
        fabAdd.setOnClickListener(onButtonClick());

        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });
    }
    private void readFile(){
        SharedPreferences sharedPref = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String defaultValue ="DefaultName";
        String name = sharedPref.getString("name",defaultValue);
        readname.setText(name);
        Toast.makeText(this,"Data:"+name,Toast.LENGTH_SHORT).show();
    }
    private void saveFile(){
        String strName =editName.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name",strName);
        editor.commit();
        Toast.makeText(this,"Data Saved Successfully",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NextofKinActivity.this.finish();
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
    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == fabAdd) {
                    showToast("Button Add clicked");
                } else if (view == fabDelete) {
                    showToast("Button Delete clicked");
                } else {
                    showToast("Button Edit clicked");
                }
                fam.close(true);
            }
        };
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}

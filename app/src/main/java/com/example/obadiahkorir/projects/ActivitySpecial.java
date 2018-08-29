package com.example.obadiahkorir.projects;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;


public class ActivitySpecial extends AppCompatActivity {
    MediaPlayer mp;
    TextToSpeech t1;
    TextView text_output;
    ImageView btnSpeak,btncall;
    public final int SPEECH_RECOGNITION_CODE = 1;
    Context context = this;
    Button Startaistuff;
    SharedPreferences mUltiChatSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_needs);
        mUltiChatSettings = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        text_output = (TextView)findViewById(R.id.txt_output);
        btnSpeak = (ImageView) findViewById(R.id.btnSpeak);
        btncall = (ImageView) findViewById(R.id.btnCall);
        Startaistuff =(Button)findViewById(R.id.panic);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        Startaistuff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),ActivitySpecial.class);
                startActivity(i);
                ActivitySpecial.this.overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
            }
        });
        btncall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                initializeCall();
            }
        });
        welcomeSound();
    }

    public void welcomeSound() {

        String toSpeak = getResources().getString(R.string.intro);
        Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

    }

    public void onPause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    text_output.setText(text);
                }
                break;
            }
        }
    }
    public void initializeCall() {

        String textSound = text_output.getText().toString();
        textSound = textSound.trim();
        textSound = textSound.replace(" ", "");
        if (textSound != null) {
            switch (textSound) {
                case "police":
                    String policenumber = "0704202424";
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + Uri.encode(policenumber)));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling

                        return;
                    }
                    startActivity(callIntent);
                    break;
                case "ambulance":
                    String ambulance = "0706727927";
                    Intent callIntent2 = new Intent(Intent.ACTION_CALL);
                    callIntent2.setData(Uri.parse("tel:" + Uri.encode(ambulance)));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    startActivity(callIntent2);
                    break;
                case "childabuse":
                    String childabuse = "0704202424";
                    Intent callIntent3 = new Intent(Intent.ACTION_CALL);
                    callIntent3.setData(Uri.parse("tel:" + Uri.encode(childabuse)));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent3);
                    break;
                case "fire":
                    String fire = "0714988589";
                    Intent callIntent4 = new Intent(Intent.ACTION_CALL);
                    callIntent4.setData(Uri.parse("tel:" + Uri.encode(fire)));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent4);
                    break;
                default: //Oh no, it's working day
                    //This code is executed when value of variable 'day'
                    //doesn't match with any of case above
                    break;
            }


        } else {

            Intent i = new Intent(getApplicationContext(),ActivitySpecial.class);
            startActivity(i);
            ActivitySpecial.this.overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);

            return;
        }



    }

}



package com.example.obadiahkorir.projects;

/**
 * Created by Obadiah Korir on 10/20/2018.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
public class StartActivity extends AppCompatActivity {
    private TextView tv;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        iv=(ImageView)findViewById(R.id.emergency);
        tv=(TextView)findViewById(R.id.tv);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        final Intent i=new Intent(this,StartActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(5000);

                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent i = new Intent(StartActivity.this, WelcomeActivity.class);

                    startActivity(i);

                }
            }
        };
        timer.start();
    }
}
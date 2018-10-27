package com.example.obadiahkorir.projects;

/**
 * Created by Obadiah Korir on 10/20/2018.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
public class LearningActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView camera,first_aid,chat,self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polic);
        camera = (CardView)findViewById(R.id.camera);
        chat = (CardView)findViewById(R.id.chat);
        self = (CardView)findViewById(R.id.self);
        first_aid = (CardView)findViewById(R.id.first_aid);
        camera.setOnClickListener(this);
        chat.setOnClickListener(this);
        self.setOnClickListener(this);
        first_aid.setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        Intent i;
        switch (v.getId()) {
            case R.id.camera:i = (new Intent(this,camera.class));startActivity(i);break;
            case R.id.chat:i = (new Intent(this,chat.class));startActivity(i);break;
            case R.id.first_aid:i = (new Intent(this,first_aid.class));startActivity(i);break;
            case R.id.self:i = (new Intent(this,self.class));startActivity(i);break;
            default:break;



        }
    }
}

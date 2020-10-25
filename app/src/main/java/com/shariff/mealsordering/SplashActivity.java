package com.shariff.mealsordering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    Animation left;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        left = AnimationUtils.loadAnimation(this,R.anim.left_animation);
       //topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        //bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        img = (ImageView) findViewById(R.id.img);
        //sl1 = (TextView)findViewById(R.id.t1);
        //sl2 = (TextView)findViewById(R.id.t2);
        img.setAnimation(left);
        //l1.setAnimation(bottomAnim);
        //sl2.setAnimation(bottomAnim);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(getApplicationContext(),LoginAcitivty.class);
                startActivity(in);
            }
        }, 3000);
    }
}
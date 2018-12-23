package com.lintend.forum.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.lintend.forum.R;
import com.lintend.forum.loginStuffs.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    int Sleep_time = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
      LogoLauncher  logoLauncher = new LogoLauncher();
      logoLauncher.start();

    }

    private class LogoLauncher extends  Thread{
        public void run(){

            try {
                sleep(1000*Sleep_time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(SplashScreenActivity.this,LoginActivity.class);
            startActivity(i);
            SplashScreenActivity.this.finish();

        }
    }
}

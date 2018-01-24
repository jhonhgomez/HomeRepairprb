package com.electiva.juan.homerepair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Juan on 28/11/2015.
 */


public class Splash extends Activity {

    boolean spActive;
    boolean spPaused;
    long spTime = 3000;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spPaused = false;
        spActive = true;

        Thread splashTimer = new Thread() {
            public void run() {
                try {
                    long ms = 0;
                    while (spActive && ms < spTime) {
                        sleep(100);
                        if (!spPaused)
                            ms += 100;
                    }
                    startActivity(new Intent

                            ("com.google.app.jhpi.CLEARSPLASH"));
                    finish();
                } catch (Exception e) {
                    //Thread exception
                    System.out.println(e.toString());
                }
            }
        };
        splashTimer.start();
        setContentView(R.layout.activity_splash);
        return;
    }
}

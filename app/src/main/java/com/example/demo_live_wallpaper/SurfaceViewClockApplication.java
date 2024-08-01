package com.example.demo_live_wallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SurfaceViewClockApplication extends AppCompatActivity {

    MySurface mySurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view_clock_application);

        mySurface = new MySurface(getApplicationContext());
        setContentView(mySurface);

        try {
            mySurface.analogClock.vyberZoSharedPreferences("priZastaveni");
        } catch (Exception e) {
            mySurface.analogClock.nastavUvodneHodnoty();
            e.printStackTrace();
        }

    }
}
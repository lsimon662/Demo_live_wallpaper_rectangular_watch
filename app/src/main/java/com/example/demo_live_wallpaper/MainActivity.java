package com.example.demo_live_wallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;


import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    public static final String YOUTUBE_ID = "JrqN83cVHRU";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, ClockWallpaperService.class));
        startActivity(intent);

         */

        setContentView(R.layout.main);

    }

    public void setClockLw(View v) {
        Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, ClockWallpaperService.class));
        startActivity(intent);
    }

    /*public void showApplication(View v) {
        Intent akcia = new Intent(this, ClockApplication.class);
        *//*EditText mojaSprava = (EditText) findViewById(R.id.edt_Msg);
        String spr = mojaSprava.getText().toString();

        akcia.putExtra("SPRAVA", spr);*//*

        startActivity(akcia);
    }*/

    public void showApplication(View view) {

        Intent akcia = new Intent(this, ClockApplication.class);

        startActivity(akcia);

    }

    public void surfaceViewShowApplication(View view) {

        Intent akcia = new Intent(this, SurfaceViewClockApplication.class);

        startActivity(akcia);

    }

    public void exit(View v) {
        System.exit(0);
    }
}
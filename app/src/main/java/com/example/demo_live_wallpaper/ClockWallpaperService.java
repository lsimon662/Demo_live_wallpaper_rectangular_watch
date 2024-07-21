package com.example.demo_live_wallpaper;


import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

/**
 * Clock wallpaper service
 *
 * @author sylsau - sylvain.saurel@gmail.com - http://www.ssaurel.com
 *
 */
public class ClockWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new ClockWallpaperEngine();
    }

    private class ClockWallpaperEngine extends Engine implements
            OnSharedPreferenceChangeListener {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private Paint paint;
        /** hands colors for hour, min, sec */
        private int[] colors = { 0xFFFF0000, 0xFF0000FF, 0xFFA2BC13 };
        private int bgColor;
        private int width;
        private int height;
        private boolean visible = true;
        private boolean displayHandSec;
        private AnalogClock clock;
        private SharedPreferences prefs;
        // Canvas platno;
        // kontrola git

        public ClockWallpaperEngine() {
            prefs = PreferenceManager
                    .getDefaultSharedPreferences(ClockWallpaperService.this);
            prefs.registerOnSharedPreferenceChangeListener(this);
            displayHandSec = prefs.getBoolean(SettingsActivity.DISPLAY_HAND_SEC_KEY, true);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            // bgColor = Color.parseColor("#C0C0C0");

            clock = new AnalogClock(getApplicationContext());
            // bgColor = clock.farbaPozadia;
            // clock.nastavUvodneHodnoty();
            // "MojeUserPrefs"
            SharedPreferences sp = getSharedPreferences("MojeUserPrefs", Context.MODE_PRIVATE);
            if(sp != null)
                clock.vyberZoSharedPreferences("MojeUserPrefs");
            bgColor = clock.farbaPozadia;

            /*clock.inicializujHodiny();
            clock.kresliBody(platno, 0,0, clock.farbaZnaciek);
            if(clock.b_arabic_numbers)
                clock.kresliCisla(platno, clock.cisla_arabske, 0,0, clock.farbaCisel);
            clock.kresliRucicky(platno);*/

            handler.post(drawRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
            prefs.unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    draw(canvas);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            handler.removeCallbacks(drawRunner);

            if (visible) {
                handler.postDelayed(drawRunner, 5);
            }
        }

        private void draw(Canvas canvas) {
            canvas.drawColor(bgColor);
//            clock.config(width / 2, height / 2, (int) (width * 0.6f),
//                    new Date(), paint, colors, displayHandSec);

            clock.config(width, height,
                    new Date());

            // clock.inicializujHodiny();
            clock.draw(canvas);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
        {
            if (SettingsActivity.DISPLAY_HAND_SEC_KEY.equals(key)) {
                displayHandSec = sharedPreferences.getBoolean(
                        SettingsActivity.DISPLAY_HAND_SEC_KEY, true);
            }
        }
    }
}

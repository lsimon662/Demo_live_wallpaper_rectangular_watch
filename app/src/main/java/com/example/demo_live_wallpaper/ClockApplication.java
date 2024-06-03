package com.example.demo_live_wallpaper;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.Date;

public class ClockApplication extends AppCompatActivity {

    private Paint paint;
    // private int[] colors = { 0xFFFF0000, 0xFF0000FF, 0xFFA2BC13 };
    // private int bgColor;
    private int width;
    private int height;
    private boolean visible = true;
    // private boolean displayHandSec;
    private AnalogClock analogClock;
    // private SharedPreferences prefs;

    Handler h;
    int max = 44;
    int barva;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_application);

        analogClock = (AnalogClock) findViewById(R.id.analogClock);

        // analogClock.nastavUvodneHodnoty();

        // analogClock = new AnalogClock(getApplicationContext());

      /*  width = analogClock.getWidth();
        height = analogClock.getHeight();*/

        // c = (Canvas) hodiny.platno;

        new Thread(new Task()).start();

        h = new Handler(Looper.getMainLooper());

        barva = ResourcesCompat.getColor(getResources(), R.color.your_color, null);
        System.out.println("***  ***  *** barva " + barva + " width " + analogClock.getWidth() + " height " + analogClock.getHeight());

    }

    public void startProgress(View v) {

        new Thread(new Task()).start();
    }

    public void exit(View v) {
        System.exit(0);
    }

    class Task implements Runnable {

        @Override
        public void run() {
           /* for (int i = 0; i <= max; i++) {
                final int value = i; */
            while (true) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                analogClock.inicializujHodiny();
                analogClock.invalidate();
            }
           //  }
        }
    }
}
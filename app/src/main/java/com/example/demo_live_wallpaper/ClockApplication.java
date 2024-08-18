package com.example.demo_live_wallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuCompat;

import java.util.Date;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ClockApplication extends AppCompatActivity {

    androidx.appcompat.app.AlertDialog.Builder zostava;

    private boolean displayHandSec = true;
    private AnalogClock analogClock;
    View decorView;
    int uiOptions;

    // Handler h;
    int barva, mojaUvodnaFarba;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        decorView = getWindow().getDecorView();

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        // getSupportActionBar().setHideOnContentScrollEnabled(false);
        // getSupportActionBar().setShowHideAnimationEnabled(false);
        // getSupportActionBar().hide();
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // nevypinaj screen
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if(Build.VERSION.SDK_INT >= 19) {
            // Toast.makeText(this, "SDK >= 19", Toast.LENGTH_SHORT).show();
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        // getWindow().setStatusBarColor(R.color.purple_700);

        // getSupportActionBar().hide();

        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_clock_application);

        analogClock = (AnalogClock) findViewById(R.id.analogClock);

        try {
            analogClock.vyberZoSharedPreferences("MojeUserPrefs");
        } catch (Exception e) {
            analogClock.nastavUvodneHodnoty();
            e.printStackTrace();
        }

        new Thread(new Task()).start();

        // thread = Thread.currentThread();

        // h = new Handler(Looper.getMainLooper());

        barva = ResourcesCompat.getColor(getResources(), R.color.your_color, null);
        // System.out.println("*** ClockApplication  *** onCreate *** barva " + barva + " width " + analogClock.getWidth() + " height " + analogClock.getHeight());

    }

    public void exit(View v) {
        System.exit(0);
    }

    class Task implements Runnable {

        @Override
        public void run() {
         while (true) {
                 try {
                    Thread.sleep(100);
                    // System.out.println(" thread " + thread);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                analogClock.config(analogClock.getWidth(), analogClock.getHeight(), new Date());
                 // analogClock.inicializujHodiny();
                 // analogClock.invalidate();
                analogClock.postInvalidateDelayed(50);

        //    }
             }
         }
    }

    private void setWindowFlag(int i, boolean b) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if(b) {
            winParams.flags |= i;
        } else
            winParams.flags &= ~i;
        {
            win.setAttributes(winParams);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Toast.makeText(this, "Dotyk obrazovky", Toast.LENGTH_SHORT).show();
        if(!getSupportActionBar().isShowing() && event.getAction() == MotionEvent.ACTION_DOWN) {
            // if(uiOptions > 0  && event.getDownTime() < 7000000) {
            // Toast.makeText(this, "Dotyk obrazovky ukaz status bar " + event.toString(), Toast.LENGTH_SHORT).show();
            // getSupportActionBar().show();
            // getWindow().setStatusBarColor(farbaPozadia);
            decorView.setSystemUiVisibility(0);
            event.setAction(MotionEvent.ACTION_UP);
        }
        else {
            // decorView.setSystemUiVisibility(uiOptions);
            if(getSupportActionBar().isShowing() && event.getAction() == MotionEvent.ACTION_DOWN) {
                // Toast.makeText(this, "Dotyk obrazovky skry status bar", Toast.LENGTH_SHORT).show();
                decorView = getWindow().getDecorView();
                // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0x00000000);
                // int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                // getSupportActionBar().hide();
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
        return false;
        // return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);

        nastavMenu(menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.m_background).getIcon().setTint(analogClock.farbaPozadia);
        menu.findItem(R.id.m_background_color_gradient).getIcon().setTint(analogClock.farbaPozadiaGradient);

        menu.findItem(R.id.m_hands).getIcon().setTint(analogClock.farbaRuciciek);
        menu.findItem(R.id.m_shift_hands).getIcon().setTint(analogClock.farbaVysuvnikaRuciciek);

        menu.findItem(R.id.inner_hands).getIcon().setTint(analogClock.farbaVnutraRuciciek);
        menu.findItem(R.id.inner_seconds_hand).getIcon().setTint(analogClock.farbaVnutraSekundovejRucicky);

        menu.findItem(R.id.hand_shift_seconds_color).getIcon().setTint(analogClock.farbaVysuvnikaSekundovejRucicky);  //   24.4.2024

        // menu.findItem(R.id.m_margin).getIcon().setTint(farbaObvodovejCiary);

        menu.findItem(R.id.m_marks).getIcon().setTint(analogClock.farbaZnaciek);
        menu.findItem(R.id.numbers).getIcon().setTint(analogClock.farbaCisel);
        menu.findItem(R.id.contour_color).getIcon().setTint(analogClock.farbaObvodovejCiary);
        menu.findItem(R.id.inner_contour_color).getIcon().setTint(analogClock.farbaVnutraObvodovejCiary);

        menu.findItem(R.id.inner_contour_color_gradient).getIcon().setTint(analogClock.farbaVnutraObvodovejCiaryGradient);

        menu.findItem(R.id.shadow_color).getIcon().setTint(analogClock.farbaTiena);
        menu.findItem(R.id.hand_seconds_color).getIcon().setTint(analogClock.farbaSekundovejRucicky);

        menu.findItem(R.id.inner_roud_color).getIcon().setTint(analogClock.farbaOzdobnehoKruhu);
        menu.findItem(R.id.beam_color).getIcon().setTint(analogClock.farbaOzdobnychLucov);

        nastavMenu(menu);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    public void nastavMenu(Menu menu) {

        if (analogClock.b_top) {
            menu.findItem(R.id.top).setChecked(true);
            // menu.findItem(R.id.rc_distance).setChecked(false);
            // menu.findItem(R.id.rc_angle).setChecked(false);
        }

        if (analogClock.b_middle) {
            menu.findItem(R.id.middle).setChecked(true);
            // menu.findItem(R.id.rc_distance).setChecked(false);
            // menu.findItem(R.id.rc_angle).setChecked(false);
        }

        if (analogClock.b_bottom) {
            menu.findItem(R.id.bottom).setChecked(true);
            // menu.findItem(R.id.rc_distance).setChecked(false);
            // menu.findItem(R.id.rc_angle).setChecked(false);
        }

        if (analogClock.h_round) {
            menu.findItem(R.id.round).setChecked(true);
            // menu.findItem(R.id.rc_distance).setChecked(false);
            // menu.findItem(R.id.rc_angle).setChecked(false);
        }

        if (analogClock.h_rect_cd) {
            menu.findItem(R.id.rc_distance).setChecked(true);
            // menu.findItem(R.id.rc_angle).setChecked(false);
            // menu.findItem(R.id.round).setChecked(false);
        }

        if (analogClock.h_rect_ca) {
            menu.findItem(R.id.rc_angle).setChecked(true);
            // menu.findItem(R.id.round).setChecked(false);
            // menu.findItem(R.id.rc_distance).setChecked(false);
        }

        if (analogClock.h_shadow)
            menu.findItem(R.id.shadow).setChecked(true);
        else
            menu.findItem(R.id.shadow).setChecked(false);

        if(analogClock.h_line_constant_length) {
            menu.findItem(R.id.constant_length_line).setChecked(true);
            // menu.findItem(R.id.mechanic).setChecked(false);
        }

        if(analogClock.h_mechanic) {
            menu.findItem(R.id.mechanic).setChecked(true);
            // menu.findItem(R.id.line).setChecked(false);
        }

        if(analogClock.h_mechanic_full) {
            menu.findItem(R.id.mechanic_full).setChecked(true);
            // menu.findItem(R.id.line).setChecked(false);
        }

        // chod ruciciek
        if(analogClock.h_continouos) {
            menu.findItem(R.id.continouos).setChecked(true);
            // menu.findItem(R.id.step).setChecked(false);
        }

        if(analogClock.h_step) {
            menu.findItem(R.id.step).setChecked(true);
            // menu.findItem(R.id.continouos).setChecked(false);
        }

        if(analogClock.h_background_gradient) {
            menu.findItem(R.id.m_background_gradient).setChecked(true);
        }

        if(analogClock.h_inner_contour_gradient) {
            menu.findItem(R.id.inner_contour_gradient).setChecked(true);
        }

        // tvar bodov po obvode
        if(analogClock.b_lines) {
            menu.findItem(R.id.m_lines).setChecked(true);
            // menu.findItem(R.id.m_circles).setChecked(false);
        }

        if(analogClock.b_circles) {
            menu.findItem(R.id.m_circles).setChecked(true);
            // menu.findItem(R.id.m_lines).setChecked(false);
        }

        if(analogClock.kazdaPiata)
            menu.findItem(R.id.everyFifth).setChecked(true);
        else
            menu.findItem(R.id.everyFifth).setChecked(false);

        // cisla po obvode
        if (analogClock.b_arabic_numbers) {
            menu.findItem(R.id.arabic_numbers).setChecked(true);
            // menu.findItem(R.id.roman_numbers).setChecked(false);
            // menu.findItem(R.id.none_numbers).setChecked(false);
        }

        if (analogClock.b_roman_numbers) {
            menu.findItem(R.id.roman_numbers).setChecked(true);
            // menu.findItem(R.id.arabic_numbers).setChecked(false);
            // menu.findItem(R.id.none_numbers).setChecked(false);
        }

        if (analogClock.b_none) {
            menu.findItem(R.id.none_numbers).setChecked(true);
            // menu.findItem(R.id.arabic_numbers).setChecked(false);
            // menu.findItem(R.id.roman_numbers).setChecked(false);
        }

        // pomocne ciary
        if (analogClock.h_inner_round)
            menu.findItem(R.id.inner_round).setChecked(true);
        else
            menu.findItem(R.id.inner_round).setChecked(false);

        if (analogClock.h_beam)
            menu.findItem(R.id.beam).setChecked(true);
        else
            menu.findItem(R.id.beam).setChecked(false);

        if (analogClock.h_contour)
            menu.findItem(R.id.contour).setChecked(true);
        else
            menu.findItem(R.id.contour).setChecked(false);

        if (analogClock.h_inner_contour)
            menu.findItem(R.id.inner_contour).setChecked(true);
        else
            menu.findItem(R.id.inner_contour).setChecked(false);

        if (analogClock.h_angle)
            menu.findItem(R.id.angle).setChecked(true);
        else
            menu.findItem(R.id.angle).setChecked(false);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {

            // **************************
            case R.id.top:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.b_middle = false;
                    analogClock.b_bottom = false;
                    // b_item2 = true;
                    // AlertDialog.Builder bld = new AlertDialog.Builder(getContext());
                }
                analogClock.b_top = item.isChecked();
                // Toast.makeText(this, "Numbers Arabic selected", Toast.LENGTH_SHORT).show();
                // return true;
                break;

            case R.id.middle:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.b_top = false;
                    analogClock.b_bottom = false;
                }
                analogClock.b_middle = item.isChecked();
                // return true;
                break;

            case R.id.bottom:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.b_top = false;
                    analogClock.b_middle = false;
                }
                analogClock.b_bottom = item.isChecked();
                // Toast.makeText(this, "No Numbers selected", Toast.LENGTH_SHORT).show();
                // return true;
                break;

            // ****************************************

            /* case R.id.m_color:   // bolo item3
                // Toast.makeText(this, "Item 3 selected ", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                break;
                // return true; */

           /* case R.id.line:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.h_mechanic = false;
                    analogClock.h_mechanic_full = false;
                    analogClock.h_line_constant_length = false;
                }
                analogClock.h_line = item.isChecked();
                // Toast.makeText(this, "Hand line ", Toast.LENGTH_SHORT).show();
                // break;
                return true;*/

            case R.id.constant_length_line:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.h_mechanic = false;
                    analogClock.h_mechanic_full = false;
                    // analogClock.h_line = false;
                }
                analogClock.h_line_constant_length = item.isChecked();
                // Toast.makeText(this, "Hand line ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.mechanic:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.h_line_constant_length = false;
                    // analogClock.h_line = false;
                    analogClock.h_mechanic_full = false;
                }
                analogClock.h_mechanic = item.isChecked();
                // Toast.makeText(this, "Hand mechanism ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.mechanic_full:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.h_line_constant_length = false;
                    // analogClock.h_line = false;
                    analogClock.h_mechanic = false;
                }
                analogClock.h_mechanic_full = item.isChecked();
                // Toast.makeText(this, "Hand mechanism ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.continouos:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.h_step = false;
                }
                analogClock.h_continouos = item.isChecked();
                return true;

            case R.id.step:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.h_continouos = false;
                }
                analogClock.h_step = item.isChecked();
                return true;

            case R.id.round:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.h_rect_ca = false;
                    analogClock.h_rect_cd = false;
                }
                analogClock.h_round = item.isChecked();
                // analogClock.h_line = true;
                analogClock.h_line_constant_length = true;
                analogClock.h_mechanic = false;
                analogClock.h_mechanic_full = false;
                // Toast.makeText(this, "Round selected ", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.rc_angle:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.h_round = false;
                    analogClock.h_rect_cd = false;

                }
                analogClock.h_rect_ca = item.isChecked();
                // analogClock.h_line = false;
                analogClock.h_line_constant_length = false;
                analogClock.h_mechanic = true;
                // Toast.makeText(this, "Rectangle constant angle selected ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.rc_distance:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.h_rect_ca = false;
                    analogClock.h_round = false;

                }
                analogClock.h_rect_cd = item.isChecked();
                // analogClock.h_line = false;
                analogClock.h_line_constant_length = false;
                analogClock.h_mechanic = true;
                // Toast.makeText(this, "Rectangle constant distance selected ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.m_lines:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);

                    analogClock.b_circles = false;
                }
                analogClock.b_lines = item.isChecked();
                // Toast.makeText(this, "Marks lines ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.m_circles:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.b_lines = false;

                }
                analogClock.b_circles = item.isChecked();
                // Toast.makeText(this, "Marks circles ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.everyFifth:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                analogClock.kazdaPiata = item.isChecked();

                // Toast.makeText(this, "Shadow selected", Toast.LENGTH_SHORT).show();
                return true;

            // case R.id.m_background_transparent:   // farba pozadia
            // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
            // mojaUvodnaFarba = farbaPozadia;
            // analogClock.farbaPozadia = Color.TRANSPARENT;
            // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
            // otvorVyberFarby('P');
            // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
            // break;

            case R.id.m_background:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = analogClock.farbaPozadia;
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                otvorVyberFarby('P');
                // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
                break;
            // return true;

            case R.id.m_background_gradient:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                analogClock.h_background_gradient = item.isChecked();
                // Toast.makeText(this, "Shadow selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.inner_contour_gradient:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                analogClock.h_inner_contour_gradient = item.isChecked();
                // Toast.makeText(this, "Shadow selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.m_marks:
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                // Toast.makeText(this, "Sub Mark color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = analogClock.farbaZnaciek;
                otvorVyberFarby('Z');
                break;
            // return true;

            case R.id.numbers:
                // Toast.makeText(this, "Sub Numbers color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = analogClock.farbaCisel;
                otvorVyberFarby('C');
                break;

            case R.id.shadow_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = analogClock.farbaTiena;
                otvorVyberFarby('T');
                break;

            case R.id.contour_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = analogClock.farbaObvodovejCiary;
                otvorVyberFarby('O');
                break;

            case R.id.inner_contour_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = analogClock.farbaVnutraObvodovejCiary;
                otvorVyberFarby('I');
                break;

            case R.id.hand_seconds_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = analogClock.farbaSekundovejRucicky;
                otvorVyberFarby('S');
                break;

            case R.id.inner_roud_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = analogClock.farbaOzdobnehoKruhu;
                otvorVyberFarby('K');
                break;

            case R.id.beam_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = analogClock.farbaOzdobnychLucov;
                otvorVyberFarby('B');
                break;

            case R.id.m_hands:     // farba ruciciek
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                // Toast.makeText(this, "Sub Hands color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = analogClock.farbaRuciciek;
                otvorVyberFarby('R');
                break;

            case R.id.inner_hands:
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                // Toast.makeText(this, "Sub Hands color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = analogClock.farbaVnutraRuciciek;
                otvorVyberFarby('V');
                break;

            case R.id.m_background_color_gradient:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = analogClock.farbaPozadiaGradient;
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                otvorVyberFarby('A');
                // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
                break;
            // return true;

            case R.id.inner_contour_color_gradient:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = analogClock.farbaVnutraObvodovejCiaryGradient;
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                otvorVyberFarby('D');
                // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
                break;
            // return true;

            case R.id.m_shift_hands:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = analogClock.farbaVysuvnikaRuciciek;
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                otvorVyberFarby('Y');
                // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
                break;
            // return true;

            case R.id.hand_shift_seconds_color:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = analogClock.farbaVysuvnikaSekundovejRucicky;
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                otvorVyberFarby('F');
                // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
                break;
            // return true;

            case R.id.inner_seconds_hand:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = analogClock.farbaVnutraSekundovejRucicky;
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                otvorVyberFarby('Q');
                // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
                break;
            // return true;

            // return true;
            case R.id.arabic_numbers:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.b_roman_numbers = false;
                    analogClock.b_none = false;
                    // b_item2 = true;
                    // AlertDialog.Builder bld = new AlertDialog.Builder(getContext());
                }
                analogClock.b_arabic_numbers = item.isChecked();
                // Toast.makeText(this, "Numbers Arabic selected", Toast.LENGTH_SHORT).show();
                // return true;
                break;

            case R.id.roman_numbers:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.b_arabic_numbers = false;
                    analogClock.b_none = false;
                }
                analogClock.b_roman_numbers = item.isChecked();
                // return true;
                break;

            case R.id.none_numbers:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    analogClock.b_arabic_numbers = false;
                    analogClock.b_roman_numbers = false;
                }
                analogClock.b_none = item.isChecked();
                // Toast.makeText(this, "No Numbers selected", Toast.LENGTH_SHORT).show();
                // return true;
                break;

            case R.id.shadow:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                analogClock.h_shadow = item.isChecked();

                // Toast.makeText(this, "Shadow selected", Toast.LENGTH_SHORT).show();
                // return true;
                break;

            case R.id.beam:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                analogClock.h_beam = item.isChecked();
                // return true;
                break;

            case R.id.contour:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                analogClock.h_contour = item.isChecked();
                // Toast.makeText(this, "Kresli obrys", Toast.LENGTH_SHORT).show();
                break;
            // return true;

            case R.id.inner_contour:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                analogClock.h_inner_contour = item.isChecked();
                // Toast.makeText(this, "Kresli obrys", Toast.LENGTH_SHORT).show();
                break;
            // return true;

            case R.id.angle:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                analogClock.h_angle = item.isChecked();
                // Toast.makeText(this, "Kresli pomocne ciary", Toast.LENGTH_SHORT).show();
                return true;
            // break;

            case R.id.inner_round:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                analogClock.h_inner_round = item.isChecked();
                // Toast.makeText(this, "Inner Round selected " + h_inner_round, Toast.LENGTH_SHORT).show();
                return true;
            // break;

            case R.id.polomer:
                String polomer_pom = analogClock.mojPolomer;

                zostava = new AlertDialog.Builder(ClockApplication.this);
                zostava.setTitle("Edge radius 10 to 320");
                final EditText vstupPolomeru = new EditText(ClockApplication.this);

                vstupPolomeru.setInputType(InputType.TYPE_CLASS_PHONE);
                vstupPolomeru.setText("" + analogClock.polomer);
//                zostava.setView(vstupHmotnosti);

                LinearLayout linear_po = new LinearLayout(this);

                linear_po.setOrientation(LinearLayout.VERTICAL);

                SeekBar seek_po = new SeekBar(this);
                seek_po.setMin(10);
                seek_po.setMax(320);
                seek_po.setProgress(analogClock.polomer);

                // seek_po.setBackgroundColor(Color.BLUE);
                seek_po.setBottom(140);
                seek_po.setTop(140);
                seek_po.setPadding(80,80,80,80);

                linear_po.addView(vstupPolomeru);
                linear_po.addView(seek_po);

                zostava.setView(linear_po);

                seek_po.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        vstupPolomeru.setText("" + progress);

                        analogClock.mojPolomer = vstupPolomeru.getText().toString();

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });


                zostava.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        analogClock.mojPolomer = vstupPolomeru.getText().toString();   // tu test
                        analogClock.polomer = Integer.parseInt(analogClock.mojPolomer);

                        // Toast.makeText(GraphicActivityThread.this, "Radius : " + mojText, Toast.LENGTH_LONG).show();
                    }
                });

                zostava.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        analogClock.mojPolomer = polomer_pom;
                        dialog.cancel();
                    }
                });
                zostava.show();
                // break;
                return true;

            case R.id.m_margin:

                String margin_pom = analogClock.mojTextOdsad;

                zostava = new AlertDialog.Builder(ClockApplication.this);
                zostava.setTitle("Margin 0 TO 360");
                final EditText vstupOdsadenia = new EditText(ClockApplication.this);

                vstupOdsadenia.setInputType(InputType.TYPE_CLASS_PHONE);
                vstupOdsadenia.setText("" + analogClock.okraj);

                // zostava.setView(vstupOdsadenia);

                LinearLayout linear_vo = new LinearLayout(this);

                linear_vo.setOrientation(LinearLayout.VERTICAL);

                SeekBar seek_vo = new SeekBar(this);
                seek_vo.setMin(0);
                seek_vo.setMax(360);
                seek_vo.setProgress(analogClock.okraj);

                seek_vo.setBottom(120);
                seek_vo.setTop(120);
                seek_vo.setPadding(80,60,60,80);

                linear_vo.addView(vstupOdsadenia);
                linear_vo.addView(seek_vo);

                zostava.setView(linear_vo);

                seek_vo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        vstupOdsadenia.setText("" + progress);

                        analogClock.mojTextOdsad = vstupOdsadenia.getText().toString();

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                zostava.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        analogClock.mojTextOdsad = vstupOdsadenia.getText().toString();
                        // Toast.makeText(ClockApplication.this, "Okraj : " + mojTextOdsad, Toast.LENGTH_LONG).show();
                    }
                });

                zostava.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        analogClock.mojTextOdsad = margin_pom;
                        dialog.cancel();
                    }
                });
                zostava.show();
                break;
            // return true;

            case R.id.zosuvPer:

                String zosuv_per_pom = analogClock.mojTextZosuv; //

                zostava = new AlertDialog.Builder(ClockApplication.this);
                zostava.setTitle("Height [%] of Screen from 40 to 100");
                final EditText vstupZosuvu = new EditText(ClockApplication.this);

                vstupZosuvu.setInputType(InputType.TYPE_CLASS_PHONE);
                vstupZosuvu.setText("" + analogClock.zosuv_per);

                // zostava.setView(vstupZosuvu);

                LinearLayout linear_vz = new LinearLayout(this);

                linear_vz.setOrientation(LinearLayout.VERTICAL);

                SeekBar seek_vz = new SeekBar(this);
                seek_vz.setMin(40);
                seek_vz.setMax(100);
                seek_vz.setProgress(analogClock.zosuv_per);

                seek_vz.setBottom(140);
                seek_vz.setTop(140);
                seek_vz.setPadding(80,80,80,80);

                linear_vz.addView(vstupZosuvu);
                linear_vz.addView(seek_vz);

                zostava.setView(linear_vz);

                seek_vz.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        vstupZosuvu.setText("" + progress);
                        analogClock.mojTextZosuv = vstupZosuvu.getText().toString();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                zostava.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        analogClock.mojTextZosuv = vstupZosuvu.getText().toString();
                        // Toast.makeText(ClockApplication.this, "Zosuv : " + mojTextZosuv, Toast.LENGTH_LONG).show();
                    }
                });

                zostava.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        analogClock.mojTextZosuv = zosuv_per_pom;
                        dialog.cancel();
                    }
                });
                zostava.show();
                break;


            case R.id.hrubkaKontury:

                String mojaHrubkaObvodovejCiary_pom = analogClock.mojaHrubkaObvodovejCiary;

                zostava = new AlertDialog.Builder(ClockApplication.this);
                zostava.setTitle("Contour Line Thickness from 1 to 128");
                final EditText vstupHrubkyKontury = new EditText(ClockApplication.this);

                vstupHrubkyKontury.setInputType(InputType.TYPE_CLASS_PHONE);
                vstupHrubkyKontury.setText("" + analogClock.hrubkaObvodovejCiary);
                // zostava.setView(vstupHrubkyKontury);

                LinearLayout linear_hk = new LinearLayout(this);

                linear_hk.setOrientation(LinearLayout.VERTICAL);

                SeekBar seek_hk = new SeekBar(this);
                seek_hk.setMin(1);
                seek_hk.setMax(128);
                seek_hk.setProgress(analogClock.hrubkaObvodovejCiary);

                seek_hk.setBottom(140);
                seek_hk.setTop(140);
                seek_hk.setPadding(80,80,80,80);

                linear_hk.addView(vstupHrubkyKontury);
                linear_hk.addView(seek_hk);

                zostava.setView(linear_hk);

                seek_hk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        vstupHrubkyKontury.setText("" + progress);

                        analogClock.mojaHrubkaObvodovejCiary = vstupHrubkyKontury.getText().toString();

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                zostava.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        analogClock.mojaHrubkaObvodovejCiary = vstupHrubkyKontury.getText().toString();
                        // Toast.makeText(ClockApplication.this, "Zosuv : " + mojTextZosuv, Toast.LENGTH_LONG).show();
                    }
                });

                zostava.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        analogClock.mojaHrubkaObvodovejCiary = mojaHrubkaObvodovejCiary_pom;
                        dialog.cancel();
                    }
                });
                zostava.show();
                break;

            case R.id.mOdsadenieObvodovejCiary:

                String mojeOdsadenieObvodovejCiary_pom = analogClock.mojeOdsadenieObvodovejCiary;

                zostava = new AlertDialog.Builder(ClockApplication.this);

                zostava.setTitle("Contour Line Distance from -48 to 128");

                final EditText vstupTextOdsadenieObvodovejCiary = new EditText(ClockApplication.this);

                vstupTextOdsadenieObvodovejCiary.setInputType(InputType.TYPE_CLASS_PHONE);
                vstupTextOdsadenieObvodovejCiary.setText("" + analogClock.odsadenieObvodovejCiary);

                LinearLayout linear_oc = new LinearLayout(this);

                linear_oc.setOrientation(LinearLayout.VERTICAL);

                SeekBar seek_oc = new SeekBar(this);
                seek_oc.setMin(-48);
                seek_oc.setMax(128);
                seek_oc.setProgress(analogClock.odsadenieObvodovejCiary);

                seek_oc.setBottom(140);
                seek_oc.setTop(140);
                seek_oc.setPadding(80,80,80,80);

                linear_oc.addView(vstupTextOdsadenieObvodovejCiary);
                linear_oc.addView(seek_oc);

                zostava.setView(linear_oc);

                seek_oc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        vstupTextOdsadenieObvodovejCiary.setText("" + progress);

                        analogClock.mojeOdsadenieObvodovejCiary = vstupTextOdsadenieObvodovejCiary.getText().toString();
                        // findViewById(R.id.pnh).draw(com.example.pravouhlehodiny.PohladNaHodiny.p);

                        // super.kresliPevneCiary(platno, 0,0,farbaPozadia);
                        // new PohladNaHodiny();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                zostava.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        analogClock.mojeOdsadenieObvodovejCiary = vstupTextOdsadenieObvodovejCiary.getText().toString();
                        // Toast.makeText(ClockApplication.this, "Zosuv : " + mojTextZosuv, Toast.LENGTH_LONG).show();
                    }
                });

                zostava.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        analogClock.mojeOdsadenieObvodovejCiary = mojeOdsadenieObvodovejCiary_pom;
                        dialog.cancel();
                    }
                });
                zostava.show();
                break;

            case R.id.m_set_wallpaper:

                // finishActivity(0);
                analogClock.ulozDoSharedPreferences("preWallpaper");

                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(this, ClockWallpaperService.class));

                startActivity(intent);

                // finishAffinity();

                break;

            case R.id.m_default_settings:   // zatial len test z citani hodnot
                // spravne ma byt nastavit menu aj premenne
                analogClock.nastavUvodneHodnoty();

                super.closeOptionsMenu();

                break;

            case R.id.m_save_settings:

                // if (sp != null) {
                // cifernik
                analogClock.ulozDoSharedPreferences("MojeUserPrefs");

                break;

            case R.id.m_restore_settings:

                analogClock.vyberZoSharedPreferences("MojeUserPrefs");
                super.closeOptionsMenu();

                break;

            case R.id.m_whole_screen:

                if(getSupportActionBar().isShowing()) {
                    decorView = getWindow().getDecorView();
                    // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0x00000000);
                    // int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

                    uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

                    // getSupportActionBar().hide();
                    decorView.setSystemUiVisibility(uiOptions);
                }

                break;

            case R.id.m_exit:
                analogClock.ulozDoSharedPreferences("priZastaveni");
                finishAffinity();

                try {
                    super.finalize();
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                System.exit(0);

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

        // return super.onOptionsItemSelected(item);

        return true;
    }


    public void otvorVyberFarby(char co) {

        AmbilWarnaDialog vyberFarby = new AmbilWarnaDialog(this, mojaUvodnaFarba, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mojaUvodnaFarba = color;
                // mojeRozlozenie.setBackgroundColor(mojaUvodnaFarba);
                if (co == 'P') {
                    analogClock.farbaPozadia = color;
                    // farbaPozadia = Color.TRANSPARENT;
                }

                if (co == 'R') {
                    analogClock.farbaRuciciek = color;
                }

                if (co == 'V') {
                    analogClock.farbaVnutraRuciciek = color;
                }

                if (co == 'Z') {
                    analogClock.farbaZnaciek = color;
                }

                if (co == 'C') {
                    analogClock.farbaCisel = color;
                }

                if (co == 'T') {
                    analogClock.farbaTiena = color;
                }

                if (co == 'O') {
                    analogClock.farbaObvodovejCiary = color;
                }

                if (co == 'I') {
                    analogClock.farbaVnutraObvodovejCiary = color;
                }

                if (co == 'S') {
                    analogClock.farbaSekundovejRucicky = color;
                }

                if (co == 'K') {
                    analogClock.farbaOzdobnehoKruhu = color;
                }

                if (co == 'B') {
                    analogClock.farbaOzdobnychLucov = color;
                }

                if (co == 'A') {
                    analogClock.farbaPozadiaGradient = color;
                }

                if (co == 'D') {
                    analogClock.farbaVnutraObvodovejCiaryGradient = color;
                }

                if (co == 'Y') {  //
                    analogClock.farbaVysuvnikaRuciciek = color;
                }

                if (co == 'F') {
                    analogClock.farbaVysuvnikaSekundovejRucicky = color;
                }

                if (co == 'Q') {
                    analogClock.farbaVnutraSekundovejRucicky = color;
                }

            }

        });
        vyberFarby.show();
    }

    /*void nastavUvodneHodnoty() {

        // analogClock.farbaPozadia = Color.rgb(20,50,10);

        analogClock.farbaPozadia = ResourcesCompat.getColor(getResources(), R.color.farbaPozadia, null);

        // farbaPozadia = Color.TRANSPARENT;
        mojaUvodnaFarba = analogClock.farbaPozadia;
        analogClock.farbaRuciciek = ResourcesCompat.getColor(getResources(), R.color.farbaRuciciek, null);

        analogClock.farbaVysuvnikaRuciciek = ResourcesCompat.getColor(getResources(), R.color.farbaVysuvnikaRuciciek, null);
        // Color.rgb(140,230,160);
        // tu je problem s farbou
        // analogClock.farbaPozadiaGradient = analogClock.farbaRuciciek;

        analogClock.farbaPozadiaGradient = ResourcesCompat.getColor(getResources(), R.color.farbaPozadiaGradient, null);

        // farbaVnutraRuciciek = farbaPozadia;
        analogClock.farbaZnaciek = ResourcesCompat.getColor(getResources(), R.color.farbaZnaciek, null);
//                Color.WHITE;
        analogClock.farbaCisel = ResourcesCompat.getColor(getResources(), R.color.farbaCisel, null);  // Color.WHITE;
        analogClock.farbaTiena = ResourcesCompat.getColor(getResources(), R.color.farbaTiena, null);  // Color.DKGRAY;
        analogClock.farbaObvodovejCiary = ResourcesCompat.getColor(getResources(), R.color.farbaObvodovejCiary, null);  // Color.rgb(240,240,210);

        analogClock.farbaVnutraObvodovejCiary = ResourcesCompat.getColor(getResources(), R.color.farbaVnutraObvodovejCiary, null);  // Color.rgb(5, 20, 20);
        analogClock.farbaVnutraRuciciek = ResourcesCompat.getColor(getResources(), R.color.farbaVnutraRuciciek, null);  // analogClock.farbaVnutraObvodovejCiary;
        analogClock.farbaVnutraObvodovejCiaryGradient = ResourcesCompat.getColor(getResources(), R.color.farbaVnutraObvodovejCiaryGradient, null); // analogClock.farbaRuciciek;
        analogClock.farbaSekundovejRucicky = ResourcesCompat.getColor(getResources(), R.color.farbaSekundovejRucicky, null);  // Color.rgb(255,40,30);

        analogClock.farbaVnutraSekundovejRucicky = ResourcesCompat.getColor(getResources(), R.color.farbaVnutraSekundovejRucicky, null);

        analogClock.farbaVysuvnikaSekundovejRucicky = ResourcesCompat.getColor(getResources(), R.color.farbaVysuvnikaSekundovejRucicky, null);

        analogClock.farbaOzdobnehoKruhu = ResourcesCompat.getColor(getResources(), R.color.farbaOzdobnehoKruhu, null);  // Color.rgb(20, 60, 70);
        analogClock.farbaOzdobnychLucov = ResourcesCompat.getColor(getResources(), R.color.farbaOzdobnychLucov, null); // Color.rgb(50, 40, 78);

        analogClock.mojPolomer = getResources().getString(R.string.radius);  // "100"; // uvodne nastavenie polomeru
        analogClock.mojTextOdsad = getResources().getString(R.string.odsad);  //  "200"; // okraj

        analogClock.mojTextZosuv = getResources().getString(R.string.zosuv); //  "55"; // prisposobenie vysky
        analogClock.mojTextPoloha = getResources().getString(R.string.poloha);  //  "MIDDLE";

        analogClock.mojaHrubkaObvodovejCiary = getResources().getString(R.string.hrubkaobvodovejciary); // "48";
        analogClock.mojeOdsadenieObvodovejCiary = getResources().getString(R.string.odsadenieobvodovejciary);  // "-24";

        // poloha cifernika
        analogClock.b_top = getResources().getBoolean(R.bool.b_top);  //  false;
        analogClock.b_middle = getResources().getBoolean(R.bool.b_middle);  //  true;
        analogClock.b_bottom = getResources().getBoolean(R.bool.b_bottom);  //  false;

        // tvar ciferniku
        analogClock.h_round = getResources().getBoolean(R.bool.h_round); // false;    // okruhly
        analogClock.h_rect_cd = getResources().getBoolean(R.bool.h_rect_cd);   // true;  // pravouhly konst vzdialenost bodov po obvode
        analogClock.h_rect_ca = getResources().getBoolean(R.bool.h_rect_ca); // false;  // pravouhly konst uhol chodu rucicky
        analogClock.h_shadow = getResources().getBoolean(R.bool.h_shadow);  //  true;

        // tvar rucicky
        // analogClock.h_line = false;  //
        analogClock.h_line_constant_length = getResources().getBoolean(R.bool.h_line_constant_length);  // false;
        analogClock.h_mechanic = getResources().getBoolean(R.bool.h_mechanic);  //  true;
        analogClock.h_mechanic_full = getResources().getBoolean(R.bool.h_mechanic_full);  //  false;
        // beh rucicky
        analogClock.h_continouos = getResources().getBoolean(R.bool.h_continouos);  //  true;
        analogClock.h_step = getResources().getBoolean(R.bool.h_step);  //  false;
        // tvar obvodovych znaciek
        analogClock.b_lines = getResources().getBoolean(R.bool.b_lines);  //  true;
        analogClock.b_circles = getResources().getBoolean(R.bool.b_circles);  //  false;
        analogClock.kazdaPiata = getResources().getBoolean(R.bool.kazdaPiata);  // true;
        // cisla po obvode
        analogClock.b_arabic_numbers = getResources().getBoolean(R.bool.b_arabic_numbers);  // true;
        analogClock.b_roman_numbers = getResources().getBoolean(R.bool.b_roman_numbers);  //  false;
        analogClock.b_none = getResources().getBoolean(R.bool.b_none);  //  false;
        // pomocne ciary
        analogClock.h_inner_round = getResources().getBoolean(R.bool.inner_round);   //  false;
        analogClock.h_beam = getResources().getBoolean(R.bool.beam);   // false;
        analogClock.h_contour = getResources().getBoolean(R.bool.contour);  // false;
        analogClock.h_inner_contour = getResources().getBoolean(R.bool.innercontour);  // true;
        analogClock.h_angle = getResources().getBoolean(R.bool.angle);  //  false;

        analogClock.h_background_gradient = getResources().getBoolean(R.bool.h_background_gradient);  //  true;
        analogClock.h_inner_contour_gradient = getResources().getBoolean(R.bool.h_inner_contour_gradient);  //  true;
    }*/

    /*void ulozDoSharedPreferences(String nazovOdkladaciehoSuboru) {

        SharedPreferences sp = getSharedPreferences(nazovOdkladaciehoSuboru, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean("TOP", analogClock.b_top);
        editor.putBoolean("MIDDLE", analogClock.b_middle);
        editor.putBoolean("BOTTOM", analogClock.b_bottom);

        // editor.putBoolean("LINE", analogClock.h_line);
        editor.putBoolean("LINECONSTANTLENGTH", analogClock.h_line_constant_length);

        editor.putBoolean("MECHANIC", analogClock.h_mechanic);
        editor.putBoolean("MECHANICFULL", analogClock.h_mechanic_full);

        editor.putBoolean("CONTINOUOS", analogClock.h_continouos);
        editor.putBoolean("STEP", analogClock.h_step);

        editor.putBoolean("ROUND", analogClock.h_round);
        editor.putBoolean("RECT_CD", analogClock.h_rect_cd);
        editor.putBoolean("RECT_CA", analogClock.h_rect_ca);
        editor.putBoolean("SHADOW", analogClock.h_shadow);

        // znacky po obvode
        editor.putBoolean("LINES", analogClock.b_lines);
        editor.putBoolean("CIRCLES", analogClock.b_circles);
        editor.putBoolean("EVERYFIFTH", analogClock.kazdaPiata);
        // cisla po obvode
        editor.putBoolean("ARABIC", analogClock.b_arabic_numbers);
        editor.putBoolean("ROMAN", analogClock.b_roman_numbers);
        editor.putBoolean("NONE", analogClock.b_none);
        // pomocne ciary
        editor.putBoolean("INNER_ROUND", analogClock.h_inner_round);

        editor.putBoolean("BEAM", analogClock.h_beam);
        editor.putBoolean("CONTOUR", analogClock.h_contour);
        editor.putBoolean("INNERCONTOUR", analogClock.h_inner_contour);
        editor.putBoolean("ANGLE", analogClock.h_angle);

        editor.putBoolean("BACKGROUNDGRADIENT", analogClock.h_background_gradient);
        editor.putBoolean("INNERROUNDGRADIENT", analogClock.h_inner_contour_gradient);

        editor.putString("RADIUS", analogClock.mojPolomer);
        editor.putString("ODSAD", analogClock.mojTextOdsad);
        editor.putString("ZOSUV", analogClock.mojTextZosuv);
        editor.putString("POLOHA", analogClock.mojTextPoloha);
        editor.putString("HRUBKAOBVODOVEJCIARY", analogClock.mojaHrubkaObvodovejCiary);
        editor.putString("ODSADENIEOBVODOVEJCIARY", analogClock.mojeOdsadenieObvodovejCiary);

        editor.putInt("FARBAPOZADIA", analogClock.farbaPozadia);

        // editor.putInt("FARBAPOZADIA", ResourcesCompat.getColor(getResources(), R.color.farbaPozadia, null));

        editor.putInt("FARBAPOZADIAGRADIENT", analogClock.farbaPozadiaGradient);

        // editor.putInt("FARBAPOZADIAGRADIENT", ResourcesCompat.getColor(getResources(), R.color.farbaPozadiaGradient, null));

        editor.putInt("FARBARUCICIEK", analogClock.farbaRuciciek);
        editor.putInt("FARBAVNUTRARUCICIEK", analogClock.farbaVnutraRuciciek);

        editor.putInt("FARBAVYSUVNIKARUCICIEK", analogClock.farbaVysuvnikaRuciciek);

        editor.putInt("FARBAZNACIEK", analogClock.farbaZnaciek);
        editor.putInt("FARBACISEL", analogClock.farbaCisel);
        editor.putInt("FARBATIENA", analogClock.farbaTiena);
        editor.putInt("FARBAOBVODOVEJCIARY", analogClock.farbaObvodovejCiary);
        editor.putInt("FARBAVNUTRAOBVODOVEJCIARY", analogClock.farbaVnutraObvodovejCiary);
        editor.putInt("FARBAVNUTRAOBVODOVEJCIARYGRADIENT", analogClock.farbaVnutraObvodovejCiaryGradient);
        editor.putInt("FARBASEKUNDOVEJRUCICKY", analogClock.farbaSekundovejRucicky);

        editor.putInt("FARBAVNUTRASEKUNDOVEJRUCICKY", analogClock.farbaVnutraSekundovejRucicky);

        editor.putInt("FARBAVYSUVNIKASEKUNDOVEJRUCICKY", analogClock.farbaVysuvnikaSekundovejRucicky);

        editor.putInt("FARBAOZDOBNEHOKRUHU", analogClock.farbaOzdobnehoKruhu);
        editor.putInt("FARBAOZDOBNYCHLUCOV", analogClock.farbaOzdobnychLucov);

        editor.apply();

        if(nazovOdkladaciehoSuboru == "MojeUserPrefs")
            Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
    }

    void vyberZoSharedPreferences(String nazovOdkladaciehoSuboru) {

        SharedPreferences sp = getSharedPreferences(nazovOdkladaciehoSuboru, Context.MODE_PRIVATE);
        // if (sp != null) {
        // cifernik
        analogClock.b_top = sp.getBoolean("TOP", getResources().getBoolean(R.bool.b_top));  //  false);
        analogClock.b_middle = sp.getBoolean("MIDDLE", getResources().getBoolean(R.bool.b_middle));  //  true);
        analogClock.b_bottom = sp.getBoolean("BOTTOM", getResources().getBoolean(R.bool.b_bottom));  //  false);

        // analogClock.h_line = sp.getBoolean("LINE", false);
        analogClock.h_line_constant_length = sp.getBoolean("LINECONSTANTLENGTH", getResources().getBoolean(R.bool.h_line_constant_length));  //  false);
        analogClock.h_mechanic = sp.getBoolean("MECHANIC", getResources().getBoolean(R.bool.h_mechanic));  // true);
        analogClock.h_mechanic_full = sp.getBoolean("MECHANICFULL", getResources().getBoolean(R.bool.h_mechanic_full));  //  false);

        analogClock.h_continouos = sp.getBoolean("CONTINOUOS", getResources().getBoolean(R.bool.h_continouos));  // true);
        analogClock.h_step = sp.getBoolean("STEP", getResources().getBoolean(R.bool.h_step));  //  false);

        analogClock.h_round = sp.getBoolean("ROUND", getResources().getBoolean(R.bool.h_round));  //  false);
        analogClock.h_rect_cd = sp.getBoolean("RECT_CD", getResources().getBoolean(R.bool.h_rect_cd));  //  true);
        analogClock.h_rect_ca = sp.getBoolean("RECT_CA", getResources().getBoolean(R.bool.h_rect_ca));  //  false);
        analogClock.h_shadow = sp.getBoolean("SHADOW", getResources().getBoolean(R.bool.h_shadow));  //  false);

        analogClock.b_lines = sp.getBoolean("LINES", getResources().getBoolean(R.bool.b_lines));  //  true);
        analogClock.b_circles = sp.getBoolean("CIRCLES", getResources().getBoolean(R.bool.b_circles));  //  false);
        analogClock.kazdaPiata = sp.getBoolean("EVERYFIFTH", getResources().getBoolean(R.bool.kazdaPiata));  //  false);

        analogClock.b_arabic_numbers = sp.getBoolean("ARABIC", getResources().getBoolean(R.bool.b_arabic_numbers));  //  true);
        analogClock.b_roman_numbers = sp.getBoolean("ROMAN", getResources().getBoolean(R.bool.b_roman_numbers));  //  false);
        analogClock.b_none = sp.getBoolean("NONE", getResources().getBoolean(R.bool.b_none));  //  false);

        analogClock.h_inner_round = sp.getBoolean("INNER_ROUND", getResources().getBoolean(R.bool.inner_round));  //   false);
        analogClock.h_beam = sp.getBoolean("BEAM", false);
        analogClock.h_contour = sp.getBoolean("CONTOUR", getResources().getBoolean(R.bool.contour));   //  false);
        analogClock.h_inner_contour = sp.getBoolean("INNERCONTOUR", getResources().getBoolean(R.bool.innercontour));   //  true);
        analogClock.h_angle = sp.getBoolean("ANGLE", getResources().getBoolean(R.bool.angle));   //  false);

        analogClock.h_background_gradient = sp.getBoolean("BACKGROUNDGRADIENT", true);
        analogClock.h_inner_contour_gradient = sp.getBoolean("INNERROUNDGRADIENT", true);

        analogClock.mojPolomer = sp.getString("RADIUS", getResources().getString(R.string.radius));   // "150");

        analogClock.mojTextOdsad = sp.getString("ODSAD", getResources().getString(R.string.odsad));  // "120");

        analogClock.mojTextZosuv = sp.getString("ZOSUV", getResources().getString(R.string.zosuv));
        analogClock.mojTextPoloha = sp.getString("POLOHA", getResources().getString(R.string.poloha));  // "MIDDLE");

        analogClock.mojaHrubkaObvodovejCiary = sp.getString("HRUBKAOBVODOVEJCIARY", getResources().getString(R.string.hrubkaobvodovejciary));  // "48");
        analogClock.mojeOdsadenieObvodovejCiary = sp.getString("ODSADENIEOBVODOVEJCIARY", getResources().getString(R.string.odsadenieobvodovejciary));  //  "-24");

        analogClock.farbaPozadia = sp.getInt("FARBAPOZADIA",  ResourcesCompat.getColor(getResources(), R.color.farbaPozadia, null));

        analogClock.farbaPozadiaGradient = sp.getInt("FARBAPOZADIAGRADIENT", ResourcesCompat.getColor(getResources(), R.color.farbaPozadiaGradient, null));

        mojaUvodnaFarba = analogClock.farbaPozadia;
        analogClock.farbaRuciciek = sp.getInt("FARBARUCICIEK", ResourcesCompat.getColor(getResources(), R.color.farbaRuciciek, null));

        analogClock.farbaVysuvnikaRuciciek = sp.getInt("FARBAVYSUVNIKARUCICIEK", ResourcesCompat.getColor(getResources(), R.color.farbaVysuvnikaRuciciek, null));

        analogClock.farbaVnutraRuciciek = sp.getInt("FARBAVNUTRARUCICIEK", ResourcesCompat.getColor(getResources(), R.color.farbaVnutraRuciciek, null)); //analogClock.farbaPozadia);
        analogClock.farbaZnaciek = sp.getInt("FARBAZNACIEK", ResourcesCompat.getColor(getResources(), R.color.farbaZnaciek, null));  // Color.WHITE);
        analogClock.farbaCisel = sp.getInt("FARBACISEL", ResourcesCompat.getColor(getResources(), R.color.farbaCisel, null)); // Color.WHITE);
        analogClock.farbaTiena = sp.getInt("FARBATIENA", ResourcesCompat.getColor(getResources(), R.color.farbaTiena, null));  //  Color.GRAY);
        analogClock.farbaObvodovejCiary = sp.getInt("FARBAOBVODOVEJCIARY", ResourcesCompat.getColor(getResources(), R.color.farbaObvodovejCiary, null));  // Color.rgb(80,100,60));

        analogClock.farbaVnutraObvodovejCiary = sp.getInt("FARBAVNUTRAOBVODOVEJCIARY", ResourcesCompat.getColor(getResources(), R.color.farbaVnutraObvodovejCiary, null)); // Color.rgb(5, 20, 20));

        analogClock.farbaVnutraObvodovejCiaryGradient = sp.getInt("FARBAVNUTRAOBVODOVEJCIARYGRADIENT", ResourcesCompat.getColor(getResources(), R.color.farbaVnutraObvodovejCiaryGradient, null));  // Color.rgb(5, 20, 20));

        analogClock.farbaSekundovejRucicky = sp.getInt("FARBASEKUNDOVEJRUCICKY", ResourcesCompat.getColor(getResources(), R.color.farbaSekundovejRucicky, null)); // Color.rgb(50,50,50));

        analogClock.farbaVnutraSekundovejRucicky = sp.getInt("FARBAVNUTRASEKUNDOVEJRUCICKY", ResourcesCompat.getColor(getResources(), R.color.farbaVnutraSekundovejRucicky, null)); // Color.rgb(50,50,50));

        analogClock.farbaVysuvnikaSekundovejRucicky = sp.getInt("FARBAVYSUVNIKASEKUNDOVEJRUCICKY", ResourcesCompat.getColor(getResources(), R.color.farbaVysuvnikaSekundovejRucicky, null)); // Color.rgb(50,50,50));

        analogClock.farbaOzdobnehoKruhu = sp.getInt("FARBAOZDOBNEHOKRUHU", ResourcesCompat.getColor(getResources(), R.color.farbaOzdobnehoKruhu, null));  //  Color.rgb(20, 60, 70));
        analogClock.farbaOzdobnychLucov = sp.getInt("FARBAOZDOBNYCHLUCOV",ResourcesCompat.getColor(getResources(), R.color.farbaOzdobnychLucov, null));  //  Color.rgb(50, 40, 78));

        if(nazovOdkladaciehoSuboru == "MojeUserPrefs")
            Toast.makeText(this, "Settings Restored", Toast.LENGTH_SHORT).show();

    }*/

}
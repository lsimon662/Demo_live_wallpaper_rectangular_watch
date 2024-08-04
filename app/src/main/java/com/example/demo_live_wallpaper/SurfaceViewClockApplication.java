package com.example.demo_live_wallpaper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import yuku.ambilwarna.AmbilWarnaDialog;

public class SurfaceViewClockApplication extends AppCompatActivity {
    androidx.appcompat.app.AlertDialog.Builder zostava;

    MySurface mySurface;
    int mojaUvodnaFarba;

    View decorView;
    int uiOptions;

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

        // setContentView(R.layout.activity_surface_view_clock_application);

        mySurface = new MySurface(getApplicationContext());
        setContentView(mySurface);

        try {
            mySurface.analogClock.vyberZoSharedPreferences("priZastaveni");
        } catch (Exception e) {
            mySurface.analogClock.nastavUvodneHodnoty();
            e.printStackTrace();
        }

    }

    // public void exit(View v) {System.exit(0);}

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
    protected void onDestroy() {
        // Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    @Override
    protected void onStop() {
        // Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        super.onStop();
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

        menu.findItem(R.id.m_background).getIcon().setTint(mySurface.analogClock.farbaPozadia);
        menu.findItem(R.id.m_background_color_gradient).getIcon().setTint(mySurface.analogClock.farbaPozadiaGradient);

        menu.findItem(R.id.m_hands).getIcon().setTint(mySurface.analogClock.farbaRuciciek);
        menu.findItem(R.id.m_shift_hands).getIcon().setTint(mySurface.analogClock.farbaVysuvnikaRuciciek);

        menu.findItem(R.id.inner_hands).getIcon().setTint(mySurface.analogClock.farbaVnutraRuciciek);
        menu.findItem(R.id.inner_seconds_hand).getIcon().setTint(mySurface.analogClock.farbaVnutraSekundovejRucicky);

        menu.findItem(R.id.hand_shift_seconds_color).getIcon().setTint(mySurface.analogClock.farbaVysuvnikaSekundovejRucicky);  //   24.4.2024

        // menu.findItem(R.id.m_margin).getIcon().setTint(farbaObvodovejCiary);

        menu.findItem(R.id.m_marks).getIcon().setTint(mySurface.analogClock.farbaZnaciek);
        menu.findItem(R.id.numbers).getIcon().setTint(mySurface.analogClock.farbaCisel);
        menu.findItem(R.id.contour_color).getIcon().setTint(mySurface.analogClock.farbaObvodovejCiary);
        menu.findItem(R.id.inner_contour_color).getIcon().setTint(mySurface.analogClock.farbaVnutraObvodovejCiary);

        menu.findItem(R.id.inner_contour_color_gradient).getIcon().setTint(mySurface.analogClock.farbaVnutraObvodovejCiaryGradient);

        menu.findItem(R.id.shadow_color).getIcon().setTint(mySurface.analogClock.farbaTiena);
        menu.findItem(R.id.hand_seconds_color).getIcon().setTint(mySurface.analogClock.farbaSekundovejRucicky);

        menu.findItem(R.id.inner_roud_color).getIcon().setTint(mySurface.analogClock.farbaOzdobnehoKruhu);
        menu.findItem(R.id.beam_color).getIcon().setTint(mySurface.analogClock.farbaOzdobnychLucov);

        nastavMenu(menu);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    public void nastavMenu(Menu menu) {

        if (mySurface.analogClock.b_top) {
            menu.findItem(R.id.top).setChecked(true);
            // menu.findItem(R.id.rc_distance).setChecked(false);
            // menu.findItem(R.id.rc_angle).setChecked(false);
        }

        if (mySurface.analogClock.b_middle) {
            menu.findItem(R.id.middle).setChecked(true);
            // menu.findItem(R.id.rc_distance).setChecked(false);
            // menu.findItem(R.id.rc_angle).setChecked(false);
        }

        if (mySurface.analogClock.b_bottom) {
            menu.findItem(R.id.bottom).setChecked(true);
            // menu.findItem(R.id.rc_distance).setChecked(false);
            // menu.findItem(R.id.rc_angle).setChecked(false);
        }

        if (mySurface.analogClock.h_round) {
            menu.findItem(R.id.round).setChecked(true);
            // menu.findItem(R.id.rc_distance).setChecked(false);
            // menu.findItem(R.id.rc_angle).setChecked(false);
        }

        if (mySurface.analogClock.h_rect_cd) {
            menu.findItem(R.id.rc_distance).setChecked(true);
            // menu.findItem(R.id.rc_angle).setChecked(false);
            // menu.findItem(R.id.round).setChecked(false);
        }

        if (mySurface.analogClock.h_rect_ca) {
            menu.findItem(R.id.rc_angle).setChecked(true);
            // menu.findItem(R.id.round).setChecked(false);
            // menu.findItem(R.id.rc_distance).setChecked(false);
        }

        if (mySurface.analogClock.h_shadow)
            menu.findItem(R.id.shadow).setChecked(true);
        else
            menu.findItem(R.id.shadow).setChecked(false);

        if(mySurface.analogClock.h_line_constant_length) {
            menu.findItem(R.id.constant_length_line).setChecked(true);
            // menu.findItem(R.id.mechanic).setChecked(false);
        }

        if(mySurface.analogClock.h_mechanic) {
            menu.findItem(R.id.mechanic).setChecked(true);
            // menu.findItem(R.id.line).setChecked(false);
        }

        if(mySurface.analogClock.h_mechanic_full) {
            menu.findItem(R.id.mechanic_full).setChecked(true);
            // menu.findItem(R.id.line).setChecked(false);
        }

        // chod ruciciek
        if(mySurface.analogClock.h_continouos) {
            menu.findItem(R.id.continouos).setChecked(true);
            // menu.findItem(R.id.step).setChecked(false);
        }

        if(mySurface.analogClock.h_step) {
            menu.findItem(R.id.step).setChecked(true);
            // menu.findItem(R.id.continouos).setChecked(false);
        }

        if(mySurface.analogClock.h_background_gradient) {
            menu.findItem(R.id.m_background_gradient).setChecked(true);
        }

        if(mySurface.analogClock.h_inner_contour_gradient) {
            menu.findItem(R.id.inner_contour_gradient).setChecked(true);
        }

        // tvar bodov po obvode
        if(mySurface.analogClock.b_lines) {
            menu.findItem(R.id.m_lines).setChecked(true);
            // menu.findItem(R.id.m_circles).setChecked(false);
        }

        if(mySurface.analogClock.b_circles) {
            menu.findItem(R.id.m_circles).setChecked(true);
            // menu.findItem(R.id.m_lines).setChecked(false);
        }

        if(mySurface.analogClock.kazdaPiata)
            menu.findItem(R.id.everyFifth).setChecked(true);
        else
            menu.findItem(R.id.everyFifth).setChecked(false);

        // cisla po obvode
        if (mySurface.analogClock.b_arabic_numbers) {
            menu.findItem(R.id.arabic_numbers).setChecked(true);
            // menu.findItem(R.id.roman_numbers).setChecked(false);
            // menu.findItem(R.id.none_numbers).setChecked(false);
        }

        if (mySurface.analogClock.b_roman_numbers) {
            menu.findItem(R.id.roman_numbers).setChecked(true);
            // menu.findItem(R.id.arabic_numbers).setChecked(false);
            // menu.findItem(R.id.none_numbers).setChecked(false);
        }

        if (mySurface.analogClock.b_none) {
            menu.findItem(R.id.none_numbers).setChecked(true);
            // menu.findItem(R.id.arabic_numbers).setChecked(false);
            // menu.findItem(R.id.roman_numbers).setChecked(false);
        }

        // pomocne ciary
        if (mySurface.analogClock.h_inner_round)
            menu.findItem(R.id.inner_round).setChecked(true);
        else
            menu.findItem(R.id.inner_round).setChecked(false);

        if (mySurface.analogClock.h_beam)
            menu.findItem(R.id.beam).setChecked(true);
        else
            menu.findItem(R.id.beam).setChecked(false);

        if (mySurface.analogClock.h_contour)
            menu.findItem(R.id.contour).setChecked(true);
        else
            menu.findItem(R.id.contour).setChecked(false);

        if (mySurface.analogClock.h_inner_contour)
            menu.findItem(R.id.inner_contour).setChecked(true);
        else
            menu.findItem(R.id.inner_contour).setChecked(false);

        if (mySurface.analogClock.h_angle)
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
                    mySurface.analogClock.b_middle = false;
                    mySurface.analogClock.b_bottom = false;
                    // b_item2 = true;
                    // AlertDialog.Builder bld = new AlertDialog.Builder(getContext());
                }
                mySurface.analogClock.b_top = item.isChecked();
                // Toast.makeText(this, "Numbers Arabic selected", Toast.LENGTH_SHORT).show();
                // return true;
                break;

            case R.id.middle:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.b_top = false;
                    mySurface.analogClock.b_bottom = false;
                }
                mySurface.analogClock.b_middle = item.isChecked();
                // return true;
                break;

            case R.id.bottom:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.b_top = false;
                    mySurface.analogClock.b_middle = false;
                }
                mySurface.analogClock.b_bottom = item.isChecked();
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
                    mySurface.analogClock.h_mechanic = false;
                    mySurface.analogClock.h_mechanic_full = false;
                    mySurface.analogClock.h_line_constant_length = false;
                }
                mySurface.analogClock.h_line = item.isChecked();
                // Toast.makeText(this, "Hand line ", Toast.LENGTH_SHORT).show();
                // break;
                return true;*/

            case R.id.constant_length_line:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.h_mechanic = false;
                    mySurface.analogClock.h_mechanic_full = false;
                    // mySurface.analogClock.h_line = false;
                }
                mySurface.analogClock.h_line_constant_length = item.isChecked();
                // Toast.makeText(this, "Hand line ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.mechanic:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.h_line_constant_length = false;
                    // mySurface.analogClock.h_line = false;
                    mySurface.analogClock.h_mechanic_full = false;
                }
                mySurface.analogClock.h_mechanic = item.isChecked();
                // Toast.makeText(this, "Hand mechanism ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.mechanic_full:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.h_line_constant_length = false;
                    // mySurface.analogClock.h_line = false;
                    mySurface.analogClock.h_mechanic = false;
                }
                mySurface.analogClock.h_mechanic_full = item.isChecked();
                // Toast.makeText(this, "Hand mechanism ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.continouos:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.h_step = false;
                }
                mySurface.analogClock.h_continouos = item.isChecked();
                return true;

            case R.id.step:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.h_continouos = false;
                }
                mySurface.analogClock.h_step = item.isChecked();
                return true;

            case R.id.round:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.h_rect_ca = false;
                    mySurface.analogClock.h_rect_cd = false;
                }
                mySurface.analogClock.h_round = item.isChecked();
                // mySurface.analogClock.h_line = true;
                mySurface.analogClock.h_line_constant_length = true;
                mySurface.analogClock.h_mechanic = false;
                mySurface.analogClock.h_mechanic_full = false;
                // Toast.makeText(this, "Round selected ", Toast.LENGTH_SHORT).show();
                // break;
                return true;
            case R.id.rc_angle:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.h_round = false;
                    mySurface.analogClock.h_rect_cd = false;

                }
                mySurface.analogClock.h_rect_ca = item.isChecked();
                // mySurface.analogClock.h_line = false;
                mySurface.analogClock.h_line_constant_length = false;
                mySurface.analogClock.h_mechanic = true;
                // Toast.makeText(this, "Rectangle constant angle selected ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.rc_distance:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.h_rect_ca = false;
                    mySurface.analogClock.h_round = false;

                }
                mySurface.analogClock.h_rect_cd = item.isChecked();
                // mySurface.analogClock.h_line = false;
                mySurface.analogClock.h_line_constant_length = false;
                mySurface.analogClock.h_mechanic = true;
                // Toast.makeText(this, "Rectangle constant distance selected ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.m_lines:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);

                    mySurface.analogClock.b_circles = false;
                }
                mySurface.analogClock.b_lines = item.isChecked();
                // Toast.makeText(this, "Marks lines ", Toast.LENGTH_SHORT).show();
                // break;
                return true;

            case R.id.m_circles:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.b_lines = false;

                }
                mySurface.analogClock.b_circles = item.isChecked();
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
                mySurface.analogClock.kazdaPiata = item.isChecked();

                // Toast.makeText(this, "Shadow selected", Toast.LENGTH_SHORT).show();
                return true;

            // case R.id.m_background_transparent:   // farba pozadia
            // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
            // mojaUvodnaFarba = farbaPozadia;
            // mySurface.analogClock.farbaPozadia = Color.TRANSPARENT;
            // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
            // otvorVyberFarby('P');
            // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
            // break;

            case R.id.m_background:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = mySurface.analogClock.farbaPozadia;
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
                mySurface.analogClock.h_background_gradient = item.isChecked();
                // Toast.makeText(this, "Shadow selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.inner_contour_gradient:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                mySurface.analogClock.h_inner_contour_gradient = item.isChecked();
                // Toast.makeText(this, "Shadow selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.m_marks:
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                // Toast.makeText(this, "Sub Mark color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = mySurface.analogClock.farbaZnaciek;
                otvorVyberFarby('Z');
                break;
            // return true;

            case R.id.numbers:
                // Toast.makeText(this, "Sub Numbers color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = mySurface.analogClock.farbaCisel;
                otvorVyberFarby('C');
                break;

            case R.id.shadow_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = mySurface.analogClock.farbaTiena;
                otvorVyberFarby('T');
                break;

            case R.id.contour_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = mySurface.analogClock.farbaObvodovejCiary;
                otvorVyberFarby('O');
                break;

            case R.id.inner_contour_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = mySurface.analogClock.farbaVnutraObvodovejCiary;
                otvorVyberFarby('I');
                break;

            case R.id.hand_seconds_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = mySurface.analogClock.farbaSekundovejRucicky;
                otvorVyberFarby('S');
                break;

            case R.id.inner_roud_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = mySurface.analogClock.farbaOzdobnehoKruhu;
                otvorVyberFarby('K');
                break;

            case R.id.beam_color:
                // Toast.makeText(this, "Sub Shadow color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = mySurface.analogClock.farbaOzdobnychLucov;
                otvorVyberFarby('B');
                break;

            case R.id.m_hands:     // farba ruciciek
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                // Toast.makeText(this, "Sub Hands color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = mySurface.analogClock.farbaRuciciek;
                otvorVyberFarby('R');
                break;

            case R.id.inner_hands:
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                // Toast.makeText(this, "Sub Hands color ", Toast.LENGTH_SHORT).show();
                mojaUvodnaFarba = mySurface.analogClock.farbaVnutraRuciciek;
                otvorVyberFarby('V');
                break;

            case R.id.m_background_color_gradient:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = mySurface.analogClock.farbaPozadiaGradient;
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                otvorVyberFarby('A');
                // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
                break;
            // return true;

            case R.id.inner_contour_color_gradient:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = mySurface.analogClock.farbaVnutraObvodovejCiaryGradient;
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                otvorVyberFarby('D');
                // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
                break;
            // return true;

            case R.id.m_shift_hands:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = mySurface.analogClock.farbaVysuvnikaRuciciek;
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                otvorVyberFarby('Y');
                // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
                break;
            // return true;

            case R.id.hand_shift_seconds_color:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = mySurface.analogClock.farbaVysuvnikaSekundovejRucicky;
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                otvorVyberFarby('F');
                // item.getIcon().setColorFilter(farbaPozadia, PorterDuff.Mode.SRC_ATOP);
                break;
            // return true;

            case R.id.inner_seconds_hand:   // farba pozadia
                // item.getIcon().setColorFilter(mojaUvodnaFarba, PorterDuff.Mode.SRC_ATOP);
                mojaUvodnaFarba = mySurface.analogClock.farbaVnutraSekundovejRucicky;
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
                    mySurface.analogClock.b_roman_numbers = false;
                    mySurface.analogClock.b_none = false;
                    // b_item2 = true;
                    // AlertDialog.Builder bld = new AlertDialog.Builder(getContext());
                }
                mySurface.analogClock.b_arabic_numbers = item.isChecked();
                // Toast.makeText(this, "Numbers Arabic selected", Toast.LENGTH_SHORT).show();
                // return true;
                break;

            case R.id.roman_numbers:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.b_arabic_numbers = false;
                    mySurface.analogClock.b_none = false;
                }
                mySurface.analogClock.b_roman_numbers = item.isChecked();
                // return true;
                break;

            case R.id.none_numbers:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                    mySurface.analogClock.b_arabic_numbers = false;
                    mySurface.analogClock.b_roman_numbers = false;
                }
                mySurface.analogClock.b_none = item.isChecked();
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
                mySurface.analogClock.h_shadow = item.isChecked();

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
                mySurface.analogClock.h_beam = item.isChecked();
                // return true;
                break;

            case R.id.contour:
                if(item.isChecked()) {
                    item.setChecked(false);
                }
                else {
                    item.setChecked(true);
                }
                mySurface.analogClock.h_contour = item.isChecked();
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
                mySurface.analogClock.h_inner_contour = item.isChecked();
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
                mySurface.analogClock.h_angle = item.isChecked();
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
                mySurface.analogClock.h_inner_round = item.isChecked();
                // Toast.makeText(this, "Inner Round selected " + h_inner_round, Toast.LENGTH_SHORT).show();
                return true;
            // break;

            case R.id.polomer:
                String polomer_pom = mySurface.analogClock.mojPolomer;

                zostava = new AlertDialog.Builder(SurfaceViewClockApplication.this);
                zostava.setTitle("Edge radius 10 to 320");
                final EditText vstupPolomeru = new EditText(SurfaceViewClockApplication.this);

                vstupPolomeru.setInputType(InputType.TYPE_CLASS_PHONE);
                vstupPolomeru.setText("" + mySurface.analogClock.polomer);
//                zostava.setView(vstupHmotnosti);

                LinearLayout linear_po = new LinearLayout(this);

                linear_po.setOrientation(LinearLayout.VERTICAL);

                SeekBar seek_po = new SeekBar(this);
                seek_po.setMin(10);
                seek_po.setMax(320);
                seek_po.setProgress(mySurface.analogClock.polomer);

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

                        mySurface.analogClock.mojPolomer = vstupPolomeru.getText().toString();

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });


                zostava.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mySurface.analogClock.mojPolomer = vstupPolomeru.getText().toString();   // tu test
                        mySurface.analogClock.polomer = Integer.parseInt(mySurface.analogClock.mojPolomer);

                        // Toast.makeText(GraphicActivityThread.this, "Radius : " + mojText, Toast.LENGTH_LONG).show();
                    }
                });

                zostava.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mySurface.analogClock.mojPolomer = polomer_pom;
                        dialog.cancel();
                    }
                });
                zostava.show();
                // break;
                return true;

            case R.id.m_margin:

                String margin_pom = mySurface.analogClock.mojTextOdsad;

                zostava = new AlertDialog.Builder(SurfaceViewClockApplication.this);
                zostava.setTitle("Margin 0 TO 360");
                final EditText vstupOdsadenia = new EditText(SurfaceViewClockApplication.this);

                vstupOdsadenia.setInputType(InputType.TYPE_CLASS_PHONE);
                vstupOdsadenia.setText("" + mySurface.analogClock.okraj);

                // zostava.setView(vstupOdsadenia);

                LinearLayout linear_vo = new LinearLayout(this);

                linear_vo.setOrientation(LinearLayout.VERTICAL);

                SeekBar seek_vo = new SeekBar(this);
                seek_vo.setMin(0);
                seek_vo.setMax(360);
                seek_vo.setProgress(mySurface.analogClock.okraj);

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

                        mySurface.analogClock.mojTextOdsad = vstupOdsadenia.getText().toString();

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
                        mySurface.analogClock.mojTextOdsad = vstupOdsadenia.getText().toString();
                        // Toast.makeText(ClockApplication.this, "Okraj : " + mojTextOdsad, Toast.LENGTH_LONG).show();
                    }
                });

                zostava.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mySurface.analogClock.mojTextOdsad = margin_pom;
                        dialog.cancel();
                    }
                });
                zostava.show();
                break;
            // return true;

            case R.id.zosuvPer:

                String zosuv_per_pom = mySurface.analogClock.mojTextZosuv; //

                zostava = new AlertDialog.Builder(SurfaceViewClockApplication.this);
                zostava.setTitle("Height [%] of Screen from 40 to 100");
                final EditText vstupZosuvu = new EditText(SurfaceViewClockApplication.this);

                vstupZosuvu.setInputType(InputType.TYPE_CLASS_PHONE);
                vstupZosuvu.setText("" + mySurface.analogClock.zosuv_per);

                // zostava.setView(vstupZosuvu);

                LinearLayout linear_vz = new LinearLayout(this);

                linear_vz.setOrientation(LinearLayout.VERTICAL);

                SeekBar seek_vz = new SeekBar(this);
                seek_vz.setMin(40);
                seek_vz.setMax(100);
                seek_vz.setProgress(mySurface.analogClock.zosuv_per);

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
                        mySurface.analogClock.mojTextZosuv = vstupZosuvu.getText().toString();
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
                        mySurface.analogClock.mojTextZosuv = vstupZosuvu.getText().toString();
                        // Toast.makeText(ClockApplication.this, "Zosuv : " + mojTextZosuv, Toast.LENGTH_LONG).show();
                    }
                });

                zostava.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mySurface.analogClock.mojTextZosuv = zosuv_per_pom;
                        dialog.cancel();
                    }
                });
                zostava.show();
                break;


            case R.id.hrubkaKontury:

                String mojaHrubkaObvodovejCiary_pom = mySurface.analogClock.mojaHrubkaObvodovejCiary;

                zostava = new AlertDialog.Builder(SurfaceViewClockApplication.this);
                zostava.setTitle("Contour Line Thickness from 1 to 128");
                final EditText vstupHrubkyKontury = new EditText(SurfaceViewClockApplication.this);

                vstupHrubkyKontury.setInputType(InputType.TYPE_CLASS_PHONE);
                vstupHrubkyKontury.setText("" + mySurface.analogClock.hrubkaObvodovejCiary);
                // zostava.setView(vstupHrubkyKontury);

                LinearLayout linear_hk = new LinearLayout(this);

                linear_hk.setOrientation(LinearLayout.VERTICAL);

                SeekBar seek_hk = new SeekBar(this);
                seek_hk.setMin(1);
                seek_hk.setMax(128);
                seek_hk.setProgress(mySurface.analogClock.hrubkaObvodovejCiary);

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

                        mySurface.analogClock.mojaHrubkaObvodovejCiary = vstupHrubkyKontury.getText().toString();

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
                        mySurface.analogClock.mojaHrubkaObvodovejCiary = vstupHrubkyKontury.getText().toString();
                        // Toast.makeText(ClockApplication.this, "Zosuv : " + mojTextZosuv, Toast.LENGTH_LONG).show();
                    }
                });

                zostava.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mySurface.analogClock.mojaHrubkaObvodovejCiary = mojaHrubkaObvodovejCiary_pom;
                        dialog.cancel();
                    }
                });
                zostava.show();
                break;

            case R.id.mOdsadenieObvodovejCiary:

                String mojeOdsadenieObvodovejCiary_pom = mySurface.analogClock.mojeOdsadenieObvodovejCiary;

                zostava = new AlertDialog.Builder(SurfaceViewClockApplication.this);

                zostava.setTitle("Contour Line Distance from -48 to 128");

                final EditText vstupTextOdsadenieObvodovejCiary = new EditText(SurfaceViewClockApplication.this);

                vstupTextOdsadenieObvodovejCiary.setInputType(InputType.TYPE_CLASS_PHONE);
                vstupTextOdsadenieObvodovejCiary.setText("" + mySurface.analogClock.odsadenieObvodovejCiary);

                LinearLayout linear_oc = new LinearLayout(this);

                linear_oc.setOrientation(LinearLayout.VERTICAL);

                SeekBar seek_oc = new SeekBar(this);
                seek_oc.setMin(-48);
                seek_oc.setMax(128);
                seek_oc.setProgress(mySurface.analogClock.odsadenieObvodovejCiary);

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

                        mySurface.analogClock.mojeOdsadenieObvodovejCiary = vstupTextOdsadenieObvodovejCiary.getText().toString();
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
                        mySurface.analogClock.mojeOdsadenieObvodovejCiary = vstupTextOdsadenieObvodovejCiary.getText().toString();
                        // Toast.makeText(ClockApplication.this, "Zosuv : " + mojTextZosuv, Toast.LENGTH_LONG).show();
                    }
                });

                zostava.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mySurface.analogClock.mojeOdsadenieObvodovejCiary = mojeOdsadenieObvodovejCiary_pom;
                        dialog.cancel();
                    }
                });
                zostava.show();
                break;

          /*  case R.id.m_set_wallpaper:

                // finishActivity(0);
                ulozDoSharedPreferences("preWallpaper");

                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(this, ClockWallpaperService.class));

                startActivity(intent);

                finishAffinity();

                break;*/

            case R.id.m_default_settings:   // zatial len test z citani hodnot
                // spravne ma byt nastavit menu aj premenne
                mySurface.analogClock.nastavUvodneHodnoty();

                super.closeOptionsMenu();

                break;

            case R.id.m_save_settings:

                // if (sp != null) {
                // cifernik
                mySurface.analogClock.ulozDoSharedPreferences("MojeUserPrefs");

                break;

            case R.id.m_restore_settings:

                mySurface.analogClock.vyberZoSharedPreferences("MojeUserPrefs");
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
                mySurface.analogClock.ulozDoSharedPreferences("priZastaveni");
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
                    mySurface.analogClock.farbaPozadia = color;
                    // farbaPozadia = Color.TRANSPARENT;
                }

                if (co == 'R') {
                    mySurface.analogClock.farbaRuciciek = color;
                }

                if (co == 'V') {
                    mySurface.analogClock.farbaVnutraRuciciek = color;
                }

                if (co == 'Z') {
                    mySurface.analogClock.farbaZnaciek = color;
                }

                if (co == 'C') {
                    mySurface.analogClock.farbaCisel = color;
                }

                if (co == 'T') {
                    mySurface.analogClock.farbaTiena = color;
                }

                if (co == 'O') {
                    mySurface.analogClock.farbaObvodovejCiary = color;
                }

                if (co == 'I') {
                    mySurface.analogClock.farbaVnutraObvodovejCiary = color;
                }

                if (co == 'S') {
                    mySurface.analogClock.farbaSekundovejRucicky = color;
                }

                if (co == 'K') {
                    mySurface.analogClock.farbaOzdobnehoKruhu = color;
                }

                if (co == 'B') {
                    mySurface.analogClock.farbaOzdobnychLucov = color;
                }

                if (co == 'A') {
                    mySurface.analogClock.farbaPozadiaGradient = color;
                }

                if (co == 'D') {
                    mySurface.analogClock.farbaVnutraObvodovejCiaryGradient = color;
                }

                if (co == 'Y') {  //
                    mySurface.analogClock.farbaVysuvnikaRuciciek = color;
                }

                if (co == 'F') {
                    mySurface.analogClock.farbaVysuvnikaSekundovejRucicky = color;
                }

                if (co == 'Q') {
                    mySurface.analogClock.farbaVnutraSekundovejRucicky = color;
                }

            }

        });
        vyberFarby.show();
    }

}
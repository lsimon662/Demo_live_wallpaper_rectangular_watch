package com.example.demo_live_wallpaper;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import java.util.Calendar;
import java.util.Date;

public class AnalogClock extends View {

        /** center X. */
        private float x;
        /** center Y. */
        private float y;
        // int radius;
        private Calendar cal;
        private Paint kresba;
        // private Bitmap clockDial = BitmapFactory.decodeResource(getResources(), R.drawable.widgetdial);
        // private int sizeScaled = -1;
        // private Bitmap clockDialScaled;
        /** Hands colors. */
        private int[] colors;
        private boolean displayHandSec;

        private int sirka, vyska, vyska_neredukovana;

        private int vypln = 0;          // padding
        private int velkostPisma;

        int zosuv, zosuv_per;
        int hrubkaObvodovejCiary;
        int odsadenieObvodovejCiary;

        int medzeraOdCisiel;    // numeralSpacing
        int skratenieRucicky, skratenieHodinovejRucicky = 0;

        // private Paint kresba;
        private boolean jeInicializovana;
        // private int[] cisla = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        private String[] cisla_arabske = {"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
        private String[] cisla_rimske = {"XII", "I", "II", "III", "IIII", "V", "VI", "VII", "VIII", "IX", "X", "XI"};
        Rect pravouholnik = new Rect();

        String mojPolomer;     // polomer rohou
    String mojTextOdsad;   // ogray
    String mojTextZosuv;
    String mojTextPoloha;
    String mojaHrubkaObvodovejCiary;
    String mojeOdsadenieObvodovejCiary;

    int farbaPozadia;
    int farbaRuciciek;
    int farbaVnutraRuciciek;
    int farbaZnaciek;
    int farbaCisel;
    int farbaTiena;
    int farbaObvodovejCiary;
    int farbaVnutraObvodovejCiary;
    int farbaSekundovejRucicky;
    int farbaOzdobnehoKruhu;
    int farbaOzdobnychLucov;
    int farbaVysuvnikaRuciciek;
    int farbaVnutraObvodovejCiaryGradient;
    int farbaVnutraSekundovejRucicky;
    int farbaPozadiaGradient;
    int farbaVysuvnikaSekundovejRucicky;
    boolean h_line_constant_length;
    boolean h_mechanic_full;

    boolean h_background_gradient;
    boolean h_inner_contour_gradient;

    // tvar cifernika
    boolean h_round;    // okruhly
    boolean h_rect_cd;  // pravouhly konst vzdialenost bodov po obvode
    boolean h_rect_ca;  // pravouhly konst uhol chodu rucicky
    boolean h_shadow;
    // poloha
    boolean b_top;
    boolean b_middle;
    boolean b_bottom;
    // tvar rucicky
    boolean h_line;
    boolean h_mechanic;
    // beh rucicky
    boolean h_continouos;
    boolean h_step;
    // tvar obvodovych znaciek
    boolean b_lines;
    boolean b_circles;
    boolean kazdaPiata;
    // cisla po obvode
    boolean b_arabic_numbers;
    boolean b_roman_numbers;
    boolean b_none;
    // pomocne ciary
    boolean h_inner_round;
    boolean h_beam;
    boolean h_contour;
    boolean h_inner_contour;
    boolean h_angle;

    RelativeLayout mojeRozlozenie;
    int mojaUvodnaFarba;
    // private int umiestnenie, umiestnenie2;
    Shader shader;

    int polomer, okraj, min, min_bez_okraja;

    Cifernik cf, cfj;

    public AnalogClock(Context context) {
            super(context);
            // cal = Calendar.getInstance();
        }

    public AnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        System.out.println("Start z XML constructor c. 2 " + attrs.getPositionDescription() + " sirka " + getWidth());
         nastavUvodneHodnoty();
        // inicializujHodiny();
    }


    public void config(float x, float y, int size, Date date, Paint paint, int[] colors, boolean displayHandSec) {
             this.x = x;
             this.y = y;
        System.out.println("*** config  x " + this.x + " y " + this.y);
            this.sirka = (int) (x * 2.0);  // kvoli kompatibilite
            this.vyska_neredukovana = (int) (y * 2.0);
        System.out.println("*** config vyska_neredukovana " + vyska_neredukovana + " sirka " + this.sirka + " x " + x + " y " + y);

            // this.kresba = paint;
             // this.colors = colors;
//            this.displayHandSec = displayHandSec;

            // cal.setTime(date);
        
        }

    public void inicializujHodiny() {      // initClock

        // this.vyska = zosuv_per * getHeight() / 100;   //
        this.vyska = zosuv_per * this.vyska_neredukovana / 100;   //

        // !!!!!  tu je problem
        min = Math.min(this.sirka, this.vyska);
        // dopletena je min s min bez okraja
        // aj vyska a vyska redukovana treba zmenit nazvy na vyska a vyska_zo_zosuvom
        // lebo je z toho potom maglajs
        // min = Math.min(this.sirka - okraj, this.vyska - okraj);

        vypln = medzeraOdCisiel + 80;
        velkostPisma = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

        // polomer = min / 4 - vypln; // preco je zamiesany polomer s polohou cisel

        System.out.println("*** inicializuj vyska_neredukovana " + this.vyska_neredukovana + " sirka " + this.sirka + " polomer " + polomer);

        skratenieRucicky = min / 20;
        skratenieHodinovejRucicky = min / 7;

        if(mojPolomer != "" && mojPolomer != null)
            try { polomer = Integer.parseInt(mojPolomer);
                if(polomer < 5) polomer = 5;
                if(polomer > 320) polomer = 320;
                // okraj = Integer.parseInt(mojTextOdsad);
                // System.out.println("Polomer : " + polomer);
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe); }

        if(mojTextOdsad != "" && mojTextOdsad != null) {
            try {
                okraj = Integer.parseInt(mojTextOdsad);
                if (okraj < 0) okraj = 0;
                if (okraj > 360) okraj = 360;

                System.out.println("Okraj : " + okraj);
            } catch (NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }
        }

        if(mojTextZosuv != "" && mojTextZosuv != null)
            try {
                zosuv_per = Integer.parseInt(mojTextZosuv);
                if(zosuv_per < 40)
                    zosuv_per = 40;
                if(zosuv_per > 100)
                    zosuv_per = 100;

                System.out.println("Zosuv [%] : " + zosuv_per);} catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe); }

        if(mojaHrubkaObvodovejCiary != "" && mojaHrubkaObvodovejCiary != null)
            try {
                hrubkaObvodovejCiary = Integer.parseInt(mojaHrubkaObvodovejCiary);
                if(hrubkaObvodovejCiary < 1) hrubkaObvodovejCiary = 1;
                if(hrubkaObvodovejCiary > 128) hrubkaObvodovejCiary = 128;

                System.out.println("Hrubka obvodovej ciary : " + hrubkaObvodovejCiary);} catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe); }

        if(mojeOdsadenieObvodovejCiary != "" && mojeOdsadenieObvodovejCiary != null)
            try {
                odsadenieObvodovejCiary = Integer.parseInt(mojeOdsadenieObvodovejCiary);
                if(odsadenieObvodovejCiary > 128) odsadenieObvodovejCiary = 128;
                if(odsadenieObvodovejCiary < -48) odsadenieObvodovejCiary = -48;

                System.out.println("Odsadenie obvodovej ciary / -47 to 127 / : " + odsadenieObvodovejCiary);} catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe); }

        cal = Calendar.getInstance();
        kresba = new Paint();
        jeInicializovana = true; // isInit
        cf = new Cifernik(60);     // pre kreslenie cifernika a sekundovy beh
        cfj = new Cifernik(1500);  // pre jemny beh
    }

    protected void onDraw(Canvas platno) {
            super.onDraw(platno);

            if (kresba != null && sirka > 0 && vyska > 0) {

                if(h_background_gradient) {
                    shader = new RadialGradient(sirka / 2, vyska_neredukovana / 2, vyska_neredukovana / 2,
                            farbaPozadia, farbaPozadiaGradient, Shader.TileMode.CLAMP);
                    // Paint paint = new Paint();
                    kresba.setShader(shader);
                } else
                    kresba.setColor(farbaPozadia);

                platno.drawRect(new RectF(0, 0, sirka, vyska_neredukovana), kresba);
                kresba.setShader(null);

                kresba.setColor(farbaPozadia);

//                umiestnenie =  cal.get(Calendar.SECOND) * 25 + cal.get(Calendar.MILLISECOND) / 40;
//                umiestnenie2 =  cal.get(Calendar.SECOND) * 100 + cal.get(Calendar.MILLISECOND) / 10;

                kresliPevneCiary(platno, 0,0,farbaPozadia);
                platno.save();

                kresba.setColor(Color.GREEN);
                kresba.setStrokeWidth(12);
                kresba.setTextSize(24);
                kresba.setTypeface(Typeface.MONOSPACE);

//                platno.drawText(" mojTextPoloha " + mojTextPoloha + " mojaHrubkaObvodovejCiary " + mojaHrubkaObvodovejCiary + " mojeOdsadenieObvodovejCiary " + mojeOdsadenieObvodovejCiary, sirka / 50, vyska / 1.35f,kresba);
//platno.drawText(" mojPolomer " + mojPolomer + " mojTextOdsad " + mojTextOdsad + " mojTextZosuv " + mojTextZosuv, sirka / 50.0f, vyska / 1.3f,kresba);
//platno.drawText(" polomer " + polomer + " okraj " + okraj + " zosuv " + zosuv + " zosuv_per " + zosuv_per, sirka / 50.0f, vyska / 1.25f,kresba);
//platno.drawText(" sirka " + sirka + " vyska_neredukovana " + vyska_neredukovana + " vyska " + vyska + " min " + min, sirka / 50.0f, vyska / 1.2f,kresba);
//platno.drawText(" medzera od cisel " + medzeraOdCisiel + " vypln " + vypln, sirka / 50.0f, vyska / 1.16f,kresba);


                kresliBody(platno, 0,0, farbaZnaciek);

                 platno.save();

                if(b_arabic_numbers)
                    kresliCisla(platno, cisla_arabske, 0,0, farbaCisel);

                if(b_roman_numbers)
                    kresliCisla(platno, cisla_rimske, 0,0, farbaCisel);

                 platno.save();

                 kresliRucicky(platno);
            }
        }



    public void kresliPevneCiary(Canvas platno, int posunT_X, int posunT_Y, int farba) {

        if(h_background_gradient) {
            System.out.println("*** ClockApplication  *** kresliPevneCiary *** barva " + mojaUvodnaFarba + " width " + sirka + " height " + vyska_neredukovana);
            shader = new RadialGradient(sirka / 2, vyska_neredukovana / 2, vyska_neredukovana / 2,
                    farbaPozadia, farbaPozadiaGradient, Shader.TileMode.CLAMP);
            // Paint paint = new Paint();
            kresba.setShader(shader);
        } else
            kresba.setColor(farbaPozadia);

        if(h_inner_contour_gradient) {
            shader = new RadialGradient(sirka / 2, vyska_neredukovana / 2, vyska_neredukovana / 4,
                    farbaVnutraObvodovejCiary, farbaVnutraObvodovejCiaryGradient, Shader.TileMode.MIRROR);
            kresba.setShader(shader);
        }
        else
            kresba.setShader(null);


        if (b_top)
            zosuv = 0;    // TOP


        if (b_middle)
            zosuv = vyska_neredukovana - vyska;   // MIDDLE
            // zosuv = getHeight() - vyska;   // MIDDLE


        if (b_bottom)
            zosuv = 2 * (vyska_neredukovana - vyska); // BOTTOM
            // zosuv = 2 * (getHeight() - vyska); // BOTTOM


        if(h_rect_ca) {
            cf.CifernikKU(okraj, polomer, sirka, vyska);
            if (h_continouos)
                cfj.CifernikKU(okraj, polomer, sirka, vyska);
        }

        if(h_rect_cd) {
            cf.CifernikKV(okraj, polomer, sirka, vyska);
            if(h_continouos)
                cfj.CifernikKV(okraj, polomer, sirka, vyska);
        }
        // cf.CifernikKV(okraj, polomer / 2 - 10, sirka, vyska);

        if(h_round) {
            cf.CifernikO(okraj, sirka, vyska);
            if(h_continouos)
                cfj.CifernikO(okraj, sirka, vyska);
        }

        // *********************

        if(h_inner_contour && h_round) {
            kresba.setStrokeWidth(0);
            kresba.setStyle(Paint.Style.FILL);  // kresli obvod, nie vnutro obluka
            // kresba.setStrokeCap(Paint.Cap.ROUND);
            // kresba.setStyle(Paint.Style.STROKE);
            kresba.setColor(farbaVnutraObvodovejCiary);
            platno.drawCircle(sirka / 2,
                    vyska / 2 + zosuv / 2, (min - okraj) / 2 - odsadenieObvodovejCiary, kresba);
        }

        if(h_contour && h_round) {
            kresba.setStrokeWidth(hrubkaObvodovejCiary);
            kresba.setStyle(Paint.Style.STROKE);  // kresli obvod, nie vnutro obluka
            // kresba.setStrokeCap(Paint.Cap.ROUND);
            // kresba.setStyle(Paint.Style.STROKE);

            kresba.setColor(farbaObvodovejCiary);

            platno.drawCircle(sirka / 2,
                    vyska / 2 + zosuv / 2, (min - okraj ) / 2 - odsadenieObvodovejCiary, kresba);
        }

        if(h_inner_contour && !h_round) {  // draw contour
            kresba.setStrokeWidth(0);
            kresba.setStyle(Paint.Style.FILL);

            kresba.setColor(farbaVnutraObvodovejCiary);

            platno.drawRoundRect(cf.point_U_L(okraj + odsadenieObvodovejCiary, 0, sirka, vyska).getX(),
                    cf.point_U_L(okraj + odsadenieObvodovejCiary, polomer + odsadenieObvodovejCiary, sirka, vyska).getY() + zosuv / 2,
                    cf.point_B_R(okraj + odsadenieObvodovejCiary, 0, sirka, vyska).getX(),
                    cf.point_B_R(okraj + odsadenieObvodovejCiary, polomer + odsadenieObvodovejCiary, sirka, vyska).getY() + zosuv / 2,
                    polomer,
                    polomer,
                    kresba
            );
        }

        if(h_contour && !h_round) {  // draw contour
            // obvod cifernika
            kresba.setStyle(Paint.Style.STROKE);
            kresba.setColor(farbaObvodovejCiary);
            kresba.setStrokeWidth(hrubkaObvodovejCiary);
            platno.drawRoundRect(cf.point_U_L(okraj + odsadenieObvodovejCiary, 0, sirka, vyska).getX(),
                    cf.point_U_L(okraj + odsadenieObvodovejCiary, polomer + odsadenieObvodovejCiary, sirka, vyska).getY() + zosuv / 2,
                    cf.point_B_R(okraj + odsadenieObvodovejCiary, 0, sirka, vyska).getX(),
                    cf.point_B_R(okraj + odsadenieObvodovejCiary, polomer + odsadenieObvodovejCiary, sirka, vyska).getY() + zosuv / 2,
                    polomer,
                    polomer,
                    kresba
            );
        }

        if(h_angle && !h_round) {
            kresba.setColor(Color.rgb(80,30,30));
            kresba.setStrokeWidth(2);
            platno.drawLine(cf.point_L_U(okraj, polomer, sirka, vyska).getX(),
                    cf.point_L_U(okraj, polomer, sirka, vyska).getY() + zosuv / 2,
                    cf.point_M_U_L(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_U_L(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            platno.drawLine(cf.point_U_L(okraj, polomer, sirka, vyska).getX(),
                    cf.point_U_L(okraj, polomer, sirka, vyska).getY() + zosuv / 2,
                    cf.point_M_U_L(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_U_L(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_M_U_L(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_U_L(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            platno.drawLine(cf.point_R_U(okraj, polomer, sirka, vyska).getX(),
                    cf.point_R_U(okraj, polomer, sirka, vyska).getY() + zosuv / 2,
                    cf.point_M_U_R(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_U_R(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            platno.drawLine(cf.point_U_R(okraj, polomer, sirka, vyska).getX(),
                    cf.point_U_R(okraj, polomer, sirka, vyska).getY() + zosuv / 2,
                    cf.point_M_U_R(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_U_R(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_M_U_R(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_U_R(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            platno.drawLine(cf.point_R_B(okraj, polomer, sirka, vyska).getX(),
                    cf.point_R_B(okraj, polomer, sirka, vyska).getY() + zosuv / 2,
                    cf.point_M_B_R(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_B_R(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            platno.drawLine(cf.point_B_R(okraj, polomer, sirka, vyska).getX(),
                    cf.point_B_R(okraj, polomer, sirka, vyska).getY() + zosuv / 2,
                    cf.point_M_B_R(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_B_R(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_M_B_R(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_B_R(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            platno.drawLine(cf.point_L_B(okraj, polomer, sirka, vyska).getX(),
                    cf.point_L_B(okraj, polomer, sirka, vyska).getY() + zosuv / 2,
                    cf.point_M_B_L(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_B_L(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            platno.drawLine(cf.point_B_L(okraj, polomer, sirka, vyska).getX(),
                    cf.point_B_L(okraj, polomer, sirka, vyska).getY() + zosuv / 2,
                    cf.point_M_B_L(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_B_L(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_M_B_L(okraj, polomer, sirka, vyska).getX(),
                    cf.point_M_B_L(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            kresba.setColor(Color.rgb(120,10,10));
            // stred zaciatky a horny koniec pravej ciary
            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_R_U(okraj, polomer, sirka, vyska).getX(),
                    cf.point_R_U(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            // stred zaciatky a horny koniec lavej ciary
            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_L_U(okraj, polomer, sirka, vyska).getX(),
                    cf.point_L_U(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            // stred zaciatky a spodny koniec pravej ciary
            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_R_B(okraj, polomer, sirka, vyska).getX(),
                    cf.point_R_B(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            // stred zaciatky a spodny koniec lavej ciary
            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_L_B(okraj, polomer, sirka, vyska).getX(),
                    cf.point_L_B(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            // stred zaciatky a pravy koniec hornej ciary
            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_U_R(okraj, polomer, sirka, vyska).getX(),
                    cf.point_U_R(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            // stred zaciatky a lavy koniec hornej ciary
            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_U_L(okraj, polomer, sirka, vyska).getX(),
                    cf.point_U_L(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            // stred zaciatky a pravy koniec spodej ciary
            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_B_R(okraj, polomer, sirka, vyska).getX(),
                    cf.point_B_R(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

            // stred zaciatky a lavy koniec spodnej ciary
            platno.drawLine(sirka / 2,
                    vyska / 2 + zosuv / 2,
                    cf.point_B_L(okraj, polomer, sirka, vyska).getX(),
                    cf.point_B_L(okraj, polomer, sirka, vyska).getY() + zosuv / 2, kresba);

        }

        if(h_inner_contour_gradient) {
            kresba.setShader(null);
        }
    }

    public void kresliBody(Canvas platno, int posunT_X, int posunT_Y, int farba) {

        int velkostBodky = 0;
        int dlzkaCiarky = 50;

        // *********************** tu uz kresli znacky
        int dlzkaPoZnacku;
        double uholZnacky;
        kresba.setStrokeWidth(10);
        kresba.setColor(farba);

        for(int pocitadlo = 0; pocitadlo <= cf.cislo - 1; pocitadlo++) {

            dlzkaPoZnacku = (int) (cf.body[pocitadlo].dlzka(this.sirka / 2, this.vyska / 2));
            uholZnacky = (double) (cf.body[pocitadlo].uhol(this.sirka / 2, this.vyska / 2));
            if (pocitadlo % 5 == 0) {
                if(h_shadow)
                    kresba.setShadowLayer(6f, 9f,6f, farbaTiena);
                kresba.setStrokeWidth(12);
                dlzkaCiarky = 50;  // to sa odpocitava, je to ako naopak
                velkostBodky = 16;
            } else {
                if (!kazdaPiata) {
                    if(h_shadow)
                        kresba.setShadowLayer(4f, 7f,4f, farbaTiena);
                    kresba.setStrokeWidth(5);
                    dlzkaCiarky = 25;   // zmena 29.8.2021
                    velkostBodky = 6;
                }
            }

            if (b_lines) {
                kresba.setStrokeCap(Paint.Cap.SQUARE);
                if((pocitadlo % 5 == 0) || (pocitadlo % 5 != 0 && !kazdaPiata)) {
                    kresba.setColor(farba);
                    platno.drawLine((float) (sirka / 2 + Math.cos(uholZnacky) * (dlzkaPoZnacku) + posunT_X),
                            (float) (vyska / 2 + Math.sin(uholZnacky) * (dlzkaPoZnacku) + zosuv / 2 + posunT_Y),
                            (float) (sirka / 2 + Math.cos(uholZnacky) * (dlzkaPoZnacku - dlzkaCiarky) + posunT_X),
                            (float) (vyska / 2 + Math.sin(uholZnacky) * (dlzkaPoZnacku - dlzkaCiarky) + zosuv / 2 + posunT_Y), kresba);
                    kresba.setStrokeCap(Paint.Cap.ROUND);
                }
            }

            if (b_circles) {
                if((pocitadlo % 5 == 0) || (pocitadlo % 5 != 0 && !kazdaPiata)) {
                    kresba.setColor(farba);
                    platno.drawCircle(cf.body[pocitadlo].getX() + posunT_X, cf.body[pocitadlo].getY() + zosuv / 2 + posunT_Y, velkostBodky, kresba);
                    // if(inner_round)
                }
            }

            kresba.clearShadowLayer();

            if (h_inner_round && posunT_X == 0) {
                kresba.setStrokeWidth(4);
                // min = Math.min(vyska, sirka);  // kdesi tu hodnotu prepisal tak znovu
                // min = 200;
                // kresba.setColor(Color.rgb(20, 220, 120));
                // farbaOzdobnehoKruhu = Color.rgb(20, 220, 120);
                kresba.setColor(farbaOzdobnehoKruhu);
                platno.drawLine((float) (sirka / 2 + 0.18 * Math.cos(uholZnacky) * (min - okraj) + posunT_X),
                        (float) (vyska / 2 + 0.18 * Math.sin(uholZnacky) * (min - okraj) + zosuv / 2 + posunT_Y),
                        (float) (sirka / 2 + 0.35 * Math.cos(uholZnacky) * (min - okraj) + posunT_X),
                        (float) (vyska / 2 + 0.35 * Math.sin(uholZnacky) * (min - okraj) + zosuv / 2 + posunT_Y), kresba);

                // platno.drawLine((float) (sirka / 2 + 0.19 * Math.cos(uholZnacky) * min + posunT_X),
                //        (float) (vyska / 2 + 0.19 * Math.sin(uholZnacky) * min + zosuv / 2 + posunT_Y),
                //        (float) (sirka / 2 + 0.31 * Math.cos(uholZnacky) * min + posunT_X),
                //        (float) (vyska / 2 + 0.31 * Math.sin(uholZnacky) * min + zosuv / 2 + posunT_Y), kresba);
            }

            if (h_beam && posunT_X == 0) {
                kresba.setStrokeWidth(3);
                // kresba.setColor(Color.rgb(80, 70, 248));
                // farbaOzdobnychLucov = Color.rgb(80, 70, 248);
                kresba.setColor(farbaOzdobnychLucov);
                platno.drawLine((float) (sirka / 2 + 0.1 * Math.cos(uholZnacky) * (dlzkaPoZnacku) + posunT_X),
                        (float) (vyska / 2 + 0.1 * Math.sin(uholZnacky) * (dlzkaPoZnacku) + zosuv / 2 + posunT_Y),
                        (float) (sirka / 2 + Math.cos(uholZnacky) * (dlzkaPoZnacku - dlzkaCiarky - 25) + posunT_X),
                        (float) (vyska / 2 + Math.sin(uholZnacky) * (dlzkaPoZnacku - dlzkaCiarky - 25) + zosuv / 2 + posunT_Y), kresba);
            }
        }

    }

    private void kresliHodinovuRucicku(Canvas platno, int umiestnenie, int posunT_X, int posunT_Y, int farba, int farbaPodkladu, int farbaVysuvnikaRuciciek) {

        Double uhol;
        int polomerRucicky;
        this.farbaVysuvnikaRuciciek = farbaVysuvnikaRuciciek;

        // min = Math.min(vyska - okraj, sirka - okraj);
        // Double uhol = (double) umiestnenie;

        if(h_continouos) {
            uhol = cfj.body[umiestnenie].uhol(sirka >> 1, vyska >> 1);
            // int polomerRucicky = jeHodina ? polomer - skratenieRucicky - skratenieHodinovejRucicky : polomer - skratenieRucicky;
            polomerRucicky = (int) (cfj.body[umiestnenie].dlzka(sirka >> 1, vyska >> 1));
        }
        else {
            uhol = cf.body[umiestnenie].uhol(sirka >> 1, vyska >> 1);
            // int polomerRucicky = jeHodina ? polomer - skratenieRucicky - skratenieHodinovejRucicky : polomer - skratenieRucicky;
            polomerRucicky = (int) (cf.body[umiestnenie].dlzka(sirka >> 1, vyska >> 1));
        }
        kresba.setStrokeCap(Paint.Cap.ROUND);


        if(h_line_constant_length) {
            kresba.setStrokeWidth(26);
            if(h_shadow)
                kresba.setShadowLayer(6f, 9f, 5f, farbaTiena);
            // kresba.setStrokeCap(Paint.Cap.ROUND);
            kresba.setColor(farba);
            /* platno.drawLine((float) (sirka / 2 + posunT_X),
                    (float) (vyska / 2 + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.7 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.7 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba); */

            platno.drawLine((float) (sirka / 2 - 0.02 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 - 0.02 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.4 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.4 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);
        }

        if(h_mechanic) {

            kresba.setStrokeWidth(3);    // 10
            kresba.setStrokeCap(Paint.Cap.ROUND);
            kresba.setColor(farba);
            if(h_shadow)
                kresba.setShadowLayer(6f, 9f, 5f, farbaTiena);
            /* platno.drawLine((float) (sirka / 2 + 0.75 * Math.cos(uhol) * (polomerRucicky - min * 0.4) + posunT_X),
                    (float) (vyska / 2 + 0.75 * Math.sin(uhol) * (polomerRucicky - min * 0.4) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.75 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.75 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba); */

            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.46) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.46) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.85 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.85 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setStrokeWidth(26);
            kresba.setColor(farba);

            /* platno.drawLine((float) (sirka / 2 + posunT_X), (float) (vyska / 2 + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.3 * Math.cos(uhol) * min + posunT_X),
                    (float) (vyska / 2 + 0.3 * Math.sin(uhol) * min + zosuv / 2 + posunT_Y),
                    kresba); */

            platno.drawLine((float) (sirka / 2 - 0.02 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 - 0.02 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.4 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.4 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setStrokeWidth(3);   // 10
            kresba.setColor(farbaPodkladu);
            kresba.clearShadowLayer();
            /* platno.drawLine((float) (sirka / 2 + 0.03 * Math.cos(uhol) * min + posunT_X),
                    (float) (vyska / 2 + 0.03 * Math.sin(uhol) * min + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.3 * Math.cos(uhol) * min + posunT_X),
                    (float) (vyska / 2 + 0.3 * Math.sin(uhol) * min + zosuv / 2 + posunT_Y),
                    kresba); */

            platno.drawLine((float) (sirka / 2 + 0.03 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.03 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.4 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.4 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setColor(farbaVysuvnikaRuciciek);
            /* platno.drawLine((float) (sirka / 2 + 0.75 * Math.cos(uhol) * (polomerRucicky - min * 0.4) + posunT_X),
                    (float) (vyska / 2 + 0.75 * Math.sin(uhol) * (polomerRucicky - min * 0.4) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.75 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.75 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba); */

            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.46) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.46) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.85 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.85 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

            // kruzok na konci tiahla
            platno.drawCircle((float) (sirka / 2 + 0.85 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 +  0.85 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y), 3, kresba);

        }

        if(h_mechanic_full) {

            kresba.setStrokeWidth(3);    // 10
            kresba.setStrokeCap(Paint.Cap.ROUND);
            kresba.setColor(farba);
            if(h_shadow)
                kresba.setShadowLayer(6f, 9f, 5f, farbaTiena);

                /* platno.drawLine((float) (sirka / 2 + 0.75 * Math.cos(uhol) * (polomerRucicky - min * 0.4) + posunT_X),
                        (float) (vyska / 2 + 0.75 * Math.sin(uhol) * (polomerRucicky - min * 0.4) + zosuv / 2 + posunT_Y),
                        (float) (sirka / 2 + 0.75 * Math.cos(uhol) * polomerRucicky + posunT_X),
                        (float) (vyska / 2 + 0.75 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                        kresba); */

            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.5) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.5) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.85 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.85 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setStrokeWidth(26);
            kresba.setColor(farba);

                /* platno.drawLine((float) (sirka / 2 + posunT_X), (float) (vyska / 2 + zosuv / 2 + posunT_Y),
                        (float) (sirka / 2 + 0.3 * Math.cos(uhol) * min + posunT_X),
                        (float) (vyska / 2 + 0.3 * Math.sin(uhol) * min + zosuv / 2 + posunT_Y),
                        kresba); */

            platno.drawLine((float) (sirka / 2 - 0.02 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 - 0.02 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.4 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.4 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setStrokeWidth(3);   // 10
            kresba.clearShadowLayer();
            kresba.setColor(farbaVysuvnikaRuciciek);

                /* platno.drawLine((float) (sirka / 2 + 0.75 * Math.cos(uhol) * (polomerRucicky - min * 0.4) + posunT_X),
                        (float) (vyska / 2 + 0.75 * Math.sin(uhol) * (polomerRucicky - min * 0.4) + zosuv / 2 + posunT_Y),
                        (float) (sirka / 2 + 0.75 * Math.cos(uhol) * polomerRucicky + posunT_X),
                        (float) (vyska / 2 + 0.75 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                        kresba);  */

            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.5) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.5) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.85 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.85 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

            // kruzok na konci tiahla
            platno.drawCircle((float) (sirka / 2 + 0.85 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 +  0.85 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y), 3, kresba);

        }
    }

    private void kresliMinutovuRucicku(Canvas platno, int umiestnenie, int posunT_X, int posunT_Y, int farba, int farbaPodkladu, int farbaVysuvnikaRuciciek) {
        Double uhol;
        int polomerRucicky;
        this.farbaVysuvnikaRuciciek = farbaVysuvnikaRuciciek;
        min = Math.min(vyska - okraj, sirka - okraj);

        // kresba.setStrokeCap(Paint.Cap.SQUARE);
        // Double uhol = Math.PI * umiestnenie / 30 - Math.PI / 2;
        if(h_continouos) {
            uhol = cfj.body[umiestnenie].uhol((float) sirka / 2.0f, (float) (vyska / 2.0f));
            polomerRucicky = (int) (cfj.body[umiestnenie].dlzka((float) sirka / 2.0f, (float) (vyska / 2.0f)));
        }
        else {
            uhol = cf.body[umiestnenie].uhol(sirka / 2.0f, vyska / 2.0f);
            polomerRucicky = (int) (cf.body[umiestnenie].dlzka(sirka / 2.0f, (vyska) / 2.0f));
        }

        /*if(h_line) {
            kresba.setStrokeWidth(14);
            kresba.setColor(farba);
            if(h_shadow)
                kresba.setShadowLayer(8f, 10f,6f, farbaTiena);
            platno.drawLine((float) (sirka / 2 + posunT_X),
                    (float) (vyska / 2 + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.9 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.9 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);
            // sem nakopiruj
            *//* platno.drawLine((float) (sirka / 2 + posunT_X),
                    (float) (vyska / 2 + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.45 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.45 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba); *//*

            kresba.clearShadowLayer();
        }*/

        if(h_line_constant_length) {
            kresba.setStrokeWidth(14);
            kresba.setColor(farba);
            if(h_shadow)
                kresba.setShadowLayer(8f, 10f,6f, farbaTiena);
            /* platno.drawLine((float) (sirka / 2 + posunT_X),
                    (float) (vyska / 2 + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.9 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.9 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);  */
            // sem nakopiruj
            platno.drawLine((float) (sirka / 2 - 0.03 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 - 0.03 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.475 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.475 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.clearShadowLayer();
        }

        if(h_mechanic) {
            kresba.setStrokeWidth(3);   // 10
            kresba.setStrokeCap(Paint.Cap.ROUND);
            kresba.setColor(farbaVysuvnikaRuciciek);
            if(h_shadow)
                kresba.setShadowLayer(8f, 10f,6f, farbaTiena);
            /* platno.drawLine((float) (sirka / 2 + 0.9 * Math.cos(uhol) * (polomerRucicky - min * 0.4) + posunT_X),
                    (float) (vyska / 2 + 0.9 * Math.sin(uhol) * (polomerRucicky - min * 0.4) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.9 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.9 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);  */
            // tu
            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.46) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.46) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.96 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.96 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setStrokeWidth(14);
            kresba.setColor(farba);
            /* platno.drawLine(sirka / 2 + posunT_X, vyska / 2 + zosuv / 2 + posunT_Y,
                    (float) (sirka / 2 + 0.38 * Math.cos(uhol) * min + posunT_X),
                    (float) (vyska / 2 + 0.38 * Math.sin(uhol) * min + zosuv / 2 + posunT_Y),
                    kresba); */
            // sem
            platno.drawLine((float) (sirka / 2 - 0.03 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 - 0.03 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.475 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.475 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setStrokeWidth(3);  //10
            kresba.setColor(farbaPodkladu);
            kresba.clearShadowLayer();

            /* platno.drawLine((float) (sirka / 2 + 0.03 * Math.cos(uhol) * min + posunT_X),
                    (float) (vyska / 2 + 0.03 * Math.sin(uhol) * min + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.38 * Math.cos(uhol) * min + posunT_X),
                    (float) (vyska / 2 + 0.38 * Math.sin(uhol) * min + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setColor(farba);
            platno.drawLine((float) (sirka / 2 + 0.9 * Math.cos(uhol) * (polomerRucicky - min * 0.4) + posunT_X),
                    (float) (vyska / 2 + 0.9 * Math.sin(uhol) * (polomerRucicky - min * 0.4) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.9 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.9 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);  */
            // sem
            platno.drawLine((float) (sirka / 2 + 0.03 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.03 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.475 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.475 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);

            // zopakovat predlzenie bez tiena
            kresba.setColor(farbaVysuvnikaRuciciek);

            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.46) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.46) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.96 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.96 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

            // kruzok na konci tiahla
            platno.drawCircle((float) (sirka / 2 + 0.96 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 +  0.96 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y), 3, kresba);

        }

        if(h_mechanic_full) {
            kresba.setStrokeWidth(3);   // 10
            kresba.setStrokeCap(Paint.Cap.ROUND);
            kresba.setColor(farbaVysuvnikaRuciciek);
            if(h_shadow)
                kresba.setShadowLayer(8f, 10f,6f, farbaTiena);
            /* platno.drawLine((float) (sirka / 2 + 0.9 * Math.cos(uhol) * (polomerRucicky - min * 0.4) + posunT_X),
                    (float) (vyska / 2 + 0.9 * Math.sin(uhol) * (polomerRucicky - min * 0.4) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.9 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.9 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba); */

            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.46) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.46) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.96 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.96 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);


            kresba.setStrokeWidth(14);
            kresba.setColor(farba);
            /* platno.drawLine(sirka / 2 + posunT_X, vyska / 2 + zosuv / 2 + posunT_Y,
                    (float) (sirka / 2 + 0.38 * Math.cos(uhol) * min + posunT_X),
                    (float) (vyska / 2 + 0.38 * Math.sin(uhol) * min + zosuv / 2 + posunT_Y),
                    kresba); */

            platno.drawLine((float) (sirka / 2 - 0.03 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 - 0.03 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.475 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.475 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setStrokeWidth(3);  //10
            kresba.clearShadowLayer();
            kresba.setColor(farbaVysuvnikaRuciciek);
            /* platno.drawLine((float) (sirka / 2 + 0.9 * Math.cos(uhol) * (polomerRucicky - min * 0.4) + posunT_X),
                    (float) (vyska / 2 + 0.9 * Math.sin(uhol) * (polomerRucicky - min * 0.4) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.9 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.9 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);  */

            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.46) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.46) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.96 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.96 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

            // kruzok na konci tiahla
            platno.drawCircle((float) (sirka / 2 + 0.96 *  Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.96 *  Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y), 3, kresba);
        }

    }

    private void kresliSekundovuRucicku(Canvas platno, int umiestnenie, int posunT_X, int posunT_Y, int farba, int farbaPodkladu, int farbaVysuvnikaSekundovejRucicky) {

        Double uhol;
        int polomerRucicky;
        this.farbaVysuvnikaSekundovejRucicky = farbaVysuvnikaSekundovejRucicky;

        int min = Math.min(vyska - okraj, sirka - okraj);

        if(h_continouos) {
            uhol = cfj.body[umiestnenie].uhol(sirka / 2, vyska / 2);
            polomerRucicky = (int) (cfj.body[umiestnenie].dlzka(sirka / 2, vyska / 2));
        }
        else {
            uhol = cf.body[umiestnenie].uhol(sirka / 2, vyska / 2);
            polomerRucicky = (int) (cf.body[umiestnenie].dlzka(sirka / 2, vyska / 2));
        }

        kresba.setStrokeCap(Paint.Cap.ROUND);

        // kresba.setShadowLayer(6f, 4f,8f, Color.GRAY);

        // kresba.setStrokeWidth(18);
       /* kresba.setColor(getResources().getColor(android.R.color.darker_gray));
        platno.drawLine(sirka / 2 + 14, vyska / 2 + 3,
                (float) (sirka / 2 + 0.9 * Math.cos(uhol) * polomerRucicky + 14),
                (float) (vyska / 2 + 0.9 * Math.sin(uhol) * polomerRucicky + 3),
                kresba); */

       /* if(h_line) {
            kresba.setStrokeWidth(7);
            kresba.setColor(farba);
            if(h_shadow)
                kresba.setShadowLayer(8f, 10f,6f, farbaTiena);

            platno.drawLine((float) (sirka / 2 + posunT_X),
                    (float) (vyska / 2 + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.9 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.9 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

        }*/

        if(h_line_constant_length) {

            if(h_shadow)
                kresba.setShadowLayer(10f, 12f,8f, farbaTiena);

            kresba.setStrokeWidth(7);
            kresba.setColor(farba);
            /* platno.drawLine((float) (sirka / 2 + posunT_X), (float) (vyska / 2 + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.38 * Math.cos(uhol) * min + posunT_X),
                    (float) (vyska / 2 + 0.38 * Math.sin(uhol) * min + zosuv / 2 + posunT_Y),
                    kresba);  */

            platno.drawLine((float) (sirka / 2 - 0.06 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 - 0.06 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.47 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.47 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);

        }

        if(h_mechanic) {

            kresba.setStrokeWidth(3);
            kresba.setColor(farbaVysuvnikaSekundovejRucicky);
            if(h_shadow)
                kresba.setShadowLayer(10f, 12f,8f, farbaTiena);

            /* platno.drawLine((float) (sirka / 2 + 0.92 * Math.cos(uhol) * (polomerRucicky - min * 0.4) + posunT_X),
                    (float) (vyska / 2 + 0.92 * Math.sin(uhol) * (polomerRucicky - min * 0.4) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.92 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.92 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba); */

            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.46) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.46) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.96 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.96 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setStrokeWidth(7);
            kresba.setColor(farba);
            /* platno.drawLine((float) (sirka / 2 + posunT_X), (float) (vyska / 2 + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.38 * Math.cos(uhol) * min + posunT_X),
                    (float) (vyska / 2 + 0.38 * Math.sin(uhol) * min + zosuv / 2 + posunT_Y),
                    kresba); */

            platno.drawLine((float) (sirka / 2 - 0.06 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 - 0.06 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.48 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.48 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setStrokeWidth(3);
            kresba.setColor(farbaPodkladu);
            kresba.clearShadowLayer();
            platno.drawLine((float) (sirka / 2 + 0.03 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.03 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.48 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.48 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);

            // zopakovat predlzenie bez tiena
            kresba.setColor(farbaVysuvnikaSekundovejRucicky);

            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.46) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.46) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.96 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.96 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

            // kruzok na konci tiahla
            platno.drawCircle((float) (sirka / 2 + 0.96 *  Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 +  0.96 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y), 3, kresba);
        }

        if(h_mechanic_full) {   // plna

            kresba.setStrokeWidth(3);
            kresba.setColor(farbaVysuvnikaSekundovejRucicky);
            if(h_shadow)
                kresba.setShadowLayer(10f, 12f,8f, farbaTiena);

            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.46) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.46) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.96 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.96 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setStrokeWidth(7);
            kresba.setColor(farba);

            platno.drawLine((float) (sirka / 2 - 0.06 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 - 0.06 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.48 * Math.cos(uhol) * (min - okraj) + posunT_X),
                    (float) (vyska / 2 + 0.48 * Math.sin(uhol) * (min - okraj) + zosuv / 2 + posunT_Y),
                    kresba);

            kresba.setStrokeWidth(3);
            kresba.setColor(farbaVysuvnikaSekundovejRucicky);
            kresba.clearShadowLayer();

            platno.drawLine((float) (sirka / 2 + Math.cos(uhol) * (polomerRucicky - (min - okraj) * 0.46) + posunT_X),
                    (float) (vyska / 2 + Math.sin(uhol) * (polomerRucicky - (min - okraj) * 0.46) + zosuv / 2 + posunT_Y),
                    (float) (sirka / 2 + 0.96 * Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.96 * Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y),
                    kresba);

            // kruzok na konci tiahla
            platno.drawCircle((float) (sirka / 2 + 0.96 *  Math.cos(uhol) * polomerRucicky + posunT_X),
                    (float) (vyska / 2 + 0.96 *  Math.sin(uhol) * polomerRucicky + zosuv / 2 + posunT_Y), 3, kresba);
        }
    }

    public void kresliRucicky(Canvas platno) {
        Calendar c = Calendar.getInstance();

        float hodina = c.get(Calendar.HOUR_OF_DAY);
        hodina = hodina >= 12 ? hodina - 12 : hodina;

        if(h_continouos) { //
            kresliStred(platno, 0, 0, 24, farbaRuciciek);
            kresliHodinovuRucicku(platno, (int) (hodina * 5 * 25 + c.get(Calendar.MINUTE) * 25 / 12), 0, 0, farbaRuciciek, farbaVnutraRuciciek, farbaVysuvnikaRuciciek);
            kresliMinutovuRucicku(platno, (int) (c.get(Calendar.MINUTE) * 25 + c.get(Calendar.SECOND) * 25 / 60), 0, 0, farbaRuciciek, farbaVnutraRuciciek, farbaVysuvnikaRuciciek);
            kresliSekundovuRucicku(platno, c.get(Calendar.SECOND) * 25 + c.get(Calendar.MILLISECOND) / 40, 0, 0, farbaSekundovejRucicky, farbaVnutraSekundovejRucicky, farbaVysuvnikaSekundovejRucicky);
            kresliStred(platno, 0, 0, 18, farbaSekundovejRucicky);
        }
        // kresliSekundovuRucicku(platno, c.get(Calendar.SECOND) * 100 + c.get(Calendar.MILLISECOND) / 10, 0, 0, farbaRuciciek, farbaPozadia);
        else {
            kresliStred(platno, 0, 0, 24, farbaRuciciek);
            kresliHodinovuRucicku(platno, (int)(hodina * 5 + c.get(Calendar.MINUTE) / 12), 0, 0, farbaRuciciek, farbaVnutraRuciciek, farbaVysuvnikaRuciciek);
            kresliMinutovuRucicku(platno, c.get(Calendar.MINUTE), 0, 0, farbaRuciciek, farbaVnutraRuciciek, farbaVysuvnikaRuciciek);
            kresliSekundovuRucicku(platno, c.get(Calendar.SECOND), 0, 0, farbaSekundovejRucicky, farbaVnutraSekundovejRucicky, farbaVysuvnikaSekundovejRucicky);
            kresliStred(platno, 0, 0, 18, farbaSekundovejRucicky);
        }

    }

    private void kresliCisla(Canvas platno, String[] cisla, int posunT_X, int posunT_Y, int farba) {

        int dlzkaPoZnacku;
        double uholZnacky;

        kresba.setTextSize(velkostPisma);
        kresba.setColor(farba);
        kresba.setStrokeWidth(18);
        kresba.setStyle(Paint.Style.FILL);
        kresba.setDither(true);
        kresba.setAntiAlias(true);

        if(h_shadow)
            kresba.setShadowLayer(4f, 7f,4f, farbaTiena);

        for(int cislo = 1; cislo <= cisla.length; cislo++) {
            // System.out.println(" cislo " + cislo + " sirka " + (sirka >> 1) + " vyska " + (vyska >> 1) + " cisla.length " + cisla.length);
            dlzkaPoZnacku = (int) (cf.body[(cislo - 1) * 5].dlzka(sirka >> 1, vyska >> 1));
            uholZnacky = (double) (cf.body[(cislo - 1) * 5].uhol(sirka >> 1, vyska >> 1));
            // String docasny = String.valueOf(cislo - 1);

            // kresba.getTextBounds(docasny, 0, docasny.length(), pravouholnik);
            kresba.getTextBounds(cisla[cislo - 1], 0, cisla[cislo - 1].length(), pravouholnik);

            // Double uhol = Math.PI / 6 * (cislo - 3);
            // int x = (int) (sirka / 2 + Math.cos(uhol) * polomer - pravouholnik.width() / 2);
            // int y = (int) (vyska / 2 + Math.sin(uhol) * polomer + pravouholnik.height() / 2);

            int x = (int) (((sirka >> 1) + Math.cos(uholZnacky) * (dlzkaPoZnacku - 100)) - (pravouholnik.width() >> 1));
            int y = (int) (((vyska >> 1) + Math.sin(uholZnacky) * (dlzkaPoZnacku - 100)) + (pravouholnik.height() >> 1));

            // platno.drawText(docasny, x, y + zosuv / 2, kresba);
            platno.drawText(cisla[cislo - 1], x + posunT_X, y + (zosuv >> 1) + posunT_Y, kresba);
        }
    }

    private void kresliStred(Canvas platno, int posunT_X, int posunT_Y, int priemer,int farba) {
        kresba.setStyle(Paint.Style.FILL);
        kresba.setColor(farba);
        platno.drawCircle(sirka / 2.0f + posunT_X, vyska / 2.0f + zosuv / 2.0f + posunT_Y,priemer, kresba);
    }

    void nastavUvodneHodnoty() {

        // ponaho.farbaPozadia = Color.rgb(20,50,10);

        farbaPozadia = ResourcesCompat.getColor(getResources(), R.color.farbaPozadia, null);

        // farbaPozadia = Color.TRANSPARENT;
        mojaUvodnaFarba = farbaPozadia;
        farbaRuciciek = ResourcesCompat.getColor(getResources(), R.color.farbaRuciciek, null);

        farbaVysuvnikaRuciciek = ResourcesCompat.getColor(getResources(), R.color.farbaVysuvnikaRuciciek, null);


        farbaPozadiaGradient = ResourcesCompat.getColor(getResources(), R.color.farbaPozadiaGradient, null);

        // farbaVnutraRuciciek = farbaPozadia;
        farbaZnaciek = ResourcesCompat.getColor(getResources(), R.color.farbaZnaciek, null);
//                Color.WHITE;
        farbaCisel = ResourcesCompat.getColor(getResources(), R.color.farbaCisel, null);  // Color.WHITE;
        farbaTiena = ResourcesCompat.getColor(getResources(), R.color.farbaTiena, null);  // Color.DKGRAY;
        farbaObvodovejCiary = ResourcesCompat.getColor(getResources(), R.color.farbaObvodovejCiary, null);  // Color.rgb(240,240,210);

        farbaVnutraObvodovejCiary = ResourcesCompat.getColor(getResources(), R.color.farbaVnutraObvodovejCiary, null);  // Color.rgb(5, 20, 20);
        farbaVnutraRuciciek = ResourcesCompat.getColor(getResources(), R.color.farbaVnutraRuciciek, null);  // ponaho.farbaVnutraObvodovejCiary;
        farbaVnutraObvodovejCiaryGradient = ResourcesCompat.getColor(getResources(), R.color.farbaVnutraObvodovejCiaryGradient, null); // ponaho.farbaRuciciek;
        farbaSekundovejRucicky = ResourcesCompat.getColor(getResources(), R.color.farbaSekundovejRucicky, null);  // Color.rgb(255,40,30);

        farbaVnutraSekundovejRucicky = ResourcesCompat.getColor(getResources(), R.color.farbaVnutraSekundovejRucicky, null);

        farbaVysuvnikaSekundovejRucicky = ResourcesCompat.getColor(getResources(), R.color.farbaVysuvnikaSekundovejRucicky, null);

        farbaOzdobnehoKruhu = ResourcesCompat.getColor(getResources(), R.color.farbaOzdobnehoKruhu, null);  // Color.rgb(20, 60, 70);
        farbaOzdobnychLucov = ResourcesCompat.getColor(getResources(), R.color.farbaOzdobnychLucov, null); // Color.rgb(50, 40, 78);

        mojPolomer = getResources().getString(R.string.radius);  // "100"; // uvodne nastavenie polomeru
        mojTextOdsad = getResources().getString(R.string.odsad);  //  "200"; // okraj

        mojTextZosuv = getResources().getString(R.string.zosuv); //  "55"; // prisposobenie vysky
        mojTextPoloha = getResources().getString(R.string.poloha);  //  "MIDDLE";

        mojaHrubkaObvodovejCiary = getResources().getString(R.string.hrubkaobvodovejciary); // "48";
        mojeOdsadenieObvodovejCiary = getResources().getString(R.string.odsadenieobvodovejciary);  // "-24";

        // poloha cifernika
        b_top = getResources().getBoolean(R.bool.b_top);  //  false;
        b_middle = getResources().getBoolean(R.bool.b_middle);  //  true;
        b_bottom = getResources().getBoolean(R.bool.b_bottom);  //  false;

        // tvar ciferniku
        h_round = getResources().getBoolean(R.bool.h_round); // false;    // okruhly
        h_rect_cd = getResources().getBoolean(R.bool.h_rect_cd);   // true;  // pravouhly konst vzdialenost bodov po obvode
        h_rect_ca = getResources().getBoolean(R.bool.h_rect_ca); // false;  // pravouhly konst uhol chodu rucicky
        h_shadow = getResources().getBoolean(R.bool.h_shadow);  //  true;

        h_line_constant_length = getResources().getBoolean(R.bool.h_line_constant_length);  // false;
        h_mechanic = getResources().getBoolean(R.bool.h_mechanic);  //  true;
        h_mechanic_full = getResources().getBoolean(R.bool.h_mechanic_full);  //  false;
        // beh rucicky
        h_continouos = getResources().getBoolean(R.bool.h_continouos);  //  true;
        h_step = getResources().getBoolean(R.bool.h_step);  //  false;
        // tvar obvodovych znaciek
        b_lines = getResources().getBoolean(R.bool.b_lines);  //  true;
        b_circles = getResources().getBoolean(R.bool.b_circles);  //  false;
        kazdaPiata = getResources().getBoolean(R.bool.kazdaPiata);  // true;
        // cisla po obvode
        b_arabic_numbers = getResources().getBoolean(R.bool.b_arabic_numbers);  // true;
        b_roman_numbers = getResources().getBoolean(R.bool.b_roman_numbers);  //  false;
        b_none = getResources().getBoolean(R.bool.b_none);  //  false;
        // pomocne ciary
        h_inner_round = getResources().getBoolean(R.bool.inner_round);   //  false;
        h_beam = getResources().getBoolean(R.bool.beam);  // false;
        h_contour = getResources().getBoolean(R.bool.contour);  // false;
        h_inner_contour = getResources().getBoolean(R.bool.innercontour);  // true;
        h_angle = getResources().getBoolean(R.bool.angle);  //  false;

        h_background_gradient = getResources().getBoolean(R.bool.h_background_gradient);  //  true;
        h_inner_contour_gradient = getResources().getBoolean(R.bool.h_inner_contour_gradient);  //  true;
    }

    void ulozDoSharedPreferences(String nazovOdkladaciehoSuboru) {

        SharedPreferences sp = getContext().getSharedPreferences(nazovOdkladaciehoSuboru, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean("TOP", this.b_top);
        editor.putBoolean("MIDDLE", this.b_middle);
        editor.putBoolean("BOTTOM", this.b_bottom);

        // editor.putBoolean("LINE", this.h_line);
        editor.putBoolean("LINECONSTANTLENGTH", this.h_line_constant_length);

        editor.putBoolean("MECHANIC", this.h_mechanic);
        editor.putBoolean("MECHANICFULL", this.h_mechanic_full);

        editor.putBoolean("CONTINOUOS", this.h_continouos);
        editor.putBoolean("STEP", this.h_step);

        editor.putBoolean("ROUND", this.h_round);
        editor.putBoolean("RECT_CD", this.h_rect_cd);
        editor.putBoolean("RECT_CA", this.h_rect_ca);
        editor.putBoolean("SHADOW", this.h_shadow);

        // znacky po obvode
        editor.putBoolean("LINES", this.b_lines);
        editor.putBoolean("CIRCLES", this.b_circles);
        editor.putBoolean("EVERYFIFTH", this.kazdaPiata);
        // cisla po obvode
        editor.putBoolean("ARABIC", this.b_arabic_numbers);
        editor.putBoolean("ROMAN", this.b_roman_numbers);
        editor.putBoolean("NONE", this.b_none);
        // pomocne ciary
        editor.putBoolean("INNER_ROUND", this.h_inner_round);

        editor.putBoolean("BEAM", this.h_beam);
        editor.putBoolean("CONTOUR", this.h_contour);
        editor.putBoolean("INNERCONTOUR", this.h_inner_contour);
        editor.putBoolean("ANGLE", this.h_angle);

        editor.putBoolean("BACKGROUNDGRADIENT", this.h_background_gradient);
        editor.putBoolean("INNERROUNDGRADIENT", this.h_inner_contour_gradient);

        editor.putString("RADIUS", this.mojPolomer);
        editor.putString("ODSAD", this.mojTextOdsad);
        editor.putString("ZOSUV", this.mojTextZosuv);
        editor.putString("POLOHA", this.mojTextPoloha);
        editor.putString("HRUBKAOBVODOVEJCIARY", this.mojaHrubkaObvodovejCiary);
        editor.putString("ODSADENIEOBVODOVEJCIARY", this.mojeOdsadenieObvodovejCiary);

        editor.putInt("FARBAPOZADIA", this.farbaPozadia);

        // editor.putInt("FARBAPOZADIA", ResourcesCompat.getColor(getResources(), R.color.farbaPozadia, null));

        editor.putInt("FARBAPOZADIAGRADIENT", this.farbaPozadiaGradient);

        // editor.putInt("FARBAPOZADIAGRADIENT", ResourcesCompat.getColor(getResources(), R.color.farbaPozadiaGradient, null));

        editor.putInt("FARBARUCICIEK", this.farbaRuciciek);
        editor.putInt("FARBAVNUTRARUCICIEK", this.farbaVnutraRuciciek);

        editor.putInt("FARBAVYSUVNIKARUCICIEK", this.farbaVysuvnikaRuciciek);

        editor.putInt("FARBAZNACIEK", this.farbaZnaciek);
        editor.putInt("FARBACISEL", this.farbaCisel);
        editor.putInt("FARBATIENA", this.farbaTiena);
        editor.putInt("FARBAOBVODOVEJCIARY", this.farbaObvodovejCiary);
        editor.putInt("FARBAVNUTRAOBVODOVEJCIARY", this.farbaVnutraObvodovejCiary);
        editor.putInt("FARBAVNUTRAOBVODOVEJCIARYGRADIENT", this.farbaVnutraObvodovejCiaryGradient);
        editor.putInt("FARBASEKUNDOVEJRUCICKY", this.farbaSekundovejRucicky);

        editor.putInt("FARBAVNUTRASEKUNDOVEJRUCICKY", this.farbaVnutraSekundovejRucicky);

        editor.putInt("FARBAVYSUVNIKASEKUNDOVEJRUCICKY", this.farbaVysuvnikaSekundovejRucicky);

        editor.putInt("FARBAOZDOBNEHOKRUHU", this.farbaOzdobnehoKruhu);
        editor.putInt("FARBAOZDOBNYCHLUCOV", this.farbaOzdobnychLucov);

        editor.apply();

//        if(nazovOdkladaciehoSuboru == "MojeUserPrefs")
//            Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
    }

    void vyberZoSharedPreferences(String nazovOdkladaciehoSuboru) {

        SharedPreferences sp = getContext().getSharedPreferences(nazovOdkladaciehoSuboru, Context.MODE_PRIVATE);
        // if (sp != null) {
        // cifernik
        this.b_top = sp.getBoolean("TOP", getResources().getBoolean(R.bool.b_top));  //  false);
        this.b_middle = sp.getBoolean("MIDDLE", getResources().getBoolean(R.bool.b_middle));  //  true);
        this.b_bottom = sp.getBoolean("BOTTOM", getResources().getBoolean(R.bool.b_bottom));  //  false);

        // this.h_line = sp.getBoolean("LINE", false);
        this.h_line_constant_length = sp.getBoolean("LINECONSTANTLENGTH", getResources().getBoolean(R.bool.h_line_constant_length));  //  false);
        this.h_mechanic = sp.getBoolean("MECHANIC", getResources().getBoolean(R.bool.h_mechanic));  // true);
        this.h_mechanic_full = sp.getBoolean("MECHANICFULL", getResources().getBoolean(R.bool.h_mechanic_full));  //  false);

        this.h_continouos = sp.getBoolean("CONTINOUOS", getResources().getBoolean(R.bool.h_continouos));  // true);
        this.h_step = sp.getBoolean("STEP", getResources().getBoolean(R.bool.h_step));  //  false);

        this.h_round = sp.getBoolean("ROUND", getResources().getBoolean(R.bool.h_round));  //  false);
        this.h_rect_cd = sp.getBoolean("RECT_CD", getResources().getBoolean(R.bool.h_rect_cd));  //  true);
        this.h_rect_ca = sp.getBoolean("RECT_CA", getResources().getBoolean(R.bool.h_rect_ca));  //  false);
        this.h_shadow = sp.getBoolean("SHADOW", getResources().getBoolean(R.bool.h_shadow));  //  false);

        this.b_lines = sp.getBoolean("LINES", getResources().getBoolean(R.bool.b_lines));  //  true);
        this.b_circles = sp.getBoolean("CIRCLES", getResources().getBoolean(R.bool.b_circles));  //  false);
        this.kazdaPiata = sp.getBoolean("EVERYFIFTH", getResources().getBoolean(R.bool.kazdaPiata));  //  false);

        this.b_arabic_numbers = sp.getBoolean("ARABIC", getResources().getBoolean(R.bool.b_arabic_numbers));  //  true);
        this.b_roman_numbers = sp.getBoolean("ROMAN", getResources().getBoolean(R.bool.b_roman_numbers));  //  false);
        this.b_none = sp.getBoolean("NONE", getResources().getBoolean(R.bool.b_none));  //  false);

        this.h_inner_round = sp.getBoolean("INNER_ROUND", getResources().getBoolean(R.bool.inner_round));  //   false);
        this.h_beam = sp.getBoolean("BEAM", false);
        this.h_contour = sp.getBoolean("CONTOUR", getResources().getBoolean(R.bool.contour));   //  false);
        this.h_inner_contour = sp.getBoolean("INNERCONTOUR", getResources().getBoolean(R.bool.innercontour));   //  true);
        this.h_angle = sp.getBoolean("ANGLE", getResources().getBoolean(R.bool.angle));   //  false);

        this.h_background_gradient = sp.getBoolean("BACKGROUNDGRADIENT", true);
        this.h_inner_contour_gradient = sp.getBoolean("INNERROUNDGRADIENT", true);

        this.mojPolomer = sp.getString("RADIUS", getResources().getString(R.string.radius));   // "150");

        this.mojTextOdsad = sp.getString("ODSAD", getResources().getString(R.string.odsad));  // "120");

        this.mojTextZosuv = sp.getString("ZOSUV", getResources().getString(R.string.zosuv));
        this.mojTextPoloha = sp.getString("POLOHA", getResources().getString(R.string.poloha));  // "MIDDLE");

        this.mojaHrubkaObvodovejCiary = sp.getString("HRUBKAOBVODOVEJCIARY", getResources().getString(R.string.hrubkaobvodovejciary));  // "48");
        this.mojeOdsadenieObvodovejCiary = sp.getString("ODSADENIEOBVODOVEJCIARY", getResources().getString(R.string.odsadenieobvodovejciary));  //  "-24");

        this.farbaPozadia = sp.getInt("FARBAPOZADIA",  ResourcesCompat.getColor(getResources(), R.color.farbaPozadia, null));

        this.farbaPozadiaGradient = sp.getInt("FARBAPOZADIAGRADIENT", ResourcesCompat.getColor(getResources(), R.color.farbaPozadiaGradient, null));

        mojaUvodnaFarba = this.farbaPozadia;
        this.farbaRuciciek = sp.getInt("FARBARUCICIEK", ResourcesCompat.getColor(getResources(), R.color.farbaRuciciek, null));

        this.farbaVysuvnikaRuciciek = sp.getInt("FARBAVYSUVNIKARUCICIEK", ResourcesCompat.getColor(getResources(), R.color.farbaVysuvnikaRuciciek, null));

        this.farbaVnutraRuciciek = sp.getInt("FARBAVNUTRARUCICIEK", ResourcesCompat.getColor(getResources(), R.color.farbaVnutraRuciciek, null)); //this.farbaPozadia);
        this.farbaZnaciek = sp.getInt("FARBAZNACIEK", ResourcesCompat.getColor(getResources(), R.color.farbaZnaciek, null));  // Color.WHITE);
        this.farbaCisel = sp.getInt("FARBACISEL", ResourcesCompat.getColor(getResources(), R.color.farbaCisel, null)); // Color.WHITE);
        this.farbaTiena = sp.getInt("FARBATIENA", ResourcesCompat.getColor(getResources(), R.color.farbaTiena, null));  //  Color.GRAY);
        this.farbaObvodovejCiary = sp.getInt("FARBAOBVODOVEJCIARY", ResourcesCompat.getColor(getResources(), R.color.farbaObvodovejCiary, null));  // Color.rgb(80,100,60));

        this.farbaVnutraObvodovejCiary = sp.getInt("FARBAVNUTRAOBVODOVEJCIARY", ResourcesCompat.getColor(getResources(), R.color.farbaVnutraObvodovejCiary, null)); // Color.rgb(5, 20, 20));

        this.farbaVnutraObvodovejCiaryGradient = sp.getInt("FARBAVNUTRAOBVODOVEJCIARYGRADIENT", ResourcesCompat.getColor(getResources(), R.color.farbaVnutraObvodovejCiaryGradient, null));  // Color.rgb(5, 20, 20));

        this.farbaSekundovejRucicky = sp.getInt("FARBASEKUNDOVEJRUCICKY", ResourcesCompat.getColor(getResources(), R.color.farbaSekundovejRucicky, null)); // Color.rgb(50,50,50));

        this.farbaVnutraSekundovejRucicky = sp.getInt("FARBAVNUTRASEKUNDOVEJRUCICKY", ResourcesCompat.getColor(getResources(), R.color.farbaVnutraSekundovejRucicky, null)); // Color.rgb(50,50,50));

        this.farbaVysuvnikaSekundovejRucicky = sp.getInt("FARBAVYSUVNIKASEKUNDOVEJRUCICKY", ResourcesCompat.getColor(getResources(), R.color.farbaVysuvnikaSekundovejRucicky, null)); // Color.rgb(50,50,50));

        this.farbaOzdobnehoKruhu = sp.getInt("FARBAOZDOBNEHOKRUHU", ResourcesCompat.getColor(getResources(), R.color.farbaOzdobnehoKruhu, null));  //  Color.rgb(20, 60, 70));
        this.farbaOzdobnychLucov = sp.getInt("FARBAOZDOBNYCHLUCOV",ResourcesCompat.getColor(getResources(), R.color.farbaOzdobnychLucov, null));  //  Color.rgb(50, 40, 78));

//        if(nazovOdkladaciehoSuboru == "MojeUserPrefs")
//            Toast.makeText(this, "Settings Restored", Toast.LENGTH_SHORT).show();

    }


}


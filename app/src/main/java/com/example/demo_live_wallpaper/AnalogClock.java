package com.example.demo_live_wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.Calendar;
import java.util.Date;

public class AnalogClock extends View {

        /** center X. */
        private float x;
        /** center Y. */
        private float y;
        private int radius;
        private Calendar cal;
        private Paint kresba;
        // private Bitmap clockDial = BitmapFactory.decodeResource(getResources(), R.drawable.widgetdial);
        private int sizeScaled = -1;
        private Bitmap clockDialScaled;
        /** Hands colors. */
        private int[] colors;
        private boolean displayHandSec;

        int sirka;
        int vyska;
        int min;

        private int vypln;          // padding
        private int velkostPisma;

        int okraj; // default - odsadi body cifernika od okraja
        int polomer;  // radius
        int zosuv_per;  // dafault 80%
        int zosuv;
        int hrubkaObvodovejCiary;
        int odsadenieObvodovejCiary;

        private int medzeraOdCisiel;    // numeralSpacing
        private int skratenieRucicky, skratenieHodinovejRucicky = 0;

        // private Paint kresba;
        private boolean jeInicializovana;
        // private int[] cisla = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        private String[] cisla_arabske = {"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
        private String[] cisla_rimske = {"XII", "I", "II", "III", "IIII", "V", "VI", "VII", "VIII", "IX", "X", "XI"};
        private Rect pravouholnik = new Rect();
        Cifernik cf;
        Cifernik cfj;

    String mojPolomer;     // polomer rohou
    String mojTextOdsad;   // ogray
    String mojTextZosuv;
    String mojTextPoloha;

    // static String mojTextVyska;
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

    public AnalogClock(Context context) {
            super(context);
            cal = Calendar.getInstance();
        }

    public AnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        System.out.println("Start z XML constructor c. 2 " + attrs.getPositionDescription() + " sirka " + getWidth());
        // nastavUvodneHodnoty();
        // inicializujHodiny();
    }


    public void config(float x, float y, int size, Date date, Paint paint, int[] colors, boolean displayHandSec) {
            this.x = x;
            this.y = y;
            this.kresba = paint;
            this.colors = colors;
            this.displayHandSec = displayHandSec;

            cal.setTime(date);

            // scale bitmap if needed
           /* if (size != sizeScaled) {
                clockDialScaled = Bitmap.createScaledBitmap(clockDial, size, size, false);
                radius = size / 2;
            }*/
        }

    protected void onDraw(Canvas platno) {
            super.onDraw(platno);

            if (kresba != null) {
                inicializujHodiny();
                kresliBody(platno, 0,0, farbaZnaciek);
                if(b_arabic_numbers)
                    kresliCisla(platno, cisla_arabske, 0,0, farbaCisel);
                kresliRucicky(platno);

                // kresliStred(platno, 0,0,40, farbaRuciciek);

                // draw clock img
                // platno.drawBitmap(clockDialScaled, x - radius, y - radius, null);
                // platno.save();
                // platno.drawColor(Color.YELLOW);
                float sec = cal.get(Calendar.SECOND);
                float min = cal.get(Calendar.MINUTE);
                float hour = cal.get(Calendar.HOUR_OF_DAY);
                //draw hands
                // kresba.setColor(farbaPozadia);
                // platno.drawLine(x, y , (float) (x + (radius * 0.5f) * Math.cos(Math.toRadians((hour / 12.0f * 360.0f) - 90f))),
                   //     (float) (y + (radius * 0.5f) * Math.sin(Math.toRadians((hour / 12.0f * 360.0f) - 90f))), kresba);
                // platno.save();
                // kresba.setColor(colors[1]);
                // platno.drawLine(x, y, (float) (x + (radius * 0.6f) * Math.cos(Math.toRadians((min / 60.0f * 360.0f) - 90f))),
                 //       (float) (y + (radius * 0.6f) * Math.sin(Math.toRadians((min / 60.0f * 360.0f) - 90f))), kresba);
                // platno.save();

                 if (displayHandSec) {
                    kresba.setColor(colors[2]);
                    platno.drawLine(x, y, (float) (x + (radius * 0.7f) * Math.cos(Math.toRadians((sec / 60.0f * 360.0f) - 90f))),
                            (float) (y + (radius * 0.7f) * Math.sin(Math.toRadians((sec / 60.0f * 360.0f) - 90f))), kresba);
                }
            }
        }

    public void inicializujHodiny() {      // initClock

        nastavUvodneHodnoty();

        // h_shadow = false;
        zosuv_per = Integer.parseInt(mojTextZosuv);    //   65;

        vyska = (int) ((zosuv_per * y * 2) / 100);

        sirka = (int) x * 2;  // 1048;
        // vyska = (int) y * 2;  // 1280;

        min = Math.min(vyska, sirka);

        // zosuv = super.getHeight() - vyska;
        zosuv = (int) (y * 2 - vyska);

        // zosuv = 360;

        vypln = medzeraOdCisiel + 80;
        velkostPisma = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

        polomer = min / 4 - vypln;
        skratenieRucicky = min / 20;
        skratenieHodinovejRucicky = min / 7;
        kresba = new Paint();
        jeInicializovana = true; // isInit
        cf = new Cifernik(60);     // pre kreslenie cifernika a sekundovy beh
        // if(h_continouos)
        // cfj = new Cifernik(6000);
        cfj = new Cifernik(1500);  // pre jemny beh
        okraj = 50;
        cf.CifernikKU(okraj, polomer, sirka, vyska);
        cfj.CifernikKU(okraj,polomer,sirka,vyska);

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

            dlzkaPoZnacku = (int) (cf.body[pocitadlo].dlzka(sirka / 2, vyska / 2));
            uholZnacky = (double) (cf.body[pocitadlo].uhol(sirka / 2, vyska / 2));
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

        min = Math.min(vyska - okraj, sirka - okraj);
        // Double uhol = (double) umiestnenie;

        if(h_continouos) {
            uhol = cfj.body[umiestnenie].uhol(sirka / 2, vyska / 2);
            // int polomerRucicky = jeHodina ? polomer - skratenieRucicky - skratenieHodinovejRucicky : polomer - skratenieRucicky;
            polomerRucicky = (int) (cfj.body[umiestnenie].dlzka(sirka / 2, vyska / 2));
        }
        else {
            uhol = cf.body[umiestnenie].uhol(sirka / 2, vyska / 2);
            // int polomerRucicky = jeHodina ? polomer - skratenieRucicky - skratenieHodinovejRucicky : polomer - skratenieRucicky;
            polomerRucicky = (int) (cf.body[umiestnenie].dlzka(sirka / 2, vyska / 2));
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
            uhol = cfj.body[umiestnenie].uhol(sirka / 2, vyska / 2);
            polomerRucicky = (int) (cfj.body[umiestnenie].dlzka(sirka / 2, (vyska) / 2));
        }
        else {
            uhol = cf.body[umiestnenie].uhol(sirka / 2, vyska / 2);
            polomerRucicky = (int) (cf.body[umiestnenie].dlzka(sirka / 2, (vyska) / 2));
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

        // int min = Math.min(vyska - okraj, sirka - okraj);

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
            kresliMinutovuRucicku(platno, (int)(c.get(Calendar.MINUTE) * 25 + c.get(Calendar.SECOND) * 25 / 60), 0, 0, farbaRuciciek, farbaVnutraRuciciek, farbaVysuvnikaRuciciek);
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
        // for(int cislo : cisla) {
        for(int cislo = 1; cislo <= cisla.length; cislo++) {
            dlzkaPoZnacku = (int) (cf.body[(cislo - 1) * 5].dlzka(sirka / 2, vyska / 2));
            uholZnacky = (double) (cf.body[(cislo - 1) * 5].uhol(sirka / 2, vyska / 2));
            // String docasny = String.valueOf(cislo - 1);

            // kresba.getTextBounds(docasny, 0, docasny.length(), pravouholnik);
            kresba.getTextBounds(cisla[cislo - 1], 0, cisla[cislo - 1].length(), pravouholnik);

            // Double uhol = Math.PI / 6 * (cislo - 3);
            // int x = (int) (sirka / 2 + Math.cos(uhol) * polomer - pravouholnik.width() / 2);
            // int y = (int) (vyska / 2 + Math.sin(uhol) * polomer + pravouholnik.height() / 2);

            int x = (int) ((sirka / 2 + Math.cos(uholZnacky) * (dlzkaPoZnacku - 100)) - pravouholnik.width() / 2);
            int y = (int) ((vyska / 2 + Math.sin(uholZnacky) * (dlzkaPoZnacku - 100)) + pravouholnik.height() / 2);

            // platno.drawText(docasny, x, y + zosuv / 2, kresba);
            platno.drawText(cisla[cislo - 1], x + posunT_X, y + zosuv / 2 + posunT_Y, kresba);
        }
    }

    private void kresliStred(Canvas platno, int posunT_X, int posunT_Y, int priemer,int farba) {
        kresba.setStyle(Paint.Style.FILL);
        kresba.setColor(farba);
        platno.drawCircle(sirka / 2 + posunT_X, vyska / 2 + zosuv / 2 + posunT_Y,priemer, kresba);
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


}


package com.example.demo_live_wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;
import java.util.Date;

public class MySurface extends SurfaceView {

    Calendar c;
    float sekundy;
    float minuty;
    float hodina;
    float milli;
    int bgColor;
    boolean navratZWallpaper = false;

    Bitmap clockDialScaled, clockDialScaled2;
    AnalogClock analogClock;
    Anim anim;
    SurfaceHolder drziakPovrchu;
    Canvas platno = null;


//    SurfaceHolder drziakPovrchu;
//    Canvas platno;
    Paint paint;

    private boolean playing = true;

    public MySurface(Context context) {
        super(context);
        analogClock = new AnalogClock(context.getApplicationContext());
        // analogClock.nastavUvodneHodnoty();
        bgColor = analogClock.farbaPozadia;
        anim = new Anim();
        // new Anim().start();
        anim.start();
    }

    private class Anim extends Thread {

        int counter = 0;
        int counter_past;

        // boolean h_rect_ca = true;
        int okraj;
        int polomer;
        boolean h_continouos;

        Cifernik cf, cfj;

        @Override
        public void run() {
            long last_updated_time = 0;
            long delay = 5;

            cf = new Cifernik(60);     // pre kreslenie cifernika a sekundovy beh
            cfj = new Cifernik(1500);

            int img_ids[] = {
                    R.drawable.bubble_1,
                    R.drawable.bubble_2,
                    R.drawable.bubble_3,
                    R.drawable.bubble_4,
                    R.drawable.bubble_5,
                    R.drawable.bubble_6,
                    R.drawable.bubble_7,
                    R.drawable.bubble_8,
                };

            int img_ids2[] = {
                    R.drawable.runningcatt_1,
                    R.drawable.runningcatt_2,
                    R.drawable.runningcatt_3,
                    R.drawable.runningcatt_4,
                    R.drawable.runningcatt_5,
                    R.drawable.runningcatt_6,
                    R.drawable.runningcatt_7,
                    R.drawable.runningcatt_8
            };

            while (true) {
                if (playing) {
                    long current_time = System.currentTimeMillis();

                    if (current_time > last_updated_time + delay) {

                        counter_past = counter;

                        if (counter >= 64) {
                            counter = 0;
                        }
                        c = Calendar.getInstance();
                        sekundy = c.get(Calendar.SECOND);
                        minuty = c.get(Calendar.MINUTE);
                        hodina = c.get(Calendar.HOUR_OF_DAY);
                        milli = c.get(Calendar.MILLISECOND);
                        // draw(img_ids2[counter / 8]);
                        draw(img_ids2[counter / 8]);

                        last_updated_time = current_time;

                        counter++;
                        counter++;
                        counter++;
                        counter++;
                    }
                }
            }

        }

        private void draw(int img_ids2) {

//            SurfaceHolder drziakPovrchu;
//            Canvas platno;

          /*  if(navratZWallpaper == true) {
                // urob novy holder nejake scale treba
                System.out.println(" ***** Thread " + currentThread() + " drziakPovrchu pred getHolder() " + drziakPovrchu + " navratZWallpaper " + navratZWallpaper);
                navratZWallpaper = false;
            }*/

            drziakPovrchu = getHolder();

            drziakPovrchu.setSizeFromLayout();

            System.out.println(" ***** Thread " + currentThread() + " drziakPovrchu " + drziakPovrchu + " drziakPovrchu.getSurface() " + drziakPovrchu.getSurface());

            if(drziakPovrchu.getSurface().isValid() == false) {

                System.out.println(" ***  holder.getSurface().isValid()) " + drziakPovrchu.getSurface().isValid() + " Current Thread " + currentThread());

            }

            platno = drziakPovrchu.lockCanvas();

             if (platno != null) {

                int sirka = platno.getWidth();
                int vyska = platno.getHeight();
                draw(platno);

                Bitmap bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), img_ids2);
                clockDialScaled2 = Bitmap.createScaledBitmap(bitmap2, sirka / 4, sirka / 8, false);
                platno.drawBitmap(clockDialScaled2, 3 * sirka / 8, 3 * vyska / 8, paint);

                try {
                        drziakPovrchu.unlockCanvasAndPost(platno);
                    } catch (Exception e) {

                        System.out.println("No canvas lock");
                }

             }




        }
        private void draw(Canvas canvas) {

            int sirka = canvas.getWidth();
            int vyska = canvas.getHeight();

            analogClock.config(sirka, vyska, new Date());

            analogClock.draw(canvas);
        }

    }

}

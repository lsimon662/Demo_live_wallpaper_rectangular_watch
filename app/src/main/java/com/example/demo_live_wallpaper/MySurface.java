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
    Bitmap clockDialScaled, clockDialScaled2;
    AnalogClock analogClock;
    Anim anim;

    SurfaceHolder holder;

    Canvas canvas;
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

        boolean h_rect_ca = true;
        int okraj;
        int polomer;
        boolean h_continouos;

        Cifernik cf, cfj;

        @Override
        public void run() {
            long last_updated_time = 0;
            long delay = 25;

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

            holder = getHolder();

            canvas = holder.lockCanvas();

            if (canvas != null) {
                int sirka = canvas.getWidth();
                int vyska = canvas.getHeight();

               /* canvas.drawColor(Color.BLACK);
                paint = new Paint(Paint.ANTI_ALIAS_FLAG);

                okraj = 50;
                polomer = sirka / 3;

                if(h_rect_ca) {
                    cf.CifernikKU(okraj, polomer, sirka, vyska);
                    if (h_continouos)
                        cfj.CifernikKU(okraj, polomer, sirka, vyska);
                }*/

                // canvas.drawColor(bgColor);
//            clock.config(width / 2, height / 2, (int) (width * 0.6f),
//                    new Date(), paint, colors, displayHandSec);

                analogClock.config(sirka, vyska, new Date());

                // clock.inicializujHodiny();
                analogClock.draw(canvas);

                Bitmap bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), img_ids2);
                clockDialScaled2 = Bitmap.createScaledBitmap(bitmap2, sirka / 4, sirka / 8, false);
                canvas.drawBitmap(clockDialScaled2, 3 * sirka / 8, 5 * vyska / 8, paint);

//                paint.setColor(Color.RED);
                // paint.setStrokeWidth(25);
//                paint.setTextSize(64);
//
//                 canvas.drawLine(sirka / 2,vyska / 2,(float) (sirka / 2 + 450 * Math.cos((float) (counter / 5))),(float) (vyska / 2 + 450 * Math.sin((float) (counter / 5))), paint);

               /* canvas.drawText("" + sekundy, sirka / 2 - 300,vyska / 2 + 300, paint);

                // analogClock.draw(canvas);

                paint.setColor(Color.GREEN);
                canvas.drawLine(sirka / 2,
                        vyska / 2,
                        (float) (sirka / 2 + sirka / 5 * Math.cos(hodina * Math.PI / 6 - Math.PI / 2)),
                        (float) (vyska / 2 + sirka / 5 * Math.sin(hodina * Math.PI / 6 - Math.PI / 2)), paint);

                paint.setColor(Color.MAGENTA);
                canvas.drawLine(sirka / 2,
                        vyska / 2,
                        (float) (sirka / 2 + sirka / 4 * Math.cos(minuty * Math.PI / 30 - Math.PI / 2)),
                        (float) (vyska / 2 + sirka / 4 * Math.sin(minuty * Math.PI / 30 - Math.PI / 2)), paint);

                paint.setColor(Color.YELLOW);
                canvas.drawLine(sirka / 2,
                        vyska / 2,
                        (float) (sirka / 2 + sirka / 3 * Math.cos((sekundy * 6 * Math.PI / 180 - Math.PI / 2))),
                        (float) (vyska / 2 + sirka / 3 * Math.sin((sekundy * 6 * Math.PI / 180 - Math.PI / 2))), paint);

                paint.setColor(Color.LTGRAY);
                canvas.drawLine(sirka / 2,
                        vyska / 2,
                        (float) (sirka / 2 + sirka / 3 * Math.cos((sekundy * 6 * Math.PI / 180 - Math.PI / 2) + (milli * Math.PI / 30000))),
                        (float) (vyska / 2 + sirka / 3 * Math.sin((sekundy * 6 * Math.PI / 180 - Math.PI / 2) + (milli * Math.PI / 30000))), paint);
*/
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

}

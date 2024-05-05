package com.example.demo_live_wallpaper;

public class Cifernik {
    // class Cifernik {
        double dlzkaCifernika;   // celkova dlzka cifernika

        double dlzkaUseku;       // pracovna dlzka jednotlivych ciar a oblukov
        double dielik;           // dlzkaCifernika / 60
        double dielikJemny;      // dlzkaCifernika / 600
        // int cislo = 0;
        int cislo;
        int pocetDielikov;
        // Bod[] body = new Bod[600];   // kvoli hladkemu chodu
        Bod[] body;
        // Bod[] bodyJemne = new Bod[600];

    Cifernik(int pocetDielikov) {
        this.pocetDielikov = pocetDielikov;
        body = new Bod[pocetDielikov];
    }

    class Bod {
        int xOva;
        int yOva;

        Bod(int xOva, int yOva) {
            this.xOva = xOva;
            this.yOva = yOva;
        }

        int getX() {
            return this.xOva;
        };

        int getY() {
            return this.yOva;
        };

        double dlzka(double x1, double y1) {
            double x2 = this.xOva;
            double y2 = this.yOva;
            return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
        };

        double uhol(double x1, double y1) {
            double x2 = this.xOva;
            double y2 = this.yOva;

            double pomocnyUhol = Math.atan((y2 - y1) / (x2 - x1));  // arctg vrati spravne len 2 kvadranty, dalsie musi pricitat pi

            if ((x2 - x1) < 0)
                pomocnyUhol += Math.PI;

            if ((x2 - x1) == 0 && (y2 - y1 < 0))
                pomocnyUhol = -Math.PI / 2.0;

            if ((x2 - x1) == 0 && (y2 - y1 > 0))
                pomocnyUhol = Math.PI / 2.0;

            return pomocnyUhol;
        };
       }

        Bod point_L_U(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Left - Upper
            return new Bod(okraj, okraj + radius);
        }

        Bod point_L_B(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Left - Bottom
            return new Bod(okraj, oknoVyska - okraj - radius);
        }

        Bod point_R_U(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Right Line - Upper
            return new Bod(oknoSirka - okraj, okraj + radius);
        }

        Bod point_R_B(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Left - Bottom
            return new Bod(oknoSirka - okraj, oknoVyska - okraj - radius);
        }


        Bod point_U_L(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Upper line - Left point
            return new Bod(okraj + radius, okraj);
        }

        Bod point_U_R(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Upper line - Right point
            return new Bod(oknoSirka - okraj - radius, okraj);
        }

        Bod point_B_L(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Bottom line - Left point
            return new Bod(okraj + radius, oknoVyska - okraj);
        }

        Bod point_B_R(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Bottom line - Right point
            return new Bod(oknoSirka - okraj - radius, oknoVyska - okraj);
        }

        Bod point_M_U_L(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Middle radius Upper Left
            return new Bod(okraj + radius, okraj + radius);
        }

        Bod point_M_U_R(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Middle radius Upper Right
            return new Bod(oknoSirka - okraj - radius, okraj + radius);
        }

        Bod point_M_B_L(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Middle radius Bottom Right
            return new Bod(okraj + radius, oknoVyska - okraj - radius);
        }

        Bod point_M_B_R(int okraj, int radius, int oknoSirka, int oknoVyska) {  // Middle radius Bottom Left
            return new Bod(oknoSirka - okraj - radius, oknoVyska - okraj - radius);
        }


        void CifernikKV(int okraj, int radius, int oknoSirka, int oknoVyska) {
            // ****   cifernik s konstantnou vzdialenostou obvodovych sekundovych bodov
            dlzkaCifernika = oknoSirka / 2.0 - okraj - radius;      // horna prava polovica ciary
            dlzkaCifernika += Math.PI * radius / 2.0;                // horny ptavy stvrtobluk
            dlzkaCifernika += oknoVyska - 2.0 * okraj - 2.0 * radius;  // prava zvisla ciata
            dlzkaCifernika += Math.PI * radius / 2.0;                // dolny ptavy stvrtobluk
            dlzkaCifernika += oknoSirka - 2.0 * okraj - 2.0 * radius;      // spodna ciary
            dlzkaCifernika += Math.PI * radius / 2.0;                // lavy spodny stvrtobluk
            dlzkaCifernika += oknoVyska - 2.0 * okraj - 2.0 * radius;  // lava zvisla ciata
            dlzkaCifernika += Math.PI * radius / 2.0;                // lavy horny stvrtobluk
            dlzkaCifernika += oknoSirka / 2.0 - okraj - radius;      // horna lava polovica ciary

            dielik = dlzkaCifernika / pocetDielikov;
            // dielikJemny = dlzkaCifernika / 600.0;
            cislo = 0;

            // ***   horna rovna strana prava polovica
            int dlzka = (int) (oknoSirka / 2.0 - okraj - radius);
            double rozstup = 0.0;
//            System.out.println(" Hodiny : " + new Date().getHours() + " Minuty : " + new Date().getMinutes() + " Sekundy : " + new Date().getSeconds());
//            g.setColor(Color.black.darker());
            while (rozstup <= dlzka) {
//                g.drawLine(oknoSirka / 2, oknoVyska / 2,
//                          (int) (oknoSirka / 2 + rozstup), okraj);
                body[cislo] = new Bod((int)(oknoSirka / 2.0 + rozstup), okraj);    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
//                  body[cislo].yOva = okraj;
//                g.drawOval((int) (oknoSirka / 2 - 3 + rozstup), okraj - 3, 6, 6);
                rozstup += dielik;
                cislo++;
            }

            double zbytok = rozstup - dlzka;
            // pravy horny obluk
            double uhol = - zbytok / radius;
//            System.out.print("*** Uhol : " + uhol + " zbytok : " + zbytok + " radius : " + radius);
            while (uhol > - Math.PI / 2.0) {  // cez uhol, lepsie je ist jednotne cez vseobecnu dlzku
                body[cislo] = new Bod((int) (oknoSirka - okraj - radius + radius * Math.cos(uhol + Math.PI / 2.0)),
                        (int) (okraj + radius - radius * Math.sin(uhol + Math.PI / 2.0)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
                uhol -= dielik / radius;
                cislo++;
//                System.out.println("  ****  Cislo : " + cislo + " Uhol : " + uhol);
            }
            zbytok = ((uhol + Math.PI / 2.0) * radius);  // odpocitat od dlzky pravej zvislej steny
//            System.out.println("Dielik : " + dielik + " *** Zbytok : " + zbytok);

            // tretia cast pravy bok
            dlzka = (int) (oknoVyska - 2.0 * okraj - 2.0 * radius);
//            System.out.print(" dlzka 3 : " + dlzka);
            rozstup = - zbytok;

            while (rozstup <= dlzka) {
//                 body[cislo] = new Bod((oknoSirka / 2, oknoVyska / 2, oknoVyska - okraj - 3, (int) (okraj + radius - 3 + rozstup));
                body[cislo] = new Bod(oknoSirka - okraj, (int) (okraj + radius + rozstup));
                rozstup += dielik;
                cislo++;
            }
            zbytok = rozstup - dlzka;
//            System.out.println("  *** Zbytok : " + zbytok);

            // obluk vpravo dolu
            uhol = - zbytok / radius;
//            System.out.print("*** Uhol : " + uhol + " zbytok : " + zbytok + " radius : " + radius);

            // obluk hore vpravo
            // uhol musi klesat aj test sa musi zmenit
            while (uhol > - Math.PI / 2.0) {  // cez uhol, lepsie je ist jednotne cez vseobecnu dlzku
                body[cislo] = new Bod((int) (oknoSirka - okraj - radius + radius * Math.cos(uhol)),
                        (int) (oknoVyska - okraj - radius - radius * Math.sin(uhol)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
                uhol -= dielik / radius;
                cislo++;
//                System.out.println("  ****  Cislo : " + cislo + " Uhol : " + uhol);
            }
            zbytok = ((uhol + Math.PI / 2.0 ) * radius);  // odpocitat od dlzky pravej zvislej steny
//            System.out.println("Dielik : " + dielik + " *** Zbytok : " + zbytok);

            // spodna rovna strana
            dlzka = (int) (oknoSirka - 2.0 * okraj - 2.0 * radius);
//            rozstup = zbytok;
            rozstup = dlzka + zbytok;

            while (rozstup >= 0) {
                body[cislo] = new Bod((int) (okraj + radius + rozstup), oknoVyska - okraj);
                rozstup -= dielik;
                cislo++;
            }
            zbytok = - rozstup;
//            System.out.println("Zbytok : " + zbytok);
            // lavy dolny obluk
            uhol = - zbytok / radius;
//            System.out.println(" *** Uhol : " + uhol + " *** zbytok : " + zbytok + " radius : " + radius);

            // obluk hore vpravo
            // uhol musi klesat aj test sa musi zmenit
            while (uhol >= - Math.PI / 2.0) {  // cez uhol, lepsie je ist jednotne cez vseobecnu dlzku
                body[cislo] = new Bod((int) (okraj + radius - radius * Math.cos(uhol + Math.PI / 2.0)),
                        (int) (oknoVyska - okraj - radius + radius * Math.sin(uhol + Math.PI / 2.0)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
                uhol -= dielik / radius;
                cislo++;
            }

            zbytok = (float) ((uhol + Math.PI / 2.0) * radius);  // odpocitat od dlzky pravej zvislej steny
//            System.out.println("Dielik : " + dielik + " *** Zbytok : " + zbytok);
            // bocna lava strana
            dlzka = (int) (oknoVyska - 2.0 * okraj - 2.0 * radius);
//            System.out.print(" dlzka 3 : " + dlzka);
            rozstup = zbytok + dlzka;

            while (rozstup >= 0) {
//                 body[cislo] = new Bod((oknoSirka / 2, oknoVyska / 2, oknoVyska - okraj - 3, (int) (okraj + radius - 3 + rozstup));
                body[cislo] = new Bod(okraj, (int) (okraj + radius + rozstup));
                rozstup -= dielik;
                cislo++;
            }

            zbytok = - rozstup;
            // lavy horny obluk
            uhol = - zbytok / radius;
//            System.out.print("*** Uhol : " + uhol + " zbytok : " + zbytok + " radius : " + radius);
            // obluk hore vpravo
            // uhol musi klesat aj test sa musi zmenit
            while (uhol > - Math.PI / 2.0) {  // cez uhol, lepsie je ist jednotne cez vseobecnu dlzku
                body[cislo] = new Bod((int) (okraj + radius + radius * Math.cos(uhol + Math.PI)),
                        (int) (okraj + radius - radius * Math.sin(uhol + Math.PI)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
                uhol -= dielik / radius;
                cislo++;
                //    System.out.println("  ****  Cislo : " + cislo + " Uhol : " + uhol);
            }
            zbytok = (float) ((uhol + Math.PI / 2.0) * radius);  // odpocitat od dlzky pravej zvislej steny
            // System.out.println("Dielik : " + dielik + " *** Zbytok : " + zbytok);
            // Horna rovna strana lava cast
            dlzka = (int) (oknoSirka / 2.0 - okraj - radius);
            rozstup = - zbytok;

            while ((rozstup < dlzka )  && (cislo < pocetDielikov)) {
//            while ((rozstup < dlzka )  && (cislo < 600)) {
                body[cislo] = new Bod((int)(okraj + radius + rozstup), okraj);    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
                rozstup += dielik;
                cislo++;
            }
        }

        void CifernikKU(int okraj, int radius, int oknoSirka, int oknoVyska) {
            //        Cifernik s konstantnym uhlom
            //        dlzkaCifernika;
            dlzkaCifernika = oknoSirka / 2.0 - okraj - radius;      // horna prava polovica ciary
            dlzkaCifernika += Math.PI * radius / 2;                // horny ptavy stvrtobluk
            dlzkaCifernika += oknoVyska - 2.0 * okraj - 2.0 * radius;  // prava zvisla ciata
            dlzkaCifernika += Math.PI * radius / 2.0;                // dolny ptavy stvrtobluk
            dlzkaCifernika += oknoSirka - 2.0 * okraj - 2.0 * radius;      // spodna ciary
            dlzkaCifernika += Math.PI * radius / 2.0;                // lavy spodny stvrtobluk
            dlzkaCifernika += oknoVyska - 2.0 * okraj - 2.0 * radius;  // lava zvisla ciata
            dlzkaCifernika += Math.PI * radius / 2.0;                // lavy horny stvrtobluk
            dlzkaCifernika += oknoSirka / 2.0 - okraj - radius;      // horna lava polovica ciary
            //        this.pomer = (double) vyska / (double) sirka;
            //        this.uholDiag = Math.atan(this.pomer);
            cislo = 0;
            //  tu sa menilo  **********************

            // double dielU = Math.PI / 30.0;
            double dielU = Math.PI / (pocetDielikov / 2.0);

            double uholDR1 = Math.atan((oknoSirka / 2.0 - okraj - radius) / (oknoVyska / 2.0 - okraj));
            double uholDR2 = Math.atan((oknoSirka / 2.0 - okraj) / (oknoVyska / 2.0 - okraj - radius));
//        System.out.println("Vyska : " + oknoVyska + " Sirka : " + oknoSirka);
//        System.out.println("Radius : " + radius + " Uhol DR1 " + uholDR1 + " Uhol DR2 " + uholDR2);
            // horna rovna strana polovica
            //  int dlzka = oknoSirka / 2 - okraj - radius;
            double uhol = 0.0;   // (float) (Math.PI / 2.0);
            while (uhol <= uholDR1) {
//                System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1);
                body[cislo] = new Bod((int)(oknoSirka / 2.0 + (oknoVyska / 2.0 - okraj) * Math.tan(uhol)), okraj);    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
                uhol += dielU;
                cislo++;
            }
            // horny pravy obluk
            // uho stred okna stred obluka
            double wi = oknoSirka / 2.0 - okraj - radius;
            double he = oknoVyska / 2.0 - okraj - radius;
//            double beta = Math.atan((oknoSirka / 2.0 - okraj - radius) / (oknoVyska / 2.0 - okraj - radius));
            double da = Math.sqrt(wi * wi + he * he);
            double beta = Math.atan(wi / he);

            double gama = uhol - beta;    //   v dalsom obluku uplne inak
//            double da = (oknoVyska - okraj - radius) * Math.tan(beta);
            double mi = Math.asin(da * Math.sin(gama) / radius);
            double theta = Math.PI / 2.0 - uhol - mi;
//            System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1 + " Beta : " + beta + " Gama : " + gama);
            while (uhol <= uholDR2 && uhol >=uholDR1) {
//                System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1);
                body[cislo] = new Bod((int)(oknoSirka - okraj - radius - radius *  Math.sin(theta - Math.PI / 2.0)),
                        (int)(okraj + radius - radius *  Math.cos(theta - Math.PI / 2.0)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
                uhol += dielU;
                gama = uhol - beta;
                mi = Math.asin(da * Math.sin(gama) / radius);
                theta = Math.PI / 2.0 - uhol - mi;
//                System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1 + " Beta : " + beta + " Gama : " + gama);
                cislo++;
            }
            // prava bocna rovna strana
            while (uhol >= uholDR2 && uhol <= Math.PI - uholDR2) {
//                System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1);
//                (int) (oknoSirka / 2 + rozstup), okraj);
                body[cislo] = new Bod((int) (oknoSirka - okraj), (int) (oknoVyska / 2.0 + (oknoSirka / 2.0 - okraj) / Math.tan(-uhol)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
//                  body[cislo].yOva = okraj;
                uhol += dielU;
                cislo++;
            }

            // pravy dolny obluk
            double uholR = uholDR1 + uholDR2 - Math.PI;
//            gama = uhol - beta - Math.PI + uholDR2 + uholDR1;
            gama = Math.PI - uhol - beta;  // tu pi

            mi = Math.asin(da * Math.sin(gama) / radius);
            theta = Math.PI / 2.0 - uhol /* - uholR */ + mi;   //   ??????????????????????
            // pravy dolny obluk
//            System.out.println("Uhol : " + uhol + " DielU : " + dielU + " UholDR1 : " + uholDR1 + " Beta : " + beta + " Gama : " + gama);
            while (uhol >= Math.PI - uholDR2 && uhol <= Math.PI - uholDR1) {
//                body[cislo] = new Bod((int)((oknoSirka - okraj) - radius *  Math.sin(theta + Math.PI / 2.0)), (int)(okraj + radius - radius *  Math.cos(theta - Math.PI / 2.0)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
//                body[cislo] = new Bod((int)(oknoSirka - okraj - radius - radius *  Math.sin(theta - Math.PI / 2.0)),
//                                      (int)(okraj + radius - radius *  Math.cos(theta - Math.PI / 2.0)));
                body[cislo] = new Bod((int)(oknoSirka - okraj - radius + radius * Math.sin(theta + Math.PI / 2.0)),
                        (int)(oknoVyska - okraj - radius - radius * Math.cos(theta - Math.PI / 2.0)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
//                  body[cislo].yOva = okraj;
                uhol += dielU;
                gama = Math.PI - uhol - beta;
                mi = Math.asin(da * Math.sin(gama) / radius);
                theta = Math.PI / 2.0 - uhol + /* uholR - */ mi;
//                System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1 + " Beta : " + beta + " Gama : " + gama);
                cislo++;
            }
            // ***********************

            // dolna rovna strana
            while (uhol >= Math.PI - uholDR1 && uhol <= Math.PI + uholDR1) {
//                System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1);
//                (int) (oknoSirka / 2 + rozstup), okraj);
//                body[cislo] = new Bod((int)(oknoSirka / 2.0 + (oknoVyska / 2.0 - okraj) * Math.tan(uhol)), okraj);    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
                body[cislo] = new Bod((int)(oknoSirka / 2.0 - (oknoVyska / 2.0 - okraj) * Math.tan(uhol - Math.PI)), oknoVyska - okraj);    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
//                  body[cislo].yOva = okraj;
                uhol += dielU;
                cislo++;
            }

            // dolny lavy obluk
            gama = uhol - Math.PI - beta;

//            da = (oknoVyska - okraj - radius) * Math.tan(beta);
            da = Math.sqrt(wi * wi + he * he);

            mi = Math.asin(da * Math.sin(gama) / radius);
            theta = Math.PI / 2.0 - uhol + Math.PI - mi;

//            System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1 + " Beta : " + beta + " Gama : " + gama);
            while (uhol >= Math.PI + uholDR1 && uhol <= Math.PI + uholDR2) {
//                System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1);
//                (int) (oknoSirka / 2 + rozstup), okraj);
//                body[cislo] = new Bod((int)((oknoSirka - okraj - radius) - radius *  Math.sin(theta - Math.PI / 2.0)), (int)(okraj + radius - radius *  Math.cos(theta - Math.PI / 2.0)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
                body[cislo] = new Bod((int)((okraj + radius) + radius *  Math.sin(theta - Math.PI / 2.0)), (int)(oknoVyska - okraj - radius + radius *  Math.cos(theta - Math.PI / 2.0)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
                uhol += dielU;
                gama = uhol - Math.PI - beta;
                mi = Math.asin(da * Math.sin(gama) / radius);
                theta = Math.PI / 2.0 - uhol + Math.PI - mi;
//                System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1 + " Beta : " + beta + " Gama : " + gama);
                cislo++;
            }

            // lava bocna rovna strana
            while (uhol >= uholDR2 + Math.PI && uhol <= 2 * Math.PI - uholDR2) {
//                System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1);
//                (int) (oknoSirka / 2 + rozstup), okraj);
                body[cislo] = new Bod(okraj, (int) (oknoVyska / 2.0 - (oknoSirka / 2.0 - okraj) / Math.tan( - uhol)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
//                  body[cislo].yOva = okraj;
                uhol += dielU;
                cislo++;
            }

            // uholR = uholDR1 + uholDR2 - Math.PI;
            // gama = uhol - beta - Math.PI + uholDR2 + uholDR1;
            gama = 2.0 * Math.PI - uhol - beta;  // tu pi

            mi = Math.asin(da * Math.sin(gama) / radius);
            theta = Math.PI / 2.0 - uhol /* - uholR */ + mi;   //   ??????????????????????
            // pravy dolny obluk
//            System.out.println("Uhol : " + uhol + " DielU : " + dielU + " UholDR1 : " + uholDR1 + " Beta : " + beta + " Gama : " + gama);
            while (uhol >= 2 * Math.PI - uholDR2 && uhol <= 2 * Math.PI - uholDR1) {
//                body[cislo] = new Bod((int)((oknoSirka - okraj) - radius *  Math.sin(theta + Math.PI / 2.0)), (int)(okraj + radius - radius *  Math.cos(theta - Math.PI / 2.0)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
//                body[cislo] = new Bod((int)(oknoSirka - okraj - radius - radius *  Math.sin(theta - Math.PI / 2.0)),
//                                      (int)(okraj + radius - radius *  Math.cos(theta - Math.PI / 2.0)));
                body[cislo] = new Bod((int)(okraj + radius + radius * Math.sin(theta + Math.PI / 2.0)),
                        (int)(okraj + radius - radius * Math.cos(theta - Math.PI / 2.0)));    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
//                  body[cislo].yOva = okraj;
                uhol += dielU;
                gama = 2.0 * Math.PI - uhol - beta;
                mi = Math.asin(da * Math.sin(gama) / radius);
                theta = Math.PI / 2.0 - uhol + /* uholR - */ mi;
//                System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1 + " Beta : " + beta + " Gama : " + gama);
                cislo++;
            }


            // horny rovny usek lava strana
//            while (uhol >= 2 * Math.PI - uholDR1 && uhol < 2 * Math.PI && cislo < 600) {
            while (uhol >= 2 * Math.PI - uholDR1 && uhol < 2 * Math.PI && cislo < pocetDielikov) {
//                System.out.println("Uhol : " + uhol + " Dielik : " + dielik + " UholDR1 : " + uholDR1);
//                (int) (oknoSirka / 2 + rozstup), okraj);
                body[cislo] = new Bod((int)(oknoSirka / 2.0 + (oknoVyska / 2.0 - okraj) * Math.tan(uhol)), okraj);    //  .xOva = (int)(oknoSirka / 2 - 3 + rozstup);
//                  body[cislo].yOva = okraj;
                uhol += dielU;
                cislo++;
            }
        }

        void CifernikO(int okraj, int oknoSirka, int oknoVyska) {  // okruhly cifernik
            cislo = 0;
            double polomer = oknoSirka / 2.0 - okraj;
            if(oknoVyska < oknoSirka)
                polomer = oknoVyska / 2.0 - okraj;

            for(int i = 0; i < pocetDielikov; i++) {
            // for(int i = 0; i < 6000; i++) {
                // body[cislo] = new Bod((int)(oknoSirka / 2.0 + polomer * Math.cos(- Math.PI / 2.0 + cislo * Math.PI / 30.0)), (int)(oknoVyska / 2.0 + polomer * Math.sin(- Math.PI / 2.0 + cislo * Math.PI / 30.0)));
                body[cislo] = new Bod((int)(oknoSirka / 2.0 + polomer * Math.cos(- Math.PI / 2.0 + cislo * Math.PI / (pocetDielikov / 2.0))), (int)(oknoVyska / 2.0 + polomer * Math.sin(- Math.PI / 2.0 + cislo * Math.PI / (pocetDielikov / 2.0))));
                cislo++;
            }
        }
    }

// }

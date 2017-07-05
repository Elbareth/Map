package com.example.boruch.mapa;

import android.content.Context;

/**
 * Created by Boruch on 2017-06-11.
 */

public class Licznik implements Runnable {

    public static final long MILI_TO_MIN=60000; // Ile jest milisekund w minucie
    public static final long MILI_TO_HOURS=3600000; // ile jest milisekund w godzinie

    private Context context;
    private long start;
    private boolean isRun;

    public Licznik(Context context){
        this.context=context;
    }

    public void startTime(){
        start= System.currentTimeMillis(); // zwraca czas w milisekundach do startu określa w jakiej chwili rozpoczynamy odliczane
        isRun=true; // boolean
    }

    public void stopTime(){
        isRun=false;
    } //boolean

    @Override
    public void run(){
        while (isRun){ // boolean
            long sys = System.currentTimeMillis() - start; // obecny czas od początkowego
            int sec = (int) (sys/1000)%60;
            int min = (int) (sys/MILI_TO_MIN)%60;
            int hour = (int) (sys/MILI_TO_HOURS)%24;
            int mili = (int) sys%1000;
            try{
                ((MapsActivity)context).updateTime(String.format("%02d:%02d:%02d:%03d",hour,min,sec,mili)); // format mojej godziny przesyłam do Contextu akttywności głównej tam mam metodę aktualizującą czas i wyświetlającą go
                Thread.sleep(10); // uśpij wątek na 10 milisekund
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        try{
            Thread.sleep(10); // Usypiam na kolejne 10 milisek
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

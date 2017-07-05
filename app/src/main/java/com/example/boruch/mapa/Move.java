package com.example.boruch.mapa;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Boruch on 2017-06-13.
 */

public class Move extends Activity implements SensorEventListener{

    Bitmap ludzik,back; // Rysunki przedstawiające ludzika i tło
    SensorManager sensorManager; // Do czujnika
    DrawLudzik draw; // obiekt klasy
    float x,y, sensorX, sensorY;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        try{
            Thread.sleep(20); // Próbuje uspać wątek na 20 milisekund
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        catch (ExceptionInInitializerError e){
            e.printStackTrace();
        }
        sensorX = event.values[1]; // Ustawiam współrzędnymi
        sensorY = event.values[0];
    }

    public class DrawLudzik extends SurfaceView implements Runnable{ // klasa do rysowania ludzika według położenia telefonu

        SurfaceHolder surfaceHolder; // Obiekt klasy służy do kontroli co znajduje się na wyświetlaczu
        Thread thread =null; // Nowy wątek
        boolean isRun = true;

        DrawLudzik(Context context){
            super(context);
            surfaceHolder = getHolder(); // SurfaceView
        }
        public void pause(){
            isRun=false; // boolean
            while (true){
                try{
                    thread.join(); // Czekamy aż wątek umrze
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                catch (ExceptionInInitializerError e){
                    e.printStackTrace();
                }
                break;
            }
            thread = null; // zerujemy wątek
        }
        public void resume(){
            isRun = true; // boolean
            thread = new Thread(this); //Tworzymy nowy wątek
            thread.start(); // Zaczynamy nowy wątek
        }
        @Override // Implementacja Runnable wywoływane przy thread.start()
        public void run(){
            while (isRun){ // W zależności od boolean
                if(!surfaceHolder.getSurface().isValid()){ // Dostęp do powierzchni zwraca true jeśli powierzchnia jest prawidlowa to
                    continue; // kontunuuj
                }
                Canvas canvas = surfaceHolder.lockCanvas(); // Canvas służy do rysowania
                canvas.drawColor(Color.LTGRAY); // Tło obecnie nie widoczne było w jasno szarym kolorze
                float startX = 50, startY =50;
                canvas.drawBitmap(back, 0, 0, null);// Ustawiam tło na obrazek przedstawiający bieżnie róg obrazka jest w punkcie 0 0
                canvas.drawBitmap(ludzik, startX+sensorX * 10, startY + sensorY *10, null); // Rysuje ludzika zaczyna rysować w punkcje 50 50 a potem w zależności od czujnika
                surfaceHolder.unlockCanvasAndPost(canvas); // Koniec edycji kanwy
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // Czujniki
        if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size()!=0){ // Jeśli mamy aceleromtetr
            Sensor sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0); // To nasz czujnik będzie ackelerometrem
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL); // SDN zmiany ekranu tworzymy listener dla naszego czujnika
        }
        ludzik = BitmapFactory.decodeResource(getResources(),R.drawable.ludzik); // ustawiamy obazkami odpowiednie bitmapy
        back = BitmapFactory.decodeResource(getResources(),R.drawable.lekkoatletyka);
        x=y=sensorX=sensorY=0;
        draw = new DrawLudzik(this);
        if(draw!=null){
            draw.resume(); // wywołujemy tą metodę d tworzenie wątku
            setContentView(draw); // Widzimy to co znajduje się  w klasie draw
        }

    }
    @Override
    protected void onPause(){
        sensorManager.unregisterListener(this); // Nie obserwuje czujników
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}

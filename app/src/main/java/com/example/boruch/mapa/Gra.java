package com.example.boruch.mapa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;

public class Gra extends Activity {

    Draw ludzik; // Obiekt klasy Draw z ludzikiem na powierzchni

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ludzik = new Draw(this);
        setContentView(ludzik); // To obiekt klasy Draw staje się widokiem
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // Ustawiamy ekran w pozycji horyzontalnej zostało to też zaznaczone w manifeście
        Intent intent = new Intent(this,Move.class); // Przenosimy się do klasy Move która porusza ludzikiem
        startActivity(intent);
    }
}

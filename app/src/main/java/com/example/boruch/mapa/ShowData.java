package com.example.boruch.mapa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShowData extends Activity { // Baza danych oparta na plikach

    TextView textView;
    View view;

    public void readFile(View view) throws FileNotFoundException{
        StringBuilder text = new StringBuilder();
        try{
            InputStream inputStream = openFileInput("data.txt"); // otwieram plik
            if(inputStream!=null){ // jeśli się udało
                InputStreamReader isr = new InputStreamReader(inputStream); // odczytywanie znaków
                BufferedReader bufferedReader = new BufferedReader(isr); // bufor
                String line=null;
                while ((line = bufferedReader.readLine())!=null){ // dopóki czytana linia nie będzie pusta
                    text.append(line); // Wprowadź tekst do pliku
                    text.append('\n');
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        textView = (TextView) findViewById(R.id.info);
        textView.setText(text); // pokaż co zostało odczytane
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        try{
            readFile(view); // Próbujemy naszą metodę odczytującą zawartość pliku
        }
        catch (IOException e){
            Toast.makeText(this,"Nie udało się otworzyć pliku",Toast.LENGTH_SHORT).show(); // Jeśli się nie powiedzie wyświetlamy toast
            e.printStackTrace();
        }
    }
}

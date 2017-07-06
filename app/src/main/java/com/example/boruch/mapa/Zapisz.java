package com.example.boruch.mapa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class Zapisz extends Activity  implements OnItemSelectedListener{

    TextView textView, textView2;
    EditText editText;
    Spinner spinner;
    String curr;
    String cos2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zapisz);
        textView = (TextView) findViewById(R.id.czas); // do wyświetlanai czasu
        editText = (EditText) findViewById(R.id.odczyt); // do nadawania tytuł trasy
        spinner = (Spinner) findViewById(R.id.spin); // spinner
        ArrayAdapter <String> adapter = new ArrayAdapter<>(Zapisz.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.transport)); // zostanie wyświetlone to co jest w string
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // widok listy rozwijanej
        spinner.setAdapter(adapter);
        curr = spinner.getSelectedItem().toString(); // Co obecnie zostało wybrane
        String cos = getIntent().getStringExtra("czas"); // Otrzymuje czas z Maps Activity
        if(cos!=null){
            textView.setText(cos); // ustawiam tym moje textView
        }
        textView2 = (TextView) findViewById(R.id.dystnas);
        cos2 = getIntent().getStringExtra("dystans"); // Z MapsActivity otrzymuje też wyliczony dystans
        Toast.makeText(this,"Mój distance to "+ cos2, Toast.LENGTH_SHORT);
        textView2.setText(cos2 + " km"); // konwertuje float na String i wstawiam do textView
    }
    @Override
    public void onItemSelected(AdapterView<?> arg0,View view, int pos, long id){
        curr = spinner.getSelectedItem().toString(); // Wybrana opcja
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Nic
    }

    public void zapisz2(View view) {
        //String wyswietl = editText.getText().toString();
        //Zapis do bazy danych
        String all = editText.getText().toString()+"    "+curr+"    "+textView.getText().toString()+"   "+textView2.getText().toString(); // Tworzę napis
        try{
            OutputStreamWriter out =new OutputStreamWriter(openFileOutput("data.txt",MODE_APPEND)); // Otwieram plik do dopisywania
            out.write(all);
            out.write('\n');
            out.close(); // Zamykam plik
            Toast.makeText(this,"Zapisano dane do bazy",Toast.LENGTH_SHORT).show(); // Wyświetlam informacje
        }
        catch (IOException e){
            Toast.makeText(this,"Niestety Twoich wyników nie udało się zapisać",Toast.LENGTH_SHORT).show(); // W przeciwnym razie ta informacje
            e.printStackTrace();
        }
    }
}

    public void onNothingSelected(AdapterView<?> parent) {
        // Nic
    }

    public void zapisz2(View view) {
        //String wyswietl = editText.getText().toString();
        //Zapis do bazy danych
        String all = editText.getText().toString()+"    "+curr+"    "+textView.getText().toString()+"   "+textView2.getText().toString(); // Tworzę napis
        try{
            OutputStreamWriter out =new OutputStreamWriter(openFileOutput("data.txt",MODE_APPEND)); // Otwieram plik do dopisywania
            out.write(all);
            out.write('\n');
            out.close(); // Zamykam plik
            Toast.makeText(this,"Zapisano dane do bazy",Toast.LENGTH_SHORT).show(); // Wyświetlam informacje
        }
        catch (IOException e){
            Toast.makeText(this,"Niestety Twoich wyników nie udało się zapisać",Toast.LENGTH_SHORT).show(); // W przeciwnym razie ta informacje
            e.printStackTrace();
        }
    }
}

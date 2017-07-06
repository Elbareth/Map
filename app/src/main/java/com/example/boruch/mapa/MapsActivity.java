package com.example.boruch.mapa;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private GPS gps;
    private Location location;
    double longatut,latitute;
    double startlong =0, startlat =0, stoplat, stoplong;
    private TextView textView;
    private Licznik licz;
    private Thread thread;
    private Context context;
    double distance;
    double lat, longat;
    String loc;

    public void menu(View view){ // Przycisk po naciśnięciu którego otworzy mi się dialog
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(this); // Buduje dialog
        final String[] options = {"Zapisz","Baza danych","Szukaj","Gra","Anuluj"}; // Jakie opcje będą w dialogu
        alterDialog.setTitle("Wybierz opcje");
        alterDialog.setCancelable(false);
        alterDialog.setItems(options, new DialogInterface.OnClickListener() { // "Nasłuchuje który z przycisków został wybrany
            @Override
            public void onClick(DialogInterface dialog, int position) { // Gdy nastąpi zdarzenie - jakiś przycisk wybierzemy
                if(position==0){ // Otworzy nam sie zapisz
                    context.getApplicationContext();
                    Intent intent = new Intent(context,Zapisz.class); // Otwiera nam się wtedy klasa Zapisz
                    intent.putExtra("czas",textView.getText().toString()); // Przesyłamy do niej iformacje z licznika, pod słowem czas ukryty będzie stan
                    intent.putExtra("dystans",String.valueOf(distance)); // Przesyłamy również informacje o obliczonym dystansie
                    startActivity(intent); // Po przesłaniu informacji zaczynamy nową aktywnść
                }
                if(position==1){  // Oworzy nam się baza danych
                    context.getApplicationContext();
                    Intent intent = new Intent(context,ShowData.class);
                    startActivity(intent);
                }
                if(position==2){ // Otworzy nam się opcja wyszukiwania miejsc
                    AlertDialog.Builder build = new AlertDialog.Builder(context); // Będziemy wpisywać interesujące nas miejsca w dialog
                    build.setTitle("Wyszukiwanie");
                    final EditText editText = new EditText(context); // Towrzymy pole tekstowe
                    editText.setInputType(InputType.TYPE_CLASS_TEXT ); // Co będzie na wejściu?
                    build.setView(editText); // Co zostanie zamieszczone w dialogu
                    build.setPositiveButton("Szukaj", new DialogInterface.OnClickListener() { // Tworze listenera nasłuchującego czy nacisnę opcje Szukaj
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loc = editText.getText().toString(); // Jeśli nacisnęłam to ładuje String
                            show(loc); // Uruchamiam metodę show z loc jako argumentem
                        }
                    });
                    build.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { // Tworze listener czekający aż nacisnę anuluj
                            dialog.cancel(); // Jeśli tak zamknij dialog
                        }
                    });

                    build.show(); // Pokaż dialog z Szukaj i Anuluj
                }
                if(position==3){ // Otwiera się Gra
                    context.getApplicationContext();
                    Intent intent = new Intent(context,Gra.class); // Przechodzę do klasy gra
                    startActivity(intent);
                }
            }
        });
        AlertDialog ad = alterDialog.create(); // Tworzę dialog z 5 opcjami
        ad.show(); // Pokaż dialog
    }

    public void start(View view){ // Przycisk start odpowiedzialny za licznik i początek trasy
        if(licz==null){ // Jeśli nie mam licznika
            licz = new Licznik(context); // Utwórz obiekt klasy Licznik
            thread = new Thread(licz); // Tworzymy wątek którego parametrem jest licznik
            thread.start(); // zaczynam nowy wątek z licz
            licz.startTime(); // Wywołuję metodę startTime
        }
        gps = new GPS(getApplicationContext()); // Tworzę nowy obiekt GPS
        location = gps.getLocation(); // Pobieram lokalizację dla poczatku trasy
        startlat = location.getLatitude();
        startlong = location.getLongitude();
    }

    public void stop(View view){ // Gdy nacisnę Stop licznik się zatrzyma i zostanie pobrany koniec trasy
        if(licz!=null){
            licz.stopTime();
            thread.interrupt(); // Przerwanie wątku
            thread = null;
            licz = null;
        }
        gps = new GPS(getApplicationContext());
        location = gps.getLocation();
        stoplat = location.getLatitude();
        stoplong = location.getLongitude();
        onResume(); // Wywołuje metodę onResume, która zwiera w sobie metodę do obliczania odelgłości
    }

    public void updateTime(final String time){ // Metoda do aktualizacji czasu
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                textView.setText(time); // Co określony czas zmień napis
            }
        });
    }

    private double calculate(double latitute, double longatut, double startlat, double startlong){ // Do obliczania odległości
        Location l1 = new Location("Początek");
        Location l2 = new Location("Obecna pozycja");
        l1.setLatitude(startlat);
        l1.setLongitude(startlong);
        l2.setLongitude(longatut);
        l2.setLatitude(latitute);
        Toast.makeText(this, "Współrzędne początku "+ l1.getLatitude() + " "+ l1.getLongitude()+ " oraz współrzędne końca " + l2.getLatitude() + " "+ l2.getLongitude(), Toast.LENGTH_LONG);
        distance = l1.distanceTo(l2);
        Toast.makeText(this,"Twój dystans to " + distance, Toast.LENGTH_SHORT);
        return distance / 1000; // Zwraca dystans w kilometrach
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        gps = new GPS(getApplicationContext()); // Tworzę obiekt klasy GPS
        location = gps.getLocation(); // Pobieram pozycję i ustawiam długośc i szerokość geograficzną
        latitute = location.getLatitude();
        longatut = location.getLongitude();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        textView = (TextView) findViewById(R.id.licznik); // Towrzę miejsce na licznik
        context =this;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(startlong!=0&&startlat!=0){
            distance = calculate(stoplat,stoplong,startlat,startlong); // wywołuję metodę calulate do obliczania dystansu
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void show(String loc){ // Metoda do pokazywania wyszukanych przez Nas miejsc
        if(loc!=null||!loc.equals("")){
            Geocoder geocoder = new Geocoder(this); // Tworzę obiekt klasy geocoder który służy do transformacji nazwy np. miasta na współrzędne
            List<Address> adresList = null; // Tworzę liste w której zamieszcze wyszukane adresy
            try{ // To jest w try - catch bo moze się nie powieść wyszukanie
                adresList = geocoder.getFromLocationName(loc,1); // do mojej listy dodaje lokalizacje, geocoder transformuje na współrzędne nazwę zawartą w loc, a 1 oznacza że wyszukuje 1 miejsce
                while (adresList.size()==0){ // Jeśli nic nie zostanie dodane do listy
                    adresList = geocoder.getFromLocationName(loc,1); // Próbuj dodać dalej
                }
                if(adresList.size()>0){ // Coś znajduje sie  w liscie
                    Address adres = adresList.get(0); // Wyłuskujemy element pierwszy listy
                    LatLng lat = new LatLng(adres.getLatitude(), adres.getLongitude()); // Tworzę obiekt klasy z wyszukaną długością i szerokością geograficzną, klasa latlng służy do przechowywania długości i szerokości
                    //if (mMap!=null){
                    mMap.addMarker(new MarkerOptions().position(lat).title(loc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); // Ustawiamy marker w wyszukane miejsce
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(lat)); // Przestawiamy kamere w wyszukane miejsce
                    //}
                }
            }
            catch (IOException e){
                e.printStackTrace(); // Jeśli się nie powiedzie napisz dlaczego
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitute, longatut); // Ustawiamy na nasza długosć i szerokość geograficzną
        mMap.addMarker(new MarkerOptions().position(sydney).title("Jesteś tu")); // Ustawiamy marker
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)); // Ustawiamy kamerę
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) ); // Przybliżamy ja do takiej odległości
    }
}

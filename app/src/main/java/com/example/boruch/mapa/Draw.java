package com.example.boruch.mapa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;
import android.widget.ImageView;

/**
 * Created by Boruch on 2017-06-13.
 */

public class Draw extends SurfaceView {

    Bitmap ludzik; // Tworze mapę pikseli dla ludzika

    public Draw(Context context){
        super(context);
        ludzik = BitmapFactory.decodeResource(getResources(),R.drawable.ludzik); // na podstawie wejscia zostane utworzona bitmapa
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    } // Rysuje ludzika na powierzchni ekranu
}

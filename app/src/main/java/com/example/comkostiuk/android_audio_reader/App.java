package com.example.comkostiuk.android_audio_reader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Activité permettant de lancer l'application seulement.
 * Sa seule fonction est de démarrer les service AppService.
 * */

public class App extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        startService(new Intent(this, AppService.class));

        finish();

    }

}

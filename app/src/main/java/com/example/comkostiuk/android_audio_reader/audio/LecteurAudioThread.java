package com.example.comkostiuk.android_audio_reader.audio;

import android.media.MediaPlayer;
import android.os.Environment;
import java.io.IOException;

/**
 * Created by mkostiuk on 27/04/2017.
 */

/**
 * Classe décrivant un Thread permettant de lancer la lecture d'un fichier audio
 * On passe à ce Thread un MediaPlayer déjà instancié ainsi que du numéro du fichier à lire
 * */
public class LecteurAudioThread extends Thread {

    private int numFichiers;
    private MediaPlayer mp;

    public LecteurAudioThread(int n, MediaPlayer m) {
        numFichiers = n;
        mp = m;
    }
    @Override
    public void run() {
        try {
            mp.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/FileReceiver/Audio/test" + numFichiers + ".mp3");
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

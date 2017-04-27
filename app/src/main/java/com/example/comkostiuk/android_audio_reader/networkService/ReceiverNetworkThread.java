package com.example.comkostiuk.android_audio_reader.networkService;


import android.os.Environment;
import android.os.StrictMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import xdroid.toaster.Toaster;

/**
 * Created by mkostiuk on 25/04/2017.
 */

/*
* Classe permettant de sauvegarder les fichiers reçus dans la mémoire interne de l'appareil.
* Il est possible de recevoir plusieurs fichiers d'affilé, on ajout un numéro à la fin afin
* de les différencier.
*
* Exemple: Lors d'un cours, le professeur envoie plusieurs fichiers audios explicatifs, un par slide
*  ils seront tous sauvegardés au même endroit et avec le même nom, mais avec un numéro différent.
* */
public class ReceiverNetworkThread implements Runnable  {

    private Socket socket;
    private byte[] buffer;
    private InputStream inputStream;
    private OutputStream outputStream;


    public ReceiverNetworkThread(Socket s) throws IOException {

        Toaster.toast("Demmarage thread...");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        socket = s;
        inputStream = socket.getInputStream();
        File f = new File(nameFic());
        f.setWritable(true);
        outputStream = new FileOutputStream(f);
        buffer = new byte[16192];

        Toaster.toast("Connexion établie!!!");
    }

    @Override
    public void run() {
        int n;
        try {
            while ((n = inputStream.read(buffer)) != -1)
                outputStream.write(buffer,0,n);

            outputStream.close();
            inputStream.close();
            socket.close();
            Toaster.toast("Fichier reçu!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //On construit le nom du fichier selon le nombre de fichiers contenus dans le répertoire Audio
    public String nameFic() {
        String ret;

        File dir = new File(Environment.getExternalStorageDirectory().getPath()
                + "/FileReceiver/Audio/");

        int nb = dir.listFiles().length;

        ret = Environment.getExternalStorageDirectory().getPath() + "/FileReceiver/Audio/test" + nb + ".mp3";

        return ret;
    }
}

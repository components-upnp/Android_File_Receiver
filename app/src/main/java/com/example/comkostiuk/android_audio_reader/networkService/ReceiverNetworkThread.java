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
        File f = new File(Environment.getExternalStorageDirectory().getPath() + "/test.mp3");
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
}

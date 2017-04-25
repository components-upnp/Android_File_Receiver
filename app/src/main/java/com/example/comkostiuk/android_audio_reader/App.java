package com.example.comkostiuk.android_audio_reader;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.comkostiuk.android_audio_reader.networkService.ReceiverNetworkThread;
import com.example.comkostiuk.android_audio_reader.upnp.Service;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.util.Timer;
import java.util.TimerTask;

import xdroid.toaster.Toaster;

public class App extends AppCompatActivity {

    private Service service;
    private ServiceConnection serviceConnection;
    private byte[] buffer = new byte[16192];
    private OutputStream outputStream;
    private InputStream inputStream;

    private Socket socket;
    private BufferedReader fileReader;
    private BufferedReader sortie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

       startService(new Intent(this, AppService.class));

       finish();

       /* StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        service = new Service();
        serviceConnection = service.getService();

        getApplicationContext().bindService(new Intent(this, AndroidUpnpServiceImpl.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE);

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                set();
            }
        }, 5000);*/

    }

    public void set() {
        service.getRecorderLocalService().getManager().getImplementation().getPropertyChangeSupport()
                .addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("file")) {
                            Toaster.toast("Connexion serveur distant...");
                            String address = (String) evt.getNewValue();
                            int port =  Integer.getInteger((String) evt.getOldValue());

                                //socket = new Socket("192.168.43.223", 10302);
                                //new Thread( new ReceiverNetworkThread(socket)).start();


                        }
                        else if (evt.getPropertyName().equals("receiving")) {
                            Toaster.toast("Reception fichier...");
                            try {
                                socket = new Socket("192.168.43.223", 10302);
                                new Thread( new ReceiverNetworkThread(socket)).start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }
}

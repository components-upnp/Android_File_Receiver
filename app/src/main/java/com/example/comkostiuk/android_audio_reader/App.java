package com.example.comkostiuk.android_audio_reader;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.comkostiuk.android_audio_reader.upnp.Service;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class App extends AppCompatActivity {

    private Service service;
    private ServiceConnection serviceConnection;
    private byte[] buffer = new byte[16192];
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

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
        }, 5000);

    }

    public void set() {
        service.getRecorderLocalService().getManager().getImplementation().getPropertyChangeSupport()
                .addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("receiving")) {
                            boolean value = (boolean) evt.getNewValue();

                            if (value) {
                                try {
                                    //On sauvegarde le fichier à la racine
                                    File f = new File(Environment.getExternalStorageDirectory().getPath() + "/test.mp3");
                                    f.setWritable(true);
                                    outputStream = new FileOutputStream(f);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                try {
                                    outputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else if (evt.getPropertyName().equals("file")) {
                            buffer = (byte[]) evt.getNewValue();
                            try {
                                outputStream.write(buffer);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}

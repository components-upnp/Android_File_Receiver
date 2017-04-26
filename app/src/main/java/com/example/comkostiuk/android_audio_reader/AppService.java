package com.example.comkostiuk.android_audio_reader;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.comkostiuk.android_audio_reader.networkService.ReceiverNetworkThread;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import xdroid.toaster.Toaster;

/**
 * Created by mkostiuk on 25/04/2017.
 */

public class AppService extends Service {

    private Intent intent;
    private com.example.comkostiuk.android_audio_reader.upnp.Service service;
    private ServiceConnection serviceConnection;
    private ReceiverNetworkThread receiverNetworkThread;
    private Socket socket;
    private MediaPlayer mediaPlayer;
    private Context context;
    private  Logger log;

    @Override
    public void onCreate(){


        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/FileReceiver/");

        if (!dir.exists()) {
            dir.mkdir();
            dir.setReadable(true);
            dir.setExecutable(true);
        }

        service = new com.example.comkostiuk.android_audio_reader.upnp.Service(log);
        serviceConnection = service.getService();

        context = this;

        mediaPlayer = new MediaPlayer();

        getApplicationContext().bindService(new Intent(this, AndroidUpnpServiceImpl.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE);

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                set();

                //On arrete le service au bout de deux heures
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        onDestroy();
                    }
                }, 2 * 60 * 60 * 1000);
            }
        }, 5000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent i) {
        intent = i;
        return null;
    }

    public void set() {
        service.getRecorderLocalService().getManager().getImplementation().getPropertyChangeSupport()
                .addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("file")) {
                            log.log(Level.INFO,(String) evt.getNewValue());
                            if ( ((String)evt.getNewValue()).equals("fin") ) {
                                try {

                                    mediaPlayer.setDataSource(openFileOutput(Environment.getExternalStorageDirectory().getPath() + "/test.mp3", MODE_PRIVATE).getFD());
                                    mediaPlayer.prepare();
                                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            Toaster.toast("Lecture fichier audio!!!");
                                            mediaPlayer.start();
                                        }
                                    });

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Toaster.toast("Connexion serveur distant...");
                                String address = (String) evt.getNewValue();
                                int port = Integer.getInteger((String) evt.getOldValue());
                            }


                        }
                        else if (evt.getPropertyName().equals("receiving")) {

                            try {
                                socket = new Socket("192.168.43.223", 10302);
                                receiverNetworkThread = new ReceiverNetworkThread(socket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Toaster.toast("Reception fichier...");
                            new Thread(receiverNetworkThread).start();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        service.stop();
        stopSelf();
    }
}

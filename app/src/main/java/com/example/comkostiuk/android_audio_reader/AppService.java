package com.example.comkostiuk.android_audio_reader;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.comkostiuk.android_audio_reader.audio.LecteurAudioThread;
import com.example.comkostiuk.android_audio_reader.networkService.ReceiverNetworkThread;

import org.fourthline.cling.android.AndroidUpnpServiceImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
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


/**
 *  Service principal, il transmet les informations entre les différents modules.
 *  Il initialise le service UPnP et écoute ses évènements.
 *  Il est le repsonsable de la création des Threads de réception et de lecture des fichier, ainsi que
 *  de l'initialisation des répertoires utiles pour les données de l'application.
 * */
public class AppService extends Service {

    public static final int NOTIFICATION_ID = 1;

    public static final String ACTION_1 = "action_1";


    private Intent intent;
    private com.example.comkostiuk.android_audio_reader.upnp.Service service;
    private ServiceConnection serviceConnection;
    private ReceiverNetworkThread receiverNetworkThread;
    private Socket socket;
    private MediaPlayer mediaPlayer;
    private Context context;
    private  Logger log;
    private ArrayList<Thread> fileAudio;
    private int nbFic;
    private boolean isPlaying;
    private Thread lectureCourante;

    @Override
    public void onCreate(){


        //On commence par préparer les répertoires de l'application.
        //On initialise aussi un Logger
        File appDir = new File(Environment.getExternalStorageDirectory().getPath() + "/FileReceiver/");
        File audioDir = new File(Environment.getExternalStorageDirectory().getPath() + "/FileReceiver/Audio/");
        try {
            Handler fh = new FileHandler(Environment.getExternalStorageDirectory().getPath() + "/FileReceiver/log.log", false);
            log = Logger.getLogger("com.example.comkostiuk_audio_reader");
            log.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!appDir.exists()) {
            appDir.mkdir();
            appDir.setReadable(true);
            appDir.setExecutable(true);
        }

        if (!audioDir.exists()) {
            audioDir.mkdir();
            audioDir.setReadable(true);
            audioDir.setExecutable(true);
        } else {
            for (File f : audioDir.listFiles())
                f.delete();
        }

        //Il n'y a pour l'instant aucun fichiers reçus
        nbFic = 0;

        service = new com.example.comkostiuk.android_audio_reader.upnp.Service(log);
        serviceConnection = service.getService();

        context = this;

        //création du lecteur audion, il ne lit pas alors isPlaying est à false
        mediaPlayer = new MediaPlayer();
        isPlaying = false;

        //On lance le service UPnP
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

        fileAudio = new ArrayList<>();
    }

    //On récupère l'Intent créé avec l'application
    @Nullable
    @Override
    public IBinder onBind(Intent i) {
        intent = i;
        return null;
    }

    /*Fonction permettant de mettre en place le listener sur la fin de lecture d'un fichier,
    * qui permet de lancer la lecture du fichier suivant ou d'attendre un nouveau fichier.
    *
    *
     *  */

    public void set() {

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toaster.toast("Test fin lecture!!!");

                mediaPlayer.stop();
                mediaPlayer.reset();

                //Si des fichiers sont en attente de lecture, on passe au suivant
                if (!fileAudio.isEmpty()) {
                    Toaster.toast("Lecture fichier: "+nbFic);
                    lectureCourante = fileAudio.remove(0);
                    lectureCourante.start();
                    isPlaying = true;
                }
                else {  //sinon on stoppe les lectures et on attend
                    isPlaying = false;
                }
            }
        });

        //Listener décrivant les actions à faire lors de la réception d'évènements UPnP
        service.getRecorderLocalService().getManager().getImplementation().getPropertyChangeSupport()
                .addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        //Evénement de type file
                        if (evt.getPropertyName().equals("file")) {
                            log.log(Level.INFO,(String) evt.getNewValue());
                            //Traitement événement valeur == fin
                            //Un fichier a été reçu, on créé un Thread
                            if ( ((String)evt.getNewValue()).equals("fin") ) {
                                fileAudio.add(new LecteurAudioThread(nbFic, mediaPlayer));
                                nbFic++;

                                if (! isPlaying) {
                                    isPlaying = true;
                                    lectureCourante = fileAudio.remove(0);
                                    lectureCourante.start();
                                }
                            }
                            else if (((String)evt.getNewValue()).equals("lol")) {
                                displayNotification(context);
                            }
                            else {
                                Toaster.toast("Connexion serveur distant...");
                                String address = (String) evt.getNewValue();
                                int port = Integer.getInteger((String) evt.getOldValue());
                            }


                        }
                        else if (evt.getPropertyName().equals("receiving")) {
                            if ((boolean) evt.getNewValue()) {
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
                    }
                });
    }


    //Comportement de l'application lors de son interruption
    @Override
    public void onDestroy() {
        service.stop();
        stopSelf();
    }


    //fonction permettant d'afficher une notification test à l'utilisateur lors d'un évènement
    public static void displayNotification(Context context) {

        Intent action1Intent = new Intent(context, NotificationActionService.class)
                .setAction(ACTION_1);

        PendingIntent action1PendingIntent = PendingIntent.getService(context, 0,
                action1Intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.im)
                        .setContentTitle("Sample Notification")
                        .setContentText("Notification text goes here")
                        .addAction(new NotificationCompat.Action(R.drawable.im,
                                "Action 1", action1PendingIntent))
                        .setContentIntent(action1PendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


    //Classe interne permettant de créer un service gérant les actions faites sur la notification
    //L'utilisateur peut passer au fichier audio suivant ou précédent
    public static class NotificationActionService extends IntentService {
        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String action = intent.getAction();
            Toaster.toast("Received notification action: " + action);
            if (ACTION_1.equals(action)) {
                // TODO: handle action 1.
                System.out.println("Action 1 notification");
            }
        }
    }
}

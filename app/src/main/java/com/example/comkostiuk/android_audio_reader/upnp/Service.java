package com.example.comkostiuk.android_audio_reader.upnp;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UDN;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by comkostiuk on 20/04/2017.
 */

public class Service {
    private AndroidUpnpService upnpService;
    private UDN udnRecorder;
    private ServiceConnection serviceConnection;


    public Service() {


        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                upnpService = (AndroidUpnpService) service;


                LocalService<FileReceiverController> remoteControllerService = getRecorderLocalService();

                // Register the device when this activity binds to the service for the first time
                if (remoteControllerService == null) {
                    try {
                        System.err.println("CREATION DEVICE!!!");
                        udnRecorder = new SaveUDN().getUdn();
                        LocalDevice remoteDevice = FileReceiverDevice.createDevice(udnRecorder);

                        upnpService.getRegistry().addDevice(remoteDevice);

                    } catch (Exception ex) {
                        System.err.println("Creating Android remote controller device failed !!!");
                        return;
                    }
                }

                System.out.println("Creation device reussie...");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                upnpService = null;
            }
        };
    }

    public LocalService<FileReceiverController> getRecorderLocalService() {
        if (upnpService == null)
            return null;

        LocalDevice remoteDevice;
        if ((remoteDevice = upnpService.getRegistry().getLocalDevice(udnRecorder, true)) == null)
            return null;

        return (LocalService<FileReceiverController>)
                remoteDevice.findService(new UDAServiceType("FileReceiverController", 1));
    }

    public LocalService<FileReceivedService> getFileReceivedService() {
        if (upnpService == null)
            return null;

        LocalDevice remoteDevice;
        if ((remoteDevice = upnpService.getRegistry().getLocalDevice(udnRecorder, true)) == null)
            return null;

        return (LocalService<FileReceivedService>)
                remoteDevice.findService(new UDAServiceType("FileReceivedService", 1));
    }

    public ServiceConnection getService() {
        return serviceConnection;
    }

    public void stop() {
        upnpService.get().shutdown();
    }

    public UDN getUdnRecorder() {
        return udnRecorder;
    }
}

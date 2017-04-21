package com.example.comkostiuk.android_audio_reader.upnp;

import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

/**
 * Created by comkostiuk on 20/04/2017.
 */

public class FileReceiverDevice {
    static LocalDevice createDevice(UDN udn)
            throws ValidationException, LocalServiceBindingException {

        DeviceType type =
                new UDADeviceType("FileReceiverController", 1);

        DeviceDetails details =
                new DeviceDetails(
                        "File Receiver",
                        new ManufacturerDetails("IRIT"),
                        new ModelDetails("AndroidController", "Reçoit un fichier", "v1")
                );

        LocalService service =
                new AnnotationLocalServiceBinder().read(FileReceiverController.class);

        service.setManager(
                new DefaultServiceManager<>(service, FileReceiverController.class)
        );

        return new LocalDevice(
                new DeviceIdentity(udn),
                type,
                details,

                service
        );
    }
}
package com.example.comkostiuk.android_audio_reader.upnp;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;

import java.beans.PropertyChangeSupport;

/**
 * Created by mkostiuk on 26/06/2017.
 */


@UpnpService(
        serviceId = @UpnpServiceId("FileReceivedService"),
        serviceType = @UpnpServiceType(value = "FileReceivedService", version = 1)
)
public class FileReceivedService {

    private final PropertyChangeSupport propertyChangeSupport;

    public FileReceivedService() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable(name = "PathFileReceived")
    private String pathFileReceived = "";

    @UpnpAction(name = "SetPathFileReceived")
    public void setPathFileReceived(@UpnpInputArgument(name = "PathFileReceived") String p) {
        pathFileReceived = p;
        getPropertyChangeSupport().firePropertyChange("PathFileReceived", "", pathFileReceived);
    }
}

package com.example.comkostiuk.android_audio_reader.upnp;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;

import java.beans.PropertyChangeSupport;

/**
 * Created by comkostiuk on 20/04/2017.
 */

@UpnpService(
        serviceType = @UpnpServiceType(value = "FileReceiverController"),
        serviceId = @UpnpServiceId("FileReceiverController")
)
public class FileReceiverController {

    private final PropertyChangeSupport propertyChangeSupport;

    public FileReceiverController() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable(defaultValue = "null", sendEvents = false)
    private byte[] file = null;

    @UpnpStateVariable(defaultValue = "false", sendEvents = false)
    private boolean receiving = false;

    @UpnpAction
    public void setReceiving(@UpnpInputArgument(name = "NewReceivingValue") boolean newReceivingValue) {
        boolean oldValue = receiving;
        receiving = newReceivingValue;

        getPropertyChangeSupport().firePropertyChange("receiving",oldValue,receiving);
    }

    @UpnpAction
    public void setFile(@UpnpInputArgument(name = "NewFileValue") byte[] newFileValue) {
        file = newFileValue;
        getPropertyChangeSupport().firePropertyChange("file",null,file);
    }
}

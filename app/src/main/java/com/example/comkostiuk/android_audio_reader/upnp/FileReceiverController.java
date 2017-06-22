package com.example.comkostiuk.android_audio_reader.upnp;

import com.example.comkostiuk.android_audio_reader.xml.LecteurXml;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;
import org.xml.sax.SAXException;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by comkostiuk on 20/04/2017.
 */


/**
 * Description du service UPnP offert par le composant
 * */
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
    private String file = null;

    @UpnpStateVariable(defaultValue = "false", sendEvents = false)
    private boolean receiving = false;

    @UpnpAction
    public void setReceiving(@UpnpInputArgument(name = "NewReceivingValue") boolean newReceivingValue) {
        boolean oldValue = receiving;
        receiving = newReceivingValue;

        getPropertyChangeSupport().firePropertyChange("receiving",oldValue,receiving);
    }

    @UpnpAction
    public void setFile(@UpnpInputArgument(name = "NewFileValue") String newFileValue) throws IOException, SAXException, ParserConfigurationException {
        file = newFileValue;

        if (!file.equals("fin")) {
            LecteurXml l = new LecteurXml(file);
            HashMap<String,String> args = new HashMap<>();
            args.put("IP", l.getIp());
            args.put("FILENAME", l.getFileName());
            System.err.println("FILE NAME DE MERDE : " + l.getFileName());
            getPropertyChangeSupport().firePropertyChange("file", null, args);
        }
    }
}

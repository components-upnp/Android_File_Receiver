package com.example.comkostiuk.android_audio_reader.upnp;

import android.content.Context;
import android.os.Environment;

import org.fourthline.cling.model.types.UDN;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import xdroid.toaster.Toaster;

/**
 * Created by mkostiuk on 26/04/2017.
 */

public class SaveUDN {




    public UDN getUdn() throws IOException {

        UDN ret;
        File fi = new File(Environment.getExternalStorageDirectory().getPath() + "/FileReceiver/udn.txt");

        if (!fi.exists()) {
            fi.createNewFile();
            fi.setWritable(true);
        }

        InputStream is = new FileInputStream(fi);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = br.readLine();

        System.err.println("recup UDN");

        if ((line != null) || (line == "")) {
            ret =  UDN.valueOf(line);
        }
        else {
            ret = new UDN(UUID.randomUUID());
            OutputStream o = new FileOutputStream(fi);
            //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(o));
            o.write(ret.toString().getBytes());
        }
        Toaster.toast("UDN: " + ret.toString());
        return ret;
    }
}

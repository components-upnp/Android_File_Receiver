package com.example.comkostiuk.android_audio_reader.xml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by mkostiuk on 22/06/2017.
 */

public class LecteurXml {

    private String udn;
    private String ip;
    private String fileName;

    public LecteurXml(String xml) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {

            boolean isUdn = false;
            boolean isIp = false;
            boolean isFileName = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (qName.equalsIgnoreCase("UDN"))
                    isUdn = true;
                if (qName.equalsIgnoreCase("IP"))
                    isIp = true;
                if (qName.equalsIgnoreCase("FILENAME"))
                    isFileName = true;
            }

            @Override
            public void characters(char ch[], int start, int length) {
                if (isFileName) {
                    isFileName = false;
                    fileName = new String(ch, start, length);
                    System.err.println(fileName);
                }
                if (isIp) {
                    isIp = false;
                    ip = new String(ch,start, length);
                    System.err.println(ip);
                }
                if (isUdn) {
                    isUdn = false;
                    udn = new String(ch, start, length);
                }
            }
        };
        sp.parse(new InputSource(new StringReader(xml)), handler);
    }

    public String getFileName() {
        return fileName;
    }

    public String getIp() {
        return ip;
    }

    public String getUdn() {
        return udn;
    }
}

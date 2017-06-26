package com.example.comkostiuk.android_audio_reader.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by mkostiuk on 26/06/2017.
 */

public class GenerateurXml {

    public String getDocXml(String udn, String path) throws TransformerException, ParserConfigurationException {
        String namespace = "/";
        Document doc;
        DocumentBuilderFactory db = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        builder = db.newDocumentBuilder();
        doc = builder.newDocument();
        Element root = doc.createElementNS(namespace, "FileReceiver");
        doc.appendChild(root);

        Element u = doc.createElementNS(namespace, "UDN");
        root.appendChild(u);
        u.appendChild(doc.createTextNode(udn.toString()));

        Element f = doc.createElementNS(namespace, "PATHFILE");
        root.appendChild(f);
        f.appendChild(doc.createTextNode(path));

        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(source, result);
        return writer.toString();
    }
}

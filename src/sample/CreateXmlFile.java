package sample;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class CreateXmlFile {
    public static final String xmlFilePath = "C:\\Users\\User\\Documents\\xmlfile.xml";

    public CreateXmlFile() throws TransformerConfigurationException {
    }

    public static void create() {
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("appointments");
            document.appendChild(root);

            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);

            File file = new File(xmlFilePath);

            if (file.createNewFile()) {
                StreamResult streamResult = new StreamResult(file);

                // If you use
                // StreamResult result = new StreamResult(System.out);
                // the output will be pushed to the standard output ...
                // You can use that for debugging

                transformer.transform(domSource, streamResult);

                System.out.println("Done creating XML File");
            }
            else
                System.out.println("File already exists");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addElement(int y, int m, int d, int h, int min, String n)
            throws ParserConfigurationException, TransformerConfigurationException,
            TransformerException, SAXException, IOException {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFilePath);
        Element root = document.getDocumentElement();

        Element date = document.createElement("date");

        root.appendChild(date);
        Element year = document.createElement("year");
        Element month = document.createElement("month");
        Element day = document.createElement("day");
        Element hour = document.createElement("hour");
        Element minute = document.createElement("minute");
        Element note = document.createElement("note");

        year.appendChild(document.createTextNode(String.valueOf(y)));
        month.appendChild(document.createTextNode(String.valueOf(m)));
        day.appendChild(document.createTextNode(String.valueOf(d)));
        hour.appendChild(document.createTextNode(String.valueOf(h)));
        minute.appendChild(document.createTextNode(String.valueOf(min)));
        note.appendChild(document.createTextNode(n));

        date.appendChild(year);
        year.appendChild(month);
        month.appendChild(day);
        day.appendChild(hour);
        hour.appendChild(minute);
        minute.appendChild(note);


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(xmlFilePath));
        transformer.transform(domSource, streamResult);

        DOMSource source = new DOMSource(document);


    }

}

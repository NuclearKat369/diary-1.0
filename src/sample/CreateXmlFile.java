package sample;

import org.w3c.dom.*;
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
import javax.xml.xpath.*;
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

                transformer.transform(domSource, streamResult);

                System.out.println("Done creating XML File");
            } else
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

        Element appointment = document.createElement("appointment");

        root.appendChild(appointment);

//        Element year = document.createElement("year");
//        Element month = document.createElement("month");
//        Element day = document.createElement("day");
//        Element hour = document.createElement("hour");
//        Element minute = document.createElement("minute");
//        Element note = document.createElement("note");
//
//        year.appendChild(document.createTextNode(String.valueOf(y)));
//        month.appendChild(document.createTextNode(String.valueOf(m)));
//        day.appendChild(document.createTextNode(String.valueOf(d)));
//        hour.appendChild(document.createTextNode(String.valueOf(h)));
//        minute.appendChild(document.createTextNode(String.valueOf(min)));
//        note.appendChild(document.createTextNode(n));
//
//        appointment.appendChild(year);
//        year.appendChild(month);
//        month.appendChild(day);
//        day.appendChild(hour);
//        hour.appendChild(minute);
//        minute.appendChild(note);

        Attr year = document.createAttribute("year");
        Attr month = document.createAttribute("month");
        Attr day = document.createAttribute("day");
        Attr hour = document.createAttribute("hour");
        Attr minute = document.createAttribute("minute");
        Attr note = document.createAttribute("note");

        year.setValue(String.valueOf(y));
        month.setValue(String.valueOf(m));
        day.setValue(String.valueOf(d));
        hour.setValue(String.valueOf(h));
        minute.setValue(String.valueOf(min));
        note.setValue(n);

        appointment.setAttributeNode(year);
        appointment.setAttributeNode(month);
        appointment.setAttributeNode(day);
        appointment.setAttributeNode(hour);
        appointment.setAttributeNode(minute);
        appointment.setAttributeNode(note);


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(xmlFilePath));
        transformer.transform(domSource, streamResult);

        DOMSource source = new DOMSource(document);


    }

    public static void readXml() {

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(xmlFilePath));

            document.getDocumentElement().normalize();

            System.out.println("Root element : " + document.getDocumentElement().getNodeName());

            NodeList nList = document.getElementsByTagName("appointment");

            System.out.println("---------------------");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                System.out.println("\nCurrent element : " + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    System.out.println("year : " + eElement.getAttribute("year"));
                    System.out.println("month : " + eElement.getAttribute("month"));
                    System.out.println("day : " + eElement.getAttribute("day"));
                    System.out.println("hour : " + eElement.getAttribute("hour"));
                    System.out.println("minute : " + eElement.getAttribute("minute"));
                    System.out.println("note : " + eElement.getAttribute("note"));
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String searchXml(int y, int m, int d, int h, int min) throws XPathExpressionException,
            ParserConfigurationException, IOException, SAXException {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(xmlFilePath));

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            String year = String.valueOf(y);
            String month = String.valueOf(m);
            String day = String.valueOf(d);
            String hour = String.valueOf(h);
            String minute = String.valueOf(min);
            String note = "";

//            String xPathValue = "/appointments/appointment[@year='" + year + "' and @month='" + month +
//                    "' and @day='" + day+ "' and @hour='" + hour + "' and @minute='" + minute + "']";
        String xPathValue = "/appointments/appointment[@year='" + year + "' and @month='" + month +
                "' and @day='" + day+ "' and @hour='" + hour + "']";

            XPathExpression expr = xPath.compile(xPathValue);
            NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element el = (org.w3c.dom.Element) nodeList.item(i);
                    int checkMinutes = Integer.parseInt(el.getAttribute("minute"));
                    if(0 <= min & min <= 29) { //проверяет, попадают ли минуты события в слот 00 - 29 мин
                        if (Integer.parseInt(el.getAttribute("minute")) < 30) {
                            note = " :" + checkMinutes + " | " + el.getAttribute("note") + "\n" + note;
                            System.out.println("note:" + note);
                        }
                    }
                    else if (30 <= min & min <= 59){ //проверяет, попадают ли минуты события в слот 30 - 59 мин
                        if (Integer.parseInt(el.getAttribute("minute")) >= 30) {
                            note = " :" + checkMinutes + " | " + el.getAttribute("note") + "\n" + note;
                            System.out.println("note:" + note);
                        }
                    }
                }
            }
            return note;
        }

    }




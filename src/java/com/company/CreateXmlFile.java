package com.company;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;

public class CreateXmlFile {
    public static final String xmlFilePath = "C:\\Users\\User\\Documents\\xmlfile.xml";

    public CreateXmlFile() {
    }

    // Создание XML-файла, если его ещё не существует
    public static void create() {
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // Корневой элемент XML-файла
            Element root = document.createElement("appointments");
            document.appendChild(root);

            // Создание XML файла
            // Трансформирование DOM-объекта в XML-файл
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);

            File file = new File(xmlFilePath);

            if (file.createNewFile()) {
                StreamResult streamResult = new StreamResult(file);

                transformer.transform(domSource, streamResult);

                System.out.println("XML-файл создан");
            } else
                System.out.println("Файл уже существует");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Добавление элемента (события) в XML-файл
    public static void addElement(int y, int m, int d, int h, int min, String n)
            throws ParserConfigurationException, TransformerException, SAXException, IOException {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFilePath);
        Element root = document.getDocumentElement();

        Element appointment = document.createElement("appointment");

        root.appendChild(appointment);

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

    }

//    public static void readXml() {
//
//        try {
//            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
//            Document document = documentBuilder.parse(new File(xmlFilePath));
//
//            document.getDocumentElement().normalize();
//
//            NodeList nList = document.getElementsByTagName("appointment");
//
//            for (int i = 0; i < nList.getLength(); i++) {
//                Node nNode = nList.item(i);
//
//                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//                    Element eElement = (Element) nNode;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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

        String note = "";

        String xPathValue = "/appointments/appointment[@year='" + year + "' and @month='" + month +
                "' and @day='" + day + "' and @hour='" + hour + "']";

        XPathExpression expr = xPath.compile(xPathValue);
        NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        if (nodeList != null && nodeList.getLength() > 0) {

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element el = (org.w3c.dom.Element) nodeList.item(i);
                int checkMinutes = Integer.parseInt(el.getAttribute("minute"));

                // Проверяет, попадают ли минуты события в слот 00 - 29 мин
                if (0 <= min & min <= 29) {
                    // Минутам меньше 10 спереди добавляется 0
                    if (checkMinutes < 10) {
                        note = " :0" + checkMinutes + " | " + el.getAttribute("note") + "\n" + note;
                    } else if (10 <= checkMinutes & checkMinutes < 30) {
                        note = ":" + checkMinutes + "| " + el.getAttribute("note") + "\n" + note;
                    }
                //проверяет, попадают ли минуты события в слот 30 - 59 мин
                } else if (30 <= min & min <= 59) {
                    if (Integer.parseInt(el.getAttribute("minute")) >= 30) {
                        note = ":" + checkMinutes + "| " + el.getAttribute("note") + "\n" + note;
                    }
                }
            }
        }
        return note;
    }

    // Поиск событий, приходящихся на один день, на основании полученных данных строится график занятости
    public static int searchXmlNumberOfEvents(int y, int m, int d) throws XPathExpressionException,
            ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(xmlFilePath));

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

        String year = String.valueOf(y);
        String month = String.valueOf(m);
        String day = String.valueOf(d);
        int numberOfEvents = 0;


        String xPathValue = "/appointments/appointment[@year='" + year + "' and @month='" + month +
                "' and @day='" + day + "']";

        XPathExpression expr = xPath.compile(xPathValue);
        NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        if (nodeList != null && nodeList.getLength() > 0) {

            for (int i = 0; i < nodeList.getLength(); i++) {
                numberOfEvents++;

            }
        }
        return numberOfEvents;
    }

    // Удаление элемента из XML-файла на основании переданных данных о дате и времени
    public static void removeElement(int y, int m, int d, int h, int min)
            throws IOException, SAXException, ParserConfigurationException, TransformerException, XPathExpressionException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(xmlFilePath));

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        String year = String.valueOf(y);
        String month = String.valueOf(m);
        String day = String.valueOf(d);
        String hour = String.valueOf(h);
        String minute = String.valueOf(min);

        String xPathValue = "/appointments/appointment[@year='" + year + "' and @month='" + month +
                "' and @day='" + day + "' and @hour='" + hour + "' and @minute='" + minute + "']";

        XPathExpression expr = xPath.compile(xPathValue);
        NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        if (nodeList != null && nodeList.getLength() > 0) {

            for (int i = 0; i < nodeList.getLength(); i++) {

                Element el = (Element) nodeList.item(i);
                el.getParentNode().removeChild(el);
            }
        }

        document.normalize();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(xmlFilePath));
        transformer.transform(domSource, streamResult);

    }

}




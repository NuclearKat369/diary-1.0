package sample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ToXmlFile {

    public static void readXml() {
        String filePath = "appointments.xml";
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("appointments");
            //now XML is loaded as Document in memory, lets convert it to Object List
            List<Appointment> appList = new ArrayList<Appointment>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                appList.add(getApp(nodeList.item(i)));
            }
            //lets print Employee list information
            for (Appointment app : appList) {
                System.out.println(app.toString());
                System.out.println();
            }
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }

    }

    private static Appointment getApp(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
        Appointment app = new Appointment();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            app.setDay(Integer.parseInt(getTagValue("day", element)));
            app.setMonth(Integer.parseInt(getTagValue("month", element)));
            app.setYear(Integer.parseInt(getTagValue("year", element)));
            app.setHour(Integer.parseInt(getTagValue("hour", element)));
            app.setMinutes(Integer.parseInt(getTagValue("minutes", element)));
            app.setNote(getTagValue("note", element));
        }
        return app;
    }


    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    public static void updateXml() {
        String filePath = "appointments.xml";
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            //update attribute value
            updateAttributeValue(doc);

            //update Element value
//            updateElementValue(doc);

            //delete element
//            deleteElement(doc);

            //add new element
            addElement(doc);

            //write the updated document to file or console
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("appointments.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            System.out.println("XML file updated successfully");

        } catch (SAXException | ParserConfigurationException | IOException | TransformerException e1) {
            e1.printStackTrace();
        }
    }

    private static void addElement(Document doc) {
//        NodeList employees = doc.getElementsByTagName("appointment");
//        Element emp = null;
//
//        //loop for each employee
//        for(int i=0; i<employees.getLength();i++){
//            emp = (Element) employees.item(i);
//            Element salaryElement = doc.createElement("day");
//            salaryElement.appendChild(doc.createTextNode("10000"));
//            emp.appendChild(salaryElement);
//        }
    }

//    private static void deleteElement(Document doc) {
//        NodeList employees = doc.getElementsByTagName("appointments");
//        Element emp = null;
//        //loop for each employee
//        for(int i=0; i<employees.getLength();i++){
//            emp = (Element) employees.item(i);
//            Node genderNode = emp.getElementsByTagName("month").item(0);
//            emp.removeChild(genderNode);
//        }
//
//    }

    private static void updateElementValue(Document doc) {
//        NodeList employees = doc.getElementsByTagName("appointment");
//        Element emp = null;
//        //loop for each employee
//        for(int i=0; i<employees.getLength();i++){
//            emp = (Element) employees.item(i);
//            Node name = emp.getElementsByTagName("note").item(0);
//            name.setNodeValue("Bye-bye");
//        }
    }

    private static void updateAttributeValue(Document doc) {
        NodeList employees = doc.getElementsByTagName("appointment");
        Element app = null;
        //loop for each employee
        for(int i=0; i<employees.getLength();i++){
            app = (Element) employees.item(i);
            int gender = Integer.parseInt(app.getElementsByTagName("year").item(0).getFirstChild().getNodeValue());
            if(gender == 2007){
                //prefix id attribute with M
                app.setAttribute("minutes", "10"+app.getAttribute("minutes"));
            }else{
                //prefix id attribute with F
                app.setAttribute("minutes", "11"+app.getAttribute("minutes"));
            }
        }
    }
}







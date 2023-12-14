import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;

public class Helper {
    private static ConfigModel config;

    public static ConfigModel readXML(String fileName) {
        try {
            File file = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList generalList = doc.getElementsByTagName("General");
            Node generalNode = generalList.item(0);

            if (generalNode.getNodeType() == Node.ELEMENT_NODE) {
                Element generalElement = (Element) generalNode;

                String mailServer = generalElement.getElementsByTagName("MailServer").item(0).getTextContent();
                String smtp = generalElement.getElementsByTagName("SMTP").item(0).getTextContent();
                String pop3 = generalElement.getElementsByTagName("POP3").item(0).getTextContent();
                String autoload = generalElement.getElementsByTagName("Autoload").item(0).getTextContent();

                config = new ConfigModel(mailServer, smtp, pop3, autoload);
            }

            NodeList filterList = doc.getElementsByTagName("Filter");
            Node filterNode = filterList.item(0);

            if (filterNode.getNodeType() == Node.ELEMENT_NODE) {
                Element filterElement = (Element) filterNode;
                processFilterType(filterElement, "Project");
                processFilterType(filterElement, "Important");
                processFilterType(filterElement, "Work");
                processFilterType(filterElement, "Spam");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    private static void processFilterType(Element filterElement, String filterType) {
        NodeList nodeList = filterElement.getElementsByTagName(filterType);
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node projectNode = nodeList.item(temp);

            if (projectNode.getNodeType() == Node.ELEMENT_NODE) {
                Map<String, Map<String, List<String>>> filterMap = new HashMap<>();
                Element projectElement = (Element) projectNode;

                Map<String, List<String>> map = new HashMap<>();
                map.put("From", Arrays.asList(projectElement.getElementsByTagName("From").item(0).getTextContent().split(",")));
                map.put("Subject", Arrays.asList(projectElement.getElementsByTagName("Subject").item(0).getTextContent().split(",")));
                map.put("Body", Arrays.asList(projectElement.getElementsByTagName("Body").item(0).getTextContent().split(",")));
                map.put("Content", Arrays.asList(projectElement.getElementsByTagName("Content").item(0).getTextContent().split(",")));

                filterMap.put(filterType, map);
                config.setFilterMap(filterMap);
            }
        }
    }
}
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;

public class Helper {
    private static ConfigModel config;
    private static Map<String, Map<String, List<String>>> filterMap = new HashMap<>();
    private static Element filterElement;

    public static ConfigModel readXML() {
        try {
            File file = new File("config.xml");
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
                filterElement = (Element) filterNode;
                processFilterType("Project");
                processFilterType("Important");
                processFilterType("Work");
                processFilterType("Spam");
            }
            config.setFilterMap(filterMap);
            // System.out.println(config.getFilterMap().get("Project").get("From").get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    private static void processFilterType(String filterType) {
        NodeList nodeList = filterElement.getElementsByTagName(filterType);
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                Map<String, List<String>> map = new HashMap<>();
                map.put("From", Arrays.asList(element.getElementsByTagName("From").item(0).getTextContent().split(",")));
                map.put("Subject", Arrays.asList(element.getElementsByTagName("Subject").item(0).getTextContent().split(",")));
                map.put("Body", Arrays.asList(element.getElementsByTagName("Body").item(0).getTextContent().split(",")));
                map.put("Content", Arrays.asList(element.getElementsByTagName("Content").item(0).getTextContent().split(",")));

                filterMap.put(filterType, map);
            }
        }
    }
}
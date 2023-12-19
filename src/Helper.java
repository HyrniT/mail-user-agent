import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    private static ConfigModel config;
    private static Map<String, Map<String, List<String>>> filterMap = new HashMap<>();
    private static Element filterElement;
    private static List<String> attachmentFiles = new ArrayList<>();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    public static void modifyXML(String tagName, String parentTagName, String newValue) {
        try {
            File file = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            NodeList parentList = doc.getElementsByTagName(parentTagName);

            if (parentList.getLength() > 0) {
                Node parent = parentList.item(0);

                if (parent.getNodeType() == Node.ELEMENT_NODE) {
                    Element parentElement = (Element) parent;
                    NodeList nodeList = parentElement.getElementsByTagName(tagName);

                    if (nodeList.getLength() > 0) {
                        Node node = nodeList.item(0);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            element.setTextContent(newValue);

                            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            DOMSource source = new DOMSource(doc);
                            StreamResult result = new StreamResult(new File("config.xml"));
                            transformer.transform(source, result);

                            System.out.println(tagName + " modified: " + newValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void sendMail(UserModel user, EmailModel mail, ConfigModel config) {
        try {
            String host = config.getMailServer();
            int port = Integer.parseInt(config.getSMTP());

            Socket socket = new Socket(host, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            System.out.println("SMTP");
            System.out.println(reader.readLine());

            sendCommand(writer, "HELO " + host);

            System.out.println(reader.readLine());

            sendCommand(writer, "MAIL FROM:<" + user.getEmail() + ">");

            System.out.println(reader.readLine());

            if (mail.getTo() != null) {
                for (String to : mail.getTo()) {
                    sendCommand(writer, "RCPT TO: <" + to + ">");
                    System.out.println(reader.readLine());
                }
            }
            
            if (mail.getCc() != null) {
                for (String cc : mail.getCc()) {
                    sendCommand(writer, "RCPT TO:<" + cc + ">");
                    System.out.println(reader.readLine());
                }
            }

            if (mail.getBcc() != null) {
                for (String bcc : mail.getBcc()) {
                    sendCommand(writer, "RCPT TO: <" + bcc + ">");
                    System.out.println(reader.readLine());
                }
            }

            sendCommand(writer, "DATA");

            System.out.println(reader.readLine());

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
            dateFormat.setTimeZone(TimeZone.getDefault());
            String dateHeader = dateFormat.format(currentDate);

            sendCommand(writer, "Date: " + dateHeader);

            sendCommand(writer, "MIME-Version: 1.0");

            sendCommand(writer, "User-Agent: Mozilla Thunderbird");

            sendCommand(writer, "From: " + mail.getFrom());
            if (mail.getTo() != null && mail.getTo().length > 0) {
                sendCommand(writer, "To: " + String.join(",", mail.getTo()));
            }

            if (mail.getCc() != null && mail.getCc().length > 0) {
                sendCommand(writer, "Cc: " + String.join(",", mail.getCc()));
            }

            if (mail.getBcc() != null && mail.getBcc().length > 0) {
                sendCommand(writer, "Bcc: " + String.join(",", mail.getBcc()));
            }

            if (mail.getTitle().length() > 0) {
                sendCommand(writer, "Subject: " + mail.getTitle());
            }

            if (mail.getContent().length() > 0) {
                sendCommand(writer, "Content-Type: text/plain; charset=\"UTF-8\"");
                sendCommand(writer, "Content-Transfer-Encoding: 7bit");
                sendCommand(writer, "");
                sendCommand(writer, mail.getContent());
                sendCommand(writer, "");
            }

            
            if (mail.getAttachmentFiles() != null && mail.getAttachmentFiles().size() > 0) {
                sendCommand(writer, "");
                sendCommand(writer, "Content-Type: multipart/mixed; boundary=separator");
                for (String attachmentFile : mail.getAttachmentFiles()) {
                    File file = new File(attachmentFile);
                    if (file.exists()) {
                        sendCommand(writer, "--separator");
                        String contentType = Files.probeContentType(Path.of(attachmentFile));
                        sendCommand(writer, "Content-Type: " + contentType + "; name=\"" + file.getName() + "\"");
                        sendCommand(writer, "Content-Disposition: attachment; filename=\"" + file.getName() + "\"");
                        sendCommand(writer, "Content-Transfer-Encoding: base64");

                        sendCommand(writer, "");

                        byte[] fileBytes = Files.readAllBytes(Paths.get(attachmentFile));
                        String base64EncodedFile = Base64.getEncoder().encodeToString(fileBytes);
                        sendCommand(writer, base64EncodedFile);

                        sendCommand(writer, "");
                    }
                }
                sendCommand(writer, "--separator--");
            }
            sendCommand(writer, ".");

            System.out.println(reader.readLine());

            sendCommand(writer, "QUIT");

            System.out.println(reader.readLine());
            
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<EmailModel> getMail(UserModel user, ConfigModel config) {
        List<EmailModel> emailList = new ArrayList<>();

        try (Socket socket = new Socket(config.getMailServer(), Integer.parseInt(config.getPOP3()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            
            System.out.println("POP3");
            System.out.println(reader.readLine());

            sendCommand(writer, "USER " + user.getEmail());
            System.out.println(reader.readLine());

            sendCommand(writer, "PASS " + user.getPassword());
            System.out.println(reader.readLine());

            sendCommand(writer, "STAT");
            String statResponse = reader.readLine();
            System.out.println(statResponse);

            if (statResponse.startsWith("+OK") && statResponse.split(" ").length > 1) {
                int numOfEmails = Integer.parseInt(statResponse.split(" ")[1]);
                
                List<String> uidlList = new ArrayList<>();
                sendCommand(writer, "UIDL");
                String uidlResponse;
                while (!(uidlResponse = reader.readLine()).equals(".")) {
                    uidlList.add(uidlResponse);
                }
                
                Set<String> existingUIDLs = loadExistingUIDLs(user.getEmail());
                
                for (int i = 1; i <= numOfEmails; i++) {
                    sendCommand(writer, "RETR " + i);
                    
                    String emailLine;
                    StringBuilder emailHeader = new StringBuilder();
                    StringBuilder emailContent = new StringBuilder();
                    StringBuilder attachmentContent = new StringBuilder();
                    boolean isAttachmentSession = false;
                    boolean isContentSession = false;
                    while (!(emailLine = reader.readLine()).equals(".")) {
                        if (emailLine.startsWith("Content-Type: multipart/mixed;")) {
                            isAttachmentSession = true;
                        }
                        if (emailLine.startsWith("Content-Transfer-Encoding: 7bit")) {
                            isContentSession = true;
                        }
                        if (isAttachmentSession) {
                            attachmentContent.append(emailLine).append("\r\n");
                        } else {
                            if (isContentSession) {
                                emailContent.append(emailLine).append("\r\n");
                            } else {
                                emailHeader.append(emailLine).append("\r\n");
                            }
                        }
                    }

                    if (i <= uidlList.size()) {
                        String uidlResponseLine = uidlList.get(i);
                        String[] uidlParts = uidlResponseLine.split(" ");
                        if (uidlParts.length >= 2) {
                            String uid = uidlParts[1];
                            if (!existingUIDLs.contains(uid)) {
                                EmailModel email = new EmailModel();
                                email.setDate(getValueFromEmailHeader(emailHeader.toString(), "Date"));
                                email.setFrom(getValueFromEmailHeader(emailHeader.toString(), "From"));
                                email.setTo(getValuesFromEmailHeader(emailHeader.toString(), "To"));
                                email.setCc(getValuesFromEmailHeader(emailHeader.toString(), "Cc"));
                                email.setBcc(getValuesFromEmailHeader(emailHeader.toString(), "Bcc"));
                                email.setTitle(getValueFromEmailHeader(emailHeader.toString(), "Subject"));
                                email.setContent(emailContent.toString());
                                
                                if (isAttachmentSession) {
                                    saveAttachments(attachmentContent.toString(), user.getEmail());
                                    email.setAttachmentFiles(attachmentFiles);
                                }

                                emailList.add(email);

                                saveEmailContent(user.getEmail(), uid, emailHeader.toString() + emailContent.toString() + attachmentContent.toString());

                                System.out.println("Email saved: " + user.getEmail() + "/" + uid);
                                System.out.println("--------------------------------------------------");

                                existingUIDLs.add(uid);
                                saveExistingUIDLs(user.getEmail(), existingUIDLs);
                            } else {
                                System.out.println("Email with UID " + uid + " already exists. Skipping...");
                            }
                        }
                    }
                }
            }

            sendCommand(writer, "QUIT");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return emailList;
    }

    public static String getValueFromEmailHeader(String emailContent, String fieldName) {
        String patternString = fieldName + ": (.*?)\\r\\n";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(emailContent);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String[] getValuesFromEmailHeader(String emailContent, String fieldName) {
        String patternString = fieldName + ": (.*?)\\r\\n";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(emailContent);

        List<String> values = new ArrayList<>();
        while (matcher.find()) {
            values.add(matcher.group(1));
        }

        return values.toArray(new String[0]);
    }

    private static void sendCommand(BufferedWriter writer, String command) {
        try {
            writer.write(command + "\r\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveEmailContent(String userEmail, String fileName, String content) {
        try {
            Path directoryPath = Paths.get(".data", userEmail);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = Paths.get(directoryPath.toString(), fileName);

            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                writer.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveEmailAttachment(String userEmail, String fileName, byte[] content) {
        try {
            Path directoryPath = Paths.get(".data", userEmail);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = Paths.get(directoryPath.toString(), fileName);
            attachmentFiles.add(filePath.toString());

            Files.write(filePath, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAttachments(String emailAttachment, String userEmail) {
        String patternString = "Content-Type: .*?; name=\"(.*?)\".*?Content-Transfer-Encoding: base64\\s+\\n(.*?)\\s+\\n--separator";
        Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL); // Pattern.DOTALL is used to match newline characters
        Matcher matcher = pattern.matcher(emailAttachment);

        while (matcher.find()) {
            String fileName = matcher.group(1);
            String base64Content = matcher.group(2);

            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);

            saveEmailAttachment(userEmail, fileName, decodedBytes);
            System.out.println("Attachment saved: " + fileName);
        }
    }

    private static Set<String> loadExistingUIDLs(String userEmail) {
        Set<String> existingUIDLs = new HashSet<>();
        try {
            Path filePath = Paths.get(".data", userEmail, "emails.txt");
            if (Files.exists(filePath)) {
                existingUIDLs.addAll(Files.readAllLines(filePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existingUIDLs;
    }

    private static void saveExistingUIDLs(String userEmail, Set<String> existingUIDLs) {
        try {
            Path directoryPath = Paths.get(".data", userEmail);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = Paths.get(directoryPath.toString(), "emails.txt");
            Files.write(filePath, existingUIDLs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
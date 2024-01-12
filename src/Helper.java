import javax.swing.JOptionPane;
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

    private static ConfigModel _config;
    private static Map<String, Map<String, List<String>>> filterMap = new HashMap<>();
    private static Element filterElement;
    private static List<String> attachmentFiles = new ArrayList<>();

    public static String jarPath;

    public Helper() {
        try {
            jarPath = new File(App.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                    .getAbsolutePath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static ConfigModel readXML() {
        try {
            String filePath = Paths.get(jarPath, "config.xml").toString();
            File file = new File(filePath);

            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "Config file not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

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

                _config = new ConfigModel(mailServer, smtp, pop3, autoload);
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
            _config.setFilterMap(filterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _config;
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
                map.put("From",
                        Arrays.asList(element.getElementsByTagName("From").item(0).getTextContent().split(",")));
                map.put("Subject",
                        Arrays.asList(element.getElementsByTagName("Subject").item(0).getTextContent().split(",")));
                map.put("Body",
                        Arrays.asList(element.getElementsByTagName("Body").item(0).getTextContent().split(",")));
                map.put("Content",
                        Arrays.asList(element.getElementsByTagName("Content").item(0).getTextContent().split(",")));

                filterMap.put(filterType, map);
            }
        }
    }

    public static void sendMail(UserModel user, EmailModel mail, ConfigModel config) {
        try {
            String host = config.getMailServer();
            int port = Integer.parseInt(config.getSMTP());
            boolean hasAttachment = mail.getAttachmentFiles() != null && mail.getAttachmentFiles().size() > 0;

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
                    sendCommand(writer, "RCPT TO:<" + to + ">");
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
                    sendCommand(writer, "RCPT TO:<" + bcc + ">");
                    System.out.println(reader.readLine());
                }
            }

            sendCommand(writer, "DATA");

            System.out.println(reader.readLine());

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getDefault());
            String dateHeader = dateFormat.format(currentDate);

            if (hasAttachment) {
                sendCommand(writer, "Content-Type: multipart/mixed; boundary=\"separator\"");
            }

            sendCommand(writer, "Message-ID: <" + UUID.randomUUID().toString() + ">");

            sendCommand(writer, "Date: " + dateHeader);

            sendCommand(writer, "MIME-Version: 1.0");

            sendCommand(writer, "User-Agent: Mozilla Thunderbird");

            sendCommand(writer, "Content-Language: en-US");

            if (mail.getTo() != null && mail.getTo().length > 0) {
                sendCommand(writer, "To: " + String.join(",", mail.getTo()));
            }

            if (mail.getCc() != null && mail.getCc().length > 0) {
                sendCommand(writer, "Cc: " + String.join(",", mail.getCc()));
            }

            // if (mail.getBcc() != null && mail.getBcc().length > 0) {
            //     sendCommand(writer, "Bcc: " + String.join(",", mail.getBcc()));
            // }

            sendCommand(writer, "From: " + user.getFullname() + " <" + mail.getFrom().trim() + ">");

            if (mail.getTitle().length() > 0) {
                sendCommand(writer, "Subject: " + mail.getTitle().trim());
            }

            if (hasAttachment) {
                sendCommand(writer, "");
                sendCommand(writer, "This is multi-part message in MIME format.");
                sendCommand(writer, "--separator");
            }

            sendCommand(writer, "Content-Type: text/plain; charset=\"UTF-8\"; format=flowed");
            sendCommand(writer, "Content-Transfer-Encoding: 7bit");
            sendCommand(writer, "");
            if (mail.getContent().length() > 0) {
                sendCommand(writer, mail.getContent().trim());
                sendCommand(writer, "");
            }

            if (hasAttachment) {
                for (String attachmentFile : mail.getAttachmentFiles()) {
                    File file = new File(attachmentFile);
                    if (file.exists()) {
                        sendCommand(writer, "--separator");
                        String contentType = Files.probeContentType(Paths.get(attachmentFile));
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

            // sendCommand(writer, "QUIT");

            // System.out.println(reader.readLine());

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<EmailModel> getEmails(UserModel user, ConfigModel config, List<EmailModel> emailList) {
        String userEmail = user.getEmail();

        try (Socket socket = new Socket(config.getMailServer(), Integer.parseInt(config.getPOP3()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            System.out.println("POP3");
            System.out.println(reader.readLine());

            sendCommand(writer, "USER " + userEmail);
            System.out.println(reader.readLine());

            sendCommand(writer, "PASS " + user.getPassword());
            System.out.println(reader.readLine());

            sendCommand(writer, "STAT");
            String statResponse = reader.readLine();
            System.out.println(statResponse);

            if (statResponse.startsWith("+OK") && statResponse.split(" ").length > 1) {
                // int numOfEmails = Integer.parseInt(statResponse.split(" ")[1]);
                Set<String> existingUIDLs = loadExistingUIDLs(userEmail);
                List<String> uidlList = new ArrayList<>();

                sendCommand(writer, "UIDL");

                String uidlResponse;
                while (!(uidlResponse = reader.readLine()).equals(".")) {
                    uidlList.add(uidlResponse);
                }

                for (int i = 1; i < uidlList.size(); i++) {
                    String uidl = uidlList.get(i).split(" ")[1];
                    String uid = uidl.substring(0, uidl.length() - 4);
                    if (!existingUIDLs.contains(uid)) {
                        sendCommand(writer, "RETR " + i);

                        String emailLine;
                        StringBuilder emailData = new StringBuilder();
                        StringBuilder emailContent = new StringBuilder();
                        boolean isBcc = true;
                        boolean isHeaderSession = true;
                        boolean isBodySession = false;
                        boolean hasAttachment = false;
                        EmailModel email = new EmailModel();
                        String subject = "";
                        String body = "";
                        String from = "";
                        email.setId(uid);

                        while (!(emailLine = reader.readLine()).equals(".")) {
                            emailData.append(emailLine).append("\r\n");
                            if (isHeaderSession) {
                                if (emailLine.startsWith("Content-Type: multipart/mixed;")) {
                                    hasAttachment = true;
                                }
                                if (emailLine.startsWith("Date")) {
                                    email.setDate(getValueFromLine(emailLine, "Date"));
                                }
                                if (emailLine.startsWith("To")) {
                                    String[] toArray = getValuesFromLine(emailLine, "To");
                                    email.setTo(toArray);
                                    if (Arrays.asList(toArray).contains(userEmail)) {
                                        isBcc = false;
                                    }
                                }
                                if (emailLine.startsWith("Cc")) {
                                    String[] ccArray = getValuesFromLine(emailLine, "Cc");
                                    email.setCc(ccArray);
                                    if (Arrays.asList(ccArray).contains(userEmail)) {
                                        isBcc = false;
                                    }
                                }
                                if (emailLine.startsWith("From")) {
                                    from = getValueFromLine(emailLine, "From");
                                    email.setFrom(from);
                                }
                                if (emailLine.startsWith("Subject")) {
                                    subject = getValueFromLine(emailLine, "Subject");
                                    email.setTitle(subject);
                                }
                                if (emailLine.equals("")) {
                                    isHeaderSession = false;
                                    isBodySession = true;
                                }
                            }
                            if (isBodySession) {
                                emailContent.append(emailLine).append("\r\n");
                            }
                        }
                        if (isBcc) {
                            email.setTo(new String[] { userEmail });
                            email.setBcc(new String[] { userEmail });
                        }
                        if (hasAttachment) {
                            body = getEmailContentWithAttachment(emailContent.toString());
                            saveAttachments(emailContent.toString(), userEmail);
                            email.setAttachmentFiles(attachmentFiles);
                        } else {
                            body = emailContent.toString().trim();
                        }
                        email.setContent(body);

                        if (email.getTag().equals("Inbox")) {
                            final String _from = from;
                            _config.getFilterMap().forEach((k, v) -> {
                                if (v.get("From").contains(_from)) {
                                    email.setTag(k);
                                }
                            });
                        }

                        if (email.getTag().equals("Inbox")) {
                            final String _subject = subject;
                            _config.getFilterMap().forEach((k, v) -> {
                                for (String s : v.get("Subject")) {
                                    if (!s.isEmpty() && _subject.contains(s)) {
                                        email.setTag(k);
                                    }
                                }
                            });
                        }

                        if (email.getTag().equals("Inbox")) {
                            final String _body = body;
                            _config.getFilterMap().forEach((k, v) -> {
                                for (String s : v.get("Body")) {
                                    if (!s.isEmpty() && _body.contains(s)) {
                                        email.setTag(k);
                                    }
                                }
                            });
                        }

                        if (email.getTag().equals("Inbox")) {
                            final String _content = subject + body;
                            _config.getFilterMap().forEach((k, v) -> {
                                for (String s : v.get("Content")) {
                                    if (!s.isEmpty() && _content.contains(s)) {
                                        email.setTag(k);
                                    }
                                }
                            });
                        }

                        emailList.add(0, email);
                        existingUIDLs.add(uid);
                        saveEmail(userEmail, uid, emailData.toString());
                        saveExistingUIDLs(userEmail, existingUIDLs);

                    } else {
                        // System.out.println("Email already exists: " + uid);
                    }
                }
            }

            sendCommand(writer, "QUIT");

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return emailList;
    }

    public static List<EmailModel> loadEmails(UserModel user, ConfigModel config, List<EmailModel> emailList) {
        String userEmail = user.getEmail();
        Path directoryPath = Paths.get(jarPath, "data", userEmail, "emails");
        File[] files = directoryPath.toFile().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {

                        String emailLine;
                        StringBuilder emailData = new StringBuilder();
                        StringBuilder emailContent = new StringBuilder();
                        boolean isBcc = true;
                        boolean isHeaderSession = true;
                        boolean isBodySession = false;
                        boolean hasAttachment = false;
                        EmailModel email = new EmailModel();
                        String subject = "";
                        String body = "";
                        String from = "";

                        while ((emailLine = reader.readLine()) != null) {
                            email.setId(file.getName().substring(0, file.getName().length() - 4));
                            emailData.append(emailLine).append("\r\n");
                            if (isHeaderSession) {
                                if (emailLine.startsWith("Content-Type: multipart/mixed;")) {
                                    hasAttachment = true;
                                }
                                if (emailLine.startsWith("Date")) {
                                    email.setDate(getValueFromLine(emailLine, "Date"));
                                }
                                if (emailLine.startsWith("To")) {
                                    String[] toArray = getValuesFromLine(emailLine, "To");
                                    email.setTo(toArray);
                                    if (Arrays.asList(toArray).contains(userEmail)) {
                                        isBcc = false;
                                    }
                                }
                                if (emailLine.startsWith("Cc")) {
                                    String[] ccArray = getValuesFromLine(emailLine, "Cc");
                                    email.setCc(ccArray);
                                    if (Arrays.asList(ccArray).contains(userEmail)) {
                                        isBcc = false;
                                    }
                                }
                                if (emailLine.startsWith("From")) {
                                    from = getValueFromLine(emailLine, "From");
                                    email.setFrom(from);
                                }
                                if (emailLine.startsWith("Subject")) {
                                    subject = getValueFromLine(emailLine, "Subject");
                                    email.setTitle(subject);
                                }
                                if (emailLine.equals("")) {
                                    isHeaderSession = false;
                                    isBodySession = true;
                                }
                            }
                            if (isBodySession) {
                                emailContent.append(emailLine).append("\r\n");
                            }
                        }
                        if (isBcc) {
                            email.setTo(new String[] { userEmail });
                            email.setCc(new String[0]);
                            email.setBcc(new String[] { userEmail });
                        }
                        if (hasAttachment) {
                            body = getEmailContentWithAttachment(emailContent.toString());
                            loadAttachments(emailContent.toString(), userEmail);
                            email.setAttachmentFiles(attachmentFiles);
                        } else {
                            body = emailContent.toString();
                        }
                        email.setContent(body);

                        if (email.getTag().equals("Inbox")) {
                            final String _from = from;
                            _config.getFilterMap().forEach((k, v) -> {
                                if (v.get("From").contains(_from)) {
                                    email.setTag(k);
                                }
                            });
                        }

                        if (email.getTag().equals("Inbox")) {
                            final String _subject = subject;
                            _config.getFilterMap().forEach((k, v) -> {
                                for (String s : v.get("Subject")) {
                                    if (!s.isEmpty() && _subject.contains(s)) {
                                        email.setTag(k);
                                    }
                                }
                            });
                        }

                        if (email.getTag().equals("Inbox")) {
                            final String _body = body;
                            _config.getFilterMap().forEach((k, v) -> {
                                for (String s : v.get("Body")) {
                                    if (!s.isEmpty() && _body.contains(s)) {
                                        email.setTag(k);
                                    }
                                }
                            });
                        }

                        if (email.getTag().equals("Inbox")) {
                            final String _content = subject + body;
                            _config.getFilterMap().forEach((k, v) -> {
                                for (String s : v.get("Content")) {
                                    if (!s.isEmpty() && _content.contains(s)) {
                                        email.setTag(k);
                                    }
                                }
                            });
                        }

                        emailList.add(0, email);
                        // emailList.sort((e1, e2) -> e2.getDate().compareTo(e1.getDate()));

                        System.out.println("Email loaded: " + userEmail + File.separatorChar + file.getName());
                        System.out.println("--------------------------------------------------");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        return emailList;
    }

    public static String getValueFromLine(String line, String fieldName) {
        String patternString = fieldName + ": (.*)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String[] getValuesFromLine(String line, String fieldName) {
        String patternString = fieldName + ": (.*)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String value = matcher.group(1);
            return value.split(", ");
        } else {
            return new String[0];
        }
    }

    private static void sendCommand(BufferedWriter writer, String command) {
        try {
            writer.write(command + "\r\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveEmail(String userEmail, String fileName, String content) {
        try {
            Path directoryPath = Paths.get(jarPath, "data", userEmail, "emails");
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = Paths.get(directoryPath.toString(), fileName + ".txt");

            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                writer.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // private static String getEmailContent(String data) {
    //     String patternString = "Content-Transfer-Encoding: 7bit\\s+\\n(.*?)\\s+\\n";
    //     Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL); // Pattern.DOTALL is used to match newline
    //                                                                       // characters
    //     Matcher matcher = pattern.matcher(data);
    //     String content = "";

    //     if (matcher.find()) {
    //         content = matcher.group(1);
    //     }
    //     return content;
    // }

    private static String getEmailContentWithAttachment(String data) {
        String patternString = "Content-Transfer-Encoding: 7bit\\s+\\n(.*?)\\s+\\n--";
        Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL); // Pattern.DOTALL is used to match newline
                                                                          // characters
        Matcher matcher = pattern.matcher(data);
        String content = "";

        if (matcher.find()) {
            content = matcher.group(1);
        }
        return content;
    }

    private static void saveAttachment(String userEmail, String fileName, byte[] content) {
        try {
            Path directoryPath = Paths.get(jarPath, "data", userEmail);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = Paths.get(directoryPath.toString(), fileName);
            attachmentFiles.add(fileName);

            Files.write(filePath, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveAttachments(String data, String userEmail) {
        String patternString = "Content-Type: .*?; name=\"(.*?)\".*?Content-Transfer-Encoding: base64\\s+\\n(.*?)\\s+\\n--";
        Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL); // Pattern.DOTALL is used to match newline
                                                                          // characters
        Matcher matcher = pattern.matcher(data);

        while (matcher.find()) {
            String fileName = matcher.group(1);
            String base64Content = matcher.group(2);

            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);

            saveAttachment(userEmail, fileName, decodedBytes);
            System.out.println("Attachment saved: " + fileName);
        }
    }

    private static void loadAttachments(String data, String userEmail) {
        String patternString = "Content-Type: .*?; name=\"(.*?)\".*?--";
        Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL); // Pattern.DOTALL is used to match newline
                                                                          // characters
        Matcher matcher = pattern.matcher(data);

        while (matcher.find()) {
            String fileName = matcher.group(1);
            attachmentFiles.add(fileName);
            System.out.println("Attachment loaded: " + fileName);
        }
    }

    private static Set<String> loadExistingUIDLs(String userEmail) {
        Set<String> existingUIDLs = new HashSet<>();
        try {
            Path filePath = Paths.get(jarPath, "data", userEmail, "emails.txt");
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
            Path directoryPath = Paths.get(jarPath, "data", userEmail);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = Paths.get(directoryPath.toString(), "emails.txt");
            Files.write(filePath, existingUIDLs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAttachmentFileNamesFromSavedEmail(String userEmail, String emailId) {
        List<String> attachmentFileNames = new ArrayList<>();

        try {
            Path filePath = Paths.get(jarPath, "data", userEmail, "emails", emailId + ".txt");

            if (Files.exists(filePath)) {
                String emailContent = new String(Files.readAllBytes(filePath));

                attachmentFileNames = getAttachmentFileNames(emailContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return attachmentFileNames;
    }

    private static List<String> getAttachmentFileNames(String emailContent) {
        List<String> attachmentFileNames = new ArrayList<>();

        String patternString = "Content-Type: .*?; name=\"(.*?)\".*?--";
        Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);

        Matcher matcher = pattern.matcher(emailContent);

        while (matcher.find()) {
            String fileName = matcher.group(1);
            attachmentFileNames.add(fileName);
        }

        return attachmentFileNames;
    }
}
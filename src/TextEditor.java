import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class TextEditor implements FileOperations {
    private String currentFile;
    private String content;
    private final Map<String, Command> commands;
    private XmlParser xmlParser;

    public TextEditor() {
        commands = new HashMap<>();
        commands.put("open", new OpenCommand(this));
        commands.put("close", new CloseCommand(this));
        commands.put("save", new SaveCommand(this));
        commands.put("saveas", new SaveAsCommand(this, this));
        commands.put("help", new HelpCommand());
        commands.put("exit", new ExitCommand());
        commands.put("print", new PrintCommand(this));
        commands.put("select", new SelectCommand(this));
        commands.put("set", new SetCommand(this));
        commands.put("children", new ChildrenCommand(this));
        commands.put("child", new ChildCommand(this));
        commands.put("text", new TextCommand(this));
        commands.put("delete", new DeleteCommand(this));
        commands.put("newchild", new NewChildCommand(this));
    }

    @Override
    public void open(String fileName) {
        System.out.println("Successfully opened " + fileName);
        currentFile = fileName;
        content = readXmlFileContent(fileName);
        if (content != null && !content.isEmpty()) {
            System.out.println("File content:");
            System.out.println(content);
        } else {
            System.out.println("No content to display.");
        }
    }


    @Override
    public void close() {
        System.out.println("Successfully closed " + currentFile);
        currentFile = null;
        content = null;
        xmlParser = null;
    }

    public void save(String fileContent) {
        //<to>Tove</to><from>Jani</from><heading>Reminder</heading>

        if (currentFile != null) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new File(currentFile));
                doc.getDocumentElement().normalize();

                Document additionalContentDoc = dBuilder.parse(new InputSource(new StringReader("<root>" + fileContent + "</root>")));
                Node additionalContentRoot = additionalContentDoc.getDocumentElement();

                NodeList additionalNodes = additionalContentRoot.getChildNodes();
                for (int i = 0; i < additionalNodes.getLength(); i++) {
                    Node node = additionalNodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Node importedNode = doc.importNode(node, true);
                        doc.getDocumentElement().appendChild(importedNode);
                    }
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(currentFile));
                transformer.transform(source, result);

                System.out.println("Successfully saved content to " + currentFile);
            } catch (Exception e) {
                System.out.println("Error occurred while saving to the file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("No file is currently open.");
        }
    }

    @Override
    public void saveAs(String fileName) {
        System.out.println("Successfully saved as " + fileName);
        currentFile = fileName;
        if (content != null) {
            writeFileContent(fileName, content);
        } else {
            System.out.println("No content to save.");
        }
    }

    @Override
    public boolean isOpen() {
        return currentFile != null;
    }

    private String readXmlFileContent(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            Document doc = parseXmlContent(content.toString());
            if (doc != null) {
                xmlParser = new XmlParser(doc, currentFile);
                return extractTextContent(doc.getDocumentElement()).trim();
            } else {
                System.out.println("Error occurred while parsing XML content.");
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error occurred while reading the file.");
            e.printStackTrace();
            return null;
        }
    }

    private String extractTextContent(Node node) {
        StringBuilder content = new StringBuilder();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node child = nodeList.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                String text = child.getTextContent().trim();
                if (!text.isEmpty()) {
                    content.append(text).append("\n");
                }
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                String childText = extractTextContent(child);
                if (!childText.isEmpty()) {
                    content.append(childText).append("\n");
                }
            }
        }
        return content.toString().trim();
    }

    private Document parseXmlContent(String xmlContent) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(new InputSource(new StringReader(xmlContent)));
        } catch (Exception e) {
            System.out.println("Error occurred while parsing XML content.");
            e.printStackTrace();
            return null;
        }
    }

    private void writeFileContent(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Error occurred while writing the file.");
            e.printStackTrace();
        }
    }

    public String getContent() {
        return content;
    }

    public XmlParser getXmlParser() {
        return xmlParser;
    }

    public void executeCommand(String commandLine) {
        String[] parts = commandLine.split(" ");
        String commandName = parts[0];
        Command command = commands.get(commandName);
        if (command == null) {
            System.out.println("Command not found. Type 'help' for available commands.");
            return;
        }
        command.execute(parts);
    }
}

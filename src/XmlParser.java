import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    private final Document doc;
    private final XPath xpath;
    private final String filePath;

    public XmlParser(Document doc, String filePath) {
        this.doc = doc;
        this.filePath = filePath;
        this.xpath = XPathFactory.newInstance().newXPath();
    }

    public void print() {
        String textContent = extractTextContent(doc.getDocumentElement());
        System.out.println(textContent);
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

    public String select(String id, String key) throws XPathExpressionException {
        XPathExpression expr = xpath.compile("//*[@id='" + id + "']/" + key);
        return (String) expr.evaluate(doc, XPathConstants.STRING);
    }

    public void set(String id, String key, String value) throws Exception {
        Element element = getElementById(id);
        if (element != null) {
            NodeList nodes = element.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node instanceof Element && ((Element) node).getTagName().equals(key)) {
                    node.setTextContent(value);
                    System.out.println("Attribute '" + key + "' set to '" + value + "' for element with id " + id);
                    writeXmlToFile(filePath);
                    return;
                }
            }
            System.out.println("Attribute '" + key + "' not found for element with id " + id);
        } else {
            System.out.println("Element with id " + id + " not found.");
        }
    }

    public List<String> children(String id) throws XPathExpressionException {
        List<String> childrenList = new ArrayList<>();
        Element element = getElementById(id);
        if (element != null) {
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    String childName = ((Element) child).getTagName();
                    childrenList.add(childName);
                }
            }
        } else {
            System.out.println("Element with id " + id + " not found.");
        }
        return childrenList;
    }

    public String child(String id, int n) throws XPathExpressionException {
        Element element = getElementById(id);
        if (element != null) {
            NodeList childNodes = element.getChildNodes();
            int count = 0;
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    count++;
                    if (count == n) {
                        return ((Element) child).getTagName();
                    }
                }
            }
        } else {
            System.out.println("Element with id " + id + " not found.");
        }
        return null;
    }

    public String text(String id) throws XPathExpressionException {
        Element element = getElementById(id);
        if (element != null) {
            StringBuilder textContent = new StringBuilder();
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    textContent.append(((Element) child).getTextContent().trim()).append("\n");
                }
            }
            return textContent.toString().trim();
        } else {
            System.out.println("Element with id " + id + " not found.");
            return null;
        }
    }

    public void delete(String id, String key) throws Exception {
        Element element = getElementById(id);
        if (element != null) {
            Attr attr = element.getAttributeNode(key);
            if (attr != null) {
                element.removeAttributeNode(attr);
                writeXmlToFile(filePath);
                System.out.println("Attribute '" + key + "' deleted from element with id " + id);
            } else {
                System.out.println("Attribute '" + key + "' not found for element with id " + id);
            }
        } else {
            System.out.println("Element with id " + id + " not found.");
        }
    }

    public void newchild(String id) throws IOException {
        Element element = getElementById(id);
        if (element != null) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            try {
                Element newChild = doc.createElement("newchild");
                newChild.setAttribute("id", "new_id");
                element.appendChild(newChild);

                writeXmlToFile(filePath);
                System.out.println("New child added to element with id " + id);
            } catch (Exception e) {
                System.out.println("Error occurred while adding new child: " + e.getMessage());
            }
        } else {
            System.out.println("Element with id " + id + " not found.");
        }
    }

    private void writeXmlToFile(String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

            System.out.println("Changes saved to the file.");
        } catch (Exception e) {
            System.out.println("Error occurred while saving changes to the file.");
            e.printStackTrace();
        }
    }

    private Element getElementById(String id) {
        XPathExpression expr;
        try {
            expr = xpath.compile("//*[@id='" + id + "']");
            return (Element) expr.evaluate(doc, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }
}

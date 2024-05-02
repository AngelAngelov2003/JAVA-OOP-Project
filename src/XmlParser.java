import org.w3c.dom.*;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;

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
        XPathExpression expr = xpath.compile("//person[@id='" + id + "']/" + key);
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
        } catch (TransformerException e) {
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

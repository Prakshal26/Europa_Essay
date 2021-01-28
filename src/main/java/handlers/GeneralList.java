package handlers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class GeneralList {

    static void handleItem(NodeList nodeList, StringBuilder stringBuilder) {

        for (int i=0;i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.TEXT_NODE) {
                stringBuilder.append(node.getNodeValue().trim());
            }
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element subElement = (Element) node;
                if (subElement.getTagName().equalsIgnoreCase("I")) {
                    stringBuilder.append("<i> ");
                    stringBuilder.append(subElement.getTextContent());
                    stringBuilder.append("</i>");
                }
                if (subElement.hasChildNodes() &&
                        subElement.getTagName()!="I") {
                    handleItem(subElement.getChildNodes(),stringBuilder);
                }
            }
        }
    }

    public static void handleGeneralList(NodeList nodeList, StringBuilder stringBuilder, HashMap<String,String> endNoteMap) {

        for (int i=0;i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                stringBuilder.append(node.getNodeValue().trim());
            }
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element subElement = (Element) node;
                if (subElement.getTagName().equalsIgnoreCase("ITEM-NAME")) {
                    stringBuilder.append("<b>");
                    stringBuilder.append(subElement.getTextContent());
                    stringBuilder.append("</b>");
                    stringBuilder.append("<br>");
                }
                if (subElement.getTagName().equalsIgnoreCase("ITEM")) {
                    //Handle Item Tag, as some Item are having I within them.
                    HandleParagraph.handleParagraph(subElement.getChildNodes(),stringBuilder, endNoteMap);
                    //handleItem(subElement.getChildNodes(),stringBuilder);
                    stringBuilder.append("<br>");
                }
                if (subElement.getTagName().equalsIgnoreCase("I")) {
                    stringBuilder.append("<i> ");
                    stringBuilder.append(subElement.getTextContent());
                    stringBuilder.append("</i>");
                }
                if (subElement.hasChildNodes() && subElement.getTagName()!="I" &&
                        subElement.getTagName()!="ITEM") {
                    handleGeneralList(subElement.getChildNodes(),stringBuilder,endNoteMap);
                }
            }
        }
    }
}

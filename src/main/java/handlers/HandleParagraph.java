package handlers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class HandleParagraph {

    public static void handleParagraph(NodeList nodeList, StringBuilder stringBuilder,  HashMap<String,String> endNoteMap) {

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
                if (subElement.getTagName().equalsIgnoreCase("B")) {
                    stringBuilder.append("<b> ");
                    stringBuilder.append(subElement.getTextContent());
                    stringBuilder.append("</b>");
                }
                if (subElement.getTagName().equalsIgnoreCase("XR")) {
                    if (subElement.hasAttribute("REF")) {
                        stringBuilder.append(" ");
                        stringBuilder.append("<a href =\"https://www.europaworld.com/entry/");
                        stringBuilder.append((subElement.getAttribute("REF")).toLowerCase());
                        stringBuilder.append("\">");
                        stringBuilder.append(subElement.getTextContent());
                        stringBuilder.append("</a>");
                    }
                }
                if (subElement.getTagName().equalsIgnoreCase("SC")) {
                    stringBuilder.append("<span style=\"font-variant:small-caps; font-weight:bold\">");
                    stringBuilder.append(" ");
                    stringBuilder.append(subElement.getTextContent());
                    stringBuilder.append("</span>");
                }
                if (subElement.getTagName().equalsIgnoreCase("PERSON-NAME")) {
                    stringBuilder.append("<b> ");
                    AuthorHandler.handleIndividualAuthor(subElement.getChildNodes(),stringBuilder);
                    stringBuilder.append("</b>");
                }
                if (subElement.getTagName().equalsIgnoreCase("ENDNOTEREF")) {
                    stringBuilder.append("<sup>");
                    stringBuilder.append("<a href =\"#");
                    stringBuilder.append(subElement.getAttribute("IDREF").toLowerCase() + "\">");
                    stringBuilder.append(endNoteMap.get(subElement.getAttribute("IDREF")));
                    stringBuilder.append("</a>");
                    stringBuilder.append("</sup>");
                }
                if (subElement.getTagName().equalsIgnoreCase("INTERNET")) {
                        stringBuilder.append("<br>");
                        stringBuilder.append("Internet: ");
                        stringBuilder.append("<a href =\"");
                        stringBuilder.append(subElement.getTextContent());
                        stringBuilder.append("\">");
                        stringBuilder.append(subElement.getTextContent());
                        stringBuilder.append("</a><br>");
                }
                if (subElement.hasChildNodes() && subElement.getTagName()!="B" &&
                        subElement.getTagName()!="I" &&
                        subElement.getTagName()!="XR" &&
                        subElement.getTagName()!="SC" &&
                        subElement.getTagName()!="ENDNOTEREF" &&
                        subElement.getTagName()!="INTERNET" &&
                        subElement.getTagName()!="PERSON-NAME") {
                    handleParagraph(subElement.getChildNodes(),stringBuilder, endNoteMap);
                }
            }
        }
    }
}

package handlers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import parser.EndNoteRefMap;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class TableHandler {

    static void handleEntry(NodeList nodeList, StringBuilder stringBuilder, String open, String close, HashMap<String,String> endNoteMap) {

        for (int i=0; i< nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.getTagName().equalsIgnoreCase("ENTRY")) {
                    stringBuilder.append(open);
                    HandleParagraph.handleParagraph(element.getChildNodes(), stringBuilder, endNoteMap);
                    stringBuilder.append(close);
                }
            }
        }
    }

    static void handleTableRow(NodeList nodeList, StringBuilder stringBuilder, String open, String close, HashMap<String,String> endNoteMap) {

        for (int i=0; i< nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                stringBuilder.append("<tr>");
                handleEntry(element.getChildNodes(), stringBuilder, open,close, endNoteMap);
                stringBuilder.append("</tr>");
            }
        }
    }

     static void handleTGroup(NodeList nodeList, StringBuilder stringBuilder, HashMap<String,String> endNoteMap) {

        for (int i=0; i< nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.getTagName().equalsIgnoreCase("THEAD")) {
                    handleTableRow(element.getChildNodes(), stringBuilder, "<th>","</th>", endNoteMap);
                }
                if (element.getTagName().equalsIgnoreCase("TBODY")) {
                    handleTableRow(element.getChildNodes(), stringBuilder, "<td>","</td>", endNoteMap);
                }
            }
        }

    }
    static void handleTable (NodeList nodeList, StringBuilder stringBuilder, HashMap<String,String> endNoteMap) {

        for (int i=0; i< nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.getTagName().equalsIgnoreCase("HEADING")) {
                    stringBuilder.append("<b>");
                    stringBuilder.append(element.getTextContent());
                    stringBuilder.append("</b><br>");
                }
                if (element.getTagName().equalsIgnoreCase("TGROUP")) {
                    stringBuilder.append("<table>");
                    handleTGroup(element.getChildNodes(),stringBuilder, endNoteMap);
                    stringBuilder.append("</table>");
                }
            }
        }
    }

    static void handleSource(NodeList nodeList, StringBuilder stringBuilder) {

        for (int i=0; i< nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.getTagName().equalsIgnoreCase("SOURCE-HEAD")) {
                    stringBuilder.append("<b>");
                    stringBuilder.append(element.getTextContent());
                    stringBuilder.append(":</b> ");
                }
                if (element.getTagName().equalsIgnoreCase("SOURCE-INFO")) {
                    stringBuilder.append(element.getTextContent());
                }
            }
        }
    }

    static void match(Element element, StringBuilder stringBuilder, HashMap<String,String> endNoteMap) {

        String tagName = element.getTagName();

        switch (tagName) {

            case "TABLE-HEADING":
                stringBuilder.append("<b>");
                stringBuilder.append(element.getTextContent());
                stringBuilder.append("</b><br>");
                break;
            case "INCLUDED-FILE":
                break;
            case "HEADING-NOTE":
                stringBuilder.append("(");
                stringBuilder.append(element.getTextContent());
                stringBuilder.append(")");
              //  stringBuilder.append("<br>");
                break;
            case "TABLE":
                handleTable(element.getChildNodes(),stringBuilder, endNoteMap);
                break;
            case "NOTE":
                stringBuilder.append("<br>");
                HandleParagraph.handleParagraph(element.getChildNodes(),stringBuilder, endNoteMap);
                stringBuilder.append("<br>");
                break;
            case "ENDNOTE":
              //  stringBuilder.append("<p>");
                if (element.hasAttribute("REF-SYMBOL")) {
                    stringBuilder.append("<sup ID=\""+element.getAttribute("ID").toLowerCase()+"\">");
                    stringBuilder.append(element.getAttribute("REF-SYMBOL"));
                    stringBuilder.append("</sup> ");
                }
                stringBuilder.append(element.getTextContent());
                stringBuilder.append("<br>");
                break;
            case "SOURCES":
                stringBuilder.append("<br>");
                handleSource(element.getChildNodes(), stringBuilder);
                break;
            default:
                break;
        }

    }

    public static void handleTableBlock (NodeList nodeList, StringBuilder stringBuilder, HashMap<String,String> endNoteMap) {

        for (int i=0; i< nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element element = (Element) node;
                match(element, stringBuilder, endNoteMap);
            }
        }

    }
}

package handlers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import parser.EndNoteRefMap;

import java.util.HashMap;


public class GeneralSection {

    static void handleSubElements(Element element, StringBuilder stringBuilder, HashMap<String,String> endNoteMap) {

        String tagName = element.getTagName();
        switch (tagName) {
            case "HEADING":
                stringBuilder.append("<p><b>");
                HandleParagraph.handleParagraph(element.getChildNodes(),stringBuilder,endNoteMap);
                stringBuilder.append("</b></p>");
                break;
            case "HEADING-NOTE":
                stringBuilder.append("(");
                HandleParagraph.handleParagraph(element.getChildNodes(),stringBuilder,endNoteMap);
                stringBuilder.append(")");
                break;
            case "P":
            case "BLOCKQUOTE":
            case "ARTICLE":
                stringBuilder.append("<p>");
                HandleParagraph.handleParagraph(element.getChildNodes(),stringBuilder,endNoteMap);
                stringBuilder.append("</p>");
                break;
            case "GEN-LIST":
                stringBuilder.append("<p>");
                GeneralList.handleGeneralList(element.getChildNodes(),stringBuilder, endNoteMap);
                stringBuilder.append("</p>");
                break;
            case "ENDNOTE":
                if (element.hasAttribute("REF-SYMBOL")) {
                    stringBuilder.append("<sup ID=\""+element.getAttribute("ID").toLowerCase()+"\">");
                    stringBuilder.append(element.getAttribute("REF-SYMBOL"));
                    stringBuilder.append("</sup> ");
                }
                stringBuilder.append(element.getTextContent());
                stringBuilder.append("<br>");
                break;
            case "CHRON-LIST":
                ChronListHandler.chronListHandler(element, stringBuilder, endNoteMap);
                break;
            case "FIGURE-GROUP":
                break;
            case "TABLE-BLOCK":
            case "TABLE-BLOCK-SIMPLE":
                stringBuilder.append("<p>");
                TableHandler.handleTableBlock(element.getChildNodes(), stringBuilder, endNoteMap);
                stringBuilder.append("</p>");
                break;
            case "GEN-SECTION":
                //Gen-Section also has Gen-Section tag inside it.
                handleGeneralSection(element,stringBuilder, endNoteMap);
                break;
            default:
                break;
        }
    }

    public static void handleGeneralSection(Element element, StringBuilder generalSectionBuilder, HashMap<String,String> endNoteMap) {

        NodeList nodeList = element.getChildNodes();
        for (int i=0; i<nodeList.getLength();i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE) {
                Element subElement = (Element) node;
                handleSubElements(subElement, generalSectionBuilder, endNoteMap);
            }
        }
    }
}

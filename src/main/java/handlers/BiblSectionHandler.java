package handlers;

import database.PostgreConnect;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pojo.Reference;

import java.util.HashMap;

public class BiblSectionHandler {

    public static void handleBiblList(Element element, Reference reference, StringBuilder stringBuilder, HashMap<String,String> endNoteMap) {

        NodeList nodeList = element.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element subElement = (Element) nNode;
                if (subElement.getTagName().equalsIgnoreCase("BIBL-ENTRY")) {
                    if (subElement.hasAttribute("TYPE")) {
                        if (subElement.getAttribute("TYPE").equalsIgnoreCase("IMPLIED-AUTHOR")) {
                            stringBuilder.append("——");
                        }
                    }
                    HandleParagraph.handleParagraph(subElement.getChildNodes(), stringBuilder,endNoteMap);
                    stringBuilder.append("<br>");
                }
                if (subElement.getTagName().equalsIgnoreCase("P")) {
                    stringBuilder.append("<p>");
                    HandleParagraph.handleParagraph(subElement.getChildNodes(), stringBuilder,endNoteMap);
                    stringBuilder.append("</p>");
                }
            }
        }
    }


    public static int handleBiblSection(Element element, int parentId, int dataId, String xmlId, HashMap<String,String> endNoteMap) {

        int insertedId = 0;
        int maxInsertedId = -1;
        boolean inserted = false;
        NodeList nodeList = element.getChildNodes();
        StringBuilder stringBuilder = new StringBuilder();
        Reference reference = new Reference();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element subElement = (Element) node;
                if (subElement.getTagName().equalsIgnoreCase("HEADING")) {
                    reference.setHeading(subElement.getTextContent());
                }
                if (subElement.getTagName().equalsIgnoreCase("ALT-HEADING")) {
                    reference.setAltHeading(subElement.getTextContent());
                }
//                if (subElement.getTagName().equalsIgnoreCase("HEADING-NOTE")) {
//                    StringBuilder headingNoteBuilder = new StringBuilder();
//                    BiblListHandlers.handleParagraph(subElement.getChildNodes(),headingNoteBuilder);
//                    country.setHeadingNote(headingNoteBuilder.toString());
//                }
                if (subElement.getTagName().equalsIgnoreCase("P")) {
                    stringBuilder.append("<p>");
                    HandleParagraph.handleParagraph(subElement.getChildNodes(),stringBuilder,endNoteMap);
                    stringBuilder.append("</p>");
                }
                if (subElement.getTagName().equalsIgnoreCase("BIBL-LIST")) {
                    stringBuilder.append("<p>");
                    BiblSectionHandler.handleBiblList(subElement, reference, stringBuilder, endNoteMap);
                    stringBuilder.append("</p>");
                }
                if (subElement.getTagName().equalsIgnoreCase("BIBL-SECTION")) {

                    reference.setDescription(stringBuilder.toString());
                    reference.setParentId(parentId);
                    reference.setXmlId(xmlId);
                    reference.setDataId(dataId);
                    if (subElement.hasAttribute("ID")) {
                        xmlId = subElement.getAttribute("ID").toLowerCase();
                    }
                    if (!inserted) {
                        insertedId = PostgreConnect.insertReference(reference);
                        if (maxInsertedId< insertedId) {
                            maxInsertedId = insertedId;
                        }
                        inserted = true;
                    }
                    handleBiblSection(subElement, insertedId,0, xmlId,endNoteMap);
                }
            }
        }
        if (!inserted) {
            reference.setDescription(stringBuilder.toString());
            reference.setParentId(parentId);
            reference.setXmlId(xmlId);
            reference.setDataId(dataId);
            insertedId = PostgreConnect.insertReference(reference);
            if (maxInsertedId< insertedId) {
                maxInsertedId = insertedId;
            }
        }
        return maxInsertedId;
    }
}

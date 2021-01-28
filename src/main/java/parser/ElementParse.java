package parser;

import database.PostgreConnect;
import handlers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pojo.Region;

import java.util.HashMap;

public class ElementParse {

    static boolean match(Element element, Region region, StringBuilder dataBuilder, boolean inserted, HashMap<String,String> endNoteMap) {

        String tagName = element.getTagName();
        int insertedId = 0;
        switch (tagName) {

            case "HEADING":
                dataBuilder.append("<p><b>");
                HandleParagraph.handleParagraph(element.getChildNodes(),dataBuilder,endNoteMap);
                dataBuilder.append("</b></p>");
                break;
            case "ALT-HEADING":
                region.setAltHeading(element.getTextContent());
                break;
            case "AUTHOR-GROUP":
                dataBuilder.append(AuthorHandler.handleAuthorGroup(element));
                break;
            case "P":
            case "BLOCKQUOTE":
            case "ARTICLE":
                HandleParagraph.handleParagraph(element.getChildNodes(),dataBuilder,new HashMap<>());
                break;
            case "P-GROUP":
                GeneralSection.handleGeneralSection(element,dataBuilder,endNoteMap);
                break;
            case "GEN-LIST":
                GeneralList.handleGeneralList(element.getChildNodes(), dataBuilder, new HashMap<>());
                break;
            case "FIGURE-GROUP":

                break;
            case "TABLE-BLOCK":
            case "TABLE-BLOCK-SIMPLE":
                TableHandler.handleTableBlock(element.getChildNodes(), dataBuilder, endNoteMap);
                break;
            case "CHRON-LIST":
                ChronListHandler.chronListHandler(element, dataBuilder, endNoteMap);
                break;
            case "GEN-SECTION":
                //This Tag will come Multiple Times so handle Accordingly.
                GeneralSection.handleGeneralSection(element, dataBuilder, endNoteMap);
                break;
            case "BIBL-SECTION":
                //insert the data received till now. and make inserted = true.
                region.setDescription(dataBuilder.toString());
                if (!inserted) {
                    insertedId = PostgreConnect.insertRegion(region);
                    inserted = true;
                }
                String xmlId = "";
                if (element.hasAttribute("ID")) {
                    xmlId = element.getAttribute("ID").toLowerCase();
                }
                BiblSectionHandler.handleBiblSection(element,0, insertedId, xmlId, endNoteMap);
                break;
            default:
                break;
        }
        return inserted;
    }

    static void parseFiles(Document doc) {

        doc.getDocumentElement().normalize();
        Node entryNode = doc.getDocumentElement();

       // CountryList countryList = new CountryList();
        NodeList nodeList = entryNode.getChildNodes();
        HashMap<String,String> endNoteMap = new HashMap<>();

        EndNoteRefMap.createEndNoteMap(nodeList,endNoteMap);

        Element entryElement = (Element) entryNode;
        Region region = new Region();
        boolean inserted = false;

        if (entryElement.hasAttribute("ISO")) {
            region.setName(entryElement.getAttribute("ISO"));
        }
        if (entryElement.hasAttribute("ID")) {
            region.setXmlId(entryElement.getAttribute("ID").toLowerCase());
        }
        //Insert Country and Get id.
        //insert only if not already Present. If Already Present Get the Id.
        //int countryId = PostgreConnect.insertCountryList(countryList);

        StringBuilder dataBuilder  = new StringBuilder();

        for (int i=0; i<nodeList.getLength();i++) {
            Node nNode = nodeList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)nNode;
              inserted =  ElementParse.match(element, region, dataBuilder, inserted, endNoteMap);
            }
        }
        if (!inserted) {
            region.setDescription(dataBuilder.toString());
            PostgreConnect.insertRegion(region);
            inserted = true;
        }
        System.out.println(dataBuilder);
    }
}


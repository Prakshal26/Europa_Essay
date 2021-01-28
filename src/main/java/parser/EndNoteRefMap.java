package parser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class EndNoteRefMap {

    public static void createEndNoteMap(NodeList nodeList, HashMap<String,String> endNoteMap) {

        //This is to store * or + in the ENdNote. Check Public Holiday for BN.IS
        for (int j = 0; j < nodeList.getLength(); j++) {
            Node nodeEndNode = nodeList.item(j);
            if (nodeEndNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodeEndNode;
                if (element.getTagName().equalsIgnoreCase("ENDNOTE")) {
                    if (element.hasAttribute("ID")) {
                        endNoteMap.put(element.getAttribute("ID"),element.getAttribute("REF-SYMBOL"));
                    }
                }
                if (element.hasChildNodes() && element.getTagName()!="ENDNOTE") {
                    createEndNoteMap(element.getChildNodes(),endNoteMap);
                }
            }
        }
    }
}

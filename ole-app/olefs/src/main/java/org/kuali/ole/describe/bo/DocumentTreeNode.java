package org.kuali.ole.describe.bo;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.model.bo.*;
import org.kuali.ole.docstore.model.enums.DocType;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 12/20/12
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentTreeNode {

    private String title;
    private String uuid;
    private boolean select;
    private boolean returnCheck;

    private OleDocument oleDocument;
    private Bib bib;
    private Holdings holdings;
    private Item item;
    private BibTree bibTree;

    private String selectedInstance;
    private Map<String, String> selectionMap;
    private String holdingLocation;

    public DocumentTreeNode() {

    }

    public BibTree getBibTree() {
        return bibTree;
    }

    public void setBibTree(BibTree bibTree) {
        this.bibTree = bibTree;
    }

    public Holdings getHoldings() {
        return holdings;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        if (item != null) {
            setTitle(buildTreeDataForItem(item, item.getHolding()));
            setUuid(item.getId());
        } else {
            setTitle("");
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setBib(Bib bib) {
        this.bib = bib;
        StringBuilder titleBuilder = new StringBuilder();
        if (bib != null) {
            if (bib.getTitle() != null) {
                titleBuilder.append(bib.getTitle());
            }
            if (titleBuilder.length() > 0) {
                titleBuilder.append(" / ");
            }
            if (bib.getAuthor() != null) {
                titleBuilder.append(bib.getAuthor());
            }
            if (bib.isStaffOnly()) {
                //setTitle("<i><font color='red'>" + StringEscapeUtils.escapeHtml(titleBuilder.toString()) + "</font></i>");
                setTitle("<i><font color='red'>" + encodeString(titleBuilder.toString()) + "</font></i>");
            } else {
                //setTitle(StringEscapeUtils.escapeHtml(titleBuilder.toString()));
                setTitle(encodeString(titleBuilder.toString()));
            }
        } else {
            if (bib.isStaffOnly()) {
                setTitle("<i><font color='red'>" + "Bibliographic Title" + "</font></i>");
            } else {
                setTitle("Bibliographic Title");
            }
        }

        setUuid(bib.getId());

    }

    protected String encodeString(String label) {
        StringBuilder result = new StringBuilder();
        StringCharacterIterator iterator = new StringCharacterIterator(label);
        char character = iterator.current();
        while (character != CharacterIterator.DONE) {
            if (character == '<') {
                result.append("&lt;");
            } else if (character == '>') {
                result.append("&gt;");
            } else if (character == '&') {
                result.append("&amp;");
            } else if (character == '\"') {
                result.append("&quot;");
            } else if (character == '\t') {
                addCharEntity(9, result);
            } else if (character == '!') {
                addCharEntity(33, result);
            } else if (character == '#') {
                addCharEntity(35, result);
            } else if (character == '$') {
                addCharEntity(36, result);
            } else if (character == '%') {
                addCharEntity(37, result);
            } else if (character == '\'') {
                addCharEntity(39, result);
            } else if (character == '(') {
                addCharEntity(40, result);
            } else if (character == ')') {
                addCharEntity(41, result);
            } else if (character == '*') {
                addCharEntity(42, result);
            } else if (character == '+') {
                addCharEntity(43, result);
            } else if (character == ',') {
                addCharEntity(44, result);
            } else if (character == '-') {
                addCharEntity(45, result);
            } else if (character == '.') {
                addCharEntity(46, result);
            } else if (character == '/') {
                addCharEntity(47, result);
            } else if (character == ':') {
                addCharEntity(58, result);
            } else if (character == ';') {
                addCharEntity(59, result);
            } else if (character == '=') {
                addCharEntity(61, result);
            } else if (character == '?') {
                addCharEntity(63, result);
            } else if (character == '@') {
                addCharEntity(64, result);
            } else if (character == '[') {
                addCharEntity(91, result);
            } else if (character == '\\') {
                addCharEntity(92, result);
            } else if (character == ']') {
                addCharEntity(93, result);
            } else if (character == '^') {
                addCharEntity(94, result);
            } else if (character == '_') {
                addCharEntity(95, result);
            } else if (character == '`') {
                addCharEntity(96, result);
            } else if (character == '{') {
                addCharEntity(123, result);
            } else if (character == '|') {
                addCharEntity(124, result);
            } else if (character == '}') {
                addCharEntity(125, result);
            } else if (character == '~') {
                addCharEntity(126, result);
            } else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }

    private void addCharEntity(Integer escapeId, StringBuilder labelBuilder) {
        String padding = "";
        if (escapeId <= 9) {
            padding = "00";
        } else if (escapeId <= 99) {
            padding = "0";
        } else {
            //no prefix
        }
        String number = padding + escapeId.toString();
        labelBuilder.append("&#" + number + ";");
    }


    public void setHoldings(HoldingsTree holdingsTree) {
        this.holdings = holdingsTree.getHoldings();

        if (holdingsTree != null && holdingsTree.getHoldings() != null) {
            if (holdingsTree.getHoldings().getHoldingsType().equalsIgnoreCase("electronic")) {
                setEholdings(holdingsTree);
            } else {
                setTitle(buildTreeDataForHoldings(holdingsTree));
                setUuid(holdingsTree.getHoldings().getId());
            }

        } else {
            setTitle("");
        }

    }

    public void setWorkBibDocument(WorkBibDocument workBibDocument) {
        this.oleDocument = workBibDocument;
        StringBuilder titleBuilder = new StringBuilder();
        if (workBibDocument != null) {
            if (workBibDocument.getTitle() != null) {
                titleBuilder.append(workBibDocument.getTitle());
            }
            if (titleBuilder.length() > 0) {
                titleBuilder.append(" / ");
            }
            if (workBibDocument.getAuthor() != null) {
                titleBuilder.append(workBibDocument.getAuthor());
            }

            setTitle(StringEscapeUtils.escapeHtml(titleBuilder.toString()));
        } else {
            setTitle("Bibliographic Title");
        }

        setUuid(workBibDocument.getId());

    }

    public void setWorkItemDocument(Item item) {
        this.item = item;
        if (item != null) {
            setTitle(buildTreeDataForItem(item));
        } else {
            setTitle("Item");
        }

        setUuid(item.getId());
    }

    public void setWorkHoldingsDocument(Item item, Holdings holdings) {
        this.item = item;
        if (item != null) {
            setTitle(buildTreeDataForItem(item, holdings));
        } else {
            setTitle("Item");
        }

        setUuid(item.getId());
    }

    public void setEholdings(HoldingsTree holdingsTree) {
        this.holdings = holdingsTree.getHoldings();
        setTitle(buildTreeDataForHoldings(holdingsTree));
        String eHoldingsTitle = OLEConstants.E_HOLDINGS_DOC_TYPE;
        StringBuffer stringBuffer = new StringBuffer();
        if (holdingsTree != null && holdingsTree.getHoldings() != null) {

               /* if (holdings.getLocationName() != null && holdings.getLocationName().length() > 0) {
                    stringBuffer.append(holdings.getLocationName());
                }
               *//* if (stringBuffer.length() > 0 && workEInstanceDocument.getWorkEHoldingsDocument().geteResourceName() != null && workEInstanceDocument.getWorkEHoldingsDocument().geteResourceName().length() > 0) {
                    stringBuffer.append("-");
                }
                if (workEInstanceDocument.getWorkEHoldingsDocument().geteResourceName() != null) {
                    stringBuffer.append(workEInstanceDocument.getWorkEHoldingsDocument().geteResourceName());
                }*//*

            if (stringBuffer.length() > 0) {
                setTitle(stringBuffer.toString());
            } else {
                setTitle(eHoldingsTitle);
            }*/

            if ( holdingsTree.getHoldings().isStaffOnly()) {
                String label = getTitle();
                label = "<i><font color='red'>" + label + "</font></i>";
                setTitle(label);
            }
        }


        setUuid( holdingsTree.getHoldings().getId() + " " + DocType.EHOLDINGS.getCode());
    }

    public OleDocument getWorkBibDocument() {
        getTitle();
        getUuid();
        return oleDocument = new WorkBibDocument();
    }

    public OleDocument getWorkInstanceDocument() {
/*        getTitle();
        getUuid();*/
        return oleDocument = new WorkInstanceDocument();
    }

    public OleDocument getWorkItemDocument() {
        getTitle();
        getUuid();
        return oleDocument = new WorkItemDocument();
    }


    public OleDocument getWorkEInstanceDocument() {
        getTitle();
        getUuid();
        return oleDocument = new WorkEInstanceDocument();
    }


    public String getSelectedInstance() {
        return selectedInstance;
    }

    public void setSelectedInstance(String selectedInstance) {
        this.selectedInstance = selectedInstance;
    }


    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Map<String, String> getSelectionMap() {
        return selectionMap;
    }

    public void setSelectionMap(Map<String, String> selectionMap) {
        this.selectionMap = selectionMap;
    }

    public boolean isReturnCheck() {
        return returnCheck;
    }

    public void setReturnCheck(boolean returnCheck) {
        this.returnCheck = returnCheck;
    }


    public String buildTreeDataForHoldings(HoldingsTree holdingsTree) {

        return new EditorFormDataHandler().getHoldingsLabel(holdingsTree);
    }

    public String buildTreeDataForItem(Item item, Holdings holdings) {

        return new EditorFormDataHandler().getItemLabel(holdings, item);

    }

    public String buildTreeDataForItem(Item item) {
        String itemTitle = item.getDisplayLabel();
        if (StringUtils.isNotBlank(itemTitle)) {
            return itemTitle;
        }
        return "Item";

    }

    public String getHoldingLocation() {
        return holdingLocation;
    }

    public void setHoldingLocation(String holdingLocation) {
        this.holdingLocation = holdingLocation;
    }
}

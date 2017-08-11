package org.kuali.ole.describe.bo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.describe.form.EditorForm;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.instance.LocationLevel;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.ole.docstore.common.document.EHoldings;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: pp7788
 * Date: 12/12/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditorFormDataHandler {

    private static final Logger LOG = Logger.getLogger(EditorFormDataHandler.class);

    public EditorForm buildLeftPaneData(EditorForm editorForm) {
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        List<BibTree> bibTreeList = editorForm.getDocumentForm().getBibTreeList();
        if (bibTreeList == null) {
            bibTreeList = editorForm.getBibTreeList();
        }
        Collection<String> uuids = null;
        List<HoldingsTree> holdingsTrees = new ArrayList<HoldingsTree>();
        List<HoldingsTree> eHoldingsTrees = new ArrayList<HoldingsTree>();

        if (bibTreeList != null) {
            for (BibTree bibTree : bibTreeList) {
                if (bibTree != null && bibTree.getBib() != null) {
                    editorForm.setBibId(bibTree.getBib().getId());
                    if (bibTree.getHoldingsTrees() != null) {
                        Collections.sort(bibTree.getHoldingsTrees());
                        for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                            Collections.sort(holdingsTree.getItems());
                            if (holdingsTree.getHoldings().getId() != null) {
                                Holdings holdings = holdingsTree.getHoldings();
                                if (holdings instanceof EHoldings) {
                                    eHoldingsTrees.add(holdingsTree);
                                } else {
                                    holdingsTrees.add(holdingsTree);
                                }
                            }
                        }
                    }
                }
            }
        }
        //uuids = uuidList.values();
        Node<DocumentTreeNode, String> rootNode = null;
        try {
            rootNode = documentSelectionTree.addHoldingsTree(holdingsTrees, DocType.HOLDINGS.getCode());

            if (eHoldingsTrees.size() > 0) {
                documentSelectionTree.addHoldingsTree(eHoldingsTrees, DocType.EHOLDINGS.getCode());
            }
        } catch (SolrServerException e) {
            LOG.error("buildLeftPaneData Exception:" + e);
        }
        editorForm.getLeftTree().setRootElement(rootNode);
        return editorForm;
    }


    public String prepareBibTree(EditorForm editorForm) {

        List<BibTree> bibTreeList = editorForm.getBibTreeList();
        String editable = editorForm.getEditable();
        StringBuilder sb = new StringBuilder();
        String docFormat = editorForm.getDocFormat();
        if (bibTreeList != null) {
            sb.append("<ul id='navigation'>");
            for (BibTree bibTree : bibTreeList) {
                sb.append("&nbsp;&nbsp;");
                if (editable.equals("true") && bibTree.getBib().getId() != null) {
                    sb.append(
                            "<img src='../krms/images/add.png' alt='Add Holdings' title='Add Holdings' class='addInstance' id='")
                            .append(bibTree.getBib().getId() + "_work_instance_oleml").append("'></img>&nbsp;");
                    sb.append(
                            "<img src='../krad/images/cancel.png' alt='Delete Bib' title='Delete Bib' class='deleteBib' id='")
                            .append(bibTree.getBib().getId() + "_work_bibliographic_marc").append("'></img>");
                }
                if (bibTree != null) {
                    List<HoldingsTree> holdingsTreeList = bibTree.getHoldingsTrees();
                    Holdings holdings = new Holdings();
                    List<Item> itemList = new ArrayList<Item>();
                    if (null != holdingsTreeList && holdingsTreeList.size() > 0) {
                        sb.append("<ul>");
                        for (HoldingsTree holdingsTree : holdingsTreeList) {
                            holdings = holdingsTree.getHoldings();
                            if (holdings != null) {
                                String holdingIdentifier = holdings.getId()
                                        + "_work_holdings_oleml_" + bibTree.getBib().getId();
                                //String holdingData = getHoldingsLabel(workHoldingsDocument);

                                sb.append("<li><div align='left' title='View/Edit Holdings' class='holdingIdentifierClass' id='")
                                        .append(holdingIdentifier).append("'");
                                if (holdings.getId() != null && holdings.getId().equals(editorForm.getDocId())) {
                                    sb.append(" style='color:blue'");
                                }

                                //sb.append(">").append(holdingData);
                                sb.append("&nbsp;&nbsp;");
                                if ((editable.equals("true") && (holdingsTree.getHoldings().getId() != null))) {
                                    sb.append(
                                            "<img src='../krms/images/add.png' alt='Add Item' title='Add Item' class='addItem' id='")
                                            .append(holdingsTree.getHoldings().getId() + "_work_item_oleml_"
                                                    + bibTree.getBib().getId()).append("'></img>");
                                    sb.append(
                                            "<img src='../krad/images/cancel.png' alt='Delete Instance' title='Delete Instance' class='deleteBib' id='")
                                            .append(holdingsTree.getHoldings().getId() + "_work_instance_oleml" + "_"
                                                    + bibTree.getBib().getId()).append("'></img>");
                                }
                                sb.append("</div>");
                                sb.append("<ul>");
                            }
                            itemList = holdingsTree.getItems();
                            if (itemList != null) {
                                for (int i = 0; i < itemList.size(); i++) {
                                    Item item = itemList.get(i);
                                    if (item != null) {
                                        String itemIdentifier = item.getId() + "_work_item_oleml_"
                                                + holdingsTree.getHoldings().getId() + "_"
                                                + bibTree.getBib().getId();
                                        //  String itemLevelContent = getItemLabel(holdings, workItemDocument);

                                        sb.append("<li><div align='left' title='View/Edit Item' class='itemIdentifierClass' id='")
                                                .append(itemIdentifier).append("'");
                                        if (item.getId() != null && item.getId().equals(editorForm.getDocId())) {
                                            sb.append(" style='color:blue'");
                                        }
                                        // sb.append(">").append(itemLevelContent);
                                        sb.append("&nbsp;&nbsp;");
                                        if ((editable.equals("true")) && (item.getId() != null)) {
                                            sb.append(
                                                    "<img src='../krad/images/cancel.png' alt='Add Item' title='Delete Item' class='deleteBib' id='")
                                                    .append(item.getId() + "_work_item_oleml" + "_"
                                                            + item.getId()).append("'></img>").append("</div></li>");
                                        }

                                    }
                                }
                            }
                            sb.append("</ul></li>");
                        }
                        // sb.append("</ul></li>");
                    } else {
                        sb.append("</li>");
                    }
                }
            }
            sb.append("</ul>");
        }
        return sb.toString();
    }

    public String getItemLabel(Holdings holdings, Item item) {

        String itemLabel = "";
        StringBuffer itemLabelBuffer = new StringBuffer();
        StringBuffer itemLabelBufferForStaffOnly = new StringBuffer();
        /*StringBuffer holdingsCallNumberInformation = new StringBuffer();
        OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(holdings.getContent());
        org.kuali.ole.docstore.common.document.content.instance.Item oleItem = new ItemOlemlRecordProcessor().fromXML(item.getContent());
        //prefix+callNumber for holdings
        if (oleHoldings != null && oleHoldings.getCallNumber() != null) {
            getCallNumberInformation(holdingsCallNumberInformation, oleHoldings.getCallNumber().getPrefix(),
                    oleHoldings.getCallNumber().getNumber());
        }*/

//        //prefix+callNumber for item
//        if (oleItem.getLocation() != null) {
//            if ((oleHoldings != null) && oleHoldings.getLocation() != null && oleHoldings.getLocation().getLocationLevel() != null && (oleHoldings.getLocation().getLocationLevel().getName() != null)) {
//                if ((oleItem != null) && (oleItem.getLocation().getLocationLevel() != null)) {
//                    addDataToLabel(itemLabelBuffer, getLocationCode(oleItem.getLocation().getLocationLevel()));
//                }
//            } else if (oleItem.getLocation() != null) {
//                addDataToLabel(itemLabelBuffer, getLocationCode(oleItem.getLocation().getLocationLevel()));
//            }
//        }
//        if (oleItem.getEnumeration() != null) {
//            addDataToLabel(itemLabelBuffer, oleItem.getEnumeration());
//        }
//        if (oleItem.getChronology() != null) {
//            addDataToLabel(itemLabelBuffer, oleItem.getChronology());
//        }
//        if (oleItem.getCopyNumber() != null) {
//            addDataToLabel(itemLabelBuffer, oleItem.getCopyNumber());
//        }
//        if (oleItem.getBarcodeARSL() != null) {
//            addDataToLabel(itemLabelBuffer, oleItem.getBarcodeARSL());
//        }
//        if (oleItem.getAccessInformation() != null && oleItem.getAccessInformation().getBarcode() != null) {
//            addDataToLabel(itemLabelBuffer, oleItem.getAccessInformation().getBarcode());
//        }
        itemLabelBuffer.append(item.getDisplayLabel());
        if (StringUtils.isEmpty(itemLabelBuffer.toString().trim())) {
            itemLabelBuffer = itemLabelBuffer.append("Item");
        }
        if (itemLabelBuffer.length() > 0) {
            if (item.isAnalytic()) {
                itemLabelBuffer.append("<a href='analyticsController?viewId=AnalyticsSummaryView&amp;methodToCall=showAnalyticsSummary&amp;itemId="+ item.getId()
                        + "&amp;docType=item&amp' class='analytics' target='_blank'>AI</a>");
            }
            if (item.isStaffOnly()) {
                itemLabelBufferForStaffOnly.append("<i><font color='red'>");
                itemLabelBufferForStaffOnly.append(itemLabelBuffer);
                itemLabelBufferForStaffOnly.append("</font></i>");
            } else {
                itemLabelBufferForStaffOnly.append(itemLabelBuffer);
            }
            itemLabel = itemLabelBufferForStaffOnly.toString();
        }

        return itemLabel;
    }


    public String getHoldingsLabel(HoldingsTree holdingsTree) {
        StringBuffer holdingsLabelBuffer = new StringBuffer();
        StringBuffer holdingsLabelBufferForStaffOnly = new StringBuffer();
        String holdingsLabel = "";
        //OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(holdings.getContent());
        Boolean staffOnlyFlag = holdingsTree.getHoldings().isStaffOnly();
        String linkedBibCount = "";
        boolean analyticHoldingFlag = false;
        String itemId = "";
        //String analyticHoldingLabel = "";
//        if (oleHoldings.getLocation() != null  && oleHoldings.getLocation().getLocationLevel()!=null ) {
//            addDataToLabel(holdingsLabelBuffer, getLocationCode(oleHoldings.getLocation().getLocationLevel()));
//        }
//        if(oleHoldings!=null && oleHoldings.getCallNumber()!=null)  {
//            getCallNumberInformation(holdingsLabelBuffer,oleHoldings.getCallNumber().getPrefix(),
//                oleHoldings.getCallNumber().getNumber());
//        }
//
//        if (oleHoldings.getCopyNumber() != null) {
//            addDataToLabel(holdingsLabelBuffer, oleHoldings.getCopyNumber());
//        }
        if (StringUtils.isNotBlank(holdingsTree.getHoldings().getDisplayLabel())) {
            if (staffOnlyFlag != null && staffOnlyFlag) {
                holdingsLabelBuffer.append("<i><font color='red'>");
                holdingsLabelBuffer.append(holdingsTree.getHoldings().getDisplayLabel());
                holdingsLabelBuffer.append("</font></i>");
            } else {
                holdingsLabelBuffer.append(holdingsTree.getHoldings().getDisplayLabel());
            }
        }

        boolean boundWithFlag = false;

        if (holdingsLabelBuffer.length() > 0) {
            int bibUUIDS = 0;
            String showBibLink = null;
            String showSHLink = null;
            if(holdingsTree.getHoldings().isBoundWithBib()){
                bibUUIDS = holdingsTree.getHoldings().getBibs().getBibs().size();
            }
            if (holdingsTree.getHoldings().isSeries()) {
                showSHLink = "<a href='analyticsController?viewId=AnalyticsSummaryView&amp;methodToCall=showAnalyticsSummary&amp;holdingsId="+ holdingsTree.getHoldings().getId()
                        + "&amp;docType=holdings&amp' class='analytics' target='_blank'>SH</a>";
                //holdingsLabel = holdingsLabelBuffer + showBibLink;
                holdingsLabelBuffer.append(showSHLink);
            }
            String holdingsId = "";
            for (Item item : holdingsTree.getItems()) {
                if (item.isAnalytic()) {
                    for (Holdings holdings : item.getHoldings()) {
                            holdingsId = holdings.getId();
                    }
                    if(!(holdingsId.equals(holdingsTree.getHoldings().getId()))){
                        //analyticHoldingLabel = item.getHolding().getDisplayLabel();
//                        analyticHoldingLabel = holdingsTree.getHoldings().getDisplayLabel();
                        analyticHoldingFlag = true;
                        itemId = item.getId();
                    }
                }
            }
            if(analyticHoldingFlag == true) {
                showSHLink = "<a href='analyticsController?viewId=AnalyticsSummaryView&amp;methodToCall=showAnalyticsSummary&amp;itemId="+ itemId
                        + "&amp;docType=item&amp' class='analytics' target='_blank'>AH</a>" ;
//                holdingsLabelBuffer.append(analyticHoldingLabel);
                holdingsLabelBuffer.append(showSHLink);
            }
            if (bibUUIDS > 0) {
                if ((bibUUIDS > 1)) {
                    boundWithFlag = true;
                    linkedBibCount = "BIBS(" + (bibUUIDS) + ")";
                }
                showBibLink =
                        "<a href='editorcontroller?viewId=ShowBibView&amp;methodToCall=showBibs&amp;holdingsId="
                                + holdingsTree.getHoldings().getId()
                                + "&amp;docCategory=work&amp;docType=holdings&amp;docFormat=oleml&amp;editable=true' class='boundWithbibs' target='_blank'>"
                                + linkedBibCount + "</a>";
            }
            if (boundWithFlag) {
                //holdingsLabel = holdingsLabelBuffer + showBibLink;
                holdingsLabelBuffer.append(showBibLink);
            }
            holdingsLabel = holdingsLabelBuffer.toString();
        }
        if(StringUtils.isEmpty(holdingsLabel.trim()))   {
             holdingsLabel ="Holdings";
        }
        return holdingsLabel;
    }


    private String getLocationCode(LocationLevel locationLevel) {
        String locationCode = "";
        while (locationLevel != null) {
            String name = locationLevel.getName();
            if (name != null) {
                BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
                Map parentCriteria = new HashMap();
                parentCriteria.put("locationCode", name);
                OleLocation oleLocationCollection = businessObjectService.findByPrimaryKey(OleLocation.class, parentCriteria);
                if (oleLocationCollection != null) {
                    String code = oleLocationCollection.getLocationCode();
                    if (locationCode.equalsIgnoreCase("")) {
                        locationCode = code;
                    } else {
                        locationCode = locationCode + "/" + code;
                    }
                }
            }
            locationLevel = locationLevel.getLocationLevel();
        }
        return locationCode;
    }

    private void getCallNumberInformation(StringBuffer holdingsData, String prefix, String callNumber) {

        if (prefix != null) {
            addDataToLabel(holdingsData, prefix);
        }

        if (callNumber != null) {
            addDataToLabel(holdingsData, callNumber);
        }

    }


    private void addDataToLabel(StringBuffer label, String data) {
        if (data.length() > 0) {
            if (label.length() > 0) {
                label.append("-");
            }
            label.append(data);
        }
    }


    public String validateTitleForLeftPane(String title) {
        StringBuffer stringBuffer = new StringBuffer();
        String regex = "<";
        String replaceString = " &lt; ";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(title);
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, replaceString);
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

}

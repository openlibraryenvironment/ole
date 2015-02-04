package org.kuali.ole.describe.bo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.form.WorkInstanceOlemlForm;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.SourceHoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.common.document.content.instance.Location;
import org.kuali.ole.docstore.common.document.content.instance.LocationLevel;
import org.kuali.ole.describe.form.InstanceEditorForm;
import org.kuali.ole.describe.service.impl.InstanceEditorTreeServiceImpl;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * InstanceEditorFormDataHandler is the data handler class for Instance Editor
 */
public class InstanceEditorFormDataHandler {
    private static final Logger LOG = Logger.getLogger(InstanceEditorFormDataHandler.class);
    private static final String loginUser = GlobalVariables.getUserSession().getPrincipalId();

    private BusinessObjectService businessObjectService;

    /**
     * This Method will ingest a new record in docStore for Instance Record type.
     * returns formatted xml
     *
     * @param uifFormBase
     * @param uuid
     * @return content
     */
    public String buildInstanceRecordForDocStore(UifFormBase uifFormBase, String uuid) {
        InstanceEditorForm instanceEditorForm = (InstanceEditorForm) uifFormBase;
        List<Instance> oleInstanceList = new ArrayList<Instance>();
        InstanceCollection instanceCollection = new InstanceCollection();

        buildLocationLevels(instanceEditorForm);
        if (instanceEditorForm.getInstance().getSourceHoldings() != null) {
            SourceHoldings sourceHoldings = buildSourceHoldings(instanceEditorForm);
            instanceEditorForm.getInstance().setSourceHoldings(sourceHoldings);
        }
        if (instanceEditorForm.getSelectedItem() == null) {
            Item item = new Item();
            Items items = new Items();
            items.setItem(Arrays.asList(item));
            instanceEditorForm.getInstance().setItems(items);
        }
        buildAuditDetailsForNewInstance(instanceEditorForm);

        oleInstanceList.add(instanceEditorForm.getInstance());

        instanceCollection.setInstance(oleInstanceList);
        String content = new InstanceOlemlRecordProcessor().toXML(instanceCollection);
        return content;
    }

    public void buildLocationLevels(InstanceEditorForm instanceEditorForm) {
        OleHoldings oleHoldings = instanceEditorForm.getSelectedHolding();
        Location location = new Location();
        LocationLevel locationLevel = new LocationLevel();

        String holdingsLocationName = oleHoldings.getLocation().getLocationLevel().getName();
        if (!holdingsLocationName.equalsIgnoreCase("")) {
            locationLevel = createLocationLevel(holdingsLocationName, locationLevel);
            location.setLocationLevel(locationLevel);
            location.setPrimary("true");
            location.setStatus("permanent");
            oleHoldings.setLocation(location);
        } else {
            oleHoldings.setLocation(null);
        }

        if (instanceEditorForm.getSelectedItem() != null) {
            Item item = instanceEditorForm.getSelectedItem();
            Location itemLocation = new Location();
            LocationLevel itemLocationLevel = new LocationLevel();

            String itemsLocationName = item.getLocation().getLocationLevel().getName();
            if (!itemsLocationName.equalsIgnoreCase("")) {
                itemLocationLevel = createLocationLevel(itemsLocationName, itemLocationLevel);
                itemLocation.setLocationLevel(itemLocationLevel);
                itemLocation.setPrimary("true");
                itemLocation.setStatus("temporary");
                item.setLocation(itemLocation);
            } else {
                item.setLocation(null);
            }
        }
    }

    public LocationLevel createLocationLevel(String locationName, LocationLevel locationLevel) {
        if (locationName != null && !locationName.equalsIgnoreCase("")) {
            BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
            String[] names = locationName.split("/");
            Map parentCriteria = new HashMap();
            parentCriteria.put("locationCode", names[0]);
            OleLocation oleLocationCollection = businessObjectService.findByPrimaryKey(OleLocation.class, parentCriteria);
            String levelName = oleLocationCollection.getLocationName();
            String locationCode = oleLocationCollection.getLocationCode();
            String levelCode = oleLocationCollection.getOleLocationLevel().getLevelName();
            locationLevel.setName(locationCode);
            locationLevel.setLevel(levelCode);
            String locName = "";
            if (locationName.contains("/"))
                locName = locationName.replace(names[0] + "/", "");
            else
                locName = locationName.replace(names[0], "");
            if (locName != null && !locName.equals("")) {
                LocationLevel newLocationLevel = new LocationLevel();
                locationLevel.setLocationLevel(createLocationLevel(locName, newLocationLevel));
            }
        }
        return locationLevel;
    }

    public SourceHoldings buildSourceHoldings(InstanceEditorForm instanceEditorForm) {
        SourceEditorForUI sourceEditorForUI = instanceEditorForm.getSelectedSourceHolding();
        WorkBibMarcRecord workBibMarcRecord = new WorkBibMarcRecord();
        workBibMarcRecord.setLeader(sourceEditorForUI.getLeader());
        workBibMarcRecord.setControlFields(buildBibliographicContrlFields(sourceEditorForUI.getControlFields()));
        workBibMarcRecord.setDataFields(buildBibliographicDataFields(sourceEditorForUI.getDataFields()));

        SourceHoldings sourceHoldings = new SourceHoldings();
        Extension extension = new Extension();
        extension.getContent().add(workBibMarcRecord);
        sourceHoldings.setHoldings(extension);
        sourceHoldings.setHoldingsIdentifier(sourceEditorForUI.getUuid());

        if (!sourceEditorForUI.getPrimary().equalsIgnoreCase("true")) {
            sourceHoldings.setPrimary("false");
        } else {
            sourceHoldings.setPrimary(sourceEditorForUI.getPrimary());
        }

        return sourceHoldings;
    }

    private List<ControlField> buildBibliographicContrlFields(List<MarcEditorControlField> controlFields) {
        List<ControlField> marcControlFieldList = new ArrayList<ControlField>();
        for (int i = 0; i < controlFields.size(); i++) {
            ControlField marcControlField = new ControlField();
            marcControlField.setTag(controlFields.get(i).getTag());
            marcControlField.setValue(controlFields.get(i).getValue());
            marcControlFieldList.add(marcControlField);
        }
        return marcControlFieldList;
    }

    private List<DataField> buildBibliographicDataFields(List<MarcEditorDataField> dataFields) {
        String marcEditorDataFieldValue;
        List<DataField> marcDataFieldList = new ArrayList<DataField>();
        for (int i = 0; i < dataFields.size(); i++) {
            DataField marcDataField = new DataField();
            List<SubField> marcSubFieldList = new ArrayList<SubField>();
            marcEditorDataFieldValue = dataFields.get(i).getValue();
            StringTokenizer str = new StringTokenizer(marcEditorDataFieldValue, "|");
            while (str.hasMoreTokens()) {
                SubField marcSubField = new SubField();
                String dataFieldTokenizedVal = str.nextToken();
                String code = Character.toString(dataFieldTokenizedVal.charAt(0));
                marcSubField.setCode(code);
                String value = dataFieldTokenizedVal.substring(1, dataFieldTokenizedVal.length());
                marcSubField.setValue(value);
                marcSubFieldList.add(marcSubField);
            }
            marcDataField.setSubFields(marcSubFieldList);
            marcDataField.setTag(dataFields.get(i).getTag());
            marcDataField.setInd1(dataFields.get(i).getInd1());
            marcDataField.setInd2(dataFields.get(i).getInd2());
            marcDataFieldList.add(marcDataField);
        }
        return marcDataFieldList;
    }

    public SourceEditorForUI buildSourceHoldingForUI(InstanceEditorForm instanceEditorForm) {
        SourceHoldings sourceHoldings = instanceEditorForm.getInstance().getSourceHoldings();
        SourceEditorForUI sourceEditorForUI = new SourceEditorForUI();
        if (sourceHoldings.getHoldings() != null && !sourceHoldings.getHoldings().equals(null) && !sourceHoldings.getHoldings().equals("")) {
            WorkBibMarcRecord workBibMarcRecord = getWorkBibMarcRecord(sourceHoldings.getHoldings());
            if (workBibMarcRecord != null) {
                sourceEditorForUI.setLeader(workBibMarcRecord.getLeader());
                sourceEditorForUI.setControlFields(buildMarcEditorControlFields(workBibMarcRecord.getControlFields()));
                sourceEditorForUI.setDataFields(buildMarcEditorDataFields(workBibMarcRecord.getDataFields()));
            }
            sourceEditorForUI.setUuid(sourceHoldings.getHoldingsIdentifier());
            sourceEditorForUI.setPrimary(sourceHoldings.getPrimary());

            instanceEditorForm.setSelectedSourceHolding(sourceEditorForUI);
        }
        return sourceEditorForUI;
    }

    public List<MarcEditorControlField> buildMarcEditorControlFields(List<ControlField> controlFields) {
        List<MarcEditorControlField> marcControlFieldList = new ArrayList<MarcEditorControlField>();
        for (int i = 0; i < controlFields.size(); i++) {
            MarcEditorControlField marcEditorControlField = new MarcEditorControlField();
            marcEditorControlField.setTag(controlFields.get(i).getTag());
            marcEditorControlField.setValue(controlFields.get(i).getValue());
            marcControlFieldList.add(marcEditorControlField);
        }
        return marcControlFieldList;
    }

    public void buildLocationsForUI(InstanceEditorForm instanceEditorForm) {
        OleHoldings oleHoldings = instanceEditorForm.getInstance().getOleHoldings();
        LocationLevel locationLevel = oleHoldings.getLocation().getLocationLevel();
    }

    public List<MarcEditorDataField> buildMarcEditorDataFields(List<DataField> dataFields) {
        List<MarcEditorDataField> marcEditorDataFields = new ArrayList<MarcEditorDataField>();
        for (int i = 0; i < dataFields.size(); i++) {
            MarcEditorDataField marcEditorDataField = new MarcEditorDataField();
            marcEditorDataField.setTag(dataFields.get(i).getTag());
            marcEditorDataField.setInd1(dataFields.get(i).getInd1());
            marcEditorDataField.setInd2(dataFields.get(i).getInd2());
            List<SubField> subFields = dataFields.get(i).getSubFields();
            String subFieldVal = null;
            for (int j = 0; j < subFields.size(); j++) {
                String code = subFields.get(j).getCode();
                String value = subFields.get(j).getValue();
                if (subFieldVal != null)
                    subFieldVal = subFieldVal + "|" + code + value;
                else
                    subFieldVal = "|" + code + value;
            }
            marcEditorDataField.setValue(subFieldVal);
            marcEditorDataFields.add(marcEditorDataField);
        }
        return marcEditorDataFields;
    }

    /**
     * This Method will build the holdingContent from the docStore and returns the responseXml
     * based on the itemIdentifier  additionalAttributes has been modified.
     * toXml method in HoldingOlemlRecordProcessor will convert the oleHolding in to response itemContent.
     *
     * @param oleHolding
     * @return holdingContent
     */
    public String buildHoldingContent(OleHoldings oleHolding) {
        Location location = new Location();
        LocationLevel locationLevel = new LocationLevel();

        String holdingsLocationName = (oleHolding.getLocation() != null ? oleHolding.getLocation().getLocationLevel().getName() : "");
        if (StringUtils.isNotEmpty(holdingsLocationName)) {
            locationLevel = createLocationLevel(holdingsLocationName, locationLevel);
            location.setLocationLevel(locationLevel);
            location.setPrimary("true");
            location.setStatus("permanent");
            oleHolding.setLocation(location);
        } else {
            oleHolding.setLocation(null);
        }


        //Updating Audit details
//        AdditionalAttributes additionalAttributes = getFirstAdditionalAttributes(oleHolding.getExtension());
//        updateAuditDetails(additionalAttributes);

        String holdingContent = new HoldingOlemlRecordProcessor().toXML(oleHolding);
        return holdingContent;
    }

    /**
     * This Method will build the itemContent from the docStore and returns the responseXml
     * based on the itemIdentifier  additionalAttributes has been modified.
     * toXml method in ItemOlemlRecordProcessor will convert the oleItem in to response itemContent.
     *
     * @param oleItem
     * @return itemContent
     */
    public String buildItemContent(Item oleItem) throws Exception {
        Location itemLocation = new Location();
        LocationLevel itemLocationLevel = new LocationLevel();

        String itemsLocationName = (oleItem.getLocation() != null ? oleItem.getLocation().getLocationLevel().getName() : "");
        if(StringUtils.isNotEmpty(itemsLocationName)) {
            if (!itemsLocationName.equalsIgnoreCase("")) {
                itemLocationLevel = createLocationLevel(itemsLocationName, itemLocationLevel);
                itemLocation.setLocationLevel(itemLocationLevel);
                oleItem.setLocation(itemLocation);
                itemLocation.setPrimary("true");
                itemLocation.setStatus("temporary");
            }
        } else {
            oleItem.setLocation(null);
        }


        //compute statistical searching name if statistical searching code is available
        if (oleItem.getStatisticalSearchingCode().size() > 0) {
            computeStatisticalSearching(oleItem.getStatisticalSearchingCode().get(0));
        }

        //compute item type name if item type code is available
        computeItemType(oleItem.getItemType());

        if (oleItem.getTemporaryItemType() != null) {
            String tempItemTypeFillValue = computeTempItemType(oleItem.getTemporaryItemType());
            oleItem.getTemporaryItemType().setFullValue(tempItemTypeFillValue);
        }
        //Updating Audit details
        AdditionalAttributes additionalAttributes = null;
        if ((oleItem.getExtension() != null) &&
                oleItem.getExtension().getContent() != null && oleItem.getExtension().getContent().size() > 0) {
            additionalAttributes = (AdditionalAttributes) oleItem.getExtension().getContent().get(0);
            updateAuditDetails(additionalAttributes);
        } else {
            Extension extension = new Extension();
            additionalAttributes = new AdditionalAttributes();
            additionalAttributes.setAttribute(OLEConstants.CREATED_BY, GlobalVariables.getUserSession().getPrincipalName());
            additionalAttributes.setAttribute(OLEConstants.DATE_ENTERED,
                    String.valueOf(new SimpleDateFormat("MMM dd, yyyy").format(new Date())));
            extension.getContent().add(additionalAttributes);
            oleItem.setExtension(extension);
        }
        String itemContent = new ItemOlemlRecordProcessor().toXML(oleItem);
        return itemContent;
    }

    public String buildItemContent(Item oleItem, String staffOnlyFlagForItem) throws Exception {
        Location itemLocation = new Location();
        LocationLevel itemLocationLevel = new LocationLevel();

        String itemsLocationName = (oleItem.getLocation() != null ? oleItem.getLocation().getLocationLevel().getName() : "");
        if(StringUtils.isNotEmpty(itemsLocationName)) {
            if (!itemsLocationName.equalsIgnoreCase("")) {
                itemLocationLevel = createLocationLevel(itemsLocationName, itemLocationLevel);
                itemLocation.setLocationLevel(itemLocationLevel);
                oleItem.setLocation(itemLocation);
                itemLocation.setPrimary("true");
                itemLocation.setStatus("temporary");
            }
        } else {
            oleItem.setLocation(null);
        }
        oleItem.setStaffOnlyFlag(Boolean.valueOf(staffOnlyFlagForItem));


        //compute statistical searching name if statistical searching code is available
        if (oleItem.getStatisticalSearchingCode().size() > 0) {
            computeStatisticalSearching(oleItem.getStatisticalSearchingCode().get(0));
        }

        //compute item type name if item type code is available
        computeItemType(oleItem.getItemType());

        if (oleItem.getTemporaryItemType() != null) {
            String tempItemTypeFillValue = computeTempItemType(oleItem.getTemporaryItemType());
            oleItem.getTemporaryItemType().setFullValue(tempItemTypeFillValue);
        }

        String itemContent = new ItemOlemlRecordProcessor().toXML(oleItem);
        return itemContent;
    }


    public String buildSourceHoldingContent(SourceHoldings sourceHoldings) throws Exception {
        //Updating Audit details
        AdditionalAttributes additionalAttributes = getFirstAdditionalAttributes(sourceHoldings.getExtension());
        updateAuditDetails(additionalAttributes);

        String holdingContent = new SourceHoldingOlemlRecordProcessor().toXML(sourceHoldings);
        return holdingContent;
    }

    /**
     * This Method will build the Left Pane hierarchy details.
     *
     * @param instanceEditorForm
     * @return instanceEditorForm
     */
    public InstanceEditorForm buildLeftPaneData(InstanceEditorForm instanceEditorForm) {

        InstanceEditorTreeServiceImpl InstanceEditorTreeServiceImpl = new InstanceEditorTreeServiceImpl();
        String treeData = InstanceEditorTreeServiceImpl.createTreeStructure(instanceEditorForm);
        LOG.info("tree data-->" + treeData);
        instanceEditorForm.setTreeData(treeData);
        return instanceEditorForm;
    }

    private WorkBibMarcRecord getWorkBibMarcRecord(Extension extension) {
        for (Object obj : extension.getContent()) {
            if (obj instanceof WorkBibMarcRecord) {
                return (WorkBibMarcRecord) obj;
            }
        }
        return null;
    }

    public void setLocationDetails(InstanceEditorForm instanceEditorForm) {
        OleHoldings oleHoldings = instanceEditorForm.getInstance().getOleHoldings();
        Location oleHoldingsLocation = oleHoldings.getLocation();
        if (oleHoldingsLocation != null) {
            LocationLevel holdingsLocationLevel = oleHoldingsLocation.getLocationLevel();
            String holdingLocationCode = getLocationCode(holdingsLocationLevel);
            oleHoldings.getLocation().getLocationLevel().setName(holdingLocationCode);
        }

        List<Item> itemList = instanceEditorForm.getInstance().getItems().getItem();
        for (Item item : itemList) {
            Location location = item.getLocation();
            if (location != null) {
                LocationLevel itemLocationLevel = location.getLocationLevel();
                String itemLocationCode = getLocationCode(itemLocationLevel);
                item.getLocation().getLocationLevel().setName(itemLocationCode);
            }
        }
    }

    public String getLocationCode(LocationLevel locationLevel) {
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

    /**
     * TO set location values in the view
     *
     * @param instanceForm
     */
    public void setLocationDetails(WorkInstanceOlemlForm instanceForm) {
        OleHoldings oleHoldings = instanceForm.getSelectedHolding();
        if (oleHoldings != null) {
            Location oleHoldingsLocation = oleHoldings.getLocation();
            if (oleHoldingsLocation != null) {
                LocationLevel holdingsLocationLevel = oleHoldingsLocation.getLocationLevel();
                String holdingLocationCode = getLocationCode(holdingsLocationLevel);
                if (holdingsLocationLevel != null) {
                    oleHoldings.getLocation().getLocationLevel().setName(holdingLocationCode);
                }

            }
        }
        Item item = instanceForm.getSelectedItem();
        if (item != null) {
            Location location = item.getLocation();
            if (location != null) {
                LocationLevel itemLocationLevel = location.getLocationLevel();
                String itemLocationCode = getLocationCode(itemLocationLevel);
                if (itemLocationLevel != null) {
                    item.getLocation().getLocationLevel().setName(itemLocationCode);
                }
            }
        }
    }

    /**
     * Method to build Audit Details: Setting Created details for New record and updated details for existing records
     *
     * @param instanceEditorForm
     */
    private void buildAuditDetailsForNewInstance(InstanceEditorForm instanceEditorForm) {
        Extension extension = new Extension();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setAttribute(OLEConstants.CREATED_BY, loginUser);
        additionalAttributes.setAttribute(OLEConstants.DATE_ENTERED,
                String.valueOf(new SimpleDateFormat("MMM dd, yyyy").format(new Date())));
        extension.getContent().add(additionalAttributes);

        if (instanceEditorForm.getSelectedHolding() != null) {
            instanceEditorForm.getInstance().getOleHoldings().setExtension(extension);
        } else if (instanceEditorForm.getSelectedSourceHolding() != null) {
            instanceEditorForm.getInstance().getSourceHoldings().setExtension(extension);
        }
        instanceEditorForm.getInstance().getItems().getItem().get(0).setExtension(extension);
    }

    private void buildAuditDetailsForNewInstance(WorkInstanceOlemlForm instanceEditorForm, String staffOnlyFlagForHoldings) {
        Extension extension = new Extension();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setAttribute(OLEConstants.CREATED_BY, loginUser);
        additionalAttributes.setAttribute(OLEConstants.DATE_ENTERED,
                String.valueOf(new SimpleDateFormat("MMM dd, yyyy").format(new Date())));
        additionalAttributes.setAttribute("staffOnlyFlag", staffOnlyFlagForHoldings);
        additionalAttributes.setAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY,GlobalVariables.getUserSession().getPrincipalName());
        String holdingsCreatedDate=null;
        Format formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        holdingsCreatedDate = formatter.format(new Date());
        additionalAttributes.setAttribute(AdditionalAttributes.HOLDINGS_DATE_ENTERED,holdingsCreatedDate);
        String user = null;
        user = GlobalVariables.getUserSession().getPrincipalName();

        additionalAttributes.setAttribute("createdBy", user);
        extension.getContent().add(additionalAttributes);

        if (instanceEditorForm.getSelectedHolding() != null) {
            instanceEditorForm.getInstance().getOleHoldings().setExtension(extension);
        } else if (instanceEditorForm.getSelectedSourceHolding() != null) {
            instanceEditorForm.getInstance().getSourceHoldings().setExtension(extension);
        }
        //instanceEditorForm.getInstance().getItems().getItem().get(0).setExtension(extension);
    }

    /**
     * Method to set Meta Data fields while loading Holdings/Item record
     *
     * @param instanceEditorForm
     * @param docType
     * @return instanceEditorForm
     */
    public InstanceEditorForm setMetaData(InstanceEditorForm instanceEditorForm, String docType) {

        if (docType.equalsIgnoreCase(OLEConstants.HOLDING_DOC_TYPE)) {
            AdditionalAttributes holdingAdditionalAttributes =
                    getFirstAdditionalAttributes(instanceEditorForm.getInstance().getOleHoldings().getExtension());
            if (holdingAdditionalAttributes != null) {
                instanceEditorForm.getHoldingRecordMetaData().setCreatedBy(
                        holdingAdditionalAttributes.getAttribute(OLEConstants.CREATED_BY));
                instanceEditorForm.getHoldingRecordMetaData().setDateEntered(
                        holdingAdditionalAttributes.getAttribute(OLEConstants.DATE_ENTERED));
                InstanceRecordUpdatedDetails holdingRecordUpdatedDetails = new InstanceRecordUpdatedDetails();
                holdingRecordUpdatedDetails.setLastUpdated(holdingAdditionalAttributes.getAttribute(OLEConstants.LAST_UPDATED));
                holdingRecordUpdatedDetails.setLastUpdatedBy(holdingAdditionalAttributes.getAttribute(OLEConstants.LAST_UPDATED_BY));
                instanceEditorForm.getHoldingRecordMetaData().setUpdatedDetailsList(Arrays.asList(holdingRecordUpdatedDetails));
            }
        } else if (docType.equalsIgnoreCase(OLEConstants.ITEM_DOC_TYPE)) {
            AdditionalAttributes itemAdditionalAttributes = getFirstAdditionalAttributes(
                    instanceEditorForm.getSelectedItem().getExtension());
            if (itemAdditionalAttributes != null) {
                instanceEditorForm.getItemRecordMetaData().setCreatedBy(
                        itemAdditionalAttributes.getAttribute(OLEConstants.CREATED_BY));
                instanceEditorForm.getItemRecordMetaData().setDateEntered(
                        itemAdditionalAttributes.getAttribute(OLEConstants.DATE_ENTERED));
                InstanceRecordUpdatedDetails itemRecordUpdatedDetails = new InstanceRecordUpdatedDetails();
                itemRecordUpdatedDetails.setLastUpdated(itemAdditionalAttributes.getAttribute(OLEConstants.LAST_UPDATED));
                itemRecordUpdatedDetails.setLastUpdatedBy(itemAdditionalAttributes.getAttribute(OLEConstants.LAST_UPDATED_BY));
                instanceEditorForm.getItemRecordMetaData().setUpdatedDetailsList(Arrays.asList(itemRecordUpdatedDetails));
            }
        } else if (docType.equalsIgnoreCase(OLEConstants.SOURCEHOLDINGS_DOC_TYPE)) {

            AdditionalAttributes sourceHoldingAttributes =
                    getFirstAdditionalAttributes(instanceEditorForm.getInstance().getSourceHoldings().getExtension());
            if (sourceHoldingAttributes != null) {
                instanceEditorForm.getHoldingRecordMetaData().setCreatedBy(
                        sourceHoldingAttributes.getAttribute(OLEConstants.CREATED_BY));
                instanceEditorForm.getHoldingRecordMetaData().setDateEntered(
                        sourceHoldingAttributes.getAttribute(OLEConstants.DATE_ENTERED));
                InstanceRecordUpdatedDetails holdingRecordUpdatedDetails = new InstanceRecordUpdatedDetails();
                holdingRecordUpdatedDetails.setLastUpdated(sourceHoldingAttributes.getAttribute(OLEConstants.LAST_UPDATED));
                holdingRecordUpdatedDetails.setLastUpdatedBy(sourceHoldingAttributes.getAttribute(OLEConstants.LAST_UPDATED_BY));
                instanceEditorForm.getHoldingRecordMetaData().setUpdatedDetailsList(Arrays.asList(holdingRecordUpdatedDetails));
            }

        }
        return instanceEditorForm;
    }

    private void updateAuditDetails(AdditionalAttributes additionalAttributes) {
        if (additionalAttributes == null) {
            additionalAttributes = new AdditionalAttributes();
        }
        additionalAttributes.setAttribute(OLEConstants.LAST_UPDATED_BY, loginUser);
        additionalAttributes.setAttribute(OLEConstants.LAST_UPDATED,
                String.valueOf(new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a").format(new Date())));
    }

    private void updateAuditDetails(AdditionalAttributes additionalAttributes, String staffOnlyFlagForItem) {
        if (additionalAttributes == null) {
            additionalAttributes = new AdditionalAttributes();
        }
        additionalAttributes.setAttribute(OLEConstants.LAST_UPDATED_BY, loginUser);
        additionalAttributes.setAttribute(OLEConstants.LAST_UPDATED,
                String.valueOf(new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a").format(new Date())));
        additionalAttributes.setAttribute("staffOnlyFlag", staffOnlyFlagForItem);
    }

    private AdditionalAttributes getFirstAdditionalAttributes(Extension extension) {
        if (extension != null) {
            for (Object obj : extension.getContent()) {
                if (obj instanceof AdditionalAttributes) {
                    return (AdditionalAttributes) obj;
                }
            }
        }
        return null;
    }

    /**
     * This Method will ingest a new record in docStore for Instance Record type.
     * returns formatted xml
     *
     * @param workInstanceOlemlForm
     * @param uuid
     * @return content
     */
    public String buildInstanceRecordForDocStore(WorkInstanceOlemlForm workInstanceOlemlForm, String uuid) {
        List<Instance> oleInstanceList = new ArrayList<Instance>();
        InstanceCollection instanceCollection = new InstanceCollection();

        buildLocationLevels(workInstanceOlemlForm);
        if (workInstanceOlemlForm.getInstance().getSourceHoldings() != null) {
            SourceHoldings sourceHoldings = buildSourceHoldings(workInstanceOlemlForm);
            workInstanceOlemlForm.getInstance().setSourceHoldings(sourceHoldings);
        }
        // Modified if condition for left pane content.
        if (workInstanceOlemlForm.getSelectedItem() == null) {
            Item item = new Item();
            String enumeration = "New";
            String chronology = "Item";
            String volumeNumber = "Item";
            item.setVolumeNumber(volumeNumber);
            item.setCopyNumber("");
            item.setCallNumber(item.getCallNumber());
            Items items = new Items();
            items.setItem(Arrays.asList(item));
            workInstanceOlemlForm.getInstance().setItems(items);
        }
        buildAuditDetailsForNewInstance(workInstanceOlemlForm);

        oleInstanceList.add(workInstanceOlemlForm.getInstance());

        instanceCollection.setInstance(oleInstanceList);
        String content = new InstanceOlemlRecordProcessor().toXML(instanceCollection);
        return content;
    }


    public String buildInstanceRecordForDocStore(WorkInstanceOlemlForm workInstanceOlemlForm, String uuid, String staffOnlyForHoldings) {
        List<Instance> oleInstanceList = new ArrayList<Instance>();
        InstanceCollection instanceCollection = new InstanceCollection();

        buildLocationLevels(workInstanceOlemlForm);
        if (workInstanceOlemlForm.getInstance() != null && workInstanceOlemlForm.getInstance().getSourceHoldings() != null) {
            SourceHoldings sourceHoldings = buildSourceHoldings(workInstanceOlemlForm);
            workInstanceOlemlForm.getInstance().setSourceHoldings(sourceHoldings);
        }
        // Modified if condition for left pane content.
        if (workInstanceOlemlForm.getSelectedItem() == null) {
            Item item = new Item();
            String volumeNumber = "Item";
            item.setVolumeNumber(volumeNumber);
            item.setCopyNumber("");
            item.setCallNumber(item.getCallNumber());
            Items items = new Items();
            items.setItem(Arrays.asList(item));
            workInstanceOlemlForm.getInstance().setItems(items);
        }
        buildAuditDetailsForNewInstance(workInstanceOlemlForm, staffOnlyForHoldings);

        oleInstanceList.add(workInstanceOlemlForm.getInstance());

        instanceCollection.setInstance(oleInstanceList);
        String content = new InstanceOlemlRecordProcessor().toXML(instanceCollection);
        return content;
    }

    public void buildLocationLevels(WorkInstanceOlemlForm instanceEditorForm) {
        OleHoldings oleHoldings = instanceEditorForm.getSelectedHolding();
        Location location = new Location();
        LocationLevel locationLevel = new LocationLevel();

        String holdingsLocationName = oleHoldings.getLocation().getLocationLevel().getName();
        if (StringUtils.isNotEmpty(holdingsLocationName)) {
            locationLevel = createLocationLevel(holdingsLocationName, locationLevel);
            location.setLocationLevel(locationLevel);
            location.setPrimary("true");
            location.setStatus("permanent");
            oleHoldings.setLocation(location);
        } else {
            oleHoldings.setLocation(null);
        }

        if (instanceEditorForm.getSelectedItem() != null) {
            Item item = instanceEditorForm.getSelectedItem();
            Location itemLocation = new Location();
            LocationLevel itemLocationLevel = new LocationLevel();

            //String itemsLocationName = item.getLocation().getLocationLevel().getName();
            if (item.getLocation() != null && item.getLocation().getLocationLevel() != null
                    && item.getLocation().getLocationLevel().getName() != null) {
                if (!item.getLocation().getLocationLevel().getName().equalsIgnoreCase("")) {
                    itemLocationLevel = createLocationLevel(item.getLocation().getLocationLevel().getName(),
                            itemLocationLevel);
                    itemLocation.setLocationLevel(itemLocationLevel);
                    itemLocation.setPrimary("true");
                    itemLocation.setStatus("temporary");
                    item.setLocation(itemLocation);
                }
            } else {
                item.setLocation(null);
                item.setVolumeNumber("Item");
                //  item.setEnumeration("New");

            }
            instanceEditorForm.setSelectedItem(item);
        }
    }

    public SourceHoldings buildSourceHoldings(WorkInstanceOlemlForm instanceEditorForm) {
        SourceEditorForUI sourceEditorForUI = instanceEditorForm.getSelectedSourceHolding();
        WorkBibMarcRecord workBibMarcRecord = new WorkBibMarcRecord();
        workBibMarcRecord.setLeader(sourceEditorForUI.getLeader());
        workBibMarcRecord.setControlFields(buildBibliographicContrlFields(sourceEditorForUI.getControlFields()));
        workBibMarcRecord.setDataFields(buildBibliographicDataFields(sourceEditorForUI.getDataFields()));

        SourceHoldings sourceHoldings = new SourceHoldings();
        Extension extension = new Extension();
        extension.getContent().add(workBibMarcRecord);
        sourceHoldings.setHoldings(extension);
        sourceHoldings.setHoldingsIdentifier(sourceEditorForUI.getUuid());

        if (!sourceEditorForUI.getPrimary().equalsIgnoreCase("true")) {
            sourceHoldings.setPrimary("false");
        } else {
            sourceHoldings.setPrimary(sourceEditorForUI.getPrimary());
        }

        return sourceHoldings;
    }


    /**
     * Method to build Audit Details: Setting Created details for New record and updated details for existing records
     *
     * @param workInstanceOlemlForm
     */
    private void buildAuditDetailsForNewInstance(WorkInstanceOlemlForm workInstanceOlemlForm) {
        Extension extension = new Extension();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setAttribute(OLEConstants.CREATED_BY, loginUser);
        additionalAttributes.setAttribute(OLEConstants.DATE_ENTERED,
                String.valueOf(new SimpleDateFormat("MMM dd, yyyy").format(new Date())));
        extension.getContent().add(additionalAttributes);

        if (workInstanceOlemlForm.getSelectedHolding() != null) {
            workInstanceOlemlForm.getInstance().getOleHoldings().setExtension(extension);
        } else if (workInstanceOlemlForm.getSelectedSourceHolding() != null) {
            workInstanceOlemlForm.getInstance().getSourceHoldings().setExtension(extension);
        }
        workInstanceOlemlForm.getInstance().getItems().getItem().get(0).setExtension(extension);
    }

    /**
     * Compute statistical searching name if statistical code value is available.
     *
     * @param statisticalSearchingCode
     */
    private void computeStatisticalSearching(StatisticalSearchingCode statisticalSearchingCode) {
        OleStatisticalSearchingCodes oleStatisticalSearchingCodes = new OleStatisticalSearchingCodes();
        String statisticalSearchingFullValue = "";
        if (statisticalSearchingCode != null) {
            if (statisticalSearchingCode.getCodeValue() != null) {
                String statisticalSearchingCodeValue = statisticalSearchingCode.getCodeValue();
                //statistical searching code is available
                if (StringUtils.isNotEmpty(statisticalSearchingCodeValue)) {
                    BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
                    Map parentCriteria = new HashMap();
                    parentCriteria.put("statisticalSearchingCode", statisticalSearchingCodeValue);
                    oleStatisticalSearchingCodes = businessObjectService
                            .findByPrimaryKey(OleStatisticalSearchingCodes.class, parentCriteria);
                    if (oleStatisticalSearchingCodes != null) {
                        statisticalSearchingFullValue = oleStatisticalSearchingCodes.getStatisticalSearchingName();
                        statisticalSearchingCode.setFullValue(statisticalSearchingFullValue);
                    }
                }
            }
        }
    }


    /**
     * compute item type name if item type code is available
     *
     * @param itemType
     */
    private void computeItemType(ItemType itemType) {
        OleInstanceItemType oleInstanceItemType = new OleInstanceItemType();
        String itemTypeName = "";
        if (itemType != null) {
            if (itemType.getCodeValue() != null) {
                String itemTypeCodeValue = itemType.getCodeValue();
                //item type code is available
                if (StringUtils.isNotEmpty(itemTypeCodeValue)) {
                    BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
                    Map parentCriteria = new HashMap();
                    parentCriteria.put("instanceItemTypeCode", itemTypeCodeValue);
                    oleInstanceItemType = businessObjectService
                            .findByPrimaryKey(OleInstanceItemType.class, parentCriteria);
                    if (oleInstanceItemType != null) {
                        itemTypeName = oleInstanceItemType.getInstanceItemTypeName();
                        itemType.setFullValue(itemTypeName);
                    }
                }
            }
        }
    }

    private String computeTempItemType(ItemType itemType) {
        OleInstanceItemType oleInstanceItemType = new OleInstanceItemType();
        String tempItemTypeName = "";
        if (itemType != null) {
            if (itemType.getCodeValue() != null) {
                String tempItemTypeCodeValue = itemType.getCodeValue();
                //item type code is available
                if (StringUtils.isNotEmpty(tempItemTypeCodeValue)) {
                    BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
                    Map parentCriteria = new HashMap();
                    parentCriteria.put("instanceItemTypeCode", tempItemTypeCodeValue);
                    oleInstanceItemType = businessObjectService
                            .findByPrimaryKey(OleInstanceItemType.class, parentCriteria);
                    if (oleInstanceItemType != null) {
                        tempItemTypeName = oleInstanceItemType.getInstanceItemTypeName();
                    }
                }
            }
        }
        return tempItemTypeName;
    }
    /**
     * Gets the businessObjectService attribute.
     *
     * @return Returns the businessObjectService
     */
    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public String getParameter(String nameSpace, String component, String name) {
        String parameter = "";
        try {
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put("namespaceCode", nameSpace);
            criteriaMap.put("componentCode", component);
            criteriaMap.put("name", name);
            List<ParameterBo> parametersList = (List<ParameterBo>) getBusinessObjectService()
                    .findMatching(ParameterBo.class, criteriaMap);
            for (ParameterBo parameterBo : parametersList) {
                parameter = parameterBo.getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception:" + e.getMessage(), e);
        }
        return parameter;
    }


}
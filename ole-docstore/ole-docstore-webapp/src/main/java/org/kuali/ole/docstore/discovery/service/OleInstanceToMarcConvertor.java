package org.kuali.ole.docstore.discovery.service;

import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.Items;
import org.kuali.ole.docstore.discovery.bo.OleDiscoveryMarcExportProfile;
import org.kuali.ole.docstore.discovery.bo.OleDiscoveryMarcMappingField;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/20/13
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleInstanceToMarcConvertor {

    private static final Logger LOG = Logger.getLogger(String.valueOf(OleInstanceToMarcConvertor.class));

    public List<DataField> generateDataField(InstanceCollection instanceCollection, OleDiscoveryMarcExportProfile oleDiscoveryMarcExportProfile) {
        List<Instance> instanceList = new ArrayList<Instance>();
        List<DataField> dataFieldList = new ArrayList<DataField>();
        if (instanceCollection != null) {
            DataField dataField;
            Instance instance;
            List<SubField> SubFieldList;
            for (int i = 0; i < instanceCollection.getInstance().size(); i++) {
                instance = instanceCollection.getInstance().get(i);
                Items items = (Items) instance.getItems();
                for (int j = 0; j < items.getItem().size(); j++) {
                    dataField = new DataField();
                    dataField.setTag(oleDiscoveryMarcExportProfile.getDataField());
                    dataField.setSubFields(generateSubFields(oleDiscoveryMarcExportProfile, instance, items.getItem().get(j)));
                    dataFieldList.add(dataField);
                }
            }
        }
        return dataFieldList;
    }

    private List<SubField> generateSubFields(OleDiscoveryMarcExportProfile oleDiscoveryMarcExportProfile, Instance instance, Item item) {
        List<SubField> subFieldList = new ArrayList<SubField>();
        List<OleDiscoveryMarcMappingField> oleDiscoveryMarcMappingFieldList = oleDiscoveryMarcExportProfile.getOleDiscoveryMarcMappingFields();

        for (OleDiscoveryMarcMappingField mappingField : oleDiscoveryMarcMappingFieldList) {
            if (mappingField.getItemField().equalsIgnoreCase("Call Number")) {
                subFieldList.add(generateMarcCallNumber(instance, mappingField.getMarcField()));
            }
            if (mappingField.getItemField().equalsIgnoreCase("Class Scheme")) {
                subFieldList.add(generateMarcCallSchema(instance, mappingField.getMarcField()));
            }
            if (mappingField.getItemField().equalsIgnoreCase("Copy Number")) {
                subFieldList.add(generateMarcCopyNumber(item, mappingField.getMarcField()));
            }
            if (mappingField.getItemField().equalsIgnoreCase("Item Id")) {
                subFieldList.add(generateMarcItemNumber(item, mappingField.getMarcField()));
            }
            if (mappingField.getItemField().equalsIgnoreCase("Type")) {
                subFieldList.add(generateMarcItemType(item, mappingField.getMarcField()));
            }
            if (mappingField.getItemField().equalsIgnoreCase("Date Created")) {
                subFieldList.add(generateMarcDateCreated(instance, mappingField.getMarcField()));
            }
            if (mappingField.getItemField().equalsIgnoreCase("Current Location")) {
                subFieldList.add(generateCurrentLocation(item, mappingField.getMarcField()));
            }
            if (mappingField.getItemField().equalsIgnoreCase("Home Location")) {
                subFieldList.add(generateHomeLocation(instance, mappingField.getMarcField()));
            }
            if (mappingField.getItemField().equalsIgnoreCase("Library")) {
                subFieldList.add(generateLibraryLocation(instance, mappingField.getMarcField()));
            }
        }

        return subFieldList;
    }

    private SubField generateMarcCallNumber(Instance instance, String code) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue("");
        if (instance != null && instance.getOleHoldings() != null && instance.getOleHoldings().getCallNumber() != null && instance.getOleHoldings().getCallNumber().getNumber() != null) {
            subField.setValue(instance.getOleHoldings().getCallNumber().getNumber());
        }
        return subField;
    }

    private SubField generateMarcCallSchema(Instance instance, String code) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue("");
        if (instance != null && instance.getOleHoldings() != null && instance.getOleHoldings().getCallNumber() != null && instance.getOleHoldings().getCallNumber().getType() != null) {
            subField.setValue(instance.getOleHoldings().getCallNumber().getType());
        }
        return subField;
    }

    private SubField generateMarcCopyNumber(Item item, String code) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue("");
        if (item != null && item.getCopyNumber() != null) {
            subField.setValue(item.getCopyNumber());
        }
        return subField;
    }

    private SubField generateMarcEnumeration(Item item, String code) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue("");
        if (item != null && item.getEnumeration() != null) {
            subField.setValue(item.getEnumeration());
        }
        return subField;
    }

    private SubField generateMarcItemNumber(Item item, String code) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue("");
        if (item != null && item.getAccessInformation() != null && item.getAccessInformation().getBarcode() != null) {
            subField.setValue(item.getAccessInformation().getBarcode());
        }
        return subField;
    }

    private SubField generateMarcItemType(Item item, String code) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue("");
        if (item != null && item.getItemType() != null) {
            subField.setValue(item.getItemType().getCodeValue());
        }
        return subField;
    }


    private SubField generateMarcDateCreated(Instance instance, String code) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue("");
        try {
            if (instance != null && instance.getExtension().getContent() != null) {
                List<Object> additionalAttributes = (List<Object>) instance.getExtension().getContent();
                if (additionalAttributes.size() > 0) {
                    AdditionalAttributes additionalAttributes1 = (AdditionalAttributes) additionalAttributes.get(0);
                    subField.setValue(additionalAttributes1.getDateEntered());
                }
            }
        } catch (Exception e) {
            LOG.info("Date Created value is not available" + e);
        }
        return subField;
    }

    private SubField generateCurrentLocation(Item item, String code) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue("");
        try {
            if (item != null && item.getLocation().getLocationLevel().getName() != null) {
                if (!item.getLocation().getLocationLevel().equals("") && item.getLocation().getLocationLevel() != null) {
                    if (item.getLocation().getLocationLevel().getName().equals("Shelving Location")) {
                        subField.setValue(item.getLocation().getLocationLevel().getName());
                    }
                }
            }
        } catch (Exception e) {
            LOG.info("Current Location is not available" + e );
        }
        return subField;
    }

    private SubField generateHomeLocation(Instance instance, String code) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue("");
        try {
            if (instance != null) {
                if (!instance.getOleHoldings().getLocation().equals("") && instance.getOleHoldings().getLocation().getLocationLevel() != null) {
                    if (instance.getOleHoldings().getLocation().getLocationLevel().getName().equals("Collection")) {
                        subField.setValue(instance.getOleHoldings().getLocation().getLocationLevel().getName());
                    }
                }
            }
        } catch (Exception e) {
            LOG.info("Home Location is not available" + e);
        }
        return subField;
    }


    private SubField generateLibraryLocation(Instance instance, String code) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue("");
        try {
            if (instance != null) {
                if (!instance.getOleHoldings().getLocation().equals("") && instance.getOleHoldings().getLocation().getLocationLevel() != null) {
                    if (instance.getOleHoldings().getLocation().getLocationLevel().getName().equals("Library")) {
                        subField.setValue(instance.getOleHoldings().getLocation().getLocationLevel().getName());
                    }
                }
            }
        } catch (Exception e) {
            LOG.info("Library Location is not available" + e);
        }
        return subField;
    }

}

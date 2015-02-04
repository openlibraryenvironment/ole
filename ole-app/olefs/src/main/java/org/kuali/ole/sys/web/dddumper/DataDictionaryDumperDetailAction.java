/*
 * Copyright 2010 The Regents of the University of California.
 */
package org.kuali.ole.sys.web.dddumper;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kns.datadictionary.BusinessObjectEntry;
import org.kuali.rice.kns.datadictionary.InquiryDefinition;
import org.kuali.rice.kns.datadictionary.LookupDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableCollectionDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableFieldDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableItemDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableSectionDefinition;
import org.kuali.rice.kns.service.BusinessObjectDictionaryService;
import org.kuali.rice.kns.service.BusinessObjectMetaDataService;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService;
import org.kuali.rice.kns.service.TransactionalDocumentDictionaryService;
import org.kuali.rice.kns.web.struts.action.KualiAction;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.AttributeSecurity;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.datadictionary.ReferenceDefinition;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.PersistenceStructureService;

public class DataDictionaryDumperDetailAction extends KualiAction {
    protected transient MaintenanceDocumentDictionaryService maintDocSvc;
    protected transient BusinessObjectDictionaryService boSvc;
    protected transient DataDictionaryService ddSvc;
    protected transient BusinessObjectMetaDataService boMetaSvc;
    protected transient TransactionalDocumentDictionaryService transDocSvc;
    protected transient MetadataManager mm;
    protected PersistenceStructureService persistSS;
    protected transient DescriptorRepository dr;
    protected Set<String> documentClasses = new HashSet<String>();
    protected Set<String> objectsToSkip = new HashSet<String>();
    protected DataDictionaryDumperDetailForm dumperDetailForm;
    protected String entityClass;
    protected String jstlKey;
    protected String docClass;
    protected String type;
    protected List<DataDictionaryDumperDocumentRow> rowList;
    protected List<DataDictionaryDumperSection> sectionList;
    protected List<DataDictionaryDumperDocumentRow> unprocessedRows;

    protected static final Logger LOG = Logger.getLogger(DataDictionaryDumperDetailAction.class);

    public DataDictionaryDumperDetailAction() {
        maintDocSvc = KNSServiceLocator.getMaintenanceDocumentDictionaryService();
        boSvc = KNSServiceLocator.getBusinessObjectDictionaryService();
        ddSvc = KNSServiceLocator.getDataDictionaryService();
        boMetaSvc = KNSServiceLocator.getBusinessObjectMetaDataService();
        transDocSvc = KNSServiceLocator.getTransactionalDocumentDictionaryService();

        mm = MetadataManager.getInstance();
        dr = mm.getRepository();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        dumperDetailForm = (DataDictionaryDumperDetailForm) form;
        entityClass = request.getParameter("entryClass");
//        entityClass = entityClass.substring(6);
        jstlKey = request.getParameter("jstlKey");
        docClass = request.getParameter("docClass");
        type = request.getParameter("type");
        sectionList = dumperDetailForm.getSections();
        dumperDetailForm.setTitle(request.getParameter("title"));

        if (docClass.endsWith("MaintenanceDocument")) {
            processDumpDataDictionary_MaintenanceDocuments();
        }
        else {
            processDumpDataDictionary_TransactionalDocuments();
        }

        return mapping.findForward("basic");
    }

    public void processDumpDataDictionary_MaintenanceDocuments() {
        Class<BusinessObject> boClass = maintDocSvc.getDataObjectClass(jstlKey);// Could use request param
        processDumpDataDictionary_MaintenanceDocuments(boClass);

//        if (ObjectUtils.getNestedValue((boClass.newInstance()), "extension") != null) {
//            Class<BusinessObject> tempClass = (Class<BusinessObject>) ObjectUtils.getNestedValue((boClass.newInstance()), "extension").getClass();
//            processUnprocessedRows(tempClass, unprocessedRows);
//        }
        // processUnprocessedRows(null, unprocessedRows);
        for (MaintainableCollectionDefinition boCollection : maintDocSvc.getMaintainableCollections(jstlKey)) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("*******" + boCollection.getName());
            }
            Class<BusinessObject> collectionClass = (Class<BusinessObject>) boCollection.getBusinessObjectClass();
            processDumpDataDictionary_MaintenanceDocumentCollections(collectionClass);
            processUnprocessedRows(collectionClass, unprocessedRows);
        }
    }

    protected DataDictionaryDumperSection getDetailSectionForBoClass( Class<? extends BusinessObject> boClass ) {
        DataDictionaryDumperSection detailSection = new DataDictionaryDumperSection();
        rowList = detailSection.getDocumentRows();
        unprocessedRows = new ArrayList<DataDictionaryDumperDocumentRow>();

        detailSection.setDocId(jstlKey);
        DocumentEntry documentEntry = ddSvc.getDataDictionary().getDocumentEntry(jstlKey);
        entityClass = boClass.getName();
        detailSection.setBusinessObject(entityClass);

        DocumentType docType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(documentEntry.getDocumentTypeName());
        if ( docType != null ) {
            detailSection.setDocName(docType.getLabel());
        } else {
            detailSection.setDocName("Not Found: " + documentEntry.getDocumentTypeName() );
        }

        if ( documentEntry.getDocumentAuthorizerClass() != null ) {
            detailSection.setDocumentAuthorizerClass(documentEntry.getDocumentAuthorizerClass().getName());
        }
        if ( documentEntry.getBusinessRulesClass() != null ) {
            detailSection.setBusinessObjectRulesClass(documentEntry.getBusinessRulesClass().getName());
        }
        if ( dr.getDescriptorFor(boClass) != null ) {
            detailSection.setTable(dr.getDescriptorFor(boClass).getFullTableName());
        }
        return detailSection;
    }

    protected void processDumpDataDictionary_MaintenanceDocuments(Class<BusinessObject> boClass) {
        DataDictionaryDumperSection detailSection = getDetailSectionForBoClass(boClass);

        if (boClass != null) {

            ClassDescriptor cd = dr.getDescriptorFor(boClass);
            List<MaintainableSectionDefinition> sections = maintDocSvc.getMaintainableSections(jstlKey);
            Collection<ReferenceDefinition> existenceChecks = maintDocSvc.getDefaultExistenceChecks(jstlKey);
            for (MaintainableSectionDefinition section : sections) {
                Collection<MaintainableItemDefinition> items = section.getMaintainableItems();
                for (MaintainableItemDefinition item : items) {
                    DataDictionaryDumperDocumentRow detailRow = new DataDictionaryDumperDocumentRow();
                    if (item.getName().endsWith("versionNumber")) {
                        continue;
                    }
                    StringBuilder row = new StringBuilder(1000);
                    if (item instanceof MaintainableFieldDefinition) {
                        MaintainableFieldDefinition field = (MaintainableFieldDefinition) item;
                        String fieldName = field.getName();
                        FieldDescriptor fd = cd.getFieldDescriptorByName(fieldName);
                        LookupDefinition ld = null;
                        InquiryDefinition id = null;
                        if (ddSvc.getDataDictionary().getBusinessObjectEntry(boClass.getSimpleName()) != null) {
                            ld = ((BusinessObjectEntry)ddSvc.getDataDictionary().getBusinessObjectEntry(boClass.getSimpleName())).getLookupDefinition();
                            id = ((BusinessObjectEntry)ddSvc.getDataDictionary().getBusinessObjectEntry(boClass.getSimpleName())).getInquiryDefinition();
                        }
                        List<String> lookupFields = (ld == null) ? Collections.EMPTY_LIST : ld.getLookupFieldNames();
                        List<String> resultFields = (ld == null) ? Collections.EMPTY_LIST : ld.getResultFieldNames();

                        detailRow.setName(ddSvc.getAttributeLabel(boClass, fieldName));
                        detailRow.setFieldName(fieldName);
                        detailRow.setRequired(field.isRequired());
                        detailRow.setDefaultValue((field.getDefaultValue() == null) ? ((field.getDefaultValueFinderClass() != null) ? field.getDefaultValueFinderClass().getSimpleName() : "") : field.getDefaultValue());
                        detailRow.setReadOnly(field.isUnconditionallyReadOnly());
                        if (ddSvc.getAttributeValidatingExpression(boClass, fieldName) != null) {
                            detailRow.setValidationRules(ddSvc.getAttributeValidatingExpression(boClass, fieldName).toString());
                        }
                        else {
                            detailRow.setValidationRules("(null)");
                        }
                        if (ddSvc.getAttributeMaxLength(boClass, fieldName) != null) {
                            detailRow.setMaxLength(ddSvc.getAttributeMaxLength(boClass, fieldName).toString());
                        }
                        else {
                            detailRow.setMaxLength("(null)");
                        }
                        detailRow.setExistenceCheck(hasExistenceCheck(existenceChecks, fieldName));
                        detailRow.setLookupParam(lookupFields.contains(fieldName));
                        detailRow.setLookupResult(resultFields.contains(fieldName));
                        try {
                            String fullControlDefinition = ddSvc.getAttributeControlDefinition(boClass, fieldName).getClass().getSimpleName();
                            detailRow.setControlDefinition(fullControlDefinition.substring(0, fullControlDefinition.indexOf("ControlDefinition")));
                        }
                        catch (NullPointerException npe) {
                            // Probably from a collection at .getClass
                            break;
                        }
                        AttributeSecurity fieldSecurity = ddSvc.getAttributeSecurity(boClass.getName(), fieldName);
                        if (fieldSecurity != null) {
                            detailRow.setFieldSecurity((fieldSecurity.isHide() ? "Hide<br>" : "") + (fieldSecurity.isMask() ? "Mask<br>" : "") + (fieldSecurity.isPartialMask() ? "Partial Mask<br>" : "") + (fieldSecurity.isReadOnly() ? "Read Only" : ""));
                        }
                        else {
                            detailRow.setFieldSecurity("");
                        }
                        if (fd != null) {
                            detailRow.setFieldType(fd.getColumnType());
                            detailRow.setColumn(fd.getColumnName());
                            detailRow.setColumnNo(fd.getColNo());
                            rowList.add(detailRow);
                        }
                        else {
                            unprocessedRows.add(detailRow);
                        }

                    }

                }
            }

        }
        Collections.sort(rowList);
        detailSection.setDocumentRows(rowList);
        sectionList.add(detailSection);

    }

    protected void processDumpDataDictionary_MaintenanceDocumentCollections(Class<BusinessObject> boClass) {
        DataDictionaryDumperSection detailSection = getDetailSectionForBoClass(boClass);
        detailSection.setCollection(true);

        if (boClass != null) {

            ClassDescriptor cd = dr.getDescriptorFor(boClass);
            List<MaintainableSectionDefinition> sections = maintDocSvc.getMaintainableSections(jstlKey);
            Collection<ReferenceDefinition> existenceChecks = maintDocSvc.getDefaultExistenceChecks(jstlKey);
            Collection<AttributeDefinition> items = ddSvc.getDataDictionary().getBusinessObjectEntry(boClass.getSimpleName()).getAttributes();
            for (AttributeDefinition item : items) {
                DataDictionaryDumperDocumentRow detailRow = new DataDictionaryDumperDocumentRow();
                //System.out.println(entityClass + " item name " + item.getName() + " item class " + item.getClass());
                if (item.getName().endsWith("versionNumber")) {
                    continue;
                }
                StringBuilder row = new StringBuilder(1000);

                FieldDescriptor fd = cd.getFieldDescriptorByName(item.getName());
                LookupDefinition ld = null;
                InquiryDefinition id = null;
                if (ddSvc.getDataDictionary().getBusinessObjectEntry(boClass.getSimpleName()) != null) {
                    ld = ((BusinessObjectEntry)ddSvc.getDataDictionary().getBusinessObjectEntry(boClass.getSimpleName())).getLookupDefinition();
                    id = ((BusinessObjectEntry)ddSvc.getDataDictionary().getBusinessObjectEntry(boClass.getSimpleName())).getInquiryDefinition();
                }
                List<String> lookupFields = (ld == null) ? Collections.EMPTY_LIST : ld.getLookupFieldNames();
                List<String> resultFields = (ld == null) ? Collections.EMPTY_LIST : ld.getResultFieldNames();

                detailRow.setName(ddSvc.getAttributeLabel(boClass, item.getName()));
                detailRow.setFieldName(item.getName());
                detailRow.setRequired(item.isRequired());
                if (ddSvc.getAttributeValidatingExpression(boClass, item.getName()) != null) {
                    detailRow.setValidationRules(ddSvc.getAttributeValidatingExpression(boClass, item.getName()).toString());
                }
                else {
                    detailRow.setValidationRules("(null)");
                }
                if (ddSvc.getAttributeMaxLength(boClass, item.getName()) != null) {
                    detailRow.setMaxLength(ddSvc.getAttributeMaxLength(boClass, item.getName()).toString());
                }
                else {
                    detailRow.setMaxLength("(null)");
                }
                detailRow.setExistenceCheck(hasExistenceCheck(existenceChecks, item.getName()));
                detailRow.setLookupParam(lookupFields.contains(item.getName()));
                detailRow.setLookupResult(resultFields.contains(item.getName()));
                try {
                    String fullControlDefinition = ddSvc.getAttributeControlDefinition(boClass, item.getName()).getClass().getSimpleName();
                    detailRow.setControlDefinition(fullControlDefinition.substring(0, fullControlDefinition.indexOf("ControlDefinition")));
                }
                catch (NullPointerException npe) {
                    // Probably from a collection at .getClass
                    break;
                }
                AttributeSecurity fieldSecurity = ddSvc.getAttributeSecurity(boClass.getName(), item.getName());
                if (fieldSecurity != null) {
                    detailRow.setFieldSecurity((fieldSecurity.isHide() ? "Hide<br>" : "") + (fieldSecurity.isMask() ? "Mask<br>" : "") + (fieldSecurity.isPartialMask() ? "Partial Mask<br>" : "") + (fieldSecurity.isReadOnly() ? "Read Only" : ""));
                }
                else {
                    detailRow.setFieldSecurity("");
                }
                if (fd != null) {
                    detailRow.setFieldType(fd.getColumnType());
                    detailRow.setColumn(fd.getColumnName());
                    detailRow.setColumnNo(fd.getColNo());
                    rowList.add(detailRow);
                }
                else {
                    unprocessedRows.add(detailRow);
                }


            }


        }
        Collections.sort(rowList);
        detailSection.setDocumentRows(rowList);
        sectionList.add(detailSection);

    }

    /**
     * @param boClass
     * @param processingRows
     * @throws Exception
     */
    protected void processUnprocessedRows(Class<BusinessObject> boClass, List<DataDictionaryDumperDocumentRow> processingRows) {
        DataDictionaryDumperSection detailSection = new DataDictionaryDumperSection();
        rowList = detailSection.getDocumentRows();
        unprocessedRows = new ArrayList<DataDictionaryDumperDocumentRow>();
        if ( rowList != null ) {
            detailSection.setDocId(jstlKey);
            DocumentType docType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(jstlKey);
            if ( docType != null ) {
                detailSection.setDocName(docType.getLabel());
            } else {
                detailSection.setDocName("Not Found: " + jstlKey );
            }


            for (DataDictionaryDumperDocumentRow row : processingRows) {

                DataDictionaryDumperDocumentRow detailRow = new DataDictionaryDumperDocumentRow();

                detailRow.setName(row.getName());
                detailRow.setRequired(row.isRequired());
                detailRow.setDefaultValue(row.getDefaultValue());
                detailRow.setReadOnly(row.isReadOnly());
                detailRow.setValidationRules(row.getValidationRules());
                detailRow.setMaxLength(row.getMaxLength());
                detailRow.setExistenceCheck(row.isExistenceCheck());
                detailRow.setLookupParam(row.isLookupParam());
                detailRow.setLookupResult(row.isLookupResult());
                detailRow.setControlDefinition(row.getControlDefinition());
                detailRow.setFieldSecurity(row.getFieldSecurity());
                if (boClass != null) {
                    detailSection.setBusinessObject(boClass.getName());
                    if ( dr.hasDescriptorFor(boClass) ) {
                        detailSection.setTable(dr.getDescriptorFor(boClass).getFullTableName());
                        String fieldName = row.getFieldName().substring(row.getFieldName().indexOf('.') + 1);
                        FieldDescriptor fd = dr.getDescriptorFor(boClass).getFieldDescriptorByName(fieldName);
                        if (fd != null) {
                            detailRow.setFieldType(fd.getColumnType());
                            detailRow.setColumn(fd.getColumnName());
                            detailRow.setColumnNo(fd.getColNo());
                            rowList.add(detailRow);
                        }
                    } else {
                        unprocessedRows.add(row);
                    }
                }
                else {
                    // detailSection.setBusinessObject("Unprocessed Rows");
                    // detailSection.setTable("Not Applicable");
                    // rowList.add(detailRow); //If boClass is null this is terminal iteration and all rows must be processed
                }
            }
            Collections.sort(rowList);
            detailSection.setDocumentRows(rowList);
            if (detailSection.getDocumentRows().size() > 0) {
                sectionList.add(detailSection);// If there are only rows that are not on the extension table.
            }
        }
    }

    public void processDumpDataDictionary_TransactionalDocuments() {
        try {
            Class<? extends Document> documentClazz = (Class<? extends Document>) Class.forName(entityClass);
            processDumpDataDictionary_TransactionalDocuments(documentClazz);
        } catch (ClassNotFoundException ex) {
            LOG.error( "Unable to find document class: " + entityClass );
        }
    }

    public void processDumpDataDictionary_TransactionalDocuments(Class<? extends Document> documentClazz) {

        /*
         * get all docs check if transactional get doc class look for instance of AccountingLineBase find contained BOs - loop over
         * their attributes (from DD - to get collection attributes) ignore document header class TODO: check persistence layer for
         * updateability TODO: include source/target accounting lines? TODO: handle collections and nested BOs
         */
        DataDictionaryDumperSection detailSection = getDetailSectionForBoClass(documentClazz);

        if (!dr.hasDescriptorFor(documentClazz)) {
            return;
        }
        ClassDescriptor cd = dr.getDescriptorFor(documentClazz);
        List<AttributeDefinition> attributes = ddSvc.getDataDictionary().getDocumentEntry(jstlKey).getAttributes();
        // sb.append( attributes );
        for (AttributeDefinition attribute : attributes) {
            DataDictionaryDumperDocumentRow detailRow = new DataDictionaryDumperDocumentRow();
            String attributeName = attribute.getName();
            if (attributeName.startsWith("documentHeader.")) {
                continue;
            }
            if (attributeName.endsWith("versionNumber")) {
                continue;
            }
            StringBuilder row = new StringBuilder(1000);
            FieldDescriptor fd = cd.getFieldDescriptorByName(attributeName);

            detailRow.setName(attribute.getLabel());

            detailRow.setRequired(attribute.isRequired());
            detailRow.setValidationRules((attribute.getValidationPattern() == null) ? "" : attribute.getValidationPattern().getRegexPattern().toString());
            detailRow.setMaxLength((attribute.getMaxLength() == null) ? "" : attribute.getMaxLength().toString());
            detailRow.setFieldType((fd == null) ? "" : fd.getColumnType());

            detailRow.setControlDefinition(ddSvc.getAttributeControlDefinition(documentClazz, attributeName).getClass().getSimpleName());
            AttributeSecurity fieldSecurity = ddSvc.getAttributeSecurity(documentClazz.getName(), attributeName);
            if (fieldSecurity != null) {
                detailRow.setFieldSecurity((fieldSecurity.isHide() ? "Hide<br>" : "") + (fieldSecurity.isMask() ? "Mask<br>" : "") + (fieldSecurity.isPartialMask() ? "Partial Mask<br>" : "") + (fieldSecurity.isReadOnly() ? "Read Only" : ""));
            }
            else {
                detailRow.setFieldSecurity("");
            }

            if (fd != null) {
                detailRow.setFieldType(fd.getColumnType());
                detailRow.setColumn(fd.getColumnName());
                detailRow.setColumnNo(fd.getColNo());
//                System.out.println(fd.getColNo());
            }
            // System.out.println(detailRow.getName()+"  "+detailRow.getColumnNo());

            rowList.add(detailRow);
        }
        PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(documentClazz);
        for (PropertyDescriptor prop : props) {
            DataDictionaryDumperDocumentRow detailRow = new DataDictionaryDumperDocumentRow();

            if (!objectsToSkip.contains(prop.getDisplayName()) && prop.getPropertyType() != null && PersistableBusinessObject.class.isAssignableFrom(prop.getPropertyType()) && ddSvc.getDataDictionary().getBusinessObjectEntry(prop.getPropertyType().getSimpleName()) != null) {
                if (!dr.hasDescriptorFor(prop.getPropertyType())) {
                    continue;
                }
                cd = dr.getDescriptorFor(prop.getPropertyType());

                attributes = ddSvc.getDataDictionary().getBusinessObjectEntry(prop.getPropertyType().getSimpleName()).getAttributes();
                for (AttributeDefinition attribute : attributes) {
                    String attributeName = attribute.getName();
                    if (attributeName.endsWith("versionNumber")) {
                        continue;
                    }
                    FieldDescriptor fd = cd.getFieldDescriptorByName(attributeName);

                    detailRow.setName(prop.getName());
                    // detailRow.setRequired( );
                    detailRow.setValidationRules(prop.getClass().getSimpleName());
                    // detailRow.setMaxLength((attribute.getMaxLength() == null)?"":attribute.getMaxLength().toString() );
                    detailRow.setFieldType(prop.getPropertyType().getSimpleName());
                    detailRow.setColumn("Property");
                    /*
                     * if (fd!=null){ detailRow.setFieldType(fd.getColumnType()); detailRow.setColumn(fd.getColumnName());
                     * detailRow.setColumnNo(fd.getColNo()); }
                     */
                    // System.out.println(prop.getDisplayName()+"/"+attributeName+" in prop/att "+detailRow.getName()+"  "+detailRow.getColumnNo());
                    rowList.add(detailRow);
                }
            }
        }
        // Collections.sort(rowList);
        // for (DataDictionaryDumperDetailForm.DataDictionaryDumperSection.DataDictionaryDumperDocumentRow tmpRow : rowList){
        // System.out.println(" in List "+tmpRow.getName()+"  "+tmpRow.getColumnNo());
        // }
        detailSection.setDocumentRows(rowList);
        sectionList.add(detailSection);

    }

    protected boolean hasExistenceCheck(Collection<ReferenceDefinition> existenceChecks, String attributeName) {

        for (ReferenceDefinition ref : existenceChecks) {
            if (attributeName.equals(ref.getAttributeToHighlightOnFail())) {
                return true;
            }
        }
        return false;
    }
}
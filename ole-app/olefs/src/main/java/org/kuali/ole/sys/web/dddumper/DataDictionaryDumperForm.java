/*
 * Copyright 2010 The Regents of the University of California.
 */
package org.kuali.ole.sys.web.dddumper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.kuali.rice.kns.datadictionary.BusinessObjectEntry;
import org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.GlobalBusinessObject;
import org.kuali.rice.krad.datadictionary.DataDictionaryEntry;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.service.KRADServiceLocator;

public class DataDictionaryDumperForm extends KualiForm {
	private static final long serialVersionUID = 3632283509506923869L;
    private Set<DataDictionaryEntry> dataDictionaryEntries = new HashSet<DataDictionaryEntry>();
    private List<BusinessObjectEntry> businessObjectEntries = new ArrayList<BusinessObjectEntry>();
    private List<DocumentEntry> documentEntries = new ArrayList<DocumentEntry>();
    private List<DocumentEntry> maintenanceDocumentEntries = new ArrayList<DocumentEntry>();
    private List<DocumentEntry> transactionalDocumentEntries = new ArrayList<DocumentEntry>();

    private static final Logger LOG = Logger.getLogger(DataDictionaryDumperForm.class);

    public Set<DataDictionaryEntry> getDataDictionaryEntries() {
		return dataDictionaryEntries;
	}
	public void setDataDictionaryEntries(Set<DataDictionaryEntry> dataDictionaryEntries) {
		this.dataDictionaryEntries = dataDictionaryEntries;
	}
	public List<DocumentEntry> getDocumentEntries() {
		return documentEntries;
	}
	public void setDocumentEntries(List<DocumentEntry> documentEntries) {
		for (DocumentEntry currentEntry : documentEntries){
//			try{
//				if (!KNSServiceLocator.getWorkflowInfoService().isCurrentActiveDocumentType(currentEntry.getDocumentTypeName())) continue; //Skip inactive.
//			}catch (WorkflowException we){
//				if(LOG.isInfoEnabled()){
//					LOG.info("Doc "+currentEntry.getDocumentTypeName()+" not listed due to exception. ", we);
//				}
//				continue; //Assume if this happens that we just want to skip this doc type.
//			}
			if (MaintenanceDocument.class.isAssignableFrom(currentEntry.getDocumentClass())){
				Class<? extends BusinessObject> boClass = ((MaintenanceDocumentEntry)currentEntry).getBusinessObjectClass();

				if( GlobalBusinessObject.class.isAssignableFrom(boClass) )
                 {
                    continue; //Skip globals.
                }
				if ( !KRADServiceLocator.getPersistenceStructureService().isPersistable( boClass ))
                 {
                    continue;  //Skip user and its ilk.
                }

				addMaintenanceDocumentEntry(currentEntry);
			}else{
				addTransactionalDocumentEntry(currentEntry);
			}
		}
		this.documentEntries = documentEntries;
	}
	public List<BusinessObjectEntry> getBusinessObjectEntries() {
		return businessObjectEntries;
	}
	public void setBusinessObjectEntries(
			List<BusinessObjectEntry> businessObjectEntries) {
		this.businessObjectEntries = businessObjectEntries;
	}
	public List<DocumentEntry> getMaintenanceDocumentEntries() {
		return maintenanceDocumentEntries;
	}
	public void addMaintenanceDocumentEntry(DocumentEntry maintenanceDocumentEntry) {
		this.maintenanceDocumentEntries.add(maintenanceDocumentEntry);
	}
	public List<DocumentEntry> getTransactionalDocumentEntries() {
		return transactionalDocumentEntries;
	}
	public void addTransactionalDocumentEntry(DocumentEntry transactionalDocumentEntry) {
		this.transactionalDocumentEntries.add(transactionalDocumentEntry);
	}

}

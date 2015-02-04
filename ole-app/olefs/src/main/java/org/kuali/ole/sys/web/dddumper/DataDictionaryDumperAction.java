/*
 * Copyright 2010 The Regents of the University of California.
 */
package org.kuali.ole.sys.web.dddumper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kns.datadictionary.BusinessObjectEntry;
import org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.kns.web.struts.action.KualiAction;
import org.kuali.rice.krad.datadictionary.DocumentEntry;

public class DataDictionaryDumperAction extends KualiAction {
    private static final Logger LOG = Logger.getLogger(DataDictionaryDumperAction.class);

    protected static DataDictionaryService dataDictionaryService;
    protected static List<DocumentEntry> documentEntries;
    protected static List<BusinessObjectEntry> businessObjectEntries;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		DataDictionaryDumperForm dumperForm = (DataDictionaryDumperForm)form;
		dumperForm.setDocumentEntries(getDocumentEntries());
		dumperForm.setBusinessObjectEntries(getBusinessObjectEntries());
        return mapping.findForward("basic");
	}

    public DataDictionaryService getDataDictionaryService() {
        if (dataDictionaryService == null) {
            dataDictionaryService = SpringContext.getBean(DataDictionaryService.class);
        }
        return dataDictionaryService;
    }

    public List<DocumentEntry> getDocumentEntries() {
        if ( documentEntries == null ) {
            List<DocumentEntry> temp =  new ArrayList<DocumentEntry>( new HashSet<DocumentEntry>( getDataDictionaryService().getDataDictionary().getDocumentEntries().values() ) );
            Collections.sort(temp, new Comparator<DocumentEntry>() {
                @Override
                public int compare(DocumentEntry o1, DocumentEntry o2) {
                    if ( o1 instanceof MaintenanceDocumentEntry && o2 instanceof MaintenanceDocumentEntry ) {
                        return ((MaintenanceDocumentEntry)o1).getBusinessObjectClass().getName().compareTo( ((MaintenanceDocumentEntry)o2).getBusinessObjectClass().getName() );
                    } else if ( o1 instanceof MaintenanceDocumentEntry && !(o2 instanceof MaintenanceDocumentEntry) ) {
                        return -1;
                    } else if ( !(o1 instanceof MaintenanceDocumentEntry) && o2 instanceof MaintenanceDocumentEntry ) {
                        return 1;
                    }
                    return o1.getDocumentClass().getName().compareTo(o2.getDocumentClass().getName());
                }
            });
            documentEntries = temp;
        }
        return documentEntries;
    }

    public List<BusinessObjectEntry> getBusinessObjectEntries() {
        List<BusinessObjectEntry> temp =  new ArrayList<BusinessObjectEntry>( new HashSet( getDataDictionaryService().getDataDictionary().getBusinessObjectEntries().values() ) );
        Collections.sort(temp, new Comparator<BusinessObjectEntry>() {
            @Override
            public int compare(BusinessObjectEntry o1, BusinessObjectEntry o2) {
                return o1.getObjectLabel().compareTo(o2.getObjectLabel());
            }
        });
        businessObjectEntries = temp;
        return businessObjectEntries;
    }

}

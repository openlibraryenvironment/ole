package org.kuali.ole.batch.impl;

import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.batch.document.OLEBatchProcessDefinitionDocument;

public interface OLEBatchProcess {
    String EXT_MARCXML = ".xml";
    String EXT_MARC = ".mrc";
    String XML_DEC = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String SOLR_DT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    String JOB_DT_FORMAT = "MM-dd-yyyy HH:mm:ss";
    String FILTER_DT_FORMAT = "MM/dd/yyyy";
    String PERCENT = "%";
    String MARC = "marc";
    String MARCXML = "marcxml";
    String EXPORT_FULL = "full";
    String EXPORT_INC = "incremental";
    String INCREMENTAL_EXPORT_EX_STAFF = "incremental_ex_staff";
    String EXPORT_FILTER = "filter";
    String EXPORT_EX_STAFF = "ex_staff";
    String EXT_ERR_TXT = "_ERROR.txt";
    String EXPORT_BIB_ONLY = "BIBONLY";
    String EXPORT_BIB_AND_INSTANCE = "BIBANDINSTANCE";
    String EXPORT_BIB_INSTANCE_AND_EINSTANCE = "BIBINSTANCEEINSTANCE";

    public void process(OLEBatchProcessDefinitionDocument oleBatchProcessDefinitionDocument, OLEBatchProcessJobDetailsBo jobBo) throws Exception;
}

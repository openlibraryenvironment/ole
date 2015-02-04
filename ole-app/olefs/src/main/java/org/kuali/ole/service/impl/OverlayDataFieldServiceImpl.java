package org.kuali.ole.service.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.service.OverlayDataFieldService;
import org.kuali.ole.util.StringUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 2/3/13
 * Time: 6:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayDataFieldServiceImpl implements OverlayDataFieldService{

    private static final Logger LOG = Logger.getLogger(OverlayDataFieldServiceImpl.class);

    public LinkedHashMap<String,DataField> getDataFieldValueMap(List<DataField> dataFieldList)throws Exception{
        LinkedHashMap<String,DataField> dataFieldValueMap = new LinkedHashMap<String,DataField>();
        if(dataFieldList != null){
            for(DataField dataField : dataFieldList){
                dataFieldValueMap.put(getTagKey(dataField),dataField);
            }
        }
        return dataFieldValueMap;
    }

    public LinkedHashMap<String,SubField> getSubFieldValueMap(List<DataField> dataFieldList)throws Exception{
        LinkedHashMap<String,SubField> subFieldValueMap = new LinkedHashMap<String,SubField>();
        String ind1 = null;
        String ind2 = null;
        StringBuffer key = new StringBuffer();
        if(dataFieldList != null){
            for(DataField dataField : dataFieldList){
                List<SubField> subFieldList = dataField.getSubFields();
                for(SubField subField : subFieldList){
                    key.append(getTagKey(dataField));
                    key.append(OLEConstants.DELIMITER_DOLLAR).append(subField.getCode());
                    subFieldValueMap.put(key.toString(),subField);
                    if(LOG.isDebugEnabled()){
                        LOG.debug("field key---------->"+key.toString());
                        LOG.debug("value-------------->"+subField.getValue());
                    }
                    key = new StringBuffer();
                }
            }
        }
        return subFieldValueMap;
    }

    public HashMap<String,HashMap<SubField,DataField>> getDataFieldValueSubFieldMap(List<DataField> dataFieldList){
        HashMap<String,HashMap<SubField,DataField>> dataFieldSubFieldValueMap = new HashMap<String,HashMap<SubField,DataField>>();
        String tagKey = null;
        if(dataFieldList != null){
            for(DataField dataField : dataFieldList){
                tagKey =getTagKey(dataField);
                for(SubField subField : dataField.getSubFields()){
                    tagKey = tagKey+StringUtil.trimEmptyNullValues(subField.getCode());
                    HashMap dataFieldSubFieldMap = new HashMap<SubField,DataField>();
                    dataFieldSubFieldMap.put(subField,dataField);
                    dataFieldSubFieldValueMap.put(tagKey,dataFieldSubFieldMap);
                }
            }
        }
        return dataFieldSubFieldValueMap;
    }

    public String getTagKey(DataField dataField){
        StringBuffer keySB = new StringBuffer();
        String ind1 = dataField.getInd1()!=null? StringUtil.trimHashNullValues(dataField.getInd1()):OLEConstants.DELIMITER_HASH;
        String ind2 = dataField.getInd2()!=null? StringUtil.trimHashNullValues(dataField.getInd2()):OLEConstants.DELIMITER_HASH;
        keySB.append(dataField.getTag()).append(ind1).append(ind2);
        return keySB.toString();
    }
}

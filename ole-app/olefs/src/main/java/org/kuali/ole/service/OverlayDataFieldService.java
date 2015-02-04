package org.kuali.ole.service;

import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 2/3/13
 * Time: 6:14 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OverlayDataFieldService {

    //public HashMap<String,DataField> getDataFieldValueMap(List<DataField> dataFieldList)throws Exception;
    //public TreeMap<String,DataField> getDataFieldValueMap(List<DataField> dataFieldList)throws Exception;
    public LinkedHashMap<String,DataField> getDataFieldValueMap(List<DataField> dataFieldList)throws Exception;

    //public HashMap<String,SubField> getSubFieldValueMap(List<DataField> dataFieldList)throws Exception;
    //public TreeMap<String,SubField> getSubFieldValueMap(List<DataField> dataFieldList)throws Exception;
    public LinkedHashMap<String,SubField> getSubFieldValueMap(List<DataField> dataFieldList)throws Exception;

    public HashMap<String,HashMap<SubField,DataField>> getDataFieldValueSubFieldMap(List<DataField> dataFieldList)throws Exception;

    public String getTagKey(DataField dataField)throws Exception;

}

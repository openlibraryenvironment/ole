package org.kuali.ole.service;


import org.kuali.ole.bo.serachRetrieve.OleSRUBibDocument;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 7:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleSRUDataService {


    public List getBibRecordsIdList(Map reqParamMap, String solrQuery);

    public String getOPACXMLSearchRetrieveResponse(List oleBibIDList, Map reqParamMap);

}

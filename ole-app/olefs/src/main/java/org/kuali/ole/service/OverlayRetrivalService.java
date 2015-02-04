package org.kuali.ole.service;


import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.ingest.pojo.OverlayOption;
import org.kuali.ole.select.bo.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/10/12
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */

public interface OverlayRetrivalService {

    public List<OleGloballyProtectedField> getGloballyProtectedFields() throws Exception;

    public List getGloballyProtectedFieldsList()throws Exception;

    public List getGloballyProtectedFieldsModificationList()throws Exception;

    public OverlayOption getAddOverlayOption(List<OverlayOption> overlayOptionList)throws Exception;

    public OverlayOption getAddOverlayOptionWithWildCardSearch(List<DataField> newDatafields, List<OverlayOption> overlayOptionList)throws Exception;

    public OverlayOption getDeleteOverlayOption(List<OverlayOption> overlayOptionList)throws Exception;

    public OverlayOption getDeleteOverlayOptionWithWildCardSearch(List<DataField> newDatafields, List<OverlayOption> overlayOptionList);

    public OverlayOption getUpdateOverlayOption(List<OverlayOption> overlayOptionList)throws Exception;

    public OverlayOption getUpdateOverlayOptionWithWildCardSearch(List<DataField> newDatafields, List<OverlayOption> overlayOptionList)throws Exception;
    
    public OleCallNumber getCallNumberRecord(String inputValue)throws Exception;

    public OleCallNumber getCallNumberRecord(HashMap<String, String> criteriaMap)throws Exception;

    public OleCode getOleCodeRecord(String inputValue)throws Exception;

    public OleCode getOleCodeRecord(HashMap<String, String> criteriaMap)throws Exception;

    public OleBudgetCode getOleBudgetCode(String inputValue)throws Exception;

    public OleBudgetCode getOleBudgetCode(HashMap<String, String> criteriaMap)throws Exception;

    public OleVendorAccountInfo getAccountObjectForVendorRefNo(HashMap<String, String> criteriaMap)throws Exception;

}

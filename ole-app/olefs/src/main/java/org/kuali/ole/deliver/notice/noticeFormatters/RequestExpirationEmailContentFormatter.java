package org.kuali.ole.deliver.notice.noticeFormatters;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 6/23/15.
 */
public class RequestExpirationEmailContentFormatter extends RequestEmailContentFormatter{


    @Override
    public String getCustomItemHeaderInfo(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap) {
        return null;
    }

    @Override
    public String getCustomItemFooterInfo(OleDeliverRequestBo oleDeliverRequestBo,Map<String,String> fieldLabelMap) {
        return null;
    }
}

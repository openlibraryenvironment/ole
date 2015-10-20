package org.kuali.ole.template;

import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.service.NoticeMailContentFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 10/20/15.
 */
public class EresourceAlertContentFormatter extends NoticeMailContentFormatter {

    @Override
    protected void processCustomNoticeInfo(OleLoanDocument oleLoanDocument, OleNoticeBo oleNoticeBo) {

    }

    protected String getBaseFTLTemplate() {
        return "eResourceNotification.ftl";
    }

    protected List<String> getFTLList(){
        List<String> fileNames = new ArrayList<>();
        fileNames.add("eResourceNotification.ftl");
        return fileNames;
    }

}

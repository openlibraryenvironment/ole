package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.marc4j.marc.Record;

import java.util.List;

/**
 * Created by pvsubrah on 1/4/16.
 */
public abstract class BibHandler extends Handler {

    public String process001And003(String marcContent, String bibId) {
        List<Record> records = getMarcRecordUtil().convertMarcXmlContentToMarcRecord(marcContent);
        if(CollectionUtils.isNotEmpty(records)) {
            Record record = records.get(0);
            replaceBibIdTo001Tag(record,bibId);
            replaceOrganizationCodeTo003Tag(record);
            return getMarcRecordUtil().convertMarcRecordToMarcContent(record);
        }
        return marcContent;
    }

    public void replaceBibIdTo001Tag(Record record,String bibId) {
        getMarcRecordUtil().updateControlFieldValue(record,"001",bibId);
    }

    public void replaceOrganizationCodeTo003Tag(Record record) {
        String controlField003Value = getMarcRecordUtil().getControlFieldValue(record, "003");
        String organizationCode = ConfigContext.getCurrentContextConfig().getProperty("organization.marc.code");
        if(StringUtils.isBlank(controlField003Value)) {
            getMarcRecordUtil().updateControlFieldValue(record,"003",organizationCode);
        }
    }
}

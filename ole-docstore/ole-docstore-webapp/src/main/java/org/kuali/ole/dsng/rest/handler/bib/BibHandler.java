package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.marc4j.marc.Record;

import java.util.List;

/**
 * Created by pvsubrah on 1/4/16.
 */
public abstract class BibHandler extends Handler {
    public String replaceBibIdTo001Tag(String marcContent,String bibId) {
        List<Record> records = getMarcRecordUtil().convertMarcXmlContentToMarcRecord(marcContent);
        if(CollectionUtils.isNotEmpty(records)) {
            Record record = records.get(0);
            getMarcRecordUtil().updateControlFieldValue(record,"001",bibId);
            return getMarcRecordUtil().convertMarcRecordToMarcContent(record);
        }
        return null;
    }
}

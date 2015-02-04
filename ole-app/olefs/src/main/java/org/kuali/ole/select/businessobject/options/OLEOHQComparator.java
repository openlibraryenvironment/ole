package org.kuali.ole.select.businessobject.options;

import org.apache.commons.lang3.StringUtils;
import org.displaytag.model.DefaultComparator;
import org.kuali.ole.BibliographicRecordHandler;
import org.kuali.ole.describe.service.DocstoreHelperService;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: syedk
 * Date: 10/4/13
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEOHQComparator implements Comparator {


    private DefaultComparator comparator = new DefaultComparator();
    private DocstoreClientLocator docstoreClientLocator;
    private BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }


    @Override
    public int compare(Object obj1, Object obj2) {
        obj1 = compareUsingIndicator2(obj1);
        obj2 = compareUsingIndicator2(obj2);
        return comparator.compare(obj1,obj2);
    }

    public Object compareUsingIndicator2(Object obj) {
        if(obj.toString().contains(OLEConstants.TITLE_DISPLAY)){
            obj = obj.toString().replaceAll(OLEConstants.TITLE_DISPLAY, OLEConstants.TITLE_SORT);
            if(obj.toString().contains(OLEConstants.UUID_FOR_TITLE_SORT + "=")) {
                String uuid = obj.toString().split(OLEConstants.UUID_FOR_TITLE_SORT + "=")[1].split("['\"']")[0];
               /* DocstoreHelperService docstoreHelperService = new DocstoreHelperService();
                String docstoreData = null;*/
                try {
                  /*  docstoreData = docstoreHelperService.getDocstoreData(uuid);*/
                    Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(uuid);
                    if(bib.getContent() != null) {
                        //BibMarcRecord bibMarcRecord = new BibMarcRecordProcessor().fromXML(docstoreData).getRecords().get(0);
                        BibMarcRecord bibMarcRecord = bibMarcRecordProcessor.fromXML(bib.getContent()).getRecords().get(0);
                        String indicatorValueAsString = bibMarcRecord.getDataFields().get(0).getInd2();
                        String oldTitle = obj.toString().split("Title_sort=")[1].split("&")[0];
                        String newTitle = bibMarcRecord.getDataFields().get(0).getSubFields().get(0).getValue();
                        // This condition executed for Title added through other than Order Import (Batch process).
                        if(!StringUtils.isNumeric(newTitle)) {
                            obj = obj.toString().replace(oldTitle,newTitle);
                        }
                        if(!indicatorValueAsString.equalsIgnoreCase(" ")){
                            Integer indicatorValue = Integer.valueOf(indicatorValueAsString);
                            obj = obj.toString().split(OLEConstants.TITLE_SORT)[0] +OLEConstants.TITLE_SORT + "=" + obj.toString().split(OLEConstants.TITLE_SORT)[1].substring(indicatorValue+1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }


}

package org.kuali.ole.batch.keyvalue;

import org.kuali.ole.gl.web.util.OriginEntryFileComparator;
import org.kuali.ole.select.document.service.OLEEncumberOpenRecurringOrdersService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.io.File;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 1/27/15
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEncumberOpenRecurringOrdersValueFinder extends KeyValuesBase {

    public List getKeyValues() {
        List activeLabels = new ArrayList();
        OLEEncumberOpenRecurringOrdersService encumberOpenRecurringOrdersService = SpringContext.getBean(OLEEncumberOpenRecurringOrdersService.class) ;
        File[] fileList = encumberOpenRecurringOrdersService.getAllFileInBatchDirectory();
        List<File> sortedFileList = Arrays.asList(fileList);
        Collections.sort(sortedFileList, new OriginEntryFileComparator());

        for (File file : sortedFileList) {
            String fileName = file.getName();
            // build display file name with date and size
            Date date = new Date(file.lastModified());
            String timeInfo = "(" + SpringContext.getBean(DateTimeService.class).toDateTimeString(date) + ")";
            String sizeInfo = "(" + (new Long(file.length())).toString() + ")";
            activeLabels.add(new ConcreteKeyValue(fileName, timeInfo + " " + fileName + " " + sizeInfo));
        }

        return activeLabels;
    }
}

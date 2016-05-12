package org.kuali.ole.oleng.util;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.impl.datetime.DateTimeServiceImpl;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by SheikS on 3/1/2016.
 */
public class BatchDateTimeUtil extends DateTimeServiceImpl {

    @Override
    public Date convertToDate(String dateString) throws ParseException {
        String[] strings = new String[3];
        strings[0] = "yyyyMMdd";
        strings[1] = "yyyyMM";
        strings[2] = "dd.MMM.yyyy";
        return parseAgainstFormatArray(dateString, strings);
    }
}

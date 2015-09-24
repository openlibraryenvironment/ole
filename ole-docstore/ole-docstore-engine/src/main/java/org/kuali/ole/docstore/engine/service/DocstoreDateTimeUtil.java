package org.kuali.ole.docstore.engine.service;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.impl.datetime.DateTimeServiceImpl;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 9/14/15.
 */
public class DocstoreDateTimeUtil extends DateTimeServiceImpl {

    @Override
    public Date convertToDate(String dateString) throws ParseException {
        String[]strings = new String[6];
        strings[0] = "MM/dd/yyyy hh:mm:ssa";
        strings[1] = "MM/dd/yyyy hh:mm:ss a";
        strings[2] = "MM/dd/yyyy hh:mm:ss";
        strings[3] = "MM/dd/yyyy HH:mm:ss";
        strings[4] = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString("info.DateFormat")+"hh:mm:ss";
        strings[5] = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString("info.DateFormat")+"HH:mm:ss";
        return parseAgainstFormatArray(dateString, strings);
    }
}

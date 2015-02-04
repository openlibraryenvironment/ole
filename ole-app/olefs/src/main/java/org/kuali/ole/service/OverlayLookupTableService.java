package org.kuali.ole.service;

import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 2/2/13
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OverlayLookupTableService {

    public String getFieldValueFromSubField(String incomingField, LinkedHashMap<String, SubField> subFieldMap)throws Exception;

}

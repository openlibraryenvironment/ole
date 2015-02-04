package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleBibliographicRecordStatus;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/27/12
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibliographicRecordStatus
        extends KeyValuesBase {
    private boolean blankOption;

    public boolean isBlankOption() {
        return blankOption;
    }

    public void setBlankOption(boolean blankOption) {
        this.blankOption = blankOption;
    }

    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleBibliographicRecordStatus> oleBibliographicRecordStatuses = KRADServiceLocator
                .getBusinessObjectService().findAll(OleBibliographicRecordStatus.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleBibliographicRecordStatus type : oleBibliographicRecordStatuses) {
            if (type.isActive()) {
                options.add(new ConcreteKeyValue(type.getBibliographicRecordStatusCode(),
                        type.getBibliographicRecordStatusName()));
            }
        }
        return options;
    }


}

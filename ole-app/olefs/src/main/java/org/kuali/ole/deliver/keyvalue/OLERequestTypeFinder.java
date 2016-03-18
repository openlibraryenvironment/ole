package org.kuali.ole.deliver.keyvalue;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.document.OleMaintenanceDocumentBase;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleDeliverRequestType;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 7/15/13
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERequestTypeFinder extends UifKeyValuesFinderBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> requestKeyLabels = new ArrayList<KeyValue>();
        Collection<OleDeliverRequestType> oleDeliverRequestTypeCollection = KRADServiceLocator.getBusinessObjectService().findAll(OleDeliverRequestType.class);
        if(oleDeliverRequestTypeCollection.size()>0){
            for(OleDeliverRequestType oleDeliverRequestType : oleDeliverRequestTypeCollection){
               requestKeyLabels.add(new ConcreteKeyValue(oleDeliverRequestType.getRequestTypeCode(),oleDeliverRequestType.getRequestTypeCode()));
            }
        }
        return requestKeyLabels;
    }

}


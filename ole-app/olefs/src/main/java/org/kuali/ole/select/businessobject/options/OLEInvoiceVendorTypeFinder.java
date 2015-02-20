package org.kuali.ole.select.businessobject.options;

import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KeyValuesService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dileepp
 * Date: 7/2/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEInvoiceVendorTypeFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {

        List<VendorDetail> codes = (List<VendorDetail>) SpringContext.getBean(KeyValuesService.class).findAll(
                VendorDetail.class);
        // copy the list of codes before sorting, since we can't modify the results from this method
        if (codes == null) {
            codes = new ArrayList<VendorDetail>(0);
        } else {
            codes = new ArrayList<VendorDetail>(codes);
        }
        // sort using comparator.
        Collections.sort(codes, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                VendorDetail sampleVendor1 = (VendorDetail) o1;
                VendorDetail sampleVendor2 = (VendorDetail) o2;
                return sampleVendor1.getVendorName().compareTo(sampleVendor2.getVendorName());
            }
        });

        List<KeyValue> labels = new ArrayList<KeyValue>();
        labels.add(new ConcreteKeyValue("", ""));

        for (VendorDetail acctType : codes) {

           if(acctType.isActiveIndicator()){
              labels.add(new ConcreteKeyValue(acctType.getVendorHeaderGeneratedIdentifier().toString() + "-" + acctType.getVendorDetailAssignedIdentifier().toString(), acctType.getVendorName()));
           }
        }

        return labels;
    }

}

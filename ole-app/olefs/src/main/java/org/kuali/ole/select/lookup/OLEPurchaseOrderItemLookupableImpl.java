package org.kuali.ole.select.lookup;

import org.kuali.rice.kns.lookup.KualiLookupableImpl;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 2/26/14
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPurchaseOrderItemLookupableImpl  extends KualiLookupableImpl{

    /**
     * calls the lookup helper service to do "clear" behaviors
     *
     */
    @Override
    public void performClear(LookupForm lookupForm) {
        for (Iterator iter = this.getRows().iterator(); iter.hasNext();) {
            Row row = (Row) iter.next();
            for (Iterator iterator = row.getFields().iterator(); iterator.hasNext();) {
                Field field = (Field) iterator.next();
                if (!field.getPropertyName().equalsIgnoreCase("vendorHeaderGeneratedIdentifier") &&
                        !field.getPropertyName().equalsIgnoreCase("vendorDetailAssignedIdentifier")) {
                    if (field.isSecure()) {
                        field.setSecure(false);
                        field.setDisplayMaskValue(null);
                        field.setEncryptedValue(null);
                    }

                    if (!field.getFieldType().equals(Field.RADIO)) {
                        field.setPropertyValue(field.getDefaultValue());
                        if (field.getFieldType().equals(Field.MULTISELECT)) {
                            field.setPropertyValues(null);
                        }
                    }
                }
            }
        }
    }
}

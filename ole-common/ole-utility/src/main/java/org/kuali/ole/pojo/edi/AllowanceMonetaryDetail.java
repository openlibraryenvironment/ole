package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 9/20/13
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class AllowanceMonetaryDetail {

    private List<AllowanceMonetaryLineItemInformation> allowanceMonetaryLineItemInformation = new ArrayList<>();


    public void addAllowanceMonetaryLineItemInformation(AllowanceMonetaryLineItemInformation allowanceMonetaryLineItemInformation) {
        if (!this.allowanceMonetaryLineItemInformation.contains(allowanceMonetaryLineItemInformation)) {
            this.allowanceMonetaryLineItemInformation.add(allowanceMonetaryLineItemInformation);
        }
    }

    public List<AllowanceMonetaryLineItemInformation> getAllowanceMonetaryLineItemInformation() {
        return allowanceMonetaryLineItemInformation;
    }

    public void setAllowanceMonetaryLineItemInformation(List<AllowanceMonetaryLineItemInformation> allowanceMonetaryLineItemInformation) {
        this.allowanceMonetaryLineItemInformation = allowanceMonetaryLineItemInformation;
    }
}

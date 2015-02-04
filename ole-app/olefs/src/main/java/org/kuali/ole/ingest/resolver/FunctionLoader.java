package org.kuali.ole.ingest.resolver; /**
 * Copyright 2005-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.function.*;
import org.kuali.rice.krms.api.repository.function.FunctionDefinition;
import org.kuali.rice.krms.framework.engine.Function;
import org.kuali.rice.krms.framework.type.FunctionTypeService;

public class FunctionLoader implements FunctionTypeService {

    private ISBNFunction isbnFunction;
    private OleCurrentDateComparison oleCurrentDateComparison;
    private CheckDigitRoutine checkDigitRoutine;
    private OleContainsComparison containsComparison;
    private ISSNFunction issnFunction;
    private OCLCFunction oclcFunction;
    private LocationFunction locationFunction;
    private ItemBarcodeFunction itemBarcodeFunction;
    private VendorLineItemReferenceFunction vendorLineItemReferenceFunction;
    private static final Logger LOG = Logger.getLogger(FunctionLoader.class);

    /**
     *  Returns the instance of ISBNFunction or PatronMembershipExpiration based on function definition
     * @param functionDefinition
     * @return  Function(isbnFunction/patronMembershipExpiration).
     */
    @Override
    public Function loadFunction(FunctionDefinition functionDefinition) {
        if (functionDefinition.getName().equals(OLEConstants.ISBN_FUNCTION_DEF_NAME)) {
            return getISBNFunction();
        } else if(functionDefinition.getName().equals(OLEConstants.ISSN_FUNCTION_DEF_NAME)) {
            return getISSNFunction();
        } else if(functionDefinition.getName().equals(OLEConstants.OCLC_FUNCTION_DEF_NAME)) {
            return getOCLCFunction();
        } else if (functionDefinition.getName().equals(OLEConstants.OLE_CURRENT_DATE_FUNCTION)) {
            return getOleCurrentDateComparison();
        } else if(functionDefinition.getName().equals(OLEConstants.CHECK_DIGIT_ROUTINE))  {
            return getCheckDigitRoutine();
        } else if(functionDefinition.getName().equals(OLEConstants.LOCATION_FUNCTION_DEF_NAME)){
            return getLocationFunction();
        } else if(functionDefinition.getName().equals(OLEConstants.ITEM_BARCODE_FUNCTION_DEF_NAME)) {
            return getItemBarcodeFunction();
        }
        else if(functionDefinition.getName().equals(OLEConstants.VENDOR_LINEITEM_REF_NUM_FUNCTION_DEF_NAME)){
            return getVendorLineItemReferenceFunction();
        }
        else if(functionDefinition.getName().equals(OLEConstants.OLE_CONTAINS_FUNCTION)){
            return getContainsComparison();
        } else if(functionDefinition.getName().equals(OLEConstants.OLE_CIRC_POLICY_FOUND_FUNCTION))  {
            return new CirculationPolicyFoundFunction();
        }
        else if(functionDefinition.getName().equals(OLEConstants.OLE_RENEWAL_DATE_FUNCTION))  {
            return new OleRenewalDateComparison();
        }
        throw new IllegalArgumentException("Failed to load function for the given definition: " + functionDefinition.getName());
    }

    /**
     * Returns the VendorLineItemReferenceFunction.
     * if VendorLineItemReferenceFunction is null return new instance,otherwise return existing instance of vendorLineItemReferenceFunction.
     * @return  vendorLineItemReferenceFunction
     */
    public VendorLineItemReferenceFunction getVendorLineItemReferenceFunction() {
        if (null == vendorLineItemReferenceFunction) {
            vendorLineItemReferenceFunction = new VendorLineItemReferenceFunction();
        }
        return vendorLineItemReferenceFunction;
    }

    /**
     * Returns the LocationFunction.
     * if LocationFunction is null return new instance,otherwise return existing instance of locationFunction.
     * @return  locationFunction
     */
    public LocationFunction getLocationFunction() {
        if (null == locationFunction) {
            locationFunction = new LocationFunction();
        }
        return locationFunction;
    }

    /**
     * Returns the ItemBarcodeFunction.
     * if ItemBarcodeFunction is null return new instance,otherwise return existing instance of itemBarcodeFunction.
     * @return  itemBarcodeFunction
     */
    public ItemBarcodeFunction getItemBarcodeFunction() {
        if(null == itemBarcodeFunction){
            itemBarcodeFunction = new ItemBarcodeFunction();
        }
        return itemBarcodeFunction;
    }

    /**
     * Returns the ISBNFunction.
     * if ISBNFunction is null return new instance,otherwise return existing instance of isbnFunction.
     * @return  isbnFunction
     */
    private Function getISBNFunction() {
        if (null == isbnFunction) {
            isbnFunction = new ISBNFunction();
        }
        return isbnFunction;
    }
    /**
     * Returns the ISSNFunction.
     * if ISSNFunction is null return new instance,otherwise return existing instance of issnFunction.
     * @return  issnFunction
     */
    private Function getISSNFunction() {
        if (null == issnFunction) {
            issnFunction = new ISSNFunction();
        }
        return issnFunction;
    }

    /**
     * Returns the OCLCFunction.
     * if OCLCFunction is null return new instance,otherwise return existing instance of oclcFunction.
     * @return  oclcFunction
     */
    private Function getOCLCFunction() {
        if (null == oclcFunction) {
            oclcFunction = new OCLCFunction();
        }
        return oclcFunction;
    }

    /**
     * Returns the new instance of patronMembershipExpiration.
     * if patronMembershipExpiration is null returns new instance,otherwise returns existing patronMembershipExpiration.
     * @return  patronMembershipExpiration
     */
    private Function getOleCurrentDateComparison(){
        if(null == oleCurrentDateComparison){
            oleCurrentDateComparison = new OleCurrentDateComparison();
        }
        return oleCurrentDateComparison;
    }
    /**
     * Returns the new instance of containsComparison.
     * if containsComparison is null returns new instance,otherwise returns existing containsComparison.
     * @return  containsComparison
     */
    public OleContainsComparison getContainsComparison() {
        if(null == containsComparison){
            containsComparison = new OleContainsComparison();
        }
        return containsComparison;
    }

    /**
     * Returns the new instance of checkDigitRoutine.
     * if checkDigitRoutine is null returns new instance,otherwise returns existing checkDigitRoutine.
     * @return  checkDigitRoutine
     */
    public CheckDigitRoutine getCheckDigitRoutine() {
        if(null == checkDigitRoutine){
            checkDigitRoutine = new CheckDigitRoutine();
        }
        return checkDigitRoutine;
    }
}

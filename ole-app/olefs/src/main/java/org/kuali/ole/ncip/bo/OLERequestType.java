package org.kuali.ole.ncip.bo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.extensiblecatalog.ncip.v2.service.RequestType;
import org.extensiblecatalog.ncip.v2.service.Scheme;
import org.extensiblecatalog.ncip.v2.service.SchemeValuePair;
import org.kuali.ole.deliver.bo.OleDeliverRequestType;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 3/5/14
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERequestType extends SchemeValuePair {

    private static final List<RequestType> VALUES_LIST = new ArrayList<RequestType>();

    // Request is for an estimate of the charge to provide the Item or service requested.
    public static final RequestType ESTIMATE = new RequestType(Scheme.REQUEST_TYPE, "Estimate");
    // Request is to reserve the Item for future use. If the Item is not currently available, the request is placed
    // in an ordered list or queue so that the request is satisfied when the Item becomes available. Alternatively
    // the request can specify a specific date/time when the Item is required.
    public static final RequestType HOLD = new RequestType(Scheme.REQUEST_TYPE, "Hold");
    // Request is for the loan of the Item for a specified period of time.
    public static final RequestType LOAN = new RequestType(Scheme.REQUEST_TYPE, "Loan");
    // Request is for the supply of the Item with no requirement that the Item be returned.
    public static final RequestType NON_RETURNABLE_COPY = new RequestType(Scheme.REQUEST_TYPE, "Non-returnable Copy");
    // Request is for the retrieval of the Item from a location that may not be accessible to a User.
    public static final RequestType RECALL_DELIVER = new RequestType(Scheme.REQUEST_TYPE, "Recall/Delivery Request");

    public static final RequestType RECALL_HOLD = new RequestType(Scheme.REQUEST_TYPE, "Recall/Hold Request");

    public static final RequestType HOLD_DELIVER = new RequestType(Scheme.REQUEST_TYPE, "Hold/Delivery Request");

    public static final RequestType HOLD_HOLD = new RequestType(Scheme.REQUEST_TYPE, "Hold/Hold Request");

    public static final RequestType PAGE_DELIVER = new RequestType(Scheme.REQUEST_TYPE, "Page/Delivery Request");

    public static final RequestType PAGE_HOLD = new RequestType(Scheme.REQUEST_TYPE, "Page/Hold Request");

    public static final RequestType COPY = new RequestType(Scheme.REQUEST_TYPE, "Copy Request");




    private static OLERequestType[] values;

    static {
        values = VALUES_LIST.toArray(new OLERequestType[VALUES_LIST.size()]);
    }

    private OLERequestType(String scheme, String value) {
        super(scheme, value);
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getValue() {
        return this.value;
    }

    public static OLERequestType[] values() {
        return values;
    }

    /**
     * Find the RequestType that matches the scheme & value strings supplied.
     *
     * @param scheme a String representing the Scheme URI.
     * @param value  a String representing the Value in the Scheme.
     * @return a RequestType that matches, or null if none is found to match.
     */
    public static OLERequestType find(String scheme, String value) {
        OLERequestType match = null;
        for (OLERequestType requestType : values()) {
            if (requestType.getScheme() != null && requestType.getScheme().compareToIgnoreCase(scheme) == 0
                    && requestType.getValue().compareToIgnoreCase(value) == 0) {
                match = requestType;
                break;
            }
        }
        return match;
    }

    /**
     * Generic toString() implementation.
     *
     * @return String
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }


    public static void loadAll() {

        // Do nothing - merely invoking this method forces the creation of the instances defined above.
    }
}

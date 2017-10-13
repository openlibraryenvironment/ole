/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.inquiry;

import org.kuali.rice.krad.uif.widget.Inquiry;

import java.util.Map;

/**
 * Provides the contract for implementing an inquiry within the
 * inquiry framework
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Inquirable {

    /**
     * Sets the class for the data object the inquirable should retrieve
     *
     * <p>
     * Must be set before invoking any other operations on the <code>Inquirable</code>,
     * including the retrieveDataObject method
     * </p>
     *
     * @param dataObjectClass the class of the dataObject that this inquirable should
     * retrieve
     */
    public void setDataObjectClass(Class<?> dataObjectClass);

    /**
     * Responsible for retrieving the data object from its data source
     * (database, service call, etc) based on the given map of field
     * name/value pairs
     *
     * <p>
     * Given map can contain more than fields (primary key or other) necessary
     * for retrieving the data object. Method will use the fields necessary
     * based on the metadata for the data object class configured on the inquirable
     * </p>
     *
     * @param fieldValues - a map of string field names and values
     * @return the data object or null if not found
     */
    public Object retrieveDataObject(Map<String, String> fieldValues);

    /**
     * Invoked by the <code>ViewHelperService</code> to build a link to the
     * inquiry
     *
     * <p>
     * Note this is used primarily for custom <code>Inquirable</code>
     * implementations to customize the inquiry class or parameters for an
     * inquiry. Instead of building the full inquiry link, implementations can
     * make a callback to
     * org.kuali.rice.krad.uif.widget.Inquiry.buildInquiryLink(Object, String,
     * Class<?>, Map<String, String>) given an inquiry class and parameters to
     * build the link field.
     * </p>
     *
     * @param dataObject - parent object for the inquiry property
     * @param propertyName - name of the property the inquiry is being built for
     * @param inquiry - instance of the inquiry widget being built for the property
     */
    public void buildInquirableLink(Object dataObject, String propertyName, Inquiry inquiry);
}

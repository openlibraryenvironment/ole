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
package org.kuali.rice.krad.uif.field;

import org.kuali.rice.krad.datadictionary.AttributeSecurity;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

/**
 * Data field security adds attribute security to the standard component security
 *
 * <p>
 * The {@link AttributeSecurity} can be configured for the field to indicate there is security at the data layer
 * (configured by component (class) and property). If the field is backed by a data dictionary
 * {@link org.kuali.rice.krad.datadictionary.AttributeDefinition} the attribute security can be configured there and
 * will be picked up and inserted into the field security
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "dataFieldSecurity-bean")
public class DataFieldSecurity extends FieldSecurity {
    private static final long serialVersionUID = 585138507596582667L;

    private AttributeSecurity attributeSecurity;

    public DataFieldSecurity() {
        super();
    }

    /**
     * Attribute security instance configured or picked up for the field
     *
     * @return AttributeSecurity instance
     */
    @BeanTagAttribute(name="attributeSecurity",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public AttributeSecurity getAttributeSecurity() {
        return attributeSecurity;
    }

    /**
     * Setter for the fields attribute security
     *
     * @param attributeSecurity
     */
    public void setAttributeSecurity(AttributeSecurity attributeSecurity) {
        this.attributeSecurity = attributeSecurity;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentSecurity#copy()
     */
    @Override
    protected <T> void copyProperties(T componentSecurity) {
        super.copyProperties(componentSecurity);
        DataFieldSecurity dataFieldCopy = (DataFieldSecurity) componentSecurity;

        if (this.attributeSecurity != null) {
            dataFieldCopy.setAttributeSecurity((AttributeSecurity)this.attributeSecurity.copy());
        }
    }
}

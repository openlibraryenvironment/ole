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
package org.kuali.rice.krad.datadictionary.mask;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

import java.io.Serializable;

/**
 * The displayMask element specifies the type of masking to
 * be used to hide the value from un-authorized users.
 * There are three types of masking.
 */
@BeanTag(name = "mask-bean")
public class Mask implements Serializable {
    private static final long serialVersionUID = 4035984416568235531L;

    protected MaskFormatter maskFormatter;
    protected Class<? extends MaskFormatter> maskFormatterClass;

    /**
     * Masks a data value with the configured maskFormatter;
     *
     * @param value of the object
     * @return string value of the masked object
     */
    public String maskValue(Object value) {
        if (maskFormatter == null) {
            if (maskFormatterClass != null) {
                try {
                    maskFormatter = maskFormatterClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Unable to create instance of mask formatter class: " + maskFormatterClass.getName());
                }
            } else {
                throw new RuntimeException("Mask formatter not set for secure field.");
            }
        }

        return maskFormatter.maskValue(value);
    }

    /**
     * Gets the maskFormatter attribute.
     *
     * @return Returns the maskFormatter.
     */
    @BeanTagAttribute(name = "maskFormater", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public MaskFormatter getMaskFormatter() {
        return maskFormatter;
    }

    /**
     * @param maskFormatter instance to be used for masking field values.
     */
    public void setMaskFormatter(MaskFormatter maskFormatter) {
        this.maskFormatter = maskFormatter;
    }

    /**
     * Gets the maskFormatterClass attribute.
     *
     * @return Returns the maskFormatterClass.
     */
    @BeanTagAttribute(name = "maskFormatterClass", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Class<? extends MaskFormatter> getMaskFormatterClass() {
        return maskFormatterClass;
    }

    /**
     * @param maskFormatterClass element is used when a custom masking
     * algorithm is desired.  This element specifies the name of a
     * class that will perform the masking for unauthorized users.
     */
    public void setMaskFormatterClass(Class<? extends MaskFormatter> maskFormatterClass) {
        this.maskFormatterClass = maskFormatterClass;
    }

}

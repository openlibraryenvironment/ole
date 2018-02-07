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
package org.kuali.rice.kim.api.common.attribute;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * This is the contract for a KimAttribute.  A KimAttribute
 * associates categorizes an attribute and describes how to
 * find the attribute for the purpose of the kns.
 */
public interface KimAttributeContract extends Versioned, GloballyUnique, Identifiable, Inactivatable {

    /**
     * A fully-qualified class name which contains the {@link #getAttributeName()}.    This cannot be null or a blank string.
     *
     * @return the name
     */
    String getComponentName();

    /**
     * The name of the attribute on the {@link #getComponentName()}.   This cannot be null or a blank string.
     *
     * @return the name
     */
    String getAttributeName();

    /**
     * The namespace code that this kim attribute belongs too.   This cannot be null or a blank string.
     *
     * @return namespace code
     */
    String getNamespaceCode();

    /**
     * The label for the kim attribute.  This is optional and can be null or blank.
     *
     * @return the label
     */
    String getAttributeLabel();
}

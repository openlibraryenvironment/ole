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
package org.kuali.rice.krad.bo;

import org.kuali.rice.core.api.mo.ModelObjectBasic;

/**
 * Interface for classes that act as a business object within the framework
 *
 * <p>
 * Business objects are special objects to the Rice framework that indicate an object has certain features
 * (like refresh). Most business objects are persistable, see @{link PersistableBusinessObject},
 * which means Rice can provide handle the CRUD operations performed on the object. In addition, metadata from the
 * ORM layer will be available on these objects that is consumed by the framework to enable features.
 * </p>
 *
 * <p>
 * Business objects are a special kind of data object within the system. A data object is just a general object that
 * provides data within the system and can be used to back the user interfaces. In general, how the system doesn't
 * know anything about how data objects are built (for example they could come from services or some other mechanism).
 * Make a data object implement BusinessObject causes the system to make assumptions regarding the handling of that
 * object.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface BusinessObject extends ModelObjectBasic {

    /**
     * Invoked to refresh business objects related to the parent based on their key field values
     *
     * <p>
     * During processing (for example accepting user input) the field values that participate in relationships can
     * become out of sync with the related business objects (for example: suppose our business object has a property
     * name bookId with a related object of type Book that contains the id property. If the user changes the value
     * for the bookId property, our id property on the related book and the associated information is still pointing
     * to the previous book id). This method is invoked to indicate the related objects should be refreshed based
     * on their related keys. For @{link PersistableBusinessObject} implementations, most refreshes can be handled
     * by the ORM tool
     * </p>
     */
    public abstract void refresh();
}

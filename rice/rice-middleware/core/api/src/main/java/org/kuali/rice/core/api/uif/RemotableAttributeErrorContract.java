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
package org.kuali.rice.core.api.uif;

import java.util.List;

/**
 * Defines a list of one or more errors for an attribute.
 */
public interface RemotableAttributeErrorContract {

    /**
     * The name of the attribute.  Will never be a blank or null string.
     * @return attribute name
     */
    String getAttributeName();

    /**
     * A list of fully resolved error messages associated with an attribute.  Will never return null or an empty list.
     * Note: since this interface is used to communicate errors remotely across modules, all error messages <i>must</i>
     * but fully resolved and concrete, not error message resource keys.
     *
     * @return list of errors.
     */
    List<String> getErrors();

    /**
     * Convenience method that returns a String containing all error messages
     * @return returns a String containing all error messages
     */
    String getMessage();
}

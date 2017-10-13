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
package org.kuali.rice.kim.api.group;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

import java.util.Map;
/**
 * This is the contract for a Group.  A group is a collection of principals.  It's membership consists of direct principal
 * assignment and/or nested group membership.  All groups are uniquely identified by a namespace
 * code plus a name.
 *
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public interface GroupContract extends Versioned, GloballyUnique, Inactivatable, Identifiable {

    /**
     * This is the namespace code for the Group.
     *
     * <p>
     * This is a namespace code assigned to a Group.  Together with name, it makes up another unique identifier for Group
     * </p>
     *
     * @return namespaceCode
     */
    String getNamespaceCode();

    /**
     * This is the name for the Group.
     *
     * <p>
     * This is a name assigned to a Group.  Together with NamespaceCode, it makes up another unique identifier for Group
     * </p>
     *
     * @return name
     */
	String getName();

    /**
     * This a description for the Group.
     *
     * <p>
     * This is a description assigned to a Group.
     * </p>
     *
     * @return description
     */
	String getDescription();

    /**
     * This a Kim Type Id for the Group.
     *
     * <p>
     * This links a Kim Type to the Group to allow custom types of Groups.
     * </p>
     *
     * @return description
     */
	String getKimTypeId();

    /**
     * This is a set of Attributes for a Group.
     *
     * <p>
     * This is a set of attributes which are key-label pairs that are defined by the Group's Kim Type.
     * </p>
     *
     * @return attributes
     */
	Map<String, String> getAttributes();

}

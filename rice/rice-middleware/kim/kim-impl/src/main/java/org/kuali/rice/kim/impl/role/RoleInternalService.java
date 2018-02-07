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
package org.kuali.rice.kim.impl.role;

public interface RoleInternalService {
    /**
	 * Notifies all of a principal's roles and role types that the principal has been inactivated.
	 */
	void principalInactivated(String principalId ) throws IllegalArgumentException;

	/**
	 * Notifies the role service that the role with the given id has been inactivated.
	 */
	void roleInactivated(String roleId) throws IllegalArgumentException;

	/**
	 * Notifies the role service that the group with the given id has been inactivated.
	 */
    void groupInactivated(String groupId) throws IllegalArgumentException;
}

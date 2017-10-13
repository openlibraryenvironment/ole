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

import org.kuali.rice.kim.impl.services.KimImplServiceLocator;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.List;
import java.util.Map;

/**
 * Custom lookupable for the {@link RoleBo} lookup to call the role DAO for searching
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RoleLookupableImpl extends LookupableImpl {
    private static final long serialVersionUID = -3149952849854425077L;

    @Override
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        List<RoleBo> roles = getRoleDao().getRoles(searchCriteria);

        return roles;
    }

    public RoleDao getRoleDao() {
        return KimImplServiceLocator.getRoleDao();
    }
}

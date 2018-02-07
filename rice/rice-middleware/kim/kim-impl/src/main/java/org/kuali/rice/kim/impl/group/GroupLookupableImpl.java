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
package org.kuali.rice.kim.impl.group;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateUtils;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupQueryResults;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.role.RoleDao;
import org.kuali.rice.kim.impl.services.KimImplServiceLocator;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.LookupForm;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.kuali.rice.core.api.criteria.PredicateFactory.*;
import static org.kuali.rice.core.api.criteria.PredicateFactory.and;
import static org.kuali.rice.core.api.criteria.PredicateFactory.lessThan;

/**
 * Custom lookupable for the {@link GroupBo} lookup to call the group service for searching
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class GroupLookupableImpl extends LookupableImpl {
    private static final long serialVersionUID = -3149952849854425077L;

    /**
     * Translates any search criteria on principal name to member id and active data, then calls group service to
     * retrieve matching groups
     *
     * @return List<GroupBo>
     */
    @Override
    protected List<?> getSearchResults(LookupForm form, Map<String, String> searchCriteria, boolean unbounded) {
        Map<String, String> criteriaMap = new HashMap<String, String>(searchCriteria);
        QueryByCriteria.Builder criteria = QueryByCriteria.Builder.create();

        if (!criteriaMap.isEmpty()) {
            List<Predicate> predicates = new ArrayList<Predicate>();
            //principalId doesn't exist on 'Group'.  Lets do this predicate conversion separately
            if (StringUtils.isNotBlank(criteriaMap.get(KimConstants.UniqueKeyConstants.PRINCIPAL_NAME))) {
                String principalId = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(
                        criteriaMap.get(KimConstants.UniqueKeyConstants.PRINCIPAL_NAME)).getPrincipalId();
                Timestamp currentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
                predicates.add(and(equal("members.memberId", principalId), equal("members.typeCode",
                        KimConstants.KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.getCode()), and(or(isNull(
                        "members.activeFromDateValue"), greaterThanOrEqual("members.activeFromDateValue", currentTime)),
                        or(isNull("members.activeToDateValue"), lessThan("members.activeToDateValue", currentTime)))));

            }
            criteriaMap.remove(KimConstants.UniqueKeyConstants.PRINCIPAL_NAME);

            predicates.add(PredicateUtils.convertMapToPredicate(criteriaMap));
            criteria.setPredicates(and(predicates.toArray(new Predicate[predicates.size()])));
        }

        GroupQueryResults groupResults = getGroupService().findGroups(criteria.build());
        List<Group> groups = groupResults.getResults();

        //have to convert back to Bos :(
        List<GroupBo> groupBos = new ArrayList<GroupBo>(groups.size());
        for (Group group : groups) {
            groupBos.add(GroupBo.from(group));
        }

        return groupBos;
    }

    public GroupService getGroupService() {
        return KimApiServiceLocator.getGroupService();
    }
}

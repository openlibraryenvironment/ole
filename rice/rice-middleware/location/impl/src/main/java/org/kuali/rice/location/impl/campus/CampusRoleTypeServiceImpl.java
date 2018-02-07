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
package org.kuali.rice.location.impl.campus;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.type.KimAttributeField;
import org.kuali.rice.kns.kim.role.RoleTypeServiceBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CampusRoleTypeServiceImpl extends RoleTypeServiceBase {

    @Override
    protected List<String> getRequiredAttributes() {
        final List<String> attrs = new ArrayList<String>(super.getRequiredAttributes());
        attrs.add(KimConstants.AttributeConstants.CAMPUS_CODE);
        return Collections.unmodifiableList(attrs);
    }

    @Override
    public List<String> getWorkflowRoutingAttributes(String routeLevel) {
        if (StringUtils.isBlank(routeLevel)) {
            throw new RiceIllegalArgumentException("routeLevel was blank or null");
        }

        final List<String> attrs = new ArrayList<String>(super.getWorkflowRoutingAttributes(routeLevel));
        attrs.add(KimConstants.AttributeConstants.CAMPUS_CODE);
        return Collections.unmodifiableList(attrs);
    }

	@Override
	public List<KimAttributeField> getAttributeDefinitions(String kimTypeId) {
		if (StringUtils.isBlank(kimTypeId)) {
            throw new RiceIllegalArgumentException("kimTypeId was null or blank");
        }

        List<KimAttributeField> map = new ArrayList<KimAttributeField>(super.getAttributeDefinitions(kimTypeId));

		for (int i = 0; i < map.size(); i++) {
            final KimAttributeField definition = map.get(i);
			if (KimConstants.AttributeConstants.CAMPUS_CODE.equals(definition.getAttributeField().getName())) {
				KimAttributeField.Builder b = KimAttributeField.Builder.create(definition);

                RemotableAttributeField.Builder fb =  b.getAttributeField();
                fb.setRequired(true);

                b.setAttributeField(fb);
                map.set(i, b.build());
			}
		}
		return Collections.unmodifiableList(map);
	}
}

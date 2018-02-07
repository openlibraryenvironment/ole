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
package org.kuali.rice.kim.impl.type;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeInfoService;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KimTypeInfoServiceImpl implements KimTypeInfoService {

    private BusinessObjectService businessObjectService;

    @Override
    public KimType getKimType(final String id) throws RiceIllegalArgumentException {
        incomingParamCheck(id, "id");

        return KimTypeBo.to(businessObjectService.findBySinglePrimaryKey(KimTypeBo.class, id));
    }

    @Override
    public KimType findKimTypeByNameAndNamespace(final String namespaceCode, final String name) throws RiceIllegalArgumentException {
        incomingParamCheck(namespaceCode, "namespaceCode");
        incomingParamCheck(name, "name");

        final Map<String, Object> crit = new HashMap<String, Object>();
        crit.put("namespaceCode", namespaceCode);
        crit.put("name", name);
        crit.put("active", "true");

        final Collection<KimTypeBo> bos = businessObjectService.findMatching(KimTypeBo.class, crit);

        if (bos != null && bos.size() > 1) {
            throw new IllegalStateException("multiple active results were found for the namespace code: " + namespaceCode + " and name: " + name);
        }

        return bos != null && bos.iterator().hasNext() ? KimTypeBo.to(bos.iterator().next()) : null;
    }

    @Override
    public Collection<KimType> findAllKimTypes() {
        final Collection<KimTypeBo> bos
                = businessObjectService.findMatching(KimTypeBo.class, Collections.singletonMap("active", "true"));
        final Collection<KimType> ims = new ArrayList<KimType>();

        if (bos != null) {
            for (KimTypeBo bo : bos) {
                if (bo != null) {
                    ims.add(KimTypeBo.to(bo));
                }
            }
        }
        return Collections.unmodifiableCollection(ims);
    }

    public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    private void incomingParamCheck(Object object, String name) {
        if (object == null) {
            throw new RiceIllegalArgumentException(name + " was null");
        } else if (object instanceof String
                && StringUtils.isBlank((String) object)) {
            throw new RiceIllegalArgumentException(name + " was blank");
        }
    }
}

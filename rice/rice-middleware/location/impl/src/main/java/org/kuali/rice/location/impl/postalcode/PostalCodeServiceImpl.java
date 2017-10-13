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
package org.kuali.rice.location.impl.postalcode;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.location.api.postalcode.PostalCode;
import org.kuali.rice.location.api.postalcode.PostalCodeQueryResults;
import org.kuali.rice.location.api.postalcode.PostalCodeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostalCodeServiceImpl implements PostalCodeService {

    private BusinessObjectService businessObjectService;
    private CriteriaLookupService criteriaLookupService;

    @Override
    public PostalCode getPostalCode(String countryCode, String code) {
        if (StringUtils.isBlank(countryCode)) {
            throw new RiceIllegalArgumentException(("countryCode is null"));
        }

        if (StringUtils.isBlank(code)) {
            throw new RiceIllegalArgumentException(("code is null"));
        }

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("countryCode", countryCode);
        map.put("code", code);

        return PostalCodeBo.to(businessObjectService.findByPrimaryKey(PostalCodeBo.class, Collections.unmodifiableMap(map)));
    }

    @Override
    public List<PostalCode> findAllPostalCodesInCountry(String countryCode) {
        if (StringUtils.isBlank(countryCode)) {
            throw new RiceIllegalArgumentException(("countryCode is null"));
        }

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("countryCode", countryCode);
        map.put("active", Boolean.TRUE);

        final Collection<PostalCodeBo> bos = businessObjectService.findMatching(PostalCodeBo.class, Collections.unmodifiableMap(map));
        if (bos == null) {
            return Collections.emptyList();
        }

        final List<PostalCode> toReturn = new ArrayList<PostalCode>();
        for (PostalCodeBo bo : bos) {
            if (bo != null && bo.isActive()) {
                toReturn.add(PostalCodeBo.to(bo));
            }
        }

        return Collections.unmodifiableList(toReturn);
    }

    @Override
    public PostalCodeQueryResults findPostalCodes(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        incomingParamCheck(queryByCriteria, "queryByCriteria");

        GenericQueryResults<PostalCodeBo> results = criteriaLookupService.lookup(PostalCodeBo.class, queryByCriteria);

        PostalCodeQueryResults.Builder builder = PostalCodeQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<PostalCode.Builder> ims = new ArrayList<PostalCode.Builder>();
        for (PostalCodeBo bo : results.getResults()) {
            ims.add(PostalCode.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
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

    /**
     * Sets the criteriaLookupService attribute value.
     *
     * @param criteriaLookupService The criteriaLookupService to set.
     */
    public void setCriteriaLookupService(final CriteriaLookupService criteriaLookupService) {
        this.criteriaLookupService = criteriaLookupService;
    }
}

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
package org.kuali.rice.kew.rule;

import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.kew.rule.bo.RuleBaseValuesLookupableImpl;
import org.kuali.rice.kns.web.ui.Row;

import java.util.List;
import java.util.Map;

/**
 * An interface which can be implemented by a {@link WorkflowRuleAttribute} implementation which allows
 * a different List of {@link Row} objects to be returned for rendering on the rule lookup screen.
 * 
 * @see RuleBaseValuesLookupableImpl
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface WorkflowRuleSearchAttribute extends WorkflowRuleAttribute{

    /**
     * If your attribute is an OddSearchAttribute this method will be used to get search rows instead of the
     * usually called get RuleRows.  Generally this is used for Attributes that want to expose drop downs
     * for Rule Entry but need special wild card fields for searches that wouldn't work for rule entry.
     */
    List<Row> getSearchRows();
    
    /** 
     * validate search data and populate attribute with search data
     * @param paramMap
     * @return
     */
    List<RemotableAttributeError> validateSearchData(Map<String, String> paramMap);
    
}

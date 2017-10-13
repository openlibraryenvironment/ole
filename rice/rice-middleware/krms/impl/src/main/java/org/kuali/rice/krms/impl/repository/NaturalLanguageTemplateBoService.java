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
package org.kuali.rice.krms.impl.repository;

import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import java.util.List;
import java.util.Map;

/**
 * This is the interface for accessing repository {@link NaturalLanguageTemplateBo} related business objects.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public interface NaturalLanguageTemplateBoService {


    /**
     * This will create a {@link NaturalLanguageTemplate} exactly like the
     * parameter passed in except the id will be assigned and create date/user 
     * will be set.
     * 
     * @param naturalLanguageTemplate  The NaturalLanguageTemplate to create.
     * @throws IllegalArgumentException if the NaturalLanguageTemplate is null.
     * @throws IllegalStateException if the NaturalLanguageTemplate already exists in the system.
     * @return a {@link NaturalLanguageTemplate} exactly like the parameter passed in.
     * 
     */
    public NaturalLanguageTemplate createNaturalLanguageTemplate(NaturalLanguageTemplate naturalLanguageTemplate);

    /**
     * Retrieves a NaturalLanguageTemplate from the repository based on the given id.
     * 
     * @param naturalLanguageTemplateId to retrieve.
     * @return a {@link NaturalLanguageTemplate} identified by the given id.  
     * A null reference is returned if an invalid or non-existent id is supplied.
     * 
     */
    public NaturalLanguageTemplate getNaturalLanguageTemplate(String naturalLanguageTemplateId);

    /**
     * This will update an existing {@link NaturalLanguageTemplate}.
     * 
     * @param naturalLanguageTemplate  The NaturalLanguageTemplate to update.
     * @throws IllegalArgumentException if the NaturalLanguageTemplate is null.
     * @throws IllegalStateException if the NaturalLanguageTemplate does not exists in the system.
     * 
     */
    public void updateNaturalLanguageTemplate(NaturalLanguageTemplate naturalLanguageTemplate);

    /**
     * Delete the {@link NaturalLanguageTemplate} with the given id.
     * 
     * @param naturalLanguageTemplateId to delete.
     * @throws IllegalArgumentException if the NaturalLanguageTemplate is null.
     * @throws IllegalStateException if the NaturalLanguageTemplate does not exists in the system
     * 
     */
    public void deleteNaturalLanguageTemplate(String naturalLanguageTemplateId);

    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByAttributes(Map attributes);

    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByLanguageCode(String languageCode);

    public NaturalLanguageTemplate findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId(String languageCode, String typeId, String naturalLanguageUsageId);

    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByNaturalLanguageUsage(String naturalLanguageUsageId);

    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByType(String typeId);

    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByTemplate(String template);

    /**
     * Converts a mutable {@link NaturalLanguageTemplateBo} to its immutable counterpart, {@link NaturalLanguageTemplate}.
     * @param naturalLanguageTemplateBo the mutable business object.
     * @return a {@link NaturalLanguageTemplate} the immutable object.
     * 
     */
    public NaturalLanguageTemplate to(NaturalLanguageTemplateBo naturalLanguageTemplateBo);

    /**
     * Converts a immutable {@link NaturalLanguageTemplate} to its mutable {@link NaturalLanguageTemplateBo} counterpart.
     * @param naturalLanguageTemplate the immutable object.
     * @return a {@link NaturalLanguageTemplateBo} the mutable NaturalLanguageTemplateBo.
     * 
     */
    public NaturalLanguageTemplateBo from(NaturalLanguageTemplate naturalLanguageTemplate);

}

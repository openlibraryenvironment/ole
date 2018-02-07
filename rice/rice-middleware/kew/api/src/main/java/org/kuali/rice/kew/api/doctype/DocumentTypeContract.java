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
package org.kuali.rice.kew.api.doctype;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

import java.util.List;
import java.util.Map;

public interface DocumentTypeContract extends Identifiable, Versioned {

    String getName();

    Integer getDocumentTypeVersion();

    String getLabel();

    String getDescription();

    String getParentId();

    boolean isActive();

    String getHelpDefinitionUrl();

    String getDocSearchHelpUrl();

    String getPostProcessorName();

    String getApplicationId();

    boolean isCurrent();

    String getBlanketApproveGroupId();

    String getSuperUserGroupId();
    
    Map<DocumentTypePolicy, String> getPolicies();

    List<? extends DocumentTypeAttributeContract> getDocumentTypeAttributes();
        
    String getResolvedDocumentHandlerUrl();

    String getUnresolvedDocHandlerUrl();

    /**
     * @since 2.1.3
     * @return the custom DocumentTypePermissionService class or service name configured for the doc type
     */
    String getAuthorizer();

}

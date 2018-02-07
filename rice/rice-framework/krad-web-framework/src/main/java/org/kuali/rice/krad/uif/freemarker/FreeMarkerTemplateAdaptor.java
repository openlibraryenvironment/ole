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
package org.kuali.rice.krad.uif.freemarker;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.kuali.rice.krad.uif.component.Component;

import freemarker.core.Environment;
import freemarker.core.InlineTemplateAdaptor;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * Inline FreeMarker template adaptor for supporting template.ftl 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FreeMarkerTemplateAdaptor implements InlineTemplateAdaptor, Serializable {

    private static final long serialVersionUID = -4442716566711789593L;

    /**
     * Render a KRAD component template inline.
     * 
     * @see freemarker.core.InlineKradAdaptor#accept(freemarker.core.Environment)
     */
    @Override
    public void accept(Environment env) throws TemplateException, IOException {
        Component component = FreeMarkerInlineRenderUtils.resolve(env, "component", Component.class);
        
        if (component == null) {
            return;
        }

        String body = FreeMarkerInlineRenderUtils.resolve(env, "body", String.class);
        boolean componentUpdate = Boolean.TRUE.equals(FreeMarkerInlineRenderUtils.resolve(env, "componentUpdate",
                Boolean.class));
        boolean includeSrc = Boolean.TRUE.equals(FreeMarkerInlineRenderUtils.resolve(env, "includeSrc", Boolean.class));
        @SuppressWarnings("unchecked")
        Map<String, TemplateModel> tmplParms = FreeMarkerInlineRenderUtils.resolve(env, "tmplParms", Map.class);
        FreeMarkerInlineRenderUtils.renderTemplate(env, component, body, componentUpdate, includeSrc, tmplParms);
    }

}

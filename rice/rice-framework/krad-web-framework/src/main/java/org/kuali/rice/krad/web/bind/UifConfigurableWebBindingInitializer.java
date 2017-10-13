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
package org.kuali.rice.krad.web.bind;

import org.kuali.rice.core.api.util.type.AbstractKualiDecimal;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.core.api.util.type.KualiPercent;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;

/**
 * Registers standard PropertyEditors used in binding for all http requests.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifConfigurableWebBindingInitializer extends ConfigurableWebBindingInitializer {

    @Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
        super.initBinder(binder, request);

        binder.registerCustomEditor(KualiDecimal.class, new UifCurrencyEditor());
        binder.registerCustomEditor(KualiInteger.class, new UifKualiIntegerCurrencyEditor());

        binder.registerCustomEditor(KualiPercent.class, new UifPercentageEditor());

        binder.registerCustomEditor(java.sql.Time.class, new UifTimeEditor());
        binder.registerCustomEditor(java.sql.Date.class, new UifDateEditor());
        binder.registerCustomEditor(java.util.Date.class, new UifDateEditor());
        binder.registerCustomEditor(Timestamp.class, new UifTimestampEditor());

        // TODO do we need this since we are switching to spring tags
        binder.registerCustomEditor(boolean.class, new UifBooleanEditor());
        binder.registerCustomEditor(Boolean.class, new UifBooleanEditor());
        binder.registerCustomEditor(Boolean.TYPE, new UifBooleanEditor());

        // Use the spring custom number editor for Big decimals
        DecimalFormat bigIntFormatter = new DecimalFormat();
        bigIntFormatter.setMaximumFractionDigits(340);
        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, bigIntFormatter, true));
        binder.registerCustomEditor(AbstractKualiDecimal.class, new CustomNumberEditor(AbstractKualiDecimal.class,
                bigIntFormatter, true));

        // Use the spring StringTrimmerEditor editor for Strings
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));

        // Use the StringArrayPropertyEditor for string arrays with "," as the
        // separator
        binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor(",", false));

        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }
}

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
package org.kuali.rice.krad.inquiry;

import java.util.Set;

import org.kuali.rice.krad.bo.Exporter;
import org.kuali.rice.krad.datadictionary.DataObjectEntry;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.view.InquiryView;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewPresentationControllerBase;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * Implementation of {@link org.kuali.rice.krad.uif.view.ViewPresentationController} for
 * {@link InquiryView} instances
 *
 * <p>
 * Adds flag for export of inquiry record
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class InquiryViewPresentationControllerBase extends ViewPresentationControllerBase {
    private static final long serialVersionUID = 7504225899471226403L;

    /**
     * @see org.kuali.rice.krad.uif.view.ViewPresentationController#getActionFlags(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.web.form.UifFormBase)
     */
    @Override
    public Set<String> getActionFlags(View view, UifFormBase model) {
        Set<String> actionFlags = super.getActionFlags(view, model);

        if (isExportSupported((InquiryView) model.getView())) {
            actionFlags.add(KRADConstants.KUALI_ACTION_CAN_EXPORT);
        }

        return actionFlags;
    }

    /**
     * Examines the data objects data dictionary entry to determine if it supports XML export or not
     *
     * @return boolean true if it supports export, false if not
     */
    protected boolean isExportSupported(InquiryView view) {
        DataObjectEntry dataObjectEntry =
                KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDataObjectEntry(
                        view.getDataObjectClassName().getName());

        Class<? extends Exporter> exporterClass = dataObjectEntry.getExporterClass();
        if (exporterClass != null) {
            try {
                Exporter exporter = exporterClass.newInstance();
                if (exporter.getSupportedFormats(dataObjectEntry.getDataObjectClass()).contains(
                        KRADConstants.XML_FORMAT)) {
                    return true;
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to locate or create exporter class: " + exporterClass);
            }
        }

        return false;
    }
}

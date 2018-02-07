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
package org.kuali.rice.krad.uif.layout;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.uif.component.DataBinding;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.control.Control;
import org.kuali.rice.krad.uif.control.ValueConfiguredControl;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.widget.Pager;
import org.kuali.rice.krad.util.KRADUtils;

import java.util.List;

/**
 * Utilities for collection layout managers
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CollectionLayoutUtils {

    public static void prepareSelectFieldForLine(Field selectField, CollectionGroup collectionGroup, String lineBindingPath,
            Object line) {
        // if select property name set use as property name for select field
        String selectPropertyName = collectionGroup.getLineSelectPropertyName();
        if (StringUtils.isNotBlank(selectPropertyName)) {
            // if select property contains form prefix, will bind to form and not each line
            if (selectPropertyName.startsWith(UifConstants.NO_BIND_ADJUST_PREFIX)) {
                selectPropertyName = StringUtils.removeStart(selectPropertyName, UifConstants.NO_BIND_ADJUST_PREFIX);
                ((DataBinding) selectField).getBindingInfo().setBindingName(selectPropertyName);
                ((DataBinding) selectField).getBindingInfo().setBindToForm(true);

                setControlValueToLineIdentifier(selectField, line);
            } else {
                ((DataBinding) selectField).getBindingInfo().setBindingName(selectPropertyName);
                ((DataBinding) selectField).getBindingInfo().setBindByNamePrefix(lineBindingPath);
            }
        } else {
            // select property name not given, use UifFormBase#selectedCollectionLines
            String collectionLineKey = KRADUtils.translateToMapSafeKey(
                    collectionGroup.getBindingInfo().getBindingPath());
            String selectBindingPath = UifPropertyPaths.SELECTED_COLLECTION_LINES + "['" + collectionLineKey + "']";

            ((DataBinding) selectField).getBindingInfo().setBindingName(selectBindingPath);
            ((DataBinding) selectField).getBindingInfo().setBindToForm(true);

            setControlValueToLineIdentifier(selectField, line);
        }
    }

    protected static void setControlValueToLineIdentifier(Field selectField, Object line) {
        if (selectField instanceof InputField) {
            Control selectControl = ((InputField) selectField).getControl();

            selectControl.addStyleClass("kr-select-line");

            if ((selectControl != null) && (selectControl instanceof ValueConfiguredControl)) {
                String lineIdentifier =
                        KRADServiceLocatorWeb.getDataObjectMetaDataService().getDataObjectIdentifierString(line);
                ((ValueConfiguredControl) selectControl).setValue(lineIdentifier);
            }
        }
    }

    /**
     * Setup a pagerWidget's values for numberOfPages and currentPage, based on the collection size, displayStart,
     * and displayLength
     *
     * @param pagerWidget the Pager to setup
     * @param collectionGroup  the collectionGroup which uses this Pager
     * @param model the current model
     */
    protected static void setupPagerWidget(Pager pagerWidget, CollectionGroup collectionGroup, Object model){
        List<Object> modelCollection = ObjectPropertyUtils.getPropertyValue(model,
                        collectionGroup.getBindingInfo().getBindingPath());

        // The size of the collection divided by the pageLength is used to determine the number of pages for
        // the pager component (ceiling is used if there is not a full page left over in the division)
        if (modelCollection != null) {
            double pages = (double) modelCollection.size() / (double) collectionGroup.getDisplayLength();
            pagerWidget.setNumberOfPages((int) Math.ceil(pages));
        } else {
            pagerWidget.setNumberOfPages(1);
        }

        // By using displayStart, currentPage can be determined, the displayLength is added here before division,
        // because the pager is 1-based
        int currentPage = (collectionGroup.getDisplayStart() + collectionGroup.getDisplayLength()) / collectionGroup
                .getDisplayLength();
        pagerWidget.setCurrentPage(currentPage);

        if (StringUtils.isBlank(pagerWidget.getLinkScript())){
            pagerWidget.setLinkScript("retrieveCollectionPage(this, '" + collectionGroup.getId() + "');");
        }
    }
}

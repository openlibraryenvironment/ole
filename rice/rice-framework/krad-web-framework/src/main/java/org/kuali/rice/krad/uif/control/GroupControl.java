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
package org.kuali.rice.krad.uif.control;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.widget.QuickFinder;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a group control, which is a special control to handle
 * the input of a KIM Group by group name
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "kimGroupControl-bean", parent = "Uif-KimGroupControl")
public class GroupControl extends TextControl implements FilterableLookupCriteriaControl {
    private static final long serialVersionUID = 5598459655735440981L;

    private String namespaceCodePropertyName;
    private String groupIdPropertyName;

    public GroupControl() {
        super();
    }

    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        if (!(parent instanceof InputField)) {
            return;
        }

        InputField field = (InputField) parent;

        if (StringUtils.isNotBlank(groupIdPropertyName)) {
            field.getAdditionalHiddenPropertyNames().add(groupIdPropertyName);
        }

        buildGroupQuickfinder(view, model, field);
    }

    protected void buildGroupQuickfinder(View view,Object model, InputField field) {
        QuickFinder quickFinder = field.getQuickfinder();

        // don't build quickfinder if explicitly turned off
        if ((quickFinder != null) && !quickFinder.isRender()) {
            return;
        }

        boolean quickfinderCreated = false;
        if (quickFinder == null) {
            quickFinder = ComponentFactory.getQuickFinder();
            view.assignComponentIds(quickFinder);

            field.setQuickfinder(quickFinder);

            quickfinderCreated = true;
        }

        if (field.getQuickfinder() != null) {
            if (StringUtils.isBlank(field.getQuickfinder().getDataObjectClassName())) {
                field.getQuickfinder().setDataObjectClassName("org.kuali.rice.kim.impl.group.GroupBo");
            }

            if (field.getQuickfinder().getFieldConversions().isEmpty()) {
                if (StringUtils.isNotBlank(groupIdPropertyName)) {
                    field.getQuickfinder().getFieldConversions().put("id", groupIdPropertyName);
                }

                field.getQuickfinder().getFieldConversions().put("name", field.getPropertyName());

                if (StringUtils.isNotBlank(namespaceCodePropertyName)) {
                    field.getQuickfinder().getFieldConversions().put("namespaceCode", namespaceCodePropertyName);
                }
            }

            if (field.getQuickfinder().getLookupParameters().isEmpty()) {
                if (StringUtils.isNotBlank(namespaceCodePropertyName)) {
                    field.getQuickfinder().getLookupParameters().put(namespaceCodePropertyName, "namespaceCode");
                }
            }
        }

        // if we created the quickfinder here it will have missed the initialize and apply model phase (it
        // will be attached to the field for finalize)
        if (quickfinderCreated) {
            view.getViewHelperService().spawnSubLifecyle(view, model, quickFinder, field,
                    UifConstants.ViewPhases.INITIALIZE, UifConstants.ViewPhases.APPLY_MODEL);
        }

    }

    /**
     * The name of the property on the parent object that holds the group namespace
     *
     * @return namespaceCodePropertyName
     */
    @BeanTagAttribute(name="namespaceCodePropertyName")
    public String getNamespaceCodePropertyName() {
        return namespaceCodePropertyName;
    }

    /**
     * Setter for the name of the property on the parent object that holds the group namespace
     *
     * @param namespaceCodePropertyName
     */
    public void setNamespaceCodePropertyName(String namespaceCodePropertyName) {
        this.namespaceCodePropertyName = namespaceCodePropertyName;
    }

    /**
     * The name of the property on the parent object that holds the group id
     *
     * @return groupIdPropertyName
     */
    @BeanTagAttribute(name="groupIdPropertyName")
    public String getGroupIdPropertyName() {
        return groupIdPropertyName;
    }

    /**
     * Setter for the name of the property on the parent object that holds the group id
     *
     * @param groupIdPropertyName
     */
    public void setGroupIdPropertyName(String groupIdPropertyName) {
        this.groupIdPropertyName = groupIdPropertyName;
    }

    /**
     * @see FilterableLookupCriteriaControl#filterSearchCriteria(String, java.util.Map)
     */
    @Override
    public Map<String, String> filterSearchCriteria(String propertyName, Map<String, String> searchCriteria) {
        Map<String, String> filteredSearchCriteria = new HashMap<String, String>(searchCriteria);

        // check valid groupId
        // ToDo: move the groupId check and setting to the validation stage.  At that point
        //       an error should be displayed to the user that the group name and namespace is invalid.
        String groupName = searchCriteria.get(propertyName);
        String groupNamespaceCd = searchCriteria.get(namespaceCodePropertyName);
        if (StringUtils.isNotBlank(groupName) && StringUtils.isNotBlank(groupNamespaceCd)) {
            Group group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(groupNamespaceCd,groupName);
            if( group == null) {
                return null;
            } else {
                filteredSearchCriteria.put(groupIdPropertyName, group.getId());
            }
        }

        // filter
        filteredSearchCriteria.remove(propertyName);
        filteredSearchCriteria.remove(namespaceCodePropertyName);

        return filteredSearchCriteria;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        GroupControl groupControlCopy = (GroupControl) component;
        groupControlCopy.setNamespaceCodePropertyName(this.namespaceCodePropertyName);
        groupControlCopy.setGroupIdPropertyName(this.groupIdPropertyName);
    }
}

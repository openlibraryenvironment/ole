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
package org.kuali.rice.krad.web.form;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.util.SessionTransient;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Manages Uif form objects for a session
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifFormManager implements Serializable {
    private static final long serialVersionUID = -6323378881342207080L;

    private int maxNumberOfSessionForms = 5;

    private Vector accessedFormKeys;

    private Map<String, UifFormBase> sessionForms;

    /**
     * Create a new form manager with an empty list of forms for the session.
     */
    public UifFormManager() {
        this.accessedFormKeys = new Vector();
        this.sessionForms = new HashMap<String, UifFormBase>();

        String maxNumberOfSessionFormsConfig =
                CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString("maxNumberOfSessionForms");
        if (StringUtils.isNotBlank(maxNumberOfSessionFormsConfig)) {
            maxNumberOfSessionForms = Integer.parseInt(maxNumberOfSessionFormsConfig);
        }
    }

    /**
     * Add a form to the session
     *
     * @param form to be added to the session
     */
    public synchronized void addSessionForm(UifFormBase form) {
        if (form == null || StringUtils.isBlank(form.getFormKey())) {
            throw new RiceIllegalArgumentException("Form or form key was null");
        }

        sessionForms.put(form.getFormKey(), form);

        // add form key to top of vector indicating it is most recent
        if (accessedFormKeys.contains(form.getFormKey())) {
            accessedFormKeys.removeElement(form.getFormKey());
        }
        accessedFormKeys.add(form.getFormKey());

        // check if we have too many forms and need to remove an old one
        if (sessionForms.size() > maxNumberOfSessionForms) {
            // clear all inquiry forms first, temp solution until inquiry forms are not stored in session
            // TODO: remove once inquiry forms are not required to be in session
            Set<String> formKeys = new HashSet<String>(sessionForms.keySet());
            for (String formKey : formKeys) {
                UifFormBase sessionForm = sessionForms.get(formKey);
                if ((sessionForm instanceof InquiryForm) && (!formKey.equals(form.getFormKey()))) {
                    sessionForms.remove(formKey);
                    accessedFormKeys.remove(formKey);
                }
            }
        }

        // if we still have too many forms clear the oldest form
        if (sessionForms.size() > maxNumberOfSessionForms) {
            // get the oldest form we have
            String removeFormKey = (String) accessedFormKeys.get(0);
            if (sessionForms.containsKey(removeFormKey)) {
                sessionForms.remove(removeFormKey);
            }
            accessedFormKeys.removeElementAt(0);
        }
    }

    /**
     * Retrieve a form from the session
     *
     * @param formKey of the form to retrieve from the session
     * @return UifFormBase
     */
    public UifFormBase getSessionForm(String formKey) {
        if (sessionForms.containsKey(formKey)) {
            return sessionForms.get(formKey);
        }

        return null;
    }

    /**
     * Removes the stored form data and the forms from the breadcrumb history from the session
     *
     * @param form to be removed
     */
    public void removeSessionForm(UifFormBase form) {
        if (form == null || StringUtils.isBlank(form.getFormKey())) {
            return;
        }

        removeSessionFormByKey(form.getFormKey());
    }

    /**
     * Removes the stored form data and the forms from the breadcrumb history from the session
     *
     * @param formKey of the form to be removed
     */
    public void removeFormWithHistoryFormsByKey(String formKey) {
        if (sessionForms.containsKey(formKey)) {
            removeSessionFormByKey(formKey);
        }
    }

    /**
     * Removes the stored form data from the session
     *
     * @param formKey of the form to be removed
     */
    public void removeSessionFormByKey(String formKey) {
        if (accessedFormKeys.contains(formKey)) {
            accessedFormKeys.removeElement(formKey);
        }

        if (sessionForms.containsKey(formKey)) {
            sessionForms.remove(formKey);
        }
    }

    /**
     * Indicates whether the form manager has a session form with the given key
     *
     * @param formKey key of the form in session to check for
     * @return true if the manager contains the session form, false if not
     */
    public boolean hasSessionForm(String formKey) {
        return sessionForms.containsKey(formKey);
    }

    /**
     * Retrieves the session form based on the formkey and updates the non session transient
     * variables on the request form from the session form
     *
     * @param requestForm
     * @param formKey
     */
    public void updateFormWithSession(UifFormBase requestForm, String formKey) {
        UifFormBase sessionForm = sessionForms.get(formKey);
        if (sessionForm == null) {
            return;
        }

        if (!sessionForm.getClass().isAssignableFrom(requestForm.getClass())) {
            throw new RuntimeException(
                    "Session form mismatch, session form class not assignable from request form class");
        }

        List<Field> fields = new ArrayList<Field>();
        fields = ObjectUtils.getAllFields(fields, sessionForm.getClass(), UifFormBase.class);
        for (Field field : fields) {
            boolean copyValue = true;
            for (Annotation an : field.getAnnotations()) {
                if (an instanceof SessionTransient) {
                    copyValue = false;
                }
            }

            if (copyValue && ObjectPropertyUtils.isReadableProperty(sessionForm, field.getName()) && ObjectPropertyUtils
                    .isWritableProperty(sessionForm, field.getName())) {
                Object fieldValue = ObjectPropertyUtils.getPropertyValue(sessionForm, field.getName());
                ObjectPropertyUtils.setPropertyValue(requestForm, field.getName(), fieldValue);
            }
        }
    }

    /**
     * Removes the values that are marked @SessionTransient from the form.
     *
     * @param form - the form from which the session transient values have been purged
     */
    public void purgeForm(UifFormBase form) {
        List<Field> fields = new ArrayList<Field>();
        fields = ObjectUtils.getAllFields(fields, form.getClass(), UifFormBase.class);
        for (Field field : fields) {
            boolean purgeValue = false;

            if (!field.getType().isPrimitive()) {
                for (Annotation an : field.getAnnotations()) {
                    if (an instanceof SessionTransient) {
                        purgeValue = true;
                    }
                }
            }

            if (purgeValue && ObjectPropertyUtils.isWritableProperty(form, field.getName())) {
                ObjectPropertyUtils.setPropertyValue(form, field.getName(), null);
            }
        }
    }

    /**
     * Internal vector maintained to keep track of accessed form and the order in which they were accessed
     *
     * <p>
     * Used for the form clearing process. When forms are added to the manager their key is added to the top of
     * the vector. When a form needs to be cleared, the form identified by the key at the botton of this vector
     * is removed
     * </p>
     *
     * @return Vector instance holding form key strings
     */
    protected Vector getAccessedFormKeys() {
        return accessedFormKeys;
    }

    /**
     * Maximum number of forms that can be stored at one time by the manager
     *
     * @return int max number of forms
     */
    public int getMaxNumberOfSessionForms() {
        return maxNumberOfSessionForms;
    }

    /**
     * Setter for the maximum number of forms
     *
     * @param maxNumberOfSessionForms
     */
    public void setMaxNumberOfSessionForms(int maxNumberOfSessionForms) {
        this.maxNumberOfSessionForms = maxNumberOfSessionForms;
    }

}

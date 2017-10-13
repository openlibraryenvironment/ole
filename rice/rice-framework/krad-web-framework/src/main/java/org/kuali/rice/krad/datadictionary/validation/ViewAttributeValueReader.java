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
package org.kuali.rice.krad.datadictionary.validation;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AttributeValueReader which can read the correct values from all InputFields which exist on the View
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewAttributeValueReader extends BaseAttributeValueReader {
    private View view;
    private Object form;

    private List<Constrainable> inputFields = new ArrayList<Constrainable>();
    private Map<String, InputField> inputFieldMap = new HashMap<String, InputField>();

    /**
     * Constructor for ViewAttributeValueReader, the View must already be indexed and
     * the InputFields must have already be initialized for this reader to work properly
     *
     * @param view the View to validate
     * @param form model object representing the View's form data
     */
    public ViewAttributeValueReader(View view, Object form) {
        this.view = view;
        this.form = form;

        List<InputField> containerInputFields = ComponentUtils.getAllInputFieldsWithinContainer(view);
        for (InputField field : containerInputFields) {
            if (StringUtils.isNotBlank(field.getName())) {
                inputFields.add(field);
                inputFieldMap.put(field.getName(), field);
            }
        }
    }

    /*  TODO allow it to be page specific only
        public ViewAttributeValueReader(View view, Page page, UifFormBase form) {
        this.view = view;
        this.form = form;
        for(DataField field: view.getViewIndex().getDataFieldIndex().values()){
            if(field instanceof Constrainable){
                inputFields.add((Constrainable)field);
            }
        }
    }*/

    /**
     * Gets the definition which is an InputField on the View/Page
     */
    @Override
    public Constrainable getDefinition(String attributeName) {
        InputField field = inputFieldMap.get(attributeName);
        if (field != null) {
            return field;
        } else {
            return null;
        }
    }

    /**
     * Gets all InputFields (which extend Constrainable)
     *
     * @return
     */
    @Override
    public List<Constrainable> getDefinitions() {
        return inputFields;
    }

    /**
     * Returns the label associated with the InputField which has that AttributeName
     *
     * @param attributeName
     * @return
     */
    @Override
    public String getLabel(String attributeName) {
        InputField field = inputFieldMap.get(attributeName);
        if (field != null) {
            return field.getLabel();
        } else {
            return null;
        }
    }

    /**
     * Returns the View object
     *
     * @return view set in the constructor
     */
    @Override
    public Object getObject() {
        return view;
    }

    /**
     * Not used for this reader, returns null
     *
     * @return null
     */
    @Override
    public Constrainable getEntry() {
        return null;
    }

    /**
     * Returns current attributeName which represents the path
     *
     * @return attributeName set on this reader
     */
    @Override
    public String getPath() {
        return this.attributeName;
    }

    /**
     * Gets the type of value for this AttributeName as represented on the Form
     *
     * @param attributeName
     * @return
     */
    @Override
    public Class<?> getType(String attributeName) {
        Object fieldValue = ObjectPropertyUtils.getPropertyValue(form, attributeName);
        return fieldValue.getClass();
    }

    /**
     * If the current attribute being evaluated is a field of an addLine return false because it should not
     * be evaluated during Validation.
     *
     * @return false if InputField is part of an addLine for a collection, true otherwise
     */
    @Override
    public boolean isReadable() {
        if (attributeName != null && attributeName.contains(UifPropertyPaths.NEW_COLLECTION_LINES)) {
            return false;
        }
        return true;
    }

    /**
     * Return value of the field for the attributeName currently set on this reader
     *
     * @param <X> return type
     * @return value of the field for the attributeName currently set on this reader
     * @throws AttributeValidationException
     */
    @Override
    public <X> X getValue() throws AttributeValidationException {
        X fieldValue = null;
        if (StringUtils.isNotBlank(this.attributeName)) {
            fieldValue = ObjectPropertyUtils.<X>getPropertyValue(form, this.attributeName);
        }
        return fieldValue;
    }

    /**
     * Return value of the field for the attributeName passed in
     *
     * @param attributeName name (which represents a path) of the value to be retrieved on the Form
     * @param <X> return type
     * @return value of that attributeName represents on the form
     * @throws AttributeValidationException
     */
    @Override
    public <X> X getValue(String attributeName) throws AttributeValidationException {
        X fieldValue = null;
        if (StringUtils.isNotBlank(attributeName)) {
            fieldValue = ObjectPropertyUtils.<X>getPropertyValue(form, this.attributeName);
        }
        return fieldValue;
    }

    /**
     * Cones this AttributeValueReader
     *
     * @return AttributeValueReader
     */
    @Override
    public AttributeValueReader clone() {
        ViewAttributeValueReader clone = new ViewAttributeValueReader(view, form);
        clone.setAttributeName(this.attributeName);
        return clone;
    }

}

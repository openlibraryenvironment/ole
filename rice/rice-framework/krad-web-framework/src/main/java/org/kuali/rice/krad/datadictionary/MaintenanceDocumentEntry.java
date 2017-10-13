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
package org.kuali.rice.krad.datadictionary;

import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.exception.ClassValidationException;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.Maintainable;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentAuthorizer;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentAuthorizerBase;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentBase;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentPresentationControllerBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Data dictionary entry class for <code>MaintenanceDocument</code>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "maintenanceDocumentEntry-bean", parent = "uifMaintenanceDocumentEntry")
public class MaintenanceDocumentEntry extends DocumentEntry {
    private static final long serialVersionUID = 4990040987835057251L;

    protected Class<?> dataObjectClass;
    protected Class<? extends Maintainable> maintainableClass;

    protected List<String> lockingKeys = new ArrayList<String>();

    protected boolean allowsNewOrCopy = true;
    protected boolean preserveLockingKeysOnCopy = false;
    protected boolean allowsRecordDeletion = false;

    public MaintenanceDocumentEntry() {
        super();

        setDocumentClass(getStandardDocumentBaseClass());
        documentAuthorizerClass = MaintenanceDocumentAuthorizerBase.class;
        documentPresentationControllerClass = MaintenanceDocumentPresentationControllerBase.class;
    }

    public Class<? extends Document> getStandardDocumentBaseClass() {
        return MaintenanceDocumentBase.class;
    }

    /*
            This attribute is used in many contexts, for example, in maintenance docs, it's used to specify the classname
            of the BO being maintained.
     */
    public void setDataObjectClass(Class<?> dataObjectClass) {
        if (dataObjectClass == null) {
            throw new IllegalArgumentException("invalid (null) dataObjectClass");
        }

        this.dataObjectClass = dataObjectClass;
    }

    @BeanTagAttribute(name = "dataObjectClass")
    public Class<?> getDataObjectClass() {
        return dataObjectClass;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.DocumentEntry#getEntryClass()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class getEntryClass() {
        return dataObjectClass;
    }

    /*
            The maintainableClass element specifies the name of the
            java class which is responsible for implementing the
            maintenance logic.
            The normal one is KualiMaintainableImpl.java.
     */
    public void setMaintainableClass(Class<? extends Maintainable> maintainableClass) {
        if (maintainableClass == null) {
            throw new IllegalArgumentException("invalid (null) maintainableClass");
        }
        this.maintainableClass = maintainableClass;
    }

    @BeanTagAttribute(name = "maintainableClass")
    public Class<? extends Maintainable> getMaintainableClass() {
        return maintainableClass;
    }

    /**
     * @return List of all lockingKey fieldNames associated with this LookupDefinition, in the order in which they were
     *         added
     */
    public List<String> getLockingKeyFieldNames() {
        return lockingKeys;
    }

    /**
     * Gets the allowsNewOrCopy attribute.
     *
     * @return Returns the allowsNewOrCopy.
     */
    @BeanTagAttribute(name = "allowsNewOrCopy")
    public boolean getAllowsNewOrCopy() {
        return allowsNewOrCopy;
    }

    /**
     * The allowsNewOrCopy element contains a value of true or false.
     * If true, this indicates the maintainable should allow the
     * new and/or copy maintenance actions.
     */
    public void setAllowsNewOrCopy(boolean allowsNewOrCopy) {
        this.allowsNewOrCopy = allowsNewOrCopy;
    }

    /**
     * Directly validate simple fields, call completeValidation on Definition fields.
     *
     * @see org.kuali.rice.krad.datadictionary.DocumentEntry#completeValidation()
     */
    public void completeValidation() {
        super.completeValidation();

        for (String lockingKey : lockingKeys) {
            if (!DataDictionary.isPropertyOf(dataObjectClass, lockingKey)) {
                throw new AttributeValidationException(
                        "unable to find attribute '" + lockingKey + "' for lockingKey in dataObjectClass '" +
                                dataObjectClass.getName());
            }
        }

        for (ReferenceDefinition reference : defaultExistenceChecks) {
            reference.completeValidation(dataObjectClass, null);
        }

        if (documentAuthorizerClass != null && !MaintenanceDocumentAuthorizer.class.isAssignableFrom(
                documentAuthorizerClass)) {
            throw new ClassValidationException(
                    "This maintenance document for '" + getDataObjectClass().getName() + "' has an invalid " +
                            "documentAuthorizerClass ('" + documentAuthorizerClass.getName() + "').  " +
                            "Maintenance Documents must use an implementation of MaintenanceDocumentAuthorizer.");
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "MaintenanceDocumentEntry for documentType " + getDocumentTypeName();
    }

    @BeanTagAttribute(name = "lockingKeys", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getLockingKeys() {
        return lockingKeys;
    }

    /*
           The lockingKeys element specifies a list of fields
           that comprise a unique key.  This is used for record locking
           during the file maintenance process.
    */
    public void setLockingKeys(List<String> lockingKeys) {
        for (String lockingKey : lockingKeys) {
            if (lockingKey == null) {
                throw new IllegalArgumentException("invalid (null) lockingKey");
            }
        }
        this.lockingKeys = lockingKeys;
    }

    /**
     * @return the preserveLockingKeysOnCopy
     */
    @BeanTagAttribute(name = "preserveLockingKeysOnCopy")
    public boolean getPreserveLockingKeysOnCopy() {
        return this.preserveLockingKeysOnCopy;
    }

    /**
     * @param preserveLockingKeysOnCopy the preserveLockingKeysOnCopy to set
     */
    public void setPreserveLockingKeysOnCopy(boolean preserveLockingKeysOnCopy) {
        this.preserveLockingKeysOnCopy = preserveLockingKeysOnCopy;
    }

    /**
     * @return the allowRecordDeletion
     */
    @BeanTagAttribute(name = "allowsRecordDeletion")
    public boolean getAllowsRecordDeletion() {
        return this.allowsRecordDeletion;
    }

    /**
     * @param allowsRecordDeletion the allowRecordDeletion to set
     */
    public void setAllowsRecordDeletion(boolean allowsRecordDeletion) {
        this.allowsRecordDeletion = allowsRecordDeletion;
    }

}

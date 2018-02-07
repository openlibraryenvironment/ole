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
package org.kuali.rice.krad.service;

import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectExtension;
import org.kuali.rice.krad.util.ForeignKeyFieldsPopulationState;

import java.util.List;
import java.util.Map;

/**
 * Defines methods that a Persistence Service must provide. PersistenceMetadataService provides access to
 * persistence-layer information about persistable classes
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface PersistenceStructureService {
    /**
     * @param clazz
     * @return true if the given Class is persistable (is known to OJB)
     */
    public boolean isPersistable(Class clazz);

    /**
     * @param clazz Class whose primary key field names you want to list
     * @return a List of field names for the given class which are designated as key fields in the OJB repository file
     * @throws IllegalArgumentException if the given Class is null
     * @throws org.kuali.rice.krad.exception.ClassNotPersistableException if the given object is of a type not described in the OJB repository
     */
    public List listPrimaryKeyFieldNames(Class clazz);


    /**
     * @param clazz Class whose field names you want to list
     * @return a List of field names for the given class in the OJB repository file
     * @throws IllegalArgumentException if the given Class is null
     * @throws org.kuali.rice.krad.exception.ClassNotPersistableException if the given object is of a type not described in the OJB repository
     */
    public List listFieldNames(Class clazz);


    /**
     * @param clazz whose primary key field name, anonymous key marking is requested for
     * @return a Map containing the primary key name as the key and Boolean indicating whether or not the pk is marked as anonymous
     *         in the obj repository file
     * @throws IllegalArgumentException if the given Object is null
     * @throws org.kuali.rice.krad.exception.ClassNotPersistableException if the given object is of a type not described in the OJB repository
     */
    /* No references - https://test.kuali.org/confluence/x/SYCf
    public Map getPrimaryKeyFieldAnonymousMarking(Class clazz);
	*/
    
    /**
     * 
     * This method returns a List of Strings, each containing the field name of one of the primary keys, as defined in the ORM
     * layer.
     * 
     * @param clazz - Class whose primary key field names are requested
     * @return A List of Strings, each containing the field name of the primary key
     * @throws IllegalArgumentException if the given Object is null
     * @throws org.kuali.rice.krad.exception.ClassNotPersistableException if the given object is of a type not described in the OJB repository
     * 
     */
    public List getPrimaryKeys(Class clazz);

    /**
     * @param persistableObject
     * @return true if all primary key fields of the string have a non-null (and non-empty, for Strings) value
     * @throws IllegalArgumentException if the given Object is null
     * @throws org.kuali.rice.krad.exception.ClassNotPersistableException if the given object is of a type not described in the OJB repository
     */
    public boolean hasPrimaryKeyFieldValues(Object persistableObject);

    /**
     * @param persistableObject object whose primary key fields need to be cleared
     * @return the object whose primary key fields have just been cleared
     * @throws IllegalArgumentException if the given Object is null
     * @throws org.kuali.rice.krad.exception.ClassNotPersistableException if the given object is of a type not described in the OJB repository
     */
    public Object clearPrimaryKeyFields(Object persistableObject);


    /**
     * @param superclazz class whose persistable subclasses (or interface whose implementors) will be returned
     * @return a List of persistable Classes which extend or implement the given Class
     * @throws IllegalArgumentException if the given class is null
     */
    public List listPersistableSubclasses(Class superclazz);

    /**
     * @param persistableClass
     * @param attributeName Name of an attribute used in the relationship
     * @return BusinessObjectRelationship object containing information about the object type related via the named relationship of the
     *         given class, or null if the persistence service can find no object type related via the named relationship
     * @throws IllegalArgumentException if the given Class is null
     * @throws IllegalArgumentException if the given relationshipName is blanks
     * @throws org.kuali.rice.krad.exception.ClassNotPersistableException if the given Class is a type not described in the OJB repository
     */
    public Map<String,DataObjectRelationship> getRelationshipMetadata(Class persistableClass, String attributeName, String attributePrefix );

    public Map<String,DataObjectRelationship> getRelationshipMetadata(Class persistableClass, String attributeName);
    
    public String getForeignKeyFieldName(Class persistableObjectClass, String attributeName, String pkName);

    /**
     * Attempts to match the attribute name given for the class as a fk field to a reference class defined in the repository. Since
     * a fk field can have references to many tables, this returns a list of all found.
     * 
     * @param persistableObjectClass
     * @param attributeName
     * @return Map with attribue name as key of map and class as value
     */
    public Map<String,Class> getReferencesForForeignKey(Class persistableObjectClass, String attributeName);

    /**
     * 
     * This method will return a Map of all the foreign key fields and the corresponding primary key fields for a given reference.
     * 
     * The Map structure is: Key(String fkFieldName) => Value(String pkFieldName)
     * 
     * @param clazz - Class that contains the named reference
     * @param attributeName - Name of the member that is the reference you want foreign keys for
     * @return returns a Map populated as described above, with one entry per foreign key field
     * 
     */
    public Map getForeignKeysForReference(Class clazz, String attributeName);

    /**
     * 
     * This method is a PersistableBusinessObject specifific utility method. If the Class clazz passed in is a descendent of PersistableBusinessObject,
     * and if the attributeName specified exists on the object, then the class of this
     * attribute named will be returned.
     * 
     * @param clazz - class to be examined for the attribute's class
     * @param attributeName - name of the class' attribute to be examined
     * @return the class of the named attribute, if no exceptions occur
     */
    public Class<? extends PersistableBusinessObjectExtension> getBusinessObjectAttributeClass(Class<? extends PersistableBusinessObject> clazz, String attributeName);

    /**
     * Builds a map of reference pk attributes back to the foreign key.
     * 
     * @param persistableObjectClass
     * @return
     */
    public Map getNestedForeignKeyMap(Class persistableObjectClass);

    /**
     * 
     * This method checks the foreign keys for a reference on a given BO, and tests that all fk fields are populated if any are
     * populated.
     * 
     * In other words, for a given reference, it finds all the attributes of the BO that make up the foreign keys, and checks to see
     * if they all have values. It also keeps a list of all the fieldNames that do not have values.
     * 
     * @param bo - A populated BusinessObject descendent. Must contain an attributed named referenceName.
     * @param referenceName - The name of the field that is a reference we are analyzing.
     * @return A populated ForeignKeyFieldsPopulation object which represents the state of population for the foreign key fields.
     */
    public ForeignKeyFieldsPopulationState getForeignKeyFieldsPopulationState(PersistableBusinessObject bo, String referenceName);

    /**
     * 
     * This method uses the persistence layer to determine the list of reference objects contained within this parent object. For
     * example, an Account object contains sub-objects such as Chart, as well as the key that connects the two, String
     * chartOfAccountsCode.
     * 
     * The return structure is: Map<referenceName, referenceClass>.
     * 
     * As an example, an Account object passed into this would return:
     * 
     * 0:['chartOfAccounts', org.kuali.module.chart.bo.Chart] 1:['organization', org.kuali.module.chart.bo.Org] etc.
     * 
     * @param boClass Class that would like to be analyzed for reference names
     * @return Map containing the reference name for the key as a string, and the class of the reference as the value. If the object
     *         contains no references, then this Map will be empty.
     * 
     */
    public Map<String, Class> listReferenceObjectFields(Class boClass);

    /**
     * 
     * This method uses the persistence layer to determine the list of reference objects contained within this parent object. For
     * example, an Account object contains sub-objects such as Chart, as well as the key that connects the two, String
     * chartOfAccountsCode.
     * 
     * The return structure is: Map<referenceName, referenceClass>.
     * 
     * As an example, an Account object passed into this would return:
     * 
     * 0:['chartOfAccounts', org.kuali.module.chart.bo.Chart] 1:['organization', org.kuali.module.chart.bo.Org] etc.
     * 
     * @param bo BusinessObject (or subclass) instance that would like to be analyzed for reference names
     * @return Map containing the reference name for the key as a string, and the class of the reference as the value. If the object
     *         contains no references, then this Map will be empty.
     * 
     */
    public Map<String, Class> listReferenceObjectFields(PersistableBusinessObject bo);

    public Map<String, Class> listCollectionObjectTypes(Class boClass);
    public Map<String, Class> listCollectionObjectTypes(PersistableBusinessObject bo);
    
    /**
     * Returns whether there is a reference defined in the persistence layer with the given name.
     * Depending on the type of underlying persistence mechanism, this method may or may not return true
     * when the referenceName really refers to a collection type.
     * 
     * To determine whether a reference is a collection, use the hasCollection method instead.
     * 
     * In OJB, this method will return false for collection references.
     * 
     * @param boClass
     * @param referenceName
     * @return
     */
    public boolean hasReference(Class boClass, String referenceName);
    
    
    /**
     * Returns whether BOs of the given class have a collection defined within them with the given collection name.
     * 
     * @param boClass
     * @param collectionName
     * @return
     */
    public boolean hasCollection(Class boClass, String collectionName);
    
    public boolean isReferenceUpdatable(Class boClass, String referenceName);
    public boolean isCollectionUpdatable(Class boClass, String collectionName);
    
    /**
     * Returns a listing of the FK field mappings between a BO and the elements in a collection. Since this is in effect a 
     * 1:n relationship, only the complete primary key set of the parent BO will be returned.
     * 
     * for example, assume Account BO has an "acctNbrForAcct" PK, and it has a list of subAccounts, 
     * each of which has a ("acctNbrForSubAcct", "subAcctNbr") PK pair.
     * 
     * the Account PK will be mapped to some of the PK fields of the element list.  
     * When called on the Account BO class with the "subAccounts" collection name, his method should return
     * a map with a mapping of "acctNbrForAcct" (key) => "acctNbrForSubAcct"
     * 
     * @param boClass
     * @param collectionName
     * @return
     */
    public Map<String, String> getInverseForeignKeysForCollection(Class boClass, String collectionName);
    
    /**
     * Returns the name of the table underlying the business object class
     * 
     * @param boClass
     * @return
     */
    public String getTableName(Class<? extends PersistableBusinessObject> boClass);
}

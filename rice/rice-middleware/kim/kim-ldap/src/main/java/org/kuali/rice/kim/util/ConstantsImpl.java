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
package org.kuali.rice.kim.util;

import java.util.Collection;

import org.kuali.rice.kim.api.identity.entity.Entity;

/**
 * KIM Related Constants Implementation
 *
 * @author Leo Przybylski (przybyls@arizona.edu)
 */ 
class ConstantsImpl implements Constants {    
    private Collection<String> testPrincipalNames;
    private Entity entityPrototype;
    private String externalIdTypeProperty;
    private String taxExternalIdTypeCode;
    private String externalIdProperty;
    private String employeePhoneLdapProperty;
    private String employeeMailLdapProperty;
    private String defaultCountryCode;
    private String personEntityTypeCode;
    private String kimLdapIdProperty;
    private String kimLdapNameProperty;
    private String snLdapProperty;
    private String givenNameLdapProperty;
    private String entityIdKimProperty;
    private String parameterNamespaceCode;
    private String parameterDetailTypeCode;
    private String mappedParameterName;
    private String unmappedParameterName;
    private String mappedValuesName;
    private String employeeIdProperty;
    private String departmentLdapProperty;
    private String employeeTypeProperty;
    private String employeeStatusProperty;
    private String defaultCampusCode;
    private String defaultChartCode;
    private String employeeAffiliationCodes;
    private String affiliationMappings;
    private String affiliationLdapProperty;
    private String primaryAffiliationLdapProperty;

    public Collection<String> getTestPrincipalNames() {
        return testPrincipalNames;
    }

    public void setTestPrincipalNames(Collection<String> testPrincipalNames) {
        this.testPrincipalNames = testPrincipalNames;
    }

    /**
     * Gets the value of entityPrototype
     *
     * @return the value of entityPrototype
     */
    public Entity getEntityPrototype() {
        return entityPrototype;
    }

    /**
     * Sets the value of entityPrototype
     *
     * @param argEntityPrototype Value to assign to this.entityPrototype
     */
    public void setEntityPrototype(Entity argEntityPrototype) {
        this.entityPrototype = argEntityPrototype;
    }

    /**
     * Gets the value of externalIdTypeProperty
     *
     * @return the value of externalIdTypeProperty
     */
    public String getExternalIdTypeProperty() {
        return this.externalIdTypeProperty;
    }

    /**
     * Sets the value of externalIdTypeProperty
     *
     * @param argExternalIdTypeProperty Value to assign to this.externalIdTypeProperty
     */
    public void setExternalIdTypeProperty(String argExternalIdTypeProperty) {
        this.externalIdTypeProperty = argExternalIdTypeProperty;
    }

    /**
     * Gets the value of taxExternalIdTypeCode
     *
     * @return the value of taxExternalIdTypeCode
     */
    public String getTaxExternalIdTypeCode() {
        return this.taxExternalIdTypeCode;
    }

    /**
     * Sets the value of taxExternalIdTypeCode
     *
     * @param argTaxExternalIdTypeCode Value to assign to this.taxExternalIdTypeCode
     */
    public void setTaxExternalIdTypeCode(String argTaxExternalIdTypeCode) {
        this.taxExternalIdTypeCode = argTaxExternalIdTypeCode;
    }

    /**
     * Gets the value of externalIdProperty
     *
     * @return the value of externalIdProperty
     */
    public String getExternalIdProperty() {
        return this.externalIdProperty;
    }

    /**
     * Sets the value of externalIdProperty
     *
     * @param argExternalIdProperty Value to assign to this.externalIdProperty
     */
    public void setExternalIdProperty(String argExternalIdProperty) {
        this.externalIdProperty = argExternalIdProperty;
    }

    /**
     * Gets the value of employeePhoneLdapProperty
     *
     * @return the value of employeePhoneLdapProperty
     */
    public String getEmployeePhoneLdapProperty() {
        return this.employeePhoneLdapProperty;
    }

    /**
     * Sets the value of employeePhoneLdapProperty
     *
     * @param argEmployeePhoneLdapProperty Value to assign to this.employeePhoneLdapProperty
     */
    public void setEmployeePhoneLdapProperty(String argEmployeePhoneLdapProperty) {
        this.employeePhoneLdapProperty = argEmployeePhoneLdapProperty;
    }

    /**
     * Gets the value of employeeMailLdapProperty
     *
     * @return the value of employeeMailLdapProperty
     */
    public String getEmployeeMailLdapProperty() {
        return this.employeeMailLdapProperty;
    }

    /**
     * Sets the value of employeeMailLdapProperty
     *
     * @param argEmployeeMailLdapProperty Value to assign to this.employeeMailLdapProperty
     */
    public void setEmployeeMailLdapProperty(String argEmployeeMailLdapProperty) {
        this.employeeMailLdapProperty = argEmployeeMailLdapProperty;
    }

    /**
     * Gets the value of defaultCountryCode
     *
     * @return the value of defaultCountryCode
     */
    public String getDefaultCountryCode() {
        return this.defaultCountryCode;
    }

    /**
     * Sets the value of defaultCountryCode
     *
     * @param argDefaultCountryCode Value to assign to this.defaultCountryCode
     */
    public void setDefaultCountryCode(String argDefaultCountryCode) {
        this.defaultCountryCode = argDefaultCountryCode;
    }

    /**
     * Gets the value of personEntityTypeCode
     *
     * @return the value of personEntityTypeCode
     */
    public String getPersonEntityTypeCode() {
        return this.personEntityTypeCode;
    }

    /**
     * Sets the value of personEntityTypeCode
     *
     * @param argPersonEntityTypeCode Value to assign to this.personEntityTypeCode
     */
    public void setPersonEntityTypeCode(String argPersonEntityTypeCode) {
        this.personEntityTypeCode = argPersonEntityTypeCode;
    }

    /**
     * Sets the value of kimLdapIdProperty
     *
     * @param kimLdapIdProperty value to set
     */
    public void setKimLdapIdProperty(String kimLdapIdProperty) {
        this.kimLdapIdProperty = kimLdapIdProperty;
    }

    /**
     * Gets the value of kimLdapIdProperty
     *
     * @return the value of kimLdapIdProperty
     */
    public String getKimLdapIdProperty() {
        return kimLdapIdProperty;
    }

    /**
     * Gets the value of kimLdapNameProperty
     *
     * @param kimLdapNameProperty the value of kimLdapNameProperty
     */
    public void setKimLdapNameProperty(String kimLdapNameProperty) {
        this.kimLdapNameProperty = kimLdapNameProperty;
    }

    /**
     * Gets the value of kimLdapNameProperty
     *
     * @return the value of kimLdapNameProperty
     */
    public String getKimLdapNameProperty() {
        return kimLdapNameProperty;
    }

    /**
     * Gets the value of snLdapProperty
     *
     * @return the value of snLdapProperty
     */
    public String getSnLdapProperty() {
        return this.snLdapProperty;
    }

    /**
     * Sets the value of snLdapProperty
     *
     * @param argSnLdapProperty Value to assign to this.snLdapProperty
     */
    public void setSnLdapProperty(String argSnLdapProperty) {
        this.snLdapProperty = argSnLdapProperty;
    }

    /**
     * Gets the value of givenNameLdapProperty
     *
     * @return the value of givenNameLdapProperty
     */
    public String getGivenNameLdapProperty() {
        return this.givenNameLdapProperty;
    }

    /**
     * Sets the value of givenNameLdapProperty
     *
     * @param argGivenNameLdapProperty Value to assign to this.givenNameLdapProperty
     */
    public void setGivenNameLdapProperty(String argGivenNameLdapProperty) {
        this.givenNameLdapProperty = argGivenNameLdapProperty;
    }

    /**
     * Gets the value of entityIdKimProperty
     *
     * @return the value of entityIdKimProperty
     */
    public String getEntityIdKimProperty() {
        return this.entityIdKimProperty;
    }

    /**
     * Sets the value of entityIdKimProperty
     *
     * @param argEntityIdKimProperty Value to assign to this.entityIdKimProperty
     */
    public void setEntityIdKimProperty(String argEntityIdKimProperty) {
        this.entityIdKimProperty = argEntityIdKimProperty;
    }

    /**
     * Gets the value of parameterNamespaceCode
     *
     * @return the value of parameterNamespaceCode
     */
    public String getParameterNamespaceCode() {
        return this.parameterNamespaceCode;
    }

    /**
     * Sets the value of parameterNamespaceCode
     *
     * @param argParameterNamespaceCode Value to assign to this.parameterNamespaceCode
     */
    public void setParameterNamespaceCode(String argParameterNamespaceCode) {
        this.parameterNamespaceCode = argParameterNamespaceCode;
    }

    /**
     * Gets the value of parameterDetailTypeCode
     *
     * @return the value of parameterDetailTypeCode
     */
    public String getParameterDetailTypeCode() {
        return this.parameterDetailTypeCode;
    }

    /**
     * Sets the value of parameterDetailTypeCode
     *
     * @param argParameterDetailTypeCode Value to assign to this.parameterDetailTypeCode
     */
    public void setParameterDetailTypeCode(String argParameterDetailTypeCode) {
        this.parameterDetailTypeCode = argParameterDetailTypeCode;
    }

    /**
     * Gets the value of mappedParameterName
     *
     * @return the value of mappedParameterName
     */
    public String getMappedParameterName() {
        return this.mappedParameterName;
    }

    /**
     * Sets the value of mappedParameterName
     *
     * @param argMappedParameterName Value to assign to this.mappedParameterName
     */
    public void setMappedParameterName(String argMappedParameterName) {
        this.mappedParameterName = argMappedParameterName;
    }

    /**
     * Gets the value of unmappedParameterName
     *
     * @return the value of unmappedParameterName
     */
    public String getUnmappedParameterName() {
        return this.unmappedParameterName;
    }

    /**
     * Sets the value of unmappedParameterName
     *
     * @param argUnmappedParameterName Value to assign to this.unmappedParameterName
     */
    public void setUnmappedParameterName(String argUnmappedParameterName) {
        this.unmappedParameterName = argUnmappedParameterName;
    }

    /**
     * Gets the value of mappedValuesName
     *
     * @return the value of mappedValuesName
     */
    public String getMappedValuesName() {
        return this.mappedValuesName;
    }

    /**
     * Sets the value of mappedValuesName
     *
     * @param argMappedValuesName Value to assign to this.mappedValuesName
     */
    public void setMappedValuesName(String argMappedValuesName) {
        this.mappedValuesName = argMappedValuesName;
    }
    
    /**
     * Gets the value of employeeIdProperty
     *
     * @return the value of employeeIdProperty
     */
    public String getEmployeeIdProperty() {
        return this.employeeIdProperty;
    }

    /**
     * Sets the value of employeeIdProperty
     *
     * @param argEmployeeIdProperty Value to assign to this.employeeIdProperty
     */
    public void setEmployeeIdProperty(String argEmployeeIdProperty) {
        this.employeeIdProperty = argEmployeeIdProperty;
    }

    /**
     * Gets the value of departmentLdapProperty
     *
     * @return the value of departmentLdapProperty
     */
    public String getDepartmentLdapProperty() {
        return this.departmentLdapProperty;
    }

    /**
     * Sets the value of departmentLdapProperty
     *
     * @param argDepartmentLdapProperty Value to assign to this.departmentLdapProperty
     */
    public void setDepartmentLdapProperty(String argDepartmentLdapProperty) {
        this.departmentLdapProperty = argDepartmentLdapProperty;
    }

    /**
     * Gets the value of employeeTypeProperty
     *
     * @return the value of employeeTypeProperty
     */
    public String getEmployeeTypeProperty() {
        return this.employeeTypeProperty;
    }

    /**
     * Sets the value of employeeTypeProperty
     *
     * @param argEmployeeTypeProperty Value to assign to this.employeeTypeProperty
     */
    public void setEmployeeTypeProperty(String argEmployeeTypeProperty) {
        this.employeeTypeProperty = argEmployeeTypeProperty;
    }

    /**
     * Gets the value of employeeStatusProperty
     *
     * @return the value of employeeStatusProperty
     */
    public String getEmployeeStatusProperty() {
        return this.employeeStatusProperty;
    }

    /**
     * Sets the value of employeeStatusProperty
     *
     * @param argEmployeeStatusProperty Value to assign to this.employeeStatusProperty
     */
    public void setEmployeeStatusProperty(String argEmployeeStatusProperty) {
        this.employeeStatusProperty = argEmployeeStatusProperty;
    }

    /**
     * Gets the value of defaultCampusCode
     *
     * @return the value of defaultCampusCode
     */
    public String getDefaultCampusCode() {
        return this.defaultCampusCode;
    }

    /**
     * Sets the value of defaultCampusCode
     *
     * @param argDefaultCampusCode Value to assign to this.defaultCampusCode
     */
    public void setDefaultCampusCode(String argDefaultCampusCode) {
        this.defaultCampusCode = argDefaultCampusCode;
    }


    public void setDefaultChartCode(String chartCode) {
        this.defaultChartCode = chartCode;
    }

    public String getDefaultChartCode() {
        return defaultChartCode;
    }

    public String getEmployeeAffiliationCodes() {
        return employeeAffiliationCodes;
    }

    public void setEmployeeAffiliationCodes(String employeeAffiliationCodes) {
        this.employeeAffiliationCodes = employeeAffiliationCodes;
    }

    public String getAffiliationMappings() {
        return affiliationMappings;
    }

    public void setAffiliationMappings(String affiliationMappings) {
        this.affiliationMappings = affiliationMappings;
    }

    /**
     * Gets the mappings for the affiliation ldap property
     * @return mapping for KIM affiliation and LDAP
     */
    public String getAffiliationLdapProperty() {
        return affiliationLdapProperty;
    }

    /**
     * Sets the mappings for the affiliation ldap property
     * @param affiliationLdapProperty mapping for KIM affiliation and LDAP
     */
    public void setAffiliationLdapProperty(final String affiliationLdapProperty) {
        this.affiliationLdapProperty = affiliationLdapProperty;
    }

    /**
     * Gets the mappings for the primary affiliation ldap property
     * @return mapping for KIM primary affiliation and LDAP
     */
    public String getPrimaryAffiliationLdapProperty() {
        return primaryAffiliationLdapProperty;
    }

    /**
     * Sets the mappings for the affiliation ldap property
     * @param primaryAffiliationLdapProperty mapping for KIM primaryAffiliation and LDAP
     */
    public void setPrimaryAffiliationLdapProperty(final String primaryAffiliationLdapProperty) {
        this.primaryAffiliationLdapProperty = primaryAffiliationLdapProperty;
    }
}

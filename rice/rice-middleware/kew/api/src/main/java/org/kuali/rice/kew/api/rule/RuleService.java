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
package org.kuali.rice.kew.api.rule;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.springframework.cache.annotation.Cacheable;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@WebService(name = "ruleService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface RuleService {
    /**
     * gets a Rule identified by the passed in id
     *
     * @param id unique id for the Rule
     *
     * @return Rule with the passed in unique id
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code id} is null
     * @throws org.kuali.rice.core.api.exception.RiceIllegalStateException if Rule does not exist
     */
    @WebMethod(operationName = "getRule")
    @WebResult(name = "rule")
    @Cacheable(value=Rule.Cache.NAME, key="'id=' + #p0")
	Rule getRule(@WebParam(name="id") String id)
        throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * gets a Rule identified by the passed in rule name
     *
     * @param name name of the Rule
     *
     * @return Rule with the passed in unique id
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code name} is null
     * @throws org.kuali.rice.core.api.exception.RiceIllegalStateException if Rule does not exist
     */
    @WebMethod(operationName = "getRuleByName")
    @WebResult(name = "rule")
    @Cacheable(value=Rule.Cache.NAME, key="'name=' + #p0")
	Rule getRuleByName(@WebParam(name="name") String name)
        throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * gets a list of Rules with the specified templateId
     *
     * @param templateId unique id for the Rule
     *
     * @return Rules with the passed in templateId, or an empty list if none exist
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code templateId} is null
     */
    @WebMethod(operationName = "getRuleByTemplateId")
    @WebResult(name = "rules")
    @XmlElementWrapper(name = "rules", required = true)
    @XmlElement(name = "rule", required = true)
    @Cacheable(value=Rule.Cache.NAME, key="'templateId=' + #p0")
	List<Rule> getRulesByTemplateId(@WebParam(name="templateId") String templateId)
        throws RiceIllegalArgumentException;

    /**
     * Gets a list of Rules with the specified templateId and documentTypeName.  Scales up the hierarchy of
     * documentTypes
     *
     * @param templateName unique name for the Rule Template.  Cannot be null or empty
     * @param documentTypeName documentTypeName for Rule.  Cannot be null or empty
     *
     * @return Rules with the passed in templateId, documentTypeName (or parent document type)or an empty list if none exist
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code id} is null
     */
    @WebMethod(operationName = "getRulesByTemplateNameAndDocumentTypeName")
    @WebResult(name = "rules")
    @XmlElementWrapper(name = "rules", required = true)
    @XmlElement(name = "rule", required = true)
    @Cacheable(value=Rule.Cache.NAME, key="'templateName=' + #p0 + '|' + 'documentTypeName=' + #p1")
	List<Rule> getRulesByTemplateNameAndDocumentTypeName(@WebParam(name = "templateName") String templateName,
            @WebParam(name = "documentTypeName") String documentTypeName)
        throws RiceIllegalArgumentException;

    /**
     * Gets a list of Rules with the specified templateId and documentTypeName.  Scales up the hierarchy of
     * documentTypes
     *
     * @param templateName unique name for the Rule Template.  Cannot be null or empty
     * @param documentTypeName documentTypeName for Rule.  Cannot be null or empty
     * @param effectiveDate date for rule effectiveness. Can be null.  If null, current time is used.
     *
     * @return Rules with the passed in templateId, documentTypeName (or parent document type)or an empty list if none exist
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code id} is null
     */
    @WebMethod(operationName = "getRulesByTemplateNameAndDocumentTypeNameAndEffectiveDate")
    @WebResult(name = "rules")
    @XmlElementWrapper(name = "rules", required = true)
    @XmlElement(name = "rule", required = true)
	List<Rule> getRulesByTemplateNameAndDocumentTypeNameAndEffectiveDate(@WebParam(name = "templateName") String templateName,
            @WebParam(name = "documentTypeName") String documentTypeName,
            @XmlJavaTypeAdapter(value = DateTimeAdapter.class) @WebParam(name = "effectiveDate") DateTime effectiveDate)
        throws RiceIllegalArgumentException;

    /**
     * Query for rules based on the given search criteria which is a Map of rule field names to values.
     *
     * <p>
     * This method returns it's results as a List of Rules that match the given search criteria.
     * </p>
     *
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return a list of Rule objects in which the given criteria match Rule properties.  An empty list is returned if an invalid or
     *         non-existent criteria is supplied.
     */
    @WebMethod(operationName = "findRules")
    @WebResult(name = "findRules")
    RuleQueryResults findRules(@WebParam(name = "query") QueryByCriteria queryByCriteria)
        throws RiceIllegalArgumentException;

    /**
     * Executes a simulation of a document to get all previous and future route information
     *
     * @param reportCriteria criteria for the rule report to follow
     *
     * @return list of Rules representing the results of the rule report
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code reportCriteria} is null
     */
    @WebMethod(operationName = "ruleReport")
    @WebResult(name = "rules")
    @XmlElementWrapper(name = "rules", required = true)
    @XmlElement(name = "rule", required = true)
    List<Rule> ruleReport(
            @WebParam(name = "ruleCriteria") RuleReportCriteria reportCriteria)
            throws RiceIllegalArgumentException;


    /**
     * gets a RuleTemplate identified by the passed in id
     *
     * @param id unique id for the RuleTemplate
     *
     * @return RuleTemplate with the passed in unique id
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code id} is null
     * @throws org.kuali.rice.core.api.exception.RiceIllegalStateException if RuleTemplate does not exist
     */
    @WebMethod(operationName = "getRuleTemplate")
    @WebResult(name = "ruleTemplate")
    @Cacheable(value=RuleTemplate.Cache.NAME, key="'id=' + #p0")
    RuleTemplate getRuleTemplate(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * gets a RuleTemplate identified by the passed in name
     *
     * @param name unique name for the RuleTemplate
     *
     * @return RuleTemplate with the passed in unique name
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code name} is null
     * @throws org.kuali.rice.core.api.exception.RiceIllegalStateException if RuleTemplate does not exist
     */
    @WebMethod(operationName = "getRuleTemplateByName")
    @WebResult(name = "ruleTemplate")
    @Cacheable(value=RuleTemplate.Cache.NAME, key="'name=' + #p0")
    RuleTemplate getRuleTemplateByName(@WebParam(name = "name") String name) throws RiceIllegalArgumentException;

    /**
     * Query for rules based on the given search criteria which is a Map of ruleTemplate field names to values.
     *
     * <p>
     * This method returns it's results as a List of RuleTemplates that match the given search criteria.
     * </p>
     *
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return a list of RuleTemplate objects in which the given criteria match RuleTemplate properties.
     * An empty list is returned if an invalid or non-existent criteria is supplied.
     */
    @WebMethod(operationName = "findRuleTemplates")
    @WebResult(name = "findRuleTemplates")
    RuleTemplateQueryResults findRuleTemplates(@WebParam(name = "query") QueryByCriteria queryByCriteria)
        throws RiceIllegalArgumentException;

    /**
     * gets a RuleResponsibility identified by the passed in responsibilityId
     *
     * @param responsibilityId unique id for the RuleResponsibility
     *
     * @return RuleResponsibility with the passed in unique responsibilityId
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code id} is null
     * @throws org.kuali.rice.core.api.exception.RiceIllegalStateException if RuleResponsibility does not exist
     */
    @WebMethod(operationName = "getRuleResponsibility")
    @WebResult(name = "ruleResponsibility")
    @Cacheable(value=RuleResponsibility.Cache.NAME, key="'responsibilityId=' + #p0")
    RuleResponsibility getRuleResponsibility(@WebParam(name = "responsibilityId") String responsibilityId) throws RiceIllegalArgumentException;

    /**
     * gets a RuleDelegations identified by the passed in id for responsibility
     *
     * @param id unique id for the RuleDelegation's Responsibility
     *
     * @return List of RuleDelegations with the provided ReponsibilityId.  Returns an empty list if none exist.
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code id} is null
     */
    @WebMethod(operationName = "getRuleDelegationsByResponsiblityId")
    @XmlElementWrapper(name = "ruleDelegations", required = true)
    @XmlElement(name = "ruleDelegation", required = false)
    @WebResult(name = "ruleDelegations")
    @Cacheable(value=RuleDelegation.Cache.NAME, key="'id=' + #p0")
	List<RuleDelegation> getRuleDelegationsByResponsibiltityId(@WebParam(name="id") String id)
        throws RiceIllegalArgumentException;
}

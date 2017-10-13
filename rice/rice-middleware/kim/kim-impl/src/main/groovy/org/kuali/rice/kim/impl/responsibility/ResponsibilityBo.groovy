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
package org.kuali.rice.kim.impl.responsibility;


import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Type
import org.kuali.rice.kim.api.responsibility.Responsibility
import org.kuali.rice.kim.api.responsibility.ResponsibilityContract
import org.kuali.rice.kim.api.services.KimApiServiceLocator
import org.kuali.rice.kim.api.type.KimType
import org.kuali.rice.kim.api.type.KimTypeAttribute
import org.kuali.rice.kim.api.type.KimTypeInfoService
import org.kuali.rice.kim.impl.common.attribute.KimAttributeDataBo
import org.kuali.rice.kim.impl.role.RoleResponsibilityBo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.krad.service.DataDictionaryService
import org.kuali.rice.krad.service.KRADServiceLocatorWeb
import org.springframework.util.AutoPopulatingList
import org.kuali.rice.kim.api.common.template.TemplateContract
import org.kuali.rice.kim.api.KimConstants
import javax.persistence.Transient

@Entity
@Table(name = "KRIM_RSP_T")
public class ResponsibilityBo extends PersistableBusinessObjectBase implements ResponsibilityContract {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RSP_ID")
    String id

    @Column(name="NMSPC_CD")
    String namespaceCode

    @Column(name="NM")
    String name

    @Column(name="DESC_TXT", length=400)
    String description;

    @Column(name="PERM_TMPL_ID")
    String templateId

    @Column(name="ACTV_IND")
    @Type(type="yes_no")
    boolean active

    @OneToOne(targetEntity=ResponsibilityTemplateBo.class,cascade=[],fetch=FetchType.EAGER)
    @JoinColumn(name="PERM_TMPL_ID", insertable=false, updatable=false)
    ResponsibilityTemplateBo template = new ResponsibilityTemplateBo();

    @OneToMany(targetEntity=ResponsibilityAttributeBo.class,cascade=[CascadeType.ALL],fetch=FetchType.EAGER,mappedBy="id")
    @Fetch(value = FetchMode.SELECT)
    List<ResponsibilityAttributeBo> attributeDetails = new AutoPopulatingList(ResponsibilityAttributeBo.class)

    @OneToMany(targetEntity=RoleResponsibilityBo.class,cascade=[CascadeType.ALL],fetch=FetchType.EAGER,mappedBy="id")
    @Fetch(value = FetchMode.SELECT)
    List<RoleResponsibilityBo> roleResponsibilities = new AutoPopulatingList(RoleResponsibilityBo.class)

    Map<String,String> attributes

    Map<String,String> getAttributes() {
        return attributeDetails != null ? KimAttributeDataBo.toAttributes(attributeDetails) : attributes
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    static Responsibility to(ResponsibilityBo bo) {
        if (bo == null) {
            return null
        }

        return Responsibility.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    static ResponsibilityBo from(Responsibility im) {
        if (im == null) {
            return null
        }

        ResponsibilityBo bo = new ResponsibilityBo()
        bo.id = im.id
        bo.namespaceCode = im.namespaceCode
        bo.name = im.name
        bo.description = im.description
        bo.active = im.active
        bo.templateId = im.template != null ? im.template.getId() : null;
        bo.template = ResponsibilityTemplateBo.from(im.template)
        bo.attributes = im.attributes
        bo.versionNumber = im.versionNumber
		bo.objectId = im.objectId;

        return bo
    }

    ResponsibilityTemplateBo getTemplate() {
       return template;
    }

    //FIXME: temporary methods
    String getDetailObjectsValues(){
		return attributeDetails.collect {it.attributeValue}.join(KimConstants.KimUIConstants.COMMA_SEPARATOR)
	}

    String getDetailObjectsToDisplay() {
		final KimType kimType = getTypeInfoService().getKimType( getTemplate().getKimTypeId() );

        return attributeDetails.collect {
            getKimAttributeLabelFromDD(kimType.getAttributeDefinitionById(it.kimAttributeId)) + ":" + it.attributeValue
        }.join(KimConstants.KimUIConstants.COMMA_SEPARATOR)
	}

    private String getKimAttributeLabelFromDD( KimTypeAttribute attribute ){
    	return getDataDictionaryService().getAttributeLabel(attribute.getKimAttribute().getComponentName(), attribute.getKimAttribute().getAttributeName() );
    }

	private DataDictionaryService getDataDictionaryService() {
		return KRADServiceLocatorWeb.getDataDictionaryService();
	}

	private KimTypeInfoService getTypeInfoService() {
	    return KimApiServiceLocator.getKimTypeInfoService();
	}
  }

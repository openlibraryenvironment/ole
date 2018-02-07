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
package org.kuali.rice.kim.bo.ui;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimAttributeField;
import org.kuali.rice.kim.bo.impl.KimAttributes;
import org.kuali.rice.kim.framework.services.KimFrameworkServiceLocator;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.role.RoleResponsibilityBo;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.springframework.util.AutoPopulatingList;
import org.springframework.util.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */

@Entity
@IdClass(org.kuali.rice.kim.bo.ui.PersonDocumentRoleId.class)
@Table(name="KRIM_PND_ROLE_MT",uniqueConstraints=@UniqueConstraint(columnNames={"FDOC_NBR", "ROLE_ID"}))
public class PersonDocumentRole extends KimDocumentBoActivatableEditableBase {
    private static final Logger LOG = Logger.getLogger(PersonDocumentRole.class);
	private static final long serialVersionUID = 4908044213007222739L;
	@Id
	@Column(name="ROLE_ID")
	protected String roleId;
	@Column(name="KIM_TYP_ID")
	protected String kimTypeId;
	@Column(name="ROLE_NM")
	protected String roleName;
	@Transient
	protected RoleBo roleBo;
	@Column(name="NMSPC_CD")
	protected String namespaceCode;
	@Transient
	protected KimTypeBo kimRoleType;
	@Transient
	protected List<? extends KimAttributes> attributes;
	@Transient
	protected transient List<KimAttributeField> definitions;
	@Transient
	protected transient Map<String,Object> attributeEntry;
	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @Fetch(value = FetchMode.SELECT)
	@JoinColumns({
		@JoinColumn(name="ROLE_ID",insertable=false,updatable=false),
		@JoinColumn(name="FDOC_NBR", insertable=false, updatable=false)
	})
	protected List<KimDocumentRoleMember> rolePrncpls;
	@Transient
    protected KimDocumentRoleMember newRolePrncpl;
	//currently mapped as manyToMany even though it is technically a OneToMany
	//The reason for this is it is linked with a partial Composite-id, which technically can't 
	//guarantee uniqueness.  
	@ManyToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name="ROLE_ID",insertable=false,updatable=false)
    //@JoinColumns({
	//	@JoinColumn(name="ROLE_ID",insertable=false,updatable=false),
	//	@JoinColumn(name="FDOC_NBR", insertable=false, updatable=false, table="KRIM_PERSON_DOCUMENT_T")
	//})
	protected List<RoleResponsibilityBo> assignedResponsibilities = new AutoPopulatingList(RoleResponsibilityBo.class);

	@Transient
    protected boolean isEditable = true;
    
	public PersonDocumentRole() {
		attributes = new ArrayList<KimAttributes>();	
		rolePrncpls = new ArrayList<KimDocumentRoleMember>();	
		attributeEntry = new HashMap<String,Object>();
	}
	
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getKimTypeId() {
		return this.kimTypeId;
	}

	public void setKimTypeId(String kimTypeId) {
		this.kimTypeId = kimTypeId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<? extends KimAttributes> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(List<? extends KimAttributes> attributes) {
		this.attributes = attributes;
	}

	public KimTypeBo getKimRoleType() {
		if ( kimRoleType == null && StringUtils.hasText(kimTypeId)) {
			kimRoleType = KimTypeBo.from(KimApiServiceLocator.getKimTypeInfoService().getKimType(kimTypeId));
		}
		return kimRoleType;
	}

    public Map<String, KimAttributeField> getDefinitionsKeyedByAttributeName() {
        final Map<String, KimAttributeField> map = new HashMap<String, KimAttributeField>();
        for (KimAttributeField field : getDefinitions()) {
            map.put(field.getAttributeField().getName(), field);
        }
        return map;
    }

	public List<KimAttributeField> getDefinitions() {
		if (definitions == null || definitions.isEmpty()) {
	        KimTypeService kimTypeService = KimFrameworkServiceLocator.getKimTypeService(KimTypeBo.to(
                    this.getKimRoleType()));
	        //it is possible that the the roleTypeService is coming from a remote application 
	        // and therefore it can't be guarenteed that it is up and working, so using a try/catch to catch this possibility.
	        try {
    	        if ( kimTypeService != null ) {
    	        	definitions = kimTypeService.getAttributeDefinitions(getKimTypeId());
    	        } else {
    	        	definitions = Collections.emptyList();
    	        }
    		} catch (Exception ex) {
                LOG.warn("Not able to retrieve KimTypeService from remote system for KIM Role Type: " + this.getKimRoleType(), ex);
            }
		}
		
		return definitions;
	}

	public void setDefinitions(List<KimAttributeField> definitions) {
		this.definitions = definitions;
	}

	public Map<String,Object> getAttributeEntry() {
		if (attributeEntry == null || attributeEntry.isEmpty()) {
			attributeEntry = KIMServiceLocatorInternal.getUiDocumentService().getAttributeEntries(getDefinitions());
		}
		
		return this.attributeEntry;
	}

	public void setAttributeEntry(Map<String,Object> attributeEntry) {
		this.attributeEntry = attributeEntry;
	}

	public List<KimDocumentRoleMember> getRolePrncpls() {
		return this.rolePrncpls;
	}

	public void setRolePrncpls(List<KimDocumentRoleMember> rolePrncpls) {
		this.rolePrncpls = rolePrncpls;
	}

	public KimDocumentRoleMember getNewRolePrncpl() {
		return this.newRolePrncpl;
	}

	public void setNewRolePrncpl(KimDocumentRoleMember newRolePrncpl) {
		this.newRolePrncpl = newRolePrncpl;
	}

	public String getNamespaceCode() {
		return this.namespaceCode;
	}

	public void setNamespaceCode(String namespaceCode) {
		this.namespaceCode = namespaceCode;
	}

	public List<RoleResponsibilityBo> getAssignedResponsibilities() {
		return this.assignedResponsibilities;
	}

	public void setAssignedResponsibilities(
			List<RoleResponsibilityBo> assignedResponsibilities) {
		this.assignedResponsibilities = assignedResponsibilities;
	}

	/**
	 * @return the roleBo
	 */
	public RoleBo getRoleBo() {
		return this.roleBo;
	}

	/**
	 * @param roleBo the roleBo to set
	 */
	public void setRoleBo(RoleBo roleBo) {
		this.roleBo = roleBo;
	}

	/**
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return this.isEditable;
	}

	/**
	 * @param isEditable the isEditable to set
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

}

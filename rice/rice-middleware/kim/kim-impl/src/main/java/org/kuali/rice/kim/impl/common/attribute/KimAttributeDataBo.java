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
package org.kuali.rice.kim.impl.common.attribute;

import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.common.attribute.KimAttributeDataContract;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeAttribute;
import org.kuali.rice.kim.api.type.KimTypeInfoService;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class KimAttributeDataBo extends PersistableBusinessObjectBase implements KimAttributeDataContract {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(KimAttributeDataBo.class);
    private static final long serialVersionUID = 1L;

    private static KimTypeInfoService kimTypeInfoService;

    private String id;
    private String attributeValue;
    private String kimAttributeId;
    private KimAttributeBo kimAttribute;
    private String kimTypeId;
    @Transient
    private KimTypeBo kimType;

    public abstract void setAssignedToId(String s);

    @Override
    public KimAttributeBo getKimAttribute() {
        if(ObjectUtils.isNull(this.kimAttribute)
                && StringUtils.isNotBlank(kimAttributeId)) {
            this.refreshReferenceObject("kimAttribute");
        }
        return kimAttribute;
    }

    @Override
    public KimTypeBo getKimType() {
        if (kimType == null && StringUtils.isNotEmpty(id)) {
            kimType = KimTypeBo.from(KimApiServiceLocator.getKimTypeInfoService().getKimType(kimTypeId));
        }
        return kimType;
    }

    public static <T extends KimAttributeDataBo> Map<String, String> toAttributes(Collection<T> bos) {
        Map<String, String> m = new HashMap<String, String>();
        if(CollectionUtils.isNotEmpty(bos)) {
            for (T it : bos) {
                if (it != null) {
                    KimTypeAttribute attribute = null;
                    if ( it.getKimType() != null ) {
                        attribute = KimTypeBo.to(it.getKimType()).getAttributeDefinitionById(it.getKimAttributeId());
                    }
                    if ( attribute != null ) {
                        m.put(attribute.getKimAttribute().getAttributeName(), it.getAttributeValue());
                    } else {
                        m.put(it.getKimAttribute().getAttributeName(), it.getAttributeValue());
                    }
                }
            }
        }
        return m;
    }

    /** creates a list of KimAttributeDataBos from attributes, kimTypeId, and assignedToId. */
    public static <T extends KimAttributeDataBo> List<T> createFrom(Class<T> type, Map<String, String> attributes, String kimTypeId) {
        if (attributes == null) {
            //purposely not using Collections.emptyList() b/c we do not want to return an unmodifiable list.
            return new ArrayList<T>();
        }
        List<T> attrs = new ArrayList<T>();
        for (Map.Entry<String, String> it : attributes.entrySet()) {
        //return attributes.entrySet().collect {
            KimTypeAttribute attr = getKimTypeInfoService().getKimType(kimTypeId).getAttributeDefinitionByName(it.getKey());
            KimType theType = getKimTypeInfoService().getKimType(kimTypeId);
            if (attr != null && StringUtils.isNotBlank(it.getValue())) {
                try {
                    T newDetail = type.newInstance();
                    newDetail.setKimAttributeId(attr.getKimAttribute().getId());
                    newDetail.setKimAttribute(KimAttributeBo.from(attr.getKimAttribute()));
                    newDetail.setKimTypeId(kimTypeId);
                    newDetail.setKimType(KimTypeBo.from(theType));
                    newDetail.setAttributeValue(it.getValue());
                    attrs.add(newDetail);
                } catch (InstantiationException e) {
                    LOG.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    LOG.error(e.getMessage(), e);
                }
                
            }
        }
        return attrs;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getKimAttributeId() {
        return kimAttributeId;
    }

    public void setKimAttributeId(String kimAttributeId) {
        this.kimAttributeId = kimAttributeId;
    }

    @Override
    public String getKimTypeId() {
        return kimTypeId;
    }

    public void setKimTypeId(String kimTypeId) {
        this.kimTypeId = kimTypeId;
    }

    public void setKimType(KimTypeBo kimType) {
        this.kimType = kimType;
    }
    
    public void setKimAttribute(KimAttributeBo kimAttribute) {
        this.kimAttribute = kimAttribute;
    }


    public static KimTypeInfoService getKimTypeInfoService() {
        if (kimTypeInfoService==null) {
            kimTypeInfoService = KimApiServiceLocator.getKimTypeInfoService();
        }
        return kimTypeInfoService;
    }

    public static void setKimTypeInfoService(KimTypeInfoService kimTypeInfoService) {
        KimAttributeDataBo.kimTypeInfoService = kimTypeInfoService;
    }

}

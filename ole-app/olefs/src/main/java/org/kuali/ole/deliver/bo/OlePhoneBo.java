package org.kuali.ole.deliver.bo;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.api.OlePhoneContract;
import org.kuali.ole.deliver.api.OlePhoneDefinition;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by angelind on 10/6/15.
 */
public class OlePhoneBo extends PersistableBusinessObjectBase implements OlePhoneContract{

    private String olePhoneId;
    private String olePatronId;
    private String id;
    private String phoneSource;
    private String phoneSourceName;
    private OleAddressSourceBo olePhoneSourceBo = new OleAddressSourceBo();
    private EntityPhoneBo entityPhoneBo;
    private OlePatronDocument olePatronDocument;

    public String getOlePhoneId() {
        return olePhoneId;
    }

    public void setOlePhoneId(String olePhoneId) {
        this.olePhoneId = olePhoneId;
    }

    public String getOlePatronId() {
        return olePatronId;
    }

    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    public String getPhoneSource() {
        return phoneSource;
    }

    public void setPhoneSource(String phoneSource) {
        if (phoneSource!=null && !phoneSource.equals("")) {
            this.phoneSource = phoneSource;
        } else {
            this.phoneSource = null;
        }
    }

    public String getPhoneSourceName() {
        return phoneSourceName;
    }

    public void setPhoneSourceName(String phoneSourceName) {
        this.phoneSourceName = phoneSourceName;
    }

    public OleAddressSourceBo getOlePhoneSourceBo() {
        return olePhoneSourceBo;
    }

    public void setOlePhoneSourceBo(OleAddressSourceBo olePhoneSourceBo) {
        this.olePhoneSourceBo = olePhoneSourceBo;
    }

    public EntityPhoneBo getEntityPhoneBo() {
        return entityPhoneBo;
    }

    public void setEntityPhoneBo(EntityPhoneBo entityPhoneBo) {
        this.entityPhoneBo = entityPhoneBo;
    }

    public OlePatronDocument getOlePatronDocument() {
        if (null == olePatronDocument) {
            String patronId = getOlePatronId();
            if (StringUtils.isNotEmpty(patronId)) {
                Map<String, String> parameterMap = new HashMap<>();
                parameterMap.put("olePatronId", patronId);
                olePatronDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, parameterMap);
            }
        }
        return olePatronDocument;
    }

    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    public static OlePhoneDefinition to(OlePhoneBo bo) {
        if (bo == null) {
            return null;
        }
        return OlePhoneDefinition.Builder.create(bo).build();
    }

    public static OlePhoneBo from(OlePhoneDefinition imOlePhoneDefinition) {
        if (imOlePhoneDefinition == null) {
            return null;
        }
        OlePhoneBo bo = new OlePhoneBo();
        bo.olePhoneId = imOlePhoneDefinition.getOlePhoneId();
        bo.olePatronId = imOlePhoneDefinition.getOlePatronId();
        bo.id = imOlePhoneDefinition.getId();
        bo.phoneSource = imOlePhoneDefinition.getPhoneSource();
        bo.versionNumber = imOlePhoneDefinition.getVersionNumber();
        if (imOlePhoneDefinition.getOlePhoneSourceBo() != null) {
            bo.olePhoneSourceBo = OleAddressSourceBo.from(imOlePhoneDefinition.getOlePhoneSourceBo());
        }
        return bo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

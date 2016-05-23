package org.kuali.ole.deliver.bo;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.api.OleEmailContract;
import org.kuali.ole.deliver.api.OleEmailDefinition;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by angelind on 10/13/15.
 */
public class OleEmailBo extends PersistableBusinessObjectBase implements OleEmailContract {

    private String oleEmailId;
    private String olePatronId;
    private String id;
    private String emailSource;
    private String emailSourceName;
    private OleAddressSourceBo oleEmailSourceBo = new OleAddressSourceBo();
    private EntityEmailBo entityEmailBo;
    private OlePatronDocument olePatronDocument;

    public String getOleEmailId() {
        return oleEmailId;
    }

    public void setOleEmailId(String oleEmailId) {
        this.oleEmailId = oleEmailId;
    }

    public String getOlePatronId() {
        return olePatronId;
    }

    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    public String getEmailSource() {
        return emailSource;
    }

    public void setEmailSource(String emailSource) {
        if (emailSource!=null&&!emailSource.equals("")) {
            this.emailSource = emailSource;
        } else {
            this.emailSource = null;
        }
    }

    public String getEmailSourceName() {
        return emailSourceName;
    }

    public void setEmailSourceName(String emailSourceName) {
        this.emailSourceName = emailSourceName;
    }

    public OleAddressSourceBo getOleEmailSourceBo() {
        return oleEmailSourceBo;
    }

    public void setOleEmailSourceBo(OleAddressSourceBo oleEmailSourceBo) {
        this.oleEmailSourceBo = oleEmailSourceBo;
    }

    public EntityEmailBo getEntityEmailBo() {
        return entityEmailBo;
    }

    public void setEntityEmailBo(EntityEmailBo entityEmailBo) {
        this.entityEmailBo = entityEmailBo;
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

    public static OleEmailDefinition to(OleEmailBo bo) {
        if (bo == null) {
            return null;
        }
        return OleEmailDefinition.Builder.create(bo).build();
    }

    public static OleEmailBo from(OleEmailDefinition imOleEmailDefinition) {
        if (imOleEmailDefinition == null) {
            return null;
        }
        OleEmailBo bo = new OleEmailBo();
        bo.oleEmailId = imOleEmailDefinition.getOleEmailId();
        bo.olePatronId = imOleEmailDefinition.getOlePatronId();
        bo.id = imOleEmailDefinition.getId();
        bo.emailSource = imOleEmailDefinition.getEmailSource();
        bo.versionNumber = imOleEmailDefinition.getVersionNumber();
        if (imOleEmailDefinition.getOleEmailSourceBo() != null) {
            bo.oleEmailSourceBo = OleAddressSourceBo.from(imOleEmailDefinition.getOleEmailSourceBo());
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

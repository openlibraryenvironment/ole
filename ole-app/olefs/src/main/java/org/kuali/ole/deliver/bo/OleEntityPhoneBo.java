package org.kuali.ole.deliver.bo;

import org.kuali.ole.deliver.api.OleEntityPhoneContract;
import org.kuali.ole.deliver.api.OleEntityPhoneDefinition;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.krad.bo.BusinessObjectBase;

/**
 * Created by angelind on 10/6/15.
 */
public class OleEntityPhoneBo extends BusinessObjectBase implements OleEntityPhoneContract{

    private OlePhoneBo olePhoneBo;
    private EntityPhoneBo entityPhoneBo;

    public OlePhoneBo getOlePhoneBo() {
        return olePhoneBo;
    }

    public void setOlePhoneBo(OlePhoneBo olePhoneBo) {
        this.olePhoneBo = olePhoneBo;
    }

    public EntityPhoneBo getEntityPhoneBo() {
        return entityPhoneBo;
    }

    public void setEntityPhoneBo(EntityPhoneBo entityPhoneBo) {
        this.entityPhoneBo = entityPhoneBo;
    }

    public static OleEntityPhoneDefinition to(OleEntityPhoneBo bo) {
        if (bo == null) {
            return null;
        }
        return OleEntityPhoneDefinition.Builder.create(bo).build();
    }

    public static OleEntityPhoneBo from(OleEntityPhoneDefinition im) {
        if (im == null) {
            return null;
        }

        OleEntityPhoneBo bo = new OleEntityPhoneBo();

        if (im.getOlePhoneBo() != null) {
            bo.olePhoneBo = OlePhoneBo.from(im.getOlePhoneBo());
        }

        if (im.getEntityPhoneBo() != null) {
            bo.entityPhoneBo = EntityPhoneBo.from(im.getEntityPhoneBo());
        }

        return bo;
    }

    @Override
    public void refresh() {

    }
}

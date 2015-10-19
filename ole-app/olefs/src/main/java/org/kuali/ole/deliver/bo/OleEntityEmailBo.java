package org.kuali.ole.deliver.bo;

import org.kuali.ole.deliver.api.OleEntityEmailContract;
import org.kuali.ole.deliver.api.OleEntityEmailDefinition;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.krad.bo.BusinessObjectBase;

/**
 * Created by angelind on 10/13/15.
 */
public class OleEntityEmailBo extends BusinessObjectBase implements OleEntityEmailContract {

    private OleEmailBo oleEmailBo;
    private EntityEmailBo entityEmailBo;

    public OleEmailBo getOleEmailBo() {
        return oleEmailBo;
    }

    public void setOleEmailBo(OleEmailBo oleEmailBo) {
        this.oleEmailBo = oleEmailBo;
    }

    public EntityEmailBo getEntityEmailBo() {
        return entityEmailBo;
    }

    public void setEntityEmailBo(EntityEmailBo entityEmailBo) {
        this.entityEmailBo = entityEmailBo;
    }

    public static OleEntityEmailDefinition to(OleEntityEmailBo bo) {
        if (bo == null) {
            return null;
        }
        return OleEntityEmailDefinition.Builder.create(bo).build();
    }

    public static OleEntityEmailBo from(OleEntityEmailDefinition im) {
        if (im == null) {
            return null;
        }

        OleEntityEmailBo bo = new OleEntityEmailBo();

        if (im.getOleEmailBo() != null) {
            bo.oleEmailBo = OleEmailBo.from(im.getOleEmailBo());
        }

        if (im.getEntityEmailBo() != null) {
            bo.entityEmailBo = EntityEmailBo.from(im.getEntityEmailBo());
        }

        return bo;
    }

    @Override
    public void refresh() {

    }
}

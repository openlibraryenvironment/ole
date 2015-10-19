package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.phone.EntityPhoneContract;
import org.kuali.rice.krms.api.KrmsConstants;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by angelind on 10/9/15.
 */
@XmlRootElement(name = OleEntityPhoneDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OleEntityPhoneDefinition.Constants.TYPE_NAME, propOrder = {
        OleEntityPhoneDefinition.Elements.OLE_PHONE,
        OleEntityPhoneDefinition.Elements.ENTITY_PHONE
})
public class OleEntityPhoneDefinition extends AbstractDataTransferObject implements OleEntityPhoneContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.OLE_PHONE, required = false)
    private final OlePhoneDefinition olePhoneBo;

    @XmlElement(name = Elements.ENTITY_PHONE, required = false)
    private final EntityPhone entityPhoneBo;

    public OleEntityPhoneDefinition() {
        olePhoneBo = null;
        entityPhoneBo = null;
    }

    public OleEntityPhoneDefinition(Builder builder) {
        olePhoneBo = builder.getOlePhoneBo().build();
        entityPhoneBo = builder.getEntityPhoneBo().build();

    }

    @Override
    public EntityPhone getEntityPhoneBo() {
        return entityPhoneBo;
    }

    @Override
    public OlePhoneDefinition getOlePhoneBo() {
        return olePhoneBo;
    }

    public static class Builder implements OleEntityPhoneContract, ModelBuilder, Serializable {
        private static final long serialVersionUID = 1L;

        public OlePhoneDefinition.Builder olePhoneBo;
        public EntityPhone.Builder entityPhoneBo;

        public String patronNoteId;
        public String olePatronId;
        public String patronNoteTypeId;
        public String patronNoteText;
        public OlePatronNoteTypeDefinition.Builder olePatronNoteType;
        public OlePatronDefinition.Builder olePatron;
        public Long versionNumber;
        public boolean active;
        public String objectId;

        private Builder(OlePhoneDefinition.Builder olePhoneBo, EntityPhone.Builder entityPhoneBo) {
            setOlePhoneBo(olePhoneBo);
            setEntityPhoneBo(entityPhoneBo);
        }

        public static Builder create(OlePhoneDefinition.Builder olePhoneBo, EntityPhone.Builder entityPhoneBo) {
            return new Builder(olePhoneBo, entityPhoneBo);
        }

        public static Builder create(OleEntityPhoneContract oleEntityPhoneContract) {
            Builder builder = Builder.create(OlePhoneDefinition.Builder.create(oleEntityPhoneContract.getOlePhoneBo()), EntityPhone.Builder.create(oleEntityPhoneContract.getEntityPhoneBo()));
            return builder;
        }

        @Override
        public OlePhoneDefinition.Builder getOlePhoneBo() {
            return this.olePhoneBo;
        }


        public EntityPhone.Builder getEntityPhoneBo() {
            return this.entityPhoneBo;
        }

        public void setOlePhoneBo(OlePhoneDefinition.Builder olePhoneBo) {
            this.olePhoneBo = olePhoneBo;
        }

        public void setEntityPhoneBo(EntityPhone.Builder entityPhoneBo) {
            this.entityPhoneBo = entityPhoneBo;
        }

        @Override
        public OleEntityPhoneDefinition build() {
            return new OleEntityPhoneDefinition(this);
        }
    }

    static class Constants {
        public static final String ROOT_ELEMENT_NAME = "oleEntityPhoneDefinition";
        public static final String TYPE_NAME = "oleEntityPhoneDefinitionType";
    }

    static class Elements {
        public static final String OLE_PHONE = "olePhoneBo";
        public static final String ENTITY_PHONE = "entityPhoneBo";

    }

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}

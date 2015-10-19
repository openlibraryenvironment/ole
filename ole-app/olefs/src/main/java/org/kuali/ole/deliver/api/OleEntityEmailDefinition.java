package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.krms.api.KrmsConstants;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by angelind on 10/13/15.
 */
@XmlRootElement(name = OleEntityEmailDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OleEntityEmailDefinition.Constants.TYPE_NAME, propOrder = {
        OleEntityEmailDefinition.Elements.OLE_EMAIL,
        OleEntityEmailDefinition.Elements.ENTITY_EMAIL
})
public class OleEntityEmailDefinition extends AbstractDataTransferObject implements OleEntityEmailContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.OLE_EMAIL, required = false)
    private final OleEmailDefinition oleEmailBo;

    @XmlElement(name = Elements.ENTITY_EMAIL, required = false)
    private final EntityEmail entityEmailBo;

    public OleEntityEmailDefinition() {
        oleEmailBo = null;
        entityEmailBo = null;
    }

    public OleEntityEmailDefinition(Builder builder) {
        oleEmailBo = builder.getOleEmailBo().build();
        entityEmailBo = builder.getEntityEmailBo().build();

    }

    @Override
    public EntityEmail getEntityEmailBo() {
        return entityEmailBo;
    }

    @Override
    public OleEmailDefinition getOleEmailBo() {
        return oleEmailBo;
    }

    public static class Builder implements OleEntityEmailContract, ModelBuilder, Serializable {
        private static final long serialVersionUID = 1L;

        public OleEmailDefinition.Builder oleEmailBo;
        public EntityEmail.Builder entityEmailBo;

        public String patronNoteId;
        public String olePatronId;
        public String patronNoteTypeId;
        public String patronNoteText;
        public OlePatronNoteTypeDefinition.Builder olePatronNoteType;
        public OlePatronDefinition.Builder olePatron;
        public Long versionNumber;
        public boolean active;
        public String objectId;

        private Builder(OleEmailDefinition.Builder oleEmailBo, EntityEmail.Builder entityEmailBo) {
            setOleEmailBo(oleEmailBo);
            setEntityEmailBo(entityEmailBo);
        }

        public static Builder create(OleEmailDefinition.Builder oleEmailBo, EntityEmail.Builder entityEmailBo) {
            return new Builder(oleEmailBo, entityEmailBo);
        }

        public static Builder create(OleEntityEmailContract oleEntityEmailContract) {
            Builder builder = Builder.create(OleEmailDefinition.Builder.create(oleEntityEmailContract.getOleEmailBo()), EntityEmail.Builder.create(oleEntityEmailContract.getEntityEmailBo()));
            return builder;
        }

        @Override
        public OleEmailDefinition.Builder getOleEmailBo() {
            return oleEmailBo;
        }

        public EntityEmail.Builder getEntityEmailBo() {
            return this.entityEmailBo;
        }

        public void setOleEmailBo(OleEmailDefinition.Builder oleEmailBo) {
            this.oleEmailBo = oleEmailBo;
        }

        public void setEntityEmailBo(EntityEmail.Builder entityEmailBo) {
            this.entityEmailBo = entityEmailBo;
        }

        @Override
        public OleEntityEmailDefinition build() {
            return new OleEntityEmailDefinition(this);
        }
    }

    static class Constants {
        public static final String ROOT_ELEMENT_NAME = "oleEntityEmailDefinition";
        public static final String TYPE_NAME = "oleEntityEmailDefinitionType";
    }

    static class Elements {
        public static final String OLE_EMAIL = "oleEmailBo";
        public static final String ENTITY_EMAIL = "entityEmailBo";

    }

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}

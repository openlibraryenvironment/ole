package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.krms.api.KrmsConstants;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/28/12
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = OleEntityAddressDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OleEntityAddressDefinition.Constants.TYPE_NAME, propOrder = {
        OleEntityAddressDefinition.Elements.OLE_ADDRESS,
        OleEntityAddressDefinition.Elements.ENTITY_ADDRESS
})
public class OleEntityAddressDefinition extends AbstractDataTransferObject implements OleEntityAddressContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.OLE_ADDRESS, required = false)
    private final OleAddressDefinition oleAddressBo;
    @XmlElement(name = Elements.ENTITY_ADDRESS, required = false)
    private final EntityAddress entityAddressBo;

    public OleEntityAddressDefinition() {
        oleAddressBo = null;
        entityAddressBo = null;
    }

    public OleEntityAddressDefinition(Builder builder) {
        oleAddressBo = builder.getOleAddressBo().build();
        entityAddressBo = builder.getEntityAddressBo().build();

    }

    @Override
    public EntityAddress getEntityAddressBo() {
        return entityAddressBo;
    }

    @Override
    public OleAddressDefinition getOleAddressBo() {
        return oleAddressBo;
    }

    public static class Builder implements OleEntityAddressContract, ModelBuilder, Serializable {
        private static final long serialVersionUID = 1L;

        public OleAddressDefinition.Builder oleAddressBo;
        public EntityAddress.Builder entityAddressBo;

        public String patronNoteId;
        public String olePatronId;
        public String patronNoteTypeId;
        public String patronNoteText;
        public OlePatronNoteTypeDefinition.Builder olePatronNoteType;
        public OlePatronDefinition.Builder olePatron;
        public Long versionNumber;
        public boolean active;
        public String objectId;

        private Builder(OleAddressDefinition.Builder oleAddressBo, EntityAddress.Builder entityAddressBo) {

            setOleAddressBo(oleAddressBo);
            setEntityAddressBo(entityAddressBo);

        }

        public static Builder create(OleAddressDefinition.Builder oleAddressBo, EntityAddress.Builder entityAddressBo) {
            //OlePatronDefinition.Builder olePatron) {
            return new Builder(oleAddressBo, entityAddressBo);
        }

        public static Builder create(OleEntityAddressContract oleEntityAddressContract) {
            Builder builder = Builder.create(OleAddressDefinition.Builder.create(oleEntityAddressContract.getOleAddressBo()), EntityAddress.Builder.create(oleEntityAddressContract.getEntityAddressBo()));
            return builder;

        }


        @Override
        public OleAddressDefinition.Builder getOleAddressBo() {
            return this.oleAddressBo;
        }


        public EntityAddress.Builder getEntityAddressBo() {
            return this.entityAddressBo;
        }

        public void setOleAddressBo(OleAddressDefinition.Builder oleAddressBo) {
            this.oleAddressBo = oleAddressBo;
        }

        public void setEntityAddressBo(EntityAddress.Builder entityAddressBo) {
            this.entityAddressBo = entityAddressBo;
        }

        @Override
        public OleEntityAddressDefinition build() {
            return new OleEntityAddressDefinition(this);
        }
    }


    /*public OlePatronDefinition getOlePatron() {
        return this.olePatron;
    }*/
    static class Constants {
        public static final String ROOT_ELEMENT_NAME = "oleEntityAddressDefinition";
        public static final String TYPE_NAME = "oleEntityAddressDefinitionType";
    }

    static class Elements {
        public static final String OLE_ADDRESS = "oleAddressBo";
        public static final String ENTITY_ADDRESS = "entityAddressBo";

    }

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}

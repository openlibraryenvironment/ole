package org.kuali.ole.deliver.bo;

import org.kuali.ole.deliver.api.OleEntityAddressContract;
import org.kuali.ole.deliver.api.OleEntityAddressDefinition;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.krad.bo.BusinessObjectBase;

/**
 * OleEntityAddressBo provides OleEntityAddressBo information through getter and setter.
 */
public class OleEntityAddressBo extends BusinessObjectBase implements OleEntityAddressContract {

    private OleAddressBo oleAddressBo;

    private EntityAddressBo entityAddressBo;

    /**
     * Gets the value of entityAddressBo property
     *
     * @return entityAddressBo
     */
    public EntityAddressBo getEntityAddressBo() {
        return entityAddressBo;
    }

    /**
     * Sets the value for entityAddressBo property
     *
     * @param entityAddressBo
     */
    public void setEntityAddressBo(EntityAddressBo entityAddressBo) {
        this.entityAddressBo = entityAddressBo;
    }

    /**
     * Gets the value of oleAddressBo property
     *
     * @return oleAddressBo
     */
    public OleAddressBo getOleAddressBo() {
        return oleAddressBo;
    }

    /**
     * Sets the value for oleAddressBo property
     *
     * @param oleAddressBo
     */
    public void setOleAddressBo(OleAddressBo oleAddressBo) {
        this.oleAddressBo = oleAddressBo;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OleEntityAddressBo into immutable object OleEntityAddressDefinition
     *
     * @param bo
     * @return OleEntityAddressDefinition
     */
    public static OleEntityAddressDefinition to(org.kuali.ole.deliver.bo.OleEntityAddressBo bo) {
        if (bo == null) {
            return null;
        }
        return OleEntityAddressDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OleEntityAddressDefinition into PersistableBusinessObjectBase OleEntityAddressBo
     *
     * @param im
     * @return bo
     */
    public static org.kuali.ole.deliver.bo.OleEntityAddressBo from(OleEntityAddressDefinition im) {
        if (im == null) {
            return null;
        }

        org.kuali.ole.deliver.bo.OleEntityAddressBo bo = new org.kuali.ole.deliver.bo.OleEntityAddressBo();

        if (im.getOleAddressBo() != null) {
            bo.oleAddressBo = OleAddressBo.from(im.getOleAddressBo());
        }

        if (im.getEntityAddressBo() != null) {
            bo.entityAddressBo = EntityAddressBo.from(im.getEntityAddressBo());
        }

        return bo;
    }


    @Override
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
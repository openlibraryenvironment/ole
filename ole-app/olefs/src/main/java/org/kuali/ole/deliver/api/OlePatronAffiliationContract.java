package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationTypeContract;
import org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/25/12
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OlePatronAffiliationContract extends Versioned, Identifiable {

    public String getEntityAffiliationId();

    public String getAffiliationTypeCode();

    public String getCampusCode();

    public boolean isDefaultValue();

    public boolean isActive();

    public EntityAffiliationTypeContract getAffiliationType();

    //public EntityAffiliationContract getEntityAffliationBo();

    public List<? extends EntityEmploymentContract> getEmployments();

    public String getEntityId();

    public String getObjectId();
}
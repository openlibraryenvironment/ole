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
package org.kuali.rice.kim.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.criteria.CriteriaValue;
import org.kuali.rice.core.api.criteria.EqualPredicate;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.entity.EntityDefaultQueryResults;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences;
import org.kuali.rice.kim.dao.LdapPrincipalDao;
import org.kuali.rice.kim.impl.identity.IdentityServiceImpl;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;

import javax.jws.WebParam;

/**
 * Implementation of {@link IdentityService} that communicates with and serves information
 * from the UA Enterprise Directory Service.
 * 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LdapIdentityDelegateServiceImpl extends IdentityServiceImpl {
    private LdapPrincipalDao principalDao;

    @Override
	public Entity getEntity(String entityId) {
        if (StringUtils.isBlank(entityId)) {
            throw new RiceIllegalArgumentException("entityId is blank");
        }

        Entity edsInfo = getPrincipalDao().getEntity(entityId);
        if (edsInfo != null) {
            return edsInfo;
        } else {
            return super.getEntity(entityId);
        }
	}
	
	/**
	 * Overridden to populate this information from the LdapPrincipalDao
	 */
    @Override
	public Entity getEntityByPrincipalId(String principalId) {
        if (StringUtils.isBlank(principalId)) {
            throw new RiceIllegalArgumentException("principalId is blank");
        }

        Entity edsInfo = getPrincipalDao().getEntityByPrincipalId(principalId);
        if (edsInfo != null) {
            return edsInfo;
        } else {
            return super.getEntityByPrincipalId(principalId);
        }
	}
	
    @Override
	public Entity getEntityByPrincipalName(String principalName) {
        if (StringUtils.isBlank(principalName)) {
            throw new RiceIllegalArgumentException("principalName is blank");
        }

        final Entity edsInfo = getPrincipalDao().getEntityByPrincipalName(principalName);
        if (edsInfo != null) {
            return edsInfo;
        } else {
            return super.getEntityByPrincipalName(principalName);
        }
	}
	
    @Override
	public EntityDefault getEntityDefault(String entityId) {
        if (StringUtils.isBlank(entityId)) {
            throw new RiceIllegalArgumentException("entityId is blank");
        }

        EntityDefault edsInfo = getPrincipalDao().getEntityDefault(entityId);
        if (edsInfo != null) {
            return edsInfo;
        } 
        else {
            return super.getEntityDefault(entityId);
        }
	}
	
    @Override
	public EntityDefault getEntityDefaultByPrincipalId(String principalId) {
        if (StringUtils.isBlank(principalId)) {
            throw new RiceIllegalArgumentException("principalId is blank");
        }

        final EntityDefault retval = getPrincipalDao().getEntityDefaultByPrincipalId(principalId);
        if (retval != null) {
            return retval;
        }
        else {
            return super.getEntityDefaultByPrincipalId(principalId);
        }
	}
	
    @Override
	public EntityDefault getEntityDefaultByPrincipalName(String principalName) {
        if (StringUtils.isBlank(principalName)) {
            throw new RiceIllegalArgumentException("principalName is blank");
        }

        final EntityDefault retval = getPrincipalDao().getEntityDefaultByPrincipalName(principalName);
        if (retval != null) {
            return retval;
        }
        else {
            return super.getEntityDefaultByPrincipalName(principalName);
        }
	}
    
	
    private static <U extends CriteriaValue<?>> Object getVal(U toConv) {
        Object o = toConv.getValue();
        if (o instanceof DateTime) {
            return new Timestamp(((DateTime) o).getMillis());
        }
        return o;
    }
    
    /**
     * Password lookups not supported by EDS. Use Natural Authentication strategies instead
     * of this if that's what you need.
     *
     */
    @Override
    @Deprecated
	public Principal getPrincipalByPrincipalNameAndPassword(String principalName, String password) {
        if (StringUtils.isBlank(principalName)) {
            throw new RiceIllegalArgumentException("principalName is blank");
        }

        //not validating password

        return getPrincipalByPrincipalName(principalName);
    }
	
    @Override
	public EntityPrivacyPreferences getEntityPrivacyPreferences(String entityId) {
        if (StringUtils.isBlank(entityId)) {
            throw new RiceIllegalArgumentException("entityId is blank");
        }

        return getPrincipalDao().getEntityPrivacyPreferences(entityId);
	}

    @Override
	public Principal getPrincipal(String principalId) {
        if (StringUtils.isBlank(principalId)) {
            throw new RiceIllegalArgumentException("principalId is blank");
        }

        final Principal edsInfo = getPrincipalDao().getPrincipal(principalId);
            if (edsInfo != null) {
	        return edsInfo;
	    } else {
	        return super.getPrincipal(principalId);
	    }
    }

    /**
     * Gets a list of {@link org.kuali.rice.kim.api.identity.principal.Principal} from a string list of principalId.
     *
     * <p>
     * This method will only return principals that exist.  It will return null if the none of the principals exist.
     * </p>
     *
     * @param principalIds the unique id to retrieve the principal by. cannot be null.
     * @return a list of {@link org.kuali.rice.kim.api.identity.principal.Principal} or null
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if the principalId is blank
     */
    @Override
    public List<Principal> getPrincipals(@WebParam(name = "principalIds") List<String> principalIds) {
        List<Principal>  ret = new ArrayList<Principal>();
        for(String p: principalIds) {
            Principal principalInfo = getPrincipal(p);

            if (principalInfo != null) {
                ret.add(principalInfo) ;
            }
        }
        return ret;
    }

    @Override
	public Principal getPrincipalByPrincipalName(String principalName) {
        if (StringUtils.isBlank(principalName)) {
            throw new RiceIllegalArgumentException("principalName is blank");
        }

        final Principal edsInfo = getPrincipalDao().getPrincipalByName(principalName);
        if (edsInfo != null) {
            return edsInfo;
        } else {
            return super.getPrincipalByPrincipalName(principalName);
        }
    }

    public void setPrincipalDao(LdapPrincipalDao principalDao) {
        this.principalDao = principalDao;
    }

    public LdapPrincipalDao getPrincipalDao() {
        return principalDao;
    } 
}
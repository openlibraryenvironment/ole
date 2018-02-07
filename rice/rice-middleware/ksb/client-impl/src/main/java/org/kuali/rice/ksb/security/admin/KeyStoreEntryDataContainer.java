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
package org.kuali.rice.ksb.security.admin;

import java.io.Serializable;
import java.security.KeyStore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to hold displayable data for KeyStore entries 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KeyStoreEntryDataContainer implements Serializable {
    
    private static final String DISPLAYABLE_ENTRY_TYPE_TRUSTED_CERTIFICATE = "Trusted Certificate";
    private static final String DISPLAYABLE_ENTRY_TYPE_PRIVATE_KEY = "Private Key";
    private static final String DISPLAYABLE_ENTRY_TYPE_SECRET_KEY = "Secret Key";
    
    public static final Map<Class<? extends KeyStore.Entry>,String> DISPLAYABLE_ENTRY_TYPES = new HashMap<Class<? extends KeyStore.Entry>,String>();
    static {
        DISPLAYABLE_ENTRY_TYPES.put(KeyStore.TrustedCertificateEntry.class, DISPLAYABLE_ENTRY_TYPE_TRUSTED_CERTIFICATE);
        DISPLAYABLE_ENTRY_TYPES.put(KeyStore.PrivateKeyEntry.class, DISPLAYABLE_ENTRY_TYPE_PRIVATE_KEY);
        DISPLAYABLE_ENTRY_TYPES.put(KeyStore.SecretKeyEntry.class, DISPLAYABLE_ENTRY_TYPE_SECRET_KEY);
    }

    private String alias;
    private Date createDate;
    private Class<? extends KeyStore.Entry> type;
    
    public KeyStoreEntryDataContainer(String alias, Date createDate) {
        super();
        this.alias = alias;
        this.createDate = createDate;
    }
    
    public boolean isAllowsRemoval() {
        if (KeyStore.TrustedCertificateEntry.class.equals(type)) {
            return true;
        }
        return false;
    }
    
    public String getDisplayType() {
        return DISPLAYABLE_ENTRY_TYPES.get(getType());
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return this.alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return this.createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the type
     */
    public Class<? extends KeyStore.Entry> getType() {
        return this.type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Class<? extends KeyStore.Entry> type) {
        this.type = type;
    }
    
}

package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * Created by angelind on 10/6/15.
 */
public interface OlePhoneContract extends Versioned, Identifiable {

    public String getId();

    public String getOlePhoneId();

    public String getOlePatronId();

    public String getPhoneSource();

    public OleAddressSourceContract getOlePhoneSourceBo();
}

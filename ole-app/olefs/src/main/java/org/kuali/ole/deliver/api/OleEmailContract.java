package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * Created by angelind on 10/13/15.
 */
public interface OleEmailContract extends Versioned, Identifiable {

    public String getId();

    public String getOleEmailId();

    public String getOlePatronId();

    public String getEmailSource();

    public OleAddressSourceContract getOleEmailSourceBo();
}

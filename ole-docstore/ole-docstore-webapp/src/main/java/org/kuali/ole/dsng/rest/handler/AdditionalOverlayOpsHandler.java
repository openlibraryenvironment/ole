package org.kuali.ole.dsng.rest.handler;

import java.util.List;

/**
 * Created by SheikS on 3/21/2016.
 */
public abstract  class AdditionalOverlayOpsHandler {
    public abstract Boolean isInterested(String type);

    public abstract boolean isValid(String condition, List<String> values, Object object);
}

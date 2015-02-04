package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/25/12
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleStatisticalCategoryContract extends Versioned, Identifiable {

    public String getOleStatisticalCategoryId();

    public String getOleStatisticalCategoryCode();

    public String getOleStatisticalCategoryName();

    public String getOleStatisticalCategoryDesc();

    public boolean isActive();

}

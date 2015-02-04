package org.kuali.ole.service;


import org.kuali.ole.select.bo.OLEHoldingsRecord;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 4/11/14
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OLEHoldingsDao {

    public List<OLEHoldingsRecord> getHoldingsByCollection(Collection bibIds);
}

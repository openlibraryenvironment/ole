package org.kuali.ole.systemintegration.rest.bo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 3/12/14
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class HoldingsSerialHistory {

    Mains mains;
    Indexes indexes;
    Supplementaries supplementaries;

    public Mains getMains() {
        return mains;
    }

    public void setMains(Mains mains) {
        this.mains = mains;
    }

    public Indexes getIndexes() {
        return indexes;
    }

    public void setIndexes(Indexes indexes) {
        this.indexes = indexes;
    }

    public Supplementaries getSupplementaries() {
        return supplementaries;
    }

    public void setSupplementaries(Supplementaries supplementaries) {
        this.supplementaries = supplementaries;
    }
}

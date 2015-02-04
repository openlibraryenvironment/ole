package org.kuali.ole.systemintegration.rest.bo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 4/22/14
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class Supplementaries {

    List<SerialReceivingSupplement> supplementary;

    public List<SerialReceivingSupplement> getSupplementary() {
        return supplementary;
    }

    public void setSupplementary(List<SerialReceivingSupplement> supplementary) {
        this.supplementary = supplementary;
    }
}

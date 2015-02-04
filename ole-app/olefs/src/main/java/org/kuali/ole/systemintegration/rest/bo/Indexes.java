package org.kuali.ole.systemintegration.rest.bo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 4/22/14
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Indexes {

    List<SerialReceivingIndex> index;

    public List<SerialReceivingIndex> getIndex() {
        return index;
    }

    public void setIndex(List<SerialReceivingIndex> index) {
        this.index = index;
    }
}

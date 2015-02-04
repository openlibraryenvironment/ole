package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: htcuser
 * Date: 4/18/12
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class EDIOrders {
    private List<EDIOrder> orders = new ArrayList<EDIOrder>();

    public List<EDIOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<EDIOrder> orders) {
        this.orders = orders;
    }
}

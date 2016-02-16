package org.kuali.ole.docstore.common.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelind on 2/16/16.
 */
public class OrderResponse {

    private String processType;
    private List<OrderData> orderDatas;

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public List<OrderData> getOrderDatas() {
        if(null == orderDatas) {
            orderDatas = new ArrayList<>();
        }
        return orderDatas;
    }

    public void setOrderDatas(List<OrderData> orderDatas) {
        this.orderDatas = orderDatas;
    }

    public void addOrderData(OrderData orderData) {
        getOrderDatas().add(orderData);
    }
}

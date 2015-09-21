package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * Created by pvsubrah on 8/28/15.
 */
public class OLEForm extends UifFormBase {
    private DroolsExchange droolsExchange;

    public DroolsExchange getDroolsExchange() {
        if(null == droolsExchange){
            droolsExchange = new DroolsExchange();
        }
        return droolsExchange;
    }

    public void setDroolsExchange(DroolsExchange droolsExchange) {
        this.droolsExchange = droolsExchange;
    }
}

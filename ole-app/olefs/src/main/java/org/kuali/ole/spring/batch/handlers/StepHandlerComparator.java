package org.kuali.ole.spring.batch.handlers;

import java.util.Comparator;

/**
 * Created by pvsubrah on 12/21/15.
 */
public class StepHandlerComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        StepHandler sh1 = (StepHandler) o1;
        StepHandler sh2 = (StepHandler) o2;

        return sh1.getBatchProfileDataTransformer().getStep().compareTo(((StepHandler) o2).getBatchProfileDataTransformer().getStep());
    }
}

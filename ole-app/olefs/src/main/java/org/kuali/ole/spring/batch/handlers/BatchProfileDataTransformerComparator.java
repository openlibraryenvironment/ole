package org.kuali.ole.spring.batch.handlers;

import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataTransformer;

import java.util.Comparator;

/**
 * Created by pvsubrah on 12/22/15.
 */
public class BatchProfileDataTransformerComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        BatchProfileDataTransformer sh1 = (BatchProfileDataTransformer) o1;
        BatchProfileDataTransformer sh2 = (BatchProfileDataTransformer) o2;

        return sh1.getStep().compareTo(sh2.getStep());
    }
}

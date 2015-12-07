package org.kuali.ole.audit;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;

import java.util.List;

/**
 * Created by pvsubrah on 11/5/15.
 */
public class ObjectDiffer {

    private static ObjectDiffer objectDiffer;
    private static Javers javers;

    private ObjectDiffer() {

    }

    public static ObjectDiffer getInstance(){
        synchronized (ObjectDiffer.class) {
            if (null == objectDiffer) {
                objectDiffer = new ObjectDiffer();
                javers = JaversBuilder.javers().build();
            }
        }
        return objectDiffer;
    }

    public List<ValueChange> diff(Object oldObject, Object newObject){

        Diff diff = javers.compare(oldObject, newObject);

        List<ValueChange> changesByType = diff.getChangesByType(ValueChange.class);

        return changesByType;
    }
}

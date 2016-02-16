package org.kuali.ole.batchannotation;

import org.springframework.batch.item.ItemProcessor;

public class DummyProcessor implements ItemProcessor<Dummy, Dummy> {

    @Override
    public Dummy process(Dummy dummy) throws Exception {
        int dummyProperty = dummy.getDummyProperty() * dummy.getDummyProperty();
        Dummy transformedDummy = new Dummy(dummyProperty);
        return transformedDummy;
    }
}
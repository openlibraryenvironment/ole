package org.kuali.ole.batch;

import org.springframework.batch.item.ItemProcessor;

public class DummyProcessor implements ItemProcessor<Dummy, Dummy> {

    @Override
    public Dummy process(Dummy dummy) throws Exception {
        int dummyProperty = dummy.getDummyProperty() * 2;
        Dummy transformedDummy = new Dummy(dummyProperty);
        return transformedDummy;
    }

}

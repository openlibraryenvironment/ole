package org.kuali.ole.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class DummyWriter implements ItemWriter<Dummy> {

    @Override
    public void write(List<? extends Dummy> items) throws Exception {
        for (Dummy d : items) {
            System.out.println(d);
        }
    }
}
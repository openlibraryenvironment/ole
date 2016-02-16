package org.kuali.ole.batchxml;

import java.text.ParseException;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomFieldSetMapper implements FieldSetMapper<Item> {

    @Override
    public Item mapFieldSet(FieldSet fieldSet) throws BindException {
        Item item = new Item();
        item.setId(fieldSet.readInt(0));
        item.setProperty(fieldSet.readString(1));
        return item;
    }
}
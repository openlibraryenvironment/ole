package org.kuali.ole.oleng.batch.process.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SheikS on 2/15/2016.
 */
public class ValueByPriority {
    private int priority;
    private String field;
    private boolean multiValue;
    private List<String> values;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isMultiValue() {
        return multiValue;
    }

    public void setMultiValue(boolean multiValue) {
        this.multiValue = multiValue;
    }

    public List<String> getValues() {
        if (null == values) {
            values = new ArrayList<>();
        }
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void addValues(String value) {
        if (null != value) {
            getValues().add(value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueByPriority that = (ValueByPriority) o;

        if (priority != that.priority) return false;
        return field != null ? field.equals(that.field) : that.field == null;

    }

    @Override
    public int hashCode() {
        int result = priority;
        result = 31 * result + (field != null ? field.hashCode() : 0);
        return result;
    }
}

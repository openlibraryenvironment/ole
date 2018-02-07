/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.uif.util;

import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.comparator.NumericValueComparator;
import org.kuali.rice.krad.comparator.TemporalValueComparator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.layout.TableLayoutManager;
import org.kuali.rice.krad.uif.view.ExpressionEvaluator;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Comparator used for server side sorting of CollectionGroup data.
 * 
 * <p>
 * This may include DataFields, as well as Fields that don't map directly to elements in the model
 * collection, such as {@link org.kuali.rice.krad.uif.field.LinkField}s that may contain
 * expressions.
 * </p>
 * 
 * <p>
 * NOTE: This class is not thread safe, and each instance is intended to be used only once.
 * </p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MultiColumnComparator implements Comparator<Integer> {

    private final List<Object> modelCollection;
    private final CollectionGroup collectionGroup;
    private final List<ColumnSort> columnSorts;
    private final View view;

    // we use the layout manager a lot, so for convenience we'll keep a handy reference to it
    private final TableLayoutManager tableLayoutManager;

    // we need the prototype row to be able to get Fields that can be used in extracting & calculating column values
    private final List<Field> prototypeRow;

    // if we have to evaluate expressions to sort a column, we want to cache the values so we don't have to
    // evaluate the same expressions repeatedly.  This cache could get too big, so we'll use a weak reference map
    private final WeakHashMap<String, String> calculatedValueCache;

    // Reflection is used to determine the class of certain column values.  Cache those classes
    private final HashMap<String, Class> propertyClassCache;

    /**
     * Constructs a MultiColumnComparator instance
     * 
     * @param modelCollection the model collection that the CollectionGroup is associated with
     * @param collectionGroup the CollectionGroup whose columns are being sorted
     * @param columnSorts A list from highest to lowest precedence of the column sorts to apply
     * @param view The view
     */
    public MultiColumnComparator(List<Object> modelCollection, CollectionGroup collectionGroup,
            List<ColumnSort> columnSorts, View view) {
        this.modelCollection = modelCollection;
        this.collectionGroup = collectionGroup;
        this.columnSorts = columnSorts;
        this.view = view;

        //
        // initialize convenience members and calculated members.  Caches first!
        //

        calculatedValueCache = new WeakHashMap<String, String>();
        propertyClassCache = new HashMap<String, Class>();

        tableLayoutManager = (TableLayoutManager) collectionGroup.getLayoutManager();
        prototypeRow = buildPrototypeRow();
    }

    /**
     * Compares the modelCollecton element at index1 to the element at index2 based on the provided
     * {@link org.kuali.rice.krad.uif.util.ColumnSort}s.
     * 
     * @param index1 the index of the first modelCollection element used for comparison
     * @param index2 the index of the second modelCollection element used for comparison
     * @return 0 if the two elements are considered equal, a positive integer if the element at
     *         index1 is considered greater, else a negative integer
     */
    @Override
    public int compare(Integer index1, Integer index2) {
        int sortResult = 0;

        for (ColumnSort columnSort : columnSorts) {

            Field protoField = prototypeRow.get(columnSort.getColumnIndex()); // get the prototype field for this column
            Object modelElement1 = modelCollection.get(index1);
            Object modelElement2 = modelCollection.get(index2);

            if (isOneNull(modelElement1, modelElement2)) { // is one of the modelCollection elements null?
                sortResult = compareOneIsNull(modelElement1, modelElement2);
            } else if (protoField instanceof DataField) {
                sortResult = compareDataFieldValues(columnSort, (DataField) protoField, index1, index2);
            } else {
                sortResult = compareFieldStringValues(columnSort, protoField, index1, index2);
            }

            if (sortResult != 0) { // stop looking at additional columns, we've made our determination
                // Handle sort direction here
                if (columnSort.getDirection() == ColumnSort.Direction.DESC) {
                    sortResult *= -1;
                }

                break;
            }
        }

        return sortResult;
    }

    /**
     * Compare the DataField values for the two modelCollection element indexes.
     * 
     * @param columnSort the comparison metadata (which column number, which direction, what type of
     *        sort)
     * @param protoField the prototype DataField for the column being sorted
     * @param index1 the index of the first modelCollection element for comparison
     * @param index2 the index of the second modelCollection element for comparison
     * @return 0 if the two elements are considered equal, a positive integer if the element at
     *         index1 is considered greater, else a negative integer
     */
    private int compareDataFieldValues(ColumnSort columnSort, DataField protoField, Integer index1, Integer index2) {
        final int sortResult;// for DataFields, try to get the property value and use it directly

        final Object modelElement1 = modelCollection.get(index1);
        final Object modelElement2 = modelCollection.get(index2);

        // get the rest of the property path after the collection
        final String propertyPath = protoField.getBindingInfo().getBindingName();
        final Class<?> columnDataClass = getColumnDataClass(propertyPath);

        // we can do smart comparisons for Comparables
        if (Comparable.class.isAssignableFrom(columnDataClass)) {
            Comparable datum1 = (Comparable) ObjectUtils.getPropertyValue(modelElement1, propertyPath);
            Comparable datum2 = (Comparable) ObjectUtils.getPropertyValue(modelElement2, propertyPath);

            if (isOneNull(datum1, datum2)) {
                sortResult = compareOneIsNull(datum1, datum2);
            } else if (String.class.equals(columnDataClass)) {
                sortResult = columnTypeCompare((String) datum1, (String) datum2, columnSort.getSortType());
            } else {
                sortResult = datum1.compareTo(datum2);
            }
        } else { // resort to basic column string value comparison if the column data class isn't Comparable
            sortResult = compareFieldStringValues(columnSort, protoField, index1, index2);
        }

        return sortResult;
    }

    /**
     * Attempt to determine the class of the column data value using the given modelCollection.
     * 
     * <p>
     * If the class can not be determined, Object will be returned.
     * </p>
     * 
     * @param propertyPath the path to the datum (which applies to modelCollection elements) whose
     *        class we are attempting to determine
     * @return the class of the given property from the modelElements, or Object if the class cannot
     *         be determined.
     */
    private Class<?> getColumnDataClass(String propertyPath) {
        Class<?> dataClass = propertyClassCache.get(propertyPath);

        if (dataClass == null) {

            // for the elements in the modelCollection while dataClass is null
            for (int i = 0; i < modelCollection.size() && dataClass == null; i++) {
                // try getting the class from the modelCollection element
                dataClass = ObjectPropertyUtils.getPropertyType(modelCollection.get(i), propertyPath);
            }

            if (dataClass == null) {
                dataClass = Object.class; // default
            }

            propertyClassCache.put(propertyPath, dataClass);
        }

        return dataClass;
    }

    /**
     * Compare the field values by computing the two string values and comparing them based on the
     * sort type.
     * 
     * @param columnSort the comparison metadata (which column number, which direction, what type of
     *        sort)
     * @param protoField the prototype Field for the column being sorted
     * @param index1 the index of the first modelCollection element for comparison
     * @param index2 the index of the second modelCollection element for comparison
     * @return 0 if the two elements are considered equal, a positive integer if the element at
     *         index1 is considered greater, else a negative integer
     */
    private int compareFieldStringValues(ColumnSort columnSort, Field protoField, Integer index1, Integer index2) {
        final int sortResult;
        final String fieldValue1;
        final String fieldValue2;

        if (!CollectionUtils.sizeIsEmpty(protoField.getPropertyExpressions())) {
            // We have to evaluate expressions
            fieldValue1 = calculateFieldValue(protoField, index1, columnSort.getColumnIndex());
            fieldValue2 = calculateFieldValue(protoField, index2, columnSort.getColumnIndex());
        } else {
            fieldValue1 = KRADUtils.getSimpleFieldValue(modelCollection.get(index1), protoField);
            fieldValue2 = KRADUtils.getSimpleFieldValue(modelCollection.get(index2), protoField);
        }

        sortResult = columnTypeCompare(fieldValue1, fieldValue2, columnSort.getSortType());
        return sortResult;
    }

    /**
     * Calculates the value for a field that may contain expressions.
     * 
     * <p>
     * Checks for a cached value for this calculated value, and if there isn't one, expressions are
     * evaluated before getting the value, which is then cached and returned.
     * </p>
     * 
     * @param protoField the Field whose expressions need evaluation
     * @param collectionIndex the index of the model collection element being used in the
     *        calculation
     * @param columnIndex the index of the column whose value is being calculated
     * @return the calculated value for the field for this collection line
     */
    private String calculateFieldValue(Field protoField, Integer collectionIndex, int columnIndex) {
        final String fieldValue1;

        // cache key format is "<elementIndex>,<columnIndex>"
        final String cacheKey = String.format("%d,%d", collectionIndex, columnIndex);
        String cachedValue = calculatedValueCache.get(cacheKey);

        if (cachedValue == null) {
            Object collectionElement = modelCollection.get(collectionIndex);
            ExpressionEvaluator expressionEvaluator = view.getViewHelperService().getExpressionEvaluator();

            // set up expression context
            Map<String, Object> viewContext = view.getContext();
            Map<String, Object> expressionContext = new HashMap<String, Object>();
            
            if (viewContext != null) {
                expressionContext.putAll(viewContext);
            }
            
            expressionContext.put(UifConstants.ContextVariableNames.LINE, collectionElement);
            expressionContext.put(UifConstants.ContextVariableNames.INDEX, collectionIndex);
            expressionContext.put(UifConstants.ContextVariableNames.COLLECTION_GROUP, collectionGroup);
            expressionContext.put(UifConstants.ContextVariableNames.MANAGER, tableLayoutManager);
            expressionContext.put(UifConstants.ContextVariableNames.COMPONENT, protoField);
            expressionContext.put(UifConstants.ContextVariableNames.PARENT, collectionGroup);

            expressionEvaluator.evaluateExpressionsOnConfigurable(view, protoField, expressionContext);

            fieldValue1 = KRADUtils.getSimpleFieldValue(collectionElement, protoField);

            calculatedValueCache.put(cacheKey, fieldValue1);
        } else {
            fieldValue1 = cachedValue;
        }

        return fieldValue1;
    }

    /**
     * Compare the string values based on the given sortType, which must match one of the constants
     * in {@link UifConstants.TableToolsValues}.
     * 
     * @param val1 The first string value for comparison
     * @param val2 The second string value for comparison
     * @param sortType the sort type
     * @return 0 if the two elements are considered equal, a positive integer if the element at
     *         index1 is considered greater, else a negative integer
     */
    private int columnTypeCompare(String val1, String val2, String sortType) {
        final int result;

        if (isOneNull(val1, val2)) {
            result = compareOneIsNull(val1, val2);
        } else if (UifConstants.TableToolsValues.STRING.equals(sortType)) {
            result = val1.compareTo(val2);
        } else if (UifConstants.TableToolsValues.NUMERIC.equals(sortType)) {
            result = NumericValueComparator.getInstance().compare(val1, val2);
        } else if (UifConstants.TableToolsValues.PERCENT.equals(sortType)) {
            result = NumericValueComparator.getInstance().compare(val1, val2);
        } else if (UifConstants.TableToolsValues.DATE.equals(sortType)) {
            result = TemporalValueComparator.getInstance().compare(val1, val2);
        } else if (UifConstants.TableToolsValues.CURRENCY.equals(sortType)) {
            // strip off non-numeric symbols, convert to KualiDecimals, and compare
            KualiDecimal decimal1 = new KualiDecimal(val1.replaceAll("[^0-9.]", ""));
            KualiDecimal decimal2 = new KualiDecimal(val2.replaceAll("[^0-9.]", ""));

            result = decimal1.compareTo(decimal2);
        } else {
            throw new RuntimeException("unknown sort type: " + sortType);
        }

        return result;
    }

    /**
     * Is one of the given objects null?
     * 
     * @param o1 the first object
     * @param o2 the second object
     * @return true if one of the given references is null, false otherwise
     */
    private boolean isOneNull(Object o1, Object o2) {
        return (o1 == null || o2 == null);
    }

    /**
     * Compare two referenced objects (assuming at least one of them is null).
     * 
     * <p>
     * The arbitrary determination here is that a non-null reference is greater than a null
     * reference, and two null references are equal.
     * </p>
     * 
     * @param o1 the first object
     * @param o2 the second object
     * @return 0 if both are null, 1 if the first is non-null, and -1 if the second is non-null.
     */
    private int compareOneIsNull(Object o1, Object o2) {
        if (o1 == null) {
            if (o2 == null) {
                return 0;
            }

            return -1;
        }

        if (o2 != null) {
            throw new IllegalStateException("at least one parameter must be null");
        }

        return 1;
    }

    /**
     * Build a List of prototype Fields representing a row of the table.
     * 
     * <p>
     * Any DataFields will have their binding paths shortened to access the model collection
     * elements directly, instead of via the data object
     * </p>
     * 
     * @return a List of prototype Fields representing a row in the table
     */
    private List<Field> buildPrototypeRow() {
        final List<Field> prototypeRow = new ArrayList<Field>(tableLayoutManager.getNumberOfColumns());

        final List<Field> allRowFields = tableLayoutManager.getAllRowFields();
        final Iterator<Field> allRowFieldsIter = allRowFields.iterator();

        // find the index of the first component beyond the add line
        int componentsSkipped = 0;
        int columnsSkipped = 0;

        if (collectionGroup.isRenderAddLine() && !collectionGroup.isReadOnly()
                && !tableLayoutManager.isSeparateAddLine()) {
            while (columnsSkipped < tableLayoutManager.getNumberOfColumns()) {
                columnsSkipped += allRowFieldsIter.next().getColSpan();
                componentsSkipped += 1;
            }
        }

        // build prototypes from first row, starting just past the add line components
        for (int i = 0; i < tableLayoutManager.getNumberOfColumns(); i++) {
            Field protoField = allRowFields.get(componentsSkipped + i).copy(); // note the adjusted index

            if (protoField instanceof DataField) {
                // adjust binding path for direct element access
                final DataField dataField = (DataField) protoField;

                // use a copy of the binding info so no shared data gets affected
                final BindingInfo bindingInfoCopy = dataField.getBindingInfo().copy();
                dataField.setBindingInfo(bindingInfoCopy);

                String elementAdjustedBindingPath = dataField.getBindingInfo().getBindingName();
                bindingInfoCopy.setBindingPath(elementAdjustedBindingPath);
            }

            prototypeRow.add(protoField);
        }

        return prototypeRow;
    }
}

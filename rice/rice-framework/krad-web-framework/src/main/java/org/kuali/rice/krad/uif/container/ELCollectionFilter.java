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
package org.kuali.rice.krad.uif.container;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.view.ExpressionEvaluator;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collection filter that evaluates a configured el expression against each line
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "conditionalCollectionFilter-bean", parent = "Uif-ConditionalCollectionFilter")
public class ELCollectionFilter implements CollectionFilter {
    private static final long serialVersionUID = 3273495753269940272L;

    private String expression = "";

    /**
     * Iterates through the collection and evaluates the el expression in context of the line. If the expression
     * evaluates to true, the line will remain, else be filtered out
     *
     * @see org.kuali.rice.krad.uif.container.CollectionFilter#filter(org.kuali.rice.krad.uif.view.View, Object,
     *      CollectionGroup)
     */
    @Override
    public List<Integer> filter(View view, Object model, CollectionGroup collectionGroup) {
        // get the collection for this group from the model
        List<Object> modelCollection = ObjectPropertyUtils.getPropertyValue(model,
                collectionGroup.getBindingInfo().getBindingPath());

        ExpressionEvaluator expressionEvaluator =
                view.getViewHelperService().getExpressionEvaluator();

        // iterate through and add index that pass the expression
        List<Integer> showIndexes = new ArrayList<Integer>();

        int lineIndex = 0;
        for (Object line : modelCollection) {
            Map<String, Object> context = new HashMap<String, Object>(collectionGroup.getContext());
            context.put(UifConstants.ContextVariableNames.LINE, line);
            context.put(UifConstants.ContextVariableNames.INDEX, lineIndex);

            Boolean conditionPasses = (Boolean) expressionEvaluator.evaluateExpression(context, expression);
            if (conditionPasses) {
                showIndexes.add(lineIndex);
            }

            lineIndex++;
        }

        return showIndexes;
    }

    /**
     * Expression that will be evaluated for each line to determine whether the line should be filtered
     *
     * <p>
     * If expression passes, the line will remain in the collection, otherwise be filtered out. The expression given
     * should evaluate to a boolean
     * </p>
     *
     * @return valid el expression that evaluates to a boolean
     */
    @BeanTagAttribute(name="expression")
    public String getExpression() {
        return expression;
    }

    /**
     * Setter for the expression to use for filtering
     *
     * @param expression
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    public <T> T copy() {
        T copiedClass = null;
        try {
            copiedClass = (T)this.getClass().newInstance();
        }
        catch(Exception exception) {
            throw new RuntimeException();
        }

        copyProperties(copiedClass);

        return copiedClass;
    }

    protected <T> void copyProperties(T eLCollectionFilter) {
        ELCollectionFilter eLCollectionFilterCopy = (ELCollectionFilter) eLCollectionFilter;
        eLCollectionFilterCopy.setExpression(this.getExpression());
    }

}

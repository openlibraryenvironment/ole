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









package org.kuali.rice.core.api.mixin

import groovyjarjarasm.asm.Opcodes
import javax.xml.bind.annotation.XmlAnyElement
import javax.xml.bind.annotation.XmlType
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ConstructorNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.ListExpression
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.messages.SimpleMessage
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.kuali.rice.core.api.CoreConstants

@GroovyASTTransformation(phase = org.codehaus.groovy.control.CompilePhase.CANONICALIZATION)
class RemoteableModelObjectTransformation implements ASTTransformation {

    private static final Class MY_CLASS = RemoteableModelObject.class;
    private static final ClassNode MY_TYPE = ClassHelper.make(MY_CLASS);

    void visit(ASTNode[] nodes, SourceUnit source) {
        AnnotatedNode parent = (AnnotatedNode) nodes[1];
        AnnotationNode anno = (AnnotationNode) nodes[0];
        if (!MY_TYPE.equals(anno.getClassNode())) {
            return
        }

        if (parent instanceof ClassNode) {
            ClassNode cNode = (ClassNode) parent;
            createFutureElementsFieldToClass(cNode, source)
            createFutureElementsToXmlTypePropOrder(cNode, source)
            createPrivateNoArgCtor(cNode, source)
            //ToStringASTTransformation.createToString(cNode, false, true, [CoreConstants.CommonElements.FUTURE_ELEMENTS])
            //EqualsAndHashCodeASTTransformation.createHashCode(cNode, true, true, false, [CoreConstants.CommonElements.FUTURE_ELEMENTS])
            //EqualsAndHashCodeASTTransformation.createEquals(cNode, true, false, [CoreConstants.CommonElements.FUTURE_ELEMENTS])
        }
    }

    static void createFutureElementsFieldToClass(ClassNode cNode, SourceUnit source) {
        //FIXME: check if field already exists - error if so

        FieldNode futureElementsNode = new FieldNode(
                CoreConstants.CommonElements.FUTURE_ELEMENTS,
                Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL,
                new ClassNode(Collection.class), ClassNode.THIS, null)

        futureElementsNode.addAnnotation(new AnnotationNode(new ClassNode(XmlAnyElement.class)))

        cNode.addField(futureElementsNode);
    }

    static void createFutureElementsToXmlTypePropOrder(ClassNode cNode, SourceUnit source) {
        final Collection<AnnotationNode> xmlTypes = cNode.getAnnotations(new ClassNode(XmlType.class))
        if (!xmlTypes) {
            source.getErrorCollector().addError(new SimpleMessage("${cNode.getName()} must contain ${XmlType.class.getName()}", source));
        }

        final AnnotationNode xmlType = xmlTypes[0]
        final Expression order = xmlType.getMember("propOrder")
        final ConstantExpression futureElementsExpr = new ConstantExpression(CoreConstants.CommonElements.FUTURE_ELEMENTS);
        final ListExpression listExpression = new ListExpression();

        if (order instanceof ListExpression) {
            ((ListExpression) order).getExpressions().each {
                errorIffutureElementsExist(cNode, source, it, futureElementsExpr)
                listExpression.addExpression(it)
            }
        } else {
            errorIffutureElementsExist(cNode, source, order, futureElementsExpr)
            listExpression.addExpression(order);
        }
        listExpression.addExpression(futureElementsExpr)
        xmlType.setMember("propOrder", listExpression)
    }

    static createPrivateNoArgCtor(ClassNode cNode, SourceUnit source) {
        for (it in cNode.getDeclaredConstructors()) {
            if (!it.getParameters() || it.getParameters().size() == 0) {
                if (!it.isPrivate()) {
                    source.getErrorCollector().addError(new SimpleMessage("${cNode.getName()} contains a non-private no arg constructor.", source));
                }
                //don't add no-arg ctor it already has one
                return;
            }
        }

        if (cNode.getSuperClass().getDeclaredConstructors().any { !it.getParameters() || it.getParameters().size() == 0 }) {
            cNode.addConstructor(new ConstructorNode(
                    Opcodes.ACC_PRIVATE, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY,
                    new ExpressionStatement(
                            new ConstructorCallExpression(cNode.getSuperClass(), ArgumentListExpression.EMPTY_ARGUMENTS))))
        } else {
            source.getErrorCollector().addError(new SimpleMessage("${cNode.getSuperClass().getName()} does not contain a no arg constructor.", source));
        }
    }

    private static void errorIffutureElementsExist(ClassNode cNode, SourceUnit source, existingExpr, curExpr) {
        //FIXME: really bad way to do this
        if (existingExpr.getText().equals('org.kuali.rice.core.api.CoreConstants$CommonElements.FUTURE_ELEMENTS')) {
            source.getErrorCollector().addError(new SimpleMessage("${cNode.getName()}'s ${XmlType.class.getName()} annotation already contains the ${CoreConstants.CommonElements.FUTURE_ELEMENTS} in propOrder", source));
        }
    }
}

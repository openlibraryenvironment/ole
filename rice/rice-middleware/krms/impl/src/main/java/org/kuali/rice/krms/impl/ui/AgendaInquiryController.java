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
package org.kuali.rice.krms.impl.ui;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.web.controller.InquiryController;
import org.kuali.rice.krad.web.form.InquiryForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.rice.krms.impl.repository.ActionBo;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.kuali.rice.krms.impl.repository.AgendaItemBo;
import org.kuali.rice.krms.impl.repository.ContextBoService;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.repository.RuleBo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = org.kuali.rice.krms.impl.util.KrmsImplConstants.WebPaths.AGENDA_INQUIRY_PATH)
public class AgendaInquiryController  extends InquiryController {

    /**
     * This method updates the existing rule in the agenda.
     */
    @RequestMapping(params = "methodToCall=" + "viewRule")
    public ModelAndView viewRule(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        AgendaEditor agendaEditor = getAgendaEditor(form);
        agendaEditor.setAddRuleInProgress(false);
        // this is the root of the tree:
        AgendaItemBo firstItem = getFirstAgendaItem(agendaEditor.getAgenda());
        //TODO: remove the hardcoded item number ... its used only for testing until the selectedAgendaItemId stuff works
        String selectedItemId = agendaEditor.getSelectedAgendaItemId();
        AgendaItemBo node = getAgendaItemById(firstItem, selectedItemId);

        setAgendaItemLine(form, node);

        form.getActionParameters().put(UifParameters.NAVIGATE_TO_PAGE_ID, "AgendaEditorView-ViewRule-Page");
        return super.navigate(form, result, request, response);
    }

    /**
     * @param form
     * @return the {@link AgendaEditor} from the form
     */
    private AgendaEditor getAgendaEditor(UifFormBase form) {
        InquiryForm inquiryForm = (InquiryForm) form;
        return ((AgendaEditor)inquiryForm.getDataObject());
    }

    /**
     * This method finds and returns the first agenda item in the agenda, or null if there are no items presently
     *
     * @param agenda
     * @return
     */
    private AgendaItemBo getFirstAgendaItem(AgendaBo agenda) {
        AgendaItemBo firstItem = null;
        if (agenda != null && agenda.getItems() != null) for (AgendaItemBo agendaItem : agenda.getItems()) {
            if (agenda.getFirstItemId().equals(agendaItem.getId())) {
                firstItem = agendaItem;
                break;
            }
        }
        return firstItem;
    }

    /**
     * Search the tree for the agenda item with the given id.
     */
    private AgendaItemBo getAgendaItemById(AgendaItemBo node, String agendaItemId) {
        if (node == null) throw new IllegalArgumentException("node must be non-null");

        AgendaItemBo result = null;

        if (agendaItemId.equals(node.getId())) {
            result = node;
        } else {
            for (AgendaItemChildAccessor childAccessor : AgendaItemChildAccessor.linkedNodes) {
                AgendaItemBo child = childAccessor.getChild(node);
                if (child != null) {
                    result = getAgendaItemById(child, agendaItemId);
                    if (result != null) break;
                }
            }
        }
        return result;
    }

    /**
     * This method sets the agendaItemLine for adding/editing AgendaItems.
     * The agendaItemLine is a copy of the agendaItem so that changes are not applied when
     * they are abandoned.  If the agendaItem is null a new empty agendaItemLine is created.
     *
     * @param form
     * @param agendaItem
     */
    private void setAgendaItemLine(UifFormBase form, AgendaItemBo agendaItem) {
        AgendaEditor agendaEditor = getAgendaEditor(form);
        if (agendaItem == null) {
            RuleBo rule = new RuleBo();
            rule.setId(getSequenceAccessorService().getNextAvailableSequenceNumber("KRMS_RULE_S", RuleBo.class)
                    .toString());
            if (StringUtils.isBlank(agendaEditor.getAgenda().getContextId())) {
                rule.setNamespace("");
            } else {
                rule.setNamespace(getContextBoService().getContextByContextId(agendaEditor.getAgenda().getContextId()).getNamespace());
            }
            agendaItem = new AgendaItemBo();
            agendaItem.setRule(rule);
            agendaEditor.setAgendaItemLine(agendaItem);
        } else {
            // TODO: Add a copy not the reference
            agendaEditor.setAgendaItemLine((AgendaItemBo) ObjectUtils.deepCopy(agendaItem));
        }


        if (agendaItem.getRule().getActions().isEmpty()) {
            ActionBo actionBo = new ActionBo();
            actionBo.setTypeId("");
            actionBo.setNamespace(agendaItem.getRule().getNamespace());
            actionBo.setRuleId(agendaItem.getRule().getId());
            actionBo.setSequenceNumber(1);
            agendaEditor.setAgendaItemLineRuleAction(actionBo);
        } else {
            agendaEditor.setAgendaItemLineRuleAction(agendaItem.getRule().getActions().get(0));
        }

        agendaEditor.setCustomRuleActionAttributesMap(agendaEditor.getAgendaItemLineRuleAction().getAttributes());
    }

    /**
     * <p>This class abstracts getting and setting a child of an AgendaItemBo, making some recursive operations
     * require less boiler plate.</p>
     *
     * <p>The word 'child' in AgendaItemChildAccessor means child in the strict data structures sense, in that the
     * instance passed in holds a reference to some other node (or null).  However, when discussing the agenda tree
     * and algorithms for manipulating it, the meaning of 'child' is somewhat different, and there are notions of
     * 'sibling' and 'cousin' that are tossed about too. It's probably worth explaining that somewhat here:</p>
     *
     * <p>General principals of relationships when talking about the agenda tree:
     * <ul>
     * <li>Generation boundaries (parent to child) are across 'When TRUE' and 'When FALSE' references.</li>
     * <li>"Age" among siblings & cousins goes from top (oldest) to bottom (youngest).</li>
     * <li>siblings are related by 'Always' references.</li>
     * </ul>
     * </p>
     * <p>This diagram of an agenda tree and the following examples seek to illustrate these principals:</p>
     * <img src="doc-files/AgendaEditorController-1.png" alt="Example Agenda Items"/>
     * <p>Examples:
     * <ul>
     * <li>A is the parent of B, C, & D</li>
     * <li>E is the younger sibling of A</li>
     * <li>B is the older cousin of C</li>
     * <li>C is the older sibling of D</li>
     * <li>F is the younger cousin of D</li>
     * </ul>
     * </p>
     */
    protected static class AgendaItemChildAccessor {

        private enum Child { WHEN_TRUE, WHEN_FALSE, ALWAYS };

        private static final AgendaItemChildAccessor whenTrue = new AgendaItemChildAccessor(Child.WHEN_TRUE);
        private static final AgendaItemChildAccessor whenFalse = new AgendaItemChildAccessor(Child.WHEN_FALSE);
        private static final AgendaItemChildAccessor always = new AgendaItemChildAccessor(Child.ALWAYS);

        /**
         * Accessors for all linked items
         */
        private static final AgendaItemChildAccessor [] linkedNodes = { whenTrue, whenFalse, always };

        /**
         * Accessors for children (so ALWAYS is omitted);
         */
        private static final AgendaItemChildAccessor [] children = { whenTrue, whenFalse };

        private final Child whichChild;

        private AgendaItemChildAccessor(Child whichChild) {
            if (whichChild == null) throw new IllegalArgumentException("whichChild must be non-null");
            this.whichChild = whichChild;
        }

        /**
         * @return the referenced child
         */
        public AgendaItemBo getChild(AgendaItemBo parent) {
            switch (whichChild) {
            case WHEN_TRUE: return parent.getWhenTrue();
            case WHEN_FALSE: return parent.getWhenFalse();
            case ALWAYS: return parent.getAlways();
            default: throw new IllegalStateException();
            }
        }

        /**
         * Sets the child reference and the child id
         */
        public void setChild(AgendaItemBo parent, AgendaItemBo child) {
            switch (whichChild) {
            case WHEN_TRUE:
                parent.setWhenTrue(child);
                parent.setWhenTrueId(child == null ? null : child.getId());
                break;
            case WHEN_FALSE:
                parent.setWhenFalse(child);
                parent.setWhenFalseId(child == null ? null : child.getId());
                break;
            case ALWAYS:
                parent.setAlways(child);
                parent.setAlwaysId(child == null ? null : child.getId());
                break;
            default: throw new IllegalStateException();
            }
        }
    }

    /**
     *  return the sequenceAssessorService
     */
    private SequenceAccessorService getSequenceAccessorService() {
        return KRADServiceLocator.getSequenceAccessorService();
    }

    /**
     * return the contextBoService
     */
    private ContextBoService getContextBoService() {
        return KrmsRepositoryServiceLocator.getContextBoService();
    }

}

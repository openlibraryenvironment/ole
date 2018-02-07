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
package org.kuali.rice.krms.impl.repository;


import java.util.Map.Entry
import org.apache.commons.lang.StringUtils
import org.kuali.rice.core.api.util.tree.Node
import org.kuali.rice.core.api.util.tree.Tree
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.krms.api.repository.LogicalOperator
import org.kuali.rice.krms.api.repository.action.ActionDefinition
import org.kuali.rice.krms.api.repository.proposition.PropositionType
import org.kuali.rice.krms.api.repository.rule.RuleDefinition
import org.kuali.rice.krms.api.repository.rule.RuleDefinitionContract
import org.kuali.rice.krms.impl.ui.CompoundOpCodeNode
import org.kuali.rice.krms.impl.ui.RuleTreeNode
import org.kuali.rice.krms.impl.ui.SimplePropositionNode
import org.kuali.rice.krms.impl.ui.SimplePropositionEditNode
import org.kuali.rice.krms.impl.ui.CompoundPropositionEditNode
import org.kuali.rice.krad.service.SequenceAccessorService
import org.kuali.rice.krad.service.KRADServiceLocator
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition

public class RuleBo extends PersistableBusinessObjectBase implements RuleDefinitionContract {
   
   def String id;
   def String namespace;
   def String description;
   def String name;
   def String typeId;
   def String propId;
   def boolean active = true;

   PropositionBo proposition;
   List<ActionBo> actions;
   List<RuleAttributeBo> attributeBos;
   //def List<PropositionBo> allChildPropositions
   
   // for Rule editor display
   Tree<RuleTreeNode, String> propositionTree;
   
   // for rule editor display
   String propositionSummary;
   private StringBuffer propositionSummaryBuffer;
   String selectedPropositionId;

   private static SequenceAccessorService sequenceAccessorService;

   public RuleBo() {
       actions = new ArrayList<ActionBo>();
       attributeBos = new ArrayList<RuleAttributeBo>();
   }

   public PropositionBo getProposition(){
       return proposition;
   }
   public void setProposition(PropositionBo proposition){
       if (proposition != null) {
           propId = proposition.getId();
       } else {
           propId = null;
       }
       this.proposition = proposition;
   }

    /**
     * set the typeId.  If the parameter is blank, then this RuleBo's
     * typeId will be set to null
     * @param typeId
     */
    public void setTypeId(String typeId) {
        if (StringUtils.isBlank(typeId)) {
            this.typeId = null;
        } else {
            this.typeId = typeId;
        }
    }
   
   public Map<String, String> getAttributes() {
       HashMap<String, String> attributes = new HashMap<String, String>();
       for (RuleAttributeBo attr : attributeBos) {
       	   attr.refreshReferenceObject("attributeDefinition");
           attributes.put( attr.getAttributeDefinition().getName(), attr.getValue() );
       }
       return attributes;
   }

   public void setAttributes(Map<String, String> attributes) {
       this.attributeBos  = new ArrayList<RuleAttributeBo>();

       if (!StringUtils.isBlank(this.typeId)) {
           List<KrmsAttributeDefinition> attributeDefinitions = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService().findAttributeDefinitionsByType(this.getTypeId());
           Map<String, KrmsAttributeDefinition> attributeDefinitionsByName = new HashMap<String, KrmsAttributeDefinition>();
           if (attributeDefinitions != null) for (KrmsAttributeDefinition attributeDefinition : attributeDefinitions) {
               attributeDefinitionsByName.put(attributeDefinition.getName(), attributeDefinition);
           }

           for (Map.Entry<String, String> attr : attributes) {
               KrmsAttributeDefinition attributeDefinition = attributeDefinitionsByName.get(attr.key);
               RuleAttributeBo attributeBo = new RuleAttributeBo();
               attributeBo.setRuleId(this.getId());
               attributeBo.setAttributeDefinitionId((attributeDefinition == null) ? null : attributeDefinition.getId());
               attributeBo.setValue(attr.getValue());
               attributeBo.setAttributeDefinition(KrmsAttributeDefinitionBo.from(attributeDefinition));
               attributeBos.add(attributeBo);
           }
       }
   }
   
   public String getPropositionSummary(){
       if (this.propositionTree == null) {
           this.propositionTree = refreshPropositionTree(false);
       }
       return propositionSummaryBuffer.toString();
   }
   
   /**
    * This method is used by the RuleEditor to display the proposition in tree form.
    *
    * @return Tree representing a rule proposition.
    */
    public Tree getPropositionTree() {
        if (this.propositionTree == null){
            this.propositionTree = refreshPropositionTree(false);
        }
        return this.propositionTree;
    }
    public void setPropositionTree(Tree<RuleTreeNode, String> tree) {
        this.propositionTree == tree;
       }

   public Tree refreshPropositionTree(Boolean editMode){
       Tree myTree = new Tree<RuleTreeNode, String>();

       Node<RuleTreeNode, String> rootNode = new Node<RuleTreeNode, String>();
       myTree.setRootElement(rootNode);

       propositionSummaryBuffer = new StringBuffer();
       PropositionBo prop = this.getProposition();
       buildPropTree( rootNode, prop, editMode );
       this.propositionTree = myTree;
       return myTree;
   }

    /**
     * This method builds a propositionTree recursively walking through the children of the proposition.
     * @param sprout - parent tree node
     * @param prop - PropositionBo for which to make the tree node
     * @param editMode - Boolean determines the node type used to represent the proposition
     *     false: create a view only node text control
     *     true: create an editable node with multiple controls
     *     null:  use the proposition.editMode property to determine the node type
     */
   private void buildPropTree( Node sprout, PropositionBo prop, Boolean editMode){
       // Depending on the type of proposition (simple/compound), and the editMode,
       // Create a treeNode of the appropriate type for the node and attach it to the
       // sprout parameter passed in.
       // If the prop is a compound proposition, calls itself for each of the compoundComponents
       if (prop != null) {
           if (PropositionType.SIMPLE.getCode().equalsIgnoreCase(prop.getPropositionTypeCode())){
               // Simple Proposition
               // add a node for the description display with a child proposition node
               Node<RuleTreeNode, String> child = new Node<RuleTreeNode, String>();
               child.setNodeLabel(prop.getDescription());
               if (prop.getEditMode()){
                   child.setNodeLabel("");
                   child.setNodeType(SimplePropositionEditNode.NODE_TYPE);
                   SimplePropositionEditNode pNode = new SimplePropositionEditNode(prop);
                   child.setData(pNode);
                } else {
                  child.setNodeType(SimplePropositionNode.NODE_TYPE);
                   SimplePropositionNode pNode = new SimplePropositionNode(prop);
                   child.setData(pNode);
               }
               sprout.getChildren().add(child);
               propositionSummaryBuffer.append(prop.getParameterDisplayString())
           }
           else if (PropositionType.COMPOUND.getCode().equalsIgnoreCase(prop.getPropositionTypeCode())){
               // Compound Proposition
               propositionSummaryBuffer.append(" ( ");
               Node<RuleTreeNode, String> aNode = new Node<RuleTreeNode, String>();
               aNode.setNodeLabel(prop.getDescription());
               // editMode has description as an editable field
               if (prop.getEditMode()){
                   aNode.setNodeLabel("");
                   aNode.setNodeType("ruleTreeNode compoundNode editNode");
                   CompoundPropositionEditNode pNode = new CompoundPropositionEditNode(prop);
                   aNode.setData(pNode);
               } else {
                   aNode.setNodeType("ruleTreeNode compoundNode");
                   RuleTreeNode pNode = new RuleTreeNode(prop);
                   aNode.setData(pNode);
               }
               sprout.getChildren().add(aNode);

               boolean first = true;
               List <PropositionBo> allMyChildren = prop.getCompoundComponents();
               int compoundSequenceNumber = 0;
               for (PropositionBo child : allMyChildren){
                   child.setCompoundSequenceNumber(++compoundSequenceNumber);  // start with 1
                   // add an opcode node in between each of the children.
                   if (!first){
                       addOpCodeNode(aNode, prop);
                   }
                   first = false;
                   // call to build the childs node
                   buildPropTree(aNode, child, editMode);
               }
               propositionSummaryBuffer.append(" ) ");
           }
       }
   }

   /**
    * 
    * This method adds an opCode Node to separate components in a compound proposition.
    * 
    * @param currentNode
    * @param prop
    * @return
    */
   private void addOpCodeNode(Node currentNode, PropositionBo prop){
       String opCodeLabel = "";
       
       if (LogicalOperator.AND.getCode().equalsIgnoreCase(prop.getCompoundOpCode())){
           opCodeLabel = "AND";
       } else if (LogicalOperator.OR.getCode().equalsIgnoreCase(prop.getCompoundOpCode())){
           opCodeLabel = "OR";
       }
       propositionSummaryBuffer.append(" "+opCodeLabel+" ");
       Node<RuleTreeNode, String> aNode = new Node<RuleTreeNode, String>();
       aNode.setNodeLabel("");
       aNode.setNodeType("ruleTreeNode compoundOpCodeNode");
       aNode.setData(new CompoundOpCodeNode(prop));
       currentNode.getChildren().add(aNode);
   }
   
   
   /**
    * 
   * Converts a mutable bo to it's immutable counterpart
   * @param bo the mutable business object
   * @return the immutable object
   */
  static RuleDefinition to(RuleBo bo) {
      if (bo == null) { return null; }
      return org.kuali.rice.krms.api.repository.rule.RuleDefinition.Builder.create(bo).build();
  }

  /**
   * Converts a immutable object to it's mutable bo counterpart
   * @param im immutable object
   * @return the mutable bo
   */
  static RuleBo from(RuleDefinition im) {
      if (im == null) { return null; }

      RuleBo bo = new RuleBo();
      bo.id = im.getId();
      bo.namespace = im.getNamespace();
      bo.name = im.getName();
      bo.description = im.getDescription();
      bo.typeId = im.getTypeId();
      bo.propId = im.getPropId();
      bo.active = im.isActive();
      bo.proposition = PropositionBo.from(im.getProposition());
      bo.versionNumber = im.getVersionNumber();
      
      bo.actions = new ArrayList<ActionBo>();
      for (ActionDefinition action : im.getActions()){
          bo.actions.add( ActionBo.from(action) );
      }

      // build the set of agenda attribute BOs
      List<RuleAttributeBo> attrs = new ArrayList<RuleAttributeBo>();

      // for each converted pair, build an RuleAttributeBo and add it to the set
      RuleAttributeBo attributeBo;
      for (Entry<String,String> entry  : im.getAttributes().entrySet()){
          KrmsAttributeDefinitionBo attrDefBo = KrmsRepositoryServiceLocator
                  .getKrmsAttributeDefinitionService()
                  .getKrmsAttributeBo(entry.getKey(), im.getNamespace());
          attributeBo = new RuleAttributeBo();
          attributeBo.setRuleId( im.getId() );
          attributeBo.setAttributeDefinitionId( attrDefBo.getId() );
          attributeBo.setValue( entry.getValue() );
          attributeBo.setAttributeDefinition( attrDefBo );
          attrs.add( attributeBo );
      }
      bo.setAttributeBos(attrs);

      return bo;
  }

    public static RuleBo copyRule(RuleBo existing){
        // create a simple proposition Bo
        RuleBo newRule = new RuleBo();

        // copy simple fields
        newRule.setId( getNewRuleId() );
        newRule.setNamespace( existing.getNamespace() );
        newRule.setDescription( existing.getDescription() );
        newRule.setTypeId( existing.getTypeId() );
        newRule.setActive(true);

        PropositionBo newProp = PropositionBo.copyProposition(existing.getProposition());
        newProp.setRuleId( newRule.getId() );
        newRule.setProposition( newProp );

        newRule.setAttributeBos(copyRuleAttributes(existing));
        newRule.setActions(copyRuleActions(existing, newRule.getId()));

        return newRule;
    }

    /**
     * Returns a new copy of this rule with new ids.
     * @param newRuleName name of the copied rule
     * @return RuleBo a copy of the this rule, with new ids, and the given name
     */
    public RuleBo copyRule(String newRuleName) {
        RuleBo copiedRule = RuleBo.copyRule(this);
        // Rule names cannot be the same, the error for being the same name is not displayed to the user, and the document is
        // said to have been successfully submitted.
        //        copiedRule.setName(rule.getName());
        copiedRule.setName(newRuleName);
        return copiedRule;
    }


    public static List<RuleAttributeBo> copyRuleAttributes(RuleBo existing){
        List<RuleAttributeBo> newAttributes = new ArrayList<RuleAttributeBo> ();
        for(RuleAttributeBo attr : existing.getAttributeBos()){
            RuleAttributeBo newAttr = new RuleAttributeBo();
            newAttr.setId( getNewRuleAttributeId() );
            newAttr.setRuleId( attr.getRuleId() );
            newAttr.setAttributeDefinitionId( attr.getAttributeDefinitionId() );
            newAttr.setValue( attr.getValue() );
            newAttributes.add( newAttr );
        }
        return newAttributes;
    }

    public static Set<ActionAttributeBo> copyActionAttributes(ActionBo existing){
        Set<ActionAttributeBo> newAttributes = new HashSet<ActionAttributeBo> ();
        for(ActionAttributeBo attr : existing.getAttributeBos()){
            ActionAttributeBo newAttr = new ActionAttributeBo();
            newAttr.setId( getNewActionAttributeId() );
            newAttr.setActionId( attr.getActionId() );
            newAttr.setAttributeDefinitionId( attr.getAttributeDefinitionId() );
            newAttr.setValue( attr.getValue() );
            newAttributes.add( newAttr );
}
        return newAttributes;
    }

    public static List<ActionBo> copyRuleActions(RuleBo existing, String ruleId){
        List<ActionBo> newActionList = new ArrayList<ActionBo> ();
        for(ActionBo action : existing.getActions()){
            ActionBo newAction = new ActionBo();
            newAction.setId( getNewActionId() );
            newAction.setRuleId( ruleId );
            newAction.setDescription( action.getDescription() );
            newAction.setName( action.getName() );
            newAction.setNamespace( action.getNamespace() );
            newAction.setTypeId( action.getTypeId() );
            newAction.setSequenceNumber( action.getSequenceNumber() );
            newAction.setAttributeBos( copyActionAttributes(action) );
            newActionList.add(newAction);
        }
        return newActionList;
    }

    /**
     * Set the SequenceAccessorService, useful for testing.
     * @param sas SequenceAccessorService to use for getNewId()
     */
    public static void setSequenceAccessorService(SequenceAccessorService sas) {
        sequenceAccessorService = sas;
    }

    /**
     * Returns the next available Rule id.
     * @return String the next available id
     */
    private static String getNewRuleId(){
        return getNewId("KRMS_RULE_S", RuleBo.class);
    }

    private static String getNewId(String table, Class clazz) {
        if (sequenceAccessorService == null) {
            // we don't assign to sequenceAccessorService to preserve existing behavior
            return KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber(table, clazz) + "";
        }
        Long id = sequenceAccessorService.getNextAvailableSequenceNumber(table, clazz);
        return id.toString();
    }

    private static String getNewActionId(){
        return getNewId("KRMS_ACTN_S", ActionBo.class);
    }

    private static String getNewRuleAttributeId(){
        return getNewId("KRMS_RULE_ATTR_S", RuleAttributeBo.class);
    }

    private static String getNewActionAttributeId(){
        return getNewId("KRMS_ACTN_ATTR_S", ActionAttributeBo.class);
    }
}

====
    Copyright 2005-2013 The Kuali Foundation

    Licensed under the Educational Community License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.opensource.org/licenses/ecl2.php

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
====

Overview

These schemas are read from the classloader (for now, "schema/" + name + ".xsd") from the XmlIngesterServiceImpl via a ClassLoaderEntityResolver.
Validation can be turned off in the XmlIngesterServiceImpl through the 'validation' property via Spring.

Schemas:

common.xsd - defines a few simple string types that reused throughout the other schemas

WorkflowData.xsd - The universal workflow data document type that imports all the others, and lists the root node of each other schema as an immediate child.

ApplicationConstants.xsd - ApplicationConstants schema

User.xsd - User schema.  This just specifies an <any> child element and will need more testing against actual institutional plugin service implementations

Group.xsd - Group schema.  This just specifies an <any> child element and will need more testing against actual institutional plugin service implementations

DocumentType.xsd - DocumentType schema.  routePath and routeNode elements specify an <any> child element.  We need to revisit this.  Since the current format would probably generate a duplicative schema, and since we have to plan for customization and integration with custom nodes down the road in the future, I have punted on this.

RuleAttribute.xsd - RuleAttribute schema.  This schema "include"s two other schemas, which are in the same namespace, in which the SearchingConfigType and RoutingConfigTypes are defined.  These two seem identical, and are both almost identical to the EDocLite FieldType.  SearchingConfigFieldType extends the EDocLite FieldType.  RoutingConfigType simply extends the SearchingConfigType (adding nothing) as identical.  We can elaborate or fold up these types as they are reviewed.

SearchingConfig.xsd - Schema defining the SearchingConfigType used by RuleAttribute.xsd schema.  This schema imports the EDocLite schema so as to simply extend the EDocLite FieldType, only adding one new element "fieldEvaluation".

RoutingConfig.xsd - Schema defining the RoutingConfigType used by RuleAttribute.xsd schema.  This schema defines only one type, RoutingConfigType, which extends SearchingConfigType, and adds nothing.  It is just identical to SearchingConfigType.  If it is determined these two are actually the same, these two "types" can just be folded into a single type definition.

RuleTemplate.xsd - RuleTemplate schema.

Rule.xsd - Rule schema.

Help - Help schema.

EDocLite.xsd - EDocLite schema, including XSLT-embedding <style> element, as well as <edl> element which defines the EDocLite doc definition, which has been broken out into a separate, included, schema, "EDocLiteDef.xsd"

EDocLiteDef.xsd - EDocLite definition schema.  Format of the EDocLite doc/form definition.  This schema resides in the same namespace as EDocLite.xsd schema and is merely directly "included".  The FieldType in this schema is extended by the SearchingConfigFieldType of RuleAttribute.xsd, since they are largely identical (all except for an additional "fieldEvaluation" element).

schema-for-xslt20.xsd - Just the XSLT schema so that XSLT can be embedded in the <style> node of the EDocLite schema.

XMLSchema.xsd - I downloaded this XML Schema Schema (boggles...) because it appeared that at one point it was inaccessible on the web and the xml parser was dying.  Not used since, but it's probably a good idea to keep around.  We may want to just use this local copy in the future to prevent everything crumbling if the intar-web goes down.


Strategy for defining schemas and updating services:

- Define a schema for the service.  Inspect sample xml docs for syntax and inspect service implementation to determine semantic constraints (like restrictions on values).
- Import new schema into WorkflowData.xsd schema and reference the root element of that schema in the appropriate position under the "data" element definition
- Go back to service implementation (or corresponding parser implementation), and modify so that it looks for its root elements as children of the document element, and only process children of /those/ elements (i.e. don't just assume that the root element is your schema's root element)
  - This is simply a matter of adding an additional outer loop that iterates over the previously-root elements when using JDOM
  - or just update the XPath expression if using XPath: /myRootType -> /data/myRootType

- Update any existing or test documents of the target schema to be wrapped in a WorkflowData.xsd "data" root element, define namespaces and schemaLocations accordingly
  They should look something like this:

<?xml version="1.0" encoding="UTF-8"?>
<data xmlns="ns:workflow"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="ns:workflow resource:WorkflowData">

  <!-- This is only a demo file showing the format of an actual ApplicationConstantsContent.xml file.  -->
  <ApplicationConstants xmlns="ns:workflow/ApplicationConstants"
                        xsi:schemaLocation="ns:workflow/ApplicationConstants resource:ApplicationConstants">

  Where ApplicationConstants and associated namespace and schema location is replaced with the root element, namespace and location of the schema under consideration.

- Remove the spring bean id reference from the notYetUpdatedServices list of the XmlIngesterService bean definition
- Redeploy workflow and attempt to ingest an updated document.  Expect the service to have problems due to JDom's requirement for manual namespace definitions
  - Update the JDom usage in service implementation (or corresponding parser implementation) to explicitly define a Namespace (private static final Namespace NAMESPACE = Namespace.getNamespace("", "ns:workflow/TheType");), and pass that to all relevant calls, such as 'getChildText("somechild", NAMESPACE)'
    - Review at later date JDom usage to see if there is either a better way to make JDom automatically respect namespaces, or an alternative to JDom altogether (W3C API, XPath, etc.)
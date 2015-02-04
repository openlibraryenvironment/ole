package org.kuali.ole.handler;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.bo.cql.CQLModifiers;
import org.kuali.ole.bo.cql.CQLPrefixes;
import org.kuali.ole.bo.cql.CQLResponseBO;
import org.kuali.ole.bo.cql.CQLSearchClauseTag;

import java.io.IOException;
import java.net.URISyntaxException;

public class OleCQLResponseHandler {

    /**
     * this method is used to convert the xml generated from cql parser to CQLResponseBO object.
     *
     * @param fileContent
     * @return CQLResponseBO object
     * @throws URISyntaxException
     * @throws IOException
     */
    public CQLResponseBO fromXML(String fileContent) throws URISyntaxException, IOException {

        XStream xStream = new XStream();
        xStream.alias("triple", CQLResponseBO.class);
        xStream.alias("modifier", CQLModifiers.class);
        xStream.alias("prefix", CQLPrefixes.class);
        xStream.aliasField("boolean", CQLResponseBO.class, "booleanTagValue");
        xStream.aliasField("value", CQLResponseBO.class, "booleanTagValue");
        xStream.aliasField("leftOperand", CQLResponseBO.class, "leftOperand");
        xStream.aliasField("rightOperand", CQLResponseBO.class, "rightOperand");
        xStream.aliasField("searchClause", CQLResponseBO.class, "searchClauseTag");
        xStream.aliasField("relation", CQLSearchClauseTag.class, "relationTag");
        Object object = xStream.fromXML(fileContent);
        return (CQLResponseBO) object;
    }

    /**
     * this method is used to convert the xml generated from cql parser to CQLSearchClauseTag object.
     *
     * @param fileContent
     * @return CQLSearchClauseTag object
     * @throws URISyntaxException
     * @throws IOException
     */
    public CQLSearchClauseTag fromCQLXML(String fileContent) throws URISyntaxException, IOException {

        XStream xStream = new XStream();
        xStream.alias("searchClause", CQLSearchClauseTag.class);
        xStream.alias("modifier", CQLModifiers.class);
        xStream.alias("prefix", CQLPrefixes.class);
        xStream.aliasField("relation", CQLSearchClauseTag.class, "relationTag");
        Object object = xStream.fromXML(fileContent);
        return (CQLSearchClauseTag) object;
    }

    public String toXML(CQLResponseBO cqlResponseBO) {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XStream xStream = new XStream();
        xStream.alias("triple", CQLResponseBO.class);
        xStream.alias("modifier", CQLModifiers.class);
        xStream.alias("prefix", CQLPrefixes.class);
        xStream.aliasField("boolean", CQLResponseBO.class, "booleanTagValue");
        xStream.aliasField("leftOperand", CQLResponseBO.class, "leftOperand");
        xStream.aliasField("rightOperand", CQLResponseBO.class, "rightOperand");
        xStream.aliasField("searchClause", CQLResponseBO.class, "searchClauseTag");
        xStream.aliasField("relation", CQLSearchClauseTag.class, "relationTag");
        String xml = xStream.toXML(cqlResponseBO);
        System.out.println("XML :: " + xml);
        stringBuffer.append(xml);
        return stringBuffer.toString();
    }


}
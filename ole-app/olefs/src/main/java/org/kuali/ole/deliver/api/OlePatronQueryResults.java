package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.criteria.QueryResults;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 6/1/12
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = OlePatronQueryResults.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OlePatronQueryResults.Constants.TYPE_NAME, propOrder = {
        OlePatronQueryResults.Elements.RESULTS,
        OlePatronQueryResults.Elements.TOTAL_ROW_COUNT,
        OlePatronQueryResults.Elements.MORE_RESULTS_AVAILALBE,
        CoreConstants.CommonElements.FUTURE_ELEMENTS})

public class OlePatronQueryResults implements QueryResults<OlePatronDefinition> {

    @XmlElementWrapper(name = Elements.RESULTS, required = false)
    @XmlElement(name = Elements.RESULT_ELEM, required = false)
    private final List<OlePatronDefinition> results;

    @XmlElement(name = Elements.TOTAL_ROW_COUNT, required = false)
    private final Integer totalRowCount;

    @XmlElement(name = Elements.MORE_RESULTS_AVAILALBE, required = true)
    private final boolean moreResultsAvailable;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OlePatronQueryResults() {
        this.results = null;
        this.totalRowCount = null;
        this.moreResultsAvailable = false;
    }

    private OlePatronQueryResults(Builder builder) {
        final List<OlePatronDefinition> temp = new ArrayList<OlePatronDefinition>();
        for (OlePatronDefinition.Builder b : builder.getResults()) {
            if (b != null) {
                temp.add(b.build());
            }
        }

        this.results = Collections.unmodifiableList(temp);
        this.totalRowCount = builder.getTotalRowCount();
        this.moreResultsAvailable = builder.isMoreResultsAvailable();
    }

    @Override
    public List<OlePatronDefinition> getResults() {
        return results;
    }

    @Override
    public Integer getTotalRowCount() {
        return totalRowCount;
    }

    @Override
    public boolean isMoreResultsAvailable() {
        return moreResultsAvailable;
    }

    public static class Builder implements ModelBuilder, QueryResults<OlePatronDefinition.Builder> {

        private List<OlePatronDefinition.Builder> results;
        private Integer totalRowCount;
        private boolean moreResultsAvailable;

        public static Builder create() {
            return new Builder();
        }

        private Builder() {
            this.results = new ArrayList<OlePatronDefinition.Builder>();
            this.moreResultsAvailable = false;
        }

        @Override
        public OlePatronQueryResults build() {
            return new OlePatronQueryResults(this);
        }

        @Override
        public List<OlePatronDefinition.Builder> getResults() {
            return Collections.unmodifiableList(this.results);
        }

        public void setResults(List<OlePatronDefinition.Builder> results) {
            this.results = new ArrayList<OlePatronDefinition.Builder>(results);
        }

        @Override
        public Integer getTotalRowCount() {
            return this.totalRowCount;
        }

        public void setTotalRowCount(Integer totalRowCount) {
            this.totalRowCount = totalRowCount;
        }

        @Override
        public boolean isMoreResultsAvailable() {
            return this.moreResultsAvailable;
        }

        public void setMoreResultsAvailable(boolean moreResultsAvailable) {
            this.moreResultsAvailable = moreResultsAvailable;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    public static class Constants {
        public final static String ROOT_ELEMENT_NAME = "patronQueryResults";
        public final static String TYPE_NAME = "patronQueryResultsType";
    }

    /**
     * A private class which exposes constants which define the XML element
     * names to use when this object is marshaled to XML.
     */
    public static class Elements {
        public final static String RESULTS = "results";
        public final static String RESULT_ELEM = "patron";
        public final static String TOTAL_ROW_COUNT = "totalRowCount";
        public final static String MORE_RESULTS_AVAILALBE = "moreResultsAvailable";
    }
}

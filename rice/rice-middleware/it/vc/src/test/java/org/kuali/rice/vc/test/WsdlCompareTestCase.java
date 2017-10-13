/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.vc.test;

import com.predic8.schema.ComplexType;
import com.predic8.schema.Sequence;
import com.predic8.soamodel.Difference;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wsdl.diff.WsdlDiffGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.test.BaselineTestCase;

import javax.xml.namespace.QName;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/*
*  Compatible Changes
*   - adding a new WSDL operation definition and associated message definitions
*   - adding a new WSDL port type definition and associated operation definitions
*   - adding new WSDL binding and service definitions
*   - adding a new optional XML Schema element or attribute declaration to a message definition
*   - reducing the constraint granularity of an XML Schema element or attribute of a message definition type
*   - adding a new XML Schema wildcard to a message definition type
*   - adding a new optional WS-Policy assertion
*   - adding a new WS-Policy alternative
*
* Incompatible Changes
*   - renaming an existing WSDL operation definition
*   - removing an existing WSDL operation definition
*   - changing the MEP of an existing WSDL operation definition
*   - adding a fault message to an existing WSDL operation definition
*   - adding a new required XML Schema element or attribute declaration to a message definition
*   - increasing the constraint granularity of an XML Schema element or attribute declaration of a message definition
*   - renaming an optional or required XML Schema element or attribute in a message definition
*   - removing an optional or required XML Schema element or attribute or wildcard from a message definition
*   - adding a new required WS-Policy assertion or expression
*   - adding a new ignorable WS-Policy expression (most of the time)
*/
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.ROLLBACK)
public abstract class WsdlCompareTestCase extends BaselineTestCase {
    private static final Logger LOG = Logger.getLogger(WsdlCompareTestCase.class);
    private static final String WSDL_URL = "wsdl.test.previous.url";
    private static final String WSDL_PREVIOUS_VERSION = "wsdl.test.previous.version";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private String previousVersion;

    private static final List<String> ignoreBreakageRegexps = Arrays.asList(
            ".*Position of any changed from .*", // change in position of an 'any' doesn't indicate a breakage for us
            ".*Position of element null changed.$" // this also indicates an 'any' changing position, ignore it too
    );

    public WsdlCompareTestCase(String moduleName) {
        super(moduleName);
    }

    protected List<String> verifyWsdlDifferences(Difference diff, String level) {
        List<String> results = new ArrayList<String>();

        if (diff.isBreaks()) {
            boolean ignore = false;
            for (String ignoreBreakageRegexp : ignoreBreakageRegexps) {
                if (diff.getDescription().matches(ignoreBreakageRegexp)) {
                    ignore = true;
                    break;
                }
            }

            if (ignore) {
                LOG.info(level + "non-breaking change" + diff.getDescription());
            } else {
                LOG.error(level + "breaking change: " + diff.getType() + diff.getDescription());
                results.add(level + diff.getDescription());
            }
        }


        //check for operation based sequence changes
        String opBreakageString = checkForOperationBasedChanges(diff);
        if (opBreakageString != null) {
            results.add(level + opBreakageString);
        }

        for (Difference moreDiff : diff.getDiffs())  {
            List<String> childBreakages = verifyWsdlDifferences(moreDiff, level + "  ");
            for (String childBreakage : childBreakages) {
                if (!diff.getDescription().trim().startsWith("Schema ")) {
                    results.add(level + diff.getDescription() + LINE_SEPARATOR + childBreakage);
                } else {
                    results.add(childBreakage);
                }
            }
        }

        return results;
    }

    /*
     * This method is essentially an extra check because java2ws marks parameters on methods as minOccurs=0, which means
     * as far as the wsdl comparison, adding a new parameter is ok, because it isn't required.
     *
     * Unfortunately, that adding the parameter breaks compatibility for us because it invalidates the java interface.
     *
     * So, This method goes through, and checks to see if the sequence change is on one of the services Operators.  If it
     * is on an operator, and there is a difference in type of the operator, we've broken compatibility and should fail.
     *
     * returns a string if there is a breakage, null otherwise
     */
    private String checkForOperationBasedChanges(Difference diff) {
        if ("sequence".equals(diff.getType())
                && diff.getA() != null
                && diff.getB() != null) {
            Sequence oldSequence = (Sequence)diff.getA();
            Sequence newSequence = (Sequence)diff.getB();
            if (newSequence.getParent() instanceof ComplexType) {
                ComplexType parent = (ComplexType)newSequence.getParent();
                String serviceName = newSequence.getSchema().getDefinitions().getName();
                PortType portType = newSequence.getSchema().getDefinitions().getPortType(serviceName);
                if (portType != null) {
                    Operation operation = portType.getOperation(parent.getName());

                    if (operation != null) {
                        return "Element cannot be added to a sequence if sequence is an Operation " +
                                diff.getDescription();
                    }
//                    assertTrue("Element cannot be added to a sequence if sequence is an Operation " + diff
//                            .getDescription(), operation == null);
                }
            }
        }
        return null;
    }

    protected List<Difference> compareWsdlDefinitions(String oldWsdl, String newWsdl) {
        WSDLParser parser = new WSDLParser();

        Definitions wsdl1;
        Definitions wsdl2;
        try {
            wsdl1 = parser.parse(oldWsdl);
        } catch (com.predic8.xml.util.ResourceDownloadException e) {
            LOG.info("Couldn't download " + oldWsdl + ", maybe the service didn't exist in this version?");
            return Collections.emptyList();
        }
        try {
            wsdl2 = parser.parse(newWsdl);
        } catch (com.predic8.xml.util.ResourceDownloadException e) {
            LOG.info("Couldn't download" + newWsdl + ", maybe the service didn't exist in this version?");
            return Collections.emptyList();
        }

        WsdlDiffGenerator diffGen = new WsdlDiffGenerator(wsdl1, wsdl2);
        return diffGen.compare();
    }

    protected String getPreviousVersionWsdlUrl(String wsdlFile, MavenVersion previousVersion) {

        StringBuilder oldWsdl = new StringBuilder(buildWsdlUrlPrefix(previousVersion.getOriginalForm()));
        oldWsdl.append("rice-");
        oldWsdl.append(getModuleName());
        oldWsdl.append("-api-");
        oldWsdl.append(previousVersion.getOriginalForm());
        oldWsdl.append("-");
        oldWsdl.append(wsdlFile);

        return oldWsdl.toString();
    }

    //String oldWsdl = MAVEN_REPO_PREFIX + MODULE + "-api/" + PREVIOUS_VERSION + "/rice-" + MODULE + "-api-" + PREVIOUS_VERSION + "-" + file.getName();
    private String buildWsdlUrlPrefix(String previousVersion) {
        String wsdlUrl = ConfigContext.getCurrentContextConfig().getProperty(WSDL_URL);

        if (StringUtils.isNotBlank(wsdlUrl)
                && StringUtils.isNotBlank(previousVersion)) {
            StringBuilder urlBuilder = new StringBuilder(wsdlUrl);
            if (!wsdlUrl.endsWith("/")) {
                urlBuilder.append("/");
            }
            urlBuilder.append("rice-");
            urlBuilder.append(getModuleName());
            urlBuilder.append("-api/");
            urlBuilder.append(previousVersion);
            urlBuilder.append("/");

            return urlBuilder.toString();

        } else {
            throw new RuntimeException("Couldn't build wsdl url prefix");
        }
    }

    /**
     * Allows an extending test to specify versions transitions of specific wsdls to omit from testing.  This can be
     * useful for ignoring version compatibility issues that have already been addressed in previously released
     * versions.
     *
     * @return a Map from wsdl file name (e.g. "DocumentTypeService.wsdl") to a list of {@link MavenVersion}s to filter
     */
    protected Map<String, List<VersionTransition>> getWsdlVersionTransitionBlacklists() {
        return new HashMap<String, List<VersionTransition>>();
    }

    protected void compareWsdlFiles(File[] wsdlFiles) {
        List<VersionCompatibilityBreakage> breakages = new ArrayList<VersionCompatibilityBreakage>();

        assertTrue("There should be wsdls to compare", wsdlFiles != null  && wsdlFiles.length > 0);

        MavenVersion previousVersion = new MavenVersion(getPreviousVersion(),
                "0" /*since this is the oldest version we'll deal with, setting the timestamp to 0 is ok for sorting */);
        MavenVersion currentVersion = getCurrentMavenVersion();
        List<MavenVersion> versions = getVersionRange(previousVersion, currentVersion);
        List<VersionTransition> transitions = generateVersionTransitions(currentVersion, versions);

        for (File wsdlFile : wsdlFiles) { // we're effectively iterating through each service
            if (wsdlFile.getName().endsWith(".wsdl")) {
                LOG.info("TESTING WSDL: " + wsdlFile.getAbsolutePath());
                String newWsdl = wsdlFile.getAbsolutePath();

                // do filtering to avoid testing blacklisted wsdl version transitions
                List<VersionTransition> wsdlTransitionBlacklist =
                        getWsdlVersionTransitionBlacklists().get(getServiceNameFromWsdlFile(wsdlFile));

                if (wsdlTransitionBlacklist == null) { wsdlTransitionBlacklist = Collections.emptyList(); }

                for (VersionTransition transition : transitions) if (!wsdlTransitionBlacklist.contains(transition)) {
                    breakages.addAll(testWsdlVersionTransition(currentVersion, wsdlFile, transition));
                } else {
                    LOG.info("Ignoring blacklisted " + transition);
                }
            }
        }

        if (!breakages.isEmpty()) {
            fail("https://jira.kuali.org/browse/KULRICE-9849\n" + buildBreakagesSummary(breakages));
        }
    }

    // Quick and dirty, and AFAIK very specific to Rice's conventions
    String getServiceNameFromWsdlFile(File wsdlFile) {
        String fileName = wsdlFile.getName();
        int beginIndex = 1 + fileName.lastIndexOf('-');
        int endIndex = fileName.lastIndexOf('.');

        return fileName.substring(beginIndex, endIndex);
    }

    /**
     * find breakages for the given wsdl's version transition
     * @param currentVersion the current version of Rice
     * @param wsdlFile the local wsdl file
     * @param transition the version transition to test
     * @return any breakages detected
     */
    private List<VersionCompatibilityBreakage> testWsdlVersionTransition(MavenVersion currentVersion, File wsdlFile, VersionTransition transition) {
        List<VersionCompatibilityBreakage> breakages = new ArrayList<VersionCompatibilityBreakage>();

        String fromVersionWsdlUrl = getPreviousVersionWsdlUrl(wsdlFile.getName(), transition.getFromVersion());
        String toVersionWsdlUrl = getPreviousVersionWsdlUrl(wsdlFile.getName(), transition.getToVersion());

        // current version isn't in the maven repo, use the local file
        if (transition.getToVersion().equals(currentVersion)) {
            toVersionWsdlUrl = wsdlFile.getAbsolutePath();
        }

        getPreviousVersionWsdlUrl(wsdlFile.getName(), transition.getToVersion());

        LOG.info("checking " + transition);

        if (fromVersionWsdlUrl == null) {
            LOG.warn("SKIPPING check, wsdl not found for " + fromVersionWsdlUrl);
        } else if (toVersionWsdlUrl == null) {
            LOG.warn("SKIPPING check, wsdl not found for " + toVersionWsdlUrl);
        } else {
            List<Difference> differences = compareWsdlDefinitions(fromVersionWsdlUrl, toVersionWsdlUrl);
            for (Difference diff : differences) {
                List<String> breakageStrings = verifyWsdlDifferences(diff, "");

                for (String breakage : breakageStrings) {
                    breakages.add(new VersionCompatibilityBreakage(
                            transition.fromVersion, transition.toVersion,
                            fromVersionWsdlUrl, toVersionWsdlUrl, breakage));
                }
            }
        }

        return breakages;
    }

    /**
     * calculate which version transitions to test given the current version, and the list of versions to consider.  The
     * results should contain a transition from the closest preceeding patch version at each minor version included in
     * the range to the nearest newer patch version within the current minor version.  That is hard to understand, so
     * an example is called for:
     * {@literal
     *             2.0.0,
     *             2.0.1,
     *                         2.1.0,
     *             2.0.2,
     * 1.0.4,
     *                         2.1.1,
     *                         2.1.2,
     *                                     2.2.0,
     *                         2.1.3,
     *                                     2.2.1,
     *                         2.1.4,
     *                                     2.2.2,
     *                         2.1.5,
     *                                     2.2.3,
     * }
     * So for the above version stream (which is sorted by time) the transitions for the range 1.0.4 to 2.2.3 would be:
     * {@literal
     * 1.0.4 -> 2.2.0,
     * 2.1.2 -> 2.2.0,
     * 2.1.3 -> 2.2.1,
     * 2.1.4 -> 2.2.2,
     * 2.1.5 -> 2.2.3,
     * }
     *
     * @param currentVersion the current version of Rice
     * @param versions the versions to consider
     * @return the calculated List of VersionTransitions
     */
    protected List<VersionTransition> generateVersionTransitions(MavenVersion currentVersion, List<MavenVersion> versions) {
        List<VersionTransition> results = new ArrayList<VersionTransition>();

        versions = new ArrayList<MavenVersion>(versions);
        Collections.sort(versions, mavenVersionTimestampComparator);

        // We want to iterate through from newest to oldest, so reverse
        Collections.reverse(versions);

        final MavenVersion currentMinorVersion = trimToMinorVersion(currentVersion);
        MavenVersion buildingTransitionsTo = currentVersion; // the version we're currently looking at transitions to

        // Keep track of minor versions we've used to build transitions to buildingTransitionsTo
        // because we want at most one transition from each minor version to any given version
        Set<MavenVersion> minorVersionsFrom = new HashSet<MavenVersion>();

        for (MavenVersion version : versions) if (version.compareTo(buildingTransitionsTo) < 0) {
            MavenVersion minorVersion = trimToMinorVersion(version);
            if (minorVersion.equals(currentMinorVersion)) {
                // One last transition to add, then start building transitions to this one
                results.add(new VersionTransition(version, buildingTransitionsTo));
                buildingTransitionsTo = version;

                // also, reset the blacklist of versions we can transition from
                minorVersionsFrom.clear();
            } else if (!minorVersionsFrom.contains(minorVersion)) {
                results.add(new VersionTransition(version, buildingTransitionsTo));
                minorVersionsFrom.add(minorVersion);
            }
        }

        // reverse our results so they go from old to new
        Collections.reverse(results);

        return results;
    }

    /**
     * Peel off the patch version and return a MavenVersion that just extends to the minor portion of the given version
     */
    private MavenVersion trimToMinorVersion(MavenVersion fullVersion) {
        return new MavenVersion(""+fullVersion.getNumbers().get(0)+"."+fullVersion.getNumbers().get(1), "0");
    }

    protected String buildBreakagesSummary(List<VersionCompatibilityBreakage> breakages) {
        StringBuilder errorsStringBuilder =
                new StringBuilder(LINE_SEPARATOR + "!!!!! Detected " + breakages.size() + " VC Breakages !!!!!"
                        + LINE_SEPARATOR);

        MavenVersion lastOldVersion = null;
        String lastOldWsdlUrl = "";

        for (VersionCompatibilityBreakage breakage : breakages) {
            // being lazy and using '!=' instead of '!lastOldVersion.equals(...)' to avoid NPEs and extra checks
            if (lastOldVersion != breakage.oldMavenVersion || lastOldWsdlUrl != breakage.oldWsdlUrl) {
                lastOldVersion = breakage.oldMavenVersion;
                lastOldWsdlUrl = breakage.oldWsdlUrl;

                errorsStringBuilder.append(LINE_SEPARATOR + "Old Version: " + lastOldVersion.getOriginalForm()
                        +", wsdl: " + lastOldWsdlUrl);
                errorsStringBuilder.append(LINE_SEPARATOR + "New Version: " + breakage.newMavenVersion.getOriginalForm()
                        +", wsdl: " + breakage.newWsdlUrl + LINE_SEPARATOR + LINE_SEPARATOR);
            }
            errorsStringBuilder.append(breakage.breakageMessage + LINE_SEPARATOR);
        }
        return errorsStringBuilder.toString();
    }

    public String getPreviousVersion() {
        if (StringUtils.isEmpty(this.previousVersion)) {
            this.previousVersion = ConfigContext.getCurrentContextConfig().getProperty(WSDL_PREVIOUS_VERSION);
        }
        return this.previousVersion;
    }

    public void setPreviousVersion(String previousVersion) {
        this.previousVersion = previousVersion;
    }

    @Override
    protected Lifecycle getLoadApplicationLifecycle() {
        SpringResourceLoader springResourceLoader = new SpringResourceLoader(new QName("VCTestHarnessResourceLoader"), "classpath:VCTestHarnessSpringBeans.xml", null);
        springResourceLoader.setParentSpringResourceLoader(getTestHarnessSpringResourceLoader());
        return springResourceLoader;
    }

    @Override
    protected List<Lifecycle> getPerTestLifecycles() {
        return new ArrayList<Lifecycle>();
    }

    @Override
    protected List<Lifecycle> getSuiteLifecycles() {
        List<Lifecycle> lifecycles = new LinkedList<Lifecycle>();

        /**
         * Initializes Rice configuration from the test harness configuration file.
         */
        lifecycles.add(new BaseLifecycle() {
            @Override
            public void start() throws Exception {
                Config config = getTestHarnessConfig();
                ConfigContext.init(config);
                super.start();
            }
        });

        return lifecycles;
    }

    /**
     * Returns the range of versions from previousVersion to currentVersion.  The versions will be in the numerical
     * range, but will be sorted by timestamp. Note that if either of the given versions aren't in maven central, they
     * won't be included in the results.
     * @param lowestVersion the lowest version in the range
     * @param highestVersion the highest version in the range
     * @return
     */
    protected List<MavenVersion> getVersionRange(MavenVersion lowestVersion, MavenVersion highestVersion) {
        ArrayList<MavenVersion> results = new ArrayList<MavenVersion>();

        if (highestVersion.compareTo(lowestVersion) <= 0) {
            throw new IllegalStateException("currentVersion " + highestVersion +
                    "  is <= previousVersion " + lowestVersion);
        }
        List<MavenVersion> riceVersions = getRiceMavenVersions();

        for (MavenVersion riceVersion : riceVersions) {
            if ( highestVersion.compareTo(riceVersion) > 0 &&
                    lowestVersion.compareTo(riceVersion) <= 0 &&
                    "".equals(riceVersion.getQualifier()) ) {
                results.add(riceVersion);
            }
        }

        return results;
    }

    // "cache" for rice maven versions, since these will not differ between tests and we have to hit
    // the maven central REST api to get them
    private static List<MavenVersion> riceMavenVersions = null;

    private static List<MavenVersion> getRiceMavenVersions() {
        if (riceMavenVersions == null) {
            String searchContent = getMavenSearchResults();
            riceMavenVersions = parseSearchResults(searchContent);

            Collections.sort(riceMavenVersions, mavenVersionTimestampComparator);

            LOG.info("Published versions, sorted by timestamp:");
            for (MavenVersion riceVersion : riceMavenVersions) {
                LOG.info("" + riceVersion.getTimestamp() + " " + riceVersion.getOriginalForm());
            }

        }
        return riceMavenVersions;
    }

    /**
     * @return the current version of Rice
     */
    private MavenVersion getCurrentMavenVersion() {
        return new MavenVersion(ConfigContext.getCurrentContextConfig().getProperty("rice.version"),
                ""+System.currentTimeMillis());
    }

    private static List<MavenVersion> parseSearchResults(String searchContent) {
        LinkedList<MavenVersion> riceVersions = new LinkedList<MavenVersion>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(searchContent);
        } catch (IOException e) {
            throw new RuntimeException("Can't parse maven search results", e);
        }
        JsonNode docsNode = rootNode.get("response").get("docs");

        for (JsonNode node : docsNode) {
            String versionStr = node.get("v").toString();
            String timestampStr = node.get("timestamp").toString();
            // System.out.println(versionStr);
            riceVersions.add(new MavenVersion(versionStr.replace(/* strip out surrounding quotes */ "\"",""), timestampStr));
        }

        Collections.sort(riceVersions);
        return riceVersions;
    }

    private static String getMavenSearchResults() {
        // using the maven search REST api specified here: http://search.maven.org/#api
        // this query gets all versions of Rice from maven central
        final String mavenSearchUrlString =
                "http://search.maven.org/solrsearch/select?q=g:%22org.kuali.rice%22+AND+a:%22rice%22&core=gav&rows=20&wt=json";

        URL mavenSearchUrl;

        try {
            mavenSearchUrl = new URL(mavenSearchUrlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException("can't parse maven search url", e);
        }

        StringBuilder contentBuilder = new StringBuilder();
        BufferedReader contentReader;
        try {
            contentReader = new BufferedReader(new InputStreamReader(mavenSearchUrl.openStream()));
            String line;
            while (null != (line = contentReader.readLine())) {
                contentBuilder.append(line + LINE_SEPARATOR);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to read search results", e);
        }
        return contentBuilder.toString();
    }

    /**
     * Utility class for parsing and comparing maven versions
     */
    protected static class MavenVersion implements Comparable<MavenVersion> {
        private static final Pattern PERIOD_PATTERN = Pattern.compile("\\.");
        private final List<Integer> numbers;
        private final String originalForm;
        private final String qualifier;
        private final Long timestamp;

        /**
         * Constructor that takes just a version string as an argument.  Beware, because 0 will be used as the timestamp!
         * @param versionString
         */
        public MavenVersion(String versionString) {
            this(versionString, "0");
        }

        public MavenVersion(String versionString, String timestampString) {
            originalForm = versionString;
            if (versionString == null || "".equals(versionString.trim())) {
                throw new IllegalArgumentException("empty or null version string");
            }
            String versionPart;
            int dashIndex = versionString.indexOf('-');
            if (dashIndex != -1 && versionString.length()-1 > dashIndex) {
                qualifier = versionString.substring(dashIndex+1).trim();
                versionPart = versionString.substring(0,dashIndex);
            } else {
                versionPart = versionString;
                qualifier = "";
            }
            String [] versionArray = PERIOD_PATTERN.split(versionPart);

            List<Integer> numbersBuilder = new ArrayList<Integer>(versionArray.length);

            for (String versionParticle : versionArray) {
                numbersBuilder.add(Integer.valueOf(versionParticle));
            }

            numbers = Collections.unmodifiableList(numbersBuilder);

            timestamp = Long.valueOf(timestampString);
        }

        @Override
        public int compareTo(MavenVersion that) {
            Iterator<Integer> thisNumbersIter = this.numbers.iterator();
            Iterator<Integer> thatNumbersIter = that.numbers.iterator();

            while (thisNumbersIter.hasNext()) {
                // all else being equal, he/she who has the most digits wins
                if (!thatNumbersIter.hasNext()) return 1;

                int numberComparison = thisNumbersIter.next().compareTo(thatNumbersIter.next());

                // if one is greater than the other, we've established primacy
                if (numberComparison != 0) return numberComparison;
            }
            // all else being equal, he/she who has the most digits wins
            if (thatNumbersIter.hasNext()) return -1;

            return compareQualifiers(this.qualifier, that.qualifier);
        }

        private static int compareQualifiers(String thisQ, String thatQ) {
            // no qualifier is considered greater than a qualifier (e.g. 1.0-SNAPSHOT is less than 1.0)
            if ("".equals(thisQ)) {
                if ("".equals(thatQ)) {
                    return 0;
                }
                return 1;
            } else if ("".equals(thatQ)) {
                return -1;
            }

            return thisQ.compareTo(thatQ);
        }

        public List<Integer> getNumbers() {
            return Collections.unmodifiableList(numbers);
        }

        public String getQualifier() {
            return qualifier;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public String getOriginalForm() {
            return originalForm;
        }

        @Override
        public String toString() {
            return "MavenVersion{" +
                    originalForm +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final MavenVersion that = (MavenVersion) o;

            if (!originalForm.equals(that.originalForm)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return originalForm.hashCode();
        }
    }

    /**
     * Comparator which can be used to sort MavenVersions by timestamp
     */
    private static final Comparator<MavenVersion> mavenVersionTimestampComparator = new Comparator<MavenVersion>() {
        @Override
        public int compare(MavenVersion o1, MavenVersion o2) {
            return o1.getTimestamp().compareTo(o2.getTimestamp());
        }
    };

    /**
     * A class representing a transition from one maven version to another
     */
    public static class VersionTransition {
        private final MavenVersion fromVersion;
        private final MavenVersion toVersion;

        public VersionTransition(MavenVersion fromVersion, MavenVersion toVersion) {
            this.fromVersion = fromVersion;
            this.toVersion = toVersion;
            if (fromVersion == null) throw new IllegalArgumentException("fromVersion must not be null");
            if (toVersion == null) throw new IllegalArgumentException("toVersion must not be null");
        }

        public VersionTransition(String fromVersion, String toVersion) {
            this(new MavenVersion(fromVersion), new MavenVersion(toVersion));
        }

        private MavenVersion getFromVersion() {
            return fromVersion;
        }

        private MavenVersion getToVersion() {
            return toVersion;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VersionTransition that = (VersionTransition) o;

            if (!fromVersion.equals(that.fromVersion)) return false;
            if (!toVersion.equals(that.toVersion)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = fromVersion.hashCode();
            result = 31 * result + toVersion.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "VersionTransition{" +
                    fromVersion.getOriginalForm() +
                    " -> " + toVersion.getOriginalForm() +
                    '}';
        }
    }

    /**
     * struct-ish class to hold data about a VC breakage
     */
    protected static class VersionCompatibilityBreakage {
        private final MavenVersion oldMavenVersion;
        private final MavenVersion newMavenVersion;
        private final String oldWsdlUrl;
        private final String newWsdlUrl;
        private final String breakageMessage;

        public VersionCompatibilityBreakage(MavenVersion oldMavenVersion, MavenVersion newMavenVersion, String oldWsdlUrl, String newWsdlUrl, String breakageMessage) {
            if (oldMavenVersion == null) throw new IllegalArgumentException("oldMavenVersion must not be null");
            if (newMavenVersion == null) throw new IllegalArgumentException("newMavenVersion must not be null");
            if (StringUtils.isEmpty(oldWsdlUrl)) throw new IllegalArgumentException("oldWsdlUrl must not be empty/null");
            if (StringUtils.isEmpty(newWsdlUrl)) throw new IllegalArgumentException("newWsdlUrl must not be empty/null");
            if (StringUtils.isEmpty(breakageMessage)) throw new IllegalArgumentException("breakageMessage must not be empty/null");
            this.oldWsdlUrl = oldWsdlUrl;
            this.newWsdlUrl = newWsdlUrl;
            this.oldMavenVersion = oldMavenVersion;
            this.newMavenVersion = newMavenVersion;
            this.breakageMessage = breakageMessage;
        }

        @Override
        public String toString() {
            return "VersionCompatibilityBreakage{" +
                    "oldMavenVersion=" + oldMavenVersion +
                    ", newMavenVersion=" + newMavenVersion +
                    ", oldWsdlUrl='" + oldWsdlUrl + '\'' +
                    ", newWsdlUrl='" + newWsdlUrl + '\'' +
                    ", breakageMessage='" + breakageMessage + '\'' +
                    '}';
        }
    }

}

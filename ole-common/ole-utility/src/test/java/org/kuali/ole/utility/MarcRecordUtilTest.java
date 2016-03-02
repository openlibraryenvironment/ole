package org.kuali.ole.utility;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.marc4j.marc.Record;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 3/2/2016.
 */
public class MarcRecordUtilTest {

    @Test
    public void testConvertMarcXmlContentToMarcRecord() throws Exception {
        String xmlContent = "<collection xmlns=\"http://www.loc.gov/MARC21/slim\">\n" +
                "<record>\n" +
                "  <leader>02855cam a2200529Ii 4500</leader>\n" +
                "  <controlfield tag=\"005\">20150718052731.8</controlfield>\n" +
                "  <controlfield tag=\"006\">m     o  d        </controlfield>\n" +
                "  <controlfield tag=\"007\">cr cnu|||unuuu</controlfield>\n" +
                "  <controlfield tag=\"008\">150413t20142014it a    ob    000 0 eng d</controlfield>\n" +
                "  <controlfield tag=\"001\">10214499</controlfield>\n" +
                "  <controlfield tag=\"003\">OLE</controlfield>\n" +
                "  <datafield tag=\"020\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">9788876425233</subfield>\n" +
                "    <subfield code=\"q\">electronic bk.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"020\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">8876425233</subfield>\n" +
                "    <subfield code=\"q\">electronic bk.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"020\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"z\">9788876425226</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"020\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">8876425225</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"020\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">9788876425226</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"024\" ind1=\"7\" ind2=\" \">\n" +
                "    <subfield code=\"a\">10.1007/978-88-7642-523-3</subfield>\n" +
                "    <subfield code=\"2\">doi</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"035\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">(OCoLC)907238315</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"040\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">N$T</subfield>\n" +
                "    <subfield code=\"b\">eng</subfield>\n" +
                "    <subfield code=\"e\">rda</subfield>\n" +
                "    <subfield code=\"e\">pn</subfield>\n" +
                "    <subfield code=\"c\">N$T</subfield>\n" +
                "    <subfield code=\"d\">N$T</subfield>\n" +
                "    <subfield code=\"d\">GW5XE</subfield>\n" +
                "    <subfield code=\"d\">E7B</subfield>\n" +
                "    <subfield code=\"d\">YDXCP</subfield>\n" +
                "    <subfield code=\"d\">COO</subfield>\n" +
                "    <subfield code=\"d\">NUI</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"049\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">MAIN</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"050\" ind1=\" \" ind2=\"4\">\n" +
                "    <subfield code=\"a\">QA312</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"072\" ind1=\" \" ind2=\"7\">\n" +
                "    <subfield code=\"a\">MAT</subfield>\n" +
                "    <subfield code=\"x\">005000</subfield>\n" +
                "    <subfield code=\"2\">bisacsh</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"072\" ind1=\" \" ind2=\"7\">\n" +
                "    <subfield code=\"a\">MAT</subfield>\n" +
                "    <subfield code=\"x\">034000</subfield>\n" +
                "    <subfield code=\"2\">bisacsh</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"082\" ind1=\"0\" ind2=\"4\">\n" +
                "    <subfield code=\"a\">515/.42</subfield>\n" +
                "    <subfield code=\"2\">23</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"245\" ind1=\"0\" ind2=\"0\">\n" +
                "    <subfield code=\"a\">Geometric measure theory and real analysis /</subfield>\n" +
                "    <subfield code=\"c\">edited by Luigi Ambrosio.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"264\" ind1=\" \" ind2=\"1\">\n" +
                "    <subfield code=\"a\">[Pisa, Italy] :</subfield>\n" +
                "    <subfield code=\"b\">Edizioni Della Normale,</subfield>\n" +
                "    <subfield code=\"c\">[2014]</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"264\" ind1=\" \" ind2=\"4\">\n" +
                "    <subfield code=\"c\">©2014</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"300\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">1 online resource :</subfield>\n" +
                "    <subfield code=\"b\">illustrations.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"336\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">text</subfield>\n" +
                "    <subfield code=\"b\">txt</subfield>\n" +
                "    <subfield code=\"2\">rdacontent</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"337\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">computer</subfield>\n" +
                "    <subfield code=\"b\">c</subfield>\n" +
                "    <subfield code=\"2\">rdamedia</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"338\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">online resource</subfield>\n" +
                "    <subfield code=\"b\">cr</subfield>\n" +
                "    <subfield code=\"2\">rdacarrier</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"490\" ind1=\"1\" ind2=\" \">\n" +
                "    <subfield code=\"a\">CRM series ;</subfield>\n" +
                "    <subfield code=\"v\">17</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"504\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">Includes bibliographical references.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"505\" ind1=\"0\" ind2=\" \">\n" +
                "    <subfield code=\"a\">Vladimir I. Bogachev: Sobolev classes on infinite-dimensional spaces -- Roberto Monti: Isoperimetric problem and minimal surfaces in the Heisenberg group -- Emanuele Spadaro: Regularity of higher codimension area minimizing integral currents -- Davide Vittone: The regularity problem for sub-Riemannian geodesics.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"520\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">In 2013, a school on Geometric Measure Theory and Real Analysis, organized by G. Alberti, C. De Lellis and myself, took place at the Centro De Giorgi in Pisa, with lectures by V. Bogachev, R. Monti, E. Spadaro and D. Vittone. The book collects the notes of the courses. The courses provide a deep and up to date insight on challenging mathematical problems and their recent developments: infinite-dimensional analysis, minimal surfaces and isoperimetric problems in the Heisenberg group, regularity of sub-Riemannian geodesics and the regularity theory of minimal currents in any dimension and codimension.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"588\" ind1=\"0\" ind2=\" \">\n" +
                "    <subfield code=\"a\">Online resource; title from PDF title page (EBSCO, viewed April 14, 2015).</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"650\" ind1=\" \" ind2=\"0\">\n" +
                "    <subfield code=\"a\">Geometric measure theory.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"650\" ind1=\" \" ind2=\"0\">\n" +
                "    <subfield code=\"a\">Geometric analysis.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"650\" ind1=\" \" ind2=\"7\">\n" +
                "    <subfield code=\"a\">MATHEMATICS / Calculus</subfield>\n" +
                "    <subfield code=\"2\">bisacsh</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"650\" ind1=\" \" ind2=\"7\">\n" +
                "    <subfield code=\"a\">MATHEMATICS / Mathematical Analysis</subfield>\n" +
                "    <subfield code=\"2\">bisacsh</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"655\" ind1=\" \" ind2=\"4\">\n" +
                "    <subfield code=\"a\">Electronic books.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"655\" ind1=\" \" ind2=\"0\">\n" +
                "    <subfield code=\"a\">Electronic books.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"700\" ind1=\"1\" ind2=\" \">\n" +
                "    <subfield code=\"a\">Ambrosio, Luigi,</subfield>\n" +
                "    <subfield code=\"e\">editor.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"776\" ind1=\"0\" ind2=\"8\">\n" +
                "    <subfield code=\"i\">Print version:</subfield>\n" +
                "    <subfield code=\"t\">Geometric measure theory and real analysis.</subfield>\n" +
                "    <subfield code=\"d\">[Place of publication not identified] : Edizioni Della Normale, 2015</subfield>\n" +
                "    <subfield code=\"z\">8876425225</subfield>\n" +
                "    <subfield code=\"w\">(OCoLC)898087256</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"830\" ind1=\" \" ind2=\"0\">\n" +
                "    <subfield code=\"a\">CRM series (Pisa, Italy) ;</subfield>\n" +
                "    <subfield code=\"v\">17.</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"856\" ind1=\"4\" ind2=\"0\">\n" +
                "    <subfield code=\"u\">http://link.springer.com/10.1007/978-88-7642-523-3</subfield>\n" +
                "    <subfield code=\"y\">SpringerLink</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"950\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">SpringerLink</subfield>\n" +
                "    <subfield code=\"b\">Springer English/International eBooks 2014 - Full Set</subfield>\n" +
                "  </datafield>\n" +
                "  <datafield tag=\"994\" ind1=\" \" ind2=\" \">\n" +
                "    <subfield code=\"a\">92</subfield>\n" +
                "    <subfield code=\"b\">CGU</subfield>\n" +
                "  </datafield>\n" +
                "</record>\n" +
                "</collection>";

        MarcRecordUtil marcRecordUtil = new MarcRecordUtil();
        List<Record> records = marcRecordUtil.convertMarcXmlContentToMarcRecord(xmlContent);
        assertTrue(CollectionUtils.isNotEmpty(records));
        System.out.println(records.get(0));
    }
}
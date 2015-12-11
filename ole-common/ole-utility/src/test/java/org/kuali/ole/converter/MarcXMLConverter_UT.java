package org.kuali.ole.converter;

import org.apache.commons.exec.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.marc4j.marc.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: ?
 * Time: ?
 * To change this template use File | Settings | File Templates.
 */
public class MarcXMLConverter_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(MarcXMLConverter_UT.class);


    @Test
    public void testConvertMarcXML() throws Exception {
        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        marcXMLConverter.generateMarcBean(new BibliographicRecord());
        URL resource = getClass().getResource("/org/kuali/ole/converter/InvYBP_Test_1207_2rec.mrc");
        File file = new File(resource.toURI());
        String marcXMLFileName = marcXMLConverter.convertRawMarcToXML(file);
        LOG.info("file:" + marcXMLFileName);
        assertNotNull(marcXMLFileName);
        File marcXMLFile = new File(marcXMLFileName);
        String marcXML = new FileUtil().readFile(marcXMLFile);
        LOG.info(marcXML);
        FileUtils.deleteQuietly(marcXMLFile);

    }

    @Test
    public void convertMRCtoXML() throws Exception {

        URL resource = getClass().getResource("/org/kuali/ole/converter/InvYBP_Test_1207_2rec.mrc");
        File marcFile = new File(resource.toURI());

        assertNotNull(marcFile);

        String rawMarc = FileUtils.readFileToString(marcFile);

        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        List<Record> records = marcXMLConverter.convertRawMarchToMarc(rawMarc);

        assertNotNull(records);


    }

    @Test
    public void convertMRCXmlToRecord() throws Exception {

        String marcXmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><collection xmlns=\"http://www.loc.gov/MARC21/slim\"><record><leader>05196ccm a2200637Ka 4500</leader><controlfield tag=\"001\">(OCoLC)807025384</controlfield><controlfield tag=\"003\">OCoLC</controlfield><controlfield tag=\"005\">20130123161628.1</controlfield><controlfield tag=\"008\">120815s2013    nyu            n    eng d</controlfield><datafield tag=\"040\" ind1=\" \" ind2=\" \"><subfield code=\"a\">BTCTA</subfield><subfield code=\"b\">eng</subfield><subfield code=\"c\">BTCTA</subfield><subfield code=\"d\">YDXCP</subfield><subfield code=\"d\">PSC</subfield><subfield code=\"d\">NhCcYBP</subfield></datafield><datafield tag=\"020\" ind1=\" \" ind2=\" \"><subfield code=\"a\">9780393920178</subfield></datafield><datafield tag=\"020\" ind1=\" \" ind2=\" \"><subfield code=\"a\">0393920178</subfield></datafield><datafield tag=\"035\" ind1=\" \" ind2=\" \"><subfield code=\"a\">(OCoLC)807025384</subfield></datafield><datafield tag=\"050\" ind1=\" \" ind2=\"4\"><subfield code=\"a\">MT91</subfield><subfield code=\"b\">.A58 2013</subfield></datafield><datafield tag=\"082\" ind1=\"0\" ind2=\"4\"><subfield code=\"a\">780.9</subfield><subfield code=\"2\">23</subfield></datafield><datafield tag=\"245\" ind1=\"0\" ind2=\"0\"><subfield code=\"a\">Anthology for Music in the nineteenth century /</subfield><subfield code=\"c\">Walter Frisch [editor].</subfield></datafield><datafield tag=\"260\" ind1=\" \" ind2=\" \"><subfield code=\"a\">New York :</subfield><subfield code=\"b\">W.W. Norton,</subfield><subfield code=\"c\">c2013.</subfield></datafield><datafield tag=\"300\" ind1=\" \" ind2=\" \"><subfield code=\"a\">1 v. of music (x, 337, 9 p.) ;</subfield><subfield code=\"c\">24 cm.</subfield></datafield><datafield tag=\"490\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">Western music in context : a Norton history</subfield></datafield><datafield tag=\"500\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Companion anthology to Music in the Nineteenth Century by Walter Frisch.</subfield></datafield><datafield tag=\"500\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Includes appendixes on orchestral score reading, instrument names and abbreviations, and a glossary of performance indications.</subfield></datafield><datafield tag=\"500\" ind1=\" \" ind2=\" \"><subfield code=\"a\">Series edited by Walter Frisch.</subfield></datafield><datafield tag=\"505\" ind1=\"0\" ind2=\"0\"><subfield code=\"t\">String Quartet in C♯ Minor, Op. 131, Movements 1 and 2 /</subfield><subfield code=\"r\">Ludwig van Beethoven --</subfield><subfield code=\"t\">Gretchen am Spinnrade, Op. 2 (D. 118) /</subfield><subfield code=\"r\">Franz Schubert --</subfield><subfield code=\"t\">Norma, Act 1, Scene 4, Casta diva /</subfield><subfield code=\"r\">Vincenzo Bellini --</subfield><subfield code=\"t\">Les Huguenots, Act 4, Scene 5, Benediction of the Swords /</subfield><subfield code=\"r\">Giacomo Meyerbeer --</subfield><subfield code=\"t\">Der Freischütz, Act 2, Scene 2, Leise, leise /</subfield><subfield code=\"r\">Carl Maria von Weber --</subfield><subfield code=\"t\">Elijah, Part 2, chorus, He, watching over Israel /</subfield><subfield code=\"r\">Felix Mendelssohn --</subfield><subfield code=\"t\">Symphonie fantastique, Movement 2, Un bal /</subfield><subfield code=\"r\">Hector Berlioz --</subfield><subfield code=\"t\">Années de pèlerinage I, Suisse, No. 4. Au bord d'une source /</subfield><subfield code=\"r\">Franz Liszt --</subfield><subfield code=\"t\">Nocturne in B♭ Minor, Op. 9, No. 1 /</subfield><subfield code=\"r\">Frédéric Chopin --</subfield><subfield code=\"t\">Dichterliebe, Op. 48, Im wunderschönen Monat Mai and Aus meinen Tränen sprießen /</subfield><subfield code=\"r\">Robert Schumann --</subfield><subfield code=\"t\">Mörike-Leider, No. 24, In der Frühe /</subfield><subfield code=\"r\">Hugo Wolf --</subfield><subfield code=\"t\">Boris Godunov, Act 2, Boris's monologue /</subfield><subfield code=\"r\">Modest Musorgsky --</subfield><subfield code=\"t\">Piano Trio No. 4 (Dumky), Op. 90, Movement 6 /</subfield><subfield code=\"r\">Antonín Dvořak --</subfield><subfield code=\"t\">Tristan und Isolde, Act 1, Scene 3, Isolde's Narrative /</subfield><subfield code=\"r\">Richard Wagner --</subfield><subfield code=\"t\">Rigoletto, Act 3, Scene #, Quartet /</subfield><subfield code=\"r\">Giuseppe Verdi --</subfield><subfield code=\"t\">Carmen, Act 1, No. 5, Habanera /</subfield><subfield code=\"r\">Georges Bizet --</subfield><subfield code=\"t\">Symphony No. 1 in C Minor, Op. 68, Movement 1 /</subfield><subfield code=\"r\">Johannes Brahms --</subfield><subfield code=\"t\">Symphony No. 6 in B Minor (Pathétique) /</subfield><subfield code=\"r\">Pyotr Il'yich Tchaikovsky --</subfield><subfield code=\"t\">La gallina, danse cubaine /</subfield><subfield code=\"r\">Louis Moreau Gottschalk --</subfield><subfield code=\"t\">Violin Sonata in A Minor, Op. 24, Movement 2 /</subfield><subfield code=\"r\">Amy Marcy Cheney Beach --</subfield><subfield code=\"t\">Lieder eines fahrenden Gesellen, No. 4, Die zwei blauen Augen /</subfield><subfield code=\"r\">Gustav Mahler --</subfield><subfield code=\"t\">La Bohème, Act 2, Musetta's Waltz /</subfield><subfield code=\"r\">Giacomo Puccini --</subfield><subfield code=\"t\">Fêtes galantes I, En sourdine /</subfield><subfield code=\"r\">Claude Debussy.</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"0\"><subfield code=\"a\">Musical analysis</subfield><subfield code=\"v\">Music collections.</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"0\"><subfield code=\"a\">Music appreciation</subfield><subfield code=\"v\">Music collections.</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"0\"><subfield code=\"a\">Music</subfield><subfield code=\"x\">History and criticism</subfield><subfield code=\"v\">Music collections.</subfield></datafield><datafield tag=\"650\" ind1=\" \" ind2=\"0\"><subfield code=\"a\">Music</subfield><subfield code=\"y\">19th century.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\" \"><subfield code=\"a\">Frisch, Walter,</subfield><subfield code=\"d\">1951-</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Beethoven, Ludwig van,</subfield><subfield code=\"d\">1770-1827.</subfield><subfield code=\"t\">Quartets,</subfield><subfield code=\"m\">strings,</subfield><subfield code=\"n\">no. 14, op. 131,</subfield><subfield code=\"r\">C♯ minor.</subfield><subfield code=\"p\">Adagio ma non troppo e molto espressivo.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Schubert, Franz,</subfield><subfield code=\"d\">1797-1828.</subfield><subfield code=\"t\">Gretchen am Spinnrade.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Bellini, Vincenzo,</subfield><subfield code=\"d\">1801-1835.</subfield><subfield code=\"t\">Norma.</subfield><subfield code=\"p\">Casta diva.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Meyerbeer, Giacomo,</subfield><subfield code=\"d\">1791-1864.</subfield><subfield code=\"t\">Huguenots.</subfield><subfield code=\"p\">Conjuration et Bénédiction des poignards.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Weber, Carl Maria von,</subfield><subfield code=\"d\">1786-1826.</subfield><subfield code=\"t\">Freischütz.</subfield><subfield code=\"p\">Leise, leise, fromme Weise.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Mendelssohn-Bartholdy, Felix,</subfield><subfield code=\"d\">1809-1847.</subfield><subfield code=\"t\">Elias.</subfield><subfield code=\"p\">Siehe der Hüter.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Berlioz, Hector,</subfield><subfield code=\"d\">1803-1869.</subfield><subfield code=\"t\">Symphonie fantastique.</subfield><subfield code=\"p\">Bal.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Liszt, Franz,</subfield><subfield code=\"d\">1811-1886.</subfield><subfield code=\"t\">Années de pèlerinage,</subfield><subfield code=\"n\">1ère année.</subfield><subfield code=\"p\">Au bord d'une source.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Chopin, Frédéric,</subfield><subfield code=\"d\">1810-1849.</subfield><subfield code=\"t\">Nocturnes,</subfield><subfield code=\"m\">piano,</subfield><subfield code=\"n\">op. 9.</subfield><subfield code=\"n\">No. 1.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Schumann, Robert,</subfield><subfield code=\"d\">1810-1856.</subfield><subfield code=\"t\">Dichterliebe.</subfield><subfield code=\"p\">Im wunderschönen Monat Mai.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Schumann, Robert,</subfield><subfield code=\"d\">1810-1856.</subfield><subfield code=\"t\">Dichterliebe.</subfield><subfield code=\"p\">Aus meinen Tränen spriessen.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Wolf, Hugo,</subfield><subfield code=\"d\">1860-1903.</subfield><subfield code=\"t\">Mörike-Lieder.</subfield><subfield code=\"p\">In der Frühe.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Mussorgsky, Modest Petrovich,</subfield><subfield code=\"d\">1839-1881.</subfield><subfield code=\"t\">Boris Godunov (Rimsky-Korsakov).</subfield><subfield code=\"p\">Monolog Borisa.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Dvořák, Antonín,</subfield><subfield code=\"d\">1841-1904.</subfield><subfield code=\"t\">Trios,</subfield><subfield code=\"m\">piano, strings,</subfield><subfield code=\"n\">op. 90,</subfield><subfield code=\"r\">E minor.</subfield><subfield code=\"p\">Lento maestoso.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Wagner, Richard,</subfield><subfield code=\"d\">1813-1883.</subfield><subfield code=\"t\">Tristan und Isolde.</subfield><subfield code=\"p\">Wie lachend sie mir Lieder singen.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Verdi, Giuseppe,</subfield><subfield code=\"d\">1813-1901.</subfield><subfield code=\"t\">Rigoletto.</subfield><subfield code=\"p\">Quartetto.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Bizet, Georges,</subfield><subfield code=\"d\">1838-1875.</subfield><subfield code=\"t\">Carmen.</subfield><subfield code=\"p\">Habanera.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Brahms, Johannes,</subfield><subfield code=\"d\">1833-1897.</subfield><subfield code=\"t\">Symphonies,</subfield><subfield code=\"n\">no. 1, op. 68,</subfield><subfield code=\"r\">C minor.</subfield><subfield code=\"p\">Allegro.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Tchaikovsky, Peter Ilich,</subfield><subfield code=\"d\">1840-1893.</subfield><subfield code=\"t\">Symphonies,</subfield><subfield code=\"n\">no. 6, op. 74,</subfield><subfield code=\"r\">B minor.</subfield><subfield code=\"p\">Adagio.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Gottschalk, Louis Moreau,</subfield><subfield code=\"d\">1829-1869.</subfield><subfield code=\"t\">Gallina.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Beach, H. H. A.,</subfield><subfield code=\"c\">Mrs.,</subfield><subfield code=\"d\">1867-1944.</subfield><subfield code=\"t\">Sonata,</subfield><subfield code=\"m\">violin, piano,</subfield><subfield code=\"n\">op. 34,</subfield><subfield code=\"r\">A minor;</subfield><subfield code=\"p\">Molto vivace.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Mahler, Gustav,</subfield><subfield code=\"d\">1860-1911.</subfield><subfield code=\"t\">Lieder eines fahrenden Gesellen.</subfield><subfield code=\"p\">Zwei blauen Augen.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Puccini, Giacomo,</subfield><subfield code=\"d\">1858-1924.</subfield><subfield code=\"t\">Bohème.</subfield><subfield code=\"p\">Quando me'n vo.</subfield></datafield><datafield tag=\"700\" ind1=\"1\" ind2=\"2\"><subfield code=\"a\">Debussy, Claude,</subfield><subfield code=\"d\">1862-1918.</subfield><subfield code=\"t\">Fêtes galantes,</subfield><subfield code=\"n\">1. recueil.</subfield><subfield code=\"p\">En sourdine.</subfield></datafield><datafield tag=\"830\" ind1=\" \" ind2=\"0\"><subfield code=\"a\">Western music in context : a Norton history.</subfield></datafield><datafield tag=\"980\" ind1=\" \" ind2=\" \"><subfield code=\"a\">99964307763</subfield><subfield code=\"b\">MUS</subfield><subfield code=\"c\">Gen</subfield><subfield code=\"d\">JRL</subfield><subfield code=\"e\">YBP</subfield><subfield code=\"f\">SL</subfield><subfield code=\"g\">509517</subfield><subfield code=\"h\">411115</subfield><subfield code=\"i\">20151123</subfield><subfield code=\"j\">40.58</subfield><subfield code=\"k\">.o99964307763</subfield><subfield code=\"l\">Paper</subfield><subfield code=\"m\">50.10</subfield><subfield code=\"n\">150923</subfield><subfield code=\"o\">162741</subfield><subfield code=\"p\">LEEH</subfield><subfield code=\"q\">1</subfield></datafield><datafield tag=\"981\" ind1=\" \" ind2=\" \"><subfield code=\"e\">B/PR</subfield></datafield><datafield tag=\"999\" ind1=\" \" ind2=\" \"><subfield code=\"n\">514-0</subfield><subfield code=\"c\">JRL</subfield><subfield code=\"r\">170</subfield></datafield></record></collection>";

        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        List<Record> records = marcXMLConverter.convertMarcXmlToRecord(marcXmlContent);

        assertNotNull(records);

        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record record = iterator.next();
            String rawContent = marcXMLConverter.convertMarcRecordToRawMarcContent(record);
            assertNotNull(rawContent);
            System.out.println(rawContent);
        }


    }
}

package org.kuali.ole.sys.context;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.oclc.NettyClient;
import org.kuali.ole.oclc.OCLCNettyServerHandler;
import org.kuali.ole.service.NettyHandler;
import org.kuali.ole.service.NettyServer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;

/**
 * Created by angelind on 2/23/16.
 */
public class OLEInitializeListenerTest{

    @Test
    public void testInitializeNettyHandlers() {
        OLEInitializeListener oleInitializeListener = new OLEInitializeListener();
        String host = "localhost";
        int port = 10001;

        Map<Integer, NettyHandler> nettyHandlerMap = new HashMap<>();
        nettyHandlerMap.put(Integer.valueOf(port), new OCLCNettyServerHandler("http://localhost:8080/olefs"));

        oleInitializeListener.startNettyServers(nettyHandlerMap);

        String request = "This is the request for TCP Server";
        String response = new NettyClient().sendRequestToNettyServer(request, host, port);
        assertTrue(StringUtils.isNotBlank(response));
        System.out.println(response);
    }

    @Test
    public void sendOCLCRequestToOLETest() {
        String host = "localhost";
        int port = 10001;

        String request = "02957pam a2200481 i 4500001001300000003000600013005001700019008004100036010001700077040008000094020002900174020002600203035002100229042000800250043003000258050002500288082001800313100003000331245010400361264006500465300002800530336002600558337002800584338002700612504005100639520106200690651002101752651004801773600003401821651004901855651003701904651003001941600006001971650005202031650005502083651003702138651003802175651004502213648002102258655003902279980013702318999002002455\u001Eocn916684758\u001EOCoLC\u001E20160203075918.4\u001E150805s2016    mauab    b    001 0 eng c\u001E  \u001Fa  2015026007\u001E  \u001FaMH/DLC\u001Fbeng\u001Ferda\u001FcHLS\u001FdDLC\u001FdOCLCO\u001FdOCLCF\u001FdYDXCP\u001FdBTCTA\u001FdBDX\u001FdOCLCQ\u001FdNhCcYBP\u001E  \u001Fa9780674659810\u001Fqhardcover\u001E  \u001Fa0674659813\u001Fqhardcover\u001E  \u001Fa(OCoLC)916684758\u001E  \u001Fapcc\u001E  \u001Faa-ch---\u001Fan-us---\u001Faa-cc---\u001E00\u001FaDS799.816\u001Fb.L55 2016\u001E00\u001Fa951.24905\u001F223\u001E1 \u001FaLin, Hsiao-ting,\u001Feauthor.\u001E10\u001FaAccidental state :\u001FbChiang Kai-shek, the United States, and the making of Taiwan /\u001FcHsiao-ting Lin.\u001E 1\u001FaCambridge, Massachusetts :\u001FbHarvard University Press,\u001Fc2016.\u001E  \u001Favii, 338 pages ;\u001Fc25 cm\u001E  \u001Fatext\u001Fbtxt\u001F2rdacontent\u001E  \u001Faunmediated\u001Fbn\u001F2rdamedia\u001E  \u001Favolume\u001Fbnc\u001F2rdacarrier\u001E  \u001FaIncludes bibliographical references and index.\u001E  \u001Fa\"Accidental State explores the historical formation in the late 1940s and early 1950s of a de facto state on Taiwan separate from the de facto state ruling the Chinese mainland. The peculiar status of the Republic of China on Taiwan as an independent state but not quite a nation-state is important for our understanding of modern East Asia. Too often we have tended to view the existence of the two political entities across the Taiwan Strait as a logical and most likely consequence of the Chinese civil war fought bitterly after World War II between Chiang Kai-shek and Mao Zedong. This book offers a new historical outlook, arguing that the making of the separate Taiwan state was by no means the result of deliberate forethought and planning either by the United States, the Nationalists, or the Communists. The process of this statemaking was intriguing, contingent, and inadvertent, and was never intended when the fate of Taiwan was first planned by FDR, Chiang Kai-shek, and Winston Churchill in the middle of World War II.\"--Provided by publisher.\u001E 0\u001FaTaiwan\u001FxHistory.\u001E 0\u001FaTaiwan\u001FxPolitics and government\u001Fy1945-1975.\u001E10\u001FaChiang, Kai-shek,\u001Fd1887-1975.\u001E 0\u001FaUnited States\u001FxForeign relations\u001Fy1945-1953.\u001E 0\u001FaTaiwan\u001FxForeign relations\u001Fy1945-\u001E 0\u001FaChina\u001FxForeign relations.\u001E17\u001FaChiang, Kai-shek,\u001Fd1887-1975.\u001F2fast\u001F0(OCoLC)fst00054695\u001E 7\u001FaDiplomatic relations.\u001F2fast\u001F0(OCoLC)fst01907412\u001E 7\u001FaPolitics and government.\u001F2fast\u001F0(OCoLC)fst01919741\u001E 7\u001FaChina.\u001F2fast\u001F0(OCoLC)fst01206073\u001E 7\u001FaTaiwan.\u001F2fast\u001F0(OCoLC)fst01207854\u001E 7\u001FaUnited States.\u001F2fast\u001F0(OCoLC)fst01204155\u001E 7\u001FaSince 1945\u001F2fast\u001E 7\u001FaHistory.\u001F2fast\u001F0(OCoLC)fst01411628\u001E  \u001Fa40025678185\u001FbHIS2\u001FcGen\u001FdJRL\u001FeYBP\u001Fg509530\u001Fh503963\u001Fi20160208\u001Fj32.36\u001FlCloth\u001Fm39.95\u001Fq1\u001FxBRNO\u001FyPhilip Morrison Burno\u001FzBRNO Public Display\u001E  \u001Fn514-0\u001FcJRL\u001Fr170\u001E\u001D";
        String response = new NettyClient().sendRequestToNettyServer(request, host, port);
        assertTrue(StringUtils.isNotBlank(response));
        System.out.println(response);
    }
}

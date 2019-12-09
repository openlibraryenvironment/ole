package org.kuali.ole.gobi.dao;

import org.apache.commons.io.IOUtils;
import org.kuali.ole.utility.OleHttpRestClient;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.marc4j.MarcReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.impl.DataFieldImpl;
import org.marc4j.marc.impl.SubfieldImpl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pvsubrah on 10/14/15.
 */
public class BibDAO extends PlatformAwareDaoBaseJdbc {

    public int updateYPBOrderKeyContent(String bibiId, String ybpOrderKey){

        StringTokenizer stringTokenizer = new StringTokenizer(bibiId, "-");
        if(stringTokenizer.countTokens() == 2){
            stringTokenizer.nextToken();
           bibiId =  stringTokenizer.nextToken();
        }

        String bibContent = getBibById(bibiId);

        MarcReader reader = new MarcXmlReader(IOUtils.toInputStream(bibContent));
        Record record = null;
        while (reader.hasNext()) {
            record = reader.next();
            DataField dataField = (DataField) record.getVariableField("980");
            if (null != dataField) {
                record.getDataFields().remove(dataField);
            }
            dataField = new DataFieldImpl();
            dataField.setTag("980");
            dataField.setIndicator1(' ');
            dataField.setIndicator2(' ');

            Subfield sf = new SubfieldImpl();
            sf.setCode('a');
            sf.setData(ybpOrderKey);

            dataField.addSubfield(sf);

            record.getDataFields().add(dataField);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MarcWriter marcWriter = new MarcXmlWriter(byteArrayOutputStream);
        marcWriter.write(record);
        marcWriter.close();
        String updatedBibContent = byteArrayOutputStream.toString();

        int result = saveOrUpdate(bibiId, updatedBibContent);

        if(result == 1){
            //indexBib(Integer.valueOf(bibiId));
        }
        return result;
    }

    public String getBibById(String bibId) {

        StringBuilder query = new StringBuilder();
            query.append("select content from ole_ds_bib_t where bib_id = '")
                    .append(bibId)
                    .append("'");

            List<Map<String, Object>> maps = executeQuery(query.toString());
            for (Iterator<Map<String, Object>> iterator = maps.iterator(); iterator.hasNext(); ) {
                Map<String, Object> objectMap = iterator.next();
                String content = (String) objectMap.get("content");
                return content;
            }
            return null;
        }

    protected List<Map<String, Object>> executeQuery(String query) {
        return getSimpleJdbcTemplate().queryForList(query);
    }

    public int saveOrUpdate(String bibId, String updatedBibContent) {
        StringBuilder query = new StringBuilder();
        query.append("update ole_ds_bib_t set content = ? where bib_id = ?");

        Object[] params = {updatedBibContent, bibId};
        int update = getSimpleJdbcTemplate().update(query.toString(), updatedBibContent, bibId);
        return update;
    }

    public String indexBib(int bibId) throws InterruptedException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-mm-dd");

        String docstoreUrl = ConfigContext.getCurrentContextConfig().getProperty("ole.docstore.url.base");
        String requestParameter = "action=start&batchSize=10&startIndex=" + bibId + "&endIndex=" + bibId+1 + "&updateDate=" + simpleDateFormat.format(new Date());
        String response = new OleHttpRestClient().sendPostRequest(docstoreUrl+"/rebuildIndex", requestParameter,"json");
        return response;
    }
}

package org.kuali.ole.externalds;


/*import net.sf.jz3950.Association;
import net.sf.jz3950.RecordResultSet;
import net.sf.jz3950.query.PrefixQuery;*/
import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.search.SearchParams;

import java.util.ArrayList;
import java.util.List;

public class Z3950DataSource
        extends AbstractExternalDataSource {

   /* private static final Logger LOG = Logger.getLogger(Z3950DataSource.class);
    private static final int RESULT_SET_ITERATION_LIMIT = 80;


    @Override
    public List<String> searchForBibs(SearchParams searchParams, DataSourceConfig dataSourceConfigInfo)
            throws Exception {

        Association association = new Association();
        String domainName = dataSourceConfigInfo.getDomainName();
        String port = dataSourceConfigInfo.getPortNum();
        String loginIdDbName = dataSourceConfigInfo.getLoginId();
        String logInId = "";
        String dataBase ="" ;
        if(loginIdDbName != null && !loginIdDbName.trim().equals("")) {
            String[] parts = loginIdDbName.split("/");
            if(parts.length==2){
                dataBase = parts[1];
                logInId = parts[0];
            } else {
                dataBase = parts[0];
            }

        }
        List<String> dataBaseList = new ArrayList<String>();
        List<String> results=new ArrayList<String>() ;
        dataBaseList.add(dataBase);
        String password = dataSourceConfigInfo.getPassword();
        String authKey =dataSourceConfigInfo.getAuthKey();
        LOG.info("Connecting to external datasource: domainName = " + dataSourceConfigInfo.getDomainName() +" portNumber = "+dataSourceConfigInfo.getPortNum());
        if(logInId != null && !logInId.trim().equals("") && password != null && ! password.trim().equals("")){
            association.connect(domainName.trim(), Integer.parseInt(port.trim()),logInId.trim(), password.trim());
        } else{
            association.connect(domainName, Integer.parseInt(port));
        }
        //String query="@attrset bib-1 @attr 1=4 \"advanced java\"";
        Z3950QueryBuilder z3950QueryBuilde = new Z3950QueryBuilder();
        String query = z3950QueryBuilde.buildQuery(searchParams);
        LOG.info("Z39.50 Query:" + query);
        results = iterateOverPartOfResultSet(association.search(new PrefixQuery(dataBaseList, query)));
        LOG.info("Z39.50 Query results size: " + results.size());
        association.disconnect();
        return results;
    }
    private List iterateOverPartOfResultSet(RecordResultSet resultSet) {
        List<String> results = new ArrayList<String>();
        LOG.info(("Iterating over maximum of " + RESULT_SET_ITERATION_LIMIT + " record(s) from "
                + resultSet.getTotalResults() + " record(s) in total"));
        int recordCount = 0;
        try{
        while(resultSet.hasNext()){
                net.sf.jz3950.record.Record record =   resultSet.next();
                String tempdata = record.getData() ;
                   results.add(tempdata);
                       if (++recordCount == RESULT_SET_ITERATION_LIMIT) {
                             break;
                       }
            }
         } catch(Exception ex){
            LOG.error(ex.getMessage());
            ex.printStackTrace();
        }

        return results;
    }*/
}

package org.kuali.api.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/29/12
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class DateAdapter extends XmlAdapter<String, Date> {

        @Override
        public Date unmarshal(String dateString) throws Exception {
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = formater.parse(dateString);
            Date sqlDate= new Date(date.getTime());
            return sqlDate;
        }

        @Override
        public String marshal(Date v) throws Exception {
            return v.toString();
        }

}

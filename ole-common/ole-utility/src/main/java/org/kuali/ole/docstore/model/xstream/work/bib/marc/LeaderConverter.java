package org.kuali.ole.docstore.model.xstream.work.bib.marc;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.LeaderTag;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 9/18/13
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeaderConverter implements Converter{
    private static final String leader = "00000nam a2200000 a 4500";
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        LeaderTag marcLeader = (LeaderTag) source;
        writer.setValue(getAppendedLeader(marcLeader.getValue()));
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        LeaderTag marcLeader = new LeaderTag();
        marcLeader.setValue((reader.getValue()));
        return marcLeader;
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(LeaderTag.class);
    }

    private String getAppendedLeader(String value){
        if(value==null || StringUtils.isEmpty(value)){
            return leader;
        }
        else if(value.length()>=24){
            return value;
        }
        else if(!value.startsWith(" ") && value.length()<24){
            return value;
        }
        else{
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            for(int i=value.length();i<24;i++){
                sb.append(leader.charAt(i));
            }
            return sb.toString();
        }
    }
}

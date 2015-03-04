package org.kuali.ole.circ;

import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.kuali.ole.circ.pojos.CircReportData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CircReport {

	public List<CircReportData> getReportData() throws IOException{
		
		List<CircReportData> reportDataList = new ArrayList<CircReportData>();
		
		List<String> lines = FileUtils.readLines(new File(System
				.getProperty("user.home")
				+ "/kuali/main/local/reports/circ-report.txt"));
		for (Iterator<String> itr = lines.iterator(); itr.hasNext();) {
			CircReportData circReportData = new CircReportData();
			
			String line = (String) itr.next();
			JSONObject jo = JSONObject.fromObject(line);
			String firstName = (String) jo.get("firstName");
			circReportData.setFirstName(firstName);
			
			String lastName = (String) jo.get("lastName");
			circReportData.setLastName(lastName);

			String patronType = (String) jo.get("patronType");
			circReportData.setPatronType(patronType);

			String checkoutDate = (String) jo.get("checkoutDate");
			circReportData.setCheckoutDate(checkoutDate);

			JSONObject ja = (JSONObject) jo.get("item");

			String itemId = (String) ja.get("itemId");
			circReportData.setItemId(itemId);
			
			String itemType = (String) ja.get("itemType");
			circReportData.setItemType(itemType);
			
			String itemLocation = (String) ja.get("itemLocation");
			circReportData.setItemLocation(itemLocation);

			String itemBarcode = (String) ja.get("itemBarcode");
			circReportData.setItemBarcode(itemBarcode);
			
			reportDataList.add(circReportData);
		}
		
		return reportDataList;
	}

    public String userName = "root";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

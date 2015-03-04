package org.kuali.ole.circ;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class LogReaderTest {

	@Test
	public void readLog() throws Exception {
		List<String> lines = FileUtils.readLines(new File(System
				.getProperty("user.home")
				+ "/kuali/main/local/reports/circ-report.txt"));
		for (Iterator<String> itr = lines.iterator(); itr.hasNext();) {
			String line = (String) itr.next();
			JSONObject jo = JSONObject.fromObject(line);
			String firstName = (String) jo.get("firstName");
			assertNotNull(firstName);
			System.out.println("First Name: " + firstName);

			String lastName = (String) jo.get("lastName");
			assertNotNull(lastName);
			System.out.println("Last Name: " + lastName);

			String patronType = (String) jo.get("patronType");
			assertNotNull(patronType);
			System.out.println("Patron Type: " + patronType);

			String checkoutDate = (String) jo.get("checkoutDate");
			assertNotNull(patronType);
			System.out.println("Checkout Date: " + checkoutDate);
			

			JSONObject ja = (JSONObject) jo.get("item");

			assertNotNull(ja);

			String itemId = (String) ja.get("itemId");
			System.out.println("Item Id: " + itemId);

			String itemType = (String) ja.get("itemType");
			System.out.println("Item Type: " + itemType);

			String itemLocation = (String) ja.get("itemLocation");
			System.out.println("Item Location: " + itemLocation);

			String itemBarcode = (String) ja.get("itemBarcode");
			System.out.println("Item Barcode: " + itemBarcode);
		}

	}

}

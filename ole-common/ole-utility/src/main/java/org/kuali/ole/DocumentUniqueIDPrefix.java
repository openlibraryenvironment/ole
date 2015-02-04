package org.kuali.ole;

import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 7/4/13
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentUniqueIDPrefix {

    public static String PREFIX_WORK_BIB_MARC = "wbm";
    public static String PREFIX_WORK_BIB_DUBLIN = "wbd";
    public static String PREFIX_WORK_BIB_DUBLIN_UNQUALIFIED = "wbu";
    public static String PREFIX_WORK_INSTANCE_OLEML = "wno";
    public static String PREFIX_WORK_HOLDINGS_OLEML = "who";
    public static String PREFIX_WORK_ITEM_OLEML = "wio";
    public static String PREFIX_WORK_EINSTANCE_OLEML = "wen";
    public static String PREFIX_WORK_EHOLDINGS_OLEML = "weh";
    public static String PREFIX_WORK_LICENSE_ONIXPL = "wlo";
    public static String PREFIX_WORK_LICENSE_ATTACHMENT = "wla";

    public static Map<String, Map<String, String>> categoryTypeFormatMap = new HashMap<>();
    public static Map<String, String> prefixMap = new HashMap<>();

    static {
        String key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.MARC.getCode();
        prefixMap.put(key, PREFIX_WORK_BIB_MARC);

        Map<String, String> map = new HashMap<String, String>();
        map.put("category", DocCategory.WORK.getCode());
        map.put("type", DocType.BIB.getDescription());
        map.put("format", DocFormat.MARC.getCode());
        categoryTypeFormatMap.put(PREFIX_WORK_BIB_MARC, map);

        key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.DUBLIN_CORE.getCode();
        prefixMap.put(key, PREFIX_WORK_BIB_DUBLIN);

        map = new HashMap<String, String>();
        map.put("category", DocCategory.WORK.getCode());
        map.put("type", DocType.BIB.getDescription());
        map.put("format", DocFormat.DUBLIN_CORE.getCode());
        categoryTypeFormatMap.put(PREFIX_WORK_BIB_DUBLIN, map);


        key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.DUBLIN_UNQUALIFIED.getCode();
        prefixMap.put(key, PREFIX_WORK_BIB_DUBLIN_UNQUALIFIED);

        map = new HashMap<String, String>();
        map.put("category", DocCategory.WORK.getCode());
        map.put("type", DocType.BIB.getDescription());
        map.put("format", DocFormat.DUBLIN_UNQUALIFIED.getCode());
        categoryTypeFormatMap.put(PREFIX_WORK_BIB_DUBLIN_UNQUALIFIED, map);

        key = DocCategory.WORK.getCode() + DocType.INSTANCE.getCode() + DocFormat.OLEML.getCode();
        prefixMap.put(key, PREFIX_WORK_INSTANCE_OLEML);

        map = new HashMap<String, String>();
        map.put("category", DocCategory.WORK.getCode());
        map.put("type", DocType.INSTANCE.getDescription());
        map.put("format", DocFormat.OLEML.getCode());
        categoryTypeFormatMap.put(PREFIX_WORK_INSTANCE_OLEML, map);

        key = DocCategory.WORK.getCode() + DocType.HOLDINGS.getCode() + DocFormat.OLEML.getCode();
        prefixMap.put(key, PREFIX_WORK_HOLDINGS_OLEML);

        map = new HashMap<String, String>();
        map.put("category", DocCategory.WORK.getCode());
        map.put("type", DocType.HOLDINGS.getDescription());
        map.put("format", DocFormat.OLEML.getCode());
        categoryTypeFormatMap.put(PREFIX_WORK_HOLDINGS_OLEML, map);

        map = new HashMap<String, String>();

        key = DocCategory.WORK.getCode() + DocType.ITEM.getCode() + DocFormat.OLEML.getCode();
        prefixMap.put(key, PREFIX_WORK_ITEM_OLEML);

        map = new HashMap<String, String>();
        map.put("category", DocCategory.WORK.getCode());
        map.put("type", DocType.ITEM.getDescription());
        map.put("format", DocFormat.OLEML.getCode());
        categoryTypeFormatMap.put(PREFIX_WORK_ITEM_OLEML, map);


        key = DocCategory.WORK.getCode() + DocType.EINSTANCE.getCode() + DocFormat.OLEML.getCode();
        prefixMap.put(key, PREFIX_WORK_EINSTANCE_OLEML);

        map = new HashMap<String, String>();
        map.put("category", DocCategory.WORK.getCode());
        map.put("type", DocType.EINSTANCE.getDescription());
        map.put("format", DocFormat.OLEML.getCode());
        categoryTypeFormatMap.put(PREFIX_WORK_EINSTANCE_OLEML, map);

        key = DocCategory.WORK.getCode() + DocType.EHOLDINGS.getCode() + DocFormat.OLEML.getCode();
        prefixMap.put(key, PREFIX_WORK_EHOLDINGS_OLEML);

        map = new HashMap<String, String>();
        map.put("category", DocCategory.WORK.getCode());
        map.put("type", DocType.EHOLDINGS.getDescription());
        map.put("format", DocFormat.OLEML.getCode());
        categoryTypeFormatMap.put(PREFIX_WORK_EHOLDINGS_OLEML, map);

        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.ONIXPL;
        prefixMap.put(key, PREFIX_WORK_LICENSE_ONIXPL);

        map = new HashMap<String, String>();
        map.put("category", DocCategory.WORK.getCode());
        map.put("type", DocType.LICENSE.getDescription());
        map.put("format", DocFormat.ONIXPL.getCode());
        categoryTypeFormatMap.put(PREFIX_WORK_LICENSE_ONIXPL, map);

    }


    public static String getPrefix(String category, String type, String format) {
        return prefixMap.get(category + type + format);
    }

    public static boolean hasPrefix(String uuid) {
        if (uuid != null) {
            String[] prefixes = uuid.split("-");
            if (prefixes.length == 2) {
                return true;
            }
        }
        return false;
    }

    public static Map<String, String> getCategoryTypeFormat(String uuid) {
        String[] prefixes = uuid.split("-");
        return categoryTypeFormatMap.get(prefixes[0]);
    }

    public static String getBibFormatType(String uuid) {
        Map<String, String> map = getCategoryTypeFormat(uuid);
        return map.get("format");
    }

    public static String getPrefixedId(String prefix, String id) {
        if (hasPrefix(id)) {
            return id;
        }
        return prefix + "-" + id;
    }

    public static String getDocumentId(String uuid) {
        if (hasPrefix(uuid)) {
            String[] prefixes = uuid.split("-");
            return prefixes[1];
        }
        return uuid;
    }
}

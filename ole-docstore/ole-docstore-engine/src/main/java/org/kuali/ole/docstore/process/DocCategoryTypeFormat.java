package org.kuali.ole.docstore.process;

import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: SG7940
 * Date: 6/19/12
 * Time: 9:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocCategoryTypeFormat {

    public List<String> getCategories() {
        List<String> categoryList = new ArrayList<String>();
        categoryList.add(DocCategory.WORK.getCode());
        return categoryList;
    }

    public List<String> getDocTypes(String category) {
        List<String> typeList = new ArrayList<String>();
        if (category.equals(DocCategory.WORK.getCode())) {
            typeList.add(DocType.BIB.getDescription());
            typeList.add(DocType.INSTANCE.getDescription());
            typeList.add(DocType.EINSTANCE.getDescription());
            typeList.add(DocType.LICENSE.getDescription());
        }
        return typeList;
    }

    public List<String> getDocFormats(String category, String type) {

        List<String> bibFormatList = new ArrayList<String>();
        List<String> instanceFormatList = new ArrayList<String>();
        List<String> LicenseFormatList = new ArrayList<String>();

        if (category.equals(DocCategory.WORK.getCode())) {
            if (type.equals(DocType.BIB.getDescription())) {
                bibFormatList.add(DocFormat.MARC.getCode());
                bibFormatList.add(DocFormat.DUBLIN_CORE.getCode());
                bibFormatList.add(DocFormat.DUBLIN_UNQUALIFIED.getCode());
                return bibFormatList;
            } else if (type.equals(DocType.INSTANCE.getDescription())) {
                instanceFormatList.add(DocFormat.OLEML.getCode());
                return instanceFormatList;
            } else if (type.equals(DocType.LICENSE.getDescription())) {
                LicenseFormatList.add(DocFormat.ONIXPL.getCode());
                LicenseFormatList.add(DocFormat.PDF.getCode());
                LicenseFormatList.add(DocFormat.DOC.getCode());
                return LicenseFormatList;
            } else if (type.equals(DocType.EINSTANCE.getDescription())){
                instanceFormatList.add(DocFormat.OLEML.getCode());
                return instanceFormatList;
            }
            else {
                return null;
            }
        }
        return null;
    }
}

package org.kuali.ole.select.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rajeshgp on 29/4/16.
 */
public class OleAgreementAuthUserKeyValues extends KeyValuesBase {


        private boolean blankOption;

        private ParameterValueResolver parameterResolverInstance;


    /**
         * Gets the blankOption attribute.
         *
         * @return Returns the blankOption
         */
        public boolean isBlankOption() {
            return this.blankOption;
        }

        /**
         * Sets the blankOption attribute value.
         *
         * @param blankOption The blankOption to set.
         */
        public void setBlankOption(boolean blankOption) {
            this.blankOption = blankOption;
        }

        /**
         * Gets the keyValues attribute.
         *
         * @return Returns the keyValues
         */

        public ParameterValueResolver getParameterResolverInstance() {
            if (null == parameterResolverInstance) {
                parameterResolverInstance = ParameterValueResolver.getInstance();
            }
            return parameterResolverInstance;
        }



    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        List<String> authValues= Arrays.asList(getParameterResolverInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.SELECT_NMSPC, OLEConstants.ERESOURCE_CMPNT, OLEConstants.AGREEMENT_AUTH_USERS).split(","));

        for(String authValue:authValues) {
                keyValues.add(new ConcreteKeyValue(authValue,authValue));
            }
            return keyValues;
        }

    }

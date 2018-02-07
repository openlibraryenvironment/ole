/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.documentoperation.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;

public class DocumentContentOperationAction extends KewKualiAction {

	private final static String ALGORITHM = "DES/ECB/PKCS5Padding";
	private final static String CHARSET = "UTF-8";
    private static Logger LOG = Logger.getLogger(DocumentContentOperationAction.class);

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward("basic");
	}

	public ActionForward encryptContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(checkPermissions()) {
			DocumentContentOperationForm docContentOperationForm = (DocumentContentOperationForm)form;
			String formDocumentId = docContentOperationForm.getDocumentId();
			String[] documentIds = formDocumentId.split(",");
			String encryptionKey = docContentOperationForm.getKey();
			for(String documentId : documentIds) {
				String docContent = getDocumentContent(documentId);
				String encryptedDocContent = encrypt(encryptionKey, docContent);
				saveDocumentContent(documentId, encryptedDocContent);
			}
		}
		return mapping.findForward("basic");
	}

	public ActionForward decryptContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(checkPermissions()) {
			DocumentContentOperationForm docContentOperationForm = (DocumentContentOperationForm)form;
			String formDocumentId = docContentOperationForm.getDocumentId();
			String[] documentIds = formDocumentId.split(",");
			String encryptionKey = docContentOperationForm.getKey();
			for(String documentId : documentIds) {
				String docContent = getDocumentContent(documentId);
				String decryptedDocContent = decrypt(encryptionKey, docContent);
				saveDocumentContent(documentId, decryptedDocContent);
			}
		}
		return mapping.findForward("basic");
	}

	private boolean checkPermissions() {
		String principalId = GlobalVariables.getUserSession().getPrincipalId();
		Map<String, String> permissionDetails = KRADUtils.getNamespaceAndActionClass(this.getClass());

        boolean canUseScreen = KimApiServiceLocator.getPermissionService().isAuthorizedByTemplate(principalId,
    			KRADConstants.KNS_NAMESPACE, KimConstants.PermissionTemplateNames.USE_SCREEN, permissionDetails,
		    		new HashMap<String, String>());
	    if(canUseScreen && !ConfigContext.getCurrentContextConfig().isProductionEnvironment()) {
		    return true;
	    } else {
		    throw new AuthorizationException(GlobalVariables.getUserSession().getPrincipalName(), "encrypt or decrypt content", this.getClass().getSimpleName());
	    }
	}

	private String getDocumentContent(final String documentId) {
		final DataSource dataSource = KEWServiceLocator.getDataSource();
		JdbcTemplate template = new JdbcTemplate(dataSource);
		String docContent = template.execute(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						String sql = "SELECT doc_cntnt_txt FROM krew_doc_hdr_cntnt_t WHERE doc_hdr_id = ?";
						PreparedStatement statement = connection.prepareStatement(sql);
						return statement;
					}
				},
				new PreparedStatementCallback<String>() {
					public String doInPreparedStatement(PreparedStatement statement) throws SQLException, DataAccessException {
						String docContent = "";
						statement.setString(1, documentId);
						ResultSet rs = statement.executeQuery();
						try {
							while(rs.next()) {
								docContent = rs.getString("doc_cntnt_txt");
							}
						} finally {
							if(rs != null) {
								rs.close();
							}
						}
						return docContent;
					}
				});
		return docContent;
	}

	private void saveDocumentContent(final String documentId, final String docContent) {
		if(StringUtils.isBlank(documentId) || StringUtils.isBlank(docContent)) {
            LOG.info("The document Id or the doc content was blank");
			return;
		}
		final DataSource dataSource = KEWServiceLocator.getDataSource();
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.execute(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						String sql = "UPDATE krew_doc_hdr_cntnt_t SET doc_cntnt_txt = ? WHERE doc_hdr_id = ?";
						PreparedStatement statement = connection.prepareStatement(sql);
						return statement;
					}
				},
				new PreparedStatementCallback<String>() {
					public String doInPreparedStatement(PreparedStatement statement) throws SQLException, DataAccessException {
						statement.setString(1, docContent);
						statement.setString(2, documentId);
						ResultSet rs = statement.executeQuery();
						if(rs != null) {
							rs.close();
						}
						return "";
					}
				});
	}

	private SecretKey getSecretKey(String encryptionKey) throws Exception {
		KeyGenerator keygen = KeyGenerator.getInstance("DES");
		SecretKey desKey = keygen.generateKey();

		// Create the cipher
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init((Cipher.UNWRAP_MODE), desKey);

		byte[] bytes = Base64.decodeBase64(encryptionKey.getBytes());

		SecretKeyFactory desFactory = SecretKeyFactory.getInstance("DES");

		DESKeySpec keyspec = new DESKeySpec(bytes);
		desKey = desFactory.generateSecret(keyspec);
		// Create the cipher
		cipher.init((Cipher.WRAP_MODE), desKey);
		return desKey;
	}

	private String encrypt(String encryptionKey, String value) throws Exception {
		if (StringUtils.isBlank(value)) {
            LOG.info("The value was was blank, returning an empty string");
            return "";
		}

		// Initialize the cipher for encryption
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(encryptionKey));

		try {
			// Our cleartext
			byte[] cleartext = value.toString().getBytes(CHARSET);

			// Encrypt the cleartext
			byte[] ciphertext = cipher.doFinal(cleartext);

			return new String(Base64.encodeBase64(ciphertext), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String decrypt(String encryptionKey, String value) throws Exception {
        if (StringUtils.isBlank(value)) {
            LOG.info("The value was was blank, returning an empty string");
            return "";
        }
		// Initialize the same cipher for decryption
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, getSecretKey(encryptionKey));

		// un-Base64 encode the encrypted data
		byte[] encryptedData = Base64.decodeBase64(value.getBytes(CHARSET));

		// Decrypt the ciphertext
		byte[] cleartext1 = cipher.doFinal(encryptedData);
		return new String(cleartext1, CHARSET);
	}
}

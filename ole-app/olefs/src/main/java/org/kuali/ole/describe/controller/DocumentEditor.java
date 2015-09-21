package org.kuali.ole.describe.controller;

import org.kuali.ole.describe.form.EditorForm;
import org.kuali.ole.docstore.common.document.BibTree;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Defines the operations to be supported by a document editor.
 * Each operation takes an EditorForm as input and returns a different EditorForm as output.
 */
public interface DocumentEditor {

    public EditorForm loadDocument(EditorForm editorForm);

    public EditorForm saveDocument(EditorForm editorForm);

    public EditorForm deleteDocument(EditorForm editorForm);

    public EditorForm createNewRecord(EditorForm editorForm, BibTree bibTree);

    public EditorForm editNewRecord(EditorForm editorForm, BibTree bibTree);

    public String saveDocument(BibTree bibTree, EditorForm form);

    public EditorForm addORDeleteFields(EditorForm editorForm, HttpServletRequest request);

    public EditorForm deleteVerify(EditorForm editorForm) throws Exception;

    public EditorForm delete(EditorForm editorForm) throws Exception;

    public EditorForm addORRemoveExtentOfOwnership(EditorForm editorForm, HttpServletRequest request);

    public EditorForm addORRemoveAccessInformationAndHoldingsNotes(EditorForm editorForm, HttpServletRequest request);

    public EditorForm addORRemoveItemNote(EditorForm editorForm, HttpServletRequest request);

    public EditorForm addORRemoveItemDonor(EditorForm editorForm, HttpServletRequest request);

    public EditorForm showBibs(EditorForm editorForm);

    public EditorForm copy(EditorForm editorForm);

    Boolean isValidUpdate(EditorForm editorForm);

    public EditorForm bulkUpdate(EditorForm editorForm,List<String> ids);

}

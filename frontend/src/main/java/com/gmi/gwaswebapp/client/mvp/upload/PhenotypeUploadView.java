package com.gmi.gwaswebapp.client.mvp.upload;

import com.gmi.gwaswebapp.client.mvp.upload.PhenotypeUploadPresenter.MyView;
import com.gmi.gwaswebapp.client.resources.MyResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class PhenotypeUploadView extends ViewWithUiHandlers<PhenotypeUploadUiHandlers> implements MyView {

	private static PhenotypeUploadViewUiBinder uiBinder = GWT
			.create(PhenotypeUploadViewUiBinder.class);

	interface PhenotypeUploadViewUiBinder extends
			UiBinder<Widget, PhenotypeUploadView> {
	}
	
	public final Widget widget;
	
	@UiField FormPanel phenotype_upload_form;
	@UiField FileUpload phenotype_file;
	@UiField TextArea phenotype_content;
	@UiField Button form_submit_btn;
	@UiField(provided=true) MyResources mainRes;

	@Inject
	public PhenotypeUploadView(MyResources resources) {
		this.mainRes = resources;
		widget = uiBinder.createAndBindUi(this);
		phenotype_upload_form.setEncoding(FormPanel.ENCODING_MULTIPART);
		phenotype_upload_form.setMethod(FormPanel.METHOD_POST);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasClickHandlers getSubmit() {
		return form_submit_btn;
	}

	@Override
	public void submitForm() {
		phenotype_upload_form.submit();
	}

	@Override
	public HandlerRegistration addFormSubmitHandler(SubmitHandler submitHandler) {
		return phenotype_upload_form.addSubmitHandler(submitHandler);
	}

	@Override
	public HasText getPhenotypeContent() {
		return phenotype_content;
	}

	@Override
	public FileUpload getPhenotypeFile() {
		return phenotype_file;
	}

	@Override
	public void setSubmitEnabled(boolean enabled) {
		form_submit_btn.setEnabled(enabled);
		form_submit_btn.setText(enabled ? "Upload" : "Uploading");
	}

	@Override
	public HandlerRegistration addFormSubmitHandler(SubmitCompleteHandler handler) {
		return phenotype_upload_form.addSubmitCompleteHandler(handler);
	}

	@Override
	public void setFormAction(String url) {
		phenotype_upload_form.setAction(url);
		
	}

	@Override
	public void resetForm() {
		phenotype_upload_form.reset();
	}
	
	

}

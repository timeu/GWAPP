package com.gmi.gwaswebapp.client.mvp.upload;

import com.gmi.gwaswebapp.client.CurrentUser;
import com.gmi.gwaswebapp.client.NameTokens;
import com.gmi.gwaswebapp.client.dto.Readers.ResultReader;
import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.events.DisplayNotificationEvent;
import com.gmi.gwaswebapp.client.mvp.main.MainPagePresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class PhenotypeUploadPresenter extends Presenter<PhenotypeUploadPresenter.MyView,PhenotypeUploadPresenter.MyProxy> implements PhenotypeUploadUiHandlers{

	public interface MyView extends View,HasUiHandlers<PhenotypeUploadUiHandlers> {
		HasClickHandlers getSubmit();
		void submitForm();
		HandlerRegistration addFormSubmitHandler(SubmitHandler submitHandler);
		HasText getPhenotypeContent();
		FileUpload getPhenotypeFile();
		void setSubmitEnabled(boolean enabled);
		HandlerRegistration addFormSubmitHandler(SubmitCompleteHandler submitCompleteHandler);
		void setFormAction(String url);
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.phenotypeUploadPage)
	public interface MyProxy extends ProxyPlace<PhenotypeUploadPresenter> {
	}
	
	protected final PlaceManager placeManager;
	protected final ResultReader resultReader;
	protected final CurrentUser currentUser;
	
	@Inject
	public PhenotypeUploadPresenter(EventBus eventBus, MyView view,
			MyProxy proxy,final PlaceManager placeManager, final ResultReader resultReader, final CurrentUser currentUser) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		this.currentUser = currentUser;
		this.resultReader = resultReader;
		getView().setUiHandlers(this);
		getView().setFormAction("/gwas/uploadPhenotype");
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this,MainPagePresenter.TYPE_SetMainContent,this);
	}

	@Override
	protected void onBind()
	{
		super.onBind();
		registerHandler(getView().getSubmit().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getView().submitForm();
			}
		}));
		
		registerHandler(getView().addFormSubmitHandler(new FormPanel.SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				if (getView().getPhenotypeContent().getText().length() == 0 && getView().getPhenotypeFile().getFilename().length() == 0)
				{
					Window.alert("The Phenotype File or Content must be specified");
					event.cancel();
				}
				else
				{
					//LoadingIndicatorEvent.fire(PhenotypeUploadPresenter.this, true);
					getView().setSubmitEnabled(false);
				}
			}
		}));
		
		registerHandler(getView().addFormSubmitHandler(new FormPanel.SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				//LoadingIndicatorEvent.fire(PhenotypeUploadPresenter.this, false);
				BackendResult result = null;
				String jsonString = null;
				//result = null;
				try
				{
					jsonString = event.getResults();
					result = resultReader.read(jsonString);
				}
				catch (Exception ex) {}
				
				if (result != null && result.getStatus() == BackendResult.STATUS.OK)
				{
					//cache.clear();
					currentUser.refresh(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							PlaceRequest request = new PlaceRequest(NameTokens.analysisPage);
							placeManager.revealPlace(request);
						}
					});
				}
				else if (result != null)
				{
					//cache.clear();
					DisplayNotificationEvent.fireError(PhenotypeUploadPresenter.this,"Upload failed",result.getStatustext());
					//DisplayShortMessageEvent.fireError(PhenotypeUploadPresenter.this,"Error during upload:" + result.getStatusText());
				}
				else {
					DisplayNotificationEvent.fireError(PhenotypeUploadPresenter.this,"Upload failed","Unknown Errro");
				}
					//DisplayShortMessageEvent.fireError(PhenotypeUploadPresenter.this,"General Error");
				getView().setSubmitEnabled(true);
			}
		}));
	}
	

}

package com.gmi.gwaswebapp.client.mvp.accession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gmi.gwaswebapp.client.CurrentUser;
import com.gmi.gwaswebapp.client.NameTokens;
import com.gmi.gwaswebapp.client.command.GetAccessionsAction;
import com.gmi.gwaswebapp.client.command.GetAccessionsActionResult;
import com.gmi.gwaswebapp.client.command.GetLocationDistributionAction;
import com.gmi.gwaswebapp.client.command.GetLocationDistributionActionResult;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.dto.Accession;
import com.gmi.gwaswebapp.client.dto.Dataset;
import com.gmi.gwaswebapp.client.dto.Readers.AccessionsReader;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionCellTableColumns.SearchTerm;
import com.gmi.gwaswebapp.client.mvp.main.MainPagePresenter;
import com.gmi.gwaswebapp.client.ui.HasSearchHandlers;
import com.gmi.gwaswebapp.client.util.AbstractDtoPredicate;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;

public class AccessionPresenter extends
		Presenter<AccessionPresenter.MyView, AccessionPresenter.MyProxy> implements AccessionUiHandlers{

	public interface MyView extends View,HasUiHandlers<AccessionUiHandlers> {
		HasData<Accession> getDisplay();
		void clearMap();
		void addMarkers(List<Accession> accessions);
		SelectionModel<Accession> getSelectionModel();
		HashMap<SearchTerm.CRITERIA,SearchTerm> getSearchTerms();
		void updateFocus();
		void drawLocationDistribution(AbstractDataTable data);
		
		HasSearchHandlers getSearchNameHandlers();;
		HasSearchHandlers getSearchCountryHandlers();
		HasSearchHandlers getSearchCollectorHandlers();
		HashMap<SearchTerm.CRITERIA,SearchTerm> getSearchCriterias();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.accessionPage)
	public interface MyProxy extends ProxyPlace<AccessionPresenter> {
	}
	
	private DataTable distributionDatatable = null;
	private ListDataProvider<Accession> accessionDataProvider = new ListDataProvider<Accession>();
	private final DispatchAsync dispatch;
	private final CurrentUser currentUser; 
	private List<AbstractDtoPredicate<Accession, ?>> accessionPredicates = new ArrayList<AbstractDtoPredicate<Accession, ?>>();
	private Accession.AccessionIdPredicate accessionIdPredicate = new Accession.AccessionIdPredicate(null);
	private Accession.AccessionNamePredicate accessionNamePredicate = new Accession.AccessionNamePredicate(""); 
	private Accession.AccessionCountryPredicate accessionCountryPredicate = new Accession.AccessionCountryPredicate("");
	private Accession.AccessionCollectorPredicate accessionCollectorPredicate = new Accession.AccessionCollectorPredicate("");

		/*@Override
		protected void onRangeChanged(HasData<Accession> display) {
			final Range range = display.getVisibleRange();
			dispatch.execute(new GetAccessionsAction(range.getStart(),range.getLength(),getView().getSearchTerms().values(),accessionsReader), new GWASCallback<GetAccessionsActionResult>(getEventBus()) {
				@Override
				public void onSuccess(GetAccessionsActionResult result) {
					updateRowData(range.getStart(), result.getAccessions().getAccessions());
					updateRowCount(result.getAccessions().getCount(), true);
					getView().updateFocus();
					getView().addMarkers(result.getAccessions().getAccessions());
				}
			});
		}
	};*/
	
	
	
	
	

	@Inject
	public AccessionPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final DispatchAsync dispatch,final CurrentUser currentUser) {
		super(eventBus, view, proxy);
		this.dispatch = dispatch;
		this.currentUser = currentUser;
		getView().setUiHandlers(this);
		getView().addMarkers(CurrentUser.accessions);
		accessionDataProvider.addDataDisplay(getView().getDisplay());
		accessionDataProvider.setList(CurrentUser.accessions);
		accessionPredicates.add(accessionNamePredicate);
		accessionPredicates.add(accessionCountryPredicate);
		accessionPredicates.add(accessionCollectorPredicate);
		accessionPredicates.add(accessionIdPredicate);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent,this);
	}
	
	
	@Override
	public void onReset() {
		super.onReset();
		drawLocationDistribution();
	}
	
	@Override
	public void onBind() {
		super.onBind();
		registerHandler(getView().getSearchNameHandlers().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String value = getView().getSearchNameHandlers().getText();
				try {
					accessionIdPredicate.setValue(Integer.parseInt(value));
				}
				catch (Exception ex) {
					accessionIdPredicate.setValue(null);
				}
				accessionNamePredicate.setValue(value);
				getView().getSearchCriterias().get(SearchTerm.CRITERIA.Name).setValue(value);
				getView().getSearchCriterias().get(SearchTerm.CRITERIA.AccessionID).setValue((accessionIdPredicate.getValue() != null ? accessionIdPredicate.getValue().toString():""));
				accessionDataProvider.setList(Accession.filter(CurrentUser.accessions,accessionPredicates));
				
			}
		}));
		
		registerHandler(getView().getSearchCountryHandlers().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String value = getView().getSearchCountryHandlers().getText();
				accessionCountryPredicate.setValue(value);
				getView().getSearchCriterias().get(SearchTerm.CRITERIA.Country).setValue(value);
				accessionDataProvider.setList(Accession.filter(CurrentUser.accessions,accessionPredicates));
			}
		}));
		
		registerHandler(getView().getSearchCollectorHandlers().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String value = getView().getSearchCollectorHandlers().getText();
				accessionCollectorPredicate.setValue(value);
				getView().getSearchCriterias().get(SearchTerm.CRITERIA.Collector).setValue(value);
				accessionDataProvider.setList(Accession.filter(CurrentUser.accessions,accessionPredicates));
			}
		}));
	}
	
	private void drawLocationDistribution() {
		if (distributionDatatable == null)	{
			dispatch.execute(new GetLocationDistributionAction(), new GWASCallback<GetLocationDistributionActionResult>(getEventBus()) {

				@Override
				public void onSuccess(GetLocationDistributionActionResult result) {
					distributionDatatable = result.getDataTables().get("All");
					drawLocationDistribution(distributionDatatable);
				}
			});
		}
		else {
			drawLocationDistribution(distributionDatatable);
		}
	}
	
	private void drawLocationDistribution(AbstractDataTable data) {
		getView().drawLocationDistribution(data);
	}

	@Override
	public void selectAccessionInTable(Accession accession) {
		getView().getSelectionModel().setSelected(accession, true);
	}
}

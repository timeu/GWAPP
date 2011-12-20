package com.gmi.gwaswebapp.client.mvp.dataset.detail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gmi.gwaswebapp.client.CurrentUser;
import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.GetAccessionsAction;
import com.gmi.gwaswebapp.client.command.GetAccessionsActionResult;
import com.gmi.gwaswebapp.client.command.GetLocationDistributionAction;
import com.gmi.gwaswebapp.client.command.GetLocationDistributionActionResult;
import com.gmi.gwaswebapp.client.command.SaveDatasetAction;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.dto.Accession;
import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.Dataset;
import com.gmi.gwaswebapp.client.dto.Readers.AccessionsReader;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.gmi.gwaswebapp.client.dto.Readers.DatasetWriter;
import com.gmi.gwaswebapp.client.events.DisplayNotificationEvent;
import com.gmi.gwaswebapp.client.events.SaveDatasetEvent;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionCellTableColumns.SearchTerm;
import com.gmi.gwaswebapp.client.ui.HasSearchHandlers;
import com.gmi.gwaswebapp.client.util.AbstractDtoPredicate;
import com.google.inject.Inject;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;

public class DatasetDetailPresenter extends
		PresenterWidget<DatasetDetailPresenter.MyView> implements DatasetDetailUiHandlers{

	

	public interface MyView extends View,HasUiHandlers<DatasetDetailUiHandlers> {
		HasData<Accession> getDisplay();
		HasText getDatasetLabel();
		HasText getDatasetDescription();
		HasText getDatasetNameEditText();
		HasText getDatasetDescriptionEditText();
		HasClickHandlers getActionButton();
		HasClickHandlers getCancelButton();
		HasClickHandlers getDisplayAllButton();
		HasClickHandlers getDisplayInSetButton();
		HasSearchHandlers getSearchNameHandlers();
		HasSearchHandlers getSearchCountryHandlers();
		HasSearchHandlers getSearchCollectorHandlers();

		
		HashMap<SearchTerm.CRITERIA,SearchTerm> getSearchCriterias();
		
		int getAvailableVerticalSpace();
		void drawMapMarkers(Collection<Accession> accessions);
		void clearMap();
		void drawLocationDistribution(AbstractDataTable data);
		void initPageSize();
		void setMode(MODE mode);
		void disableControlsForFullset(boolean isFullset);
		void initSizes();
		void insertCheckBoxColumn(Column<Accession,Boolean> column);
		void removeCheckBoxColumn(Column<Accession, Boolean> inDatasetColumn);
		void setSelectionModel(MultiSelectionModel<Accession> selectionModel);
		void setControlsForSubset(boolean isShowDisplayAll);
		void setCancelButtonVisible(boolean visible);
		void setDisplayButtonActive(boolean isInSet);
	}
	public static enum MODE {READ,EDIT,ADD};
	private final PlaceManager placeManager;
	private Dataset dataset = null;
	private ListDataProvider<Accession> accessionListProvider =new ListDataProvider<Accession>();
	
	private final CurrentUser currentUser;
	private final DispatchAsync dispatch;
	private final AccessionsReader accessionsReader;
	private final BackendResultReader backendResultReader;
	private final DatasetWriter datasetWriter;
	private DataTable distributionDataTable = null;
	private MODE activeMode = MODE.READ;
	private HandlerRegistration selectionChangeHandlerManager = null;
	private SelectionChangeEvent.Handler selectionChangeHandler = null;
	private boolean isDisplayAll = false;
	
	private Accession.AccessionIdPredicate accessionIdPredicate = new Accession.AccessionIdPredicate(null);
	private Accession.AccessionNamePredicate accessionNamePredicate = new Accession.AccessionNamePredicate(""); 
	private Accession.AccessionCountryPredicate accessionCountryPredicate = new Accession.AccessionCountryPredicate("");
	private Accession.AccessionCollectorPredicate accessionCollectorPredicate = new Accession.AccessionCollectorPredicate("");
	private List<AbstractDtoPredicate<Accession, ?>> accessionPredicates = new ArrayList<AbstractDtoPredicate<Accession, ?>>();
	private Column<Accession,Boolean> inDatasetColumn;
	private MultiSelectionModel<Accession> selectionModel = new MultiSelectionModel<Accession>(Accession.KEY_PROVIDER);
	
	@Inject
	public DatasetDetailPresenter(final EventBus eventBus, final MyView view, final DispatchAsync dispatch,
								final AccessionsReader accessionsReader, final PlaceManager placeManager,
								final BackendResultReader backendResultReader,final DatasetWriter datasetWriter,
								final CurrentUser currentUser) {
		super(eventBus, view);
		this.dispatch = dispatch;
		this.currentUser = currentUser;
		this.placeManager = placeManager;
		this.accessionsReader = accessionsReader;
		this.backendResultReader = backendResultReader;
		this.datasetWriter = datasetWriter;
		getView().setUiHandlers(this);
		accessionPredicates.add(accessionIdPredicate);
		accessionPredicates.add(accessionNamePredicate);
		accessionPredicates.add(accessionCountryPredicate);
		accessionPredicates.add(accessionCollectorPredicate);
		accessionListProvider.addDataDisplay(getView().getDisplay());
		inDatasetColumn = new Column<Accession, Boolean>(new CheckboxCell(true, false)) {

			@Override
			public Boolean getValue(Accession object) {
				return selectionModel.isSelected(object);
			}
		};
		
		selectionChangeHandler = new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				updateCharts(selectionModel.getSelectedSet());
			}
		};
	}
	
	@Override 
	protected void onBind() {
		super.onBind();
		registerHandler(getView().getActionButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (activeMode == MODE.READ)
					setMode(MODE.EDIT);
				else {
					saveChanges();
				}
			}
		}));
		
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
				List<Accession> accessions = getAccessionsToDisplay();
				accessionListProvider.setList(Accession.filter(accessions,accessionPredicates));
							
			}
		}));
		
		registerHandler(getView().getSearchCountryHandlers().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String value = getView().getSearchCountryHandlers().getText();
				accessionCountryPredicate.setValue(value);
				getView().getSearchCriterias().get(SearchTerm.CRITERIA.Country).setValue(value);
				List<Accession> accessions = getAccessionsToDisplay();
				accessionListProvider.setList(Accession.filter(accessions,accessionPredicates));
			}
		}));
		
		registerHandler(getView().getSearchCollectorHandlers().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String value = getView().getSearchCollectorHandlers().getText();
				accessionCollectorPredicate.setValue(value);
				getView().getSearchCriterias().get(SearchTerm.CRITERIA.Collector).setValue(value);
				List<Accession> accessions = getAccessionsToDisplay();
				accessionListProvider.setList(Accession.filter(accessions,accessionPredicates));
			}
		}));
		
		registerHandler(getView().getCancelButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				accessionListProvider.setList(dataset.filterAccessions(accessionPredicates));
				setMode(MODE.READ);
			}
		}));
		
		registerHandler(getView().getDisplayAllButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				isDisplayAll = true;
				List<Accession> accessions = getAccessionsToDisplay();
				accessionListProvider.setList(Accession.filter(accessions,accessionPredicates));
				getView().setDisplayButtonActive(false);
			}
		}));
		
		registerHandler(getView().getDisplayInSetButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				accessionListProvider.setList(dataset.filterAccessions(accessionPredicates));
				isDisplayAll = false;
				getView().setDisplayButtonActive(true);
			}
		}));
	}

	@Override
	protected void onReset() {
		super.onReset();
	}
	
	public void setData(Dataset dataset) {
		setData(dataset, MODE.READ);
	}

	public void setData(final Dataset dataset,MODE mode) {
		if (this.dataset != dataset) {
			getView().getDatasetLabel().setText(dataset.getName() + " (" + dataset.getAccessionCount().toString()+")");
			getView().getDatasetDescription().setText(dataset.getDescription());
			accessionListProvider.setList(new ArrayList<Accession>());
			distributionDataTable = null;
		}
		this.dataset = dataset;
		if (accessionListProvider.getList().size() == 0) {
			accessionListProvider.setList(dataset.getAccessions());
				//refreshData();
		}
		if (distributionDataTable == null) {
			dispatch.execute(new GetLocationDistributionAction(dataset.getPhenotype()), new GWASCallback<GetLocationDistributionActionResult>(getEventBus()) {

				@Override
				public void onSuccess(GetLocationDistributionActionResult result) {
					distributionDataTable = result.getDataTables().get(dataset.getName());
					if (distributionDataTable == null)
						distributionDataTable = dataset.calculateLocDistribution();
					getView().drawLocationDistribution(distributionDataTable);
				}
			});
		}
		else
			getView().drawLocationDistribution(distributionDataTable);
		setMode(mode);
		getView().disableControlsForFullset(dataset.getName().equals("Fullset") ? true : false);
	}
	
	private void refreshData() {
		//getView().initSizes();
		getView().initPageSize();
		accessionListProvider.setList(dataset.getAccessions());
		getView().drawMapMarkers(dataset.getAccessions());
	}

	@Override
	public void selectAccessionInTable(Accession accession) {
		// TODO Auto-generated method stub
		
	}
	
	private void updateCharts(Collection<Accession> accessions) {
		getView().drawMapMarkers(accessions);
		distributionDataTable = Dataset.calculateLocDistribution(accessions);
		getView().drawLocationDistribution(distributionDataTable);
	}
	
	public void setMode(MODE mode) {
		activeMode = mode;
		getView().setMode(activeMode);
		if (mode == MODE.READ) {
			getView().setCancelButtonVisible(false);
			getView().removeCheckBoxColumn(inDatasetColumn);
			if (selectionChangeHandlerManager != null) {
				selectionChangeHandlerManager.removeHandler();
				selectionChangeHandlerManager = null;
			}
			getView().getDisplay().setSelectionModel(null);
			placeManager.setOnLeaveConfirmation(null);
			if (!dataset.getName().equals("Fullset")) {
				getView().setControlsForSubset(false);
			}
		}
		else {
			getView().getDatasetNameEditText().setText(dataset.getName());
			getView().getDatasetDescriptionEditText().setText(dataset.getDescription());
			getView().setCancelButtonVisible(true);
			setSelectedAccessions(dataset.getAccessions());
			getView().setSelectionModel(selectionModel);
			selectionModel.getSelectedSet();
			getView().insertCheckBoxColumn(inDatasetColumn);
			placeManager.setOnLeaveConfirmation("You are currently editing the dataset. If you leave you will lose all changes. Do you really want to leave?");
			selectionChangeHandlerManager = selectionModel.addSelectionChangeHandler(selectionChangeHandler);
			getView().setControlsForSubset(true);
		}
	}
	
	private void saveChanges() {
		if (!getView().getDatasetNameEditText().getText().equals(""))
		{
			dataset.updateAccessions(selectionModel.getSelectedSet());
			dataset.setName(getView().getDatasetNameEditText().getText());
			dataset.setDescription(getView().getDatasetDescriptionEditText().getText());
			dispatch.execute(new SaveDatasetAction(dataset, backendResultReader, datasetWriter), new GWASCallback<BaseStatusResult>(getEventBus()) {

				@Override
				public void onSuccess(BaseStatusResult result) {
					if (result.result.getStatus() == BackendResult.STATUS.OK) {
						getView().getDatasetLabel().setText(dataset.getName() + " (" + dataset.getAccessionCount().toString()+")");
						getView().getDatasetDescription().setText(dataset.getDescription());
						if (dataset.getInternId() == null) 
							dataset.setInternId(result.result.getStatustext());
						setMode(MODE.READ);
						accessionListProvider.setList(dataset.getAccessions());
						SaveDatasetEvent.fire(getEventBus(), dataset);
						
					}
					else
					{
						DisplayNotificationEvent.fireError(getEventBus(), "Saving Dataset failed",result.result.getStatustext() );
					}
				}
				
			});
		}
		else
		{
			// TODO display popup
		}
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		refreshData();
	}
	
	private void setSelectedAccessions(List<Accession> accessions) {
		for (Accession accession: accessions ) {
			selectionModel.setSelected(accession, true);
		}
	}
	
	private List<Accession> getAccessionsFromFullSet() {
		return currentUser.getUserData().getPhenotypeFromName(dataset.getPhenotype()).getDatasetFromId("Fullset").getAccessions();
	}
	
	private List<Accession> getAccessionsToDisplay() {
		List<Accession> accessions = dataset.getAccessions();
		if (isDisplayAll)
			accessions = getAccessionsFromFullSet();
		return accessions;
	}
	
}

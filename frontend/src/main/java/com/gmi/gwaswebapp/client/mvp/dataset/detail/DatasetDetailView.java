package com.gmi.gwaswebapp.client.mvp.dataset.detail;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gmi.gwaswebapp.client.dto.Accession;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionCellTableColumns;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionCellTableColumns.SearchTerm;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionCellTableColumns.SearchTerm.CRITERIA;
import com.gmi.gwaswebapp.client.mvp.dataset.detail.DatasetDetailPresenter.MODE;
import com.gmi.gwaswebapp.client.resources.CellTableResources;
import com.gmi.gwaswebapp.client.resources.MyResources;
import com.gmi.gwaswebapp.client.ui.HasSearchHandlers;
import com.gmi.gwaswebapp.client.ui.SearchTextBox;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.GeoMap;
import com.google.inject.Inject;

public class DatasetDetailView extends ViewWithUiHandlers<DatasetDetailUiHandlers> implements
		DatasetDetailPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DatasetDetailView> {
	}
	
	@UiField(provided=true) CellTable<Accession> accession_list;
	@UiField InlineLabel dataset_name;
	@UiField InlineLabel dataset_description;
	@UiField DivElement container;
	@UiField(provided=true) SimplePager pager;
	@UiField HTMLPanel mapContainer;
	@UiField GeoMap geomap;
	@UiField HTMLPanel phenotype_container;
	@UiField TextBox dataset_name_edit;
	@UiField TextBox dataset_description_edit;
	@UiField Button action_button;
	@UiField Button cancel_button;
	@UiField Button display_inset;
	@UiField Button display_all;
	@UiField SearchTextBox Search_Name;
	@UiField SearchTextBox Search_Country;
	@UiField SearchTextBox Search_Collector;
	@UiField HTMLPanel edit_container;
	

	
	private MapWidget map;
	private final HashMap<Integer, Marker> accessionId2Marker = new HashMap<Integer, Marker>();
	private final HashMap<SearchTerm.CRITERIA,SearchTerm> searchTerms = new HashMap<SearchTerm.CRITERIA,SearchTerm>();
	private DatasetDetailPresenter.MODE mode = DatasetDetailPresenter.MODE.READ;
	private final MyResources resources;
	private final CellTableResources cellTableResources;

	@Inject
	public DatasetDetailView(final Binder binder,final MyResources resources, final CellTableResources cellTableResources) {
		this.cellTableResources = cellTableResources;
		accession_list = new CellTable<Accession>(25,cellTableResources,Accession.KEY_PROVIDER);
		accession_list.setWidth("100%",true);
		this.resources = resources;
		initCellTable();
		widget = binder.createAndBindUi(this);
		Search_Collector.getElement().setAttribute("placeHolder", "Collector");
		Search_Country.getElement().setAttribute("placeHolder", "Country");
		Search_Name.getElement().setAttribute("placeHolder", "Name or Id");
	}

	private void initCellTable() {
		searchTerms.clear();
		searchTerms.put(SearchTerm.CRITERIA.Name, new SearchTerm(SearchTerm.CRITERIA.Name));
		searchTerms.put(SearchTerm.CRITERIA.Country, new SearchTerm(SearchTerm.CRITERIA.Country));
		searchTerms.put(SearchTerm.CRITERIA.Collector, new SearchTerm(SearchTerm.CRITERIA.Collector));
		searchTerms.put(SearchTerm.CRITERIA.AccessionID, new SearchTerm(SearchTerm.CRITERIA.AccessionID));
		SearchTerm searchTerm;
		searchTerm = searchTerms.get(SearchTerm.CRITERIA.AccessionID);
		accession_list.addColumn(new AccessionCellTableColumns.AccessionIdColumn(searchTerm),"ID");
		searchTerm = searchTerms.get(SearchTerm.CRITERIA.Name);
		accession_list.addColumn(new AccessionCellTableColumns.NameColumn(searchTerm),"Name");
		//accession_list.addColumn(new AccessionCellTableColumns.LongitudeLatitudeColumn(),"Lon/Lat");
		searchTerm = searchTerms.get(SearchTerm.CRITERIA.Country);
		accession_list.addColumn(new AccessionCellTableColumns.CountryColumn(searchTerm),"Country");
		accession_list.addColumn(new AccessionCellTableColumns.CollectionDateColumn(),"Date");
		searchTerm = searchTerms.get(SearchTerm.CRITERIA.Collector);
		accession_list.addColumn(new AccessionCellTableColumns.CollectorColumn(searchTerm),"Collector");
		accession_list.setColumnWidth(accession_list.getColumn(0),50, Unit.PX);
		accession_list.setColumnWidth(accession_list.getColumn(1),33, Unit.PCT);
		accession_list.setColumnWidth(accession_list.getColumn(2),100, Unit.PX);
		accession_list.setColumnWidth(accession_list.getColumn(3),33, Unit.PCT);
		accession_list.setColumnWidth(accession_list.getColumn(4),33, Unit.PCT);
		//accession_list.setColumnWidth(accession_list.getColumn(0),10, Unit.PCT);
		pager = new SimplePager();
		pager.setDisplay(accession_list);
		//SingleSelectionModel<Accession> selectionModel = new SingleSelectionModel<Accession>(Accession.KEY_PROVIDER);
		//MultiSelectionModel<Accession> selectionModel = new MultiSelectionModel<Accession>(Accession.KEY_PROVIDER);
		/*selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				String test = "test";
				
			}
		});
		accession_list.setSelectionModel(selectionModel);*/
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasData<Accession> getDisplay() {
		return accession_list;
	}
	
	@Override
	public HasText getDatasetLabel() {
		return dataset_name;
	}

	@Override
	public HasText getDatasetDescription() {
		return dataset_description;
	}
	
	private int getPageSizeFromResolution() {
		int top_pos =  accession_list.getAbsoluteTop();
		top_pos = 270;
		int rowHeight=12;
		int pageSize = 30;
		int headerHeight = 155;
		headerHeight=23;
		int pagerHeight = 30;
		int topBarHeight = 92;
		int screen_width = Window.getClientWidth();
		if (screen_width <= 1210) {
			rowHeight = 53;
			top_pos = 165;
		}
		else if (screen_width <=1810)
			rowHeight = 40;
		else
			rowHeight = 25;
		
		// getAbsoluteTop does not work because of slidepanel
		
		//pageSize = Math.round((Window.getClientHeight()-headerHeight - pagerHeight - topBarHeight) / rowHeight);
		pageSize = Math.round((Window.getClientHeight()- top_pos-pager.getOffsetHeight() - accession_list.getHeaderHeight())  / rowHeight);
		return pageSize;
	}

	@Override
	public int getAvailableVerticalSpace() {
		int top_pos =  mapContainer.getAbsoluteTop();
		top_pos = 179;
		int availableSpace = Window.getClientHeight() - top_pos;
		return availableSpace;
	}
	
	public int getAvailableHorizontalSpace() {
		int availableSpace = Window.getClientWidth() - phenotype_container.getAbsoluteLeft();
		return availableSpace;
	}

	
	private void drawMap(Collection<Accession> accessions) {
		assert map != null;
		int size = getAvailableVerticalSpace();
  	    map.setSize("100%", Math.round(size/2)+"px");
		clearMap();
		accessionId2Marker.clear();
		for (final Accession accession:accessions) {
			MarkerOptions options = MarkerOptions.newInstance();
			final Icon icon = Icon.getDefaultIcon();
			icon.setShadowSize(Size.newInstance(0, 0));
			options.setIcon(icon);
			options.setClickable(true);
			final String markerLabel =  accession.getName()+" (" + accession.getAccessionId()+")";
			options.setTitle(markerLabel);
			final Marker marker = new Marker(LatLng.newInstance(accession.getLatitude(), accession.getLongitude()),options);
			accessionId2Marker.put(accession.getAccessionId(),marker);
			marker.addMarkerClickHandler(new MarkerClickHandler() {
				
				@Override
				public void onClick(MarkerClickEvent event) {
					InfoWindow info = map.getInfoWindow();
					info.open(marker, new InfoWindowContent(markerLabel));
					getUiHandlers().selectAccessionInTable(accession);
				}
			});
			
			map.addOverlay(marker);
		}
	}
	
	
	@Override
	public void drawMapMarkers(final Collection<Accession> accessions) {
		
		if (map == null) 
		{
			Maps.loadMapsApi("ABQIAAAAHExtiY5_qKaTj9xWKMjl4xTtEpWa7kqih2tSZ3rpmArayZxetBS7MKdCA3ZN4ypVkrPZAbUr8J2cZw", "2", false, new Runnable() {
			      public void run() {
			    	  if (map == null) {
				    	  map = new MapWidget();
				    	  map.setContinuousZoom(true);
				    	  map.addControl(new LargeMapControl());
				  		  map.addControl(new MapTypeControl());
				  		  map.addMapType(MapType.getPhysicalMap());	//2009-4-9 add the terrain map
				  		  map.setCurrentMapType(MapType.getPhysicalMap());	//2009-4-9 set the terrain map as default
				  		  map.setScrollWheelZoomEnabled(true);
				  		  mapContainer.add(map);
				  		  map.setCenter(LatLng.newInstance(52,21));
				  		  drawMap(accessions);
				  		  
			    	  }
			      } 
			});
		}
		else
		{
			drawMap(accessions);
		}
	    
	}

	@Override
	public void clearMap() {
		if (map != null)
			map.clearOverlays();
	}
	
	private GeoMap.Options createGeoMapOptions() {
		GeoMap.Options options = GeoMap.Options.create();
		options.setHeight(Math.round((getAvailableVerticalSpace()-35)/2));
		options.setWidth("100%");
		//)
		//Math.round(getAvailableHorizontalSpace()/2)
		return options;
	}

	@Override
	public void drawLocationDistribution(AbstractDataTable data) {
		geomap.draw(data,createGeoMapOptions());
		
	}

	@Override
	public void initPageSize() {
		int pageSize = getPageSizeFromResolution();
		accession_list.setPageSize(pageSize);
	}

	@Override
	public void setMode(MODE mode) {
		this.mode = mode;
		changeMode();
	}
	
	private void changeMode() {
		edit_container.setVisible(mode == MODE.READ ? false : true);
		dataset_name.setVisible(mode == MODE.READ ? true : false);
		dataset_description.setVisible(mode == MODE.READ ? true : false);
		dataset_name_edit.setVisible(!dataset_name.isVisible());
		dataset_description_edit.setVisible(!dataset_name.isVisible());
		if (mode == MODE.READ) 
			action_button.setText("Edit");
		else
			action_button.setText("Save");
	}

	@Override
	public void disableControlsForFullset(boolean isFullset) {
		if (isFullset) {
			display_all.setStylePrimaryName(resources.style().round_button_selected());
			display_all.getElement().removeAttribute("disabled");
			action_button.getElement().setAttribute("disabled","disabled");
			display_inset.getElement().setAttribute("disabled","disabled");
			
		}
		else {
			action_button.getElement().removeAttribute("disabled");
			display_inset.getElement().removeAttribute("disabled");
		}
	}

	@Override
	public HasClickHandlers getActionButton() {
		return action_button;
	}

	@Override
	public HasSearchHandlers getSearchNameHandlers() {
		return Search_Name;
	}

	@Override
	public HasSearchHandlers getSearchCountryHandlers() {
		return Search_Country;
	}

	@Override
	public HasSearchHandlers getSearchCollectorHandlers() {
		return Search_Collector;
	}

	

	@Override
	public HashMap<CRITERIA, SearchTerm> getSearchCriterias() {
		return searchTerms;
	}

	@Override
	public void initSizes() {
		int pageSize = getPageSizeFromResolution();
		accession_list.setPageSize(pageSize);
	}

	
	@Override
	public void insertCheckBoxColumn(Column<Accession,Boolean> column) {
		if (accession_list.getColumnIndex(column) == -1) {
			accession_list.addColumn(column,"");
			accession_list.setColumnWidth(column,50, Unit.PX);
		}
	}

	@Override
	public void removeCheckBoxColumn(Column<Accession, Boolean> column) {
		int index = accession_list.getColumnIndex(column);
		if (index != -1)
			accession_list.removeColumn(index);
	}

	@Override
	public void setSelectionModel(MultiSelectionModel<Accession> selectionModel) {
		accession_list.setSelectionModel(selectionModel, DefaultSelectionEventManager.<Accession> createCheckboxManager());
	}

	@Override
	public HasText getDatasetNameEditText() {
		return dataset_name_edit;
	}

	@Override
	public HasText getDatasetDescriptionEditText() {
		return dataset_description_edit;
	}

	@Override
	public void setControlsForSubset(boolean isShowDisplayAll) {
		if (isShowDisplayAll)
			display_all.getElement().removeAttribute("disabled");
		else
			display_all.getElement().setAttribute("disabled","disabled");
		display_all.setStylePrimaryName(resources.style().round_button());
		display_inset.setStylePrimaryName(resources.style().round_button_selected());
	}
	
	@Override
	public void setDisplayButtonActive(boolean isInSet) {
		display_all.setStylePrimaryName(isInSet ? resources.style().round_button() : resources.style().round_button_selected());
		display_inset.setStylePrimaryName(!isInSet ? resources.style().round_button() : resources.style().round_button_selected());
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return cancel_button;
	}

	@Override
	public void setCancelButtonVisible(boolean visible) {
		cancel_button.setVisible(visible);
		
	}

	@Override
	public HasClickHandlers getDisplayAllButton() {
		return display_all;
	}

	@Override
	public HasClickHandlers getDisplayInSetButton() {
		return display_inset;
	}
	
}

package com.gmi.gwaswebapp.client.mvp.accession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gmi.gwaswebapp.client.dto.Accession;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionCellTableColumns.SearchTerm;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionCellTableColumns.SearchTerm.CRITERIA;
import com.gmi.gwaswebapp.client.resources.CellTableResources;
import com.gmi.gwaswebapp.client.resources.MyResources;
import com.gmi.gwaswebapp.client.ui.HasSearchHandlers;
import com.gmi.gwaswebapp.client.ui.SearchTextBox;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Style.Unit;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.visualizations.GeoMap;
import com.google.inject.Inject;

public class AccessionView extends ViewWithUiHandlers<AccessionUiHandlers> implements
		AccessionPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, AccessionView> {
	}
	
	private class AccessionTable extends CellTable<Accession> {

		public AccessionTable(int pageSize, Resources resources) {
			super(pageSize,resources,Accession.KEY_PROVIDER);
		}
		
		public void updateFocus() {
			
			
		}
	}
	
	 
	private MapWidget map;
	private final MyResources resources;
	private SingleSelectionModel<Accession> selectionModel = null;
	private final HashMap<Integer, Marker> accessionId2Marker = new HashMap<Integer, Marker>();
	private final HashMap<SearchTerm.CRITERIA,SearchTerm> searchTerms = new HashMap<SearchTerm.CRITERIA,SearchTerm>();
	private List<Accession> accessions;
	@UiField(provided=true) SimplePager pager;
	@UiField HTMLPanel mapContainer;
	@UiField GeoMap geomap;
	@UiField(provided=true) AccessionTable table;
	@UiField SearchTextBox Search_Name;
	@UiField SearchTextBox Search_Country;
	@UiField SearchTextBox Search_Collector;

	@Inject
	public AccessionView(final Binder binder,MyResources resources, final CellTableResources cellTableResources) {
		this.resources = resources;
		table = new AccessionTable(getPageSizeFromResolution(),cellTableResources);
		table.setWidth("100%", true);
		table.setHeight("100%");
		initCellTable();
		widget = binder.createAndBindUi(this);
		Search_Collector.getElement().setAttribute("placeHolder", "Collector");
		Search_Country.getElement().setAttribute("placeHolder", "Country");
		Search_Name.getElement().setAttribute("placeHolder", "Name or ID");
		Maps.loadMapsApi("ABQIAAAAHExtiY5_qKaTj9xWKMjl4xTtEpWa7kqih2tSZ3rpmArayZxetBS7MKdCA3ZN4ypVkrPZAbUr8J2cZw", "2", false, new Runnable() {
		      public void run() {
		    	  map = new MapWidget();
		    	  int size = widget.getOffsetHeight()-40;
		    	  if (size == 0)
		    		  size = 700;
		    	  //Math.round(size/2)+"px"
		    	  map.setSize("100%", "100%");
		    	  map.setContinuousZoom(true);
		    	  map.addControl(new LargeMapControl());
		  		  map.addControl(new MapTypeControl());
		  		  map.addMapType(MapType.getPhysicalMap());	//2009-4-9 add the terrain map
		  		  map.setCurrentMapType(MapType.getPhysicalMap());	//2009-4-9 set the terrain map as default
		  		  map.setScrollWheelZoomEnabled(true);
		    	  mapContainer.add(map);
	    		  addMarkers(accessions);
		      } 
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	private void initCellTable() {
		searchTerms.clear();
		searchTerms.put(SearchTerm.CRITERIA.Name, new SearchTerm(SearchTerm.CRITERIA.Name));
		searchTerms.put(SearchTerm.CRITERIA.Country, new SearchTerm(SearchTerm.CRITERIA.Country));
		searchTerms.put(SearchTerm.CRITERIA.Collector, new SearchTerm(SearchTerm.CRITERIA.Collector));
		searchTerms.put(SearchTerm.CRITERIA.AccessionID, new SearchTerm(SearchTerm.CRITERIA.AccessionID));
		SearchTerm searchTerm ;
		searchTerm = searchTerms.get(SearchTerm.CRITERIA.AccessionID);
		table.addColumn(new AccessionCellTableColumns.AccessionIdColumn(searchTerm),"ID");
		searchTerm = searchTerms.get(SearchTerm.CRITERIA.Name);
		table.addColumn(new AccessionCellTableColumns.NameColumn(searchTerm),"Name");
		table.addColumn(new AccessionCellTableColumns.LongitudeColunn(),"Lon");
		table.addColumn(new AccessionCellTableColumns.LatitudeColumn(),"Lat");
		searchTerm = searchTerms.get(SearchTerm.CRITERIA.Country);
		table.addColumn(new AccessionCellTableColumns.CountryColumn(searchTerm),"Country");
		searchTerm = searchTerms.get(SearchTerm.CRITERIA.Collector);
		table.addColumn(new AccessionCellTableColumns.CollectionDateColumn(),"Date");
		table.addColumn(new AccessionCellTableColumns.CollectorColumn(searchTerm),"Collector");
		
		table.setColumnWidth(table.getColumn(0),5, Unit.PCT);
		table.setColumnWidth(table.getColumn(1),15, Unit.PCT);
		table.setColumnWidth(table.getColumn(2),8, Unit.PCT);
		table.setColumnWidth(table.getColumn(3),8, Unit.PCT);
		table.setColumnWidth(table.getColumn(4),15, Unit.PCT);
		
		selectionModel = new SingleSelectionModel<Accession>(Accession.KEY_PROVIDER);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Marker marker = accessionId2Marker.get(selectionModel.getSelectedObject().getAccessionId());
				if (marker != null) {
					map.setCenter(marker.getLatLng(),6);
					InfoWindow info = map.getInfoWindow();
					info.open(marker, new InfoWindowContent(marker.getTitle()));
				}
			}
		});
		table.setSelectionModel(selectionModel);
		pager = new SimplePager();
		pager.setDisplay(table);
	}

	@Override
	public HasData<Accession> getDisplay() {
		return table;
	}

	@Override
	public void clearMap() {
		if (map != null)
			map.clearOverlays();
	}

	@Override
	public void addMarkers(List<Accession> accessions) {
		this.accessions = accessions;
		if (map== null)
			return;
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
	public SingleSelectionModel<Accession> getSelectionModel() {
		return selectionModel;
	}

	@Override
	public HashMap<SearchTerm.CRITERIA,SearchTerm> getSearchTerms() {
		return searchTerms;
	}

	@Override
	public void updateFocus() {
		table.updateFocus();
	}
	
	private int getPageSizeFromResolution() {
		///TODO Replace static dimensions with dynamic ones.
		int rowHeight=12;
		int pageSize = 30;
		int headerHeight = 135;
		int pagerHeight = 30;
		int topBarHeight = 92;
		int screen_width = Window.getClientWidth();
		if (screen_width <= 1210)
			rowHeight = 53;
		else if (screen_width <=1810)
			rowHeight = 38;
		else
			rowHeight = 24;
		if (pageSize >=33)
			pageSize = pageSize -1;
		pageSize = Math.round((Window.getClientHeight()-headerHeight - pagerHeight - topBarHeight) / rowHeight);
		return pageSize;
	}

	@Override
	public void drawLocationDistribution(AbstractDataTable data) {
		geomap.draw(data,createGeoMapOptions());
	}
	
	private GeoMap.Options createGeoMapOptions() {
		GeoMap.Options options = GeoMap.Options.create();
		//
		options.setHeight("100%");
		//geomap.getOffsetWidth()
		options.setWidth("100%");
		return options;
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
}

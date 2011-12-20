package com.gmi.gwaswebapp.client.mvp.accession;

import java.util.Date;

import com.gmi.gwaswebapp.client.dto.Accession;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionCellTableColumns.SearchTerm;
import com.gmi.gwaswebapp.client.resources.MyResources;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.DOM;
import com.google.inject.Inject;

public interface AccessionCellTableColumns {
	
	
	public class SearchTerm  {
		public static enum CRITERIA {AccessionID,Name,Longitude,Latitude,Country,Collector,CollectionDate}
		
		private final CRITERIA criteria;
		private String value ="";
		
		public SearchTerm(CRITERIA criteria) {
			this.criteria = criteria;
		}
		
		public CRITERIA getCriteria() {
			return criteria;
		}
		
		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		public RegExp getSearchRegExp() {
			if (value == null || value == "")
				return null;
			else
				return RegExp.compile("(" + value + ")", "ig");
		}
	}
	
	
	public static class SearchCell extends AbstractCell<SearchTerm>  {
		
		
		
		@Override
		public boolean isEditing(Context context, Element parent,
				SearchTerm value) {
			// TODO Auto-generated method stub
			return super.isEditing(context, parent, value);
		}

		@Override
		public boolean resetFocus(Context context, Element parent,
				SearchTerm value) {
			String test = "test";
			return true;
		}

		interface Template extends SafeHtmlTemplates {
			@Template("<div style=\"\">{0}</div>")
			SafeHtml header(String columnName);
			
			@Template("<div style=\"width:60px;float:left;\"><input type=\"text\" value=\"{0}\" placeHolder=\"Search\" class=\"{1}\" id=\"{2}\" /></div>")
			SafeHtml input(String value,String styleNames,String uniqueId);
		}
		
		private final MyResources resource;
		private static Template template;
		private boolean isChanged = false;
		private boolean isShowSearchBox = false;
		private String searchValue=null;
		
		public SearchCell(MyResources resource) {
			super("keydown","keyup","change","blur");
			this.resource = resource;
			if (template == null) {
				template = GWT.create(Template.class);
			}
		}
		
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				SearchTerm value, SafeHtmlBuilder sb) {
			final String id = DOM.createUniqueId();
			sb.append(template.header(value.getCriteria().toString()));
			sb.append(template.input(value.getValue(),resource.style().searchbox() + " " + resource.style().searchbox_white(),id));
			if (isChanged) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			        public void execute () {
			        	final InputElement elem = DOM.getElementById(id).cast();
			        	elem.focus();
			        }
			   });
			}
		}
		
		
		

		@Override
		public void onBrowserEvent(Context context,Element 	parent, SearchTerm value,NativeEvent event,ValueUpdater<SearchTerm> valueUpdater) {
			if (value == null)
				return;
			//super.onBrowserEvent(context, parent, value, event, valueUpdater);
			if ("keyup".equals(event.getType()))
			{
				isChanged = true;
				final InputElement elem = getInputElement(parent);
				value.setValue(elem.getValue());
				if (valueUpdater != null)
					valueUpdater.update(value);
			}
			else if ("blur".equals(event.getType())) {
				
			}
		}
		
		
		protected InputElement getInputElement(Element parent) {
			Element elem = parent.getElementsByTagName("input").getItem(0);
			assert(elem.getClass() == InputElement.class);
		    return elem.cast(); 
		  }
		
	
	}
	 
	public static class SearchHeader extends Header<SearchTerm> {
	
		@Override
		public void render(Context context, SafeHtmlBuilder sb) {
			super.render(context, sb);
		}

		private SearchTerm searchTerm;
		
		public SearchHeader(SearchTerm searchTerm,ValueUpdater<SearchTerm> valueUpdater,MyResources resource) {
			super(new SearchCell(resource));
			setUpdater(valueUpdater);
			this.searchTerm = searchTerm;
			// TODO Auto-generated constructor stub
		}

		@Override
		public SearchTerm getValue() {
			return searchTerm;
		}
	}
	
	
	public static  class HighlightCell extends AbstractCell<String> {

		private static final String replaceString = "<span style='color:red;font-weight:bold;'>$1</span>";
		private final SearchTerm searchTerm;
		
		public HighlightCell(SearchTerm searchTerm) {
			super();
			this.searchTerm = searchTerm;
		}
		
		@Override
		public void render(Context context, String value, SafeHtmlBuilder sb) {
		  if (value != null) {
		    if (searchTerm != null) {
		      RegExp searchRegExp = searchTerm.getSearchRegExp();
		      // The search regex has already been html-escaped
		      if (searchRegExp != null) {
		    	  value = searchRegExp.replace(SafeHtmlUtils.htmlEscape(value),replaceString);
		      	  sb.append(SafeHtmlUtils.fromTrustedString(value));
		      }
		      else { 
		    	  sb.appendEscaped(value);
		      }
		    } else {
		    	sb.appendEscaped(value);
		    }
		  }
		}
		
	}

	public static class AccessionIdColumn extends Column<Accession, Number> {
		public AccessionIdColumn() {
			super(new NumberCell(NumberFormat.getFormat("0")));
		}

		@Override
		public Integer getValue(Accession object) {
			return object.getAccessionId();
		}
	}
	
	public static class NameColumn extends Column<Accession,String>
	{
		public NameColumn(SearchTerm searchTerm)
		{
			super(new HighlightCell(searchTerm));
		}
		
		@Override
		public String getValue(Accession object) {
			return object.getName();
		}
	}
	
	public static class LatitudeColumn extends Column<Accession,Number> {

		public LatitudeColumn() {
			super(new NumberCell());
		}

		@Override
		public Number getValue(Accession object) {
			return object.getLatitude();
		}
	}
	
	public static class LongitudeColunn extends Column<Accession,Number> {
		public LongitudeColunn() {
			super(new NumberCell());
		}

		@Override
		public Number getValue(Accession object) {
			return object.getLongitude();
		}
	}
	
	public static class LongitudeLatitudeColumn extends Column<Accession,String> {

		public LongitudeLatitudeColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(Accession object) {
			return object.getLongitude() +"/" + object.getLatitude();
		}
	}
	
	public static class CountryColumn extends Column<Accession,String> {
		public CountryColumn(SearchTerm searchTerm) {
			super(new HighlightCell(searchTerm));
		}

		@Override
		public String getValue(Accession object) {
			return object.getCountry();
		}
	}
	
	public static class CollectorColumn extends Column<Accession,String> {
		public CollectorColumn(SearchTerm searchTerm) {
			super(new HighlightCell(searchTerm));
		}

		@Override
		public String getValue(Accession object) {
			return object.getCollector();
		}
	}
	
	public static class CollectionDateColumn extends Column<Accession,Date> {
		public CollectionDateColumn() {
			super(new DateCell(DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG)));
		}

		@Override
		public Date getValue(Accession object) {
			return object.getCollectionDate();
		}
	}
}

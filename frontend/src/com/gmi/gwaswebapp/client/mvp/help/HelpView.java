package com.gmi.gwaswebapp.client.mvp.help;

import java.util.Iterator;

import org.mortbay.resource.Resource;

import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gmi.gwaswebapp.client.NameTokens;
import com.gmi.gwaswebapp.client.resources.MyResources;
import com.gmi.gwaswebapp.client.ui.SlidingPanel;
import com.google.gwt.dom.client.Element;
import com.google.gwt.place.shared.Place;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class HelpView extends ViewImpl implements HelpPresenter.MyView {

	
	
	
	private final Widget widget;
	private String currentSection;
	private String currentSubSection;

	public interface Binder extends UiBinder<Widget, HelpView> {
	}
	
	@UiField SlidingPanel contentContainer;
	@UiField Tree tree;
	
	private final PlaceManager placeManager;
	private final MyResources resources;

	@Inject
	public HelpView(final Binder binder, final PlaceManager placeManager, final MyResources resources) {
		this.placeManager = placeManager;
		this.resources = resources;
		widget = binder.createAndBindUi(this);
		initTree();
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void showSection(UIObject display,String section,String sub_section) {
		String sectionToSelect = section;
		if (sub_section != "")
			sectionToSelect = sectionToSelect + "_"+sub_section;
		setSelectedItem(sectionToSelect,null);
		tree.ensureSelectedItemVisible();
		if (!section.equals(currentSection)) { 
			HTMLPanel container = new HTMLPanel("");
			container.setStylePrimaryName(resources.style().help_container());
			container.getElement().appendChild(display.getElement());
			contentContainer.setWidget(container);
			currentSection = section;
			currentSubSection = sub_section;
		}
		else
		{
			currentSubSection = sub_section;
		}
		if (!currentSubSection.equals("") ) {
			Element elem = DOM.getElementById(currentSection +"_" + currentSubSection);
			if (elem != null) {
				elem.scrollIntoView();
			}
		}
		else
			contentContainer.getWidget().getElement().setScrollTop(0);
	}
	
	private void initTree() {
		String section_key = "section";
		String subsection_key = "subsection";
		TreeItem root = new TreeItem("Help");
		PlaceRequest rootPlace = new PlaceRequest(NameTokens.helpPage);

		tree.addItem(new Hyperlink("Introduction",placeManager.buildHistoryToken(rootPlace.with(section_key, "introduction")))).setUserObject("introduction");
		
		//Phenotypes
		TreeItem phenotypes_item = new TreeItem(new Hyperlink("Phenotypes",placeManager.buildHistoryToken(rootPlace.with(section_key, "phenotypes"))));
		phenotypes_item.setUserObject("phenotypes");
		phenotypes_item.addItem(new Hyperlink("Uploading Phenotypes",placeManager.buildHistoryToken(rootPlace.with(section_key, "phenotypes").with(subsection_key, "upload")))).setUserObject("phenotypes_upload");
		phenotypes_item.addItem(new Hyperlink("Phenotype Format",placeManager.buildHistoryToken(rootPlace.with(section_key, "phenotypes").with(subsection_key, "format")))).setUserObject("phenotypes_format");
		tree.addItem(phenotypes_item);
		
		//Accessions
		TreeItem accession_item = new TreeItem(new Hyperlink("Accessions",placeManager.buildHistoryToken(rootPlace.with(section_key, "accessions"))));
		accession_item.setUserObject("accessions");
		tree.addItem(accession_item);

		//Analysis
		TreeItem analysis_item = new TreeItem(new Hyperlink("Analysis",placeManager.buildHistoryToken(rootPlace.with(section_key, "analysis"))));
		analysis_item.setUserObject("analysis");
		analysis_item.addItem(new Hyperlink("Navigation",placeManager.buildHistoryToken(rootPlace.with(section_key, "analysis").with(subsection_key, "navigation")))).setUserObject("analysis_navigation");
		analysis_item.addItem(new Hyperlink("Phenotypes",placeManager.buildHistoryToken(rootPlace.with(section_key, "analysis").with(subsection_key, "phenotypes")))).setUserObject("analysis_phenotypes");
		analysis_item.addItem(new Hyperlink("Datasets",placeManager.buildHistoryToken(rootPlace.with(section_key, "analysis").with(subsection_key, "datasets")))).setUserObject("analysis_datasets");
		analysis_item.addItem(new Hyperlink("Transformations",placeManager.buildHistoryToken(rootPlace.with(section_key, "analysis").with(subsection_key, "transformations")))).setUserObject("analysis_transformations");
		analysis_item.addItem(new Hyperlink("Results",placeManager.buildHistoryToken(rootPlace.with(section_key, "analysis").with(subsection_key, "results")))).setUserObject("analysis_results");
		tree.addItem(analysis_item);
		
		//Tutorial
		TreeItem tutorial_item = new TreeItem(new Hyperlink("Tutorial",placeManager.buildHistoryToken(rootPlace.with(section_key, "tutorial"))));
		tutorial_item.setUserObject("tutorial");
		tutorial_item.addItem(new Hyperlink("1.) Uploading Phenotypes",placeManager.buildHistoryToken(rootPlace.with(section_key, "tutorial").with(subsection_key, "uploading")))).setUserObject("tutorial_uploading");
		tutorial_item.addItem(new Hyperlink("2.) Creating subset",placeManager.buildHistoryToken(rootPlace.with(section_key, "tutorial").with(subsection_key, "subset")))).setUserObject("tutorial_subset");
		tutorial_item.addItem(new Hyperlink("3.) Changing transformation",placeManager.buildHistoryToken(rootPlace.with(section_key, "tutorial").with(subsection_key, "transformation")))).setUserObject("tutorial_transformation");
		tutorial_item.addItem(new Hyperlink("4.) Running GWAS",placeManager.buildHistoryToken(rootPlace.with(section_key, "tutorial").with(subsection_key, "gwas")))).setUserObject("tutorial_gwas");
		tutorial_item.addItem(new Hyperlink("5.) Viewing Results",placeManager.buildHistoryToken(rootPlace.with(section_key, "tutorial").with(subsection_key, "results")))).setUserObject("tutorial_results");
		tutorial_item.addItem(new Hyperlink("6.) Running Step-Wise GWAS",placeManager.buildHistoryToken(rootPlace.with(section_key, "tutorial").with(subsection_key, "gwas_step")))).setUserObject("tutorial_gwas_step");
		tree.addItem(tutorial_item);
		Iterator<TreeItem> iterator = tree.treeItemIterator();
		while (iterator.hasNext()) {
			iterator.next().setState(true,false);
		}
	}
	
	private void setSelectedItem(String section,Iterator<TreeItem> iterator) {
		if (iterator == null) {
			if (tree.getSelectedItem() != null) {
				if (tree.getSelectedItem().getUserObject().equals(section))
					return;
			}
				
			iterator = tree.treeItemIterator();
		}
		while(iterator.hasNext()) {
			TreeItem item = iterator.next();
			if (item.getUserObject().equals(section)) {
				tree.setSelectedItem(item,false);
				return;
			}
		}
	}
}

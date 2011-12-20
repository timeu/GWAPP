package com.gmi.gwaswebapp.client.mvp.phenotype.details;

import com.gmi.gwaswebapp.client.mvp.phenotype.details.PhenotypeDetailPresenter.MyView;
import com.gmi.gwaswebapp.client.resources.MyResources;
import com.gmi.gwaswebapp.client.ui.SlidingPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class PhenotypeDetailView extends ViewImpl implements MyView {

	private static PhenotypeDetailViewUiBinder uiBinder = GWT
			.create(PhenotypeDetailViewUiBinder.class);


	interface PhenotypeDetailViewUiBinder extends
			UiBinder<Widget, PhenotypeDetailView> {
	}
	
	protected Widget widget;
	
	@UiField InlineLabel label_phenotype_name;
	@UiField InlineLabel label_num_values;
	@UiField InlineLabel label_stddev;
	@UiField InlineLabel label_condition;
	@UiField InlineLabel label_scoring;
	@UiField InlineLabel label_method;
	@UiField InlineLabel label_scale;
	@UiField HTMLPanel detailContent;
	//@UiField HTMLPanel transformationContent;
	//@UiField HTMLPanel transformationDetailContent;
	@UiField(provided=true) MyResources mainRes;

	@Inject
	public PhenotypeDetailView(MyResources resources) {
		this.mainRes = resources;
		widget = uiBinder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public HasText getPhenotypeName() {
		return label_phenotype_name;
	}
	
	@Override
	public HasText getStddev()  {
		return label_stddev;
	}
	
	@Override
	public HasText getNumvalues()  {
		return label_num_values;
	}
	
	@Override
	public HasText getCondition()  {
		return label_condition;
	}
	
	@Override
	public HasText getScoring()  {
		return label_scoring;
	}
	
	@Override
	public HasText getMethod()  {
		return label_method;
	}
	
	@Override
	public HasText getScale()  {
		return label_scale;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == PhenotypeDetailPresenter.TYPE_SetDatasetListContent)
			detailContent.clear();
			if (content != null)
				detailContent.add(content);
		else
			super.setInSlot(slot, content);
	}
}

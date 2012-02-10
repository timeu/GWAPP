package com.gmi.gwaswebapp.client.ui;

import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class QQPlotPopup extends Composite {

	private static QQPlotPopupUiBinder uiBinder = GWT
			.create(QQPlotPopupUiBinder.class);

	interface QQPlotPopupUiBinder extends UiBinder<Widget, QQPlotPopup> {
	}

	public QQPlotPopup() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiField Image qq_plot;
	
	protected Transformation transformation = null;
	protected Analysis analysis = null;
	private static String BASEURL = "/gwas/getQQPlotImage"; 
	
	public void setData(Transformation transformation) {
		this.analysis = null;
		this.transformation = transformation;
		initImageUrl();
	}
	
	public void setData(Analysis analysis) {
		this.analysis = analysis;
		this.transformation = null;
		initImageUrl();
	}

	
	protected void initImageUrl() {
		String url = BASEURL;
		if (analysis != null) 
			 url = url + "?phenotype="+analysis.getPhenotype()+"&dataset=Fullset&transformation="+analysis.getTransformation()+"&analysis=" + analysis.getType().toString()+"&result_name="+analysis.getResultName();
		else
			url = url + "?phenotype="+transformation.getPhenotype()+"&dataset=Fullset&transformation="+transformation.getName();
			
		qq_plot.setUrl(url);
	}
}

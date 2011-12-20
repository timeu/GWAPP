package com.gmi.gwaswebapp.client.mvp.transformation.details;

import com.gmi.gwaswebapp.client.command.GetTransformationAction;
import com.gmi.gwaswebapp.client.command.GetTransformationActionResult;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.visualization.client.DataTable;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class TransformationDetailPresenter extends PresenterWidget<TransformationDetailPresenter.MyView>  {


	public interface MyView extends View{

		void setData(DataTable transformationDataTable,DataTable motionchartDataTable,String transformation);
		void attachCharts();
		void detachCharts();
	}
	
	protected Transformation transformation;
	protected DataTable histogram_data;
	protected final DispatchAsync dispatch;
	
	@Inject
	public TransformationDetailPresenter(EventBus eventBus, MyView view,final DispatchAsync dispatch) {
		super(eventBus, view);
		this.dispatch = dispatch;
	}
	
	public void setTransformation(final Transformation transformation) {
		this.transformation = transformation;
		dispatch.execute(new GetTransformationAction(transformation.getPhenotype(), transformation.getDataset(),transformation.getName()), new GWASCallback<GetTransformationActionResult>(getEventBus()) {
			@Override
			public void onSuccess(GetTransformationActionResult result) {
				getView().setData(result.getTransformationDataTable(),result.getMotionchartDataTable(),transformation.getName());
			}
		});
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		getView().detachCharts();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().attachCharts();
	}
}

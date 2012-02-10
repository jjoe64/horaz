package com.horaz.client.widgets.charting;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONArray;
import com.horaz.client.widgets.BaseWidget;

public class Plot extends BaseWidget<DivElement> {
	static public Plot byId(String id) {
		Element elm = getElementById(id);
		if (allWidgetInstances.get(elm) != null) {
			return (Plot) allWidgetInstances.get(elm);
		}
		return new Plot((DivElement) elm);
	}

	private final List<Series> series = new ArrayList<Series>();
	private final PlotOptions options = new PlotOptions();

	protected Plot(DivElement elm) {
		super(elm);
	}

	native private void _init(Element elm, JavaScriptObject series, JavaScriptObject options)/*-{
		$wnd.jQuery.plot($wnd.jQuery(elm), series, options);
	}-*/;

	public void addSeries(Series series) {
		this.series.add(series);
	}

	public PlotOptions getOptions() {
		return options;
	}

	public void render() {
		JSONArray a = new JSONArray();
		for (int i=0; i<series.size(); i++) {
			a.set(i, series.get(i));
		}
		_init(getElement(), a.getJavaScriptObject(), options.getJavaScriptObject());
	}
}

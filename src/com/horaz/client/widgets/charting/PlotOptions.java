package com.horaz.client.widgets.charting;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;

public class PlotOptions extends BaseOptions {
	public XaxisOptions getXaxisOptions(int idx) {
		JSONArray xaxes = (JSONArray) get("xaxes");
		if (xaxes != null) {
			return (XaxisOptions) xaxes.get(idx);
		}
		return null;
	}

	public YaxisOptions getYaxisOptions(int idx) {
		JSONArray yaxes = (JSONArray) get("yaxes");
		if (yaxes != null) {
			return (YaxisOptions) yaxes.get(idx);
		}
		return null;
	}

	public void setBackgroundColor(String color) {
		_setOption("grid", "backgroundColor", new JSONString(color));
	}

	public void setColors(String[] colors) {
		JSONArray js = new JSONArray();
		for (int i=0; i<colors.length; i++) {
			js.set(i, new JSONString(colors[i]));
		}
		put("colors", js);
	}

	public void setGridColor(String color) {
		_setOption("grid", "color", new JSONString(color));
	}

	public void setLegendBackgroundColor(String color) {
		_setOption("legend", "backgroundColor", new JSONString(color));
	}

	public void setXaxes(XaxisOptions... options) {
		JSONArray xaxes = new JSONArray();
		for (int i=0; i<options.length; i++) {
			xaxes.set(i, options[i]);
		}
		put("xaxes", xaxes);
	}

	public void setYaxes(YaxisOptions... options) {
		JSONArray yaxes = new JSONArray();
		for (int i=0; i<options.length; i++) {
			yaxes.set(i, options[i]);
		}
		put("yaxes", yaxes);
	}
}

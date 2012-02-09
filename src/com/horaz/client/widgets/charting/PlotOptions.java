package com.horaz.client.widgets.charting;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class PlotOptions extends JSONObject {
	private void _setOption(String option, String key, JSONValue value) {
		JSONObject opt = (JSONObject) get(option);
		if (opt == null) {
			opt = new JSONObject();
			put(option, opt);
		}
		opt.put(key, value);
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

	public void setXaxisColor(String color) {
		_setOption("xaxis", "color", new JSONString(color));
	}
}

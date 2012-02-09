package com.horaz.client.widgets.charting;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;


public class Series extends JSONObject {
	public Series(String label, Point... data) {
		put("label", new JSONString(label));
		JSONArray a = new JSONArray();
		for (int i=0; i<data.length; i++) {
			a.set(i, data[i]);
		}
		put("data", a);
	}
}

package com.horaz.client.widgets.charting;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;


public class Point extends JSONArray {
	private static final long serialVersionUID = 7259876416370992539L;

	public Point(double x, double y) {
		set(0, new JSONNumber(x));
		set(1, new JSONNumber(y));
	}
}

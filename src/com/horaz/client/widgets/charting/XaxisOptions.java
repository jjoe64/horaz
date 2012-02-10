package com.horaz.client.widgets.charting;

import com.google.gwt.json.client.JSONString;


public class XaxisOptions extends BaseOptions {
	public void setModeTime(boolean time) {
		put("mode", time?new JSONString("time"):null);
	}
}

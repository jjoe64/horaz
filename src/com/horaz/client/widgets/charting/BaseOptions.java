package com.horaz.client.widgets.charting;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

abstract class BaseOptions extends JSONObject {
	protected void _setOption(String option, String key, JSONValue value) {
		JSONObject opt = (JSONObject) get(option);
		if (opt == null) {
			opt = new JSONObject();
			put(option, opt);
		}
		opt.put(key, value);
	}
}

package com.horaz.client.widgets.charting;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;

public class YaxisOptions extends BaseOptions {
	public enum YaxisPosition {
		LEFT, RIGHT
	}
	public void setAlignTicksWithAxis(Integer axisNumber) {
		put("alignTicksWithAxis", axisNumber==null?null:new JSONNumber(axisNumber));
	}

	public void setPosition(YaxisPosition pos) {
		put("position", new JSONString(pos.name().toLowerCase()));
	}

	public void setTickFormatter(TickFormatter tickFormatter) {
		put("tickFormatter", tickFormatter);
	}
}

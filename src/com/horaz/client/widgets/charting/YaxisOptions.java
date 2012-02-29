package com.horaz.client.widgets.charting;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;

public class YaxisOptions extends BaseOptions {
	public enum YaxisPosition {
		LEFT, RIGHT
	}

	private static native void setTickFormatterNative( JavaScriptObject axisOptions, TickFormatter tickFormatter )
	/*-{
		axisOptions.tickFormatter = function(val, axis) {
			return tickFormatter.@com.horaz.client.widgets.charting.TickFormatter::formatTickValue(Ljava/lang/Double;)(@java.lang.Double::valueOf(D)(val));
		};
	}-*/;

	public void setAlignTicksWithAxis(Integer axisNumber) {
		put("alignTicksWithAxis", axisNumber==null?null:new JSONNumber(axisNumber));
	}

	public void setMin(Integer min) {
		put("min", min==null?null:new JSONNumber(min));
	}

	public void setPosition(YaxisPosition pos) {
		put("position", new JSONString(pos.name().toLowerCase()));
	}

	public void setTickDecimals(Integer no) {
		put("tickDecimals", no==null?null:new JSONNumber(no));
	}

	public void setTickFormatter(TickFormatter tickFormatter) {
		setTickFormatterNative( getJavaScriptObject(), tickFormatter );
	}
}

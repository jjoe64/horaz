/**
 * This file is part of Horaz.
 *
 * Horaz is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Horaz is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Horaz.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright Joe's App Factory UG (haftungsbeschränkt)
 */

package com.horaz.client.widgets.charting;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;

public abstract class BaseAxisOptions extends BaseOptions {
	private static native void setTickFormatterNative( JavaScriptObject axisOptions, TickFormatter tickFormatter )
	/*-{
		axisOptions.tickFormatter = function(val, axis) {
			return tickFormatter.@com.horaz.client.widgets.charting.TickFormatter::formatTickValue(Ljava/lang/Double;)(@java.lang.Double::valueOf(D)(val));
		};
	}-*/;

	public void setAlignTicksWithAxis(Integer axisNumber) {
		put("alignTicksWithAxis", axisNumber==null?null:new JSONNumber(axisNumber));
	}

	public void setAutoscaleMargin(int percent) {
		put("autoscaleMargin", new JSONNumber(percent));
	}

	public void setColor(String color) {
		put("color", new JSONString(color));
	}

	public void setMin(Integer min) {
		put("min", min==null?null:new JSONNumber(min));
	}

	public void setModeTime(boolean time) {
		put("mode", time?new JSONString("time"):null);
	}

	public void setTickDecimals(Integer no) {
		put("tickDecimals", no==null?null:new JSONNumber(no));
	}

	public void setTickFormatter(TickFormatter tickFormatter) {
		setTickFormatterNative( getJavaScriptObject(), tickFormatter );
	}

	public void setTickLength(int i) {
		put("tickLength", new JSONNumber(i));
	}

	public void setTickSize(int i) {
		put("tickSize", new JSONNumber(i));
	}

	public void setTickSize(int i, String unit) {
		JSONArray a = new JSONArray();
		a.set(0, new JSONNumber(i));
		a.set(1, new JSONString(unit));
		put("tickSize", a);
	}

}

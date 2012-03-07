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

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;

public class PlotOptions extends BaseOptions {
	public enum LegendPosition {
		NORTH_EAST("ne")
		, SOUTH_WEST("sw")
		, NORD_WEST("nw");

		private final JSONString json;
		LegendPosition(String json) {
			this.json = new JSONString(json);
		}
		JSONString getJSON() {
			return json;
		}
	}

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

	public void setLegendPosition(LegendPosition pos) {
		_setOption("legend", "position", pos.getJSON());
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

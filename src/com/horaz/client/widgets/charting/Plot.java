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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONArray;
import com.horaz.client.widgets.BaseWidget;

/**
 * Chart widget.
 * Just wrap an empty DIV element.
 *
 * This uses the jquery plugin Flot.
 * http://code.google.com/p/flot/
 *
 * @horaz.htmltag &lt;div&gt;
 */
public class Plot extends BaseWidget<DivElement> {
	static public Plot byId(String id) {
		Element elm = getElementById(id);
		if (allWidgetInstances.get(elm) != null) {
			return (Plot) allWidgetInstances.get(elm);
		}
		return new Plot((DivElement) elm);
	}

	private final List<Series> series = new ArrayList<Series>();
	private final PlotOptions options = new PlotOptions();

	protected Plot(DivElement elm) {
		super(elm);
	}

	native private void _init(Element elm, JavaScriptObject series, JavaScriptObject options)/*-{
		$wnd.jQuery.plot($wnd.jQuery(elm), series, options);
	}-*/;

	public void addSeries(Series series) {
		this.series.add(series);
	}

	public void clearData() {
		series.clear();
	}

	public PlotOptions getOptions() {
		return options;
	}

	/**
	 * creates/refreshes the plot
	 */
	public void render() {
		JSONArray a = new JSONArray();
		for (int i=0; i<series.size(); i++) {
			a.set(i, series.get(i));
		}
		_init(getElement(), a.getJavaScriptObject(), options.getJavaScriptObject());
	}
}

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

package com.horaz.client.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SelectElement;
import com.horaz.client.widgets.events.ChangeListener;
import com.horaz.client.widgets.events.EventFactory;

/**
 * SelectMenu
 * @horaz.htmltag &lt;select&gt;
 * @horaz.events change
 */
public class SelectMenu extends BaseWidget<SelectElement> {
	private native static void _open(Element elm)/*-{
		$wnd.jQuery(elm).selectmenu('open');
	}-*/;

	/**
	 * finds selectmenu element for given id
	 * the html element has to be a select element
	 * @param id
	 */
	static public SelectMenu byId(String id) {
		Element elm = getElementById(id);
		if (allWidgetInstances.get(elm) != null) {
			return (SelectMenu) allWidgetInstances.get(elm);
		}
		return new SelectMenu((SelectElement) elm);
	}

	protected SelectMenu(SelectElement elm) {
		super(elm);
	}

	public void addChangeListener(ChangeListener changeListener) {
		EventFactory.bindEventHandler(getElement(), "change", changeListener);
	}

	/**
	 * opens the selectmenu. This is only working if the property data-native-menu=false is set.
	 * @throws IllegalStateException if data-native-menu is not false
	 */
	public void open() {
		if (getElement().getAttribute("data-native-menu") == null || Boolean.parseBoolean(getElement().getAttribute("data-native-menu"))) {
			throw new IllegalStateException("The attribute data-native-menu=false is missing.");
		}
		_open(getElement());
	}
}

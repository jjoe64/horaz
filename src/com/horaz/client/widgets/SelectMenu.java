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
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.DOM;
import com.horaz.client.widgets.events.ChangeListener;
import com.horaz.client.widgets.events.EventFactory;

/**
 * SelectMenu
 * @horaz.htmltag &lt;select&gt;
 * @horaz.events change
 */
public class SelectMenu extends BaseWidget<SelectElement> {
	native static private void _refresh(Element elm) /*-{
		$wnd.jQuery(elm).selectmenu('refresh');
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

	/**
	 * generates a new option element. see {@link #setOptions(OptionElement[])}
	 * how to add options to a select element
	 * @param text
	 * @param value
	 * @return
	 */
	public static OptionElement createOption(String text, String value) {
		OptionElement o = OptionElement.as(DOM.createOption());
		o.setText(text);
		o.setValue(value);
		return o;
	}

	protected SelectMenu(SelectElement elm) {
		super(elm);
	}

	/**
	 * is called when the value has changed.
	 * @param changeListener
	 */
	public void addChangeListener(ChangeListener changeListener) {
		EventFactory.bindEventHandler(getElement(), "change", changeListener);
	}

	/**
	 * removes all options and adds all the option elements
	 * @param opts
	 */
	public void setOptions(OptionElement[] opts) {
		getElement().clear();
		for (OptionElement opt : opts) {
			getElement().add(opt, null);
		}
		_refresh(getElement());
	}

	public void setSelectedIndex(int selectIndex) {
		getElement().setSelectedIndex(selectIndex);
		_refresh(getElement());
	}
}

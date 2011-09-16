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

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

/**
 * Toast Notification
 */
public class Toast extends BaseWidget<DivElement> {
	/**
	 * defines how long the toast will be displayed
	 */
	public enum Duration {
		SHORT, LONG
	}

	/**
	 * finds toast element for given id
	 * @param id
	 */
	static public Toast byId(String id) {
		Element elm = getElementById(id);
		if (allWidgetInstances.get(elm) != null) {
			return (Toast) allWidgetInstances.get(elm);
		}
		return new Toast((DivElement) elm);
	}

	protected Toast(DivElement elm) {
		super(elm);
	}

	/**
	 * creates a new Toast notification
	 * @param innerHTML
	 * @param duration
	 */
	public Toast(String innerHTML, Duration duration) {
		super(null);

		Element elm = DOM.createDiv();
		elm.setInnerHTML(innerHTML);
		Document.get().getBody().appendChild(elm);

		setElement((DivElement) elm);
		allWidgetInstances.put(elm, this);

		_create(elm, duration.name().toLowerCase());
	}

	native private void _create(Element elm, String duration) /*-{
		$wnd.jQuery(elm).toast({duration:duration})
	}-*/;

	native private void _show(Element elm) /*-{
		$wnd.jQuery(elm).toast('show')
	}-*/;

	/**
	 * shows the toast. automatically hides the toast after the defined timeout.
	 */
	public void show() {
		_show(getElement());
	}
}

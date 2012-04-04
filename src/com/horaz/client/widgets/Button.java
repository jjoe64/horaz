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

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Anchor;
import com.horaz.client.widgets.events.EventFactory;
import com.horaz.client.widgets.events.TapListener;

/**
 * Button
 * @horaz.htmltag &lt;a data-role="button"&gt;
 * @horaz.events tap
 * @see https://www.horaz-lang.com/dev-guide/button
 */
public class Button extends BaseWidget<AnchorElement> {
	public enum IconPosition {
		NO_TEXT("notext")
		, LEFT("left")
		, RIGHT("right")
		, TOP("top")
		, BOTTOM("bottom");

		private String v;

		IconPosition(String v) { this.v = v; }
		String getValue() { return v; }
	}

	/**
	 * finds button element for given id
	 * the html element has to be an anchor element
	 * @param id
	 */
	static public Button byId(String id) {
		Element elm = getElementById(id);
		if (allWidgetInstances.get(elm) != null) {
			return (Button) allWidgetInstances.get(elm);
		}
		return new Button((AnchorElement) elm);
	}

	public Button() {
		super(null);
		Anchor a = new Anchor();
		setElement(AnchorElement.as(a.getElement()));
		getElement().setAttribute("data-role", "button");
	}

	protected Button(AnchorElement elm) {
		super(elm);
	}

	private native void _init(Element element)/*-{
		$wnd.jQuery(element).button();
	}-*/;

	/**
	 * adds a listener for tap (touch / click) event
	 * <strong>important</strong>: when you use a tapListener, the anchor must NOT have a href-attribute!
	 * 	if you want to do a redirect use {@link Location#replace(String)} instead.
	 * @param tapListener
	 */
	public void addTapListener(TapListener tapListener) {
		EventFactory.bindEventHandler(getElement(), "tap", tapListener);
	}

	public void appendTo(Node lastChild) {
		lastChild.appendChild(getElement());
		_init(getElement());
	}

	public void setIcon(String value) {
		getElement().setAttribute("data-icon", value);
	}

	public void setIconPosition(IconPosition pos) {
		getElement().setAttribute("data-iconpos", pos.getValue());
	}

	public void setTheme(Theme t) {
		getElement().setAttribute("data-theme", t.name().toLowerCase());
	}
}

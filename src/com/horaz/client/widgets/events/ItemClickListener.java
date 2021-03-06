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

package com.horaz.client.widgets.events;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.user.client.Event;
import com.horaz.client.model.BaseModel;
import com.horaz.client.widgets.BaseWidget;
import com.horaz.client.widgets.ListView;

/**
 * when a item was clicked in the listview, this event will be fired.
 * It refers to the corresponding model.
 *
 * @param <T> model class
 */
public abstract class ItemClickListener<T extends BaseModel> implements F {
	@Override
	public void f(Event event) {
		// extract selected list item
		if (Element.is(event.getEventTarget())) {
			Element elm = Element.as(event.getEventTarget());
			Element ul = null;
			Element li = null;
			Element a = null;
			// find <ul>, <li> and <a>
			while (elm.hasParentElement()) {
				if (elm.getTagName().equals("A")) {
					a = elm;
				} else if (elm.getTagName().equals("LI")) {
					li = elm;
				} else if (elm.getTagName().equals("UL")) {
					ul = elm;
					break;
				}
				elm = elm.getParentElement();
			}

			if (ul != null && li != null) {
				@SuppressWarnings("unchecked")
				ListView<T> lv = (ListView<T>) BaseWidget.allWidgetInstances.get(ul);
				AnchorElement aElm = AnchorElement.as(a);
				if (lv.getDataStore() == null) {
					onItemClick(event, null, aElm);
				} else {
					onItemClick(event, lv.getModel((LIElement) li), aElm);
				}
			}
		}
	}

	/**
	 * is called when the event occurs.
	 *
	 * @param event
	 * @param model
	 * @param aElm
	 */
	public abstract void onItemClick(Event event, T model, AnchorElement aElm);
}

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

import com.google.gwt.dom.client.Element;

/**
 * helper for native event binding
 */
public class EventFactory {
	/**
	 * binds event 'tap' with function
	 * @param elm DOM element
	 * @param fn Function
	 */
	native static public void bindEventHandler(Element elm, String type, F fn) /*-{
		$wnd.jQuery(elm).bind(type, function(e){fn.@com.horaz.client.widgets.events.F::f(Lcom/google/gwt/user/client/Event;)(e);})
	}-*/;

	native static public void delegateEventHandler(Element elm, String selector, String type, F fn) /*-{
		$wnd.jQuery(elm).delegate(selector, type, function(e){fn.@com.horaz.client.widgets.events.F::f(Lcom/google/gwt/user/client/Event;)(e);})
	}-*/;

	private EventFactory() {}
}

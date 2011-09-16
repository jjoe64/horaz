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

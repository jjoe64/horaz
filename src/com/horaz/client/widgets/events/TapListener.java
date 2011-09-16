package com.horaz.client.widgets.events;

import com.google.gwt.user.client.Event;

/**
 * tap listener. is used for handling tap/touch/click events
 */
public abstract class TapListener implements F {
	@Override
	public void f(Event event) {
		onTap(event);
	}

	/**
	 * is called when the event occurs.
	 * @param event
	 */
	public abstract void onTap(Event event);
}

package com.horaz.client.widgets.events;

import com.google.gwt.user.client.Event;

abstract public class PageCreateListener implements F {
	@Override
	public void f(Event event) {
		onPageCreate();
	}

	/**
	 * is called when the event occurs.
	 */
	public abstract void onPageCreate();
}

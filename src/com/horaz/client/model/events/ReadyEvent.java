package com.horaz.client.model.events;

import com.google.gwt.event.shared.GwtEvent;

public class ReadyEvent extends GwtEvent<ReadyListener> {
	@SuppressWarnings("rawtypes")
	private static Type TYPE;
	@SuppressWarnings("unchecked")
	static public Type<ReadyListener> getType() {
		if (TYPE == null) {
			TYPE = new Type<ReadyListener>();
		}
		return TYPE;
	}

	@Override
	protected void dispatch(ReadyListener handler) {
		handler.onReady(this);
	}

	@Override
	public Type<ReadyListener> getAssociatedType() {
		return getType();
	}
}
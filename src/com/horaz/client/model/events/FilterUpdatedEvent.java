package com.horaz.client.model.events;

import com.google.gwt.event.shared.GwtEvent;

public class FilterUpdatedEvent extends GwtEvent<FilterUpdatedListener> {
	@SuppressWarnings("rawtypes")
	private static Type TYPE;
	@SuppressWarnings("unchecked")
	static public Type<FilterUpdatedListener> getType() {
		if (TYPE == null) {
			TYPE = new Type<FilterUpdatedListener>();
		}
		return TYPE;
	}

	@Override
	protected void dispatch(FilterUpdatedListener handler) {
		handler.onFilterUpdated(this);
	}

	@Override
	public Type<FilterUpdatedListener> getAssociatedType() {
		return getType();
	}
}

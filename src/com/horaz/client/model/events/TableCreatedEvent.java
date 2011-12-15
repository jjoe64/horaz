package com.horaz.client.model.events;

import com.google.gwt.event.shared.GwtEvent;

public class TableCreatedEvent extends GwtEvent<TableCreatedListener> {
	@SuppressWarnings("rawtypes")
	private static Type TYPE;
	@SuppressWarnings("unchecked")
	static public Type<TableCreatedListener> getType() {
		if (TYPE == null) {
			TYPE = new Type<TableCreatedListener>();
		}
		return TYPE;
	}

	@Override
	protected void dispatch(TableCreatedListener handler) {
		handler.onTableCreated(this);
	}

	@Override
	public Type<TableCreatedListener> getAssociatedType() {
		return getType();
	}
}
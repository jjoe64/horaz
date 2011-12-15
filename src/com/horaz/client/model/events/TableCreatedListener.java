package com.horaz.client.model.events;

import com.google.gwt.event.shared.EventHandler;

public interface TableCreatedListener extends EventHandler {
	public void onTableCreated(TableCreatedEvent event);
}

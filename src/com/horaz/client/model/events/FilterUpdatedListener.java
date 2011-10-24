package com.horaz.client.model.events;

import com.google.gwt.event.shared.EventHandler;

public interface FilterUpdatedListener extends EventHandler {
	public void onFilterUpdated(FilterUpdatedEvent event);
}

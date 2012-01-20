package com.horaz.client.model.events;

import com.google.gwt.event.shared.EventHandler;

public interface ReadyListener extends EventHandler {
	public void onReady(ReadyEvent event);
}

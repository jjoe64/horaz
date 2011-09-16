package com.horaz.client.model.events;

import com.google.gwt.event.shared.EventHandler;
import com.horaz.client.model.BaseModel;

/**
 * catch {@link ModelUpdatedEvent}
 * @param <T> model class
 */
public interface ModelUpdatedListener<T extends BaseModel> extends EventHandler {
	/**
	 * model was updated
	 * @param event contains updated Model
	 */
	void onModelUpdated(ModelUpdatedEvent<T> event);
}

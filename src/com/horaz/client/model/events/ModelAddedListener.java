package com.horaz.client.model.events;

import com.google.gwt.event.shared.EventHandler;
import com.horaz.client.model.BaseModel;

/**
 * catches a model added event, that is fired when a new model was added to the datastore
 * @param <T> used model class
 */
public interface ModelAddedListener<T extends BaseModel> extends EventHandler {
	/**
	 * a new model was added to the datastore
	 * @param event source
	 */
	void onModelAdded(ModelAddedEvent<T> event);
}

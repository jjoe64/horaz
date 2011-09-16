package com.horaz.client.model.events;

import com.google.gwt.event.shared.EventHandler;
import com.horaz.client.model.BaseModel;

/**
 * catches a {@link ModelRemovedEvent} when a model was removed
 * @param <T> model class
 */
public interface ModelRemovedListener<T extends BaseModel> extends EventHandler {
	/**
	 * model was removed
	 * @param event contains the removed model
	 */
	void onModelRemoved(ModelRemovedEvent<T> event);
}

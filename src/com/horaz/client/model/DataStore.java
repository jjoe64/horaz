package com.horaz.client.model;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.horaz.client.model.events.ModelAddedEvent;
import com.horaz.client.model.events.ModelAddedListener;
import com.horaz.client.model.events.ModelRemovedEvent;
import com.horaz.client.model.events.ModelRemovedListener;
import com.horaz.client.model.events.ModelUpdatedEvent;
import com.horaz.client.model.events.ModelUpdatedListener;

/**
 * abstract datastore
 * use any subclass
 *
 * @param <T> used model class
 * @horaz.events modelAdded, modelRemoved, modelUpdated
 * @see http://www.dev-horaz.com/dev-guide/datastore
 */
public abstract class DataStore<T extends BaseModel> implements HasHandlers {
	private final HandlerManager handlerManager = new HandlerManager(this);

	public DataStore() {}

	/**
	 * adds a new model to the store.
	 * {@link ModelAddedEvent} is fired here.
	 * @param newModel
	 */
	public void add(T newModel) {
		fireEvent(new ModelAddedEvent<T>(newModel));
	}

	/**
	 * register a handler for {@link ModelAddedEvent} Event
	 * @param handler
	 * @return gwt handler registration
	 */
	public HandlerRegistration addModelAddedListener(ModelAddedListener<T> handler) {
		Type<ModelAddedListener<T>> type = ModelAddedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	/**
	 * register a handler for {@link ModelRemovedEvent} Event
	 * @param handler
	 * @return gwt handler registration
	 */
	public HandlerRegistration addModelRemovedListener(ModelRemovedListener<T> handler) {
		Type<ModelRemovedListener<T>> type = ModelRemovedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	/**
	 * register a handler for {@link ModelUpdatedEvent} Event
	 * @param handler
	 * @return gwt handler registration
	 */
	public HandlerRegistration addModelUpdatedListener(ModelUpdatedListener<T> handler) {
		Type<ModelUpdatedListener<T>> type = ModelUpdatedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	/**
	 * get a model by the unique model id
	 * @param id model id
	 * @return model or null
	 */
	public abstract T get(int id);

	/**
	 * @return the current models
	 */
	public abstract List<T> getModels();

	/**
	 * removes the model from the datastore. This fires a {@link ModelRemovedEvent} Event.
	 * Connected ListViews will automatically removes the list item.
	 * @param model
	 */
	public void remove(T model) {
		fireEvent(new ModelRemovedEvent<T>(model));
	}

	/**
	 * fires {@link ModelUpdatedEvent} event, so that connected listviews updates the list item
	 * @param saveModel
	 */
	public void update(T saveModel) {
		fireEvent(new ModelUpdatedEvent<T>(saveModel));
	}
}

package com.horaz.client.model.events;

import com.google.gwt.event.shared.GwtEvent;
import com.horaz.client.model.BaseModel;

/**
 * is fired when the datastore updates a model
 * @param <T> model class
 */
public class ModelUpdatedEvent<T extends BaseModel> extends GwtEvent<ModelUpdatedListener<T>> {
	@SuppressWarnings("rawtypes")
	private static Type TYPE;
	@SuppressWarnings("unchecked")
	static public <T extends BaseModel> Type<ModelUpdatedListener<T>> getType() {
		if (TYPE == null) {
			TYPE = new Type<ModelUpdatedListener<T>>();
		}
		return TYPE;
	}

	private final T model;

	public ModelUpdatedEvent(T model) {
		this.model = model;
	}

	@Override
	protected void dispatch(ModelUpdatedListener<T> handler) {
		handler.onModelUpdated(this);
	}

	@Override
	public Type<ModelUpdatedListener<T>> getAssociatedType() {
		return getType();
	}

	public T getModel() {
		return model;
	}
}

package com.horaz.client.model.events;

import com.google.gwt.event.shared.GwtEvent;
import com.horaz.client.model.BaseModel;

/**
 * fired when a new model was added to the datastore
 * @param <T> Used model class
 */
public class ModelAddedEvent<T extends BaseModel> extends GwtEvent<ModelAddedListener<T>> {
	@SuppressWarnings("rawtypes")
	private static Type TYPE;
	@SuppressWarnings("unchecked")
	static public <T extends BaseModel> Type<ModelAddedListener<T>> getType() {
		if (TYPE == null) {
			TYPE = new Type<ModelAddedListener<T>>();
		}
		return TYPE;
	}

	private final T model;

	public ModelAddedEvent(T model) {
		this.model = model;
	}

	@Override
	protected void dispatch(ModelAddedListener<T> handler) {
		handler.onModelAdded(this);
	}

	@Override
	public Type<ModelAddedListener<T>> getAssociatedType() {
		return getType();
	}

	public T getModel() {
		return model;
	}
}

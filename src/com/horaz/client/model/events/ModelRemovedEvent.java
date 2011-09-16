package com.horaz.client.model.events;

import com.google.gwt.event.shared.GwtEvent;
import com.horaz.client.model.BaseModel;

/**
 * gets fired when a model was removed from the datastore
 * @param <T> model class
 */
public class ModelRemovedEvent<T extends BaseModel> extends GwtEvent<ModelRemovedListener<T>> {
	@SuppressWarnings("rawtypes")
	private static Type TYPE;
	@SuppressWarnings("unchecked")
	static public <T extends BaseModel> Type<ModelRemovedListener<T>> getType() {
		if (TYPE == null) {
			TYPE = new Type<ModelRemovedListener<T>>();
		}
		return TYPE;
	}

	private final T model;

	public ModelRemovedEvent(T model) {
		this.model = model;
	}

	@Override
	protected void dispatch(ModelRemovedListener<T> handler) {
		handler.onModelRemoved(this);
	}

	@Override
	public Type<ModelRemovedListener<T>> getAssociatedType() {
		return getType();
	}

	public T getModel() {
		return model;
	}
}

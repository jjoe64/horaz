package com.horaz.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.horaz.client.model.events.ModelAddedEvent;
import com.horaz.client.model.events.ModelRemovedEvent;

/**
 * simple datastore
 * It stores the data only for the time of life.
 * The data will not be saved. When the site/app reloads, the datastore is again empty.
 *
 * @param <T> used model class
 * @horaz.events modelAdded, modelRemoved, modelUpdated
 * @see http://www.dev-horaz.com/dev-guide/datastore
 */
public class SimpleDataStore<T extends BaseModel> extends DataStore<T> {
	private final Map<Integer, T> dataMap; // for performance
	private final List<T> data;

	public SimpleDataStore() {
		data = new ArrayList<T>();
		dataMap = new HashMap<Integer, T>();
	}

	/**
	 * adds a new model to the store.
	 * {@link ModelAddedEvent} is fired here.
	 * @param newModel
	 */
	@Override
	public void add(T newModel) {
		data.add(newModel);
		dataMap.put(newModel.getModelId(), newModel);
		super.add(newModel);
	}

	/**
	 * get a model by the unique model id
	 * @param id model id
	 * @return model or null
	 */
	@Override
	public T get(int id) {
		return dataMap.get(id);
	}

	/**
	 * @return the current models
	 */
	@Override
	public List<T> getModels() {
		return data;
	}

	/**
	 * removes the model from the datastore. This fires a {@link ModelRemovedEvent} Event.
	 * Connected ListViews will automatically removes the list item.
	 * @param model
	 */
	@Override
	public void remove(T model) {
		super.remove(model);
		data.remove(model);
		dataMap.remove(model.getModelId());
	}
}

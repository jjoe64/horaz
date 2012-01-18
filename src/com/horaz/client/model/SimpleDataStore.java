/**
 * This file is part of Horaz.
 *
 * Horaz is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Horaz is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Horaz.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright Joe's App Factory UG (haftungsbeschränkt)
 */

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
public class SimpleDataStore<T extends BaseModel> extends DataStore<T> implements SynchronousDataStore<T> {
	private final Map<Long, T> dataMap; // for performance
	private final List<T> data;
	private long lastModelId;

	public SimpleDataStore() {
		data = new ArrayList<T>();
		dataMap = new HashMap<Long, T>();
	}

	/**
	 * adds a new model to the store.
	 * {@link ModelAddedEvent} is fired here.
	 * @param newModel
	 */
	@Override
	public void add(T newModel) {
		newModel.setField("modelId", ++lastModelId);

		data.add(newModel);
		dataMap.put(newModel.getModelId(), newModel);
		added(newModel);
	}

	protected List<T> filter(List<T> input) {
		if (getFilter() != null) {
			List<T> output = new ArrayList<T>();
			for (T m : input) {
				if (getFilter().match(m)) {
					output.add(m);
				}
			}
			return output;
		}
		return input;
	}

	/**
	 * find the first model that matches the given filter
	 *
	 * @param filter
	 * @return first match or null
	 */
	@Override
	public T find(Filter filter) {
		List<T> r = find(filter, true);
		if (r.isEmpty()) return null;
		return r.get(0);
	}

	private List<T> find(Filter filter, boolean first) {
		List<T> r = new ArrayList<T>();
		for (T m : getModels()) {
			if (filter.match(m)) {
				r.add(m);
				if (first) {
					return r;
				}
			}
		}
		return r;
	}

	/**
	 * find first model that match a given field with the given value
	 *
	 * @param field
	 * @param value
	 * @return model or null
	 */
	@Override
	public T find(String field, Object value) {
		return find(new Filter().whereEquals(field, value));
	}

	/**
	 * find all model that match the filter
	 *
	 * @param filter
	 * @return list with all models (or empty list)
	 */
	@Override
	public List<T> findAll(Filter filter) {
		return find(filter, false);
	}

	/**
	 * find all model that match the given field with the given value
	 *
	 * @param field
	 * @param value
	 * @return list with all models (or empty list)
	 */
	@Override
	public List<T> findAll(String field, Object value) {
		return find(new Filter().whereEquals(field, value), false);
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
		return group(filter(data));
	}

	/**
	 * removes the model from the datastore. This fires a {@link ModelRemovedEvent} Event.
	 * Connected ListViews will automatically removes the list item.
	 * @param model
	 */
	@Override
	public void remove(T model) {
		removed(model);
		data.remove(model);
		dataMap.remove(model.getModelId());
	}

	@Override
	public void update(T saveModel) {
		updated(saveModel);
	}
}

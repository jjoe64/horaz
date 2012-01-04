package com.horaz.client.model;

import java.util.List;

public interface SynchronousDataStore<T extends BaseModel> {
	public void add(T newModel);
	/**
	 * find the first model that matches the given filter
	 *
	 * @param filter
	 * @return first match or null
	 */
	public T find(Filter filter);

	/**
	 * find first model that match a given field with the given value
	 *
	 * @param field
	 * @param value
	 * @return model or null
	 */
	public T find(String field, Object value);

	/**
	 * find all model that match the filter
	 *
	 * @param filter
	 * @return list with all models (or empty list)
	 */
	public List<T> findAll(Filter filter);

	/**
	 * find all model that match the given field with the given value
	 *
	 * @param field
	 * @param value
	 * @return list with all models (or empty list)
	 */
	public List<T> findAll(String field, Object value);

	/**
	 * get a model by the unique model id
	 *
	 * @param id model id
	 * @return model or null
	 */
	public T get(int id);

	/**
	 * @return the current models
	 */
	public List<T> getModels();

	public void remove(T model);

	public void update(T saveModel);

}

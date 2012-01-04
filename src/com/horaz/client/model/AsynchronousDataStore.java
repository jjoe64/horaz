package com.horaz.client.model;

import java.util.List;

import com.google.code.gwt.database.client.StatementCallback;
import com.google.gwt.core.client.JavaScriptObject;

public interface AsynchronousDataStore<T extends BaseModel> {
	public interface FindCallback<T> {
		void findCallback(List<T> result);
	}

	public void add(T newModel);

	/**
	 * find the first model that matches the given filter
	 *
	 * @param filter
	 * @return first match or null
	 */
	public void find(Filter filter, FindCallback<T> callback);

	/**
	 * find first model that match a given field with the given value
	 *
	 * @param field
	 * @param value
	 * @return model or null
	 */
	public void find(String field, Object value, FindCallback<T> callback);

	/**
	 * find all model that match the filter
	 *
	 * @param filter
	 * @return list with all models (or empty list)
	 */
	public void findAll(Filter filter, FindCallback<T> callback);

	/**
	 * find all model that match the given field with the given value
	 *
	 * @param field
	 * @param value
	 * @return list with all models (or empty list)
	 */
	public void findAll(String field, Object value, FindCallback<T> callback);

	/**
	 * get a model by the unique model id
	 *
	 * @param id model id
	 * @return model or null
	 */
	public void get(int id, StatementCallback<JavaScriptObject> callback);


	public void remove(T model);

	public void update(T saveModel);
}

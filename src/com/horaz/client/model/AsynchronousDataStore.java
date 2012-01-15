package com.horaz.client.model;

import java.util.Iterator;

import com.google.code.gwt.database.client.SQLResultSet;
import com.google.gwt.core.client.JavaScriptObject;

public interface AsynchronousDataStore<T extends BaseModel> {
	public interface FindCallback<T extends BaseModel> {
		public void onSuccess(ModelsCollection<T> results);
	}

	public class ModelsCollection<K extends BaseModel> implements Iterable<K> {
		private final SQLResultSet<JavaScriptObject> data;
		private final AsynchronousDataStore<K> parent;
		private final int size;

		public ModelsCollection(AsynchronousDataStore<K> parent, SQLResultSet<JavaScriptObject> data) {
			this.parent = parent;
			this.data = data;
			size = data.getRows().getLength(); // TODO performance: work with sql iterator
		}

		@Override
		public Iterator<K> iterator() {
			return new Iterator<K>() {
				private int cursor = 0;

				@Override
				public boolean hasNext() {
					return cursor < size;
				}

				@Override
				public K next() {
					return parent.reflectJavaScriptObject(data.getRows().getItem(cursor++));
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
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
	public void get(long id, FindCallback<T> callback);

	public T reflectJavaScriptObject(JavaScriptObject jsObj);

	public void remove(T model);

	public void update(T saveModel);
}

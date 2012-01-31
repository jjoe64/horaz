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
		private final AsynchronousDataStore<BaseModel> joinedDataStore;
		private final int size;

		public ModelsCollection(AsynchronousDataStore<K> parent, AsynchronousDataStore<BaseModel> joinedDataStore, SQLResultSet<JavaScriptObject> data) {
			this.parent = parent;
			this.data = data;
			this.joinedDataStore = joinedDataStore;
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
					if (!hasNext()) return null;
					JavaScriptObject jsObj = data.getRows().getItem(cursor++);
					K mdl = parent.reflectJavaScriptObject(jsObj);
					if (joinedDataStore != null) {
						mdl.setJoinedModel(joinedDataStore.reflectJavaScriptObject(jsObj));
					}
					// store children count
					parent.storeChildrenCount(mdl, jsObj);
					return mdl;
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

	public void getChildren(T mdl, FindCallback<T> callback);

	public T reflectJavaScriptObject(JavaScriptObject jsObj);

	public void remove(T model);

	public void storeChildrenCount(T mdl, JavaScriptObject jsObj);

	public void update(T saveModel);
}

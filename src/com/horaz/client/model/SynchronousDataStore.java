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

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

	/**
	 * @return updated model
	 */
	public T getModel() {
		return model;
	}
}

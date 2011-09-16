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

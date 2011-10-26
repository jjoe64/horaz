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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.horaz.client.model.events.FilterUpdatedEvent;
import com.horaz.client.model.events.FilterUpdatedListener;
import com.horaz.client.model.events.ModelAddedEvent;
import com.horaz.client.model.events.ModelAddedListener;
import com.horaz.client.model.events.ModelRemovedEvent;
import com.horaz.client.model.events.ModelRemovedListener;
import com.horaz.client.model.events.ModelUpdatedEvent;
import com.horaz.client.model.events.ModelUpdatedListener;

/**
 * abstract datastore
 * use any subclass
 *
 * @param <T> used model class
 * @horaz.events modelAdded, modelRemoved, modelUpdated
 * @see http://www.dev-horaz.com/dev-guide/datastore
 */
public abstract class DataStore<T extends BaseModel> implements HasHandlers {
	private final HandlerManager handlerManager = new HandlerManager(this);
	private Filter filter;
	private String groupBy;

	public DataStore() {}

	/**
	 * adds a new model to the store.
	 * {@link ModelAddedEvent} is fired here.
	 * @param newModel
	 */
	public void add(T newModel) {
		fireEvent(new ModelAddedEvent<T>(newModel));
	}

	public HandlerRegistration addFilterUpdatedListener(FilterUpdatedListener handler) {
		Type<FilterUpdatedListener> type = FilterUpdatedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	/**
	 * register a handler for {@link ModelAddedEvent} Event
	 * @param handler
	 * @return gwt handler registration
	 */
	public HandlerRegistration addModelAddedListener(ModelAddedListener<T> handler) {
		Type<ModelAddedListener<T>> type = ModelAddedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	/**
	 * register a handler for {@link ModelRemovedEvent} Event
	 * @param handler
	 * @return gwt handler registration
	 */
	public HandlerRegistration addModelRemovedListener(ModelRemovedListener<T> handler) {
		Type<ModelRemovedListener<T>> type = ModelRemovedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	/**
	 * register a handler for {@link ModelUpdatedEvent} Event
	 * @param handler
	 * @return gwt handler registration
	 */
	public HandlerRegistration addModelUpdatedListener(ModelUpdatedListener<T> handler) {
		Type<ModelUpdatedListener<T>> type = ModelUpdatedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	public T find(Filter filter) {
		for (T m : getModels()) {
			if (filter.match(m)) return m;
		}
		return null;
	}

	public T find(String field, Object value) {
		return find(new Filter().whereEquals(field, value));
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	/**
	 * get a model by the unique model id
	 * @param id model id
	 * @return model or null
	 */
	public abstract T get(int id);

	public Filter getFilter() {
		return filter;
	}

	public String getGroupBy() {
		return groupBy;
	}

	/**
	 * @return the current models
	 */
	public abstract List<T> getModels();

	protected List<T> group(List<T> mdls) {
		Set<Object> already = new HashSet<Object>();
		if (groupBy != null && groupBy.length()>0) {
			List<T> r = new ArrayList<T>();
			for (T mdl : mdls) {
				if (!already.contains(mdl.getField(groupBy))) {
					r.add(mdl);
					already.add(mdl.getField(groupBy));
				}
			}
			return r;
		} else {
			return mdls;
		}
	}

	/**
	 * removes the model from the datastore. This fires a {@link ModelRemovedEvent} Event.
	 * Connected ListViews will automatically removes the list item.
	 * @param model
	 */
	public void remove(T model) {
		fireEvent(new ModelRemovedEvent<T>(model));
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
		fireEvent(new FilterUpdatedEvent());
	}

	public void setGroupBy(String field) {
		groupBy = field;
	}

	/**
	 * fires {@link ModelUpdatedEvent} event, so that connected listviews updates the list item
	 * @param saveModel
	 */
	public void update(T saveModel) {
		fireEvent(new ModelUpdatedEvent<T>(saveModel));
	}
}

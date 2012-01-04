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

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
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
 * @horaz.events modelAdded, modelRemoved, modelUpdated, filterUpdated
 * @see http://www.dev-horaz.com/dev-guide/datastore
 */
public abstract class DataStore<T extends BaseModel> implements HasHandlers {
	protected final HandlerManager handlerManager = new HandlerManager(this);
	private Filter filter;
	private String groupBy;

	public DataStore() {}

	/**
	 * adds a new model to the store.
	 * {@link ModelAddedEvent} is fired here.
	 *
	 * @param newModel
	 */
	protected void added(T newModel) {
		fireEvent(new ModelAddedEvent<T>(newModel));
	}

	/**
	 * register a handler for {@link FilterUpdatedEvent} Event
	 *
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addFilterUpdatedListener(FilterUpdatedListener handler) {
		Type<FilterUpdatedListener> type = FilterUpdatedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	/**
	 * register a handler for {@link ModelAddedEvent} Event
	 *
	 * @param handler
	 * @return gwt handler registration
	 */
	public HandlerRegistration addModelAddedListener(ModelAddedListener<T> handler) {
		Type<ModelAddedListener<T>> type = ModelAddedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	/**
	 * register a handler for {@link ModelRemovedEvent} Event
	 *
	 * @param handler
	 * @return gwt handler registration
	 */
	public HandlerRegistration addModelRemovedListener(ModelRemovedListener<T> handler) {
		Type<ModelRemovedListener<T>> type = ModelRemovedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	/**
	 * register a handler for {@link ModelUpdatedEvent} Event
	 *
	 * @param handler
	 * @return gwt handler registration
	 */
	public HandlerRegistration addModelUpdatedListener(ModelUpdatedListener<T> handler) {
		Type<ModelUpdatedListener<T>> type = ModelUpdatedEvent.getType();
		return handlerManager.addHandler(type, handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	/**
	 * @return the current filter or null
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * @return current group by clause or null
	 */
	public String getGroupBy() {
		return groupBy;
	}

	/**
	 * internal grouping feature. groups the incoming models.
	 * for every group there will be the first model as parent and
	 * the others as children.
	 *
	 * @param mdls
	 * @return grouped models
	 */
	protected List<T> group(List<T> mdls) {
		Map<Object, T> already = new HashMap<Object, T>();
		if (groupBy != null && groupBy.length()>0) {
			for (T mdl : mdls) {
				if (!already.containsKey(mdl.getField(groupBy))) {
					already.put(mdl.getField(groupBy), mdl);
				} else {
					// add as child
					already.get(mdl.getField(groupBy)).addChild(mdl);
				}
			}
			return new ArrayList<T>(already.values());
		} else {
			return mdls;
		}
	}

	/**
	 * removes the model from the datastore. This fires a {@link ModelRemovedEvent} Event.
	 * Connected ListViews will automatically removes the list item.
	 *
	 * @param model
	 */
	protected void removed(T model) {
		fireEvent(new ModelRemovedEvent<T>(model));
	}

	/**
	 * set (or unset if null) a filter for this datastore. all components that uses this datastore
	 * will display only items that matches this filter.
	 * this fires the {@link FilterUpdatedEvent}
	 *
	 * @param filter
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
		fireEvent(new FilterUpdatedEvent());
	}

	/**
	 * Groups the models by a given field.
	 * For every group there will be the first model as parent and
	 * the others as children.
	 *
	 * @param field
	 */
	public void setGroupBy(String field) {
		groupBy = field;
	}

	/**
	 * fires {@link ModelUpdatedEvent} event, so that connected listviews updates the list item
	 * @param saveModel
	 */
	protected void updated(T saveModel) {
		fireEvent(new ModelUpdatedEvent<T>(saveModel));
	}
}

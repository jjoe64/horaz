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

/**
 * fired when filter was updated for a datastore
 */
public class FilterUpdatedEvent extends GwtEvent<FilterUpdatedListener> {
	@SuppressWarnings("rawtypes")
	private static Type TYPE;
	@SuppressWarnings("unchecked")
	static public Type<FilterUpdatedListener> getType() {
		if (TYPE == null) {
			TYPE = new Type<FilterUpdatedListener>();
		}
		return TYPE;
	}

	@Override
	protected void dispatch(FilterUpdatedListener handler) {
		handler.onFilterUpdated(this);
	}

	@Override
	public Type<FilterUpdatedListener> getAssociatedType() {
		return getType();
	}
}

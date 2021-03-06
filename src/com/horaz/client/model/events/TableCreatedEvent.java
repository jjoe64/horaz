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
import com.horaz.client.model.SQLiteDataStore;

/**
 * event will be fired when {@link SQLiteDataStore} created a new table
 * within the init proccess.
 */
public class TableCreatedEvent extends GwtEvent<TableCreatedListener> {
	@SuppressWarnings("rawtypes")
	private static Type TYPE;
	@SuppressWarnings("unchecked")
	static public Type<TableCreatedListener> getType() {
		if (TYPE == null) {
			TYPE = new Type<TableCreatedListener>();
		}
		return TYPE;
	}

	@Override
	protected void dispatch(TableCreatedListener handler) {
		handler.onTableCreated(this);
	}

	@Override
	public Type<TableCreatedListener> getAssociatedType() {
		return getType();
	}
}
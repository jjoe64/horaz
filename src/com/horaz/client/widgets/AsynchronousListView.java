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

package com.horaz.client.widgets;

import java.util.Iterator;

import com.google.gwt.dom.client.UListElement;
import com.horaz.client.model.AsynchronousDataStore;
import com.horaz.client.model.AsynchronousDataStore.FindCallback;
import com.horaz.client.model.AsynchronousDataStore.ModelsCollection;
import com.horaz.client.model.BaseModel;
import com.horaz.client.model.DataStore;
import com.horaz.client.model.Filter;
import com.horaz.client.model.SQLiteDataStore;
import com.horaz.client.model.events.ReadyEvent;
import com.horaz.client.model.events.ReadyListener;

public class AsynchronousListView<T extends BaseModel> extends ListView<T> {
	/**
	 * finds listview element for a given UListElement (html ul element)
	 * @param <E> model class
	 * @param elm
	 * @return listview
	 */
	@SuppressWarnings("unchecked")
	static public <E extends BaseModel> AsynchronousListView<E> byElement(UListElement elm) {
		if (allWidgetInstances.get(elm) != null) {
			return (AsynchronousListView<E>) allWidgetInstances.get(elm);
		}
		return new AsynchronousListView<E>(elm);
	}

	/**
	 * finds listview element for given id
	 * @param id
	 */
	static public <E extends BaseModel> AsynchronousListView<E> byId(String id) {
		return byElement((UListElement) getElementById(id));
	}

	protected AsynchronousListView(UListElement ulElm) {
		super(ulElm);
	}

	@Override
	protected void createAllItems() {
		@SuppressWarnings("unchecked")
		AsynchronousDataStore<T> asyncDS = (AsynchronousDataStore<T>) getDataStore();
		asyncDS.findAll(new Filter(), new FindCallback<T>() {
			@Override
			public void onSuccess(ModelsCollection<T> results) {
				Iterator<T> it = results.iterator();
				while (it.hasNext()) {
					createNewItem(it.next());
				}
				_refresh(getElement());
			}
		});
	}

	@Override
	public void setDataStore(DataStore<T> dataStore) {
		setDataStore(dataStore, true); // autoload default true
	}

	public void setDataStore(DataStore<T> dataStore, boolean autoLoad) {
		super.setDataStore(dataStore);
		SQLiteDataStore<T> sqlDS = (SQLiteDataStore<T>) dataStore;
		if (autoLoad) {
			sqlDS.addReadyListener(new ReadyListener() {
				@Override
				public void onReady(ReadyEvent event) {
					if (getDataStore() != null) {
						// create a list item for each model
						createAllItems();
					}
				}
			});

			if (sqlDS.isReady()) {
				// create a list item for each model
				createAllItems();
			}
		}
	}
}

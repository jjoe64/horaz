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

import com.google.gwt.dom.client.UListElement;
import com.horaz.client.model.BaseModel;
import com.horaz.client.model.DataStore;
import com.horaz.client.model.SimpleDataStore;

public class SynchronousListView<T extends BaseModel> extends ListView<T> {
	/**
	 * finds listview element for a given UListElement (html ul element)
	 * @param <E> model class
	 * @param elm
	 * @return listview
	 */
	@SuppressWarnings("unchecked")
	static public <E extends BaseModel> SynchronousListView<E> byElement(UListElement elm) {
		if (allWidgetInstances.get(elm) != null) {
			return (SynchronousListView<E>) allWidgetInstances.get(elm);
		}
		return new SynchronousListView<E>(elm);
	}

	/**
	 * finds listview element for given id
	 * @param id
	 */
	static public <E extends BaseModel> SynchronousListView<E> byId(String id) {
		return byElement((UListElement) getElementById(id));
	}

	protected SynchronousListView(UListElement ulElm) {
		super(ulElm);
	}

	@Override
	protected void createAllItems() {
		for (T model : getDataStore().getModels()) {
			createNewItem(model);
		}
	}

	@Override
	public SimpleDataStore<T> getDataStore() {
		return (SimpleDataStore<T>) super.getDataStore();
	}

	@Override
	public void setDataStore(DataStore<T> dataStore) {
		super.setDataStore(dataStore);
		// create a list item for each model
		createAllItems();
	}
}

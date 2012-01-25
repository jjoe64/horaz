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
		super.setDataStore(dataStore);
		SQLiteDataStore<T> sqlDS = (SQLiteDataStore<T>) dataStore;
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

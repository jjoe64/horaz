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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.UListElement;
import com.horaz.client.model.BaseModel;
import com.horaz.client.model.DataStore;
import com.horaz.client.model.SimpleDataStore;
import com.horaz.client.model.events.FilterUpdatedEvent;
import com.horaz.client.model.events.FilterUpdatedListener;
import com.horaz.client.model.events.ModelAddedEvent;
import com.horaz.client.model.events.ModelAddedListener;
import com.horaz.client.model.events.ModelRemovedEvent;
import com.horaz.client.model.events.ModelRemovedListener;
import com.horaz.client.model.events.ModelUpdatedEvent;
import com.horaz.client.model.events.ModelUpdatedListener;
import com.horaz.client.util.RegExp;
import com.horaz.client.util.RegExp.Result;
import com.horaz.client.widgets.events.EventFactory;
import com.horaz.client.widgets.events.ItemApplyListener;
import com.horaz.client.widgets.events.ItemClickListener;

/**
 * ListView
 * Lists are used for data display, navigation, result lists, and data entry. It is possible to connect the ListView with a DataStore, so the data can be dynamically change (add, remove, update).
 * If you use a DataStore, the ListView has to have to the attribute <b>data-template="TEMPLATE_ID"</b>.
 * The template must be a container with placeholders in the syntax {#FIELD_NAME#}.
 * When a list item will be created, the innerHTML of the placeholder container will be copied and
 * the placeholders will be replaced with the values of the model for the corresponding field.
 *
 * @horaz.htmltag &lt;ul data-role="listview" data-template="TEMPLATE_ID"&gt;
 * @horaz.events itemApply, itemClick
 * @see https://www.horaz-lang.com/dev-guide/listview
 */
public class ListView<T extends BaseModel> extends BaseWidget<UListElement> {
	/**
	 * finds listview element for a given UListElement (html ul element)
	 * @param <E> model class
	 * @param elm
	 * @return listview
	 */
	@SuppressWarnings("unchecked")
	static public <E extends BaseModel> ListView<E> byElement(UListElement elm) {
		if (allWidgetInstances.get(elm) != null) {
			return (ListView<E>) allWidgetInstances.get(elm);
		}
		return new ListView<E>(elm);
	}

	/**
	 * finds listview element for given id
	 * @param id
	 */
	static public <E extends BaseModel> ListView<E> byId(String id) {
		return byElement((UListElement) getElementById(id));
	}

	private SimpleDataStore<T> dataStore;

	protected ListView(UListElement ulElm) {
		super(ulElm);
	}

	native private void _refresh(Element elm) /*-{
		$wnd.jQuery(elm).listview('refresh');
	}-*/;

	/**
	 * register a item apply listener
	 * this is a hook that will be called when a &lt;li&gt; element was created.
	 * @param itemApplyListener
	 */
	public void addItemApplyListener(ItemApplyListener<T> itemApplyListener) {
		EventFactory.delegateEventHandler(getElement(), "li", "itemApply", itemApplyListener);
	}

	/**
	 * register a item click listener
	 * @param itemClickListener
	 */
	public void addItemClickListener(ItemClickListener<T> itemClickListener) {
		EventFactory.delegateEventHandler(getElement(), "li a", "click", itemClickListener);
	}

	/**
	 * creates a new list item from a model
	 * notice: after that {@link #refresh()} has to be called.
	 * @param model
	 */
	private void createNewItem(T model) {
		String inner = generateItemInnerHTML(model);
		LIElement newItem = Document.get().createLIElement();
		newItem.setInnerHTML(inner);
		newItem.setAttribute("data-modelid", String.valueOf(model.getModelId()));
		getElement().appendChild(newItem);
	}

	private String generateItemInnerHTML(T model) {
		// create a new list item from template
		String templateid = getElement().getAttribute("data-template");
		if (templateid == null || templateid.isEmpty()) {
			throw new IllegalStateException("attribute data-template has to be set.");
		}
		Element template = getElementById(templateid);
		String inner = template.getInnerHTML();
		// replace attributes
		RegExp reg = RegExp.create("({#[^#]+#})", "g");
		Result res = reg.match(inner);
		for (String match : res.iterator()) {
			Object value = model.getField(match.substring(2, match.length()-2));
			String s = value==null?"":value.toString();
			inner = inner.replace(match, s);
		}
		return inner;
	}

	/**
	 * @return datastore
	 */
	public DataStore<T> getDataStore() {
		return dataStore;
	}

	/**
	 * get the model behind a LI-Element. The li element must have to attribute data-modelid.
	 * @param el
	 * @return model or null
	 * @throws IllegalArgumentException if data-modelid is not set
	 */
	public T getModel(LIElement el) {
		if (el.getAttribute("data-modelid") == null) {
			throw new IllegalStateException("LI Element needs the attribute data-modelid");
		}
		int id = Integer.valueOf(el.getAttribute("data-modelid"));
		return dataStore.get(id);
	}

	// TODO place this is basewidget, better implementation
	private boolean isInitFired() {
		return getElement().getClassName().contains("ui-listview");
	}

	/**
	 * renders the list items
	 */
	protected void refresh() {
		if (isInitFired()) {
			_refresh(getElement());
		}
	}

	/**
	 * remove li-element
	 * @param model
	 */
	private void removeItem(T model) {
		// TODO for performance with jquery
		long id = model.getModelId();
		for (int i=0; i<getElement().getChildCount(); i++) {
			Node node = getElement().getChild(i);
			if (Element.is(node)) {
				String attr = Element.as(node).getAttribute("data-modelid");
				if (attr != null && !attr.isEmpty() && Integer.valueOf(attr) == id) {
					getElement().removeChild(node);
					break;
				}
			}
		}
	}

	/**
	 * connect a datastore to the listview.
	 * The listview will automatically add/edit/remove the listitems, when the datastore changes
	 * @param dataStore
	 */
	public void setDataStore(SimpleDataStore<T> dataStore) {
		this.dataStore = dataStore;

		// create a list item for each model
		for (T model : dataStore.getModels()) {
			createNewItem(model);
		}
		refresh();

		// TODO unregister old event handlers
		// TODO remove dataStore (null)

		// register event handler
		this.dataStore.addModelAddedListener(new ModelAddedListener<T>() {
			@Override
			public void onModelAdded(ModelAddedEvent<T> event) {
				createNewItem(event.getModel());
				refresh();
			}
		});
		this.dataStore.addModelRemovedListener(new ModelRemovedListener<T>() {
			@Override
			public void onModelRemoved(ModelRemovedEvent<T> event) {
				removeItem(event.getModel());
				refresh();
			}
		});
		this.dataStore.addModelUpdatedListener(new ModelUpdatedListener<T>() {
			@Override
			public void onModelUpdated(ModelUpdatedEvent<T> event) {
				updateItem(event.getModel());
				refresh();
			}
		});
		this.dataStore.addFilterUpdatedListener(new FilterUpdatedListener() {
			@Override
			public void onFilterUpdated(FilterUpdatedEvent event) {
				//TODO performance, filter existing elements
				while (getElement().getChildCount()>0) {
					getElement().removeChild(getElement().getChild(0));
				}
				// create a list item for each model
				for (T model : ListView.this.dataStore.getModels()) {
					createNewItem(model);
				}

				refresh();
			}
		});
	}

	private void updateItem(T model) {
		// find li-element
		LIElement li = null;
		long id = model.getModelId();
		for (int i=0; i<getElement().getChildCount(); i++) {
			Node node = getElement().getChild(i);
			if (Element.is(node)) {
				String attr = Element.as(node).getAttribute("data-modelid");
				if (attr != null && !attr.isEmpty() && Integer.valueOf(attr) == id) {
					li = (LIElement) Element.as(node);
					break;
				}
			}
		}
		if (li == null) throw new IllegalStateException("could not find list item for model #"+id);
		li.setClassName(""); // reset class names
		String inner = generateItemInnerHTML(model);
		li.setInnerHTML(inner);
	}
}

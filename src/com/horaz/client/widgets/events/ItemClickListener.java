package com.horaz.client.widgets.events;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.Event;
import com.horaz.client.model.BaseModel;
import com.horaz.client.widgets.ListView;

/**
 * when a item was clicked in the listview, this event will be fired.
 * It refers to the corresponding model.
 * @param <T> model class
 */
public abstract class ItemClickListener<T extends BaseModel> implements F {
	@Override
	public void f(Event event) {
		// extract selected list item
		if (Element.is(event.getEventTarget())) {
			Element elm = Element.as(event.getEventTarget());
			Element ul = null;
			Element li = null;
			// find <ul> and <li>
			while (elm.hasParentElement()) {
				if (elm.getTagName().equals("LI")) {
					li = elm;
				} else if (elm.getTagName().equals("UL")) {
					ul = elm;
					break;
				}
				elm = elm.getParentElement();
			}

			if (ul != null & li != null) {
				ListView<T> lv = ListView.byElement((UListElement) ul);
				onItemClick(event, lv.getModel((LIElement) li));
			}
		}
	}

	/**
	 * is called when the event occurs.
	 * @param event
	 */
	public abstract void onItemClick(Event event, T item);
}

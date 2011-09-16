package com.horaz.client.widgets.events;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.Event;
import com.horaz.client.model.BaseModel;
import com.horaz.client.widgets.ListView;

/**
 * If you have to do some special formatting or layouting on some explicit models,
 * there is hook for manipulating the new li-element that was created from the template.
 * This catches the listview applyItem hook. The event will
 * be fired if any li-element was created from template.
 * @param <T> model class
 */
public abstract class ItemApplyListener<T extends BaseModel> implements F {
	@Override
	public void f(Event event) {
		// extract selected list item
		if (Element.is(event.getEventTarget())) {
			Element elm = Element.as(event.getEventTarget());
			Element ul = null;
			Element li = null;
			// find <li>
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
				onItemApply(event, (LIElement) li, lv.getModel((LIElement) li));
			}
		}
	}

	public abstract void onItemApply(Event event, LIElement liElement, T model);
}

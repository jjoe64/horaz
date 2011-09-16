package com.horaz.client.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

public abstract class BaseWidget<T extends Element> {
	/**
	 * maps all created widgets, so that there are no duplications.
	 */
	static protected Map<Element, BaseWidget<?>> allWidgetInstances = new HashMap<Element, BaseWidget<?>>();

	/**
	 * wrapper for document.getElementById
	 * @param id unique html id
	 * @return elm
	 * @throws IllegalStateException is element was not found
	 */
	static protected Element getElementById(String id) {
		Element elm = Document.get().getElementById(id);
		if (elm == null) {
			throw new IllegalStateException("Element not found #"+id);
		}
		return elm;
	}

	private T elm;

	protected BaseWidget(T elm) {
		if (elm != null) {
			allWidgetInstances.put(elm, this);
		}
		this.elm = elm;
	}

	/**
	 * @return the gwt dom element
	 */
	public T getElement() {
		return elm;
	}

	protected void setElement(T elm) {
		this.elm = elm;
	}
}

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

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;

public abstract class BaseWidget<T extends Element> {
	public enum Theme {
		A, B, C, D, E
	}

	/**
	 * maps all created widgets, so that there are no duplications.
	 * WARNING: USE ONLY IF YOU KNOW WHAT YOU ARE DOING
	 */
	public static Map<Element, BaseWidget<?>> allWidgetInstances = new HashMap<Element, BaseWidget<?>>();

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
		setElement(elm);
	}

	/**
	 * @return the gwt dom element
	 */
	public T getElement() {
		return elm;
	}

	public void setDimensions(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	/**
	 * use only if you know what you are doing!
	 * @param elm
	 */
	protected void setElement(T elm) {
		if (elm != null) {
			allWidgetInstances.put(elm, this);
		}
		this.elm = elm;
	}

	public void setHeight(int height) {
		getElement().getStyle().setHeight(height, Unit.PX);
	}

	public void setWidth(int width) {
		getElement().getStyle().setWidth(width, Unit.PX);
	}
}

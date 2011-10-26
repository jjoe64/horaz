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

import com.google.gwt.dom.client.Element;
import com.horaz.client.util.PageManager;
import com.horaz.client.widgets.events.EventFactory;
import com.horaz.client.widgets.events.PageCreateListener;

/**
 * Coming from jquery mobile, in Horaz you define your
 * different UIs in so called Pages (similar to Activity in Android).
 * Dialogs are simply subclasses of Page, and so you use them in the same way.
 *
 * @horaz.htmltag &lt;div data-role="page"&gt; or &lt;div data-role="dialog"&gt;
 */
public abstract class Page extends BaseWidget<Element> {
	static public Page byId(String id) {
		return new Page(getElementById(id)) {
			@Override
			public void onCreate() {}
		};
	}

	protected Page(Element elm) {
		super(elm);
		EventFactory.bindEventHandler(getElement(), "pagecreate", new PageCreateListener() {
			@Override
			public void onPageCreate() {
				onCreate();
			}
		});
	}

	public abstract void onCreate();

	/**
	 * change current page, show this
	 */
	public void show() {
		PageManager.changePage(this);
	}
}

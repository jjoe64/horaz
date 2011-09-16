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

package com.horaz.client.util;

import com.google.gwt.dom.client.Element;
import com.horaz.client.widgets.Page;

/**
 * collection of methods for controlling pages/windows
 */
public class PageManager {
	native static private void _changePage(Element elm) /*-{
		$wnd.jQuery.mobile.changePage($wnd.jQuery(elm))
	}-*/;

	/**
	 * change current page
	 * @param showPage
	 */
	static public void changePage(Page showPage) {
		_changePage(showPage.getElement());
	}

	private PageManager() {}
}

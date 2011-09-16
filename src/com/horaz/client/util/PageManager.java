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

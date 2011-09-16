package com.horaz.client.widgets;

import com.google.gwt.dom.client.Element;
import com.horaz.client.util.PageManager;

/**
 * Coming from jquery mobile, in Horaz you define your
 * different UIs in so called Pages (similar to Activity in Android).
 * Dialogs are simply subclasses of Page, and so you use them in the same way.
 *
 * @horaz.htmltag &lt;div data-role="page"&gt; or &lt;div data-role="dialog"&gt;
 */
public class Page extends BaseWidget<Element> {
	static public Page byId(String id) {
		return new Page(getElementById(id));
	}

	protected Page(Element elm) {
		super(elm);
	}

	/**
	 * change current page, show this
	 */
	public void show() {
		PageManager.changePage(this);
	}

}

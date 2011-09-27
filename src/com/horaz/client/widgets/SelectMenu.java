package com.horaz.client.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SelectElement;

/**
 * SelectMenu
 * @horaz.htmltag &lt;select&gt;
 */
public class SelectMenu extends BaseWidget<SelectElement> {
	/**
	 * finds selectmenu element for given id
	 * the html element has to be a select element
	 * @param id
	 */
	static public SelectMenu byId(String id) {
		Element elm = getElementById(id);
		if (allWidgetInstances.get(elm) != null) {
			return (SelectMenu) allWidgetInstances.get(elm);
		}
		return new SelectMenu((SelectElement) elm);
	}

	protected SelectMenu(SelectElement elm) {
		super(elm);
	}
	
	/**
	 * opens the selectmenu. This is only working if the property data-native-menu=false is set.
	 * @throws IllegalStateException if data-native-menu is not false
	 */
	public void open() {
		if (getElement().getAttribute("data-native-menu") == null || Boolean.parseBoolean(getElement().getAttribute("data-native-menu"))) {
			throw new IllegalStateException("The attribute data-native-menu=false is missing.");
		}
		_open(getElement());
	}
	
	private native static void _open(Element elm)/*-{
		$wnd.jQuery(elm).selectmenu('open');
	}-*/;
}

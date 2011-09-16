package com.horaz.client.widgets;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

/**
 * Toast Notification
 */
public class Toast extends BaseWidget<DivElement> {
	/**
	 * defines how long the toast will be displayed
	 */
	public enum Duration {
		SHORT, LONG
	}

	/**
	 * finds toast element for given id
	 * @param id
	 */
	static public Toast byId(String id) {
		Element elm = getElementById(id);
		if (allWidgetInstances.get(elm) != null) {
			return (Toast) allWidgetInstances.get(elm);
		}
		return new Toast((DivElement) elm);
	}

	protected Toast(DivElement elm) {
		super(elm);
	}

	/**
	 * creates a new Toast notification
	 * @param innerHTML
	 * @param duration
	 */
	public Toast(String innerHTML, Duration duration) {
		super(null);

		Element elm = DOM.createDiv();
		elm.setInnerHTML(innerHTML);
		Document.get().getBody().appendChild(elm);

		setElement((DivElement) elm);
		allWidgetInstances.put(elm, this);

		_create(elm, duration.name().toLowerCase());
	}

	native private void _create(Element elm, String duration) /*-{
		$wnd.jQuery(elm).toast({duration:duration})
	}-*/;

	native private void _show(Element elm) /*-{
		$wnd.jQuery(elm).toast('show')
	}-*/;

	/**
	 * shows the toast. automatically hides the toast after the defined timeout.
	 */
	public void show() {
		_show(getElement());
	}
}

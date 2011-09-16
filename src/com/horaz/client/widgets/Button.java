package com.horaz.client.widgets;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.horaz.client.widgets.events.EventFactory;
import com.horaz.client.widgets.events.TapListener;

/**
 * Button
 * @horaz.htmltag &lt;a data-role="button"&gt;
 * @horaz.events tap
 * @see https://www.horaz-lang.com/dev-guide/button
 */
public class Button extends BaseWidget<AnchorElement> {
	/**
	 * finds button element for given id
	 * the html element has to be an anchor element
	 * @param id
	 */
	static public Button byId(String id) {
		Element elm = getElementById(id);
		if (allWidgetInstances.get(elm) != null) {
			return (Button) allWidgetInstances.get(elm);
		}
		return new Button((AnchorElement) elm);
	}

	protected Button(AnchorElement elm) {
		super(elm);
	}

	/**
	 * adds a listener for tap (touch / click) event
	 * @param tapListener
	 */
	public void addTapListener(TapListener tapListener) {
		EventFactory.bindEventHandler(getElement(), "tap", tapListener);
	}
}

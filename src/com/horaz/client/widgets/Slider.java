package com.horaz.client.widgets;

import com.google.gwt.dom.client.Element;

public class Slider {
	/**
	 * refreshes the state of a slider
	 * @param elm
	 */
	native public static void refresh(Element elm)/*-{
		$wnd.jQuery(elm).slider('refresh')
	}-*/;

}

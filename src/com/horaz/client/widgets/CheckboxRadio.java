package com.horaz.client.widgets;

import com.google.gwt.dom.client.Element;

public class CheckboxRadio {
	native public static void refresh(Element elm)/*-{
		$wnd.jQuery(elm).checkboxradio('refresh')
	}-*/;
}

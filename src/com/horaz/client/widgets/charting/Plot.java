package com.horaz.client.widgets.charting;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.horaz.client.widgets.BaseWidget;

public class Plot extends BaseWidget<DivElement> {

	static public Plot byId(String id) {
		Element elm = getElementById(id);
		if (allWidgetInstances.get(elm) != null) {
			return (Plot) allWidgetInstances.get(elm);
		}
		return new Plot((DivElement) elm);
	}

	protected Plot(DivElement elm) {
		super(elm);
	}

	native private void _init(Element elm)/*-{
		$wnd.jQuery.plot($wnd.jQuery(elm), [ [[0, 0], [1, 1]], [[0, 2], [1, 1]] ]);
	}-*/;

	public void init() {
		_init(getElement());
	}
}

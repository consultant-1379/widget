package com.ericsson.eniq.events.widgets.client.dropdown.decorator;

import com.ericsson.eniq.events.widgets.client.ToString;
import com.ericsson.eniq.events.widgets.client.dropdown.IDropDownItem;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface DropDownDecorator<V extends IDropDownItem> extends ToString<V> {

    boolean isClickable(V value);

    SafeHtml decorate(V value, int index, boolean selected, boolean marked);

}

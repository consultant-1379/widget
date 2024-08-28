package com.ericsson.eniq.events.widgets.client.dropdown.decorator;

import com.ericsson.eniq.events.widgets.client.dropdown.IDropDownItem;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDownResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class DefaultDropDownDecorator<V extends IDropDownItem> implements DropDownDecorator<V> {

    private static final DropDownTemplate TEMPLATE = GWT.create(DropDownTemplate.class);


    private final DropDownResourceBundle.DropDownItemStyle itemStyle;

    public DefaultDropDownDecorator(final DropDownResourceBundle resourceBundle) {
        this.itemStyle = resourceBundle.itemStyle();
    }

    @Override
    public SafeHtml decorate(final V value, final int index, final boolean selected, final boolean marked) {
        String className = itemStyle.item();

        if (selected) {
            className += " " + itemStyle.selected();
        }

        if (marked) {
            className += " " + itemStyle.marked();
        }

        return TEMPLATE.div(index, className, SafeHtmlUtils.fromString(toString(value)));
    }

    public SafeHtml decorate(final V value) {
        return TEMPLATE.separator(itemStyle.seperatorItem(), itemStyle.separatorLine(), itemStyle.seperatorLabel(),
                SafeHtmlUtils.fromString(value.toString()));
    }

    @Override
    public boolean isClickable(final V value) {
        return true;
    }

    @Override
    public String toString(final V value) {
        if (value == null) {
            return null;
        }

        return String.valueOf(value);
    }
}

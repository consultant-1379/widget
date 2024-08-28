package com.ericsson.eniq.events.widgets.client.dropdown;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class StringDropDownItem implements IDropDownItem {
    private final String val;

    public StringDropDownItem(final String val) {
        this.val = val;
    }

    public String getValue() {
        return val;
    }

    @Override
    public String toString() {
        return val;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof StringDropDownItem) {
            final StringDropDownItem item = (StringDropDownItem) obj;
            return val.equals(item.getValue());
        }
        return false;
    }

    @Override
    public boolean isSeparator() {
        return false;
    }
}
package com.ericsson.eniq.events.widgets.client.dropdown;

/**
 * Drop down item with independent label and value
 * 
 * @author eeoicon
 * @since 2013
 */
public class KeyValuePairDropDownItem implements IDropDownItem {
    private final String label;

    private final String val;

    public KeyValuePairDropDownItem(final String label, final String val) {
        this.label = label;
        this.val = val;
    }

    public String getValue() {
        return val;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof KeyValuePairDropDownItem) {
            if (val != null && label != null) {
                final KeyValuePairDropDownItem item = (KeyValuePairDropDownItem) obj;
                return val.equals(item.val) && label.equals(item.label);
            }
            if (val != null) {
                final KeyValuePairDropDownItem item = (KeyValuePairDropDownItem) obj;
                return val.equals(item.val) && item.label.equals(label);
            }
            if (label != null) {
                final KeyValuePairDropDownItem item = (KeyValuePairDropDownItem) obj;
                return item.val.equals(val) && label.equals(item.label);
            }
            //TODO Handle better. Project Coin available?
        }
        return false;
    }

    @Override
    public boolean isSeparator() {
        return false;
    }
}
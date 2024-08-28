package com.ericsson.eniq.events.widgets.client.dropdown.oracle;

import com.google.gwt.user.client.ui.SuggestOracle;

public class DropDownSuggestion<T> implements SuggestOracle.Suggestion {

    private T item;
    private String itemStringValue;

    public DropDownSuggestion(T item, String itemStringValue) {
        this.item = item;
        this.itemStringValue = itemStringValue;
    }

    public String getDisplayString() {
        return itemStringValue;
    }

    public String getReplacementString() {
        return itemStringValue;
    }

    public T getItem() {
        return item;
    }
}

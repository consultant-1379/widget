package com.ericsson.eniq.events.widgets.client.dropdown.oracle;

import java.util.List;

import com.ericsson.eniq.events.widgets.client.ToString;

public interface DropDownSuggestOracle<T> {
    void update(List<T> items);

    boolean isRemote();

    public void setConverter(ToString<T> converter);
}

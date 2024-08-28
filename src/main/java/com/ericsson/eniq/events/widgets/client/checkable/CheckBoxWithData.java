package com.ericsson.eniq.events.widgets.client.checkable;

import com.google.gwt.user.client.ui.CheckBox;

public class CheckBoxWithData<D> extends CheckBox implements ICheckable<D> {

    private final D data;

    public CheckBoxWithData(String label, D data) {
        super(label);
        this.data = data;
    }

    public D getData() {
        return data;
    }

    @Override public CheckBox getUiItem() {
        return this;
    }

}

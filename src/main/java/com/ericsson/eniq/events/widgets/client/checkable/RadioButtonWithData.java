package com.ericsson.eniq.events.widgets.client.checkable;

import com.google.gwt.user.client.ui.RadioButton;

public class RadioButtonWithData<D> extends RadioButton implements ICheckable<D> {

    private final D data;

    public RadioButtonWithData(String groupName, String label, D data) {
        super(groupName, label);
        this.data = data;
    }

    public D getData() {
        return data;
    }

    @Override public RadioButton getUiItem() {
        return this;
    }
}

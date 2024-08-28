package com.ericsson.eniq.events.widgets.client.menu.options;

import com.google.gwt.user.client.ui.Label;

class OptionsMenuItem<D> extends Label {

   private final D itemData;

    OptionsMenuItem(String text, D itemData) {
        super(text);

        this.itemData = itemData;
    }

    D getData() {
        return itemData;
    }
}

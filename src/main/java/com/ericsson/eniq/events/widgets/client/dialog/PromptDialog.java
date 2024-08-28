package com.ericsson.eniq.events.widgets.client.dialog;

import com.ericsson.eniq.events.widgets.client.dialog.events.PromptCancelEvent;
import com.ericsson.eniq.events.widgets.client.dialog.events.PromptCancelEventHandler;
import com.ericsson.eniq.events.widgets.client.dialog.events.PromptOkEvent;
import com.ericsson.eniq.events.widgets.client.dialog.events.PromptOkEventHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class PromptDialog extends MessageDialog {

    @Override
    public DialogPanel createPanel() {
        final ClickHandler oKClickHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                fireEvent(new PromptOkEvent());
                hide();
            }
        };

        final ClickHandler cancelClickHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                fireEvent(new PromptCancelEvent());
                hide();
            }
        };

        return new PromptDialogPanel(oKClickHandler, cancelClickHandler);
    }

    public void addOkEventHandler(final PromptOkEventHandler handler) {
        addHandler(handler, PromptOkEvent.getType());
    }

    public void addCancelEventHandler(final PromptCancelEventHandler handler) {
        addHandler(handler, PromptCancelEvent.getType());
    }
}

package com.ericsson.eniq.events.widgets.client.dialog.events;

import com.google.gwt.event.shared.GwtEvent;

public class PromptOkEvent extends GwtEvent<PromptOkEventHandler> {

    static final Type<PromptOkEventHandler> TYPE = new Type<PromptOkEventHandler>();

    public static Type<PromptOkEventHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<PromptOkEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final PromptOkEventHandler handler) {
        handler.onPromptOk(this);
    }
}
package com.ericsson.eniq.events.widgets.client.dialog.events;

import com.google.gwt.event.shared.GwtEvent;

public class PromptCancelEvent extends GwtEvent<PromptCancelEventHandler> {

    static final Type<PromptCancelEventHandler> TYPE = new Type<PromptCancelEventHandler>();

    public static Type<PromptCancelEventHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<PromptCancelEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(final PromptCancelEventHandler handler) {
        handler.onPromptCancel(this);
    }
}

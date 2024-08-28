/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2010 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.checkable.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.CheckBox;

//TODO class java doc
public class ChildSelectEvent extends GwtEvent<ChildSelectEventHandler> {

    private static Type<ChildSelectEventHandler> type;

    private final CheckBox child;

    public ChildSelectEvent(final CheckBox child) {
        this.child = child;
    }

    public static Type<ChildSelectEventHandler> getType() {
        if (type == null) {
            type = new Type<ChildSelectEventHandler>();
        }
        return type;
    }

    @Override
    public Type<ChildSelectEventHandler> getAssociatedType() {
        return getType();
    }

    @Override
    protected void dispatch(final ChildSelectEventHandler handler) {
        handler.onChildSelect(this);
    }

    public CheckBox getChild() {
        return child;
    }
}

package com.ericsson.eniq.events.widgets.client.checkable.event;

import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public interface HasChildSelectEventHandler extends HasHandlers {
    HandlerRegistration addChildSelectEventHandler(ChildSelectEventHandler handler);
}

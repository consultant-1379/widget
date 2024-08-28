package com.ericsson.eniq.events.widgets.client.checkable;

import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEvent;
import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEventHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public class AllCheckableWatcher {

    private final ChildClickHandler childClickHandler = new ChildClickHandler();

    private final HandlerManager manager = new HandlerManager(this);

    public void registerChild(final CheckBox child) {
         child.addClickHandler(childClickHandler);
    }

    public HandlerRegistration addChildSelectEventHandler(final ChildSelectEventHandler handler) {
        return manager.addHandler(ChildSelectEvent.getType(), handler);
    }

    private class ChildClickHandler implements ClickHandler {
        @Override
        public void onClick(final ClickEvent event) {
            final CheckBox child = (CheckBox) event.getSource();
            manager.fireEvent(new ChildSelectEvent(child));
        }
    }
}

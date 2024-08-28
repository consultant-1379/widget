/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.scroll;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 * @author ekurshi
 * @since 2012
 *
 */
class ScrollTrack extends FocusPanel implements HasMouseDownHandlers {

    ScrollTrack() {
        getElement().getStyle().setProperty("outline", "none");
        getElement().getStyle().setOverflow(Overflow.HIDDEN);
    }

    @Override
    public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }
}

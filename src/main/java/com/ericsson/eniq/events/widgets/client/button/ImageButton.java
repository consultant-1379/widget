/*
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.ericsson.eniq.events.widgets.client.button;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.impl.FocusImpl;

public class ImageButton extends ButtonBase {

    static final FocusImpl impl = FocusImpl.getFocusImplForPanel();

    private Element actualImageElement;

    private Element imageElement;

    private Element hoverImageElement;

    private Element disabledImageElement;

    private HoverHandler hoverHandler;

    private boolean isHovered;

    public @UiConstructor
    ImageButton(final ImageResource imageResource) {
        super(impl.createFocusable());
        this.imageElement = AbstractImagePrototype.create(imageResource).createImage().getElement();
        setActualImage(imageElement);
    }
    
    public void setImage(final ImageResource imageResource){
        this.imageElement = AbstractImagePrototype.create(imageResource).createImage().getElement();
        setActualImage(imageElement);
    }

    public void setHoverImage(final ImageResource hoverImage) {
        this.hoverImageElement = AbstractImagePrototype.create(hoverImage).createImage().getElement();

        if (hoverHandler == null) {
            hoverHandler = new HoverHandler();
            addMouseOutHandler(hoverHandler);
            addMouseOverHandler(hoverHandler);
        }
    }

    public void setDisabledImage(final ImageResource disabledImage) {
        disabledImageElement = AbstractImagePrototype.create(disabledImage).createImage().getElement();
    }

    @Override
    public void setEnabled(final boolean isEnabled) {
        if (isEnabled() != isEnabled) {
            if (isEnabled) {
                getElement().getStyle().setCursor(Style.Cursor.POINTER);
                setActualImage(imageElement);
            } else {
                getElement().getStyle().clearCursor();
                setActualImage(disabledImageElement);
            }
        }

        super.setEnabled(isEnabled);
    }

    void setHover(final boolean hover) {
        if (isHovered != hover) {
            if (hover) {
                setActualImage(hoverImageElement);
            } else {
                setActualImage(imageElement);
            }

            this.isHovered = hover;
        }
    }

    private void setActualImage(final Element actual) {
        if (actual != null && !actual.equals(this.actualImageElement)) {
            if (actualImageElement != null) {
                DOM.removeChild(getElement(), actualImageElement);
            }

            this.actualImageElement = actual;
            DOM.appendChild(getElement(), actualImageElement);
        }
    }

    private class HoverHandler implements MouseOutHandler, MouseOverHandler {
        @Override
        public void onMouseOut(final MouseOutEvent event) {
            final ImageButton source = (ImageButton) event.getSource();
            if (source.isEnabled()) {
                source.setHover(false);
            }
        }

        @Override
        public void onMouseOver(final MouseOverEvent event) {
            final ImageButton source = (ImageButton) event.getSource();
            if (source.isEnabled()) {
                source.setHover(true);
                source.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            }
        }
    }

    @Override
    public void onBrowserEvent(final Event event) {
        switch (DOM.eventGetType(event)) {
        case Event.ONCLICK:
            if (!isEnabled()) {
                return;
            }
        }

        super.onBrowserEvent(event);
    }
}

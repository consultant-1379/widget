package com.ericsson.eniq.events.widgets.client.slider;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class SliderThumb extends FocusPanel implements HasMouseOverHandlers, HasMouseOutHandlers, HasMouseDownHandlers {

    private static final int ARROW_PRECISION = 1;

    protected boolean dragLocked = false;

    private int resizeStartX;

    private int preResizeWidth;

    private boolean isDragging;

    private final IColoredBar bar;

    private IColoredBar nextBar;

    private IUpdateHandler updateHandler;

    public SliderThumb(final IColoredBar coloredBar) {
        this.bar = coloredBar;
        sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEMOVE | Event.ONKEYPRESS | Event.ONCLICK);
        getElement().getStyle().setCursor(Cursor.POINTER);
    }

    public void setNextBar(final IColoredBar nextBar) {
        this.nextBar = nextBar;
        bar.setMaxWidth(bar.getWidth() + nextBar.getWidth() - nextBar.getMinWidth());
    }

    public void setThumbWidth(final int width) {
        setWidth(width + "px");
    }

    @Override
    public void onBrowserEvent(final Event event) {
        final Element e = getElement();

        switch (event.getTypeInt()) {
        case Event.ONMOUSEDOWN:
            resizeStartX = getXPosition(event);
            preResizeWidth = bar.getWidth();
            isDragging = true;
            Event.setCapture(e);
            event.preventDefault();
            final int width = nextBar.getWidth();
            final int maxWidth = width + bar.getWidth() - nextBar.getMinWidth();
            bar.setMaxWidth(maxWidth);
            break;
        case Event.ONMOUSEUP:
            isDragging = false;
            Event.releaseCapture(e);
            break;

        case Event.ONMOUSEMOVE:
            if (isDragging && !dragLocked) {
                final int xPos = getXPosition(event);
                final int deltaX = xPos - resizeStartX;
                if (updateAndNotify(deltaX)) {
                    resizeStartX = xPos;
                }
                event.preventDefault();
            }
            break;
        case Event.ONKEYPRESS:
            if (DOM.eventGetKeyCode(event) == KeyCodes.KEY_LEFT) {
                final int deltaX = -ARROW_PRECISION;
                updateAndNotify(deltaX);
            } else if (DOM.eventGetKeyCode(event) == KeyCodes.KEY_RIGHT) {
                final int deltaX = ARROW_PRECISION;
                updateAndNotify(deltaX);
            }
            event.preventDefault();
            break;
        case Event.ONCLICK:
            setFocus(true);
            event.preventDefault();
            break;
        }
        event.preventDefault();
    }

    public void setSlide(final boolean slide) {
        if (slide) {
            getElement().getStyle().setCursor(Cursor.POINTER);
        } else {
            getElement().getStyle().setCursor(Cursor.DEFAULT);
        }
        dragLocked = !slide;
    }

    private boolean updateAndNotify(final int deltaX) {
        int newWidth = preResizeWidth + deltaX;
        if (newWidth == preResizeWidth) {
            return false;
        }
        final int oldWidth = newWidth;
        newWidth = Math.min(Math.max(bar.getMinWidth(), newWidth), bar.getMaxWidth());
        if (oldWidth != newWidth) {
            return true;
        }
        bar.setBarWidth(newWidth);
        preResizeWidth = newWidth;
        nextBar.doWestResize(-deltaX);
        updateHandler.onUpdate();
        return true;
    }

    private int getXPosition(final Event event) {
        return event.getClientX();
    }

    public void setUpdatedHandler(final IUpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    //TODO not  running properly
    public void setCustomWidth(final int width) {
        updateAndNotify(preResizeWidth - width);
    }
}

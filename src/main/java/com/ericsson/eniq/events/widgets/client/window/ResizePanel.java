package com.ericsson.eniq.events.widgets.client.window;

import com.ericsson.eniq.events.widgets.client.scroll.ScrollConstants;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 * This is custom resize panel which can be used to resize any widget inside as user wants(S,N,W,E,SN,SW,NE,NW).
 * Please note that panel itself does not do any resize to it's childs, you will have to provide your ResizeHandler
 * and resize your child component based on newX and newY. This will give more flexibility to control resize process.
 * <p/>
 * See FloatingWindow as an example of usage.
 * <p/>
 * !IMPORTANT!
 * Padding constant is driving sensitivity of resize, the less it is the closer to component mouse should be
 * to see resize indicator.
 * StopResize is used when you want to maximize window and avoid resize than.
 * <p/>
 * If you consider modifying this code, please contact me as it will affect any component which is using it to resize.
 *
 * @author evyagrz
 * @author ekurshi
 * @since 06 2012
 */
public class ResizePanel extends FocusPanel {

    private static final int PADDING = 5; // This is resize panel padding, used for calculations

    private int minWidth = 40;

    private int minHeight = 40;

    private boolean inResize;

    private boolean stopResize;

    private ResizeHandler handler;

    private boolean finishedLayoutCommand = true;

    private final ScheduledCommand layoutCommand;

    private int resizeStartX;

    private int maxWidth;

    private int maxHeight;

    private int preResizeWidth;

    private int preResizeHeight;

    private int resizeStartY;

    private boolean eastResize;

    private boolean westResize;

    private boolean southResize;

    private boolean northResize;

    private boolean northEastResize;

    private boolean northWestResize;

    private boolean southEastResize;

    private boolean southWestResize;

    private int newWidth;

    private int newHeight;

    private int deltaX;

    private int deltaY;

    private int minX, minY, maxX, maxY;

    public ResizePanel() {
        sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEMOVE);
        maxWidth = Window.getClientWidth();
        maxHeight = Window.getClientHeight();
        minHeight = 0;
        minWidth = 0;
        layoutCommand = new Command() {
            @Override
            public void execute() {
                updateAndNotify();
                finishedLayoutCommand = true;
            }
        };
        setBoundries(0, 0, maxWidth, maxHeight);
        getElement().getStyle().setPadding(PADDING, Unit.PX);
        getElement().getStyle().setProperty("outline", "0");
    }

    /**
     * Pass your custom handler to resize childs
     *
     * @param handler
     */
    public void addResizeHandler(final ResizeHandler handler) {
        this.handler = handler;
        final Element element = getElement();
        this.handler.onResize(element.getClientWidth(), element.getClientHeight());//TODO with/without padding
    }

    @Override
    public void onBrowserEvent(final Event event) {
        final Element e = getElement();
        final int bottom = e.getAbsoluteBottom();
        final int top = e.getAbsoluteTop();
        final int right = e.getAbsoluteRight();
        final int left = e.getAbsoluteLeft();

        switch (event.getTypeInt()) {
        case Event.ONMOUSEDOWN:

            resizeStartX = getXPosition(event);
            resizeStartY = getYPosition(event);
            preResizeWidth = e.getClientWidth() - (PADDING * 2);
            preResizeHeight = e.getClientHeight() - (PADDING * 2);
            if (calculateParams(resizeStartX, resizeStartY, top, right, bottom, left)) {
                inResize = true;
                Event.setCapture(e);
                event.preventDefault();
                event.stopPropagation();
            }
            //event.stopPropagation();//not using preventDefault because it prevent the focus event to propagate to inner elements
            break;
        case Event.ONMOUSEUP:
            inResize = false;
            Event.releaseCapture(e);
            break;

        case Event.ONMOUSEMOVE:
            if (inResize && !stopResize) {
                final int xPos = getXPosition(event);
                final int yPos = getYPosition(event);
                doResize(xPos, yPos);
                if (newWidth == preResizeWidth && newHeight == preResizeHeight) {
                    break;
                }
                resizeLayout();
                event.preventDefault();
                event.stopPropagation();
            } else if (!stopResize) {
                //update cursor only when window is resizable
                calculateParams(getXPosition(event), getYPosition(event), top, right, bottom, left);
                updateCursor(e);
            }
            //event.stopPropagation();//not using preventDefault because it prevent the focus event to propagate to inner elements
            break;
        }
    }

    private int getYPosition(final Event event) {
        return Math.min(Math.max(minY, event.getClientY()), maxY);
    }

    private int getXPosition(final Event event) {
        return Math.min(Math.max(minX, event.getClientX()), maxX);
    }

    /**
     * Set true to make window unresizable
     * @param stopResize
     */
    public void setStopResize(final boolean stopResize) {
        this.stopResize = stopResize;
    }

    private void doResize(final int xPos, final int yPos) {
        if (westResize) {
            doWest(xPos);
        } else if (southResize) {
            doSouth(yPos);
        } else if (eastResize) {
            doEast(xPos);
        } else if (northResize) {
            doNorth(yPos);
        } else if (southEastResize) {
            doSouthEast(xPos, yPos);
        } else if (southWestResize) {
            doSouthWest(xPos, yPos);
        } else if (northWestResize) {
            doNorthWest(xPos, yPos);
        } else if (northEastResize) {
            doNorthEast(xPos, yPos);
        }

    }

    private void updateCursor(final Element e) {
        if (westResize) {
            e.getStyle().setCursor(Cursor.W_RESIZE);
        } else if (southResize) {
            e.getStyle().setCursor(Cursor.S_RESIZE);
        } else if (eastResize) {
            e.getStyle().setCursor(Cursor.E_RESIZE);
        } else if (northResize) {
            e.getStyle().setCursor(Cursor.N_RESIZE);
        } else if (southEastResize) {
            e.getStyle().setCursor(Cursor.SE_RESIZE);
        } else if (southWestResize) {
            e.getStyle().setCursor(Cursor.SW_RESIZE);
        } else if (northWestResize) {
            e.getStyle().setCursor(Cursor.NW_RESIZE);
        } else if (northEastResize) {
            e.getStyle().setCursor(Cursor.NE_RESIZE);
        } else {
            e.getStyle().setCursor(Cursor.DEFAULT);
        }
    }

    private void resizeLayout() {
        // Defer actually updating the finishedLayoutCommand, so that if we receive many
        // mouse events before finishedLayoutCommand/paint occurs, we'll only update once.
        if (finishedLayoutCommand) {
            finishedLayoutCommand = false;
            Scheduler.get().scheduleDeferred(layoutCommand);
        }
    }

    private boolean calculateParams(final int xPos, final int yPos, final int top, final int right, final int bottom,
            final int left) {
        final int bottomWithoutPadding = bottom - PADDING;
        final int rightWithoutPadding = right - PADDING;
        final int topWithoutPadding = top + PADDING;
        final int leftWithoutPadding = left + PADDING;
        eastResize = (yPos <= bottomWithoutPadding && yPos >= topWithoutPadding)
                && (xPos <= right && xPos >= rightWithoutPadding);
        westResize = (yPos <= bottomWithoutPadding && yPos >= topWithoutPadding)
                && (xPos >= left && xPos <= leftWithoutPadding);
        northResize = (xPos >= leftWithoutPadding && xPos <= rightWithoutPadding)
                && (yPos >= top && yPos <= topWithoutPadding);
        southResize = (xPos >= leftWithoutPadding && xPos <= rightWithoutPadding)
                && (yPos <= bottom && yPos >= bottomWithoutPadding);
        southEastResize = (xPos >= rightWithoutPadding && xPos <= right)
                && (yPos <= bottom && yPos >= bottomWithoutPadding);
        southWestResize = (yPos <= bottom && yPos >= bottomWithoutPadding)
                && (xPos >= left && xPos <= leftWithoutPadding);
        northEastResize = (xPos <= right && xPos >= rightWithoutPadding) && (yPos >= top && yPos <= topWithoutPadding);
        northWestResize = (xPos >= left && xPos <= leftWithoutPadding) && (yPos >= top && yPos <= topWithoutPadding);
        return eastResize || westResize || northResize || southResize || southEastResize || southWestResize
                || northEastResize || northWestResize;
    }

    private void doEast(final int xPos) {
        newWidth = preResizeWidth + (xPos - resizeStartX);
        newHeight = preResizeHeight;
    }

    private void doWest(final int xPos) {
        deltaX = xPos - resizeStartX;
        newWidth = preResizeWidth - deltaX;
        newHeight = preResizeHeight;
    }

    private void doSouth(final int yPos) {
        newWidth = preResizeWidth;
        newHeight = preResizeHeight + (yPos - resizeStartY);
    }

    private void doNorth(final int yPos) {
        newWidth = preResizeWidth;
        deltaY = yPos - resizeStartY;
        newHeight = preResizeHeight - deltaY;
    }

    private void doNorthEast(final int xPos, final int yPos) {
        newWidth = preResizeWidth + (xPos - resizeStartX);
        deltaY = yPos - resizeStartY;
        newHeight = preResizeHeight - deltaY;
    }

    private void doNorthWest(final int xPos, final int yPos) {
        deltaX = xPos - resizeStartX;
        deltaY = yPos - resizeStartY;
        newWidth = preResizeWidth - deltaX;
        newHeight = preResizeHeight - deltaY;
    }

    private void doSouthWest(final int xPos, final int yPos) {
        deltaX = xPos - resizeStartX;
        newWidth = preResizeWidth - deltaX;
        newHeight = preResizeHeight + (yPos - resizeStartY);
    }

    private void doSouthEast(final int xPos, final int yPos) {
        newWidth = preResizeWidth + (xPos - resizeStartX);
        newHeight = preResizeHeight + (yPos - resizeStartY);
    }

    private void updateAndNotify() {
        final int oldWidth = newWidth;
        final int oldHeight = newHeight;
        newWidth = Math.min(Math.max(minWidth, newWidth), maxWidth);
        newHeight = Math.min(Math.max(minHeight, newHeight), maxHeight);
        if (oldHeight != newHeight || oldWidth != newWidth) {
            return;
        }
        final Style style = getElement().getStyle();
        final int styleTop = ScrollConstants.convertToInt(style.getTop());
        final int styleLeft = ScrollConstants.convertToInt(style.getLeft());
        if (northResize || northEastResize) {
            style.setTop(styleTop + deltaY, Unit.PX);
            resizeStartY = resizeStartY + deltaY;
            preResizeHeight = newHeight;
        } else if (westResize || southWestResize) {
            style.setLeft(styleLeft + deltaX, Unit.PX);
            resizeStartX = resizeStartX + deltaX;
            preResizeWidth = newWidth;
        } else if (northWestResize) {
            style.setTop(styleTop + deltaY, Unit.PX);
            style.setLeft(styleLeft + deltaX, Unit.PX);
            resizeStartY = resizeStartY + deltaY;
            resizeStartX = resizeStartX + deltaX;
            preResizeHeight = newHeight;
            preResizeWidth = newWidth;
        }
        setWidth(newWidth + "px");
        setHeight(newHeight + "px");
        handler.onResize(newWidth + PADDING * 2, newHeight + PADDING * 2);
    }

    public int getVisibleWidth() {
        return getOffsetWidth() - PADDING * 2;
    }

    public int getVisibleHeight() {
        return getOffsetHeight() - PADDING * 2;
    }

    public void setMinWidth(final int minWidth) {
        this.minWidth = minWidth;
    }

    public void setMinHeight(final int minHeight) {
        this.minHeight = minHeight;
    }

    public void setMaxWidth(final int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMaxHeight(final int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setBoundries(final int minX, final int minY, final int maxX, final int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    interface ResizeHandler {
        void onResize(int width, int height);
    }

    public static int getPadding() {
        return PADDING;
    }
}
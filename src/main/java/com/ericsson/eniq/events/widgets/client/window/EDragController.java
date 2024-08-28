package com.ericsson.eniq.events.widgets.client.window;

import com.allen_sauer.gwt.dnd.client.util.DragClientBundle;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 * Handle Dragging of Floating Windows.
 * @author ecarsea
 */
public class EDragController implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

    protected FloatingWindow parent;

    protected boolean dragging;

    protected boolean draggable;

    protected boolean dragStarted;

    protected int dragStartX;

    protected int dragStartY;

    protected boolean inDoubleClick;

    private final FocusPanel draggableElement;

    private final Element boundaryPanel;

    private Command layoutCommand;

    public EDragController(final FloatingWindow parent, final Element boundaryPanel) {
        this.parent = parent;
        draggable = true;
        this.draggableElement = parent.getDraggable();
        this.draggableElement.addStyleName(DragClientBundle.INSTANCE.css().handle());
        this.boundaryPanel = boundaryPanel;
        initHandlers();
    }

    protected void initHandlers() {
        draggableElement.addMouseDownHandler(this);
        draggableElement.addMouseMoveHandler(this);
        draggableElement.addMouseUpHandler(this);
    }

    /**
     * MouseDownHandler
     */
    @Override
    public void onMouseDown(final MouseDownEvent event) {
        if (draggable) {
            dragStarted = true;
            DOM.setCapture(draggableElement.getElement());
            dragStartX = event.getX();
            dragStartY = event.getY();
        }
    }

    /**
     * MouseMoveHandler
     */
    @Override
    public void onMouseMove(final MouseMoveEvent event) {
        final int x = event.getX();
        final int y = event.getY();
        if (dragStartX == x && dragStartY == y) {
            return;
        } else if (dragStarted && !dragging) {
            dragging = true;
        }
        if (dragging) {
            final int absX = x + parent.getLeft();
            final int absY = y + parent.getTop();

            if (layoutCommand == null) {
                layoutCommand = new Command() {
                    @Override
                    public void execute() {
                        layoutCommand = null;
                        positionParent(absY - dragStartY, absX - dragStartX);
                    }
                };
                Scheduler.get().scheduleDeferred(layoutCommand);
            }
        }
    }

    /**
     * MouseUpHandler
     */
    @Override
    public void onMouseUp(final MouseUpEvent event) {
        if (dragging || dragStarted) {
            DOM.releaseCapture(draggableElement.getElement());
            final int absX = event.getX() + parent.getLeft();
            final int absY = event.getY() + parent.getTop();

            positionParent(absY - dragStartY, absX - dragStartX);
            dragging = false;
            dragStarted = false;
        }
    }

    /**
     * Position the parent window. Check all limits of the boundary panel
     * 
     * @param top
     * @param left
     */
    private void positionParent(final int top, final int left) {
        // limit top and left limits
        int windowLeft = Math.max(0, left);
        int windowTop = Math.max(0, top);

        // limit right and bottom limits
        int parentOffsetWidth = parent.getOffsetWidth();
        int parentOffsetHeight = parent.getOffsetHeight();

        final int windowRight = windowLeft + parentOffsetWidth;
        final int windowBottom = windowTop + parentOffsetHeight;

        int boundaryPanelOffsetWidth = boundaryPanel.getOffsetWidth();
        if (windowRight >= boundaryPanelOffsetWidth) {
            windowLeft = boundaryPanelOffsetWidth - parentOffsetWidth;
        }

        int boundaryPanelOffsetHeight = boundaryPanel.getOffsetHeight();
        if (windowBottom >= boundaryPanelOffsetHeight) {
            windowTop = boundaryPanelOffsetHeight - parentOffsetHeight;
        }

        windowLeft = Math.max(0, windowLeft);
        windowTop = Math.max(0, windowTop);

        parent.setWindowLocation(windowLeft, windowTop);
    }

    public void setDraggable(final boolean draggable) {
        this.draggable = draggable;
    }

}

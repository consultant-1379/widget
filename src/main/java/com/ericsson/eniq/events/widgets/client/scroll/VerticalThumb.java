package com.ericsson.eniq.events.widgets.client.scroll;

import static com.ericsson.eniq.events.widgets.client.scroll.ScrollConstants.*;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.eniq.events.widgets.client.scroll.events.CustomScrollEvent;
import com.ericsson.eniq.events.widgets.client.scroll.events.CustomScrollEvent.EventType;
import com.ericsson.eniq.events.widgets.client.scroll.events.CustomScrollHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author ealeerm - Alexey Ermykin
 * @author ekurshi
 * @since 31/01/12
 */
class VerticalThumb extends SimplePanel implements HasMouseOverHandlers, HasMouseOutHandlers {

    protected final IScrollable scrollablePanel;

    protected final ScrollTrack scrollTrack;

    protected boolean isScrolling = false;

    protected boolean isDragging = false;

    private int thumbPosition;

    /**
     * work around for firing scroll completion event when scrolling is finished. Event will be fired after scrolling finish rather that on every mouse wheel movement
     */

    private ScheduledCommand command;

    private int mouseY = UNDEF;

    private boolean eventCompleted = true;

    private boolean fireCustomScrollEvent = false;

    private final List<HandlerRegistration> handlerRegistrations;

    VerticalThumb(final IScrollable scrollable, final ScrollTrack scrollTrack) {
        this.scrollablePanel = scrollable;
        this.scrollTrack = scrollTrack;
        scrollTrack.add(this);
        handlerRegistrations = new ArrayList<HandlerRegistration>();
        init();
    }

    protected void init() {
        setWidth(THUMB_WIDTH + "px");
        getElement().getStyle().setProperty("margin", "auto");
        command = new ScheduledCommand() {

            @Override
            public void execute() {
                eventCompleted = true;
                fireScrollEvent(EventType.SCROLL_COMPLETION);
            }
        };
    }

    /**
     * Need to do this way, cause we need to know width and height which is calculated only onAttach
     */
    @Override
    protected void onAttach() {
        super.onAttach();
        addNativePreviewHandler();
        addMouseHandlers();
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        removeHandlers();
    }

    private void removeHandlers() {
        for (final HandlerRegistration reg : handlerRegistrations) {
            reg.removeHandler();
        }
        handlerRegistrations.clear();
    }

    private void setTop(final int value) {
        thumbPosition = value;
        getElement().getStyle().setTop(value, Style.Unit.PX);
    }

    public void redraw() {
        redraw(false);
    }

    /**
     * Calculates and applies scroll height size and other parameters. </br> Note: it should be invoked after {@link #postAttachInit(int)}.
     * 
     * @see #postAttachInit(int)
     */
    public void redraw(final boolean persistPosition) {
        // This height does not include decorations such as border, margin, and padding.
        setHeight(getThumbSize() - (THUMB_BORDER_SIZE * 2) + "px");//TODO need to revisit, when scroll is not needed and this method called
        scrollTrack.setHeight(scrollablePanel.getVerticalTrackHeight() + "px");
        if (scrollablePanel.isArrowEnabled()) {
            scrollTrack.getElement().getStyle().setMarginTop(ARROW_HEIGHT, Unit.PX);
        }
        if (persistPosition) {
            setContentPosition(scrollablePanel.getVerticalScrollPosition());
        } else {
            resetScroll();
        }
    }

    public void resetScroll() {
        scrollablePanel.setVerticalScrollPosition(0);
        setTop(0);
    }

    protected int getThumbPosition() {
        return thumbPosition;
    }

    protected int getThumbSize() {
        return scrollablePanel.getVerticalThumbHeight();
    }

    @Override
    public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
        final HandlerRegistration reg = addDomHandler(handler, MouseOutEvent.getType());
        handlerRegistrations.add(reg);
        return reg;
    }

    @Override
    public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
        final HandlerRegistration reg = addDomHandler(handler, MouseOverEvent.getType());
        handlerRegistrations.add(reg);
        return reg;
    }

    /**
     * Adds a {@link CustomScrollEvent} handler. CustomScrollEvent will be fired when dragging and scrolling completes, on line change, on page change.
     * 
     * @param handler
     *            the handler
     * @return returns the handler registration
     */
    public HandlerRegistration addCustomScrollHandler(final CustomScrollHandler handler) {
        fireCustomScrollEvent = true;
        final HandlerRegistration reg = addHandler(handler, CustomScrollEvent.getType());
        handlerRegistrations.add(reg);
        return reg;
    }

    public void setContentPosition(int position) {
        @SuppressWarnings("hiding")
        int thumbPosition;
        if (position > scrollablePanel.getMaximumVerticalScrollPosition()) {
            position = scrollablePanel.getMaximumVerticalScrollPosition();
            thumbPosition = getMaxThumbPosition();
        } else if (position < scrollablePanel.getMinimumVerticalScrollPosition()) {
            position = scrollablePanel.getMinimumVerticalScrollPosition();
            thumbPosition = getMinThumbPosition();
        } else {
            thumbPosition = calculateThumbPosFromContentPos(position);
        }
        scrollablePanel.setVerticalScrollPosition(position);
        setTop(thumbPosition);
    }

    protected int getMaxThumbPosition() {
        return scrollablePanel.getMaxVertThumbPosition();
    }

    private int calculateThumbPosFromContentPos(final int contentPosition) {
        final int position = (int) Math.floor(contentPosition * scrollablePanel.getVScrollerFactor());
        return position;
    }

    protected int calculateContentPosition(@SuppressWarnings("hiding")
    final int thumbPosition) {
        final int position = (int) Math.floor(thumbPosition / scrollablePanel.getVScrollerFactor());
        return position;
    }

    protected void onMouseOut() {
        if (!isDragging && !isScrolling) {
            mouseY = UNDEF;
        }
    }

    protected void onMouseOutFromDoc() {
        isScrolling = false;
        if (isDragging) {
            isDragging = false;
            fireScrollEvent(EventType.DRAGGED);
        }
        mouseY = UNDEF;
    }

    protected void onMouseMove(final Event.NativePreviewEvent event) {
        final int oldMouseY = mouseY;
        mouseY = event.getNativeEvent().getClientY();

        if (isDragging && oldMouseY != UNDEF) {
            final int moveDelta = mouseY - oldMouseY;
            changeScrollerPosition(moveDelta);
        }
    }

    protected void onMouseUp() {
        isScrolling = false;
        if (isDragging) {
            isDragging = false;
            fireScrollEvent(EventType.DRAGGED);
        }
    }

    private void onMouseDown(@SuppressWarnings("unused")
    final Event.NativePreviewEvent event) {
        isDragging = true;
        isScrolling = true;
    }

    private void onMouseWheelDetected(final MouseWheelEvent event) {
        int deltaY = event.getDeltaY();
        if (event.isNorth()) {
            deltaY = -MOUSE_WHEEL_SCROLL_DELTA - Math.abs(deltaY);
        } else if (event.isSouth()) {
            deltaY = MOUSE_WHEEL_SCROLL_DELTA + deltaY;
        }
        isScrolling = true;
        changeScrollerPosition(deltaY);
        if (eventCompleted && fireCustomScrollEvent) {
            eventCompleted = false;
            Scheduler.get().scheduleDeferred(command);
        }
    }

    protected void onMouseDownOnTrack(final MouseDownEvent event) {
        final int y = event.getY();
        final int scrollerTop = getThumbPosition();
        if (y < scrollerTop) { // isAboveScroller
            setContentPosition(scrollablePanel.getVerticalScrollPosition() - scrollablePanel.getPageHeight());
            fireScrollEvent(EventType.PAGE_CHANGE);
        } else if (y > scrollerTop + getThumbSize()) { // isBelowScroller
            setContentPosition(scrollablePanel.getVerticalScrollPosition() + scrollablePanel.getPageHeight());
            fireScrollEvent(EventType.PAGE_CHANGE);
        }
    }

    /**
     * Fires when scroll position has changed
     * 
     * @param eventType
     */
    public void fireScrollEvent(final EventType eventType) {
        if (fireCustomScrollEvent) {//when getHandlerCount(CustomScrollEvent.getType())>0
            fireEvent(new CustomScrollEvent(scrollablePanel.getVerticalScrollPosition(), getThumbPosition(), eventType));
        }
    }

    private void addMouseHandlers() {
        addMouseWheelHandler();
        scrollTrack.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(final MouseDownEvent event) {
                final int button = event.getNativeButton();
                if (button == NativeEvent.BUTTON_LEFT || button == NativeEvent.BUTTON_MIDDLE) {
                    onMouseDownOnTrack(event);
                }
            }
        });
    }

    protected void addMouseWheelHandler() {
        final MouseWheelHandler mouseWheelHandler = new MouseWheelHandler() {
            @Override
            public void onMouseWheel(final MouseWheelEvent event) {
                onMouseWheelDetected(event);
            }
        };

        scrollablePanel.addMouseWheelHandler(mouseWheelHandler);
        scrollTrack.addMouseWheelHandler(mouseWheelHandler);
    }

    private void addNativePreviewHandler() {
        final HandlerRegistration handlerRegistration = Event.addNativePreviewHandler(new MyNativePreviewHandler());
        handlerRegistrations.add(handlerRegistration);
    }

    public boolean changeScrollerPosition(final int moveDelta) {
        boolean moved = false;
        if (moveDelta != 0) {
            final int oldScrollerTop = getThumbPosition();
            int position = oldScrollerTop + moveDelta;
            final int contentPos;
            if (position > getMaxThumbPosition()) {
                position = getMaxThumbPosition();
                contentPos = scrollablePanel.getMaximumVerticalScrollPosition();
            } else if (position < getMinThumbPosition()) {
                position = getMinThumbPosition();
                contentPos = scrollablePanel.getMinimumVerticalScrollPosition();
            } else {
                contentPos = calculateContentPosition(position);
            }
            if (position != oldScrollerTop) {
                scrollablePanel.setVerticalScrollPosition(contentPos);
                setTop(position);
                moved = true;
            }
        }
        return moved;
    }

    protected int getMinThumbPosition() {
        return MIN_THUMB_POSITION;
    }

    private class MyNativePreviewHandler implements Event.NativePreviewHandler {
        private final Element documentBody = Document.get().getBody();

        @Override
        public void onPreviewNativeEvent(final Event.NativePreviewEvent event) {// TODO capture every event need to revisit. Nondeterministic behaviour will result if more than one GWT application registers preview handlers. See issue 3892 for details.
            final int type = event.getTypeInt();
            if (type == Event.ONMOUSEDOWN || type == Event.ONMOUSEUP || type == Event.ONMOUSEMOVE
                    || type == Event.ONMOUSEOUT) {
                final NativeEvent nativeEvent = event.getNativeEvent();
                final int button = nativeEvent.getButton();
                if ((button == NativeEvent.BUTTON_LEFT || button == NativeEvent.BUTTON_MIDDLE)
                        && Element.is(nativeEvent.getEventTarget())) {
                    final Element e = Element.as(nativeEvent.getEventTarget());
                    if (type == Event.ONMOUSEDOWN && getElement().isOrHasChild(e)) {
                        onMouseDown(event);
                        event.getNativeEvent().preventDefault();
                    } else if (type == Event.ONMOUSEUP) {
                        onMouseUp();
                        event.getNativeEvent().preventDefault();
                    } else if (type == Event.ONMOUSEMOVE) {
                        onMouseMove(event);
                        event.getNativeEvent().preventDefault();
                    } else if (type == Event.ONMOUSEOUT && e.isOrHasChild(documentBody)) {
                        onMouseOutFromDoc();
                        event.getNativeEvent().preventDefault();
                    } else if (type == Event.ONMOUSEOUT && getElement().isOrHasChild(e)) {
                        onMouseOut();
                        event.getNativeEvent().preventDefault();
                    }
                }
            }
        }
    }
}

package com.ericsson.eniq.events.widgets.client.scroll;

import static com.ericsson.eniq.events.widgets.client.scroll.ScrollConstants.*;

import com.ericsson.eniq.events.widgets.client.scroll.events.CustomScrollEvent.EventType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.user.client.Event.NativePreviewEvent;

/**
 * @author ekurshi
 * @since 14/06/12
 */
public class HorizontalThumb extends VerticalThumb {

    private int thumbPosition;

    private int mouseX = UNDEF;

    HorizontalThumb(final IScrollable scrollable, final ScrollTrack scrollTrack) {
        super(scrollable, scrollTrack);
    }

    @Override
    protected void init() {
        setHeight(THUMB_WIDTH + "px");
        getElement().getStyle().setMarginTop(1, Unit.PX);
    }

    @Override
    protected int getMaxThumbPosition() {
        return scrollablePanel.getMaxHoriThumbPosition();
    }

    @Override
    protected void addMouseWheelHandler() {
    }

    @Override
    protected void onMouseDownOnTrack(final MouseDownEvent event) {
        final int x = event.getX();
        final int scrollerLeft = getThumbPosition();
        if (x < scrollerLeft) { // isAboveScroller
            setContentPosition(scrollablePanel.getHorizontalScrollPosition() - scrollablePanel.getPageWidth());
            fireScrollEvent(EventType.PAGE_CHANGE);
        } else if (x > scrollerLeft + getThumbSize()) { // isBelowScroller
            setContentPosition(scrollablePanel.getHorizontalScrollPosition() + scrollablePanel.getPageWidth());
            fireScrollEvent(EventType.PAGE_CHANGE);
        }
    }

    @Override
    protected int getThumbSize() {
        return scrollablePanel.getHorizontalThumbWidth();
    }

    @Override
    protected int getThumbPosition() {
        return thumbPosition;
    }

    @Override
    public void redraw(final boolean persistPosition) {
        // This height does not include decorations such as border, margin, and padding.
        setWidth(getThumbSize() - (THUMB_BORDER_SIZE * 2) + "px");//TODO need to revisit, when scroll is not needed and this method called
        scrollTrack.setWidth(scrollablePanel.getHorizontalTrackWidth() + "px");
        if (persistPosition) {
            setContentPosition(scrollablePanel.getHorizontalScrollPosition());
        } else {
            resetScroll();
        }
    }

    @Override
    public void resetScroll() {
        scrollablePanel.setHorizontalScrollPosition(0);//TODO should be minimum
        setLeft(0);
    }

    @Override
    public void setContentPosition(int position) {
        int thumbPos;
        if (position > scrollablePanel.getMaximumHorizontalScrollPosition()) {
            position = scrollablePanel.getMaximumHorizontalScrollPosition();
            thumbPos = getMaxThumbPosition();
        } else if (position < scrollablePanel.getMinimumHorizontalScrollPosition()) {
            position = scrollablePanel.getMinimumHorizontalScrollPosition();
            thumbPos = getMinThumbPosition();
        } else {
            thumbPos = calculateThumbPosFromContentPos(position);
        }
        scrollablePanel.setHorizontalScrollPosition(position);
        setLeft(thumbPos);
    }

    private void setLeft(final int position) {
        thumbPosition = position;
        getElement().getStyle().setLeft(position, Unit.PX);
    }

    private int calculateThumbPosFromContentPos(final int contentPosition) {
        final int position = (int) Math.floor(contentPosition * scrollablePanel.getHScrollerFactor());
        return position;
    }

    @Override
    protected void onMouseMove(final NativePreviewEvent event) {
        final int oldMouseX = mouseX;
        mouseX = event.getNativeEvent().getClientX();

        if (isDragging && oldMouseX != UNDEF) {
            final int moveDelta = mouseX - oldMouseX;
            changeScrollerPosition(moveDelta);
        }
    }

    @Override
    protected void onMouseOut() {
        if (!isDragging && !isScrolling) {
            mouseX = UNDEF;
        }
    }

    @Override
    protected void onMouseOutFromDoc() {
        isScrolling = false;
        if (isDragging) {
            isDragging = false;
            fireScrollEvent(EventType.DRAGGED);
        }
        mouseX = UNDEF;
    }

    @Override
    public boolean changeScrollerPosition(final int moveDelta) {
        boolean moved = false;
        if (moveDelta != 0) {
            final int oldScrollerTop = getThumbPosition();
            int position = oldScrollerTop + moveDelta;
            final int contentPos;
            if (position > getMaxThumbPosition()) {
                position = getMaxThumbPosition();
                contentPos = scrollablePanel.getMaximumHorizontalScrollPosition();
            } else if (position < getMinThumbPosition()) {
                position = getMinThumbPosition();
                contentPos = scrollablePanel.getMinimumHorizontalScrollPosition();
            } else {
                contentPos = calculateContentPosition(position);
            }
            if (position != oldScrollerTop) {
                scrollablePanel.setHorizontalScrollPosition(contentPos);
                setLeft(position);
                moved = true;
            }
        }
        return moved;
    }

    @Override
    protected int calculateContentPosition(final int thumbPosition) {
        final int position = (int) Math.floor(thumbPosition / scrollablePanel.getHScrollerFactor());
        return position;
    }
}
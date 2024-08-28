package com.ericsson.eniq.events.widgets.client.slider;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class ColoredBar extends FlowPanel implements IColoredBar {
    private static final int MIN_COLOR_BAR_WIDTH = 10;

    private int minWidth = MIN_COLOR_BAR_WIDTH;

    private int maxWidth;

    private int preResizeWidth;

    @Override
    public int getWidth() {
        return getOffsetWidth();
    }

    @Override
    public int getAbsoluteRight() {
        return getAbsoluteRight();
    }

    @Override
    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(final int minWidth) {
        this.minWidth = minWidth;
    }

    @Override
    public void setBarWidth(final int width) {
        this.preResizeWidth = width;
        setWidth(width + "px");
    }

    @Override
    public void doWestResize(final int deltaX) {
        final int newWidth = preResizeWidth + deltaX;
        this.preResizeWidth = newWidth;
        setBarWidth(newWidth);
    }

    @Override
    public int getMaxWidth() {
        return maxWidth;
    }

    @Override
    public void setMaxWidth(final int maxWidth) {
        this.maxWidth = maxWidth;
    }

}

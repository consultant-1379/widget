package com.ericsson.eniq.events.widgets.client.slider;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public interface IColoredBar {
    int getWidth();

    int getAbsoluteRight();

    int getAbsoluteLeft();

    int getMinWidth();

    void setBarWidth(final int width);

    void doWestResize(int deltaX);

    int getMaxWidth();

    void setMaxWidth(int maxWidth);
}

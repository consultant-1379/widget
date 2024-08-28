/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.scroll;

import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.dom.client.HasScrollHandlers;
import com.google.gwt.user.client.ui.HasScrolling;

/**
 * @author ekurshi
 * @since 2012
 *
 */
public interface IScrollable extends HasScrollHandlers, HasScrolling, HasMouseWheelHandlers {
    /**
     * Height of vertical scroll track
     * 
     * @return height does not includes decorations such as border, margin, and padding.
     */
    int getVerticalTrackHeight();

    /**
     * Height of vertical scroll thumb
     * 
     * @return height includes decorations such as border, margin, and padding.
     */
    int getVerticalThumbHeight();

    int getMaxVertThumbPosition();

    double getVScrollerFactor();

    boolean isVerticalScrollNeeded();

    boolean isHorizontalScrollNeeded();

    int getPageHeight();

    int getPageWidth();

    boolean isArrowEnabled();

    int getHorizontalTrackWidth();

    double getHScrollerFactor();

    int getMaxHoriThumbPosition();

    int getHorizontalThumbWidth();
}

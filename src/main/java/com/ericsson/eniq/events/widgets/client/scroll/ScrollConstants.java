/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.scroll;

/**
 * @author ekurshi
 * @since 2012
 *
 */
public abstract class ScrollConstants {

    public static final int MIN_THUMB_SIZE = 15;// minimal height of scroller (actual for large content)

    public static final int UNDEF = -1;

    public static final int TRACK_BORDER_SIZE = 1;

    public static final int MOUSE_WHEEL_SCROLL_DELTA = 5;

    public static final int THUMB_WIDTH = 10;// without border

    public static final int MIN_THUMB_POSITION = 0;

    public static final int ARROW_HEIGHT = 13;

    public static final int THUMB_BORDER_SIZE = 1;

    public static final int V_LINE_SIZE = 10;

    public static final int H_LINE_SIZE = 10;

    public static final int MIN_VERTICAL_CONTENT_POSITION = 0;

    public static final int MIN_VERTICAL_THUMB_POSITION = 0;

    public static final int MIN_HORIZONTAL_CONTENT_POSITION = 0;

    public static final int MIN_HORIZONTAL_THUMB_POSITION = 0;

    public static int convertToInt(final String size) {
        int num = 0;
        if (size == null || size.isEmpty()) {
            return num;
        }
        final StringBuilder result = new StringBuilder(size.length());
        for (final char c : size.toCharArray()) {
            if (!Character.isDigit(c)) {
                break;
            }
            result.append(c);
        }
        try {
            num = Integer.parseInt(result.toString());
        } catch (final NumberFormatException e) {
            num = 0;
        }
        return num;
    }
}

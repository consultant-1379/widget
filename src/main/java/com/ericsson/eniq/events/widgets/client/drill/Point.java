/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.drill;

/**
 * @author ecarsea
 * @since 2012
 *
 */
public class Point {
    /**
     * The x coordinate of the point
     */
    public int x;

    /**
     * The y coordinate of the point
     */
    public int y;

    /**
     * @param x
     * @param y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return ("x: " + x + ", y: " + y);
    }
}

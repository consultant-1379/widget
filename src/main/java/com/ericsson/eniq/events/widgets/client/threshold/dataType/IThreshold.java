package com.ericsson.eniq.events.widgets.client.threshold.dataType;

import java.util.List;

/**
 * -----------------------------------------------------------------------
 * Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
public interface IThreshold {

    /**
     * Enter the colors used in the threshold.
     * The MIN number of colors = 2 (min number of thresholds)
     * the MAX number of colors = 4 (max number of thresholds)
     * @param colors - variable number of strings (hex colors)
     * @throws Exception - Exception thrown if colors are the same, or MIN > number_of_colors > MAX.
     */
    public void setColor(List<String> colors);

    /**
     * Set the range of the Threshold.
     * @param min
     * @param max
     */
    void setRange(final double min, final double max);

    /**
     * Get all the thresholdItems.
     * @return - A list of ThresholdItems.
     */
    List<IThresholdItem> getThresholdItems();

    /**
     * Get a thresholdItem from it's color.
     * @param color
     * @return
     */

    /**
     * Get a thresholdItem using it's color as a unique identifier.
     * @param color - the Hex Color of the threshold item.
     * @return
     * @throws Exception - if threshold item with supplied color doesn't exist.
     */
    IThresholdItem getThresholdItem(final String color) throws Exception;
}

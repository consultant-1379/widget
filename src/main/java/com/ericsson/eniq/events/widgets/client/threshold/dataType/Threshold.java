/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.threshold.dataType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eeikbe
 * @since 10/2012
 */
public class Threshold implements IThreshold{
    private static double MIN = 2;
    private static double MAX = 4;

    private double min;
    private double max;
    List<IThresholdItem> thresholdItems;

    /**
     * Enter the colors used in the threshold.
     * The MIN number of colors = 2 (min number of thresholds)
     * the MAX number of colors = 4 (max number of thresholds)
     *
     * @param color - variable number of strings (hex colors)
     * @throws Exception - Exception thrown if colors are the same, or MIN > number_of_colors > MAX.
     */
    public Threshold(String... color) {
        assert(color.length >= MIN && color.length <= MAX):"Not the required number of thresholds";

        thresholdItems = new ArrayList<IThresholdItem>(2);

        for (String c: color){
            ThresholdItem thresholdItem = new ThresholdItem();
            thresholdItem.setColor(c);
            thresholdItem.setEnabled(true);
            thresholdItems.add(thresholdItem);
        }
    }

    /**
     * Enter the colors used in the threshold.
     * The MIN number of colors = 2 (min number of thresholds)
     * the MAX number of colors = 4 (max number of thresholds)
     *
     * @param colors - variable number of strings (hex colors)
     * @throws Exception - Exception thrown if colors are the same, or MIN > number_of_colors > MAX.
     */
    @Override
    public void setColor(List<String> colors){
        int i = 0;
        for(IThresholdItem item: thresholdItems){
            item.setColor(colors.get(i++));
        }
    }

    /**
     * Set the range of the Threshold.
     *
     * @param min
     * @param max
     */
    @Override
    public void setRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Get all the thresholdItems.
     * @return - A list of ThresholdItems.
     */
    @Override
    public List<IThresholdItem> getThresholdItems() {
        return thresholdItems;

    }

    /**
     * Get a thresholdItem using it's color as an identifier.
     *
     * @param color
     * @return
     */
    @Override
    public IThresholdItem getThresholdItem(String color) throws Exception{
        IThresholdItem result = null;
        for (IThresholdItem item: thresholdItems){
            if (item.getColor().equalsIgnoreCase(color)){
                result = item;
                break;
            }
        }
        if(result == null){
            throw new Exception("NOT FOUND: Threshold Item with color = "+color);
        }
        return result;
    }
}

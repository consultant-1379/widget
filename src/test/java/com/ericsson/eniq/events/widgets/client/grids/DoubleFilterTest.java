package com.ericsson.eniq.events.widgets.client.grids;

import junit.framework.Assert;

import org.junit.Test;

import com.ericsson.eniq.events.widgets.client.grids.filter.DoubleFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */

public class DoubleFilterTest {

    @Test
    public void testDoubleEqualFilter() {
        final Double[] values = new Double[] { 2035664.7800, 0.1196, 0.6393, 0.3291, 0.1308, 0.9081, 0.0379, 0.4445,
                0.9284, 0.7343, 0.1393, 0.2064, 0.5711, 0.5887, 0.9605, 0.4740, 0.4961, 0.8565, 0.7494, 0.9081 };
        final DoubleFilter equalFilter1 = new DoubleFilter(FilterType.EQUAL, values[1]);
        Assert.assertTrue("Double equality failed.", equalFilter1.doSelect(values[1]));

        final DoubleFilter equalFilter2 = new DoubleFilter(FilterType.EQUAL, values[2]);
        Assert.assertFalse("Double equality failed.", equalFilter2.doSelect(values[3]));

        final DoubleFilter equalFilter3 = new DoubleFilter(FilterType.EQUAL, values[3]);
        Assert.assertFalse("Double equality failed.", equalFilter3.doSelect(values[5]));

        final DoubleFilter equalFilter4 = new DoubleFilter(FilterType.EQUAL, values[4]);
        Assert.assertTrue("Double equality failed.", equalFilter4.doSelect(values[4]));

        final DoubleFilter equalFilter5 = new DoubleFilter(FilterType.EQUAL, values[5]);
        Assert.assertTrue("Double equality failed.", equalFilter5.doSelect(values[19]));
    }

    @Test
    public void testDoubleGreaterThanEqualFilter() {
        final Double[] values = new Double[] { 23.7800, 34345.1196, 0.6393, 12123.3291, 0.1308, 2.9081, 0.0379,
                62365.4445, 0.9284, 34345.1197, 0.1393, 0.2064, 0.5711, 0.5887, 0.6392, 0.4740, 0.4961, 0.8565, 0.7494,
                0.7032 };
        final DoubleFilter equalFilter1 = new DoubleFilter(FilterType.GREATER_THAN_EQUAL, values[0]);
        Assert.assertTrue("Double greater that equal filter failed.", equalFilter1.doSelect(values[0]));

        final DoubleFilter equalFilter2 = new DoubleFilter(FilterType.GREATER_THAN_EQUAL, values[1]);
        Assert.assertFalse("Double greater that equal filter failed.", equalFilter2.doSelect(values[2]));

        final DoubleFilter equalFilter3 = new DoubleFilter(FilterType.GREATER_THAN_EQUAL, values[2]);
        Assert.assertFalse("Double greater that equal filter failed.", equalFilter3.doSelect(values[14]));

        final DoubleFilter equalFilter4 = new DoubleFilter(FilterType.GREATER_THAN_EQUAL, values[1]);
        Assert.assertTrue("Double greater that equal filter failed.", equalFilter4.doSelect(values[9]));

        final DoubleFilter equalFilter5 = new DoubleFilter(FilterType.GREATER_THAN_EQUAL, values[1]);
        Assert.assertTrue("Double greater that equal filter failed.", equalFilter5.doSelect(values[1]));
    }

    @Test
    public void testDoubleLessThanEqualFilter() {
        final double val0 = -0.6392;
        final double val1 = -0.6393;
        final double val2 = 23.7800;
        final double val3 = 12123.3291;
        final double val4 = 34345.1196;
        final double val5 = 34345.1197;
        final DoubleFilter equalFilter1 = new DoubleFilter(FilterType.LESS_THAN_EQUAL, val0);
        Assert.assertTrue("Double less that equal filter failed.", equalFilter1.doSelect(val0));

        final DoubleFilter equalFilter2 = new DoubleFilter(FilterType.LESS_THAN_EQUAL, val1);
        Assert.assertFalse("Double less that equal filter failed.", equalFilter2.doSelect(val0));

        final DoubleFilter equalFilter3 = new DoubleFilter(FilterType.LESS_THAN_EQUAL, val1);
        Assert.assertFalse("Double less that equal filter failed.", equalFilter3.doSelect(val2));

        final DoubleFilter equalFilter4 = new DoubleFilter(FilterType.LESS_THAN_EQUAL, val5);
        Assert.assertTrue("Double less that equal filter failed.", equalFilter4.doSelect(val4));

        final DoubleFilter equalFilter5 = new DoubleFilter(FilterType.LESS_THAN_EQUAL, val3);
        Assert.assertTrue("Double less that equal filter failed.", equalFilter5.doSelect(val1));
    }

    @Test
    public void testDoubleLessThanFilter() {
        final double val0 = 1.1392;
        final double val1 = 1.1393;
        final double val2 = 12123.3291;
        final double val3 = 22323.7800;
        final double val4 = -3433445.1197;
        final double val5 = -3433445.1196;
        final DoubleFilter equalFilter1 = new DoubleFilter(FilterType.LESS_THAN, val0);
        Assert.assertFalse("Double less that filter failed.", equalFilter1.doSelect(val0));

        final DoubleFilter equalFilter2 = new DoubleFilter(FilterType.LESS_THAN, val0);
        Assert.assertFalse("Double less that filter failed.", equalFilter2.doSelect(val1));

        final DoubleFilter equalFilter3 = new DoubleFilter(FilterType.LESS_THAN, val1);
        Assert.assertFalse("Double less that filter failed.", equalFilter3.doSelect(val2));

        final DoubleFilter equalFilter4 = new DoubleFilter(FilterType.LESS_THAN, val5);
        Assert.assertTrue("Double less that filter failed.", equalFilter4.doSelect(val4));

        final DoubleFilter equalFilter5 = new DoubleFilter(FilterType.LESS_THAN, val3);
        Assert.assertTrue("Double less that filter failed.", equalFilter5.doSelect(val1));
    }

    @Test
    public void testDoubleGreaterThanFilter() {
        final double val0 = 3433445.1197;
        final double val1 = 3433445.1196;
        final double val2 = 22323.7800;
        final double val3 = 12123.3291;
        final double val4 = -1.1392;
        final double val5 = -1.1393;

        final DoubleFilter equalFilter1 = new DoubleFilter(FilterType.GREATER_THAN, val0);
        Assert.assertFalse("Double greater that filter failed.", equalFilter1.doSelect(val0));

        final DoubleFilter equalFilter2 = new DoubleFilter(FilterType.GREATER_THAN, val0);
        Assert.assertFalse("Double greater that filter failed.", equalFilter2.doSelect(val1));

        final DoubleFilter equalFilter3 = new DoubleFilter(FilterType.GREATER_THAN, val1);
        Assert.assertFalse("Double greater that filter failed.", equalFilter3.doSelect(val2));

        final DoubleFilter equalFilter4 = new DoubleFilter(FilterType.GREATER_THAN, val5);
        Assert.assertTrue("Double greater that filter failed.", equalFilter4.doSelect(val4));

        final DoubleFilter equalFilter5 = new DoubleFilter(FilterType.GREATER_THAN, val3);
        Assert.assertTrue("Double greater that filter failed.", equalFilter5.doSelect(val1));
    }

}

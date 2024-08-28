package com.ericsson.eniq.events.widgets.client.grids;

import junit.framework.Assert;

import org.junit.Test;

import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.grids.filter.IntegerFilter;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */

public class IntegerFilterTest {

    @Test
    public void testIntegerEqualFilter() {
        final Integer val0 = -66498;
        final Integer val1 = 0;
        final Integer val2 = 101;
        final Integer val3 = 20000;
        final Integer val4 = 99308;
        final Integer val5 = 99308;
        final Integer val6 = 99309;
        final IntegerFilter filter1 = new IntegerFilter(FilterType.EQUAL, val1);
        Assert.assertTrue("Integer equality failed.", filter1.doSelect(val1));

        final IntegerFilter filter2 = new IntegerFilter(FilterType.EQUAL, val3);
        Assert.assertFalse("Integer equality failed.", filter2.doSelect(val6));

        final IntegerFilter filter3 = new IntegerFilter(FilterType.EQUAL, val0);
        Assert.assertFalse("Integer equality failed.", filter3.doSelect(val5));

        final IntegerFilter filter4 = new IntegerFilter(FilterType.EQUAL, val2);
        Assert.assertTrue("Integer equality failed.", filter4.doSelect(val2));

        final IntegerFilter filter5 = new IntegerFilter(FilterType.EQUAL, val4);
        Assert.assertTrue("Integer equality failed.", filter5.doSelect(val5));
    }

    @Test
    public void testIntegerGreaterThanEqualFilter() {
        final Integer val0 = -698;
        final Integer val1 = 0;
        final Integer val2 = 908;
        final Integer val3 = 908;
        final Integer val4 = 1010;
        final Integer val5 = 20000;
        final IntegerFilter filter1 = new IntegerFilter(FilterType.GREATER_THAN_EQUAL, val0);
        Assert.assertTrue("Integer greater than equal filter failed.", filter1.doSelect(val0));

        final IntegerFilter filter2 = new IntegerFilter(FilterType.GREATER_THAN_EQUAL, val4);
        Assert.assertFalse("Integer greater than equal filter failed.", filter2.doSelect(val3));

        final IntegerFilter filter3 = new IntegerFilter(FilterType.GREATER_THAN_EQUAL, val1);
        Assert.assertFalse("Integer greater than equal filter failed.", filter3.doSelect(val0));

        final IntegerFilter filter4 = new IntegerFilter(FilterType.GREATER_THAN_EQUAL, val0);
        Assert.assertTrue("Integer greater than equal filter failed.", filter4.doSelect(val5));

        final IntegerFilter filter5 = new IntegerFilter(FilterType.GREATER_THAN_EQUAL, val2);
        Assert.assertTrue("Integer greater than equal filter failed.", filter5.doSelect(val3));
    }

    @Test
    public void testIntegerLessThanEqualFilter() {
        final Integer val0 = 100;
        final Integer val1 = 101;
        final Integer val2 = 20000;
        final Integer val3 = 66498;
        final Integer val4 = 99308;
        final Integer val5 = 99309;
        final IntegerFilter filter1 = new IntegerFilter(FilterType.LESS_THAN_EQUAL, val0);
        Assert.assertTrue("Integer less than equal filter failed.", filter1.doSelect(val0));

        final IntegerFilter filter2 = new IntegerFilter(FilterType.LESS_THAN_EQUAL, val0);
        Assert.assertFalse("Integer less than equal filter failed.", filter2.doSelect(val1));

        final IntegerFilter filter3 = new IntegerFilter(FilterType.LESS_THAN_EQUAL, val1);
        Assert.assertFalse("Integer less than equal filter failed.", filter3.doSelect(val2));

        final IntegerFilter filter4 = new IntegerFilter(FilterType.LESS_THAN_EQUAL, val5);
        Assert.assertTrue("Integer less than equal filter failed.", filter4.doSelect(val4));

        final IntegerFilter filter5 = new IntegerFilter(FilterType.LESS_THAN_EQUAL, val3);
        Assert.assertTrue("Integer less than equal filter failed.", filter5.doSelect(val1));
    }

    @Test
    public void testIntegerLessThanFilter() {
        final Integer val0 = -698;
        final Integer val1 = 0;
        final Integer val2 = 908;
        final Integer val3 = 908;
        final Integer val4 = 1010;
        final Integer val5 = 20000;

        final IntegerFilter filter1 = new IntegerFilter(FilterType.LESS_THAN, val1);
        Assert.assertTrue("Integer less than filter failed.", filter1.doSelect(val0));

        final IntegerFilter filter2 = new IntegerFilter(FilterType.LESS_THAN, val0);
        Assert.assertFalse("Integer less than filter failed.", filter2.doSelect(val1));

        final IntegerFilter filter3 = new IntegerFilter(FilterType.LESS_THAN, val1);
        Assert.assertFalse("Integer less than filter failed.", filter3.doSelect(val2));

        final IntegerFilter filter4 = new IntegerFilter(FilterType.LESS_THAN, val5);
        Assert.assertTrue("Integer less than filter failed.", filter4.doSelect(val4));

        final IntegerFilter filter5 = new IntegerFilter(FilterType.LESS_THAN, val5);
        Assert.assertTrue("Integer less than filter failed.", filter5.doSelect(val3));
    }

    @Test
    public void testIntegerGreaterThanFilter() {
        final Integer val0 = 3433445;
        final Integer val1 = 3433444;
        final Integer val2 = 22323;
        final Integer val3 = 12123;
        final Integer val4 = 1;
        final Integer val5 = -1;

        final IntegerFilter filter1 = new IntegerFilter(FilterType.GREATER_THAN, val0);
        Assert.assertFalse("Integer greater than filter failed.", filter1.doSelect(val0));

        final IntegerFilter filter2 = new IntegerFilter(FilterType.GREATER_THAN, val0);
        Assert.assertFalse("Integer greater than filter failed.", filter2.doSelect(val1));

        final IntegerFilter filter3 = new IntegerFilter(FilterType.GREATER_THAN, val1);
        Assert.assertFalse("Integer greater than filter failed.", filter3.doSelect(val2));

        final IntegerFilter filter4 = new IntegerFilter(FilterType.GREATER_THAN, val5);
        Assert.assertTrue("Integer greater than filter failed.", filter4.doSelect(val4));

        final IntegerFilter filter5 = new IntegerFilter(FilterType.GREATER_THAN, val3);
        Assert.assertTrue("Integer greater than filter failed.", filter5.doSelect(val1));
    }

}

package com.ericsson.eniq.events.widgets.client.grids;

import junit.framework.Assert;

import org.junit.Test;

import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.grids.filter.StringFilter;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */

public class StringFilterTest {

    @Test
    public void testContainFilter() {
        final String val1 = "hdgbf dg";
        final String val2 = " a dhc12gd";
        final String val3 = "13 g had.hf";
        final String val4 = "befcgbf ghhs";
        final String val5 = "ea, chf,he";

        final StringFilter filter1 = new StringFilter(FilterType.EQUAL, "dg");
        Assert.assertTrue("String filter failed.", filter1.doSelect(val1));

        final StringFilter filter2 = new StringFilter(FilterType.EQUAL, "abc");
        Assert.assertFalse("String filter failed.", filter2.doSelect(val2));

        final StringFilter filter3 = new StringFilter(FilterType.EQUAL, "  ");
        Assert.assertFalse("String filter failed.", filter3.doSelect(val3));

        final StringFilter filter4 = new StringFilter(FilterType.EQUAL, "bed");
        Assert.assertFalse("String filter failed.", filter4.doSelect(val4));

        final StringFilter filter5 = new StringFilter(FilterType.EQUAL, "f ");
        Assert.assertTrue("String filter failed.", filter5.doSelect(val4));

        final StringFilter filter6 = new StringFilter(FilterType.EQUAL, "a,");
        Assert.assertTrue("String filter failed.", filter6.doSelect(val5));
    }

}
package com.ericsson.eniq.events.widgets.client.grids;

import java.text.ParseException;

import junit.framework.Assert;

import org.junit.Test;

import com.ericsson.eniq.events.widgets.client.grids.filter.BooleanFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class BooleanFilterTest {
    @Test
    public void testBooleanEqualFilter() throws ParseException {
        final Boolean val1 = true;
        final Boolean val2 = false;
        final Boolean val3 = true;
        final Boolean val4 = false;
        final BooleanFilter filter1 = new BooleanFilter(FilterType.EQUAL, val3);
        Assert.assertTrue("Boolean equality failed.", filter1.doSelect(val1));

        final BooleanFilter filter2 = new BooleanFilter(FilterType.EQUAL, val2);
        Assert.assertFalse("Boolean equality failed.", filter2.doSelect(val1));

        final BooleanFilter filter3 = new BooleanFilter(FilterType.EQUAL, val1);
        Assert.assertFalse("Boolean equality failed.", filter3.doSelect(val2));

        final BooleanFilter filter5 = new BooleanFilter(FilterType.EQUAL, val4);
        Assert.assertTrue("Boolean equality failed.", filter5.doSelect(val2));
    }
}

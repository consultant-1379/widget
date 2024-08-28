package com.ericsson.eniq.events.widgets.client.grids;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.ericsson.eniq.events.widgets.client.grids.filter.DateFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */

public class DateFilterTest {
    private static String DEFAULT_DATE_FORMAT = "dd/MM/yy hh:mm";

    @Test
    public void testDateEqualFilter() throws ParseException {
        final Date val1 = getDate("13/11/02 00:00");
        final Date val2 = getDate("13/11/02 01:00");
        final Date val3 = getDate("11/06/10 00:00");
        final Date val4 = getDate("23/06/11 00:00");
        final Date val5 = getDate("01/01/13 00:00");
        final Date val6 = getDate("01/10/13 00:00");
        final Date val7 = getDate("10/12/13 00:00");
        final Date val8 = getDate("10/12/13 00:00");

        final DateFilter filter1 = new DateFilter(FilterType.EQUAL, val7);
        Assert.assertTrue("Date equality failed.", filter1.doSelect(val8));

        final DateFilter filter2 = new DateFilter(FilterType.EQUAL, val3);
        Assert.assertFalse("Date equality failed.", filter2.doSelect(val2));

        final DateFilter filter3 = new DateFilter(FilterType.EQUAL, val3);
        Assert.assertFalse("Date equality failed.", filter3.doSelect(val4));

        final DateFilter filter4 = new DateFilter(FilterType.EQUAL, val1);
        Assert.assertFalse("Date equality failed.", filter4.doSelect(val5));

        final DateFilter filter5 = new DateFilter(FilterType.EQUAL, val6);
        Assert.assertTrue("Date equality failed.", filter5.doSelect(val6));
    }

    private Date getDate(final String date) throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        System.err.println(dateFormat.parse(date));
        return dateFormat.parse(date);
    }

    @Test
    public void testDateGreaterThanEqualFilter() throws ParseException {
        final Date val1 = getDate("13/11/02 00:00");
        final Date val2 = getDate("13/11/02 00:01");
        final Date val3 = getDate("11/06/10 00:00");
        final Date val4 = getDate("23/06/11 00:00");
        final Date val5 = getDate("01/01/13 00:00");
        final Date val6 = getDate("01/10/13 00:00");
        final Date val7 = getDate("10/12/13 00:00");
        final Date val8 = getDate("10/12/13 00:00");
        final DateFilter filter1 = new DateFilter(FilterType.GREATER_THAN_EQUAL, val7);
        Assert.assertTrue("Date greater than equal filter failed.", filter1.doSelect(val8));

        final DateFilter filter2 = new DateFilter(FilterType.GREATER_THAN_EQUAL, val2);
        Assert.assertFalse("Date greater than equal filter failed.", filter2.doSelect(val1));

        final DateFilter filter3 = new DateFilter(FilterType.GREATER_THAN_EQUAL, val5);
        Assert.assertFalse("Date greater than equal filter failed.", filter3.doSelect(val4));

        final DateFilter filter4 = new DateFilter(FilterType.GREATER_THAN_EQUAL, val5);
        Assert.assertTrue("Date greater than equal filter failed.", filter4.doSelect(val6));

        final DateFilter filter5 = new DateFilter(FilterType.GREATER_THAN_EQUAL, val2);
        Assert.assertTrue("Date greater than equal filter failed.", filter5.doSelect(val3));

        final DateFilter filter6 = new DateFilter(FilterType.GREATER_THAN_EQUAL, val1);
        Assert.assertTrue("Date greater than equal filter failed.", filter6.doSelect(val2));

        final DateFilter filter7 = new DateFilter(FilterType.GREATER_THAN_EQUAL, val8);
        Assert.assertTrue("Date greater than equal filter failed.", filter7.doSelect(val8));
    }

    @Test
    public void testDateLessThanEqualFilter() throws ParseException {
        final Date val1 = getDate("13/11/02 00:00");
        final Date val2 = getDate("13/11/02 00:01");
        final Date val3 = getDate("11/06/10 00:00");
        final Date val4 = getDate("23/06/11 00:00");
        final Date val5 = getDate("01/01/13 00:00");
        final Date val6 = getDate("01/10/13 00:00");
        final Date val7 = getDate("10/12/13 00:00");
        final Date val8 = getDate("10/12/13 00:00");

        final DateFilter filter1 = new DateFilter(FilterType.LESS_THAN_EQUAL, val7);
        Assert.assertTrue("Date less than equal filter failed.", filter1.doSelect(val8));

        final DateFilter filter2 = new DateFilter(FilterType.LESS_THAN_EQUAL, val1);
        Assert.assertFalse("Date less than equal filter failed.", filter2.doSelect(val2));

        final DateFilter filter3 = new DateFilter(FilterType.LESS_THAN_EQUAL, val4);
        Assert.assertFalse("Date less than equal filter failed.", filter3.doSelect(val5));

        final DateFilter filter4 = new DateFilter(FilterType.LESS_THAN_EQUAL, val6);
        Assert.assertTrue("Date less than equal filter failed.", filter4.doSelect(val5));

        final DateFilter filter5 = new DateFilter(FilterType.LESS_THAN_EQUAL, val3);
        Assert.assertTrue("Date greater than equal filter failed.", filter5.doSelect(val2));

        final DateFilter filter6 = new DateFilter(FilterType.LESS_THAN_EQUAL, val2);
        Assert.assertTrue("Date less than equal filter failed.", filter6.doSelect(val1));

        final DateFilter filter7 = new DateFilter(FilterType.LESS_THAN_EQUAL, val8);
        Assert.assertTrue("Date less than equal filter failed.", filter7.doSelect(val8));
    }

}

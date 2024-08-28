package com.ericsson.eniq.events.widgets.client.grids;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ericsson.eniq.events.widgets.client.grids.data.DataItem;
import com.ericsson.eniq.events.widgets.client.grids.filter.AbstractFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.BooleanFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.DoubleFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterCategory;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterConfigs;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class FilterConfigsTest {
    @Test
    public void testBooleanFilter() {
        final FilterConfigs<BooleanFilter, Boolean> configs1 = new FilterConfigs<BooleanFilter, Boolean>(
                FilterCategory.MATCH_ALL);
        final BooleanFilter filter1 = new BooleanFilter(FilterType.EQUAL, true);
        final List<DataItem> data = DataHelper.generateData(10);
        applyFilter(new BooleanFilter[] { filter1 }, new IAssertionHandler<BooleanFilter, Boolean>() {

            @Override
            public void applyAssertion(final Boolean columnValue, final Boolean filter,
                    final FilterConfigs<BooleanFilter, Boolean> configs) {
                final boolean applyFilters = configs.applyFilters(columnValue);
                final String message = "Boolean filter failed :: Column value : " + columnValue + ", filter applied : "
                        + filter + ", valid : " + !applyFilters;
                Assert.assertFalse(message, columnValue == applyFilters);
            }
        }, configs1, DataHelper.BOOLEAN_COLUMN, data);
        final BooleanFilter filter2 = new BooleanFilter(FilterType.EQUAL, false);
        final FilterConfigs<BooleanFilter, Boolean> configs2 = new FilterConfigs<BooleanFilter, Boolean>(
                FilterCategory.MATCH_ALL);
        applyFilter(new BooleanFilter[] { filter2 }, new IAssertionHandler<BooleanFilter, Boolean>() {

            @Override
            public void applyAssertion(final Boolean columnValue, final Boolean filter,
                    final FilterConfigs<BooleanFilter, Boolean> configs) {
                final boolean applyFilters = configs.applyFilters(columnValue);
                final String message = "Boolean filter failed :: Column value : " + columnValue + ", filter applied : "
                        + filter + ", valid : " + !applyFilters;
                Assert.assertTrue(message, columnValue == applyFilters);
            }
        }, configs2, DataHelper.BOOLEAN_COLUMN, data);

    }

    interface IAssertionHandler<C extends AbstractFilter<T>, T> {
        void applyAssertion(T val, T filter, FilterConfigs<C, T> configs);
    }

    private <C extends AbstractFilter<T>, T> void applyFilter(final C[] filters, final IAssertionHandler<C, T> handler,
            final FilterConfigs<C, T> configs, final String columnID, final List<DataItem> data) {
        for (final C filter : filters) {
            configs.addFilter(filter);
        }
        for (final DataItem dataItem : data) {
            final T columnValue = dataItem.<T> getColumnValue(columnID);
            handler.applyAssertion(columnValue, filters[0].getFilter(), configs);
        }

    }

    public <T> List<DataItem> getData(final String columnID, final T[] items) {
        final List<DataItem> dataItems = new ArrayList<DataItem>();
        for (final T t : items) {
            final DataItem e = new DataItem();
            e.setColumnValue(columnID, t);
            dataItems.add(e);
        }
        return dataItems;
    }

    @Test
    public void testDoubleEqualFilter() {
        final Double[] values = new Double[] { 0.7800, 0.1196, 0.6393, 0.3291, 0.1308, 0.9081, 0.0379, 0.4445, 0.9284,
                0.7343, 0.1393, 0.2064, 0.5711, 0.5887, 0.9605, 0.4740, 0.4961, 0.8565, 0.7494, 0.7032 };

        final double enteredVal = 0.6393;
        final List<DataItem> data = getData(DataHelper.DOUBLE_COLUMN, values);
        final FilterConfigs<DoubleFilter, Double> configs1 = new FilterConfigs<DoubleFilter, Double>(
                FilterCategory.MATCH_ALL);
        final DoubleFilter equalFilter = new DoubleFilter(FilterType.EQUAL, enteredVal);
        applyFilter(new DoubleFilter[] { equalFilter }, new IAssertionHandler<DoubleFilter, Double>() {

            @Override
            public void applyAssertion(final Double columnValue, final Double filter,
                    final FilterConfigs<DoubleFilter, Double> configs) {
                final boolean invalid = configs.applyFilters(columnValue);
                final String message = "Double filter failed :: Column value : " + columnValue + ", filter applied : "
                        + filter + ", valid : " + !invalid;
                if (columnValue.doubleValue() == enteredVal) {
                    Assert.assertFalse(message, invalid);
                } else {
                    Assert.assertTrue(message, invalid);
                }
            }
        }, configs1, DataHelper.DOUBLE_COLUMN, data);
    }

    @Test
    public void testDoubleLessThanFilter() {
        final Double[] values = new Double[] { 0.7800, 0.1196, 0.6393, 0.3291, 0.1308, 0.9081, 0.0379, 0.4445, 0.9284,
                0.7343, 0.1393, 0.2064, 0.5711, 0.5887, 0.9605, 0.4740, 0.4961, 0.8565, 0.7494, 0.7032 };

        //MATCH_ALL
        final double enteredVal = 0.9605;
        final List<DataItem> data = getData(DataHelper.DOUBLE_COLUMN, values);
        final FilterConfigs<DoubleFilter, Double> configs1 = new FilterConfigs<DoubleFilter, Double>(
                FilterCategory.MATCH_ALL);
        final DoubleFilter greaterThanEqualFilter = new DoubleFilter(FilterType.GREATER_THAN_EQUAL, enteredVal);
        //        final DoubleFilter lessThanEqualFilter = new DoubleFilter(FilterType.LESS_THAN_EQUAL, enteredVal);
        //        final DoubleFilter greaterThanFilter = new DoubleFilter(FilterType.GREATER_THAN, enteredVal);
        //        final DoubleFilter lessThanFilter = new DoubleFilter(FilterType.LESS_THAN, enteredVal);
        applyFilter(new DoubleFilter[] { greaterThanEqualFilter }, new IAssertionHandler<DoubleFilter, Double>() {

            @Override
            public void applyAssertion(final Double columnValue, final Double filter,
                    final FilterConfigs<DoubleFilter, Double> configs) {
                final boolean invalid = configs.applyFilters(columnValue);
                final String message = "Double filter failed :: Column value : " + columnValue + ", filter applied : "
                        + filter + ", valid : " + !invalid;
                if (columnValue.doubleValue() >= enteredVal) {
                    System.out.println("pass : " + columnValue);
                    Assert.assertFalse(message, invalid);
                } else {
                    System.err.println("fail : " + columnValue);
                    Assert.assertTrue(message, invalid);
                }
            }
        }, configs1, DataHelper.DOUBLE_COLUMN, data);
    }

    public void testIntergerFilter() {
        //MATCH_ALL
        //MATCH_ANY
    }

    public void testStringFilter() {
        //MATCH_ALL
        //MATCH_ANY
    }

    public void testDateFilter() {
        //MATCH_ALL
        //MATCH_ANY
    }
}

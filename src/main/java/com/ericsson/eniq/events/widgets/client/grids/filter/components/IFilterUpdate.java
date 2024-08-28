package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import com.ericsson.eniq.events.widgets.client.grids.filter.AbstractFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterConfigs;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 * @param <C>
 * @param <T>
 */
public interface IFilterUpdate<C extends AbstractFilter<T>, T> {
    void onFilterUpdated(FilterConfigs<C, T> filterConfigs);
}

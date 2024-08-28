package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import java.util.List;

import com.ericsson.eniq.events.widgets.client.grids.filter.AbstractFilter;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterCategory;
import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 * @param <C>
 * @param <T>
 */
public interface IFilterApplyHandler<C extends AbstractFilter<T>, T> {
    void onFilterApplyClick(final List<FilterType> filterTypes, List<T> filtersList, FilterCategory category);
}

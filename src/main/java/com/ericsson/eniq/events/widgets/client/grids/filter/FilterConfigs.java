package com.ericsson.eniq.events.widgets.client.grids.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 * @param <F>
 * @param <T>
 */
public class FilterConfigs<F extends AbstractFilter<T>, T> {

    private final List<F> filters;

    private final FilterCategory category;

    public FilterConfigs(final FilterCategory category) {
        this.category = category;
        filters = new ArrayList<F>();
    }

    public void addFilter(final F filter) {
        filters.add(filter);
    }

    public boolean applyFilters(final T value) {
        boolean valid = false;
        if (category == FilterCategory.MATCH_ANY) {
            for (final F filter : filters) {
                valid = isValid(value, filter);
                if (valid) {
                    break;
                }
            }
        } else {
            for (final F filter : filters) {
                valid = isValid(value, filter);
                if (!valid) {
                    break;
                }
            }
        }
        return !valid;
    }

    private boolean isValid(final T value, final F filter) {
        boolean valid = false;
        try {
            valid = filter.doSelect(value);
        } catch (final Exception e) {//not filtering null values
            //            e.printStackTrace();
        }
        return valid;
    }

    public FilterCategory getCategory() {
        return category;
    }

    public List<F> getFilters() {
        return filters;
    }

    public boolean isEmpty() {
        return filters.isEmpty();
    }
}

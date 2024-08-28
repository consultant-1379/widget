package com.ericsson.eniq.events.widgets.client.grids.filter;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class BooleanFilter extends AbstractFilter<Boolean> {
    public BooleanFilter(final FilterType type, final Boolean filter) {
        super(type, filter);
    }

    @Override
    public boolean doSelect(final Boolean value) {
        return value == getFilter();
    }

    @Override
    public String toString() {
        return String.valueOf(getFilter());
    }
}

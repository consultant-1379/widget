package com.ericsson.eniq.events.widgets.client.grids.filter;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class StringFilter extends AbstractFilter<String> {
    public StringFilter(final FilterType type, final String filter) {
        super(type, filter);
    }

    @Override
    public boolean doSelect(final String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return value.toLowerCase().contains(getFilter().toLowerCase());
    }

    @Override
    public String toString() {
        return getFilter();
    }
}

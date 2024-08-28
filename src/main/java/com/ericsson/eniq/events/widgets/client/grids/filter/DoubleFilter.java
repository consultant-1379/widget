package com.ericsson.eniq.events.widgets.client.grids.filter;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class DoubleFilter extends AbstractFilter<Double> {
    public DoubleFilter(final FilterType type, final Double filter) {
        super(type, filter);
    }

    @Override
    public boolean doSelect(final Double value) {
        switch (getType()) {
        case EQUAL:
            return value.compareTo(getFilter()) == 0;
        case LESS_THAN:
            return value.compareTo(getFilter()) < 0;
        case GREATER_THAN:
            return value.compareTo(getFilter()) > 0;
        case LESS_THAN_EQUAL:
            return value.compareTo(getFilter()) <= 0;
        case GREATER_THAN_EQUAL:
            return value.compareTo(getFilter()) >= 0;
        default:
            return false;
        }
    }

    @Override
    public String toString() {
        return getType().getSymbol() + " " + String.valueOf(getFilter());
    }
}

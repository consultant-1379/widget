package com.ericsson.eniq.events.widgets.client.grids.filter;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class DateFilter extends AbstractFilter<Date> {
    public static final String DD_MM_YY_MM_HH = "dd/MM/yy HH:mm";

    public DateFilter(final FilterType type, final Date filter) {
        super(type, filter);
    }

    @Override
    public boolean doSelect(final Date value) {
        switch (getType()) {
        case EQUAL:
            return value.getTime() == getFilter().getTime();
        case LESS_THAN_EQUAL:
            return value.getTime() <= getFilter().getTime();
        case GREATER_THAN_EQUAL:
            return value.getTime() >= getFilter().getTime();
        default:
            return false;
        }
    }

    @Override
    public String toString() {
        return DateTimeFormat.getFormat(DD_MM_YY_MM_HH).format(getFilter());
    }
}

package com.ericsson.eniq.events.widgets.client.grids.filter;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 * @param <T>
 */
public abstract class AbstractFilter<T> {
    private final FilterType type;

    private final T filter;

    public AbstractFilter(final FilterType type, final T filter) {
        this.type = type;
        this.filter = filter;
    }

    abstract boolean doSelect(T value);

    public T getFilter() {
        return filter;
    }

    public FilterType getType() {
        return type;
    }

}

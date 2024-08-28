package com.ericsson.eniq.events.widgets.client.grids.filter;

import com.ericsson.eniq.events.widgets.client.grids.filter.configs.SymbolResourceHelper;

/**
 *
 * @author ekurshi
 * @since 2013
 *
 */
public enum FilterType {

    GREATER_THAN(">"), EQUAL("="), LESS_THAN("<"), GREATER_THAN_EQUAL(">="), LESS_THAN_EQUAL("<=");
    public static FilterType getType(final String symbol) {
        for (final FilterType type : FilterType.values()) {
            if (type.getSymbol().equals(symbol)) {
                return type;
            }
        }
        return null;
    }

    private String symbol;

    private FilterType(final String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getStyle() {
        return SymbolResourceHelper.getStyle(this);
    }
}

package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import java.util.HashMap;
import java.util.Map;

import com.ericsson.eniq.events.widgets.client.grids.filter.FilterType;
import com.ericsson.eniq.events.widgets.client.grids.filter.configs.SymbolsResourceBundle.Style;
import com.google.gwt.core.client.GWT;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class SymbolResourceHelper {
    private static final SymbolsResourceBundle resourceBundle = GWT.create(SymbolsResourceBundle.class);

    private static final Map<FilterType, String> styleMap = new HashMap<FilterType, String>();
    static {
        final Style style = resourceBundle.style();
        style.ensureInjected();
        styleMap.put(FilterType.GREATER_THAN, style.greaterThanNormal());
        styleMap.put(FilterType.EQUAL, style.equalsNormal());
        styleMap.put(FilterType.LESS_THAN, style.lessThanNormal());
        styleMap.put(FilterType.GREATER_THAN_EQUAL, style.greaterThanEqualNormal());
        styleMap.put(FilterType.LESS_THAN_EQUAL, style.lessThanEqualNormal());
    }

    public static String getStyle(final FilterType type) {
        return styleMap.get(type);
    }

    public static SymbolsResourceBundle getResourcebundle() {
        return resourceBundle;
    }
}

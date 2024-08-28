package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public interface SymbolsResourceBundle extends ClientBundle {

    interface Style extends CssResource {
        String equalsNormal();

        String greaterThanEqualNormal();

        String greaterThanNormal();

        String lessThanEqualNormal();

        String lessThanNormal();
        
        String disableRemoveFilter();
        
        String enableRemoveFilter();

    }

    @Source("equals_normal.png")
    ImageResource equalsNormal();

    @Source("greater_equal_normal.png")
    ImageResource greaterThanEqualNormal();

    @Source("greater_than_normal.png")
    ImageResource greaterThanNormal();

    @Source("less_equal_normal.png")
    ImageResource lessThanEqualNormal();

    @Source("less_than_normal.png")
    ImageResource lessThanNormal();

    @Source("equals_hover.png")
    ImageResource equalsHover();

    @Source("greater_equal_hover.png")
    ImageResource greaterThanEqualHover();

    @Source("greater_than_hover.png")
    ImageResource greaterThanHover();

    @Source("less_equal_hover.png")
    ImageResource lessThanEqualHover();

    @Source("less_than_hover.png")
    ImageResource lessThanHover();

    @Source("plus_normal.png")
    ImageResource addFilter();

    @Source("minus_normal.png")
    ImageResource removeFilter();

    @Source("apply_normal.png")
    ImageResource applyNormal();

    @Source("Symbols.css")
    Style style();
}

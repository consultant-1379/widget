package com.ericsson.eniq.events.widgets.client.grids;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Style;

public interface GWTGridResource extends CellTable.Resources {
    @Override
    @Source("GWTGrid.css")
    Style cellTableStyle();

    @Source("gridBackgroundStripe.png")
    ImageResource gridBackgroundStripe();

    @Source("GWTGridBackground.css")
    GridStyle gridBackgroundStyle();

    public interface GridStyle extends CssResource {
        String gridStripeBackground();
    }
}

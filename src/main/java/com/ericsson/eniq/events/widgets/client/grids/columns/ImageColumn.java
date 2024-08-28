package com.ericsson.eniq.events.widgets.client.grids.columns;

import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.user.cellview.client.Column;

/**
 * 
 * @author ekurshi
 *
 * @param <T>
 */
public class ImageColumn<T> extends Column<T, String> {

    public ImageColumn() {
        super(new ImageCell());
    }

    @Override
    public String getValue(final T object) {
        return null;
    }
}

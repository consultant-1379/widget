package com.ericsson.eniq.events.widgets.client.grids.headers;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public interface GridHeaderResourceBundle extends ClientBundle {

    interface Style extends CssResource {
        String sortImage();

        String sortImageAsc();

        String sortImageDesc();
    }

    @Source("sort_asc.gif")
    ImageResource sortAsc();

    @Source("sort_desc.gif")
    ImageResource sortDesc();

    @Source("GridHeader.css")
    Style style();
}
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
public interface FilterConfigResourceBundle extends ClientBundle {

    interface Style extends CssResource {
        String container();

        String headerLabel();

        String footer();

        String body();

        String windowTitle();

        String categoryDropDown();

        String headerContainer();

        String filterContainer();

        String button();

        String buttonBar();
        
        String header();
        
        String closeButton();
        
        String configureImage();
        
        String headerImages();
    }

    @Source("FilterConfig.css")
    Style style();

    @Source("configure_normal.png")
    ImageResource configureNormal();

    @Source("close_normal.png")
    ImageResource closeNormal();
}

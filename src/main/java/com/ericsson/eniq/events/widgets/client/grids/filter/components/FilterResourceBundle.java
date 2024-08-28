package com.ericsson.eniq.events.widgets.client.grids.filter.components;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public interface FilterResourceBundle extends ClientBundle {

    interface Style extends CssResource {
        String appliedFilterLabel();

        String applyImage();

        String applyImageDisabled();

        String configureDisabled();

        String configureImage();

        String container();

        String filterAppliedButton();

        String filterBox();

        String filterIcon();

        String filterPopUp();

        String filterCell();
        
        String invalidText();
    }

    @Source("apply_hover.png")
    ImageResource applyHover();

    @Source("apply_normal.png")
    ImageResource applyNormal();

    @Source("configure_normal.png")
    ImageResource configureNormal();
    
    @Source("info_icon.png")
    ImageResource infoIcon();

    @Source("FilterPanel.css")
    Style style();
}
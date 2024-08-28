package com.ericsson.eniq.events.widgets.client.slider;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public interface SlideBarResourceBundle extends ClientBundle {

    @Source("SlideBar.css")
    SlideBarStyle style();

    @Source("images/drag_point_click.png")
    ImageResource slidingThumbClick();

    @Source("images/drag_point_hover.png")
    ImageResource slidingThumbHover();

    @Source("images/drag_point_normal.png")
    ImageResource slidingThumbNormal();

    public interface SlideBarStyle extends CssResource {

        String slidingThumb();

        String coloredBar();

        String slideGroup();
    }
}

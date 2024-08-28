/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.launch.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * @author ecarsea
 * @since 2011
 *
 */
public interface LaunchResourceBundle extends ClientBundle {

    @Source("images/A_launch1.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource handleOpen();

    @Source("images/A_launch1.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource handleClose();

    @Source("images/arrow_across.png")
    ImageResource arrow_across();

    @Source("images/arrow_down.png")
    ImageResource arrow_down();

    @Source("css/LaunchMenu.css")
    LaunchViewStyle style();

    interface LaunchViewStyle extends CssResource {
        String container();

        String inner();

        String header();

        String resizer();

        String footer();

        String handle();

        String open();

        String stackHeader();

        String icon();
    }
}

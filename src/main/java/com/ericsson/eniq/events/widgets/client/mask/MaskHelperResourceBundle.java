package com.ericsson.eniq.events.widgets.client.mask;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface MaskHelperResourceBundle extends ClientBundle {

    @Source("images/nightRider.gif")
    ImageResource nightRider();

    @Source("images/nightRiderBackground.png")
    ImageResource nightRiderBackground();

    @Source("MaskHelper.css")
    MaskHelperStyle style();

    interface MaskHelperStyle extends CssResource {

        String portletMaskBackground();

        String portletMask();

        String portletMaskText();
    }

}

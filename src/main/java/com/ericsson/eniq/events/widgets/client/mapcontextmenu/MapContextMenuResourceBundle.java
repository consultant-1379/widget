package com.ericsson.eniq.events.widgets.client.mapcontextmenu;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface MapContextMenuResourceBundle extends ClientBundle {

   @Source("MapContextMenu.css")
   OptionsMenuStyle style();

    @Source("images/context_arrow_down.png")
    ImageResource arrow_down();

    @Source("images/context_arrow_up.png")
    ImageResource arrow_up();

    @Source("images/context_arrow_down_disabled.png")
    ImageResource arrow_down_disabled();

    @Source("images/context_arrow_up_disabled.png")
    ImageResource arrow_up_disabled();

   interface OptionsMenuStyle extends CssResource {

       String popupPanel();

       String middleGrid();

       String arrowDownIcon();

       String arrowUpIcon();

       String arrowDownDisabledIcon();

       String arrowUpDisabledIcon();

       String grid();

       String gridRow();

       String oddRow();

       String scrollButton();
   }
}

package com.ericsson.eniq.events.widgets.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface WidgetsResourceBundle extends ClientBundle {

    // info, warning and error icons for message panels
    @Source("_images/icons/green_dialog_icon.png")
    ImageResource infoMessageIcon();

    @Source("_images/icons/red_dialog_icon.png")
    ImageResource errorMessageIcon();

    @Source("_images/icons/orange_dialog_icon.png")
    ImageResource warningMessageIcon();

    @Source("_images/icons/green_drilldialog_icon.png")
    ImageResource DrillDownIcon();

    /* Close icon used for dialog panels */
    @Source("_images/icons/close_normal.png")
    ImageResource closeIcon();

    @Source("_images/icons/close_hover.png")
    ImageResource closeIconHover();

    @Source("_images/icons/maximize.png")
    ImageResource maximizeButton();

    @Source("_images/icons/restore.png")
    ImageResource restoreButton();

    @Source("_images/icons/min.png")
    ImageResource minimizeButton();

    @Source("_images/icons/print.png")
    ImageResource printIcon();

    @Source("_images/icons/print_hover.png")
    ImageResource printHover();

    @Source("_images/icons/print_disabled.png")
    ImageResource printDisable();

    @Source("_images/icons/print_preview.png")
    ImageResource printPreviewIcon();

    @Source("_images/icons/print_preview_hover.png")
    ImageResource printPreviewHover();

    @Source("_images/icons/print_preview_disabled.png")
    ImageResource printPreviewDisable();

    @Source("_images/icons/export_to.png")
    ImageResource exportIcon();

    @Source("_images/icons/export_to_hover.png")
    ImageResource exportHover();

    @Source("_images/icons/export_to_disabled.png")
    ImageResource exportDisable();

    @Source("_images/icons/arrow_down.png")
    ImageResource arrowDown();

    @Source("_images/icons/arrow_up.png")
    ImageResource arrowUp();

    @Source("_images/icons/refresh.png")
    ImageResource refreshIcon();

    @Source("_images/icons/refresh_hover.png")
    ImageResource refreshHover();

    @Source("_images/icons/refresh_disabled.png")
    ImageResource refreshDisable();
}

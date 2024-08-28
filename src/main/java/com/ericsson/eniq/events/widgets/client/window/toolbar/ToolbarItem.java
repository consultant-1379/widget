package com.ericsson.eniq.events.widgets.client.window.toolbar;

import com.ericsson.eniq.events.widgets.client.button.ImageButton;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class ToolbarItem extends AbstractToolbarItem {
    private final ImageButton image;

    public ToolbarItem(final String id, final ClickHandler handler, final ImageResource imageResource) {
        super(id);
        image = new ImageButton(imageResource);
        image.addClickHandler(handler);
        final int height = imageResource.getHeight();
        image.setSize(imageResource.getWidth() + "px", height + "px");
    }

    @Override
    public Widget asWidget() {
        return image;
    }

    @Override
    public void setEnable(final boolean enabled) {
        image.setEnabled(enabled);
    }

    @Override
    public void setHidden(final boolean hidden) {
        image.setVisible(!hidden);

    }

    @Override
    public boolean isHidden() {
        return image.isVisible();
    }

    @Override
    public boolean isEnabled() {
        return image.isEnabled();
    }

    @Override
    public void setToolTip(final String toolTip) {
        image.setTitle(toolTip);
    }

    public void setHoverImage(final ImageResource hoverImage) {
        image.setHoverImage(hoverImage);
    }

    public void setDisabledImage(final ImageResource disabledImage) {
        image.setDisabledImage(disabledImage);
    }

    @Override
    public int getWidth() {
        return image.getOffsetWidth();
    }

    @Override
    public int getHeight() {
        return image.getOffsetHeight();
    }

}

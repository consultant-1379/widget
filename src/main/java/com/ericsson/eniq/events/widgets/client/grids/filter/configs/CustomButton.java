package com.ericsson.eniq.events.widgets.client.grids.filter.configs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */

public class CustomButton extends Composite {

    public interface ResourceBundle extends ClientBundle {

        interface Style extends CssResource {
            String closeButton();

            String equalsNormal();

            String innerHTMLContainer();

            String wrapper();

        }

        @Source("close_hover.png")
        ImageResource closeHover();

        @Source("close_normal.png")
        ImageResource closeNormal();

        @Source("equals_normal.png")
        ImageResource equalsNormal();

        @Source("CustomButton.css")
        Style style();

    }

    private static int CLOSE_IMAGE_WIDTH = 17;

    private static int BUTTON_PADDING = 6;/*1+2+2+1*/

    private Image closeButton;

    private final HTML innerHTMLContainer;

    private final FlowPanel wrapper;

    private static final ResourceBundle bundle = GWT.create(ResourceBundle.class);

    public CustomButton() {
        innerHTMLContainer = new HTML();
        wrapper = new FlowPanel();
        bundle.style().ensureInjected();
        wrapper.addStyleName(bundle.style().wrapper());
        wrapper.add(innerHTMLContainer);
        innerHTMLContainer.addStyleName(bundle.style().innerHTMLContainer());
        initWidget(wrapper);
    }

    public CustomButton(final boolean closeButtonVisible) {
        this();
        if (closeButtonVisible) {
            closeButton = new Image(bundle.closeNormal());
            closeButton.addStyleName(bundle.style().closeButton());
            wrapper.add(closeButton);
        }

    }

    public HandlerRegistration addClickHandler(final ClickHandler handler) {
        return innerHTMLContainer.addClickHandler(handler);
    }

    public HandlerRegistration addCloseClickHandler(final ClickHandler handler) {
        return closeButton.addClickHandler(handler);
    }

    public void setHeight(final int height) {
        final int availableHeigth = height - BUTTON_PADDING;
        wrapper.setHeight(availableHeigth + "px");
    }

    public void setHTML(final SafeHtml html) {
        innerHTMLContainer.setHTML(html);
        //                IMAGE_TEMPLATE.label(bundle.style().equalsNormal(), bundle.style().label(), "Custom"));
    }

    public void setWidth(final int width) {
        final int availableWidth = width - BUTTON_PADDING;
        wrapper.setWidth(availableWidth + "px");
        innerHTMLContainer.setWidth((closeButton == null ? availableWidth : (availableWidth - CLOSE_IMAGE_WIDTH))
                + "px");
    }
}

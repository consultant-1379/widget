package com.ericsson.eniq.events.widgets.client.window.title;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface TitleWindowResourceBundle extends ClientBundle {

    @Source("images/cog.png")
    ImageResource arrowForActionMenu();

    @Source("images/add_content_normal.png")
    ImageResource add();

    @Source("images/add_content_hover.png")
    ImageResource addHover();

    @Source("images/add_content_click.png")
    ImageResource addClick();

    @Source("images/x.png")
    ImageResource closeButton();

    @Source("images/maximise.png")
    ImageResource maximiseButton();

    @Source("images/restore.png")
    ImageResource restoreButton();

    @Source("TitleWindow.css")
    PortletWindowStyle style();

    interface PortletWindowStyle extends CssResource {

        String title();

        String noButton();

        String portlet();

        String bodyContainer();

        String dragHandle();

        String topContainer();
        
        String topContainerNotDraggable();

        String topMapContainer();

        String placeHolder();

        String inner();

        String outer();

        String closeButtonContainer();

        String addButton();

        /*
           * Used in GWT dnd project, but needed here to override GWT-Dnd default
           * visual look
           */
        @SuppressWarnings("UnusedDeclaration")
        @ClassName("dragdrop-selected")
        String dragdropSelected();

        @SuppressWarnings("UnusedDeclaration")
        @ClassName("dragdrop-proxy")
        String dragdropProxy();

        @SuppressWarnings("UnusedDeclaration")
        @ClassName("dragdrop-dragging")
        String dragdropDragging();
    }

}

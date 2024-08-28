package com.ericsson.eniq.events.widgets.client.window;

import com.ericsson.eniq.events.widgets.client.button.ImageButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class MinimizeWindowWidget extends Composite {

    private static final MinimizeWindowWidgetUiBinder UI_BINDER = GWT.create(MinimizeWindowWidgetUiBinder.class);

    public static int WINDOW_WIDTH = 200; //private -> protected : want to change based on title content 

    private static final int PADDING = 5; //padding between minimised windows

	private static final double LEFT_MARGIN_NOICON = 6;

    @UiField
    FocusPanel handle;

    @UiField
    Label windowTitle;

    @UiField
    ImageButton closeIcon;

    @UiField
    Image headerIcon;

    @UiField
    ImageButton maximizeRestoreButton;

    private final Handlers handlers;

    private static int leftPosition = PADDING; //magic way of lining up minimised windows LtR

    public MinimizeWindowWidget(final String title, final Handlers handlers) {
        this.handlers = handlers;
        initWidget(UI_BINDER.createAndBindUi(this));
        setWidth(WINDOW_WIDTH + "px");
        windowTitle.setText(title);
        windowTitle.setTitle(title);
        handle.addDoubleClickHandler(new DoubleClickHandler() {

            @Override
            public void onDoubleClick(final DoubleClickEvent event) {
                close();
                handlers.onRestoreClick();
            }
        });
        getElement().getStyle().setLeft(leftPosition, Unit.PX);
        headerIcon.getElement().getParentElement().getStyle().setDisplay(Display.NONE);
        leftPosition = leftPosition + WINDOW_WIDTH + PADDING;
        windowTitle.asWidget().getElement().getStyle().setMarginLeft(LEFT_MARGIN_NOICON, Unit.PX); //Previously had no Margin when icon was not specified
    }

    public MinimizeWindowWidget(final String title, final Handlers handlers, final ImageResource iconResource) {
        this(title, handlers);
        final Element element = headerIcon.getElement();
        element.getParentElement().getStyle().clearDisplay();
        element.getStyle().setDisplay(Display.BLOCK);//TODO not working, need to check
        headerIcon.setResource(iconResource);

    }

    /** Set the title (text) and tooltip (title) */
    public void setTitle(String newTitle) {
        windowTitle.setText(newTitle);
        windowTitle.setTitle(newTitle);
    }
    
    void close() {
    	removeFromParent();
    }

    @UiHandler("closeIcon")
    void onCloseClick(final ClickEvent event) {
        close();
        handlers.onCloseClick();
    }

    @UiHandler("maximizeRestoreButton")
    void onMaximizeRestoreClick(final ClickEvent event) {
        close();
        handlers.onRestoreClick();
    }

    public interface Handlers {
        void onCloseClick();

        void onRestoreClick();
    }

    interface MinimizeWindowWidgetUiBinder extends UiBinder<Widget, MinimizeWindowWidget> {
    }

}

package com.ericsson.eniq.events.widgets.client.window.title;

import com.ericsson.eniq.events.widgets.client.component.ComponentMessagePanel;
import com.ericsson.eniq.events.widgets.client.component.ComponentMessageType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Simplified version of PortletWindow from Eniq Events.
 * Can be used as container for any type of widget.
 * Can resize it's content (GWT or GXT) based on native browser events.
 * <p/>
 * !IMPORTANT!
 * TitleWindow is sharing resources with PortletWindow e.g. images, css etc.
 * Be sure not to delete any resources used by TitleWindow.
 *
 * @author evyagrz
 * @since 01 2012
 */
public class TitleWindow extends Composite implements ITitleWindow {

    private static TitleWindowUiBinder uiBinder = GWT.create(TitleWindowUiBinder.class);

    public static final int PADDINGS_HEIGHT = 5 + 5; // there are 2 paddings - upper (5) and bottom (5)

    public static final int BORDERS_HEIGHT = 1 + 1; // there are 2 borders - upper (1) and bottom (1)

    public static final int WINDOWS_TITLE_HEIGHT = 21 + 1; // title height (21) + bottom border of title (1)

    @UiField(provided = true)
    TitleWindowResourceBundle resourceBundle = TitleWindowResourceBundleHelper.getBundle();

    HTML titleLabel;

    SimplePanel closeButtonContainer;

    @UiField
    SimplePanel bodyContainer;

    @UiField
    SimplePanel messageContainer;

    SimplePanel optionButton;

    @UiField
    SimplePanel headerContainer;

    HorizontalPanel topContainer;

    private final boolean isTitleVisible;

    private final boolean isDraggable; //default = true;

    public TitleWindow(final int height) {
        this(null, height);
    }

    public TitleWindow(final String title, final int height, final boolean isDraggable) {
        initWidget(uiBinder.createAndBindUi(this));
        isTitleVisible = title != null;
        this.isDraggable = isDraggable;
        if (isTitleVisible) {
            configureTopContainer(title);
        }
        setHeight(height);
    }

    public TitleWindow(final String title, final int height) {
        initWidget(uiBinder.createAndBindUi(this));
        isTitleVisible = title != null;
        this.isDraggable = true;
        if (isTitleVisible) {
            configureTopContainer(title);
        }
        setHeight(height);
    }

    private void configureTopContainer(final String title) {
        topContainer = new HorizontalPanel();
        headerContainer.add(topContainer);
        if (isDraggable) {
            topContainer.setStyleName(resourceBundle.style().topContainer());
        } else {
            topContainer.setStyleName(resourceBundle.style().topContainerNotDraggable());
        }
        topContainer.addStyleName(resourceBundle.style().noButton());
        optionButton = new SimplePanel();
        topContainer.add(optionButton);
        final FocusPanel w = new FocusPanel();
        titleLabel = new HTML(title);
        titleLabel.setStyleName(resourceBundle.style().title());
        w.add(titleLabel);
        topContainer.add(w);
        closeButtonContainer = new SimplePanel();
        closeButtonContainer.setStyleName(resourceBundle.style().closeButtonContainer());
        topContainer.add(closeButtonContainer);
        closeButtonContainer.getElement().getParentElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
        closeButtonContainer.getElement().getParentElement().getStyle().setWidth(17, Unit.PX);
    }

    @Override
    public void setTitle(final String title) {
        titleLabel.setHTML(SafeHtmlUtils.fromSafeConstant(title));
    }

    public void setBody(final Widget body) {
        bodyContainer.setWidget(body);
    }

    /**
     * @param height total height of window including content, paddings, border, and window title, etc.
     */
    public void setHeight(final int height) {
        int contentHeight = height - (BORDERS_HEIGHT + PADDINGS_HEIGHT);
        if (isTitleVisible) {
            contentHeight = contentHeight - WINDOWS_TITLE_HEIGHT;
        }
        bodyContainer.setHeight(contentHeight + "px");
        messageContainer.setHeight(contentHeight + "px");
    }

    @Override
    public void setHeight(String height) {
        if (height != null && !height.isEmpty()) {
            if ("auto".equalsIgnoreCase(height)) { // If auto, then just set auto without px at the end
                bodyContainer.setHeight(height);
                messageContainer.setHeight(height);
            } else {
                height = height.replace("px", ""); // We assume that height is in pixels, so we remove pixels part
                setHeight(Integer.parseInt(height, 10));
            }
        }
    }

    @Override
    public void setMessage(final ComponentMessageType type, final String title, final String description) {
        bodyContainer.setVisible(false);
        messageContainer.setVisible(true);

        final ComponentMessagePanel messagePanel = new ComponentMessagePanel();
        messagePanel.populate(type, title, description);

        messageContainer.setWidget(messagePanel);
    }

    @Override
    public boolean hasMessage() {
        return messageContainer.isVisible();
    }

    @Override
    public void clearMessage() {
        messageContainer.clear();

        bodyContainer.setVisible(true);
        messageContainer.setVisible(false);
    }

    public void addOptionsButton(final ClickHandler handler) {
        final ImageResource resource = resourceBundle.arrowForActionMenu();
        final int width = resource.getWidth() + 9;

        final Image arrowImage = new Image(resource);
        arrowImage.getElement().getStyle().setPropertyPx("marginTop", 6);
        arrowImage.getElement().getStyle().setPropertyPx("marginLeft", 8);
        arrowImage.getElement().getStyle().setProperty("cursor", "pointer");

        optionButton.setWidget(arrowImage);

        topContainer.removeStyleName(resourceBundle.style().noButton());
        topContainer.setCellWidth(optionButton, width + "px");

        arrowImage.addClickHandler(handler);
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    interface TitleWindowUiBinder extends UiBinder<Widget, TitleWindow> {
    }

    /**
     * TODO: Refactor, this widget should have method onResize() and then in itself manipulate the content if needed
     *
     * @return
     */
    public Widget getBodyContent() {
        return bodyContainer.getWidget();
    }

    public SimplePanel getBodyContainer() {
        return bodyContainer;
    }
}
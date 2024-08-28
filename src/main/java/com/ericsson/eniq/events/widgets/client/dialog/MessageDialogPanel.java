/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.dialog;

import com.ericsson.eniq.events.widgets.client.WidgetsResourceBundle;
import com.ericsson.eniq.events.widgets.client.WidgetsResourceBundleHelper;
import com.ericsson.eniq.events.widgets.client.button.ImageButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class MessageDialogPanel extends Composite implements DialogPanel {

    private static final Binder binder = GWT.create(Binder.class);
    private static final WidgetsResourceBundle bundle = WidgetsResourceBundleHelper.getBundle();

    @UiField
    VerticalPanel wrapper;

    @UiField(provided = true)
    Button closeButton;

    @UiField(provided = true)
    ImageButton closeIcon;

    @UiField
    HTML messageLabel;

    @UiField
    Image icon;

    @UiField
    Label titleLabel;

    public MessageDialogPanel(final ClickHandler closeHandler) {
        createCloseButton(closeHandler);
        createCloseIcon(closeHandler);

        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public String getTitle() {
        return titleLabel.getText();
    }

    @Override
    public void setTitle(final String title) {
        titleLabel.setText(title);
    }

    @Override
    public String getMessage() {
        return messageLabel.getText();
    }

    @Override
    public void setMessage(String message) {
        message = SafeHtmlUtils.htmlEscape(message);
        message = message.replaceAll("\\\n", "<br/>");

        messageLabel.setHTML(SafeHtmlUtils.fromTrustedString(message));
    }

    private void createCloseButton(final ClickHandler closeHandler) {
        closeButton = new Button("Dismiss", closeHandler);
    }

    private void createCloseIcon(final ClickHandler closeHandler) {
        closeIcon = new ImageButton(bundle.closeIcon());
        closeIcon.setHoverImage(bundle.closeIconHover());
        closeIcon.addClickHandler(closeHandler);
    }

    @Override
    public void setDialogType(final MessageDialog.DialogType type) {
        ImageResource iconResource;

        switch (type) {
            case WARNING:
                iconResource = bundle.warningMessageIcon();
                break;
            case ERROR:
                iconResource = bundle.errorMessageIcon();
                break;

            default:
                iconResource = bundle.infoMessageIcon();
                break;
        }

        AbstractImagePrototype.create(iconResource).applyTo(icon);
    }

    interface Binder extends UiBinder<Widget, MessageDialogPanel> {
    }
}

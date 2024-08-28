package com.ericsson.eniq.events.widgets.client.dialog;

import com.ericsson.eniq.events.widgets.client.WidgetsResourceBundle;
import com.ericsson.eniq.events.widgets.client.WidgetsResourceBundleHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class PromptDialogPanel extends Composite implements DialogPanel {

    interface Binder extends UiBinder<Widget, PromptDialogPanel> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    Image icon;

    @UiField
    Label titleLabel;

    @UiField
    HTML messageLabel;

    @UiField(provided = true)
    Button okButton;

    @UiField(provided = true)
    Button cancelButton;

    private static final WidgetsResourceBundle bundle = WidgetsResourceBundleHelper.getBundle();

    public PromptDialogPanel(final ClickHandler oKClickHandler, final ClickHandler cancelClickHandler) {
        createOkButton(oKClickHandler);
        createCancelButton(cancelClickHandler);

        initWidget(binder.createAndBindUi(this));
    }

    private void createOkButton(final ClickHandler oKClickHandler) {
        okButton = new Button("Ok", oKClickHandler);
    }

    private void createCancelButton(final ClickHandler cancelClickHandler) {
        cancelButton = new Button("Cancel", cancelClickHandler);
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
}

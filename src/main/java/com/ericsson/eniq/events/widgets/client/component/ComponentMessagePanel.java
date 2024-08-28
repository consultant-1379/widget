package com.ericsson.eniq.events.widgets.client.component;

import com.ericsson.eniq.events.widgets.client.WidgetsResourceBundle;
import com.ericsson.eniq.events.widgets.client.WidgetsResourceBundleHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;

public class ComponentMessagePanel extends Composite {

    private final static MessagePanelUiBinder uiBinder = GWT.create(MessagePanelUiBinder.class);

    @UiField
    Image icon;
    
    @UiField
    DivElement name;
    
    @UiField
    DivElement description;
    
    @UiField
    FlowPanel buttonsPanel;

    public ComponentMessagePanel() {
        initWidget(uiBinder.createAndBindUi(this));
        setHeight("100%");
    }

    public void populate(ComponentMessageType type, String title) {
        populate(type, title, null);
    }

    public void populate(ComponentMessageType type, String title, String description) {
        populateIcon(type);

        this.name.setInnerText(title);
        this.description.setInnerText(description);
    }

    public void addButton(Button button) {
        buttonsPanel.add(button);
    }

    public void removeButton(Button button) {
        buttonsPanel.remove(button);
    }

    private void populateIcon(ComponentMessageType type) {
        WidgetsResourceBundle bundle = WidgetsResourceBundleHelper.getBundle();

        ImageResource imageResource;
        if (type.equals(ComponentMessageType.ERROR)) {
            imageResource = bundle.errorMessageIcon();
        } else if (type.equals(ComponentMessageType.WARN)) {
            imageResource = bundle.warningMessageIcon();
        } else { // Info is the default one
            imageResource = bundle.infoMessageIcon();
        }
        
        AbstractImagePrototype.create(imageResource).applyTo(icon);
    }

    @UiTemplate("ComponentMessagePanel.ui.xml")
    interface MessagePanelUiBinder extends UiBinder<Widget, ComponentMessagePanel> {
    }
}

package com.ericsson.eniq.events.widgets.client.collapse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class CollapsePanel extends Composite implements HasText {

    private static CollapsePanelUiBinder uiBinder = GWT.create(CollapsePanelUiBinder.class);

    private static CollapsePanelResourceBundle bundle = GWT.create(CollapsePanelResourceBundle.class);

    @UiField
    HTMLPanel headerWrapper;

    @UiField
    SpanElement header;

    @UiField
    SimplePanel content;

    private final CollapsePanelResourceBundle.CollapsePanelStyle style;

    private boolean isCollapsed = true;

    private boolean isEnabled = true;

    public CollapsePanel() {
        style = bundle.style();
        style.ensureInjected();

        initWidget(uiBinder.createAndBindUi(this));
        initEventHandlers();
    }

    @Override
    public String getText() {
        return header.getInnerText();
    }

    @Override
    public void setText(final String text) {
        header.setInnerText(text);
    }

    public void setContent(final Widget widget) {
        content.setWidget(widget);
    }

    public Widget getContent() {
        return content.getWidget();
    }

    public void clearContent() {
        content.clear();
    }

    public void setCollapsed(final boolean isCollapsed) {
        content.setVisible(!isCollapsed);

        if (isCollapsed) {
            removeStyleName(style.open());
        } else {
            addStyleName(style.open());
        }

        this.isCollapsed = isCollapsed;
    }

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public void setEnabled(final boolean isEnabled) {
        if (isEnabled) {
            removeStyleName(style.disabled());
        } else {
            addStyleName(style.disabled());
        }

        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void addHeaderClickHandler(final ClickHandler handler) {
        headerWrapper.addDomHandler(handler, ClickEvent.getType());
    }

    private void initEventHandlers() {
        headerWrapper.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (isEnabled) {
                    setCollapsed(!isCollapsed);
                }
            }
        }, ClickEvent.getType());
    }

    interface CollapsePanelUiBinder extends UiBinder<Widget, CollapsePanel> {
    }
}

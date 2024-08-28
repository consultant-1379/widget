package com.ericsson.eniq.events.widgets.client.header;

import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanelResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * SimpleHeaderPanel is a SimplePanel but with header information.
 * It is made for setting dialogue pop-up from network activity tab.
 */
public class SimpleHeaderPanel extends Composite implements HasText {

    interface SimpleHeaderPanelUiBinder extends UiBinder<Widget, SimpleHeaderPanel> {
    }

    private static SimpleHeaderPanelUiBinder uiBinder = GWT.create(SimpleHeaderPanelUiBinder.class);

    protected static CollapsePanelResourceBundle bundle = GWT.create(CollapsePanelResourceBundle.class);

    @UiField
    HTMLPanel headerWrapper;

    @UiField
    SpanElement header;

    @UiField
    SimplePanel content;

    protected CollapsePanelResourceBundle.CollapsePanelStyle style;

    public SimpleHeaderPanel() {
        style = bundle.style();
        style.ensureInjected();

        initWidget(uiBinder.createAndBindUi(this));
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
        content.setVisible(true);
    }
}

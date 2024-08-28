package com.ericsson.eniq.events.widgets.client.header;

import java.util.Iterator;

import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanelResourceBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * FlowHeaderPanel is a FlowPanel but with header information.
 * It is made for the settings panel common across SON Vis.
 */
public class FlowHeaderPanel extends Composite implements HasWidgets {

    interface FlowHeaderPanelUiBinder extends UiBinder<Widget, FlowHeaderPanel> {
    }

    private static FlowHeaderPanelUiBinder uiBinder = GWT.create(FlowHeaderPanelUiBinder.class);

    protected static CollapsePanelResourceBundle bundle = GWT.create(CollapsePanelResourceBundle.class);

    @UiField
    HTMLPanel headerWrapper;

    @UiField
    SpanElement header;

    @UiField
    SpanElement subtitle;

    @UiField
    FlowPanel body;

    protected CollapsePanelResourceBundle.CollapsePanelStyle style;

    public FlowHeaderPanel() {
        style = bundle.style();
        style.ensureInjected();

        initWidget(uiBinder.createAndBindUi(this));
    }

    public String getTitleText() {
        return header.getInnerText();
    }

    public void setTitleText(final String text) {
        header.setInnerText(text);
    }

    public String getSubtitleText() {
        return subtitle.getInnerText();
    }

    public void setSubtitleText(final String text) {
        subtitle.setInnerText(text);
    }

    /* HasWidgets interface */

    @Override
    public void add(final Widget w) {
        body.add(w);
    }

    @Override
    public void clear() {
        body.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return body.iterator();
    }

    @Override
    public boolean remove(final Widget w) {
        return body.remove(w);
    }
}

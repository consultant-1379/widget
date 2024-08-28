package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.textbox.ExtendedSuggestBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:23
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedSuggestBoxSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final FlowPanel container = new FlowPanel();
        container.getElement().getStyle().setWidth(300, Style.Unit.PX);
        final MultiWordSuggestOracle multiWordSuggestOracle = new MultiWordSuggestOracle();
        multiWordSuggestOracle.add("suggestion1");
        multiWordSuggestOracle.add("suggestion2");
        final ExtendedSuggestBox extendedSuggestBox = new ExtendedSuggestBox(multiWordSuggestOracle);
        extendedSuggestBox.setWidth("100%");
        extendedSuggestBox.setDefaultText("This suggestion box is disabled :)");
        extendedSuggestBox.getElement().getStyle().setMargin(3, Style.Unit.PX);

        final ExtendedSuggestBox extendedSuggestBox1 = new ExtendedSuggestBox(multiWordSuggestOracle);
        extendedSuggestBox1.setWidth("100%");
        extendedSuggestBox1.setDefaultText("type the letter s");
        extendedSuggestBox1.getElement().getStyle().setMargin(3, Style.Unit.PX);
        extendedSuggestBox.setEnabled(false);
        container.add(extendedSuggestBox);
        container.add(extendedSuggestBox1);
        final String source = wrapCode(sourceBundle.extendedsuggestbox().getText());
        return wrapWidgetAndSource(container, source);
    }
}

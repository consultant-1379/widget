package com.ericsson.eniq.events.widgets.client.showcase;

import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 08/05/13
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
public class WrapSource {

    public String wrapCode(final String source) {
        return "<code class=\"prettyprint lang-java\">" + source + "</code>";
    }

    public FlowPanel wrapWidgetAndSource(final com.google.gwt.user.client.ui.Widget widget, final String source) {
        final FlowPanel panel = new FlowPanel();

        final SimplePanel widgetExample = new SimplePanel(widget);
        final SimplePanel sourceExample = new SimplePanel(new HTML(source));

        widgetExample.setStyleName("example-panel");
        sourceExample.setStyleName("source-panel");

        panel.add(widgetExample);
        panel.add(sourceExample);
        return panel;
    }
}

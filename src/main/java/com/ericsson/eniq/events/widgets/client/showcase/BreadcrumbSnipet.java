package com.ericsson.eniq.events.widgets.client.showcase;

import com.ericsson.eniq.events.widgets.client.WidgetSourceBundle;
import com.ericsson.eniq.events.widgets.client.breadcrumb.BreadCrumb;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created with IntelliJ IDEA.
 * User: eeoiobr
 * Date: 09/05/13
 * Time: 10:24
 * To change this template use File | Settings | File Templates.
 */
public class BreadcrumbSnipet extends WrapSource {
    WidgetSourceBundle sourceBundle = GWT.create(WidgetSourceBundle.class);

    public FlowPanel createWidget(){
        final BreadCrumb breadCrumb = new BreadCrumb();
        breadCrumb.addBreadCrumb("Main");
        breadCrumb.addBreadCrumb("Navigation");
        breadCrumb.addBreadCrumb("Example");

        final String source = wrapCode(sourceBundle.breadcrumb().getText());
        return wrapWidgetAndSource(breadCrumb, source);
    }
}

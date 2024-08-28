package com.ericsson.eniq.events.widgets.client.breadcrumb;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface BreadCrumbResourceBundle extends ClientBundle
{
    @Source("BreadCrumb.css")
    BreadCrumbStyle css();

    interface BreadCrumbStyle extends CssResource {

        @ClassName("breadCrumb-up")
        String breadCrumbUp();

        String crumbArrow();

        @ClassName("breadCrumb-down")
        String breadCrumbDown();

        String breadCrumb();

        @ClassName("crumbArrow-down")
        String arrowDown();

    }
}
package com.ericsson.eniq.events.widgets.client.breadcrumb;

/*Leave generic to store any type of data*/
public class BreadCrumbDetails<D> {

    private final D data;

    public BreadCrumbDetails(D data) {

        this.data = data;
    }

    public D getData() {
        return data;
    }
}



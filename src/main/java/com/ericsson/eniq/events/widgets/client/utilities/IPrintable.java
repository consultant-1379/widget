package com.ericsson.eniq.events.widgets.client.utilities;

import com.google.gwt.dom.client.Element;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public interface IPrintable {
    /**
     * This method returns the cloned element of element which we wants to print.
     * @return
     */
    Element getPrintableElement();
}

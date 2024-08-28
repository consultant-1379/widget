package com.ericsson.eniq.events.widgets.client.overlay.form;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class to reduce scope for radiobuttons to form level for wizards
 */
public class WizardFormContainer extends ComplexPanel {
    public WizardFormContainer() {
        setElement(Document.get().createFormElement());
    }

    /**
     * Adds a new child widget to the panel.
     *
     * @param w the widget to be added
     */
    @Override
    public void add(Widget w) {
        add(w, getElement());
    }
}

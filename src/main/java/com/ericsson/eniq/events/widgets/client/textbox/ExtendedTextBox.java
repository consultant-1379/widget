/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2011 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.widgets.client.textbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Text box with default text, detecting paste etc.
 * @author ecarsea
 * @since 2011
 *
 */
public class ExtendedTextBox extends TextBox implements BlurHandler, FocusHandler {

    private static TextBoxResourceBundle resources;

    static {
        resources = GWT.create(TextBoxResourceBundle.class);
        resources.css().ensureInjected();
    }

    private String defaultText = "";

    private boolean isError = false;

    HandlerRegistration blurHandler;

    HandlerRegistration focusHandler;

    private String defaultTextStyle;

    public ExtendedTextBox() {
        sinkEvents(Event.ONPASTE | Event.ONKEYDOWN);
        defaultTextStyle = resources.css().defaultText();
        this.setStylePrimaryName(resources.css().textBox());
    }

    /**
     * @param defaultText
     */
    public void setDefaultText(final String defaultText) {
        this.defaultText = defaultText;
        this.setText(defaultText);

        if ((defaultText != null) && (!defaultText.isEmpty())) {
            blurHandler = addBlurHandler(this);
            focusHandler = addFocusHandler(this);
            enableDefaultText();
        } else {
            // Remove handlers
            blurHandler.removeHandler();
            focusHandler.removeHandler();
        }
    }

    public void enableDefaultText() {
        final String text = getText();

        if ((text.length() == 0) || containsDefaultText()) {
            // Show Default Text
            setText(defaultText);
            if (!defaultTextStyle.isEmpty()) {
                addStyleName(defaultTextStyle);
            }
        }
    }

    @Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
        case Event.ONPASTE:
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                @Override
                public void execute() {
                    ValueChangeEvent.fire(ExtendedTextBox.this, getText());
                }
            });
            break;
        }
    }

    /**
     * @return does it contain default text or not
     */
    public boolean containsDefaultText() {
        return this.defaultText != null && this.defaultText.equalsIgnoreCase(this.getText());
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.dom.client.FocusHandler#onFocus(com.google.gwt.event.dom.client.FocusEvent)
     */
    @Override
    public void onFocus(final FocusEvent event) {
        if (!defaultTextStyle.isEmpty()) {
            removeStyleName(defaultTextStyle);
        }
        if (containsDefaultText()) {
            // Hide Default Text
            setText("");
        }
    }

    /**
     * Set an invalid data style on  a field.
     * @param isError
     */
    public void highlightInvalidField(final boolean isError) {
        if (isError) {
            //add the error style...
            removeStyleName(resources.css().textBox());
            addStyleName(resources.css().invalidEntry());
        } else {
            //remove the error style...
            removeStyleName(resources.css().invalidEntry());
            addStyleName(resources.css().textBox());
        }
        this.isError = isError;
    }

    public void valueEntered() {
        if (!defaultTextStyle.isEmpty()) {
            removeStyleName(defaultTextStyle);
        }
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.dom.client.BlurHandler#onBlur(com.google.gwt.event.dom.client.BlurEvent)
     */
    @Override
    public void onBlur(final BlurEvent event) {
        enableDefaultText();
    }

    /**
     * If an invalid entry was entered this will return true, otherwise false.
     * @return
     */
    public boolean getIsError(){
        return isError;
    }

    /**
     * @param defaultTextStyle
     */
    public void setDefaultStyle(final String defaultTextStyle) {
        this.defaultTextStyle = defaultTextStyle;
    }

    public boolean isTextBoxEmpty() {
        return getText() == null || getText().isEmpty();
    }
}

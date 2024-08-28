package com.ericsson.eniq.events.widgets.client.textbox;

import com.ericsson.eniq.events.widgets.client.suggestbox.ExtendedSuggestDisplay;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;

/**
 * SuggestBox with default text, detecting paste etc.
 * @author egallou from ecarsea's ExtendedTextBox
 * @since 2011
 *
 */
public class ExtendedSuggestBox extends SuggestBox implements BlurHandler, FocusHandler {

    private static TextBoxResourceBundle resources;

    static {
        resources = GWT.create(TextBoxResourceBundle.class);
        resources.css().ensureInjected();
    }

    private String defaultText = "";

    HandlerRegistration blurHandler;

    HandlerRegistration focusHandler;

    private String defaultTextStyle;

    public ExtendedSuggestBox(final SuggestOracle oracle) {
        this(oracle, new ExtendedSuggestDisplay());
    }

    public ExtendedSuggestBox(final SuggestOracle oracle, SuggestionDisplay suggestionDisplay) {
        super(oracle, new TextBox(), suggestionDisplay);
        sinkEvents(Event.ONPASTE);
        defaultTextStyle = resources.css().defaultText();
        this.setStylePrimaryName(resources.css().textBox());
    }

    public ExtendedSuggestBox(final SuggestOracle oracle, ExtendedSuggestDisplay suggestionDisplay) {
        super(oracle, new TextBox(), new ExtendedSuggestDisplay());
        sinkEvents(Event.ONPASTE);
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
            blurHandler = getTextBox().addFocusHandler(this);
            focusHandler = getTextBox().addBlurHandler(this);
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
    public void setText(final String text) {
        super.setText(text);
        setTitle(text);
    }

    @Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
        case Event.ONPASTE:
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    ValueChangeEvent.fire(ExtendedSuggestBox.this, getText());
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
        SuggestionDisplay suggestionDisplay = getSuggestionDisplay();
        if(suggestionDisplay instanceof ExtendedSuggestDisplay){
         ((ExtendedSuggestDisplay) suggestionDisplay).bringToFront();
        }
        checkDefaultTextOnFocus();
    }

    public void checkDefaultTextOnFocus() {
        if (!defaultTextStyle.isEmpty()) {
            removeStyleName(defaultTextStyle);
        }
        if (containsDefaultText()) {
            // Hide Default Text
            setText("");
        }
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
     * @param defaultTextStyle
     */
    public void setDefaultStyle(final String defaultTextStyle) {
        this.defaultTextStyle = defaultTextStyle;
    }

    public boolean isTextBoxEmpty() {
        return getText() == null || getText().isEmpty();
    }

    /**
     * Set the field enabled/disabled.
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.getElement().setPropertyBoolean("disabled", !enabled);
    }
}

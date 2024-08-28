package com.ericsson.eniq.events.widgets.client.dropdown;

import com.ericsson.eniq.events.widgets.client.dropdown.decorator.DefaultDropDownDecorator;
import com.ericsson.eniq.events.widgets.client.dropdown.decorator.DropDownDecorator;
import com.ericsson.eniq.events.widgets.client.dropdown.oracle.DropDownSuggestOracle;
import com.ericsson.eniq.events.widgets.client.dropdown.oracle.DropDownSuggestion;
import com.ericsson.eniq.events.widgets.client.dropdown.oracle.LocalSuggestOracle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

public class DropDown<T extends IDropDownItem> extends AbstractDropDown<T> {

    private static final DropDownUiBinder uiBinder = GWT.create(DropDownUiBinder.class);

    // TODO: Make package local again once ExtendedSuggestBoxWithButton will be removed
    @UiField(provided = true)
    public static final DropDownResourceBundle resourceBundle = GWT.create(DropDownResourceBundle.class);

    @UiField
    SimplePanel wrapper;

    @UiField
    TextBox content;

    @UiField
    HTML icon;

    @UiField
    FlowPanel container;

    private DropDownPanel popup;

    private List<T> items;

    private int limit = 20;

    private final DropDownSuggestOracle<T> oracle;

    private String currentText = "";

    private String defaultText = "";

    private final SuggestOracle.Callback callback = new SuggestOracle.Callback() {
        @Override
        public void onSuggestionsReady(final SuggestOracle.Request request, final SuggestOracle.Response response) {
            final List<T> items = new ArrayList<T>();

            final List<DropDownSuggestion<T>> suggestions = (List<DropDownSuggestion<T>>) response.getSuggestions();

            for (final DropDownSuggestion<T> suggestion : suggestions) {
                items.add(suggestion.getItem());
            }

            showPopupWithItems(items);
        }
    };

    @SuppressWarnings("unused")
    /* If used via UiBinder, it is shown as unused*/
    public DropDown() {
        this(0); // By default assume that width is 150px in css - DropDown.css
    }

    public <R extends SuggestOracle & DropDownSuggestOracle<T>> DropDown(final R oracle) {
        this(0, oracle);
    }

    public DropDown(final int width) {
        this(width, new LocalSuggestOracle<T>());
    }

    public <R extends SuggestOracle & DropDownSuggestOracle<T>> DropDown(final int width, final R oracle) {
        this.oracle = oracle;

        initWidget(uiBinder.createAndBindUi(this));

        resourceBundle.dropDownStyle().ensureInjected();
        if (width > 0) {
            setWidth(width + Style.Unit.PX.getType());
        }

        setDecorator(new DefaultDropDownDecorator<T>(resourceBundle));

        final TextBoxEvents handler = new TextBoxEvents();
        content.addKeyDownHandler(handler);
        content.addKeyUpHandler(handler);
        content.addBlurHandler(handler);
        content.addFocusHandler(handler);
    }

    @Override
    public void setDecorator(final DropDownDecorator<T> decorator) {
        super.setDecorator(decorator);
        oracle.setConverter(decorator);
    }

    @Override
    public void setEnabled(final boolean isEnabled) {
        super.setEnabled(isEnabled);
        content.setEnabled(isEnabled);

        if (isEnabled) {
            removeStyleName(resourceBundle.dropDownStyle().disabled());
        } else {
            addStyleName(resourceBundle.dropDownStyle().disabled());
        }
    }

    @Override
    public void setValue(final T value, final boolean fireEvents) {
        super.setValue(value, fireEvents);

        String newValue;
        if (value == null) {
            newValue = defaultText;
        } else {
            newValue = getDecorator().toString(value);
            content.removeStyleName(resourceBundle.dropDownStyle().defaultText());
        }
        content.setValue(newValue);
        currentText = newValue;
    }

    public void setText(String text) {
        content.setText(text);
    }

    /**
     * Get the selected text in the dropdown.
     * @return the selected value (String).
     */
    public String getText(){
        return content.getText();
    }

    public void setDefaultText(String value) {
        content.setText(value);
        content.addStyleName(resourceBundle.dropDownStyle().defaultText());
        this.defaultText = value;
        this.setValue(null);
    }

    @Override
    public void update(final List<T> items) {
        super.update(items);

        this.items = items;

        if (!oracle.isRemote()) {
            oracle.update(items);
        }
    }

    public void setSuggestLimit(final int limit) {
        this.limit = limit;
    }

    @Override
    protected DropDownPanel getPopup() {
        if (popup == null) {
            popup = new DropDownPanel(resourceBundle, getPopupParent(), new CloseHandler<DropDownPopup>() {
                @Override
                public void onClose(final CloseEvent<DropDownPopup> popupPanelCloseEvent) {
                    // Check if text has changed and revert it to selected value
                    revertText();

                    // Clear marked item, as it is not needed anymore when dropdown is closed
                    getCellList().clearMarked();

                    // Remove style for open DropDown
                    wrapper.removeStyleName(resourceBundle.dropDownStyle().open());
                }
            });

            popup.setAutohidePartner(wrapper);
            popup.setMaxHeight(getPopupMaxHeight());
        }

        return popup;
    }

    @Override
    protected FlowPanel getPopupParent() {
        return container;
    }

    @UiHandler("icon")
    void handleClick(final ClickEvent e) {
        if (!isEnabled()) {
            return;
        }

        if (getPopup().isShowing()) {
            getPopup().hide();
        } else {
            if (oracle.isRemote()) {
                ((SuggestOracle) oracle).requestDefaultSuggestions(new SuggestOracle.Request(null, limit), callback);
                // TODO: This would be the best place to start loading indicator if needed
            } else {
                showPopupWithItems(items);
            }
        }
    }

    private void showPopupWithItems(final List<T> items) {
        if (getCellList().shouldUpdate(items)) {
            getCellList().update(items);
        }

        if (!getPopup().isShowing()) {
            getPopup().show(wrapper);
            wrapper.addStyleName(resourceBundle.dropDownStyle().open());
        }

        content.setFocus(true);
    }

    private void revertText() {
        content.removeStyleName(resourceBundle.dropDownStyle().defaultText());
        final String presentedValue = content.getText();
        final String selectedValue = getDecorator().toString(getValue());
        if (!presentedValue.equals(selectedValue)) {
            String newValue;
            if (selectedValue == null) {
                newValue = defaultText;
                content.addStyleName(resourceBundle.dropDownStyle().defaultText());
            } else {
                newValue = selectedValue;
            }

            content.setText(newValue);
            currentText = newValue;
        }
    }

    interface DropDownUiBinder extends UiBinder<Widget, DropDown> {
    }

    class TextBoxEvents implements KeyDownHandler, KeyUpHandler, BlurHandler, FocusHandler {
        @Override
        public void onKeyDown(final KeyDownEvent event) {
            switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_DOWN:
                // Select next item
                getCellList().markNext();

                event.preventDefault();
                event.stopPropagation();

                break;
            case KeyCodes.KEY_UP:
                // Select previous item
                getCellList().markPrevious();

                event.preventDefault();
                event.stopPropagation();

                break;
            case KeyCodes.KEY_ENTER:
                content.setFocus(true);
            case KeyCodes.KEY_TAB:
                // Select currently marked item (if not null) and fire event
                if (!getCellList().isEmpty()) {
                    getCellList().selectMarked();
                }

                getPopup().hide();

                break;
            }
        }

        @Override
        public void onKeyUp(final KeyUpEvent event) {

            boolean showFull = false;
            boolean showSuggested = false;

            final int keyCode = event.getNativeKeyCode();
            if (!getPopup().isShowing() && (keyCode == KeyCodes.KEY_UP || keyCode == KeyCodes.KEY_DOWN)) {
                showFull = true;
            }

            // Avoid call to oracle, if non character keys has been pressed (like esc, backspace, tab, etc.)
            final String query = content.getText();
            if (!query.equals(currentText)) {
                currentText = query;
                showSuggested = true;

                if (query.length() == 0) {
                    showFull = true;
                }
            }

            if (showFull) {
                ((SuggestOracle) oracle).requestDefaultSuggestions(new SuggestOracle.Request(null, limit), callback);
            } else if (showSuggested) {
                ((SuggestOracle) oracle).requestSuggestions(new SuggestOracle.Request(query, limit), callback);
            }
        }

        @Override
        public void onBlur(final BlurEvent event) {
            // Check if text has changed and revert it to selected value
            revertText();
        }

        /* (non-Javadoc)
         * @see com.google.gwt.event.dom.client.FocusHandler#onFocus(com.google.gwt.event.dom.client.FocusEvent)
         */
        @Override
        public void onFocus(final FocusEvent event) {
            removeDefaultText();
        }

        public void removeDefaultText() {
            content.removeStyleName(resourceBundle.dropDownStyle().defaultText());
            if (containsDefaultText()) {
                content.setText("");
            }
        }
    }

    /**
     * @return does it contain default text or not
     */
    public boolean containsDefaultText() {
        return this.defaultText != null && this.defaultText.equalsIgnoreCase(content.getText());
    }

}

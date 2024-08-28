package com.ericsson.eniq.events.widgets.client.dropdown;

import com.ericsson.eniq.events.widgets.client.dropdown.decorator.DefaultDropDownDecorator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DropDownMenu<V extends IDropDownItem> extends AbstractDropDown<V> {

    private static DropDownMenuUiBinder uiBinder = GWT.create(DropDownMenuUiBinder.class);

    @UiField(provided = true)
    static final DropDownResourceBundle resourceBundle = GWT.create(DropDownResourceBundle.class);

    @UiField
    Label content;

    @UiField
    HTML icon;

    @UiField
    FocusPanel wrapper;

    @UiField
    FlowPanel container;

    private DropDownPanel popup;

    private String defaultText;

    @SuppressWarnings("unused")
    /* If used via UiBinder, it is shown as unused*/
    public DropDownMenu() {
        initWidget(uiBinder.createAndBindUi(this));

        resourceBundle.menuStyle().ensureInjected();

        setDecorator(new DefaultDropDownDecorator<V>(resourceBundle));

        getPopup().setAutohidePartner(wrapper);
        getPopup().setMaxHeight(getPopupMaxHeight());

        final WrapperEvents handler = new WrapperEvents();
        wrapper.addKeyDownHandler(handler);
        wrapper.addKeyUpHandler(handler);
        wrapper.addBlurHandler(handler);
        wrapper.addFocusHandler(handler);
    }

    public DropDownMenu(final int width) {
        this();
        setWidth(width + Style.Unit.PX.getType());
    }

    @Override
    public void setEnabled(final boolean isEnabled) {
        super.setEnabled(isEnabled);

        if (isEnabled) {
            wrapper.setTabIndex(-1);
            removeStyleName(resourceBundle.menuStyle().disabled());
        } else {
            wrapper.setTabIndex(0);
            addStyleName(resourceBundle.menuStyle().disabled());
        }
    }

    @Override
    public void setValue(final V value, final boolean fireEvents) {
        super.setValue(value, fireEvents);

        // TODO: If by setting content, it is overflow, also set title attribute

        String newValue;
        if (value == null) {
            newValue = "";
        } else {
            newValue = getDecorator().toString(value);
        }

        content.setText(newValue);
    }

    public void setDefaultText(final String value) {
        defaultText = value;
        content.setText(defaultText);
    }

    @UiHandler("wrapper")
    void handleClick(final ClickEvent e) {
        if (!isEnabled()) {
            return;
        }

        if (getPopup().isShowing()) {
            getPopup().hide();
        } else {
            getPopup().show(wrapper);

            wrapper.addStyleName(resourceBundle.menuStyle().open());
            wrapper.setFocus(true);
        }
    }

    @Override
    public DropDownPanel getPopup() {
        if (popup == null) {
            popup = new DropDownPanel(resourceBundle, getPopupParent(), new CloseHandler<DropDownPopup>() {
                @Override
                public void onClose(final CloseEvent<DropDownPopup> dropDownPopupCloseEvent) {
                    wrapper.removeStyleName(resourceBundle.menuStyle().open());
                }
            });
        }
        return popup;
    }

    @Override
    protected FlowPanel getPopupParent() {
        return container;
    }

    private void revertText() {
        final String presentedValue = content.getText();
        final String selectedValue = getDecorator().toString(getValue());

        if (!presentedValue.equals(selectedValue) && !presentedValue.equals(defaultText)) {
            String newValue;
            if (selectedValue == null) {
                newValue = "";
            } else {
                newValue = selectedValue;
            }

            content.setText(newValue);
        }
    }

    interface DropDownMenuUiBinder extends UiBinder<Widget, DropDownMenu> {
    }

    class WrapperEvents implements KeyDownHandler, KeyUpHandler, BlurHandler, FocusHandler {
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
                wrapper.setFocus(true);
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
            final int keyCode = event.getNativeKeyCode();
            if (!getPopup().isShowing() && (keyCode == KeyCodes.KEY_UP || keyCode == KeyCodes.KEY_DOWN)) {
                getPopup().show(wrapper);

                wrapper.addStyleName(resourceBundle.menuStyle().open());
            }
        }

        @Override
        public void onBlur(final BlurEvent event) {
            // Check if text has changed and revert it to selected value
            revertText();
            wrapper.removeStyleName(resourceBundle.menuStyle().focus());
        }

        @Override
        public void onFocus(final FocusEvent event) {
            wrapper.addStyleName(resourceBundle.menuStyle().focus());
        }
    }
}

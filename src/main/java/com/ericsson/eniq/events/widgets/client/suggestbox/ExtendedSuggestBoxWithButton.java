package com.ericsson.eniq.events.widgets.client.suggestbox;

import com.ericsson.eniq.events.widgets.client.dropdown.DropDown;
import com.ericsson.eniq.events.widgets.client.dropdown.DropDownResourceBundle;
import com.ericsson.eniq.events.widgets.client.textbox.ExtendedSuggestBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.beans.EventHandler;

/**
 * Just a quick introduction of arrow for suggestbox, to show all suggestions
 * @deprecated Replace with DropDown when it will be stable enough
 */
@Deprecated
public class ExtendedSuggestBoxWithButton extends Composite {

    private static final WidgetUiBinder uiBinder = GWT.create(WidgetUiBinder.class);

    private boolean enabled;

    private boolean checkIfOpen = false;

    private String queryValue;

    @UiField(provided = true)
    ExtendedSuggestBox content;

    @UiField
    HTML icon;

    @UiField(provided = true)
    DropDownResourceBundle resourceBundle;

    public ExtendedSuggestBoxWithButton(final LiveLoadOracle oracle) {
        this(oracle, new ExtendedSuggestDisplay());
    }

    public ExtendedSuggestBoxWithButton(final LiveLoadOracle oracle, final ExtendedSuggestDisplay suggestionDisplay) {
        /** default enabled **/
        enabled = true;
        content = new ExtendedSuggestBox(oracle, suggestionDisplay);
        content.getElement().getStyle().setHeight(100, Style.Unit.PCT);

        content.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
                SuggestOracle.Suggestion selectedItem = suggestionSelectionEvent.getSelectedItem();
                checkIfOpen=false;
                if (selectedItem != null) {
                    content.valueEntered();
                }
            }
        });

        resourceBundle = DropDown.resourceBundle;
        resourceBundle.dropDownStyle().ensureInjected();

        initWidget(uiBinder.createAndBindUi(this));
        //Checks if the list is visible if not set the checkIfOpen as false
        icon.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                if(!content.isSuggestionListShowing()){
                    checkIfOpen=false;
                }
            }
        });

        icon.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                if (enabled && !oracle.isLoading()&&checkIfOpen==false) {

                    // Hacky way to avoid Suggestbox restrictions on extending
                    String oldText = content.getText();
                    content.setText("");
                    checkIfOpen=true;
                    SuggestBox.SuggestionDisplay suggestionDisplay = content.getSuggestionDisplay();
                    if(suggestionDisplay instanceof ExtendedSuggestDisplay){
                        ((ExtendedSuggestDisplay) suggestionDisplay).bringToFront();
                    }

                    content.showSuggestionList();

                    content.setText(oldText);
                }
                else if (enabled && checkIfOpen==true){           //Used to close the drop down if it is open.
                    checkIfOpen=false;
                }
            }
        });

    }

    public void setDefaultText(String defaultText) {
        content.setDefaultText(defaultText);
    }

    public void enableDefaultText() {
        content.enableDefaultText();
    }

    public void checkDefaultTextOnFocus() {
        content.checkDefaultTextOnFocus();
    }

    public TextBoxBase getTextBox() {
        return content.getTextBox();
    }

    public String getValue() {
        return content.getValue();
    }

    public String getText() {
        return content.getText();
    }

    public void setText(String text) {
        content.setText(text);
    }

    public void setEnabled(boolean isEnabled) {
        enabled = isEnabled;

        if (isEnabled) {
            // enable arrow
            removeStyleName(resourceBundle.dropDownStyle().disabled());
        } else {
            // disable arrow
            addStyleName(resourceBundle.dropDownStyle().disabled());
        }

        content.getTextBox().setEnabled(isEnabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void addValueChangeHandler(ValueChangeHandler<String> handler) {
        content.getTextBox().addValueChangeHandler(handler);
    }

    public void addSelectionHandler(SelectionHandler<SuggestOracle.Suggestion> selectionHandler) {
        content.addSelectionHandler(selectionHandler);
    }

    public void addKeyUpHandler(KeyUpHandler handler) {
        content.addKeyUpHandler(handler);
    }

    public boolean containsDefaultText() {
        return content.containsDefaultText();
    }

    public boolean isTextBoxEmpty() {
        return content.isTextBoxEmpty();
    }

    public void setPopupStyleName(String style) {
        content.setPopupStyleName(style);
    }

    interface WidgetUiBinder extends UiBinder<Widget, ExtendedSuggestBoxWithButton> {
    }
}

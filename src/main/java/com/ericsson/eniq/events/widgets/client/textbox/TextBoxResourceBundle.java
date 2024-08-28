package com.ericsson.eniq.events.widgets.client.textbox;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface TextBoxResourceBundle extends ClientBundle {
    @Source("TextBox.css")
    TextBoxStyle css();

    interface TextBoxStyle extends CssResource {

        @ClassName("extendedTextBox")
        String textBox();

        String defaultText();

        String suggestPopupContent();
        
        String invalidEntry();
    }
}
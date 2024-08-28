package com.ericsson.eniq.events.widgets.client.textbox;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class ExtendedNumberBox extends ExtendedTextBox {

    private String pattern = "^[0-9]*(.[0-9]{0,2})?$";//default up to 2decimal place

    private RegExp regExp;

    private int noOfDecimalPlaces;

    public ExtendedNumberBox() {
        regExp = RegExp.compile(pattern);
    }

    @Override
    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
        case Event.ONKEYDOWN:
            final int keyCode = event.getKeyCode();
            final boolean number = (keyCode > 47 && keyCode < 58) || (keyCode > 95 && keyCode < 106);
            final boolean delete = keyCode == 46;
            final boolean selectAll = event.getCtrlKey() && keyCode == 65;
            final boolean arrows = keyCode == 37 || keyCode == 39 || keyCode == 38 || keyCode == 40;
            final boolean enter = keyCode == 13;
            final boolean copy = event.getCtrlKey() && keyCode == 67;
            final boolean paste = event.getCtrlKey() && keyCode == 86;
            final boolean backspace = keyCode == 8;
            final boolean decimal = keyCode == 190 || keyCode == 110;
            boolean valid = true;
            if (decimal || number) {
                final MatchResult matcher = regExp.exec(getText() + (decimal ? "." : "0"));
                valid = (matcher != null || (number && noOfDecimalPlaces > 0 && getCursorPos() <= getText()
                        .indexOf(".")));
            }
            final boolean tab = keyCode == 9;
            if (!(number && valid) && !delete && !selectAll && !arrows && !enter && !copy && !paste && !backspace
                    && !(decimal && valid) && !tab) {
                event.preventDefault();
            }
            break;
        }
    }

    public void setDecimalPlace(final int noOfDecimalPlaces) {
        this.noOfDecimalPlaces = noOfDecimalPlaces;
        if (noOfDecimalPlaces > 0) {
            pattern = "^[0-9]*(.[0-9]{0," + noOfDecimalPlaces + "})?$";
        } else {
            pattern = "^[0-9]*$";
        }
        regExp = RegExp.compile(pattern);
    }
}

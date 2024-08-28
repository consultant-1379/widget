package com.ericsson.eniq.events.widgets.client.slider;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public interface Formatter {
    enum Units {
        BYTES, MINUTES, HOURS,
    }

    /*var newValue = Math.abs(value);

    var label = 'B';
    if(newValue >= 1073741824) {
        newValue = Math.round(newValue / 1073741824);
        label = 'GB';
    }
    else if(newValue >= 1048576) {
        newValue = Math.round(newValue / 1048576);
        label = 'MB';
    }
    else if(newValue >= 1024) {
        newValue = Math.round(newValue / 1024);
        label='KB';
    return newValue+' '+label;
    }*/
    String formatValue(int value, Units unit);
}

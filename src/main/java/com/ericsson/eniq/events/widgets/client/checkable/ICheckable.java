package com.ericsson.eniq.events.widgets.client.checkable;

import com.google.gwt.user.client.ui.CheckBox;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public interface ICheckable<D> {

    public D getData();

    public Boolean getValue();

    public void setValue(Boolean value);

    /**
     * Note: {@link com.google.gwt.user.client.ui.RadioButton} is {@link com.google.gwt.user.client.ui.CheckBox} as
     * well.
     *
     * @return checkable {@link com.google.gwt.user.client.ui.CheckBox}
     */
    public CheckBox getUiItem();

}
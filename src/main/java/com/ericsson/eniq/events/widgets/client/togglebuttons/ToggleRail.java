package com.ericsson.eniq.events.widgets.client.togglebuttons;

import java.util.HashMap;
import java.util.Map;

import com.ericsson.eniq.events.widgets.client.ToString;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasValue;

public class ToggleRail<V> extends Composite implements HasValue<V> {

    private V selected;

    private String selectedString;

    private ToString<V> converter;

    private final Map<String, V> values = new HashMap<String, V>();

    private final FocusPanel focusPanel = new FocusPanel();

    private final FlexTable panel = new FlexTable();

    static final ToggleRailResourceBundle resources;

    private static final int LEFT_ARROW_KEY = 37;

    private static final int RIGHT_ARROW_KEY = 39;

    private boolean isEnabled = true;

    static {
        resources = GWT.create(ToggleRailResourceBundle.class);
        resources.css().ensureInjected();
    }

    //    public ToggleRail() {
    //        throw new RuntimeException("Use a constructor with parameter(s) ToggleRail(int width ...) "
    //                + "and @UiField(provided = true) if needed");
    //    }

    public @UiConstructor
    ToggleRail(final String width) {
        this(width, new ToString<V>() {
            @Override
            public String toString(final V value) {
                return value.toString();
            }
        });
        DOM.sinkEvents(getElement(), Event.FOCUSEVENTS | Event.KEYEVENTS);
        sinkEvents(Event.FOCUSEVENTS | Event.KEYEVENTS);
    }

    public ToggleRail(final String width, final ToString<V> converter) {
        this.converter = converter;
        focusPanel.add(panel);
        focusPanel.setStyleName(resources.css().toggleRail());
        panel.setCellPadding(0);
        panel.setCellSpacing(0);

        panel.setWidth(width);
        panel.setStyleName(resources.css().toggleRail());

        panel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (isEnabled) {
                    final HTMLTable.Cell cell = panel.getCellForEvent(event);

                    final String cellText = panel.getText(cell.getRowIndex(), cell.getCellIndex());

                    if (selectedString == null || !cellText.equals(selectedString)) {
                        setValue(values.get(cellText), true);
                    }
                }
            }
        });

        focusPanel.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(final FocusEvent event) {
                for (int cell = 0; cell < panel.getCellCount(0); cell++) {
                    panel.getCellFormatter().addStyleName(0, cell, resources.css().focus());
                }
            }
        });

        focusPanel.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(final BlurEvent event) {
                for (int cell = 0; cell < panel.getCellCount(0); cell++) {
                    panel.getCellFormatter().removeStyleName(0, cell, resources.css().focus());
                }
            }
        });

        focusPanel.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(final KeyPressEvent event) {
                final int keyCode = event.getNativeEvent().getKeyCode();
                switch (keyCode) {
                case LEFT_ARROW_KEY:
                    toggleLeft();
                    break;

                case RIGHT_ARROW_KEY:
                    toggleRight();
                    break;

                default:
                    break;
                }
            }
        });
        initWidget(focusPanel);
    }

    /**
     * Add item to toggle rail
     *
     * @param item item to be added
     */
    public void add(final V item) {
        add(item, 0);
    }

    /**
     * Add item to toggle rail
     *
     * @param item  item to be added
     * @param width width of this item
     */
    public void add(final V item, final int width) {
        final String key = converter.toString(item);
        if (!values.containsKey(key)) {
            values.put(key, item);

            // Add to panel
            int column;
            if (panel.getRowCount() == 0) {
                column = 0;
            } else {
                column = panel.getCellCount(0);
            }

            panel.setText(0, column, key);

            if (width > 0) {
                panel.getCellFormatter().setWidth(0, column, width + "px");
            }
        }
    }

    /**
     * @param item item to be removed from rail
     */
    public void remove(final V item) {
        final String key = converter.toString(item);
        if (values.containsKey(key)) {
            if (item.equals(selected)) {
                selected = null;
                selectedString = null;
            }

            for (int cell = 0; cell < panel.getCellCount(0); cell++) {
                if (panel.getText(0, cell).equals(key)) {
                    panel.removeCell(0, cell);
                    break;
                }
            }

            values.remove(key);
        }
    }

    public void clear() {
        selected = null;
        selectedString = null;

        values.clear();
        panel.clear();
    }

    /**
     * Get selected item
     *
     * @return selected item
     */
    @Override
    public V getValue() {
        return selected;
    }

    /**
     * Set selected
     *
     * @param value item to select
     */
    @Override
    public void setValue(final V value) {
        setValue(value, false);
    }

    /**
     * Set selected and fire a change event
     *
     * @param value      item to select
     * @param fireEvents flag to fire event
     */
    @Override
    public void setValue(final V value, final boolean fireEvents) {
        final String selectedString = converter.toString(value);
        if (values.containsKey(selectedString)) {
            if (this.selected != null) {
                setSelected(this.selectedString, false);
            }

            setSelected(selectedString, true);

            this.selected = value;
            this.selectedString = selectedString;

            if (fireEvents) {
                ValueChangeEvent.fire(this, value);
            }
        }
    }

    public void setEnabled(final boolean isEnabled) {
        if (this.isEnabled == isEnabled) {
            return;
        }

        if (isEnabled) {
            removeStyleName(resources.css().disabled());
        } else {
            addStyleName(resources.css().disabled());
        }

        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<V> vValueChangeHandler) {
        return addHandler(vValueChangeHandler, ValueChangeEvent.getType());
    }

    /**
     * Toggle the button to the left.
     */
    public void toggleLeft() {
        int selectedCell = 0;
        for (int cell = 0; cell < panel.getCellCount(0); cell++) {
            if (panel.getText(0, cell).equals(this.selectedString)) {
                selectedCell = cell;
                break;
            }
        }
        Object newSelected = null;

        if (selectedCell == 0) {
            //if selected cell is the left most cell, then we need to cycle around to the last cell...
            newSelected = panel.getText(0, panel.getCellCount(0) - 1);
        } else {
            //select the next cell to the left...
            newSelected = panel.getText(0, selectedCell - 1);
        }
        setValue(values.get(newSelected), true);
    }

    /**
     * Toggle the button to the right.
     */
    public void toggleRight() {
        int selectedCell = 0;
        for (int cell = 0; cell < panel.getCellCount(0); cell++) {
            if (panel.getText(0, cell).equals(this.selectedString)) {
                selectedCell = cell;
                break;
            }
        }
        Object newSelected = null;

        if (selectedCell == (panel.getCellCount(0) - 1)) {
            //if selected cell is the right most cell, then we need to cycle around to the first cell...
            newSelected = panel.getText(0, 0);
        } else {
            //select the next cell to the right...
            newSelected = panel.getText(0, selectedCell + 1);
        }
        setValue(values.get(newSelected), true);
    }

    private void setSelected(final String value, final boolean selected) {
        for (int cell = 0; cell < panel.getCellCount(0); cell++) {
            if (panel.getText(0, cell).equals(value)) {
                if (selected) {
                    panel.getCellFormatter().addStyleName(0, cell, resources.css().selected());
                } else {
                    panel.getCellFormatter().removeStyleName(0, cell, resources.css().selected());
                }

                break;
            }
        }
    }
}

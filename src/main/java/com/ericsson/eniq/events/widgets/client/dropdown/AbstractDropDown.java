package com.ericsson.eniq.events.widgets.client.dropdown;

import java.util.List;

import com.ericsson.eniq.events.widgets.client.dropdown.cell.DropDownCell;
import com.ericsson.eniq.events.widgets.client.dropdown.cell.DropDownCellList;
import com.ericsson.eniq.events.widgets.client.dropdown.decorator.DropDownDecorator;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;

public abstract class AbstractDropDown<V extends IDropDownItem> extends Composite implements HasValue<V>,
        HasDropDownMouseOverHandlers<V> {

    private DropDownCellList<V> cellList;

    private DropDownDecorator<V> decorator;

    private boolean isEnabled = true;

    protected AbstractDropDown() {
    }

    public void setDecorator(final DropDownDecorator<V> decorator) {
        this.decorator = decorator;
    }

    public DropDownDecorator<V> getDecorator() {
        return decorator;
    }

    public void setEnabled(final boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void update(final List<V> items) {
        if (items == null) {
            setValue(null, true);
            getPopup().setContent(null);

            return;
        }

        // Clear the value if it is not in the list of items
        final V selected = getValue();
        if (selected != null && !items.contains(selected)) {
            setValue(null, true);
        }

        getCellList().update(items);

        getPopup().setContent(getCellList());
    }

    public V getItem(final int index) {
        return getCellList().getItem(index);
    }

    public int getItemCount() {
        return getCellList().getItemCount();
    }

    public void setItemSelected(final int piIndex) {
        getCellList().setSelected(piIndex);
    }

    /*
     * Get the Index of the currently selected DropDownCell
     * @return Index of selected item from the internal cell list
     *
    public int getSelectedIndex() {
        return getCellList().getSelectedIndex();
    }
    */

    @Override
    public V getValue() {
        return getCellList().getSelected();
    }

    @Override
    public void setValue(final V value) {
        setValue(value, false);
    }

    @Override
    public void setValue(final V value, final boolean fireEvents) {
        getCellList().setSelected(value);

        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<V> vValueChangeHandler) {
        return addHandler(vValueChangeHandler, ValueChangeEvent.getType());
    }

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.widgets.client.dropdown.HasDropDownMouseOverHandlers#addDropDownMouseOverHandler(com.ericsson.eniq.events.widgets.client.dropdown.DropDownMouseOverHandler)
     */
    @Override
    public HandlerRegistration addDropDownMouseOverHandler(final DropDownMouseOverHandler<V> handler) {
        return addHandler(handler, DropDownMouseOverEvent.getType());
    }

    protected abstract DropDownPanel getPopup();

    protected abstract FlowPanel getPopupParent();

    DropDownCellList<V> getCellList() {
        if (cellList == null) {
            cellList = new DropDownCellList<V>(getDecorator(), new DropDownCell.Delegate<V>() {
                @Override
                public void execute(final V value) {
                    getPopup().hide();
                    setValue(value, true);
                }

                @Override
                public void mouseOver(final V value) {
                    fireMouseOverEvent(value);
                }
            });
        }

        return cellList;
    }

    /**
     * @param value
     */
    protected void fireMouseOverEvent(final V value) {
        DropDownMouseOverEvent.fire(this, value);
    }

    protected int getPopupMaxHeight() {
        return 300;
    }
    
    public void setId(String id){
        getElement().setId(id);
    }
}

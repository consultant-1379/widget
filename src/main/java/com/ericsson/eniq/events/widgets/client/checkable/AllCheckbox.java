package com.ericsson.eniq.events.widgets.client.checkable;

import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEvent;
import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEventHandler;
import com.ericsson.eniq.events.widgets.client.checkable.event.HasChildSelectEventHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.event.shared.HandlerRegistration;

import java.util.*;

/**
 * "Select all" check box. <ul>It is used as <li>global check box (for all sections with all children check boxes)</li>
 * <li>"Select all" check box of individual section for a group.</li> </ul>
 */
public class AllCheckbox extends Composite implements HasChildSelectEventHandler {

    protected static final String ALL_CHECK_BOX_ENABLED_STYLE = "chk-all";
    protected static final String ALL_CHECK_BOX_DISABLED_STYLE = "chk-all-disabled";

    private final List<CheckBox> children = new ArrayList<CheckBox>();

    private final ChildClickHandler childClickHandler = new ChildClickHandler();

    private final CheckBox allCheckBox = new CheckBox();

    private  List<CheckBox> topTenCheckbox = new ArrayList<CheckBox>();

    /**
     * Checkboxes for each section/group
     */
    private final Map<String, AllCheckbox> allCheckBoxChildren = new HashMap<String, AllCheckbox>(2);

    private boolean showTopTen=false;

    public AllCheckbox() {
        allCheckBox.setText("Select all");
        allCheckBox.addStyleName(ALL_CHECK_BOX_ENABLED_STYLE);
        allCheckBox.addClickHandler(new AllCheckBoxClickHandler());
        initWidget(allCheckBox);
    }

    public AllCheckbox(boolean showTopTen) {
        this.showTopTen=showTopTen;
        allCheckBox.setText("Select Top 10");
        allCheckBox.addStyleName(ALL_CHECK_BOX_ENABLED_STYLE);
        allCheckBox.addClickHandler(new AllCheckBoxClickHandler());
        initWidget(allCheckBox);
    }

    public void setTopTenCheckbox(List<CheckBox> topTenCheckbox)    {
    this.topTenCheckbox   = topTenCheckbox;
    }

    public CheckBox getCheckBox() {
        return allCheckBox;
    }

    public void registerChild(final CheckBox child) {
        child.addClickHandler(childClickHandler);
        children.add(child);
    }

    /**
     * @param groupId group id
     * @param child   group check box
     */
    public void registerChild(String groupId, final AllCheckbox child) {
        registerChild(child.getCheckBox());
        allCheckBoxChildren.put(groupId, child);
    }

    public List<CheckBox> getChildren() {
        return children;
    }

    /**
     * @return collection of group check boxes
     */
    public Collection<AllCheckbox> getAllCheckBoxChildrenCollection() {
        return allCheckBoxChildren.values();
    }

    /**
     * @return map of group check boxes; keys are group ids
     */
    public Map<String, AllCheckbox> getAllCheckBoxChildren() {
        return allCheckBoxChildren;
    }

    public AllCheckbox getGroupCheckBox(String groupId) {
        return allCheckBoxChildren.get(groupId);
    }

    public void clear() {
        children.clear();
        allCheckBoxChildren.clear();
        allCheckBox.setValue(false, false);
    }

    public void setEnabled(final boolean isEnabled) {
        if (isEnabled) {
            allCheckBox.removeStyleName(ALL_CHECK_BOX_DISABLED_STYLE);
        } else {
            allCheckBox.addStyleName(ALL_CHECK_BOX_DISABLED_STYLE);
        }

        allCheckBox.setEnabled(isEnabled);
    }

    public boolean isAnyChildSelected() {
        for (final CheckBox child : children) {
            if (child.getValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used for selecting duplicate checkbox labels on the wizard with one click.
     *
     * @param selectedChild Looks like quick fix for different problem, therefore solve the reason why checkboxes have
     *                      identical labels and then remove this method.
     */
    @Deprecated
    public void checkDuplicateItems(final CheckBox selectedChild) {
        if (selectedChild != null) { // seen it happen

            final String selectedChildText = selectedChild.getText();
            final boolean childState = selectedChild.getValue();

            for (final CheckBox child : children) {
                final String childText = child.getText();
                if (childText.equals(selectedChildText) && !childText.equals(allCheckBox.getText())) {
                    child.setValue(childState);
                }
            }
        }
    }

    @Override
    public HandlerRegistration addChildSelectEventHandler(final ChildSelectEventHandler handler) {
        return addHandler(handler, ChildSelectEvent.getType());
    }

    public void setValue(boolean value) {
        for (final CheckBox child : children) {
            if (child.getValue()) {
                child.setValue(value);
            }
        }

        if (allCheckBox.getValue()) {
            allCheckBox.setValue(value, false);
        }
    }

    private class ChildClickHandler implements ClickHandler {
        @Override
        public void onClick(final ClickEvent event) {
            boolean result = true;

            for (final CheckBox checkBox : children) {
                if (!checkBox.getValue()) {
                    result = false;
                    break;
                }
            }
            allCheckBox.setValue(result, false);

            final CheckBox child = (CheckBox) event.getSource();
            fireEvent(new ChildSelectEvent(child));
        }
    }

    private class AllCheckBoxClickHandler implements ClickHandler {
        @Override
        public void onClick(final ClickEvent event) {
            final CheckBox allCheckBox = (CheckBox) event.getSource();
            final boolean checked = allCheckBox.getValue();
            for (final CheckBox checkBox : children) {
                if (showTopTen){
                    if(topTenCheckbox.contains(checkBox)){
                        checkBox.setValue(checked);
                        checkBox.setEnabled(!checked);
                    } else {
                        checkBox.setValue(false);
                        checkBox.setEnabled(!checked);
                    }
                }else {
                    checkBox.setValue(checked);
                }

            }



            // Should call this event here as well, as no event is called for children when check all is selected
            fireEvent(new ChildSelectEvent(null));
        }
    }
}
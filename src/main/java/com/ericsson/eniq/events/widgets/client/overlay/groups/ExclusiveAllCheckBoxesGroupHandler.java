package com.ericsson.eniq.events.widgets.client.overlay.groups;

import com.ericsson.eniq.events.widgets.client.checkable.AllCheckbox;
import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEvent;
import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEventHandler;
import com.ericsson.eniq.events.widgets.client.overlay.IWizardOverlayPanel;
import com.ericsson.eniq.events.widgets.client.overlay.translators.GroupTranslator;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public class ExclusiveAllCheckBoxesGroupHandler<T, G> extends AllCheckBoxesGroupHandler<T, G> {

    protected static final String GROUP_ID_ATTR = "groupId";
    private final GroupChildSelectEventHandler handler;

    public ExclusiveAllCheckBoxesGroupHandler(final IWizardOverlayPanel<T> overlayPanel,
            final GroupTranslator<G> groupResolver, final G[] groups) {
        super(overlayPanel, groupResolver, groups);
        handler = new GroupChildSelectEventHandler();
    }

    @Override public boolean canSelectAllGroupsItems() {
        return false;
    }

    @Override protected void onItemAdd(String groupId, CheckBox checkBox) {
        checkBox.getElement().setAttribute(GROUP_ID_ATTR, String.valueOf(groupId));
        super.onItemAdd(groupId, checkBox);        
    }

    @Override protected void onGroupContentPanelCreate(String groupId, FlowPanel panel) {
        super.onGroupContentPanelCreate(groupId, panel);
        AllCheckbox allCheckbox = allCheckboxes.get(groupId);
        allCheckbox.addChildSelectEventHandler(handler);
        allCheckbox.getCheckBox().getElement().setAttribute(GROUP_ID_ATTR, String.valueOf(groupId));
    }

    private class GroupChildSelectEventHandler implements ChildSelectEventHandler {
        @Override public void onChildSelect(ChildSelectEvent event) {
            CheckBox child = event.getChild();
            if (child != null && child.getValue()) {
                excludeOtherGroupCheckBoxes(child);
            } else {
                AllCheckbox allCheckbox = (AllCheckbox)event.getSource();
                if (allCheckbox != null) {
                    excludeOtherGroupCheckBoxes(allCheckbox.getCheckBox());
                }
            }
        }
    }

    private void excludeOtherGroupCheckBoxes(CheckBox checkBox) {
        String groupId = checkBox.getElement().getAttribute(GROUP_ID_ATTR);
        if (groupId != null) {
            for (String curGroupId : allCheckboxes.keySet()) {
                if (!groupId.equals(curGroupId)) {
                    AllCheckbox allCheckbox = allCheckboxes.get(curGroupId);
                    allCheckbox.setValue(false);
                }
            }
        }
    }
}

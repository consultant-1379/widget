package com.ericsson.eniq.events.widgets.client.overlay.groups;

import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanel;
import com.google.gwt.user.client.ui.CheckBox;

import java.util.List;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public interface IGroupHandler {

    String UNGROUPED = "Uncategorized";

    void setEnabled(boolean isEnabled);

    /**
     * Note: {@link com.google.gwt.user.client.ui.RadioButton} is {@link com.google.gwt.user.client.ui.CheckBox} as
     * well.
     *
     * @param groupId  group ID
     * @param checkBox check box or radio button
     *
     * @return not <tt>null</tt> result group id
     */
    String addItem(String groupId, CheckBox checkBox);

    List<CollapsePanel> getCollapsePanels();

    void setPanelsCollapsed(boolean isCollapsed);

    boolean isAnyPanelExpanded();

    boolean canSelectAllGroupsItems();

    String getGroupIdByCheckBox(CheckBox checkBox);

    void clear();
}
package com.ericsson.eniq.events.widgets.client.overlay.groups;

import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanel;
import com.ericsson.eniq.events.widgets.client.overlay.IWizardOverlayPanel;
import com.ericsson.eniq.events.widgets.client.overlay.translators.GroupTranslator;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

import java.util.*;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public class SimpleGroupHandler<T, G> implements IGroupHandler {

    protected final IWizardOverlayPanel<T> overlayPanel;

    private CollapsePanel ungroupedPanel;

    private final List<CollapsePanel> collapsePanels = new ArrayList<CollapsePanel>(2);

    private transient List<CollapsePanel> unmodifiableCollapsePanelsList;

    private final Map<String, Panel> groupPanels = new HashMap<String, Panel>(2);

    protected final Map<String, String> groups = new HashMap<String, String>(2);

    protected final Map<CheckBox, String> checkBoxToGroupIdMap = new HashMap<CheckBox, String>(8);

    public SimpleGroupHandler(final IWizardOverlayPanel<T> overlayPanel,
            final GroupTranslator<G> groupResolver, final G[] groups) {
        this.overlayPanel = overlayPanel;

        boolean hasUncategorized = false;
        for (final G group : groups) {
            final String id = groupResolver.getId(group);
            final String name = groupResolver.getName(group);

            if (UNGROUPED.equals(id)) {
                hasUncategorized = true;
            }

            this.groups.put(id, name);
        }

        if (!hasUncategorized) {
            this.groups.put(UNGROUPED, UNGROUPED);
        }
    }

    @Override public void setEnabled(final boolean isEnabled) {
        for (final CollapsePanel collapsePanel : collapsePanels) {
            collapsePanel.setEnabled(isEnabled);
        }

        if (ungroupedPanel != null) {
            ungroupedPanel.setEnabled(isEnabled);
        }
    }

    @Override public String addItem(String groupId, final CheckBox checkBox) {
        if (groupId == null || groupId.isEmpty()) {
            groupId = UNGROUPED;
        }

        String group = groups.get(groupId);
        if (group != null) {
            final Panel groupContentPanel = getGroupContentPanel(groupId);
            groupContentPanel.add(checkBox);

            checkBoxToGroupIdMap.put(checkBox, groupId);

            onItemAdd(groupId, checkBox);
        } else {
            throw new RuntimeException("Group with id: " + groupId + " is not in the provided list of groups");
        }

        return groupId;
    }

    @Override public String getGroupIdByCheckBox(CheckBox checkBox) {
        return checkBoxToGroupIdMap.get(checkBox);
    }

    @Override public List<CollapsePanel> getCollapsePanels() {
        if (unmodifiableCollapsePanelsList != null) {
            return unmodifiableCollapsePanelsList;
        }

        final List<CollapsePanel> panels;
        if (ungroupedPanel != null) {
            // In case we have ungrouped panel, it always has to be the last collapse panel
            panels = new ArrayList<CollapsePanel>(collapsePanels);
            panels.add(ungroupedPanel);
        } else {
            panels = collapsePanels;
        }
        return unmodifiableCollapsePanelsList = Collections.unmodifiableList(panels);
    }

    @Override public void setPanelsCollapsed(final boolean isCollapsed) {
        for (CollapsePanel collapsePanel : getCollapsePanels()) {
            collapsePanel.setCollapsed(isCollapsed);
        }
    }

    @Override public boolean isAnyPanelExpanded() {
        boolean isAnyExtended = false;
        for (CollapsePanel panel : getCollapsePanels()) {
            if (!panel.isCollapsed()) {
                isAnyExtended = true;
                break;
            }
        }
        return isAnyExtended;
    }

    @Override public boolean canSelectAllGroupsItems() {
        return true;
    }

    @Override public void clear() {
        collapsePanels.clear();
        unmodifiableCollapsePanelsList = null;
        groupPanels.clear();
        checkBoxToGroupIdMap.clear();
        ungroupedPanel = null;
    }

    protected Panel getGroupContentPanel(final String groupId) {
        if (groupPanels.containsKey(groupId)) {
            return groupPanels.get(groupId);
        }

        final FlowPanel panel = new FlowPanel();

        onGroupContentPanelCreate(groupId, panel);

        // Make checkboxes panel offset for 20px
        panel.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);

        final CollapsePanel collapsePanel = new CollapsePanel();
        collapsePanel.setContent(panel);
        collapsePanel.setText(groups.get(groupId));
        collapsePanel.setEnabled(overlayPanel.isEnabled());

        groupPanels.put(groupId, panel);

        if (!UNGROUPED.equals(groupId)) {
            collapsePanels.add(collapsePanel);
        } else {
            ungroupedPanel = collapsePanel;
        }
        unmodifiableCollapsePanelsList = null;

        return panel;
    }

    protected void onGroupContentPanelCreate(String groupId, FlowPanel panel) {
    }

    protected void onItemAdd(String groupId, CheckBox checkBox) {
    }
}
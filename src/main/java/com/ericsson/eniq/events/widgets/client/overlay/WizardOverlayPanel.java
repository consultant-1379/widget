package com.ericsson.eniq.events.widgets.client.overlay;

import com.ericsson.eniq.events.widgets.client.checkable.AllCheckbox;
import com.ericsson.eniq.events.widgets.client.checkable.CheckBoxWithData;
import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanel;
import com.ericsson.eniq.events.widgets.client.overlay.caching.SelectedItemsCache;
import com.ericsson.eniq.events.widgets.client.overlay.caching.SelectedItemsCacheHandler;
import com.ericsson.eniq.events.widgets.client.overlay.translators.GroupTranslator;
import com.ericsson.eniq.events.widgets.client.overlay.translators.ItemTranslator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.*;

public class WizardOverlayPanel<T> extends Composite {

    private static WizardOverlayPanelUiBinder uiBinder = GWT.create(WizardOverlayPanelUiBinder.class);

    @UiField
    AllCheckbox allCheckBox;

    @UiField
    FlowPanel content;

    @UiField
    PanelStyle style;

    private boolean enabled = true;

    private boolean cleared = true;

    /*LG edit*/
    private final static int MAX_TEXT_LENGTH_FOR_CB = 25;

    public final static String ELLIPSE = "...";

    private int width = 240; /*LG EDIT*/

    private List<T> items = new ArrayList<T>();

    private final List<CheckBoxWithData<T>> checkboxes = new ArrayList<CheckBoxWithData<T>>();

    private final ItemTranslator<T> itemResolver;
    private final SelectedItemsCache<T> cache;

    private GroupHandler<?> groupHandler;
    private SelectedItemsCacheHandler<T> selectedItemsCacheHandler;

    public WizardOverlayPanel(final ItemTranslator<T> itemResolver, SelectedItemsCache<T> cache) {
        this.itemResolver = itemResolver;
        this.cache = cache;

        initWidget(uiBinder.createAndBindUi(this));

        // By default disable allCheckBox
        allCheckBox.setEnabled(false);
    }

    public void setEnabled(final boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }

        this.enabled = enabled;

        if (items != null && !items.isEmpty()) {
            allCheckBox.setEnabled(enabled);
        }

        for (CheckBox checkBox : allCheckBox.getChildren()) {
            checkBox.setEnabled(enabled);
        }

        if (groupHandler != null) {
            groupHandler.setEnabled(enabled);
        }
    }

    public void setItemWidth(final int width) {
        if (this.width == width) {
            return;
        }

        // Update existing items width + store a field to update any newcomers
        this.width = width;

        // See if it is defined correctly, maybe wrapper element is needed
        for (CheckBox checkBox : allCheckBox.getChildren()) {
            checkBox.setWidth(width + "px");
        }
    }

    /**
     * Groups has to be set before update() is called
     *
     * @param groupResolver
     * @param groups
     */
    public <G> void setGroups(final GroupTranslator<G> groupResolver, final List<G> groups) {
        groupHandler = new GroupHandler<G>(groupResolver, groups);
    }

    public void setSelectedItemsCacheHandler(final SelectedItemsCacheHandler<T> selectedItemsCacheHandler) {
        this.selectedItemsCacheHandler = selectedItemsCacheHandler;
    }

    public void update(final T[] items) {
        List<T> itemsForCache = null;
        if (!this.items.isEmpty() && items != null && items.length != 0) {
            itemsForCache = selectedItemsCacheHandler.getSelectedForCache(cache.getSelected(), items);
        }

        if (!cleared) {
            clear();
        }

        // Here you can clear old items, if there are any, as it is after caching has been handled
        this.items.clear();
        this.checkboxes.clear();

        if (items == null || items.length == 0) {
            return;
        }

        Collections.addAll(this.items, items);

        if (enabled) {
            allCheckBox.setEnabled(true);
        }

        // Find out if there are any items, that are grouped
        boolean hasGroups = false;
        for (T item : items) {
            final String groupId = itemResolver.getGroupId(item);
            if (groupId != null && !groupId.isEmpty()) {
                hasGroups = true;
            }
        }

        for (T item : items) {
            final CheckBoxWithData<T> chk = createCheckBox(item);

            final String groupId = itemResolver.getGroupId(item);
            if (groupHandler != null && hasGroups) {
                groupHandler.addItem(groupId, chk);
            } else if (groupId != null && !groupId.isEmpty()) {
                throw new RuntimeException(
                        "Groups are not defined for Wizard Overlay, please use setGroups() before calling update()");
            } else {
                content.add(chk);
            }

            checkboxes.add(chk);
            allCheckBox.registerChild(chk);
        }

        if (groupHandler != null && hasGroups) {
            final List<CollapsePanel> colapsePanels = groupHandler.getColapsePanels();
            for (CollapsePanel colapsePanel : colapsePanels) {
                content.add(colapsePanel);
            }
        }

        // Remember & select items, that are in both, previous and updated list
        if (itemsForCache != null) {
            for (T item : itemsForCache) {
                for (CheckBoxWithData<T> checkbox : checkboxes) {
                    if (checkbox.getData().equals(item)) {
                        checkbox.setValue(Boolean.TRUE);
                    }
                }
            }
        }

        cleared = false;
    }

    private CheckBoxWithData<T> createCheckBox(final T item) {

        /*LG EDIT format the label like other wizards*/
        final String label = formatLength(itemResolver.getLabel(item));

        String tooltip = itemResolver.getTooltip(item);

        if (tooltip == null || "".equals(tooltip)) {
            tooltip = label;
        }

        final CheckBoxWithData<T> chk = new CheckBoxWithData<T>(label, item);
        chk.addStyleName(style.item());
        chk.setWidth(width + "px");
        chk.setTitle(tooltip);
        chk.setEnabled(enabled);

        return chk;
    }

    public void clear() {
        allCheckBox.clear();
        content.clear();

        // Do not clear items, as they are used for caching (when new data comes in,
        // compare it and check the ones that were in old data set)

        if (groupHandler != null) {
            groupHandler.clear();
        }

        cleared = true;
    }

    public List<T> getSelected() {
        final List<T> selected = new ArrayList<T>();
        for (CheckBoxWithData<T> checkbox : checkboxes) {
            if (checkbox.getValue()) {
                final T data = checkbox.getData();
                selected.add(data);
            }
        }

        return selected;
    }

    AllCheckbox getAllCheckbox() {
        return allCheckBox;
    }

    interface WizardOverlayPanelUiBinder extends UiBinder<Widget, WizardOverlayPanel> {
    }

    private class GroupHandler<G> {

        private static final String UNGROUPED = "Uncategorized";

        private CollapsePanel ungroupedPanel;

        private final List<CollapsePanel> colapsePanels = new ArrayList<CollapsePanel>();

        private final Map<String, AllCheckbox> allCheckboxes = new HashMap<String, AllCheckbox>();

        private final Map<String, Panel> groupPanels = new HashMap<String, Panel>();

        private final Map<String, String> groups = new HashMap<String, String>();

        private GroupHandler(final GroupTranslator<G> groupResolver, final List<G> groups) {

            boolean hasUncategorized = false;
            for (G group : groups) {
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

        public void setEnabled(final boolean enabled) {
            for (CollapsePanel colapsePanel : colapsePanels) {
                colapsePanel.setEnabled(enabled);
            }
            for (AllCheckbox allCheckbox : allCheckboxes.values()) {
                allCheckbox.setEnabled(enabled);
            }

            if (ungroupedPanel != null) {
                ungroupedPanel.setEnabled(enabled);
            }
        }

        public void clear() {
            colapsePanels.clear();
            allCheckboxes.clear();
            groupPanels.clear();

            ungroupedPanel = null;
        }

        public void addItem(String groupId, final CheckBox box) {
            if (groupId == null || groupId.isEmpty()) {
                groupId = UNGROUPED;
            }

            if (groups.containsKey(groupId)) {
                final Panel groupContentPanel = getGroupContentPanel(groupId);
                groupContentPanel.add(box);

                allCheckboxes.get(groupId).registerChild(box);
            } else {
                throw new RuntimeException("Group with id: " + groupId + " is not in the provided list of groups");
            }
        }

        public List<CollapsePanel> getColapsePanels() {
            if (ungroupedPanel != null) {
                // In case we have ungrouped panel, it always has to be the last collapse panel
                final List<CollapsePanel> panels = new ArrayList<CollapsePanel>(colapsePanels);
                panels.add(ungroupedPanel);
                return panels;
            }

            return colapsePanels;
        }

        private Panel getGroupContentPanel(final String groupId) {
            if (groupPanels.containsKey(groupId)) {
                return groupPanels.get(groupId);
            }

            final AllCheckbox chkAll = new AllCheckbox();
            chkAll.addStyleName(style.item());
            chkAll.setWidth(width + "px");
            chkAll.setEnabled(enabled);

            final FlowPanel panel = new FlowPanel();
            panel.add(chkAll);

            // Make checkboxes panel offset for 20px
            panel.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);

            final CollapsePanel collapsePanel = new CollapsePanel();
            collapsePanel.setContent(panel);
            collapsePanel.setText(groups.get(groupId));
            collapsePanel.setEnabled(enabled);

            allCheckBox.registerChild(chkAll.getCheckBox());

            allCheckboxes.put(groupId, chkAll);
            groupPanels.put(groupId, panel);

            if (!UNGROUPED.equals(groupId)) {
                colapsePanels.add(collapsePanel);
            } else {
                ungroupedPanel = collapsePanel;
            }

            return panel;
        }
    }

    private String formatLength(final String header) {

        if (header.length() > MAX_TEXT_LENGTH_FOR_CB) {
            return header.substring(0, MAX_TEXT_LENGTH_FOR_CB) + ELLIPSE;
        }
        return header;
    }

    interface PanelStyle extends CssResource {

        String wrapper();

        String allCheckbox();

        String item();

        String content();
    }
}

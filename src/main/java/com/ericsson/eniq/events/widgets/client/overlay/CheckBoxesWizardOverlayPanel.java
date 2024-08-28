package com.ericsson.eniq.events.widgets.client.overlay;

import com.ericsson.eniq.events.widgets.client.checkable.AllCheckbox;
import com.ericsson.eniq.events.widgets.client.checkable.CheckBoxWithData;
import com.ericsson.eniq.events.widgets.client.checkable.ICheckable;
import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEventHandler;
import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanel;
import com.ericsson.eniq.events.widgets.client.overlay.groups.AllCheckBoxesGroupHandler;
import com.ericsson.eniq.events.widgets.client.overlay.groups.IGroupHandler;
import com.ericsson.eniq.events.widgets.client.overlay.translators.GroupTranslator;
import com.ericsson.eniq.events.widgets.client.overlay.translators.ItemTranslator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;

import java.util.Collections;
import java.util.List;
import java.util.Map;

// todo: [ref] move common parts

/**
 * Base content of wizard with check boxes.
 * <p/>
 * Note: does not include radio buttons for Views - e.g. for Chart, Map, Grid, etc.
 *
 * @param <T> an object with data for one particular checkbox; e.g. {@link com.ericsson.eniq.events.common.client.json.IJSONObject}
 * @author edavboj - Davis Bojars
 * @author ealeerm - Alexey Ermykin
 */
public class CheckBoxesWizardOverlayPanel<T> extends AWizardOverlayPanel<T> {

    private static WizardOverlayPanelUiBinder uiBinder = GWT.create(WizardOverlayPanelUiBinder.class);

    /**
     * It is global checkbox for all sections/groups.
     */
    @UiField AllCheckbox globalAllCheckBox;

    @UiField FlowPanel content;

    @UiField PanelStyle style;

    public CheckBoxesWizardOverlayPanel(final ItemTranslator<T> itemResolver) {
        super(itemResolver);

        initWidget(uiBinder.createAndBindUi(this));

        // By default disable globalAllCheckBox
        globalAllCheckBox.setEnabled(false);
    }

    @Override public void setEnabled(final boolean isEnabled) {
        if (isEnabled() == isEnabled) {
            return;
        }

        super.setEnabled(isEnabled);

        if (items != null && !items.isEmpty()) {
            globalAllCheckBox.setEnabled(isEnabled);
        }

        for (final CheckBox checkBox : globalAllCheckBox.getChildren()) {
            checkBox.setEnabled(isEnabled);
        }

        for (final AllCheckbox checkBox : globalAllCheckBox.getAllCheckBoxChildrenCollection()) {
            checkBox.setEnabled(isEnabled);
        }
    }

    @Override public void setItemWidth(final int width) {
        if (getItemWidth() == width) {
            return;
        }

        super.setItemWidth(width);

        // See if it is defined correctly, maybe wrapper element is needed
        for (final CheckBox checkBox : globalAllCheckBox.getChildren()) {
            checkBox.setWidth(width + "px");
        }
    }

    /**
     * Groups has to be set before {@link CheckBoxesWizardOverlayPanel#update(Object[])} is called
     *
     * @param groupResolver translator for groups
     * @param groups        array of groups
     */
    @Override public <G> void setGroups(final GroupTranslator<G> groupResolver, G[] groups) {
        setGroupHandler(new AllCheckBoxesGroupHandler<T, G>(this, groupResolver, groups));
    }

    @Override public void setGroupHandler(IGroupHandler groupHandler) {
        super.setGroupHandler(groupHandler);
        if (!groupHandler.canSelectAllGroupsItems()) {
            globalAllCheckBox.removeFromParent();
        }
    }

    /**
     * @param items array of check boxes
     */
    @Override public void update(final T[] items) {
        List<T> itemsForCache = null;
        if (!this.items.isEmpty() && items != null && items.length != 0) {
            itemsForCache = getItemsForCache(items);
        }

        if (!isCleared) {
            clear();
        }
        clearItems();

        if (items == null || items.length == 0) {
            return;
        }

        Collections.addAll(this.items, items);

        if (isEnabled()) {
            globalAllCheckBox.setEnabled(true);
        }

        // Find out if there are any items, that are grouped
        boolean hasGroups = false;
        for (final T item : this.items) {
            final String groupId = itemResolver.getGroupId(item);
            if (groupId != null && !groupId.isEmpty()) {
                hasGroups = true;
                break;
            }
        }

        for (final T item : this.items) {
            final ICheckable<T> checkable = createCheckable(item);

            String groupId = itemResolver.getGroupId(item);
            if (hasGroups && getGroupHandler() != null) {
                CheckBox uiItem = checkable.getUiItem();
                groupId = getGroupHandler().addItem(groupId, uiItem);
                handleGroupSwitches(groupId, uiItem);
            } else if (groupId != null && !groupId.isEmpty()) {
                throw new RuntimeException(
                        "Groups are not defined for Wizard Overlay, please use setGroups() before calling update()");
            } else {
                content.add(checkable.getUiItem());
            }

            checkableItems.add(checkable);
            globalAllCheckBox.registerChild(checkable.getUiItem());
        }

        Map<String, AllCheckbox> allCheckBoxChildren = globalAllCheckBox.getAllCheckBoxChildren();
        for (String groupId : allCheckBoxChildren.keySet()) {
            AllCheckbox allCheckbox = allCheckBoxChildren.get(groupId);
            handleGroupSwitches(groupId, allCheckbox.getCheckBox());
        }

        if (hasGroups && getGroupHandler() != null) {
            final List<CollapsePanel> collapsePanels = getGroupHandler().getCollapsePanels();
            for (final CollapsePanel collapsePanel : collapsePanels) {
                content.add(collapsePanel);
            }
        }

        // Remember & select items, that are in both, previous and updated list
        if (itemsForCache != null) {
            for (final T item : itemsForCache) {
                for (final ICheckable<T> checkable : checkableItems) {
                    if (checkable.getData().equals(item)) {
                        checkable.setValue(Boolean.TRUE);
                    }
                }
            }
        }

        isCleared = false;
    }

    @Override public void setContentStyle(final String style) {
        content.setStyleName(style);
    }

    @Override public HandlerRegistration addChildSelectEventHandler(ChildSelectEventHandler childSelectEventHandler) {
        return globalAllCheckBox.addChildSelectEventHandler(childSelectEventHandler);
    }

    @Override protected ICheckable<T> instantiateCheckable(T item, String label) {
        return new CheckBoxWithData<T>(label, item);
    }

    @Override public void adjustGroupItemStyle(UIObject selectableItem) {
        selectableItem.addStyleName(style.item());
        selectableItem.addStyleName(style.groupAllCheckbox());
    }

    @Override public void registerGroupItem(String groupId, UIObject selectableItem) {
        globalAllCheckBox.registerChild(groupId, (AllCheckbox) selectableItem);
    }

    @Override protected String getItemStyle() {
        return style.item();
    }

    @Override protected FlowPanel getContent() {
        return content;
    }

    @Override public void clear() {
        globalAllCheckBox.clear();
        super.clear();
    }

    public interface PanelStyle extends CssResource {
        String wrapper();

        String allCheckbox();

        String item();

        String content();

        String groupAllCheckbox();
    }

    public interface WizardOverlayPanelUiBinder extends UiBinder<Widget, CheckBoxesWizardOverlayPanel> {
    }
}

package com.ericsson.eniq.events.widgets.client.overlay;

import com.ericsson.eniq.events.widgets.client.checkable.AllCheckableWatcher;
import com.ericsson.eniq.events.widgets.client.checkable.ICheckable;
import com.ericsson.eniq.events.widgets.client.checkable.RadioButtonWithData;
import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEventHandler;
import com.ericsson.eniq.events.widgets.client.collapse.CollapsePanel;
import com.ericsson.eniq.events.widgets.client.overlay.groups.SimpleGroupHandler;
import com.ericsson.eniq.events.widgets.client.overlay.translators.GroupTranslator;
import com.ericsson.eniq.events.widgets.client.overlay.translators.ItemTranslator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;

import java.util.Collections;
import java.util.List;

/**
 * Base content of wizard with radio buttons.
 * <p/>
 * Note: does not include radio buttons for Views - e.g. for Chart, Map, Grid, etc.
 *
 * @param <T> an object with data for one particular radio button; e.g. {@link com.ericsson.eniq.events.common.client.json.IJSONObject}
 * @author ealeerm - Alexey Ermykin
 */
public class RadioButtonWizardOverlayPanel<T> extends AWizardOverlayPanel<T> {

    private static WizardOverlayPanelUiBinder uiBinder = GWT.create(WizardOverlayPanelUiBinder.class);

    @UiField FlowPanel content;

    @UiField PanelStyle style;

    private AllCheckableWatcher allCheckableWatcher = new AllCheckableWatcher();

    public RadioButtonWizardOverlayPanel(final ItemTranslator<T> itemResolver) {
        super(itemResolver);
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Groups has to be set before update() is called
     *
     * @param groupResolver translator for groups
     * @param groups        list of groups
     */
    @Override public <G> void setGroups(final GroupTranslator<G> groupResolver, final G[] groups) {
        setGroupHandler(new SimpleGroupHandler<T, G>(this, groupResolver, groups));
    }

    /**
     * @param items array of radio buttons
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

/*      todo
        if (isEnabled() && !this.items.isEmpty()) {
            globalAllCheckBox.setEnabled(true);
        }
*/

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

            final String groupId = itemResolver.getGroupId(item);
            if (hasGroups && getGroupHandler() != null) {
                getGroupHandler().addItem(groupId, checkable.getUiItem());
            } else if (groupId != null && !groupId.isEmpty()) {
                throw new RuntimeException(
                        "Groups are not defined for Wizard Overlay, please use setGroups() before calling update()");
            } else {
                content.add(checkable.getUiItem());
            }

            checkableItems.add(checkable);
            allCheckableWatcher.registerChild(checkable.getUiItem());
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

    @Override public HandlerRegistration addChildSelectEventHandler(final ChildSelectEventHandler eventHandler) {
        return allCheckableWatcher.addChildSelectEventHandler(eventHandler);
    }

    @Override protected ICheckable<T> instantiateCheckable(T item, String label) {
        return new RadioButtonWithData<T>(itemResolver.getGroupId(item), label, item);
    }

    @Override public void adjustGroupItemStyle(UIObject selectableItem) {
        selectableItem.addStyleName(style.item());
        // selectableItem.addStyleName(style.groupAllCheckbox());
        // todo
    }

    @Override public void registerGroupItem(String groupId, UIObject selectableItem) {
    }

    @Override protected String getItemStyle() {
        return style.item();
    }

    @Override protected FlowPanel getContent() {
        return content;
    }

    public interface PanelStyle extends CssResource {
        String wrapper();
        String item();
        String content();
    }

    public interface WizardOverlayPanelUiBinder extends UiBinder<Widget, RadioButtonWizardOverlayPanel> {
    }
}

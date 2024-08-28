package com.ericsson.eniq.events.widgets.client.overlay;

import com.ericsson.eniq.events.widgets.client.checkable.ICheckable;
import com.ericsson.eniq.events.widgets.client.checkable.event.ChildSelectEventHandler;
import com.ericsson.eniq.events.widgets.client.overlay.caching.SelectedItemsCache;
import com.ericsson.eniq.events.widgets.client.overlay.caching.SelectedItemsCacheHandler;
import com.ericsson.eniq.events.widgets.client.overlay.events.WizardDblKeyChangeEvent;
import com.ericsson.eniq.events.widgets.client.overlay.events.WizardDblKeyChangeEventHandler;
import com.ericsson.eniq.events.widgets.client.overlay.groups.CurrentGroupIdHolder;
import com.ericsson.eniq.events.widgets.client.overlay.groups.IGroupHandler;
import com.ericsson.eniq.events.widgets.client.overlay.translators.ItemTranslator;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.web.bindery.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public abstract class AWizardOverlayPanel<T>
        extends Composite implements IWizardOverlayPanel<T> {

    private int width = 240;  /*LG EDIT*/

    protected boolean isCleared = true;

    protected List<T> items = new ArrayList<T>();

    protected final List<ICheckable<T>> checkableItems = new ArrayList<ICheckable<T>>();

    protected final List<T> selectedCheckableItems = new ArrayList<T>(1); // for performance

    protected final ItemTranslator<T> itemResolver;

    private Map<String, CheckableUiItemChangeHandler> uiItemChangeHandlerMap;

    protected CurrentGroupIdHolder currentGroupIdHolder;

    private boolean areGroupSwitchesFired = false;

    private IGroupHandler groupHandler;

    private boolean isEnabled = true;

    private SelectedItemsCache<T> cache;

    private SelectedItemsCacheHandler<T> selectedItemsCacheHandler;

    // Cache for selected items, as when you request from panel, you get currently selected, but we have to store
    // but before clicking launch Button, have refreshed it or updated time period
    // selected items when launchButton has been clicked to avoid situation, when you have changed something in overlay
    private List<T> cachedSelectedItems;

    private int maxTextLengthForCheckItem = DEFAULT_MAX_TEXT_LENGTH_FOR_CHECK_ITEM;

    protected AWizardOverlayPanel(ItemTranslator<T> itemResolver) {
        this.itemResolver = itemResolver;
    }

    public abstract HandlerRegistration addChildSelectEventHandler(ChildSelectEventHandler childSelectEventHandler);

    protected abstract String getItemStyle();

    protected abstract ICheckable<T> instantiateCheckable(T item, String label);

    protected abstract Panel getContent();

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(final boolean isEnabled) {
        if (isEnabled() == isEnabled) {
            return;
        }

        this.isEnabled = isEnabled;

        if (groupHandler != null) {
            groupHandler.setEnabled(isEnabled);
        }
    }

    @Override
    public void setGroupsCollapsed(final boolean isCollapsed) {
        if (groupHandler != null) {
            groupHandler.setPanelsCollapsed(isCollapsed);
        }
    }

    @Override
    public void setSelectedItemsCacheHandler(final SelectedItemsCacheHandler<T> selectedItemsCacheHandler) {
        this.selectedItemsCacheHandler = selectedItemsCacheHandler;
    }

    @Override
    public void setCache(SelectedItemsCache<T> cache) {
        this.cache = cache;
    }

    @Override
    public int getItemWidth() {
        return width;
    }

    @Override
    public void setItemWidth(final int width) {
        if (this.width == width) {
            return;
        }

        // Update existing items width + store a field to update any newcomers
        this.width = width;

        // See if it is defined correctly, maybe wrapper element is needed
        for (ICheckable<T> checkableItem : checkableItems) {
            checkableItem.getUiItem().setWidth(width + "px");
        }
    }

    @Override
    public List<T> getSelected() {
        selectedCheckableItems.clear();

        for (final ICheckable<T> checkbox : checkableItems) {
            if (checkbox.getValue()) {
                final T data = checkbox.getData();
                selectedCheckableItems.add(data);
            }
        }

        return selectedCheckableItems;
    }

    @Override
    public void setGroupHandler(IGroupHandler groupHandler) {
        this.groupHandler = groupHandler;
    }

    @Override
    public String getCurrentGroupId() {
        if (getGroupHandler() == null) {
            return null;
        }

        String groupId = null;
        for (ICheckable<T> item : checkableItems) {
            CheckBox checkBox = item.getUiItem();
            if (checkBox.getValue()) {
                groupId = getGroupHandler().getGroupIdByCheckBox(checkBox);
                if (groupId != null) {
                    break;
                }
            }
        }
        return groupId;
    }

    @Override
    public List<ICheckable<T>> getCheckableElements() {
        return checkableItems;
    }

    @Override
    public void clear() {
        getContent().clear();

        // Do not clear items, as they are used for caching (when new data comes in,
        // compare it and check the ones that were in old data set)
        if (groupHandler != null) {
            groupHandler.clear();
        }

        isCleared = true;
    }

    public void setMaxTextLengthForCheckItem(int maxTextLengthForCheckItem) {
        this.maxTextLengthForCheckItem = maxTextLengthForCheckItem;
    }

    /**
     * Use the method to have the method {@link #onGroupChange(String, String)} invoked.
     * <p/>
     * Note: the method {@link #onGroupChange(String, String)} should be overriden and implemented to fulfil the
     * appropriate logic.
     *
     * @param areGroupSwitchesFired should group switched be fired by invoking method {@link #onGroupChange(String,
     *                              String)} or not
     * @see #onGroupChange(String, String)
     */
    public void setAreGroupSwitchesFired(boolean areGroupSwitchesFired) {
        this.areGroupSwitchesFired = areGroupSwitchesFired;
        if (areGroupSwitchesFired) {
            String groupId = getCurrentGroupId();
            currentGroupIdHolder = new CurrentGroupIdHolder(groupId);
        } else {
            currentGroupIdHolder = null;
        }
    }

    /**
     * @param handler handler of group switches
     * @see #onGroupChange(String, String)
     */
    public HandlerRegistration addGroupChangeHandler(WizardDblKeyChangeEventHandler handler) {
       return addHandler(handler, WizardDblKeyChangeEvent.getType());
    }

    /**
     * To have the notifications - call method {@link #setAreGroupSwitchesFired(boolean)} before and override this
     * method in subclasses.
     * <p/>
     * Note: it is invoked if <tt>oldGroupId</tt> not equal to <tt>newGroupId</tt>.
     *
     * @param oldGroupId old group id
     * @param newGroupId new group id
     * @see #setAreGroupSwitchesFired(boolean)
     * @see #addGroupChangeHandler(com.ericsson.eniq.events.widgets.client.overlay.events.WizardDblKeyChangeEventHandler)
     */
    protected void onGroupChange(String oldGroupId, String newGroupId) {
    }

    protected String formatLength(final String header) {
        if (header.length() > maxTextLengthForCheckItem) {
            return header.substring(0, maxTextLengthForCheckItem) + ELLIPSE;
        }
        return header;
    }

    protected IGroupHandler getGroupHandler() {
        return groupHandler;
    }

    /**
     * Stores selected items for refresh & other cases.
     *
     * @return cached selected items
     */
    protected List<T> storeSelectedItems() {
        cachedSelectedItems = getSelected();
        return cachedSelectedItems;
    }

    protected List<T> getCachedSelectedItems() {
        if (cachedSelectedItems == null) {
            return getSelected();
        }
        return cachedSelectedItems;
    }

    protected ICheckable<T> createCheckable(final T item) {
        /*LG EDIT format the label like other wizards*/
        final String label = formatLength(itemResolver.getLabel(item));

        String tooltip = itemResolver.getTooltip(item);

        if (tooltip == null || "".equals(tooltip)) {
            tooltip = label;
        }

        final ICheckable<T> checkable = instantiateCheckable(item, label);
        CheckBox checkBox = checkable.getUiItem();

        checkBox.addStyleName(getItemStyle());
        checkBox.setWidth(getItemWidth() + "px");
        checkBox.setTitle(tooltip);
        checkBox.setEnabled(isEnabled);
        checkBox.setFormValue(itemResolver.getId(item));

        return checkable;
    }

    protected List<T> getItemsForCache(T[] items) {
        return selectedItemsCacheHandler.getSelectedForCache(cache.getSelected(), items);
    }

    protected void clearItems() {
        // Here you can clear old items, if there are any, as it is after caching has been handled
        this.items.clear();
        this.checkableItems.clear();
        this.selectedCheckableItems.clear();
        if (uiItemChangeHandlerMap != null) {
            uiItemChangeHandlerMap.clear();
        }
    }

    protected void handleGroupSwitches(String groupId, CheckBox checkableUiItem) {
        CheckableUiItemChangeHandler handler = getGroupSwitchesHandler(groupId);
        if (handler == null) {
            return;
        }

        checkableUiItem.addValueChangeHandler(handler);
    }

    protected CheckableUiItemChangeHandler getGroupSwitchesHandler(String groupId) {
        if (!areGroupSwitchesFired) {
            return null;
        }

        if (uiItemChangeHandlerMap == null) {
            uiItemChangeHandlerMap = new HashMap<String, CheckableUiItemChangeHandler>(3);
        }

        if (currentGroupIdHolder == null) {
            currentGroupIdHolder = new CurrentGroupIdHolder();
        }

        CheckableUiItemChangeHandler handler = uiItemChangeHandlerMap.get(groupId);
        if (handler == null) {
            handler = new CheckableUiItemChangeHandler(groupId, currentGroupIdHolder);
            uiItemChangeHandlerMap.put(groupId, handler);
        }

        return handler;
    }

    /**
     * Needs to track group changes.
     */
    protected class CheckableUiItemChangeHandler implements ValueChangeHandler<Boolean> {

        private final String groupId;
        private final CurrentGroupIdHolder currentGroupIdHolder;

        protected CheckableUiItemChangeHandler(String group, CurrentGroupIdHolder currentGroupIdHolder) {
            this.groupId = group;
            this.currentGroupIdHolder = currentGroupIdHolder;
        }

        @Override public void onValueChange(ValueChangeEvent<Boolean> ignored) {
            String currentGroupId = currentGroupIdHolder.getGroupId();
            if (!groupId.equals(currentGroupId)) {
                currentGroupIdHolder.setGroupId(groupId);
                onGroupChange(currentGroupId, groupId);
            }
        }
    }
}
package com.ericsson.eniq.events.widgets.client.overlay;

import com.ericsson.eniq.events.widgets.client.checkable.ICheckable;
import com.ericsson.eniq.events.widgets.client.overlay.caching.SelectedItemsCache;
import com.ericsson.eniq.events.widgets.client.overlay.caching.SelectedItemsCacheHandler;
import com.ericsson.eniq.events.widgets.client.overlay.groups.IGroupHandler;
import com.ericsson.eniq.events.widgets.client.overlay.translators.GroupTranslator;
import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsRenderable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;

import java.util.List;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public interface IWizardOverlayPanel<T> extends HasVisibility, EventListener, HasAttachHandlers, IsWidget, IsRenderable {

    /*LG edit*/
    int DEFAULT_MAX_TEXT_LENGTH_FOR_CHECK_ITEM = 25;

    String ELLIPSE = "...";

    void setCache(SelectedItemsCache<T> cache);

    boolean isEnabled();

    void setEnabled(boolean isEnabled);

    void setGroupsCollapsed(boolean isCollapsed);

    int getItemWidth();

    void setItemWidth(int width);

    /**
     * Look usage of method <tt>setGroupHandler(...)</tt> cause it sets up group handler as well.
     *
     * @param groupResolver group translator / resolver
     * @param groups        array of groups
     * @param <G>           group type
     * @see #setGroupHandler(com.ericsson.eniq.events.widgets.client.overlay.groups.IGroupHandler)
     */
    <G> void setGroups(GroupTranslator<G> groupResolver, G[] groups);

    /**
     * Look usage of method <tt>setGroups(...)</tt> cause it sets up group handler as well.
     *
     * @param groupHandler group handler
     * @see #setGroups(com.ericsson.eniq.events.widgets.client.overlay.translators.GroupTranslator, Object[])
     */
    void setGroupHandler(IGroupHandler groupHandler);

    String getCurrentGroupId();

    void setSelectedItemsCacheHandler(SelectedItemsCacheHandler<T> selectedItemsCacheHandler);

    /**
     * @param items array of update items
     */
    void update(T[] items);

    List<T> getSelected();

    void setContentStyle(String style);

    void adjustGroupItemStyle(UIObject selectableItem);

    void registerGroupItem(String groupId, UIObject selectableItem);

    List<ICheckable<T>> getCheckableElements();

    void clear();
}

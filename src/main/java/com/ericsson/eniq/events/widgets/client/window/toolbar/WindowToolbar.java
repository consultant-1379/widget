package com.ericsson.eniq.events.widgets.client.window.toolbar;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public class WindowToolbar extends HorizontalPanel {
    private Map<String, AbstractToolbarItem> itemMap;

    private HorizontalPanel leftToolbar;

    private HorizontalPanel rightToolbar;

    public enum Direction {
        LEFT, RIGHT
    }

    private void init() {
        itemMap = new HashMap<String, AbstractToolbarItem>();
        leftToolbar = new HorizontalPanel();
        rightToolbar = new HorizontalPanel();
        leftToolbar.setHeight("100%");
        rightToolbar.setHeight("100%");
        setVisible(true);
        add(leftToolbar);
        setCellHorizontalAlignment(leftToolbar, HasAlignment.ALIGN_LEFT);
        add(rightToolbar);
        setCellHorizontalAlignment(rightToolbar, HasAlignment.ALIGN_RIGHT);
    }

    public void addItem(final AbstractToolbarItem item, final Direction direction) {
        addItem(item, direction, 24, false);
    }

    public void addItem(final AbstractToolbarItem item, final Direction direction, final int width, final boolean paired) {
        if (itemMap == null) {
            init();
        }
        if (direction == Direction.LEFT) {
            leftToolbar.add(item);
            leftToolbar.setCellWidth(item.asWidget(), width + "px");//TODO new to remove this hard coded width
            leftToolbar.setCellHorizontalAlignment(item.asWidget(), HasAlignment.ALIGN_LEFT);
            item.asWidget().getElement().getStyle().setMarginLeft(paired ? 5 : 8, Unit.PX);

        } else {
            rightToolbar.add(item);
            rightToolbar.setCellWidth(item.asWidget(), width + "px");
            rightToolbar.setCellHorizontalAlignment(item.asWidget(), HasAlignment.ALIGN_RIGHT);
            item.asWidget().getElement().getStyle().setMarginRight(paired ? 5 : 8, Unit.PX);
        }
        itemMap.put(item.getItemId(), item);
    }

    public void removeItem(final String id) {
        final AbstractToolbarItem item = itemMap.get(id);
        if (!leftToolbar.remove(item)) {
            rightToolbar.remove(item);
        }
        itemMap.remove(item);
        if (itemMap.isEmpty()) {
            setVisible(false);
        }
    }

    public void setItemEnable(final String itemId, final boolean enabled) {
        itemMap.get(itemId).setEnable(enabled);
    }

    public void setItemHidden(final String itemId, final boolean hidden) {
        itemMap.get(itemId).setHidden(hidden);
    }

    public AbstractToolbarItem getToolbarItem(final String itemId) {
        return itemMap.get(itemId);
    }

    public void setMask(final boolean mask) {
        if (itemMap != null) {
            for (final String itemId : itemMap.keySet()) {
                itemMap.get(itemId).setEnable(!mask);
            }
        }
    }

}

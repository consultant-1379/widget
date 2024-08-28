package com.ericsson.eniq.events.widgets.client.window.toolbar;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * 
 * @author ekurshi
 * @since 2012
 *
 */
public abstract class AbstractToolbarItem implements IsWidget {

    private final String id;

    public AbstractToolbarItem(final String id) {
        this.id = id;
    }

    public String getItemId() {
        return id;
    }

    public abstract void setEnable(boolean enabled);

    public abstract void setHidden(boolean hidden);

    public abstract boolean isHidden();

    public abstract boolean isEnabled();

    public abstract void setToolTip(String toolTip);

    public abstract int getWidth();

    public abstract int getHeight();
}

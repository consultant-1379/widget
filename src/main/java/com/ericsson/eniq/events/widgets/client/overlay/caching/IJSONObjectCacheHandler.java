package com.ericsson.eniq.events.widgets.client.overlay.caching;

import com.ericsson.eniq.events.common.client.json.IJSONObject;

import java.util.ArrayList;
import java.util.List;

public class IJSONObjectCacheHandler implements SelectedItemsCacheHandler<IJSONObject> {

    protected static final IJSONObjectCacheHandler INSTANCE = new IJSONObjectCacheHandler();

    IJSONObjectCacheHandler() {
    }

    public static IJSONObjectCacheHandler getInstance() {
        return INSTANCE;
    }

    // Not so nice hack to compare JSONObjects, that we currently get as input for wizard overlay
    // TODO: Change it to normal compare (using retainAll) if using other type as input

    @Override
    public List<IJSONObject> getSelectedForCache(final List<IJSONObject> selected,
            final IJSONObject[] newItems) {
        List<IJSONObject> itemsForCache = null;
        if (!selected.isEmpty()) {
            final List<String> selectedStringPresentation = new ArrayList<String>();
            for (IJSONObject sel : selected) {
                selectedStringPresentation.add(sel.getNativeObject().toString());
            }

            for (IJSONObject item : newItems) {
                if (selectedStringPresentation.contains(item.getNativeObject().toString())) {
                    if (itemsForCache == null) {
                        itemsForCache = new ArrayList<IJSONObject>();
                    }

                    itemsForCache.add(item);
                }
            }
        }

        return itemsForCache;
    }
}

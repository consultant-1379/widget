package com.ericsson.eniq.events.widgets.client.overlay.caching;

import java.util.List;

public interface SelectedItemsCacheHandler<T> {

    List<T> getSelectedForCache(List<T> selected, T[] newItems);

}

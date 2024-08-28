package com.ericsson.eniq.events.widgets.client.grids.data;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author ekurshi
 * 
 */
public class DataItem {
    private final Map<String, Object> items;

    public DataItem() {
        items = new HashMap<String, Object>();
        //        final Date col3 = new Date();
        //        col3.setDate(Random.nextInt(28));
        //        col3.setMonth(Random.nextInt(11));
        //        col3.setHours(0);
        //        col3.setMinutes(0);
        //        col3.setSeconds(0);
        //        items.put("col1", DataHelper.createRandomString(" abcdefgh", 8));
        //        items.put("col2", Random.nextDouble());
        //        items.put("col3", col3);
        //        items.put("col4", Random.nextBoolean());
        //        items.put("col5", DataHelper.createRandomString("123456 abcdefgh", 8));
        //        items.put("col6", Random.nextInt(100));
    }

    @SuppressWarnings("unchecked")
    public <T> T getColumnValue(final String colID) {
        return (T) items.get(colID);
    }

    public <T> void setColumnValue(final String colID, final T val) {
        items.put(colID, val);
    }
}

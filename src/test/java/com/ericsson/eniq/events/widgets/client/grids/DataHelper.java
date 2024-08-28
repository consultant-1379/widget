package com.ericsson.eniq.events.widgets.client.grids;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import com.ericsson.eniq.events.widgets.client.grids.columns.ColumnInfo;
import com.ericsson.eniq.events.widgets.client.grids.columns.ColumnType;
import com.ericsson.eniq.events.widgets.client.grids.data.DataItem;

/**
 * 
 * @author ekurshi
 * @since 2013
 *
 */
public class DataHelper {
    public static final String INTEGER_COLUMN = "integerColumn";

    public static final String BOOLEAN_COLUMN = "booleanColumn";

    public static final String DATE_COLUMN = "dateColumn";

    public static final String DOUBLE_COLUMN = "doubleColumn";

    public static final String STRING_COLUMN = "stringColumn";

    public final static Random random = new Random();

    public static String createRandomString(final String randomString, final int length) {
        final StringBuffer id = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            id.append(randomString.charAt(random.nextInt(randomString.length())));
        }
        return id.toString();
    }

    public static List<DataItem> generateData(final int length) {
        final List<DataItem> data = new ArrayList<DataItem>();
        for (int i = 0; i < length; i++) {
            final DataItem item = new DataItem();
            final Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(2000 + random.nextInt(13), random.nextInt(11), random.nextInt(28), random.nextInt(24),
                    random.nextInt(60));
            item.setColumnValue(STRING_COLUMN, DataHelper.createRandomString(" abcdefgh", 8));
            item.setColumnValue(DOUBLE_COLUMN, random.nextDouble());
            final Date time = calendar.getTime();
            item.setColumnValue(DATE_COLUMN, time);
            item.setColumnValue(BOOLEAN_COLUMN, random.nextBoolean());
            item.setColumnValue(INTEGER_COLUMN, random.nextInt(100000));
            data.add(item);

        }
        return data;
    }

    public static List<ColumnInfo> getColumnInfos() {
        final List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
        final ColumnInfo e1 = new ColumnInfo(STRING_COLUMN, ColumnType.STRING, "String Column");
        e1.setFilterEnabled(true);
        e1.setWidth(150);
        e1.setSortingEnabled(true);
        columnInfos.add(e1);
        final ColumnInfo e2 = new ColumnInfo(DOUBLE_COLUMN, ColumnType.DOUBLE, "Double Column");
        e2.setFilterEnabled(true);
        e2.setSortingEnabled(true);
        e2.setWidth(250);
        columnInfos.add(e2);
        final ColumnInfo e3 = new ColumnInfo(DATE_COLUMN, ColumnType.DATE, "Date Column");
        e3.setFilterEnabled(true);
        e3.setSortingEnabled(true);
        e3.setWidth(250);
        columnInfos.add(e3);
        final ColumnInfo e4 = new ColumnInfo(BOOLEAN_COLUMN, ColumnType.BOOLEAN, "Boolean Column");
        e4.setFilterEnabled(true);
        e4.setSortingEnabled(true);
        e4.setWidth(150);
        columnInfos.add(e4);
        final ColumnInfo e5 = new ColumnInfo(INTEGER_COLUMN, ColumnType.STRING, "Integer Column");
        e5.setFilterEnabled(true);
        e5.setSortingEnabled(true);
        e5.setWidth(150);
        columnInfos.add(e5);
        //        final ColumnInfo e6 = new ColumnInfo("col6", ColumnType.DOUBLE, "Column 6");
        //        e6.setFilterEnabled(true);
        //        e6.setSortingEnabled(true);
        //        e6.setWidth(150);
        //        columnInfos.add(e6);
        return columnInfos;
    }

    public static List<DataItem> getData(final int length) {
        final List<DataItem> data = new ArrayList<DataItem>();
        for (int i = 0; i < length; i++) {
            data.add(new DataItem());
        }
        return data;
    }

    public static void populateData(final List<DataItem> data, final int length) {
        for (int i = 0; i < length; i++) {
            data.add(new DataItem());
        }
    }
}

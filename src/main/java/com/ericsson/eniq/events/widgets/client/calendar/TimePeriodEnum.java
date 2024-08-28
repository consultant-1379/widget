package com.ericsson.eniq.events.widgets.client.calendar;

public enum TimePeriodEnum {

   /* FIVE_MINUTES("5 min"),
    FIFTEEN_MINUTES("15 min"),
    THIRTY_MINUTES("30 min"),
    ONE_HOUR("1 hr"),
    TWO_HOURS("2 hrs"),
    SIX_HOURS("6 hrs"),
    TWELVE_HOURS("12 hrs"),
    ONE_DAY("1 day"),
    ONE_WEEK("1 wk"),
    TWO_WEEKS("2 wks");*/

    FIFTEEN_MINUTES("15 minutes"),
    THIRTY_MINUTES("30 minutes"),
    ONE_HOUR("1 hour"),
    TWO_HOURS("2 hours"),
    SIX_HOURS("6 hours"),
    TWELVE_HOURS("12 hours"),
    ONE_DAY("1 day"),
    ONE_WEEK("1 week");

    private static final int minutesInHour = 60;
    private static final int minutesInDay = 1440;
    private static final int daysInWeek = 7;

    TimePeriodEnum(final String timePeriodLabel) {
        this.timePeriod = timePeriodLabel;
    }

    @Override
    public String toString() {
        return timePeriod;
    }

    private final String timePeriod;

    /**
     * @param timePeriodStr
     * @return
     */
    public static TimePeriodEnum fromString(final String timePeriodStr) {
        for (final TimePeriodEnum timePeriod : TimePeriodEnum.values()) {
            if (timePeriod.toString().equals(timePeriodStr)) {
                return timePeriod;
            }
        }
        return null;
    }

    public int toMinutes() {
        switch (this) {
            case FIFTEEN_MINUTES:
                return 15;
            case THIRTY_MINUTES:
                return 30;
            case ONE_HOUR:
                return minutesInHour;
            case TWO_HOURS:
                return minutesInHour * 2;
            case SIX_HOURS:
                return 6 * minutesInHour;
            case TWELVE_HOURS:
                return 12 * minutesInHour;
            case ONE_DAY:
                return minutesInDay;
            case ONE_WEEK:
                return daysInWeek * minutesInDay;
        }

        return Integer.MAX_VALUE;
    }

    public String toFullText() {
        switch (this) {
            case FIFTEEN_MINUTES:
                return "15 Minutes";
            case THIRTY_MINUTES:
                return "30 Minutes";
            case ONE_HOUR:
                return "1 Hour";
            case TWO_HOURS:
                return "2 Hours";
            case SIX_HOURS:
                return "6 Hours";
            case TWELVE_HOURS:
                return "12 Hours";
            case ONE_DAY:
                return "1 Day";
            case ONE_WEEK:
                return "1 Week";
        }

        return null;
    }

}

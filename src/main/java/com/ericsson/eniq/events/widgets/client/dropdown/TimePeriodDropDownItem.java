package com.ericsson.eniq.events.widgets.client.dropdown;

import com.ericsson.eniq.events.common.client.time.TimePeriod;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

/**
 * 
 * @author ekurshi
 * @author eromsza
 * @since 2012
 *
 */
public class TimePeriodDropDownItem implements IDropDownItem {

    private final static DateTimeFormat labelDateFormat = DateTimeFormat.getFormat("HH:mm, yyyy-MM-dd");

    private final TimePeriod timePeriod;
    
    private final Date from;
           
    private final Date to;

    public TimePeriodDropDownItem(final TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
        this.from = null;
        this.to = null;
    }

    public TimePeriodDropDownItem(final TimePeriod timePeriod, final Date from, final Date to) {
        this.timePeriod = timePeriod;
        if (this.timePeriod.equals(TimePeriod.CUSTOM)){
            this.from = from;
            this.to = to;
        } else{
            this.from = null;
            this.to = null;
        }
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    @Override
    public String toString() {
        return (from == null || to == null) ? timePeriod.toString()
                : labelDateFormat.format(from) + " to " + labelDateFormat.format(to);
    }

    @Override
    public boolean isSeparator() {
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((timePeriod == null) ? 0 : timePeriod.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimePeriodDropDownItem other = (TimePeriodDropDownItem) obj;
        if (timePeriod != other.timePeriod)
            return false;
        return true;
    }

}

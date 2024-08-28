package com.ericsson.eniq.events.widgets.client.overlay.groups;

/**
 * @author ealeerm - Alexey Ermykin
 * @since 03 2012
 */
public class CurrentGroupIdHolder {

    private String groupId;

    public CurrentGroupIdHolder() {
    }

    public CurrentGroupIdHolder(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

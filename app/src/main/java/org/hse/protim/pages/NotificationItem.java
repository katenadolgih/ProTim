package org.hse.protim.pages;

public class NotificationItem {
    private int avatarResId;
    private String fullName;
    private String projectName;

    public NotificationItem(int avatarResId, String fullName, String projectName) {
        this.avatarResId = avatarResId;
        this.fullName = fullName;
        this.projectName = projectName;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProjectName() {
        return projectName;
    }
}


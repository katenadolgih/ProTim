package org.hse.protim.pages;

public class ProgramItem {
    public static final int TYPE_MODULE = 0;
    public static final int TYPE_SUBMODULE = 1;
    public static final int TYPE_LESSON = 2;

    public int type;
    public String text;
    public String teacher;
    public int avatarRes;
    public boolean isCompleted;

    public ProgramItem(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public ProgramItem(String title, String teacher, int avatarRes, boolean isCompleted) {
        this.type = TYPE_LESSON;
        this.text = title;
        this.teacher = teacher;
        this.avatarRes = avatarRes;
        this.isCompleted = isCompleted;
    }
}

